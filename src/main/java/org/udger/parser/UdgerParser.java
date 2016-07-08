/*
  Udger-update - Data updater for udger local and cloud parser

  author     The Udger.com Team (info@udger.com)
  copyright  Copyright (c) Udger s.r.o.
  license    GNU Lesser General Public License
  link       https://udger.com/products
*/
package org.udger.parser;

import java.io.Closeable;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sqlite.Function;

public class UdgerParser implements Closeable {

    private static final String DB_FILENAME = "udgerdb_v3.dat";

    private static final Pattern PAT_UNPERLIZE = Pattern.compile("^/?(.*?)/si$");
    private static final String ID_CRAWLER = "crawler";

    private Connection connection;

    private String dbFileName = DB_FILENAME;
    private final Map<String, Pattern> regexCache = new HashMap<>();
    private Matcher lastPatternMatcher;

    public UdgerParser() {
    }

    public UdgerParser(String dbFileName) {
        this.dbFileName = dbFileName;
    }

    public String getDbFileName() throws SQLException {
        return dbFileName;
    }

    public void setDbFileName(String dbFileName) {
        this.dbFileName = dbFileName;
    }

    @Override
    public void close() throws IOException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new IOException(e.getMessage());
        }
    }

    public UdgerUaResult parseUa(String uaString) throws SQLException {

        UdgerUaResult ret = new UdgerUaResult(uaString);

        connect();

        Integer clientId = null;
        Integer classId = null;
        PreparedStatement psCrawler = null;
        try {
            psCrawler = getFirstRow(UdgerSqlQuery.SQL_CRAWLER, uaString);
            ResultSet userAgentRs = psCrawler.executeQuery();
            if (userAgentRs != null && userAgentRs.next()) {
                fetchUserAgent(userAgentRs, ret);
                classId = 99;
                clientId = -1;
            } else {
                PreparedStatement psClient = null;
                try {
                    psClient = getFirstRow(UdgerSqlQuery.SQL_CLIENT, uaString);
                    userAgentRs = psClient.executeQuery();
                    if (userAgentRs != null && userAgentRs.next()) {
                        fetchUserAgent(userAgentRs, ret);
                        classId = ret.getClassId();
                        clientId = ret.getClientId();
                        patchVersions(ret);
                    } else {
                        ret.setUaClass("Unrecognized");
                        ret.setUaClassCode("unrecognized");
                    }
                } finally {
                    if (psClient != null) {
                        psClient.close();
                    }
                }
            }
        } finally {
            if (psCrawler != null) {
                psCrawler.close();
            }
        }

        PreparedStatement psOs = null;
        try {
            psOs = getFirstRow(UdgerSqlQuery.SQL_OS, uaString);
            ResultSet opSysRs = psOs.executeQuery();
            if (opSysRs != null && opSysRs.next()) {
                fetchOperatingSystem(opSysRs, ret);
            } else {
                if (clientId != null && clientId != 0) {
                    PreparedStatement psClientOS = null;
                    try {
                        psClientOS = getFirstRow(UdgerSqlQuery.SQL_CLIENT_OS, clientId.toString());
                        opSysRs = psClientOS.executeQuery();
                        if (opSysRs != null && opSysRs.next()) {
                            fetchOperatingSystem(opSysRs, ret);
                        }

                    } finally {
                        if (psClientOS != null) {
                            psClientOS.close();
                        }
                    }
                }
            }
        } finally {
            if (psOs != null) {
                psOs.close();
            }
        }

        PreparedStatement psDevice = null;
        try {
            psDevice = getFirstRow(UdgerSqlQuery.SQL_DEVICE, uaString);
            ResultSet devRs = psDevice.executeQuery();
            if (devRs != null && devRs.next()) {
                fetchDevice(devRs, ret);
            } else {
                if (classId != null && classId != -1) {
                    PreparedStatement psClientClass = null;
                    try {
                        psClientClass = getFirstRow(UdgerSqlQuery.SQL_CLIENT_CLASS, classId.toString());
                        devRs = psClientClass.executeQuery();
                        if (devRs != null && devRs.next()) {
                            fetchDevice(devRs, ret);
                        }

                    } finally {
                        if (psClientClass != null) {
                            psClientClass.close();
                        }
                    }
                }
            }

        } finally {
            if (psDevice != null) {
                psDevice.close();
            }
        }

        return ret;
    }

    public UdgerIpResult parseIp(String ipString) throws SQLException, UnknownHostException {
        UdgerIpResult ret = new UdgerIpResult(ipString);

        InetAddress addr = InetAddress.getByName(ipString);
        Integer ipv4int = null;
        String normalizedIp;

        if (addr instanceof Inet4Address) {
            ipv4int = 0;
            for (byte b : addr.getAddress()) {
                ipv4int = ipv4int << 8 | (b & 0xFF);
            }
            normalizedIp = addr.getHostAddress();
        } else {
            normalizedIp = addr.getHostAddress().replaceAll("((?:(?:^|:)0+\\b){2,}):?(?!\\S*\\b\\1:0+\\b)(\\S*)",
                            "::$2");
        }

        connect();

        ret.setIpClassification("Unrecognized");
        ret.setIpClassificationCode("unrecognized");

        PreparedStatement psIp = null;
        try {
            psIp = getFirstRow(UdgerSqlQuery.SQL_IP, normalizedIp);
            ResultSet ipRs = psIp.executeQuery();

            if (ipRs != null && ipRs.next()) {
                fetchUdgerIp(ipRs, ret);
                if (!ID_CRAWLER.equals(ret.getIpClassificationCode())) {
                    ret.setCrawlerFamilyInfoUrl("");
                }
            }

            if (ipv4int != null) {
                ret.setIpVer(4);
                PreparedStatement psDataCenter = null;
                try {
                    psDataCenter = getFirstRow(UdgerSqlQuery.SQL_DATACENTER, ipv4int, ipv4int);

                    ResultSet dataCenterRs = psDataCenter.executeQuery();
                    if (dataCenterRs != null && dataCenterRs.next()) {
                        fetchDataCenter(dataCenterRs, ret);
                    }
                } finally {
                    if (psDataCenter != null) {
                        psDataCenter.close();
                    }
                }

            } else {
                ret.setIpVer(6);
            }
        } finally {
            if (psIp != null) {
                psIp.close();
            }
        }
        return ret;
    }

    private void connect() throws SQLException {

        if (connection == null) {

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);

            Function.create(connection, "REGEXP", new Function() {
                @Override
                public void xFunc() throws SQLException {
                    String regex = value_text(0);
                    String param = value_text(1);
                    Pattern patRegex = regexCache.get(regex);
                    if (patRegex == null) {
                        Matcher m = PAT_UNPERLIZE.matcher(regex);
                        if (m.matches()) {
                            regex = m.group(1);
                        }
                        patRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                        regexCache.put(regex, patRegex);
                    }
                    Matcher matcher = patRegex.matcher(param);
                    boolean found = matcher.find();
                    if (found) {
                        lastPatternMatcher = matcher;
                    }
                    result(found ? 1 : 0);
                }
            });
        }
    }

    private PreparedStatement getFirstRow(String query, Object... params) throws SQLException {
        lastPatternMatcher = null;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        preparedStatement.setMaxRows(1);
        return preparedStatement;
    }

    private void fetchUserAgent(ResultSet rs, UdgerUaResult ret) throws SQLException {
        ret.setClassId(rs.getInt("class_id"));
        ret.setClientId(rs.getInt("client_id"));
        ret.setCrawlerCategory(nvl(rs.getString("crawler_category")));
        ret.setCrawlerCategoryCode(nvl(rs.getString("crawler_category_code")));
        ret.setCrawlerLastSeen(nvl(rs.getString("crawler_last_seen")));
        ret.setCrawlerRespectRobotstxt(nvl(rs.getString("crawler_respect_robotstxt")));
        ret.setUa(nvl(rs.getString("ua")));
        ret.setUaClass(nvl(rs.getString("ua_class")));
        ret.setUaClassCode(nvl(rs.getString("ua_class_code")));
        ret.setUaEngine(nvl(rs.getString("ua_engine")));
        ret.setUaFamily(nvl(rs.getString("ua_family")));
        ret.setUaFamilyCode(nvl(rs.getString("ua_family_code")));
        ret.setUaFamilyHomepage(nvl(rs.getString("ua_family_homepage")));
        ret.setUaFamilyIcon(nvl(rs.getString("ua_family_icon")));
        ret.setUaFamilyIconBig(nvl(rs.getString("ua_family_icon_big")));
        ret.setUaFamilyInfoUrl(nvl(rs.getString("ua_family_info_url")));
        ret.setUaFamilyVendor(nvl(rs.getString("ua_family_vendor")));
        ret.setUaFamilyVendorCode(nvl(rs.getString("ua_family_vendor_code")));
        ret.setUaFamilyVendorHomepage(nvl(rs.getString("ua_family_vendor_homepage")));
        ret.setUaUptodateCurrentVersion(nvl(rs.getString("ua_uptodate_current_version")));
        ret.setUaVersion(nvl(rs.getString("ua_version")));
        ret.setUaVersionMajor(nvl(rs.getString("ua_version_major")));
    }

    private void fetchOperatingSystem(ResultSet rs, UdgerUaResult ret) throws SQLException {
        ret.setOsFamily(nvl(rs.getString("os_family")));
        ret.setOs(nvl(rs.getString("os")));
        ret.setOsCode(nvl(rs.getString("os_code")));
        ret.setOsFamilyCode(nvl(rs.getString("os_family_code")));
        ret.setOsFamilyVedorHomepage(nvl(rs.getString("os_family_vedor_homepage")));
        ret.setOsFamilyVendor(nvl(rs.getString("os_family_vendor")));
        ret.setOsFamilyVendorCode(nvl(rs.getString("os_family_vendor_code")));
        ret.setOsHomePage(nvl(rs.getString("os_home_page")));
        ret.setOsIcon(nvl(rs.getString("os_icon")));
        ret.setOsIconBig(nvl(rs.getString("os_icon_big")));
        ret.setOsInfoUrl(nvl(rs.getString("os_info_url")));
    }

    private void fetchDevice(ResultSet rs, UdgerUaResult ret) throws SQLException {
        ret.setDeviceClass(nvl(rs.getString("device_class")));
        ret.setDeviceClassCode(nvl(rs.getString("device_class_code")));
        ret.setDeviceClassIcon(nvl(rs.getString("device_class_icon")));
        ret.setDeviceClassIconBig(nvl(rs.getString("device_class_icon_big")));
        ret.setDeviceClassInfoUrl(nvl(rs.getString("device_class_info_url")));
    }

    private void patchVersions(UdgerUaResult ret) {
        if (lastPatternMatcher != null) {
            String version = "";
            try {
                version = lastPatternMatcher.group(1);
            } catch (IndexOutOfBoundsException e) {
                // swallow
            }
            ret.setUaVersion(version);
            ret.setUaVersionMajor(version.split("\\.")[0]);
            ret.setUa((ret.getUa() != null ? ret.getUa() : "") + " " + version);
        } else {
            ret.setUaVersion("");
            ret.setUaVersionMajor("");
        }
    }

    private void fetchUdgerIp(ResultSet rs, UdgerIpResult ret) throws SQLException {
        ret.setCrawlerCategory(nvl(rs.getString("crawler_category")));
        ret.setCrawlerCategoryCode(nvl(rs.getString("crawler_category_code")));
        ret.setCrawlerFamily(nvl(rs.getString("crawler_family")));
        ret.setCrawlerFamilyCode(nvl(rs.getString("crawler_family_code")));
        ret.setCrawlerFamilyHomepage(nvl(rs.getString("crawler_family_homepage")));
        ret.setCrawlerFamilyIcon(nvl(rs.getString("crawler_family_icon")));
        ret.setCrawlerFamilyInfoUrl(nvl(rs.getString("crawler_family_info_url")));
        ret.setCrawlerFamilyVendor(nvl(rs.getString("crawler_family_vendor")));
        ret.setCrawlerFamilyVendorCode(nvl(rs.getString("crawler_family_vendor_code")));
        ret.setCrawlerFamilyVendorHomepage(nvl(rs.getString("crawler_family_vendor_homepage")));
        ret.setCrawlerLastSeen(nvl(rs.getString("crawler_last_seen")));
        ret.setCrawlerName(nvl(rs.getString("crawler_name")));
        ret.setCrawlerRespectRobotstxt(nvl(rs.getString("crawler_respect_robotstxt")));
        ret.setCrawlerVer(nvl(rs.getString("crawler_ver")));
        ret.setCrawlerVerMajor(nvl(rs.getString("crawler_ver_major")));
        ret.setIpCity(nvl(rs.getString("ip_city")));
        ret.setIpClassification(nvl(rs.getString("ip_classification")));
        ret.setIpClassificationCode(nvl(rs.getString("ip_classification_code")));
        ret.setIpCountry(nvl(rs.getString("ip_country")));
        ret.setIpCountryCode(nvl(rs.getString("ip_country_code")));
        ret.setIpHostname(nvl(rs.getString("ip_hostname")));
        ret.setIpLastSeen(nvl(rs.getString("ip_last_seen")));
    }

    private String nvl(String v) {
        return v != null ? v : "";
    }

    private void fetchDataCenter(ResultSet rs, UdgerIpResult ret) throws SQLException {
        ret.setDataCenterHomePage(nvl(rs.getString("datacenter_homepage")));
        ret.setDataCenterName(nvl(rs.getString("datacenter_name")));
        ret.setDataCenterNameCode(nvl(rs.getString("datacenter_name_code")));
    }
}

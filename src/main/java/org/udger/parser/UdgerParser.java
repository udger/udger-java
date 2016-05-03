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

        ResultSet userAgentRs = getFirstRow(UdgerSqlQuery.SQL_CRAWLER, uaString);
        if (userAgentRs != null && userAgentRs.next()) {
            fetchUserAgent(userAgentRs, ret);
            classId = 99;
            clientId = -1;
        } else {
            userAgentRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT, uaString);
            if (userAgentRs != null && userAgentRs.next()) {
                fetchUserAgent(userAgentRs, ret);
                classId = ret.getClassId();
                clientId = ret.getClientId();
                patchVersions(ret);
            } else {
                ret.setUaClass("Unrecognized");
                ret.setUaClassCode("unrecognized");
            }
        }

        ResultSet opSysRs = getFirstRow(UdgerSqlQuery.SQL_OS, uaString);
        if (opSysRs != null && opSysRs.next()) {
            fetchOperatingSystem(opSysRs, ret);
        } else {
            if (clientId != null && clientId != 0) {
                opSysRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT_OS, clientId.toString());
                if (opSysRs != null && opSysRs.next()) {
                    fetchOperatingSystem(opSysRs, ret);
                }
            }
        }

        ResultSet devRs = getFirstRow(UdgerSqlQuery.SQL_DEVICE, uaString);
        if (devRs != null  && devRs.next()) {
            fetchDevice(devRs, ret);
        } else {
            if (classId != null && classId != -1) {
                devRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT_CLASS, classId.toString());
                if (devRs != null && devRs.next()) {
                    fetchDevice(devRs, ret);
                }
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
            for (byte b: addr.getAddress()) {
                ipv4int = ipv4int << 8 | (b & 0xFF);
            }
            normalizedIp = addr.getHostAddress();
        } else {
            normalizedIp = addr.getHostAddress().replaceAll("((?:(?:^|:)0+\\b){2,}):?(?!\\S*\\b\\1:0+\\b)(\\S*)", "::$2");
        }

        connect();

        ret.setIpClassification("Unrecognized");
        ret.setIpClassificationCode("unrecognized");

        ResultSet ipRs = getFirstRow(UdgerSqlQuery.SQL_IP, normalizedIp);

        if (ipRs != null && ipRs.next()) {
            fetchUdgerIp(ipRs, ret);
            if (!ID_CRAWLER.equals(ret.getIpClassificationCode())) {
                ret.setCrawlerFamilyInfoUrl(null);
            }
        }

        if (ipv4int != null) {
            ret.setIpVer(4);
            ResultSet dataCenterRs = getFirstRow(UdgerSqlQuery.SQL_DATACENTER, ipv4int, ipv4int);
            if (dataCenterRs != null && dataCenterRs.next()) {
                fetchDataCenter(dataCenterRs, ret);
            }
        } else {
            ret.setIpVer(6);
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

    private ResultSet getFirstRow(String query, Object ... params) throws SQLException {
        lastPatternMatcher = null;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i=0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        preparedStatement.setMaxRows(1);
        return preparedStatement.executeQuery();
    }


    private void fetchUserAgent(ResultSet rs, UdgerUaResult ret) throws SQLException {
        ret.setClassId(rs.getInt("class_id"));
        ret.setClientId(rs.getInt("client_id"));
        ret.setCrawlerCategory(rs.getString("crawler_category"));
        ret.setCrawlerCategoryCode(rs.getString("crawler_category_code"));
        ret.setCrawlerLastSeen(rs.getString("crawler_last_seen"));
        ret.setCrawlerRespectRobotstxt(rs.getString("crawler_respect_robotstxt"));
        ret.setUa(rs.getString("ua"));
        ret.setUaClass(rs.getString("ua_class"));
        ret.setUaClassCode(rs.getString("ua_class_code"));
        ret.setUaEngine(rs.getString("ua_engine"));
        ret.setUaFamily(rs.getString("ua_family"));
        ret.setUaFamilyCode(rs.getString("ua_family_code"));
        ret.setUaFamilyHomepage(rs.getString("ua_family_homepage"));
        ret.setUaFamilyIcon(rs.getString("ua_family_icon"));
        ret.setUaFamilyIconBig(rs.getString("ua_family_icon_big"));
        ret.setUaFamilyInfoUrl(rs.getString("ua_family_info_url"));
        ret.setUaFamilyVendor(rs.getString("ua_family_vendor"));
        ret.setUaFamilyVendorCode(rs.getString("ua_family_vendor_code"));
        ret.setUaFamilyVendorHomepage(rs.getString("ua_family_vendor_homepage"));
        ret.setUaUptodateCurrentVersion(rs.getString("ua_uptodate_current_version"));
        ret.setUaVersion(rs.getString("ua_version"));
        ret.setUaVersionMajor(rs.getString("ua_version_major"));
    }

    private void fetchOperatingSystem(ResultSet rs, UdgerUaResult ret) throws SQLException {
        ret.setOsFamily(rs.getString("os_family"));
        ret.setOs(rs.getString("os"));
        ret.setOsCode(rs.getString("os_code"));
        ret.setOsFamilyCode(rs.getString("os_family_code"));
        ret.setOsFamilyVedorHomepage(rs.getString("os_family_vedor_homepage"));
        ret.setOsFamilyVendor(rs.getString("os_family_vendor"));
        ret.setOsFamilyVendorCode(rs.getString("os_family_vendor_code"));
        ret.setOsHomePage(rs.getString("os_home_page"));
        ret.setOsIcon(rs.getString("os_icon"));
        ret.setOsIconBig(rs.getString("os_icon_big"));
        ret.setOsInfoUrl(rs.getString("os_info_url"));
    }

    private void fetchDevice(ResultSet rs, UdgerUaResult ret) throws SQLException {
        ret.setDeviceClass(rs.getString("device_class"));
        ret.setDeviceClassCode(rs.getString("device_class_code"));
        ret.setDeviceClassIcon(rs.getString("device_class_icon"));
        ret.setDeviceClassIconBig(rs.getString("device_class_icon_big"));
        ret.setDeviceClassInfoUrl(rs.getString("device_class_info_url"));
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
            ret.setUaVersion(null);
            ret.setUaVersionMajor(null);
        }
    }

    private void fetchUdgerIp(ResultSet rs, UdgerIpResult ret) throws SQLException {
        ret.setCrawlerCategory(rs.getString("crawler_category"));
        ret.setCrawlerCategoryCode(rs.getString("crawler_category_code"));
        ret.setCrawlerFamily(rs.getString("crawler_family"));
        ret.setCrawlerFamilyCode(rs.getString("crawler_family_code"));
        ret.setCrawlerFamilyHomepage(rs.getString("crawler_family_homepage"));
        ret.setCrawlerFamilyIcon(rs.getString("crawler_family_icon"));
        ret.setCrawlerFamilyInfoUrl(rs.getString("crawler_family_info_url"));
        ret.setCrawlerFamilyVendor(rs.getString("crawler_family_vendor"));
        ret.setCrawlerFamilyVendorCode(rs.getString("crawler_family_vendor_code"));
        ret.setCrawlerFamilyVendorHomepage(rs.getString("crawler_family_vendor_homepage"));
        ret.setCrawlerLastSeen(rs.getString("crawler_last_seen"));
        ret.setCrawlerName(rs.getString("crawler_name"));
        ret.setCrawlerRespectRobotstxt(rs.getString("crawler_respect_robotstxt"));
        ret.setCrawlerVer(rs.getString("crawler_ver"));
        ret.setCrawlerVerMajor(rs.getString("crawler_ver_major"));
        ret.setIpCity(rs.getString("ip_city"));
        ret.setIpClassification(rs.getString("ip_classification"));
        ret.setIpClassificationCode(rs.getString("ip_classification_code"));
        ret.setIpCountry(rs.getString("ip_country"));
        ret.setIpCountryCode(rs.getString("ip_country_code"));
        ret.setIpHostname(rs.getString("ip_hostname"));
        ret.setIpLastSeen(rs.getString("ip_last_seen"));
    }

    private void fetchDataCenter(ResultSet rs, UdgerIpResult ret) throws SQLException {
        ret.setDataCenterHomePage(rs.getString("datacenter_homepage"));
        ret.setDataCenterName(rs.getString("datacenter_name"));
        ret.setDataCenterNameCode(rs.getString("datacenter_name_code"));
    }
}

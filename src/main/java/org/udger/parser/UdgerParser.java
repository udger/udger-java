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

    public UdgerUaQueryResult parseUa(String uaQuery) throws SQLException {

        UdgerUaQueryResult ret = null;

        connect();

        Integer clientId = null;
        Integer classId = null;

        UdgerUa userAgent = null;
        ResultSet userAgentRs = getFirstRow(UdgerSqlQuery.SQL_CRAWLER, uaQuery);
        if (userAgentRs != null && userAgentRs.next()) {
            userAgent = fetchUserAgent(userAgentRs);
            classId = 99;
            clientId = -1;
        } else {
            userAgentRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT, uaQuery);
            if (userAgentRs != null && userAgentRs.next()) {
                userAgent = fetchUserAgent(userAgentRs);
                classId = userAgent.getClassId();
                clientId = userAgent.getClientId();
                patchVersions(userAgent);
            }
        }

        if (userAgent != null) {
            if (ret == null) {
                ret = new UdgerUaQueryResult();
            }
            ret.setUserAgent(userAgent);
        }

        UdgerOs opSys = null;
        ResultSet opSysRs = getFirstRow(UdgerSqlQuery.SQL_OS, uaQuery);
        if (opSysRs != null && opSysRs.next()) {
            opSys = fetchOperatingSystem(opSysRs);
        } else {
            if (clientId != null && clientId != 0) {
                opSysRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT_OS, clientId.toString());
                if (opSysRs != null && opSysRs.next()) {
                    opSys = fetchOperatingSystem(opSysRs);
                }
            }
        }

        if (opSys != null) {
            if (ret == null) {
                ret = new UdgerUaQueryResult();
            }
            ret.setOs(opSys);
        }

        UdgerDevice device = null;
        ResultSet devRs = getFirstRow(UdgerSqlQuery.SQL_DEVICE, uaQuery);
        if (devRs != null  && devRs.next()) {
            device = fetchDevice(devRs);
        } else {
            if (classId != null && classId != -1) {
                devRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT_CLASS, classId.toString());
                if (devRs != null && devRs.next()) {
                    device = fetchDevice(devRs);
                }
            }
        }

        if (device != null) {
            if (ret == null) {
                ret = new UdgerUaQueryResult();
            }
            ret.setDevice(device);
        }

        return ret;
    }


    public UdgerIpQueryResult parseIp(String ipQuery) throws SQLException, UnknownHostException {

        UdgerIpQueryResult ret = null;

        InetAddress addr = InetAddress.getByName(ipQuery);
        Integer ipv4int = null;
        if (addr instanceof Inet4Address) {
            ipv4int = 0;
            for (byte b: addr.getAddress()) {
                ipv4int = ipv4int << 8 | (b & 0xFF);
            }
        }

        connect();

        UdgerIp udgerIp = new UdgerIp();
        udgerIp.setIpClassification("Unrecognized");
        udgerIp.setIpClassificationCode("unrecognized");

        ret = new UdgerIpQueryResult();
        ret.setIp(udgerIp);

        ResultSet ipRs = getFirstRow(UdgerSqlQuery.SQL_IP, addr.getHostAddress());

        if (ipRs != null && ipRs.next()) {
            loadUdgerIp(ipRs, udgerIp);
            if (!ID_CRAWLER.equals(udgerIp.getIpClassificationCode())) {
                udgerIp.setCrawlerFamilyInfoUrl(null);
            }
        }

        if (ipv4int != null) {
            udgerIp.setIpVer(4);
            ResultSet dataCenterRs = getFirstRow(UdgerSqlQuery.SQL_DATACENTER, ipv4int, ipv4int);
            if (dataCenterRs != null && dataCenterRs.next()) {
                UdgerDataCenter dc = fetchDataCenter(dataCenterRs);
                ret.setDataCenter(dc);
            }
        } else {
            udgerIp.setIpVer(6);
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


    private UdgerUa fetchUserAgent(ResultSet rs) throws SQLException {
        UdgerUa ret = new UdgerUa();
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
        return ret;
    }

    private UdgerOs fetchOperatingSystem(ResultSet rs) throws SQLException {
        UdgerOs ret = new UdgerOs();
        ret.setFamily(rs.getString("family"));
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
        return ret;
    }

    private UdgerDevice fetchDevice(ResultSet rs) throws SQLException {
        UdgerDevice ret = new UdgerDevice();
        ret.setDeviceClass(rs.getString("device_class"));
        ret.setDeviceClassCode(rs.getString("device_class_code"));
        ret.setDeviceClassIcon(rs.getString("device_class_icon"));
        ret.setDeviceClassIconBig(rs.getString("device_class_icon_big"));
        ret.setDeviceClassInfoUrl(rs.getString("device_class_info_url"));
        return ret;
    }

    private void patchVersions(UdgerUa userAgent) {
        if (lastPatternMatcher != null) {
            String version = "";
            try {
                version = lastPatternMatcher.group(1);
            } catch (IndexOutOfBoundsException e) {
                // swallow
            }
            userAgent.setUaVersion(version);
            userAgent.setUaVersionMajor(version.split("\\.")[0]);
            userAgent.setUa((userAgent.getUa() != null ? userAgent.getUa() : "") + " " + version);
        } else {
            userAgent.setUaVersion(null);
            userAgent.setUaVersionMajor(null);
        }
    }

    private void loadUdgerIp(ResultSet rs, UdgerIp udgerIp) throws SQLException {
        udgerIp.setCrawlerCategory(rs.getString("crawler_category"));
        udgerIp.setCrawlerCategoryCode(rs.getString("crawler_category_code"));
        udgerIp.setCrawlerFamily(rs.getString("crawler_family"));
        udgerIp.setCrawlerFamilyCode(rs.getString("crawler_family_code"));
        udgerIp.setCrawlerFamilyHomepage(rs.getString("crawler_family_homepage"));
        udgerIp.setCrawlerFamilyIcon(rs.getString("crawler_family_icon"));
        udgerIp.setCrawlerFamilyInfoUrl(rs.getString("crawler_family_info_url"));
        udgerIp.setCrawlerFamilyVendor(rs.getString("crawler_family_vendor"));
        udgerIp.setCrawlerFamilyVendorCode(rs.getString("crawler_family_vendor_code"));
        udgerIp.setCrawlerFamilyVendorHomepage(rs.getString("crawler_family_vendor_homepage"));
        udgerIp.setCrawlerLastSeen(rs.getString("crawler_last_seen"));
        udgerIp.setCrawlerName(rs.getString("crawler_name"));
        udgerIp.setCrawlerRespectRobotstxt(rs.getString("crawler_respect_robotstxt"));
        udgerIp.setCrawlerVer(rs.getString("crawler_ver"));
        udgerIp.setCrawlerVerMajor(rs.getString("crawler_ver_major"));
        udgerIp.setIpCity(rs.getString("ip_city"));
        udgerIp.setIpClassification(rs.getString("ip_classification"));
        udgerIp.setIpClassificationCode(rs.getString("ip_classification_code"));
        udgerIp.setIpCountry(rs.getString("ip_country"));
        udgerIp.setIpCountryCode(rs.getString("ip_country_code"));
        udgerIp.setIpHostname(rs.getString("ip_hostname"));
        udgerIp.setIpLastSeen(rs.getString("ip_last_seen"));
    }

    private UdgerDataCenter fetchDataCenter(ResultSet rs) throws SQLException {
        UdgerDataCenter ret = new UdgerDataCenter();
        ret.setDataCenterHomePage(rs.getString("datacenter_homepage"));
        ret.setDataCenterName(rs.getString("datacenter_name"));
        ret.setDataCenterNameCode(rs.getString("datacenter_name_code"));
        return ret;
    }
}

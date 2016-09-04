/*
  UdgerParser - Java agent string parser based on Udger https://udger.com/products/local_parser

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sqlite.Function;

public class UdgerParser implements Closeable {

    private static final String UDGER_UA_DEV_BRAND_LIST_URL = "https://udger.com/resources/ua-list/devices-brand-detail?brand=";

    private static final String DB_FILENAME = "udgerdb_v3.dat";

    private static final Pattern PAT_UNPERLIZE = Pattern.compile("^/?(.*?)/si$");
    private static final String ID_CRAWLER = "crawler";

    private static WordDetector clientWordDetector;
    private static WordDetector deviceWordDetector;
    private static WordDetector osWordDetector;

    private static class ClientInfo {
        private Integer clientId;
        private Integer classId;
    }

    public PerformanceData perfData = new PerformanceData();

    private Connection connection;

    private String dbFileName = DB_FILENAME;
    private final Map<String, Pattern> regexCache = new HashMap<>();
    private Matcher lastPatternMatcher;
    private int regexCount = 0;

    private final ArrayList<PreparedStatement> sqlClientPrepStmtList = new ArrayList<>();
    private final ArrayList<PreparedStatement> sqlDevicePrepStmtList = new ArrayList<>();
    private final ArrayList<PreparedStatement> sqlOsPrepStmtList = new ArrayList<>();

    private PreparedStatement sqlCrawlerPreparedStatement;
    private PreparedStatement sqlClientOsPreparedStatement;
    private PreparedStatement sqlClientClassPreparedStatement;
    private PreparedStatement sqlIpPreparedStatement;
    private PreparedStatement sqlDataCenterPreparedStatement;

    public UdgerParser() {
    }

    public void prepareParser() throws SQLException {
        connect();
        initStaticStructures(connection);
    }

    private static synchronized void initStaticStructures(Connection connection) throws SQLException {
        if (clientWordDetector == null) {
            clientWordDetector = createWordDetector(connection, "udger_client_regex", "udger_client_regex_words");
            deviceWordDetector = createWordDetector(connection, "udger_deviceclass_regex", "udger_deviceclass_regex_words");
            osWordDetector = createWordDetector(connection, "udger_os_regex", "udger_os_regex_words");
        }
    }

    private static WordDetector createWordDetector(Connection connection, String regexTableName, String wordTableName) throws SQLException {

        Set<Integer> usedWords = new HashSet<>();

        addUsedWords(usedWords, connection, regexTableName, "word_id");
        addUsedWords(usedWords, connection, regexTableName, "word2_id");

        WordDetector result = new WordDetector();

        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM " + wordTableName);
        if (rs != null) {
            while (rs.next()) {
                int id = rs.getInt("id");
                if (usedWords.contains(id)) {
                    String word = rs.getString("word").toLowerCase();
                    result.addWord(id, word);
                }
            }
        }
        return result;
    }

    private static void addUsedWords(Set<Integer> usedWords, Connection connection, String regexTableName, String wordIdColumn) throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT " + wordIdColumn + " FROM " + regexTableName);
        if (rs != null) {
            while (rs.next()) {
                usedWords.add(rs.getInt(wordIdColumn));
            }
        }
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
            if (sqlCrawlerPreparedStatement != null) {
                sqlCrawlerPreparedStatement.close();
                sqlCrawlerPreparedStatement = null;
            }
            if (sqlClientOsPreparedStatement != null) {
                sqlClientOsPreparedStatement.close();
                sqlClientOsPreparedStatement = null;
            }
            if (sqlClientClassPreparedStatement != null) {
                sqlClientClassPreparedStatement.close();
                sqlClientClassPreparedStatement = null;
            }
            if (sqlIpPreparedStatement != null) {
                sqlIpPreparedStatement.close();
                sqlIpPreparedStatement = null;
            }
            if (sqlDataCenterPreparedStatement != null) {
                sqlDataCenterPreparedStatement.close();
                sqlDataCenterPreparedStatement = null;
            }
            closeStatementList(sqlClientPrepStmtList);
            closeStatementList(sqlDevicePrepStmtList);
            closeStatementList(sqlOsPrepStmtList);
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new IOException(e.getMessage());
        }
    }

    private void closeStatementList(ArrayList<PreparedStatement> stmtList) throws SQLException {
        for (int i = 0; i < stmtList.size(); i++) {
            PreparedStatement stmt = stmtList.get(i);
            if (stmt != null) {
                stmt.close();
                stmtList.set(i, null);
            }
        }
    }

    public UdgerUaResult parseUa(String uaString) throws SQLException {

        UdgerUaResult ret = new UdgerUaResult(uaString);

        connect();

        perfData.incCallCount();

        ClientInfo clientInfo = clientDetector(uaString, ret);

        osDetector(uaString, ret, clientInfo);

        deviceDetector(uaString, ret, clientInfo);

//        if (ret.getOsFamilyCode() != null && !ret.getOsFamilyCode().isEmpty()) {
//            fetchDeviceBrand(uaString, ret);
//        }

        return ret;
    }


    private ClientInfo clientDetector(String uaString, UdgerUaResult ret) throws SQLException {

        ClientInfo result = new ClientInfo();

        if (sqlCrawlerPreparedStatement == null) {
            sqlCrawlerPreparedStatement = connection.prepareStatement(UdgerSqlQuery.SQL_CRAWLER);
        }

        // Client
        ResultSet userAgentRs = getFirstRow("sqlCrawler", sqlCrawlerPreparedStatement, uaString);
        if (userAgentRs != null && userAgentRs.next()) {
            fetchUserAgent(userAgentRs, ret);
            result.classId = 99;
            result.clientId = -1;
        } else {
            List<Integer> foundClientWords = clientWordDetector.findWords(perfData, uaString);
            boolean clientFound = false;
            if (foundClientWords.size() > 0) {
                PreparedStatement sqlClientPreparedStatement = getPreparedStatement(UdgerSqlQuery.SQL_CLIENT_ENHANCED, foundClientWords, sqlClientPrepStmtList);
                userAgentRs = getFirstRowEnhanced("sqlClient", sqlClientPreparedStatement, foundClientWords, foundClientWords, uaString);
                if (userAgentRs != null && userAgentRs.next()) {
                    fetchUserAgent(userAgentRs, ret);
                    result.classId = ret.getClassId();
                    result.clientId = ret.getClientId();
                    patchVersions(ret);
                    clientFound = true;
                }
            }
            if (!clientFound) {
                ret.setUaClass("Unrecognized");
                ret.setUaClassCode("unrecognized");
            }
        }
        return result;
    }

    private void osDetector(String uaString, UdgerUaResult ret, ClientInfo clientInfo) throws SQLException {
        List<Integer> foundOsWords = osWordDetector.findWords(perfData, uaString);
        boolean osFound = false;
        if (foundOsWords.size() > 0) {
            PreparedStatement sqlOsPreparedStatement = getPreparedStatement(UdgerSqlQuery.SQL_OS_ENHANCED, foundOsWords, sqlOsPrepStmtList);
            ResultSet opSysRs = getFirstRowEnhanced("sqlOs", sqlOsPreparedStatement, foundOsWords, foundOsWords, uaString);
            if (opSysRs != null && opSysRs.next()) {
                fetchOperatingSystem(opSysRs, ret);
                osFound = true;
            }
        }

        if (!osFound) {
            if (clientInfo.clientId != null && clientInfo.clientId != 0) {
                if (sqlClientOsPreparedStatement == null) {
                    sqlClientOsPreparedStatement = connection.prepareStatement(UdgerSqlQuery.SQL_CLIENT_OS);
                }
                ResultSet opSysRs = getFirstRow("sqlClientOs", sqlClientOsPreparedStatement, clientInfo.clientId.toString());
                if (opSysRs != null && opSysRs.next()) {
                    fetchOperatingSystem(opSysRs, ret);
                }
            }
        }

    }

    private void deviceDetector(String uaString, UdgerUaResult ret, ClientInfo clientInfo) throws SQLException {
        List<Integer> foundDeviceWords = deviceWordDetector.findWords(perfData, uaString);
        boolean deviceFound = false;
        if (foundDeviceWords.size() > 0) {
            PreparedStatement sqlDevicePreparedStatement = getPreparedStatement(UdgerSqlQuery.SQL_DEVICE_ENHANCED, foundDeviceWords, sqlDevicePrepStmtList);
            ResultSet devRs = getFirstRowEnhanced("sqlDevice", sqlDevicePreparedStatement, foundDeviceWords, foundDeviceWords, uaString);
            if (devRs != null  && devRs.next()) {
                fetchDevice(devRs, ret);
                deviceFound = true;
            }
        }

        if (!deviceFound) {
          if (clientInfo.classId != null && clientInfo.classId != -1) {
              if (sqlClientClassPreparedStatement == null) {
                  sqlClientClassPreparedStatement = connection.prepareStatement(UdgerSqlQuery.SQL_CLIENT_CLASS);
              }
              ResultSet devRs = getFirstRow("sqlClientClass", sqlClientClassPreparedStatement, clientInfo.classId.toString());
              if (devRs != null && devRs.next()) {
                  fetchDevice(devRs, ret);
              }
          }
        }
    }

    private PreparedStatement getPreparedStatement(String enhancedSql, List<Integer> foundWords,
            ArrayList<PreparedStatement> prepStmtList) throws SQLException {
        int wordCount = foundWords.size();

        while (wordCount > prepStmtList.size()) {
            prepStmtList.add(null);
        }

        PreparedStatement result = prepStmtList.get(wordCount-1);
        if (result == null) {
            StringBuilder paramList = new StringBuilder();
            for (int i = 0; i < wordCount; i++) {
                paramList.append(",?");
            }
            String param = paramList.toString().substring(1);
            String sql = String.format(enhancedSql, param, param);
            result = connection.prepareStatement(sql);
            prepStmtList.set(wordCount-1, result);
        }
        return result;
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

        if (sqlIpPreparedStatement == null) {
            sqlIpPreparedStatement = connection.prepareStatement(UdgerSqlQuery.SQL_IP);
        }
        ResultSet ipRs = getFirstRow("sqlIp", sqlIpPreparedStatement, normalizedIp);

        if (ipRs != null && ipRs.next()) {
            fetchUdgerIp(ipRs, ret);
            if (!ID_CRAWLER.equals(ret.getIpClassificationCode())) {
                ret.setCrawlerFamilyInfoUrl("");
            }
        }

        if (ipv4int != null) {
            ret.setIpVer(4);
            if (sqlDataCenterPreparedStatement == null) {
                sqlDataCenterPreparedStatement = connection.prepareStatement(UdgerSqlQuery.SQL_DATACENTER);
            }
            ResultSet dataCenterRs = getFirstRow("sqlDataCenter", sqlDataCenterPreparedStatement, ipv4int, ipv4int);
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
                    Pattern patRegex = getRegexFromCache(regex);
                    regexCount ++;
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

    private Pattern getRegexFromCache(String regex) {
        Pattern patRegex = regexCache.get(regex);
        if (patRegex == null) {
            String oldRegex = regex;
            Matcher m = PAT_UNPERLIZE.matcher(regex);
            if (m.matches()) {
                regex = m.group(1);
            }
            patRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            regexCache.put(oldRegex, patRegex);
        }
        return patRegex;
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

    private ResultSet getFirstRowEnhanced(String sqlId, PreparedStatement preparedStatement, Object ... params) throws SQLException {
        int paramIndex = 0;
        for (int i=0; i < params.length; i++) {
            if (params[i] instanceof List) {
                List<?> l = (List<?>) params[i];
                for (Object listParam: l) {
                    preparedStatement.setObject(paramIndex + 1, listParam);
                    paramIndex ++;
                }
            } else {
                preparedStatement.setObject(paramIndex + 1, params[i]);
                paramIndex ++;
            }
        }
        preparedStatement.setMaxRows(1);
        long tm1 = System.nanoTime();
        ResultSet result = preparedStatement.executeQuery();
        long tm2 = System.nanoTime();
        perfData.addTime(sqlId, tm2 - tm1);
        perfData.incCount(sqlId);
        return result;
    }

    private ResultSet getFirstRow(String sqlId, PreparedStatement preparedStatement, Object ... params) throws SQLException {
        long tm1 = System.nanoTime();
        for (int i=0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        preparedStatement.setMaxRows(1);
        ResultSet result = preparedStatement.executeQuery();
        long tm2 = System.nanoTime();
        perfData.addTime(sqlId, tm2 - tm1);
        perfData.incCount(sqlId);
        return result;
    }

    private void fetchDeviceBrand(String uaString, UdgerUaResult ret) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UdgerSqlQuery.SQL_DEVICE_REGEX);
        preparedStatement.setObject(1, ret.getOsFamilyCode());
        preparedStatement.setObject(2, ret.getOsCode());
        ResultSet devRegexRs  = preparedStatement.executeQuery();
        if (devRegexRs != null) {
            while (devRegexRs.next()) {
                String devId = devRegexRs.getString("id");
                String regex = devRegexRs.getString("regstring");
                if (devId != null && regex != null) {
                    Pattern patRegex = getRegexFromCache(regex);
                    Matcher matcher = patRegex.matcher(uaString);
                    if (matcher.matches()) {
                        ResultSet devNameListRs = getFirstRow(UdgerSqlQuery.SQL_DEVICE_NAME_LIST, devId, matcher.group(1));
                        if (devNameListRs != null && devNameListRs.next()) {
                            ret.setDeviceMarketname(devNameListRs.getString("marketname"));
                            ret.setDeviceBrand(devNameListRs.getString("brand"));
                            ret.setDeviceBrandCode(devNameListRs.getString("brand_code"));
                            ret.setDeviceBrandHomepage(devNameListRs.getString("brand_url"));
                            ret.setDeviceBrandIcon(devNameListRs.getString("icon"));
                            ret.setDeviceBrandIconBig(devNameListRs.getString("icon_big"));
                            ret.setDeviceBrandInfoUrl(UDGER_UA_DEV_BRAND_LIST_URL + devNameListRs.getString("brand_code"));
                            break;
                        }
                    }
                }
            }
        }
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
            if (lastPatternMatcher.groupCount() > 1) {
                version = lastPatternMatcher.group(1);
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


    public void printPerformanceData() {
        perfData.print(regexCount, regexCache.size());
    }
}

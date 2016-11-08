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
import java.net.Inet6Address;
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

/**
 * Main parser's class handles parser requests for user agent or IP.
 */
public class UdgerParser implements Closeable {

    private static final String DB_FILENAME = "udgerdb_v3.dat";
    private static final String UDGER_UA_DEV_BRAND_LIST_URL = "https://udger.com/resources/ua-list/devices-brand-detail?brand=";
    private static final String ID_CRAWLER = "crawler";
    private static final Pattern PAT_UNPERLIZE = Pattern.compile("^/?(.*?)/si$");

    private static class ClientInfo {
        private Integer clientId;
        private Integer classId;
    }

    private static class IdRegString {
        int id;
        int wordId1;
        int wordId2;
        Pattern pattern;
    }

    private static WordDetector clientWordDetector;
    private static WordDetector deviceWordDetector;
    private static WordDetector osWordDetector;

    private static List<IdRegString> clientRegstringList;
    private static List<IdRegString> osRegstringList;
    private static List<IdRegString> deviceRegstringList;

    private Connection connection;

    private String dbFileName = DB_FILENAME;
    private final Map<String, Pattern> regexCache = new HashMap<>();
    private Matcher lastPatternMatcher;

    private Map<String, PreparedStatement> preparedStmtMap = new HashMap<>();

    private LRUCache cache;

    private boolean osParserEnabled = true;
    private boolean deviceParserEnabled = true;
    private boolean deviceBrandParserEnabled = true;

    /**
     * Instantiates a new udger parser with LRU cache with capacity of 10.000 items
     *
     * @param dbFileName the udger database file name
     */
    public UdgerParser(String dbFileName) {
        this(dbFileName, 10000);
    }

    /**
     * Instantiates a new udger parser. Parser must be prepared by prepare() method call before it is used.
     *
     * @param dbFileName the udger database file name
     * @param cacheCapacity the LRU cache capacity
     */
    public UdgerParser(String dbFileName, int cacheCapacity) {
        this.dbFileName = dbFileName;
        if (cacheCapacity > 0) {
            cache = new LRUCache(cacheCapacity);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            for (PreparedStatement preparedStmt: preparedStmtMap.values()) {
                preparedStmt.close();
            }
            preparedStmtMap.clear();
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * Parses the user agent string and stores results of parsing in UdgerUaResult
     *
     * @param uaString the user agent string
     * @return the intance of UdgerUaResult storing results of parsing
     * @throws SQLException the SQL exception
     */
    public UdgerUaResult parseUa(String uaString) throws SQLException {

        UdgerUaResult ret;

        if (cache != null) {
            ret = cache.get(uaString);
            if (ret != null) {
                return ret;
            }
        }

        ret = new UdgerUaResult(uaString);

        prepare();

        ClientInfo clientInfo = clientDetector(uaString, ret);

        if (osParserEnabled) {
            osDetector(uaString, ret, clientInfo);
        }

        if (deviceParserEnabled) {
            deviceDetector(uaString, ret, clientInfo);
        }

        if (deviceBrandParserEnabled) {
            if (ret.getOsFamilyCode() != null && !ret.getOsFamilyCode().isEmpty()) {
                fetchDeviceBrand(uaString, ret);
            }
        }

        if (cache != null) {
            cache.put(uaString, ret);
        }

        return ret;
    }

    /**
     * Parses the IP string and stores results of parsing in UdgerIpResult.
     *
     * @param ipString the IP string
     * @return the instance of UdgerIpResult storing results of parsing
     * @throws SQLException the SQL exception
     * @throws UnknownHostException the unknown host exception
     */
    public UdgerIpResult parseIp(String ipString) throws SQLException, UnknownHostException {

        UdgerIpResult ret = new UdgerIpResult(ipString);

        InetAddress addr = InetAddress.getByName(ipString);
        Integer ipv4int = null;
        String normalizedIp = null;

        if (addr instanceof Inet4Address) {
            ipv4int = 0;
            for (byte b: addr.getAddress()) {
                ipv4int = ipv4int << 8 | (b & 0xFF);
            }
            normalizedIp = addr.getHostAddress();
        } else if (addr instanceof Inet6Address) {
            normalizedIp = addr.getHostAddress().replaceAll("((?:(?:^|:)0+\\b){2,}):?(?!\\S*\\b\\1:0+\\b)(\\S*)", "::$2");
        }

        ret.setIpClassification("Unrecognized");
        ret.setIpClassificationCode("unrecognized");

        if (normalizedIp != null) {

            prepare();

            ResultSet ipRs = getFirstRow(UdgerSqlQuery.SQL_IP, normalizedIp);

            if (ipRs != null && ipRs.next()) {
                fetchUdgerIp(ipRs, ret);
                if (!ID_CRAWLER.equals(ret.getIpClassificationCode())) {
                    ret.setCrawlerFamilyInfoUrl("");
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
                int[] ipArray = ip6ToArray((Inet6Address) addr);
                ResultSet dataCenterRs = getFirstRow(UdgerSqlQuery.SQL_DATACENTER_RANGE6,
                        ipArray[0], ipArray[0],
                        ipArray[1], ipArray[1],
                        ipArray[2], ipArray[2],
                        ipArray[3], ipArray[3],
                        ipArray[4], ipArray[4],
                        ipArray[5], ipArray[5],
                        ipArray[6], ipArray[6],
                        ipArray[7], ipArray[7]
                        );
                if (dataCenterRs != null && dataCenterRs.next()) {
                    fetchDataCenter(dataCenterRs, ret);
                }
            }
        }

        return ret;
    }

    /**
     * Checks if is OS parser enabled. OS parser is enabled by default
     *
     * @return true, if is OS parser enabled
     */
    public boolean isOsParserEnabled() {
        return osParserEnabled;
    }

    /**
     * Enable/disable the OS parser. OS parser is enabled by default. If enabled following fields
     * of UdgerUaResult are processed by the OS parser:
     *    <ul>
     * 	    <li>osFamily, osFamilyCode, OS, osCode, osHomePage, osIcon, osIconBig</li>
     * 	    <li>osFamilyVendor, osFamilyVendorCode, osFamilyVedorHomepage, osInfoUrl</li>
     *    </ul>
     *
     *  If the OSs fields are not necessary then disabling this feature can increase
     *  the parser's performance.
     *
     * @param osParserEnabled the true if os parser is to be enabled
     */
    public void setOsParserEnabled(boolean osParserEnabled) {
        this.osParserEnabled = osParserEnabled;
    }

    /**
     * Checks if is device parser enabled. Device parser is enabled by default
     *
     * @return true, if device parser is enabled
     */
    public boolean isDeviceParserEnabled() {
        return deviceParserEnabled;
    }

    /**
     * Enable/disable the device parser. Device parser is enabled by default. If enabled following fields
     *  of UdgerUaResult are filled by the device parser:
     *    <ul>
     * 	    <li>deviceClass, deviceClassCode, deviceClassIcon</li>
     * 	    <li>deviceClassIconBig, deviceClassInfoUrl</li>
     *    </ul>
     *
     *  If the DEVICEs fields are not necessary then disabling this feature can increase
     *  the parser's performance.
     *
     * @param deviceParserEnabled the true if device parser is to be enabled
     */
    public void setDeviceParserEnabled(boolean deviceParserEnabled) {
        this.deviceParserEnabled = deviceParserEnabled;
    }

    /**
     * Checks if is device brand parser enabled. Device brand parser is enabled by default.
     *
     * @return true, if device brand parser is enabled
     */
    public boolean isDeviceBrandParserEnabled() {
        return deviceBrandParserEnabled;
    }

    /**
     * Enable/disable the device brand parser. Device brand parser is enabled by default. If enabled following fields
     *  of UdgerUaResult are filled by the device brand parser:
     *    <ul>
     * 	    <li>deviceMarketname, deviceBrand, deviceBrandCode, deviceBrandHomepage</li>
     * 	    <li>deviceBrandIcon, deviceBrandIconBig, deviceBrandInfoUrl</li>
     *    </ul>
     *
     *  If the BRANDs fields are not necessary then disabling this feature can increase
     *  the parser's performance.
     *
     * @param deviceBrandParserEnabled the true if device brand parser is to be enabled
     */
    public void setDeviceBrandParserEnabled(boolean deviceBrandParserEnabled) {
        this.deviceBrandParserEnabled = deviceBrandParserEnabled;
    }

    private static synchronized void initStaticStructures(Connection connection) throws SQLException {
        if (clientRegstringList == null) {

            clientRegstringList = prepareRegexpStruct(connection, "udger_client_regex");
            osRegstringList = prepareRegexpStruct(connection, "udger_os_regex");
            deviceRegstringList = prepareRegexpStruct(connection, "udger_deviceclass_regex");

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

    private int findIdFromList(String uaString, Set<Integer> foundClientWords, List<IdRegString> list) {
        for (IdRegString irs: list) {
            if ((irs.wordId1 == 0 || foundClientWords.contains(irs.wordId1)) &&
                (irs.wordId2 == 0 || foundClientWords.contains(irs.wordId2))) {
                Matcher matcher = irs.pattern.matcher(uaString);
                if (matcher.find()) {
                    lastPatternMatcher = matcher;
                    return irs.id;
                }
            }
        }
        return -1;
    }

    private static List<IdRegString> prepareRegexpStruct(Connection connection, String regexpTableName) throws SQLException {
        List<IdRegString> ret = new ArrayList<>();
        ResultSet rs = connection.createStatement().executeQuery("SELECT rowid, regstring, word_id, word2_id FROM " + regexpTableName + " ORDER BY sequence");
        if (rs != null) {
            while (rs.next()) {
                IdRegString irs = new IdRegString();
                irs.id = rs.getInt("rowid");
                irs.wordId1 = rs.getInt("word_id");
                irs.wordId2 = rs.getInt("word2_id");
                String regex = rs.getString("regstring");
                Matcher m = PAT_UNPERLIZE.matcher(regex);
                if (m.matches()) {
                    regex = m.group(1);
                }
                irs.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                ret.add(irs);
            }
        }
        return ret;
    }


    private ClientInfo clientDetector(String uaString, UdgerUaResult ret) throws SQLException {
        ClientInfo clientInfo = new ClientInfo();

        ResultSet userAgentRs = getFirstRow(UdgerSqlQuery.SQL_CRAWLER, uaString);
        if (userAgentRs != null && userAgentRs.next()) {
            fetchUserAgent(userAgentRs, ret);
            clientInfo.classId = 99;
            clientInfo.clientId = -1;
        } else {
            int rowid = findIdFromList(uaString, clientWordDetector.findWords(uaString), clientRegstringList);
            if (rowid != -1) {
                userAgentRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT, rowid);
                userAgentRs.next();
                fetchUserAgent(userAgentRs, ret);
                clientInfo.classId = ret.getClassId();
                clientInfo.clientId = ret.getClientId();
                patchVersions(ret);
            } else {
                ret.setUaClass("Unrecognized");
                ret.setUaClassCode("unrecognized");
            }
        }
        return clientInfo;
    }

    private void osDetector(String uaString, UdgerUaResult ret, ClientInfo clientInfo) throws SQLException {
        int rowid = findIdFromList(uaString, osWordDetector.findWords(uaString), osRegstringList);
        if (rowid != -1) {
            ResultSet opSysRs = getFirstRow(UdgerSqlQuery.SQL_OS, rowid);
            opSysRs.next();
            fetchOperatingSystem(opSysRs, ret);
        } else {
            if (clientInfo.clientId != null && clientInfo.clientId != 0) {
                ResultSet opSysRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT_OS, clientInfo.clientId.toString());
                if (opSysRs != null && opSysRs.next()) {
                    fetchOperatingSystem(opSysRs, ret);
                }
            }
        }
    }

    private void deviceDetector(String uaString, UdgerUaResult ret, ClientInfo clientInfo) throws SQLException {
        int rowid = findIdFromList(uaString, deviceWordDetector.findWords(uaString), deviceRegstringList);
        if (rowid != -1) {
            ResultSet devRs = getFirstRow(UdgerSqlQuery.SQL_DEVICE, rowid);
            devRs.next();
            fetchDevice(devRs, ret);
        } else {
            if (clientInfo.classId != null && clientInfo.classId != -1) {
                ResultSet devRs = getFirstRow(UdgerSqlQuery.SQL_CLIENT_CLASS, clientInfo.classId.toString());
                if (devRs != null && devRs.next()) {
                    fetchDevice(devRs, ret);
                }
            }
        }
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


    private int[] ip6ToArray(Inet6Address addr) {
        int ret[] = new int[8];
        byte[] bytes = addr.getAddress();
        for (int i=0; i < 8; i++) {
            ret[i] = ((bytes [i*2] << 8 ) & 0xff00 )| (bytes[i*2+1] & 0xff);
        }
        return ret;
    }

    private void prepare() throws SQLException {
        connect();
        if (clientRegstringList == null) {
            initStaticStructures(connection);
        }
    }

    private void connect() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFileName);
        }
    }

    private Pattern getRegexFromCache(String regex) {
        Pattern patRegex = regexCache.get(regex);
        if (patRegex == null) {
            Matcher m = PAT_UNPERLIZE.matcher(regex);
            if (m.matches()) {
                regex = m.group(1);
            }
            patRegex = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            regexCache.put(regex, patRegex);
        }
        return patRegex;
    }

    private ResultSet getFirstRow(String query, Object ... params) throws SQLException {
        PreparedStatement preparedStatement = preparedStmtMap.get(query);
        if (preparedStatement == null) {
            preparedStatement = connection.prepareStatement(query);
            preparedStmtMap.put(query, preparedStatement);
        }
        for (int i=0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        preparedStatement.setMaxRows(1);
        return preparedStatement.executeQuery();
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
            if (lastPatternMatcher.groupCount()>=1) {
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

}

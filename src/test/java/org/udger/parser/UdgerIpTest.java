package org.udger.parser;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class UdgerIpTest {

    public static void main(String args[]) {
        InputStream is = UdgerUaTest.class.getResourceAsStream("test_ip.json");
        JsonReader jr = javax.json.Json.createReader(is);
        JsonArray ja = jr.readArray();
        UdgerParser up = new UdgerParser("udgerdb_v3.dat");
        for (int i=0; i < ja.size(); i++) {
            JsonObject jar = ja.getJsonObject(i);
            JsonObject jor = jar.getJsonObject("ret");
            String query = jar.getJsonObject("test").getString("teststring");
            try {
                UdgerIpResult ret = up.parseIp(query);
                if (checkResult(ret, jor)) {
                    System.out.println((i+1) + " : succeeded: " + query);
                } else {
                    System.out.println((i+1) + " : failed: " + query);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static boolean checkResult(UdgerIpResult ret, JsonObject jor) {
        boolean result = true;
        result = testEqual(jor, "ip_classification_code", ret != null ? ret.getIpClassificationCode() : null) && result;
        result = testEqual(jor, "datacenter_homepage", ret != null ? ret.getDataCenterHomePage() : null) && result;
        result = testEqual(jor, "crawler_respect_robotstxt", ret != null ? ret.getCrawlerRespectRobotstxt() : null) && result;
        result = testEqual(jor, "crawler_family_vendor_code", ret != null ? ret.getCrawlerFamilyVendorCode() : null) && result;
        result = testEqual(jor, "crawler_category", ret != null ? ret.getCrawlerCategory() : null) && result;
        result = testEqual(jor, "crawler_ver_major", ret != null ? ret.getCrawlerVerMajor() : null) && result;
        result = testEqual(jor, "ip_country", ret != null ? ret.getIpCountry() : null) && result;
        result = testEqual(jor, "crawler_ver", ret != null ? ret.getCrawlerVer() : null) && result;
        result = testEqual(jor, "crawler_name", ret != null ? ret.getCrawlerName() : null) && result;
        result = testEqual(jor, "ip_city", ret != null ? ret.getIpCity() : null) && result;
        result = testEqual(jor, "datacenter_name_code", ret != null ? ret.getDataCenterNameCode() : null) && result;
        result = testEqual(jor, "crawler_family_info_url", ret != null ? ret.getCrawlerFamilyInfoUrl() : null) && result;
        result = testEqual(jor, "ip_country_code", ret != null ? ret.getIpCountryCode() : null) && result;
        result = testEqual(jor, "ip_ver", ret != null ? String.valueOf(ret.getIpVer()) : null) && result;
        result = testEqual(jor, "ip_classification", ret != null ? ret.getIpClassification() : null) && result;
        result = testEqual(jor, "crawler_family_code", ret != null ? ret.getCrawlerFamilyCode() : null) && result;
        result = testEqual(jor, "crawler_family_icon", ret != null ? ret.getCrawlerFamilyIcon() : null) && result;
        result = testEqual(jor, "crawler_family_homepage", ret != null ? ret.getCrawlerFamilyHomepage() : null) && result;
        result = testEqual(jor, "crawler_family_vendor_homepage", ret != null ? ret.getCrawlerFamilyVendorHomepage() : null) && result;
        result = testEqual(jor, "datacenter_name", ret != null ? ret.getDataCenterName() : null) && result;
        result = testEqual(jor, "ip", ret != null ? ret.getIp() : null) && result;
        result = testEqual(jor, "crawler_family", ret != null ? ret.getCrawlerFamily() : null) && result;
        result = testEqual(jor, "ip_hostname", ret != null ? ret.getIpHostname() : null) && result;
        result = testEqual(jor, "crawler_category_code", ret != null ? ret.getCrawlerCategoryCode() : null) && result;
        result = testEqual(jor, "crawler_family_vendor", ret != null ? ret.getCrawlerFamilyVendor() : null) && result;
        return result;
    }

    private static boolean testEqual(JsonObject jor, String test, String ret) {
        String expected = jor.getString(test);
        if (ret == null) {
            ret = "";
        }
        if (!expected.equals(ret)) {
            System.out.println("Failed \"" + test + "\" : value=" + ret + "  expected:" + expected);
            return false;
        }
        return true;
    }
}

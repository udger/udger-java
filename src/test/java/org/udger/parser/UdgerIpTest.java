package org.udger.parser;

import java.io.IOException;
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
        UdgerParser up = null;
        try {
            UdgerParser.ParserDbData parserDbData = new UdgerParser.ParserDbData("udgerdb_v3.dat");
            up = new UdgerParser(parserDbData);
            for (int i=0; i < ja.size(); i++) {
                JsonObject jar = ja.getJsonObject(i);
                JsonObject jor = jar.getJsonObject("ret");
                String query = jar.getJsonObject("test").getString("teststring");
                try {
                    UdgerIpResult ret = up.parseIp(query);
                    System.out.print("### Test : " + (i+1) + " - ");
                    if (checkResult(ret, jor)) {
                        System.out.println("SUCCEEDED");
                    } else {
                        System.out.println("FAILED!");
                    }
                    System.out.println("Query: " + query);
//                    System.out.println("Result: " + ReflectionToStringBuilder.toString(ret, ToStringStyle.MULTI_LINE_STYLE));

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } finally {
            if (up != null) {
                try {
                    up.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static boolean checkResult(UdgerIpResult ret, JsonObject jor) {
        boolean result = true;
        result = testEqual(jor, "ip_classification_code", ret.getIpClassificationCode()) && result;
        result = testEqual(jor, "datacenter_homepage", ret.getDataCenterHomePage()) && result;
        result = testEqual(jor, "crawler_respect_robotstxt", ret.getCrawlerRespectRobotstxt()) && result;
        result = testEqual(jor, "crawler_family_vendor_code", ret.getCrawlerFamilyVendorCode()) && result;
        result = testEqual(jor, "crawler_category", ret.getCrawlerCategory()) && result;
        result = testEqual(jor, "crawler_ver_major", ret.getCrawlerVerMajor()) && result;
        result = testEqual(jor, "ip_country", ret.getIpCountry()) && result;
        result = testEqual(jor, "crawler_ver", ret.getCrawlerVer()) && result;
        result = testEqual(jor, "crawler_name", ret.getCrawlerName()) && result;
        result = testEqual(jor, "ip_city", ret.getIpCity()) && result;
        result = testEqual(jor, "datacenter_name_code", ret.getDataCenterNameCode()) && result;
        result = testEqual(jor, "crawler_family_info_url", ret.getCrawlerFamilyInfoUrl()) && result;
        result = testEqual(jor, "ip_country_code", ret.getIpCountryCode()) && result;
        result = testEqual(jor, "ip_ver", String.valueOf(ret.getIpVer())) && result;
        result = testEqual(jor, "ip_classification", ret.getIpClassification()) && result;
        result = testEqual(jor, "crawler_family_code", ret.getCrawlerFamilyCode()) && result;
        result = testEqual(jor, "crawler_family_icon", ret.getCrawlerFamilyIcon()) && result;
        result = testEqual(jor, "crawler_family_homepage", ret.getCrawlerFamilyHomepage()) && result;
        result = testEqual(jor, "crawler_family_vendor_homepage", ret.getCrawlerFamilyVendorHomepage()) && result;
        result = testEqual(jor, "datacenter_name", ret.getDataCenterName()) && result;
        result = testEqual(jor, "ip", ret.getIp()) && result;
        result = testEqual(jor, "crawler_family", ret.getCrawlerFamily()) && result;
        result = testEqual(jor, "ip_hostname", ret.getIpHostname()) && result;
        result = testEqual(jor, "crawler_category_code", ret.getCrawlerCategoryCode()) && result;
        result = testEqual(jor, "crawler_family_vendor", ret.getCrawlerFamilyVendor()) && result;
        return result;
    }

    private static boolean testEqual(JsonObject jor, String test, String ret) {
        String expected = jor.getString(test);
        if (!expected.equals(ret)) {
            System.out.println("Failed \"" + test + "\" : value=" + ret + "  expected:" + expected);
            return false;
        }
        return true;
    }
}

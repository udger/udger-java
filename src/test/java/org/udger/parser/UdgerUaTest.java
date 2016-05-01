package org.udger.parser;

import java.io.InputStream;
import java.sql.SQLException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class UdgerUaTest {

    public static void main(String args[]) {
        InputStream is = UdgerUaTest.class.getResourceAsStream("test_ua.json");
        JsonReader jr = javax.json.Json.createReader(is);
        JsonArray ja = jr.readArray();
        UdgerParser up = new UdgerParser("udgerdb_v3.dat");
        for (int i=0; i < ja.size(); i++) {
            JsonObject jar = ja.getJsonObject(i);
            JsonObject jor = jar.getJsonObject("ret");
            String query = jar.getJsonObject("test").getString("teststring");
            try {
                UdgerUaResult ret = up.parseUa(query);
                if (checkResult(ret, jor)) {
                    System.out.println((i+1) + " : succeeded: " + query);
                } else {
                    System.out.println((i+1) + " : failed: " + query);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean checkResult(UdgerUaResult ret, JsonObject jor) {
        boolean result = true;
        result = testEqual(jor, "ua_engine", ret != null ? ret.getUaEngine() : null) && result;
        result = testEqual(jor, "ua_version", ret != null ? ret.getUaVersion() : null) && result;
        result = testEqual(jor, "ua_family_code", ret != null ? ret.getUaFamilyCode() : null) && result;
        result = testEqual(jor, "ua_family_icon_big", ret != null ? ret.getUaFamilyIconBig() : null) && result;
        result = testEqual(jor, "crawler_category", ret != null ? ret.getCrawlerCategory() : null) && result;
        result = testEqual(jor, "ua_family_icon", ret != null ? ret.getUaFamilyIcon() : null) && result;
        result = testEqual(jor, "ua_family_vendor", ret != null ? ret.getUaFamilyVendor() : null) && result;
        result = testEqual(jor, "ua_family_vendor_code", ret != null ? ret.getUaFamilyVendorCode() : null) && result;
        result = testEqual(jor, "ua_uptodate_current_version", ret != null ? ret.getUaUptodateCurrentVersion() : null) && result;
        result = testEqual(jor, "ua_class_code", ret != null ? ret.getUaClassCode() : null) && result;
        result = testEqual(jor, "ua", ret != null ? ret.getUa() : null) && result;
        result = testEqual(jor, "ua_family", ret != null ? ret.getUaFamily() : null) && result;
        result = testEqual(jor, "ua_family_homepage", ret != null ? ret.getUaFamilyHomepage() : null) && result;
        result = testEqual(jor, "ua_version_major", ret != null ? ret.getUaVersionMajor() : null) && result;
        result = testEqual(jor, "ua_family_info_url", ret != null ? ret.getUaFamilyInfoUrl() : null) && result;
        result = testEqual(jor, "crawler_respect_robotstxt", ret != null ? ret.getCrawlerRespectRobotstxt() : null) && result;
        result = testEqual(jor, "ua_class", ret != null ? ret.getUaClass() : null) && result;
        result = testEqual(jor, "ua_family_vendor_homepage", ret != null ? ret.getUaFamilyVendorHomepage() : null) && result;
        result = testEqual(jor, "crawler_category_code", ret != null ? ret.getCrawlerCategoryCode() : null) && result;
//        result = testEqual(jor, "ua_string", ret.getUserAgent() != null ? ret.getUserAgent().get : null) && result;

        result = testEqual(jor, "os_family_vendor_homepage", ret != null ? ret.getOsFamilyVedorHomepage() : null) && result;
        result = testEqual(jor, "os_icon_big", ret != null ? ret.getOsIconBig() : null) && result;
        result = testEqual(jor, "os_homepage", ret != null ? ret.getOsHomePage() : null) && result;
        result = testEqual(jor, "os_icon", ret != null ? ret.getOsIcon() : null) && result;
        result = testEqual(jor, "os", ret != null ? ret.getOs() : null) && result;
        result = testEqual(jor, "os_family_code", ret != null ? ret.getOsFamilyCode() : null) && result;
        result = testEqual(jor, "os_family_vendor", ret != null ? ret.getOsFamilyVendor() : null) && result;
        result = testEqual(jor, "os_family_vendor_code", ret != null ? ret.getOsFamilyVendorCode() : null) && result;
        result = testEqual(jor, "os_code", ret != null ? ret.getOsCode() : null) && result;
        result = testEqual(jor, "os_family", ret != null ? ret.getOsFamily() : null) && result;
        result = testEqual(jor, "os_info_url", ret != null ? ret.getOsInfoUrl() : null) && result;

        result = testEqual(jor, "device_class", ret != null ? ret.getDeviceClass() : null) && result;
        result = testEqual(jor, "device_class_icon_big", ret != null ? ret.getDeviceClassIconBig() : null) && result;
        result = testEqual(jor, "device_class_icon", ret != null ? ret.getDeviceClassIcon() : null) && result;
        result = testEqual(jor, "device_class_info_url", ret != null ? ret.getDeviceClassInfoUrl() : null) && result;
        result = testEqual(jor, "device_class_code", ret != null ? ret.getDeviceClassCode() : null) && result;

        return result;
    }

    private static boolean testEqual(JsonObject jor, String test, String ret) {
        String expected = jor.getString(test);
        if (ret == null) {
            ret = "";
        }
        if (!expected.equals(ret) && expected.startsWith("https://")) {
            expected = expected.replaceAll(" ", "%20");
        }
        if (!expected.equals(ret)) {
            System.out.println("Failed \"" + test + "\" : value=" + ret + "  expected:" + expected);
            return false;
        }
        return true;
    }

}

package org.udger.parser;

import java.io.InputStream;
import java.sql.SQLException;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class UdgerDbTest {

    public static void main(String args[]) {
        InputStream is = UdgerDbTest.class.getResourceAsStream("test_ua.json");
        JsonReader jr = javax.json.Json.createReader(is);
        JsonArray ja = jr.readArray();
        UdgerParser up = new UdgerParser("udgerdb_v3.dat");
        for (int i=0; i < ja.size(); i++) {
            JsonObject jar = ja.getJsonObject(i);
            JsonObject jor = jar.getJsonObject("ret");
            String query = jar.getJsonObject("test").getString("teststring");
            try {
                UdgerUaQueryResult ret = up.parseUa(query);
                if (checkResult(ret, jor)) {
                    System.out.println("Succeeded: " + query);
                } else {
                    System.out.println("Failed: " + query);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean checkResult(UdgerUaQueryResult ret, JsonObject jor) {
        boolean result = true;
        result = testEqual(jor, "ua_engine", ret.getUserAgent() != null ? ret.getUserAgent().getUaEngine() : null) && result;
        result = testEqual(jor, "ua_version", ret.getUserAgent() != null ? ret.getUserAgent().getUaVersion() : null) && result;
        result = testEqual(jor, "ua_family_code", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamilyCode() : null) && result;
        result = testEqual(jor, "ua_family_icon_big", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamilyIconBig() : null) && result;
        result = testEqual(jor, "crawler_category", ret.getUserAgent() != null ? ret.getUserAgent().getCrawlerCategory() : null) && result;
        result = testEqual(jor, "ua_family_icon", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamilyIcon() : null) && result;
        result = testEqual(jor, "ua_family_vendor", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamilyVendor() : null) && result;
        result = testEqual(jor, "ua_family_vendor_code", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamilyVendorCode() : null) && result;
        result = testEqual(jor, "ua_uptodate_current_version", ret.getUserAgent() != null ? ret.getUserAgent().getUaUptodateCurrentVersion() : null) && result;
        result = testEqual(jor, "ua_class_code", ret.getUserAgent() != null ? ret.getUserAgent().getUaClassCode() : null) && result;
        result = testEqual(jor, "ua", ret.getUserAgent() != null ? ret.getUserAgent().getUa() : null) && result;
        result = testEqual(jor, "ua_family", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamily() : null) && result;
        result = testEqual(jor, "ua_family_homepage", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamilyHomepage() : null) && result;
        result = testEqual(jor, "ua_version_major", ret.getUserAgent() != null ? ret.getUserAgent().getUaVersionMajor() : null) && result;
        result = testEqual(jor, "ua_family_info_url", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamilyInfoUrl() : null) && result;
        result = testEqual(jor, "crawler_respect_robotstxt", ret.getUserAgent() != null ? ret.getUserAgent().getCrawlerRespectRobotstxt() : null) && result;
        result = testEqual(jor, "ua_class", ret.getUserAgent() != null ? ret.getUserAgent().getUaClass() : null) && result;
        result = testEqual(jor, "ua_family_vendor_homepage", ret.getUserAgent() != null ? ret.getUserAgent().getUaFamilyVendorHomepage() : null) && result;
        result = testEqual(jor, "crawler_category_code", ret.getUserAgent() != null ? ret.getUserAgent().getCrawlerCategoryCode() : null) && result;
//        result = testEqual(jor, "ua_string", ret.getUserAgent() != null ? ret.getUserAgent().get : null) && result;

        result = testEqual(jor, "os_family_vendor_homepage", ret.getOs() != null ? ret.getOs().getOsFamilyVedorHomepage() : null) && result;
        result = testEqual(jor, "os_icon_big", ret.getOs() != null ? ret.getOs().getOsIconBig() : null) && result;
        result = testEqual(jor, "os_homepage", ret.getOs() != null ? ret.getOs().getOsHomePage() : null) && result;
        result = testEqual(jor, "os_icon", ret.getOs() != null ? ret.getOs().getOsIcon() : null) && result;
        result = testEqual(jor, "os", ret.getOs() != null ? ret.getOs().getOs() : null) && result;
        result = testEqual(jor, "os_family_code", ret.getOs() != null ? ret.getOs().getOsFamilyCode() : null) && result;
        result = testEqual(jor, "os_family_vendor", ret.getOs() != null ? ret.getOs().getOsFamilyVendor() : null) && result;
        result = testEqual(jor, "os_family_vendor_code", ret.getOs() != null ? ret.getOs().getOsFamilyVendorCode() : null) && result;
        result = testEqual(jor, "os_code", ret.getOs() != null ? ret.getOs().getOsCode() : null) && result;
        result = testEqual(jor, "os_family", ret.getOs() != null ? ret.getOs().getFamily() : null) && result;
        result = testEqual(jor, "os_info_url", ret.getOs() != null ? ret.getOs().getOsInfoUrl() : null) && result;

        result = testEqual(jor, "device_class", ret.getDevice() != null ? ret.getDevice().getDeviceClass() : null) && result;
        result = testEqual(jor, "device_class_icon_big", ret.getDevice() != null ? ret.getDevice().getDeviceClassIconBig() : null) && result;
        result = testEqual(jor, "device_class_icon", ret.getDevice() != null ? ret.getDevice().getDeviceClassIcon() : null) && result;
        result = testEqual(jor, "device_class_info_url", ret.getDevice() != null ? ret.getDevice().getDeviceClassInfoUrl() : null) && result;
        result = testEqual(jor, "device_class_code", ret.getDevice() != null ? ret.getDevice().getDeviceClassCode() : null) && result;

        return result;
    }

    private static boolean testEqual(JsonObject jor, String test, String ret) {
        String expected = jor.getString(test);
        if (ret == null) {
            ret = "";
        }
        if (!expected.equals(ret)) {
            System.out.println("Fails: value=" + ret + "  expected:" + expected);
            return false;
        }
        return true;
    }

}

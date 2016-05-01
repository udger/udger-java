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
        UdgerParser up = new UdgerParser("/home/lada/Download/udgerdb_v3.dat");
        for (int i=0; i < ja.size(); i++) {
            JsonObject jar = ja.getJsonObject(i);
            JsonObject jor = jar.getJsonObject("ret");
            String query = jar.getJsonObject("test").getString("teststring");
            try {
                UdgerUaResult ret = up.parseUa(query);
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

    private static boolean checkResult(UdgerUaResult ret, JsonObject jor) {
        boolean result = true;
        result = testEqual(jor, "ua_engine", ret != null && ret.getUa() != null ? ret.getUa().getUaEngine() : null) && result;
        result = testEqual(jor, "ua_version", ret != null && ret.getUa() != null ? ret.getUa().getUaVersion() : null) && result;
        result = testEqual(jor, "ua_family_code", ret != null && ret.getUa() != null ? ret.getUa().getUaFamilyCode() : null) && result;
        result = testEqual(jor, "ua_family_icon_big", ret != null && ret.getUa() != null ? ret.getUa().getUaFamilyIconBig() : null) && result;
        result = testEqual(jor, "crawler_category", ret != null && ret.getUa() != null ? ret.getUa().getCrawlerCategory() : null) && result;
        result = testEqual(jor, "ua_family_icon", ret != null && ret.getUa() != null ? ret.getUa().getUaFamilyIcon() : null) && result;
        result = testEqual(jor, "ua_family_vendor", ret != null && ret.getUa() != null ? ret.getUa().getUaFamilyVendor() : null) && result;
        result = testEqual(jor, "ua_family_vendor_code", ret != null && ret.getUa() != null ? ret.getUa().getUaFamilyVendorCode() : null) && result;
        result = testEqual(jor, "ua_uptodate_current_version", ret != null && ret.getUa() != null ? ret.getUa().getUaUptodateCurrentVersion() : null) && result;
        result = testEqual(jor, "ua_class_code", ret != null && ret.getUa() != null ? ret.getUa().getUaClassCode() : null) && result;
        result = testEqual(jor, "ua", ret != null && ret.getUa() != null ? ret.getUa().getUa() : null) && result;
        result = testEqual(jor, "ua_family", ret != null && ret.getUa() != null ? ret.getUa().getUaFamily() : null) && result;
        result = testEqual(jor, "ua_family_homepage", ret != null && ret.getUa() != null ? ret.getUa().getUaFamilyHomepage() : null) && result;
        result = testEqual(jor, "ua_version_major", ret != null && ret.getUa() != null ? ret.getUa().getUaVersionMajor() : null) && result;
        result = testEqual(jor, "ua_family_info_url", ret != null && ret.getUa() != null ? ret.getUa().getUaFamilyInfoUrl() : null) && result;
        result = testEqual(jor, "crawler_respect_robotstxt", ret != null && ret.getUa() != null ? ret.getUa().getCrawlerRespectRobotstxt() : null) && result;
        result = testEqual(jor, "ua_class", ret != null && ret.getUa() != null ? ret.getUa().getUaClass() : null) && result;
        result = testEqual(jor, "ua_family_vendor_homepage", ret != null && ret.getUa() != null ? ret.getUa().getUaFamilyVendorHomepage() : null) && result;
        result = testEqual(jor, "crawler_category_code", ret != null && ret.getUa() != null ? ret.getUa().getCrawlerCategoryCode() : null) && result;
//        result = testEqual(jor, "ua_string", ret.getUserAgent() != null ? ret.getUserAgent().get : null) && result;

        result = testEqual(jor, "os_family_vendor_homepage", ret != null && ret.getOs() != null ? ret.getOs().getOsFamilyVedorHomepage() : null) && result;
        result = testEqual(jor, "os_icon_big", ret != null && ret.getOs() != null ? ret.getOs().getOsIconBig() : null) && result;
        result = testEqual(jor, "os_homepage", ret != null && ret.getOs() != null ? ret.getOs().getOsHomePage() : null) && result;
        result = testEqual(jor, "os_icon", ret != null && ret.getOs() != null ? ret.getOs().getOsIcon() : null) && result;
        result = testEqual(jor, "os", ret != null && ret.getOs() != null ? ret.getOs().getOs() : null) && result;
        result = testEqual(jor, "os_family_code", ret != null && ret.getOs() != null ? ret.getOs().getOsFamilyCode() : null) && result;
        result = testEqual(jor, "os_family_vendor", ret != null && ret.getOs() != null ? ret.getOs().getOsFamilyVendor() : null) && result;
        result = testEqual(jor, "os_family_vendor_code", ret != null && ret.getOs() != null ? ret.getOs().getOsFamilyVendorCode() : null) && result;
        result = testEqual(jor, "os_code", ret != null && ret.getOs() != null ? ret.getOs().getOsCode() : null) && result;
        result = testEqual(jor, "os_family", ret != null && ret.getOs() != null ? ret.getOs().getFamily() : null) && result;
        result = testEqual(jor, "os_info_url", ret != null && ret.getOs() != null ? ret.getOs().getOsInfoUrl() : null) && result;

        result = testEqual(jor, "device_class", ret != null && ret.getDevice() != null ? ret.getDevice().getDeviceClass() : null) && result;
        result = testEqual(jor, "device_class_icon_big", ret != null && ret.getDevice() != null ? ret.getDevice().getDeviceClassIconBig() : null) && result;
        result = testEqual(jor, "device_class_icon", ret != null && ret.getDevice() != null ? ret.getDevice().getDeviceClassIcon() : null) && result;
        result = testEqual(jor, "device_class_info_url", ret != null && ret.getDevice() != null ? ret.getDevice().getDeviceClassInfoUrl() : null) && result;
        result = testEqual(jor, "device_class_code", ret != null && ret.getDevice() != null ? ret.getDevice().getDeviceClassCode() : null) && result;

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

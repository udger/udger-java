/*
  Udger-update - Data updater for udger local and cloud parser

  author     The Udger.com Team (info@udger.com)
  copyright  Copyright (c) Udger s.r.o.
  license    GNU Lesser General Public License
  link       https://udger.com/products
*/
package org.udger.parser;

import java.io.Serializable;

public class UdgerUaResult implements Serializable {

    private static final long serialVersionUID = 1L;

    // UA
    private final String uaString;
    private Integer clientId;
    private Integer classId;
    private String uaClass = "";
    private String uaClassCode = "";
    private String ua = "";
    private String uaEngine = "";
    private String uaVersion = "";
    private String uaVersionMajor = "";
    private String crawlerLastSeen = "";
    private String crawlerRespectRobotstxt = "";
    private String crawlerCategory = "";
    private String crawlerCategoryCode = "";
    private String uaUptodateCurrentVersion = "";
    private String uaFamily = "";
    private String uaFamilyCode = "";
    private String uaFamilyHomepage = "";
    private String uaFamilyIcon = "";
    private String uaFamilyIconBig = "";
    private String uaFamilyVendor = "";
    private String uaFamilyVendorCode = "";
    private String uaFamilyVendorHomepage = "";
    private String uaFamilyInfoUrl = "";

    // OS
    private String osFamily = "";
    private String osFamilyCode = "";
    private String os = "";
    private String osCode = "";
    private String osHomePage = "";
    private String osIcon = "";
    private String osIconBig = "";
    private String osFamilyVendor = "";
    private String osFamilyVendorCode = "";
    private String osFamilyVedorHomepage = "";
    private String osInfoUrl = "";

    // DEVICE
    private String deviceClass = "";
    private String deviceClassCode = "";
    private String deviceClassIcon = "";
    private String deviceClassIconBig = "";
    private String deviceClassInfoUrl = "";

    private String deviceMarketname = "";
    private String deviceBrand = "";
    private String deviceBrandCode = "";
    private String deviceBrandHomepage = "";
    private String deviceBrandIcon = "";
    private String deviceBrandIconBig = "";
    private String deviceBrandInfoUrl = "";

    public UdgerUaResult(String uaString) {
        this.uaString = uaString;
    }

    public String getUaString() {
        return uaString;
    }

    public Integer getClientId() {
        return clientId;
    }
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }
    public Integer getClassId() {
        return classId;
    }
    public void setClassId(Integer classId) {
        this.classId = classId;
    }
    public String getUaClass() {
        return uaClass;
    }
    public void setUaClass(String uaClass) {
        this.uaClass = uaClass;
    }
    public String getUaClassCode() {
        return uaClassCode;
    }
    public void setUaClassCode(String uaClassCode) {
        this.uaClassCode = uaClassCode;
    }
    public String getUa() {
        return ua;
    }
    public void setUa(String ua) {
        this.ua = ua;
    }
    public String getUaEngine() {
        return uaEngine;
    }
    public void setUaEngine(String uaEngine) {
        this.uaEngine = uaEngine;
    }
    public String getUaVersion() {
        return uaVersion;
    }
    public void setUaVersion(String uaVersion) {
        this.uaVersion = uaVersion;
    }
    public String getUaVersionMajor() {
        return uaVersionMajor;
    }
    public void setUaVersionMajor(String uaVersionMajor) {
        this.uaVersionMajor = uaVersionMajor;
    }
    public String getCrawlerLastSeen() {
        return crawlerLastSeen;
    }
    public void setCrawlerLastSeen(String crawlerLastSeen) {
        this.crawlerLastSeen = crawlerLastSeen;
    }
    public String getCrawlerRespectRobotstxt() {
        return crawlerRespectRobotstxt;
    }
    public void setCrawlerRespectRobotstxt(String crawlerRespectRobotstxt) {
        this.crawlerRespectRobotstxt = crawlerRespectRobotstxt;
    }
    public String getCrawlerCategory() {
        return crawlerCategory;
    }
    public void setCrawlerCategory(String crawlerCategory) {
        this.crawlerCategory = crawlerCategory;
    }
    public String getCrawlerCategoryCode() {
        return crawlerCategoryCode;
    }
    public void setCrawlerCategoryCode(String crawlerCategoryCode) {
        this.crawlerCategoryCode = crawlerCategoryCode;
    }
    public String getUaUptodateCurrentVersion() {
        return uaUptodateCurrentVersion;
    }
    public void setUaUptodateCurrentVersion(String uaUptodateCurrentVersion) {
        this.uaUptodateCurrentVersion = uaUptodateCurrentVersion;
    }
    public String getUaFamily() {
        return uaFamily;
    }
    public void setUaFamily(String uaFamily) {
        this.uaFamily = uaFamily;
    }
    public String getUaFamilyCode() {
        return uaFamilyCode;
    }
    public void setUaFamilyCode(String uaFamilyCode) {
        this.uaFamilyCode = uaFamilyCode;
    }
    public String getUaFamilyHomepage() {
        return uaFamilyHomepage;
    }
    public void setUaFamilyHomepage(String uaFamilyHomepage) {
        this.uaFamilyHomepage = uaFamilyHomepage;
    }
    public String getUaFamilyIcon() {
        return uaFamilyIcon;
    }
    public void setUaFamilyIcon(String uaFamilyIcon) {
        this.uaFamilyIcon = uaFamilyIcon;
    }
    public String getUaFamilyIconBig() {
        return uaFamilyIconBig;
    }
    public void setUaFamilyIconBig(String uaFamilyIconBig) {
        this.uaFamilyIconBig = uaFamilyIconBig;
    }
    public String getUaFamilyVendor() {
        return uaFamilyVendor;
    }
    public void setUaFamilyVendor(String uaFamilyVendor) {
        this.uaFamilyVendor = uaFamilyVendor;
    }
    public String getUaFamilyVendorCode() {
        return uaFamilyVendorCode;
    }
    public void setUaFamilyVendorCode(String uaFamilyVendorCode) {
        this.uaFamilyVendorCode = uaFamilyVendorCode;
    }
    public String getUaFamilyVendorHomepage() {
        return uaFamilyVendorHomepage;
    }
    public void setUaFamilyVendorHomepage(String uaFamilyVendorHomepage) {
        this.uaFamilyVendorHomepage = uaFamilyVendorHomepage;
    }
    public String getUaFamilyInfoUrl() {
        return uaFamilyInfoUrl;
    }
    public void setUaFamilyInfoUrl(String uaFamilyInfoUrl) {
        this.uaFamilyInfoUrl = uaFamilyInfoUrl;
    }

    public String getOsFamily() {
        return osFamily;
    }
    public void setOsFamily(String osFamily) {
        this.osFamily = osFamily;
    }
    public String getOsFamilyCode() {
        return osFamilyCode;
    }
    public void setOsFamilyCode(String osFamilyCode) {
        this.osFamilyCode = osFamilyCode;
    }
    public String getOs() {
        return os;
    }
    public void setOs(String os) {
        this.os = os;
    }
    public String getOsCode() {
        return osCode;
    }
    public void setOsCode(String osCode) {
        this.osCode = osCode;
    }
    public String getOsHomePage() {
        return osHomePage;
    }
    public void setOsHomePage(String osHomePage) {
        this.osHomePage = osHomePage;
    }
    public String getOsIcon() {
        return osIcon;
    }
    public void setOsIcon(String osIcon) {
        this.osIcon = osIcon;
    }
    public String getOsIconBig() {
        return osIconBig;
    }
    public void setOsIconBig(String osIconBig) {
        this.osIconBig = osIconBig;
    }
    public String getOsFamilyVendor() {
        return osFamilyVendor;
    }
    public void setOsFamilyVendor(String osFamilyVendor) {
        this.osFamilyVendor = osFamilyVendor;
    }
    public String getOsFamilyVendorCode() {
        return osFamilyVendorCode;
    }
    public void setOsFamilyVendorCode(String osFamilyVendorCode) {
        this.osFamilyVendorCode = osFamilyVendorCode;
    }
    public String getOsFamilyVedorHomepage() {
        return osFamilyVedorHomepage;
    }
    public void setOsFamilyVedorHomepage(String osFamilyVedorHomepage) {
        this.osFamilyVedorHomepage = osFamilyVedorHomepage;
    }
    public String getOsInfoUrl() {
        return osInfoUrl;
    }
    public void setOsInfoUrl(String osInfoUrl) {
        this.osInfoUrl = osInfoUrl;
    }

    public String getDeviceClass() {
        return deviceClass;
    }
    public void setDeviceClass(String deviceClass) {
        this.deviceClass = deviceClass;
    }
    public String getDeviceClassCode() {
        return deviceClassCode;
    }
    public void setDeviceClassCode(String deviceClassCode) {
        this.deviceClassCode = deviceClassCode;
    }
    public String getDeviceClassIcon() {
        return deviceClassIcon;
    }
    public void setDeviceClassIcon(String deviceClassIcon) {
        this.deviceClassIcon = deviceClassIcon;
    }
    public String getDeviceClassIconBig() {
        return deviceClassIconBig;
    }
    public void setDeviceClassIconBig(String deviceClassIconBig) {
        this.deviceClassIconBig = deviceClassIconBig;
    }
    public String getDeviceClassInfoUrl() {
        return deviceClassInfoUrl;
    }
    public void setDeviceClassInfoUrl(String deviceClassInfoUrl) {
        this.deviceClassInfoUrl = deviceClassInfoUrl;
    }

    public String getDeviceMarketname() {
        return deviceMarketname;
    }

    public void setDeviceMarketname(String deviceMarketname) {
        this.deviceMarketname = deviceMarketname;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceBrandCode() {
        return deviceBrandCode;
    }

    public void setDeviceBrandCode(String deviceBrandCode) {
        this.deviceBrandCode = deviceBrandCode;
    }

    public String getDeviceBrandHomepage() {
        return deviceBrandHomepage;
    }

    public void setDeviceBrandHomepage(String deviceBrandHomepage) {
        this.deviceBrandHomepage = deviceBrandHomepage;
    }

    public String getDeviceBrandIcon() {
        return deviceBrandIcon;
    }

    public void setDeviceBrandIcon(String deviceBrandIcon) {
        this.deviceBrandIcon = deviceBrandIcon;
    }

    public String getDeviceBrandIconBig() {
        return deviceBrandIconBig;
    }

    public void setDeviceBrandIconBig(String deviceBrandIconBig) {
        this.deviceBrandIconBig = deviceBrandIconBig;
    }

    public String getDeviceBrandInfoUrl() {
        return deviceBrandInfoUrl;
    }

    public void setDeviceBrandInfoUrl(String deviceBrandInfoUrl) {
        this.deviceBrandInfoUrl = deviceBrandInfoUrl;
    }

}

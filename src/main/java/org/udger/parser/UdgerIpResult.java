/*
  Udger-update - Data updater for udger local and cloud parser

  author     The Udger.com Team (info@udger.com)
  copyright  Copyright (c) Udger s.r.o.
  license    GNU Lesser General Public License
  link       https://udger.com/products
*/
package org.udger.parser;

import java.io.Serializable;

public class UdgerIpResult implements Serializable {

    private static final long serialVersionUID = 1L;

    // IP
    private String ip;
    private int ipVer;
    private String ipClassification;
    private String ipClassificationCode;
    private String ipLastSeen;
    private String ipHostname;
    private String ipCountry;
    private String ipCountryCode;
    private String ipCity;
    private String crawlerName;
    private String crawlerVer;
    private String crawlerVerMajor;
    private String crawlerFamily;
    private String crawlerFamilyCode;
    private String crawlerFamilyHomepage;
    private String crawlerFamilyVendor;
    private String crawlerFamilyVendorCode;
    private String crawlerFamilyVendorHomepage;
    private String crawlerFamilyIcon;
    private String crawlerFamilyInfoUrl;
    private String crawlerLastSeen;
    private String crawlerCategory;
    private String crawlerCategoryCode;
    private String crawlerRespectRobotstxt;

    // DATACENTER
    private String dataCenterName;
    private String dataCenterNameCode;
    private String dataCenterHomePage;

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public int getIpVer() {
        return ipVer;
    }
    public void setIpVer(int ipVer) {
        this.ipVer = ipVer;
    }
    public String getIpClassification() {
        return ipClassification;
    }
    public void setIpClassification(String ipClassification) {
        this.ipClassification = ipClassification;
    }
    public String getIpClassificationCode() {
        return ipClassificationCode;
    }
    public void setIpClassificationCode(String ipClassificationCode) {
        this.ipClassificationCode = ipClassificationCode;
    }
    public String getIpLastSeen() {
        return ipLastSeen;
    }
    public void setIpLastSeen(String ipLastSeen) {
        this.ipLastSeen = ipLastSeen;
    }
    public String getIpHostname() {
        return ipHostname;
    }
    public void setIpHostname(String ipHostname) {
        this.ipHostname = ipHostname;
    }
    public String getIpCountry() {
        return ipCountry;
    }
    public void setIpCountry(String ipCountry) {
        this.ipCountry = ipCountry;
    }
    public String getIpCountryCode() {
        return ipCountryCode;
    }
    public void setIpCountryCode(String ipCountryCode) {
        this.ipCountryCode = ipCountryCode;
    }
    public String getIpCity() {
        return ipCity;
    }
    public void setIpCity(String ipCity) {
        this.ipCity = ipCity;
    }
    public String getCrawlerName() {
        return crawlerName;
    }
    public void setCrawlerName(String crawlerName) {
        this.crawlerName = crawlerName;
    }
    public String getCrawlerVer() {
        return crawlerVer;
    }
    public void setCrawlerVer(String crawlerVer) {
        this.crawlerVer = crawlerVer;
    }
    public String getCrawlerVerMajor() {
        return crawlerVerMajor;
    }
    public void setCrawlerVerMajor(String crawlerVerMajor) {
        this.crawlerVerMajor = crawlerVerMajor;
    }
    public String getCrawlerFamily() {
        return crawlerFamily;
    }
    public void setCrawlerFamily(String crawlerFamily) {
        this.crawlerFamily = crawlerFamily;
    }
    public String getCrawlerFamilyCode() {
        return crawlerFamilyCode;
    }
    public void setCrawlerFamilyCode(String crawlerFamilyCode) {
        this.crawlerFamilyCode = crawlerFamilyCode;
    }
    public String getCrawlerFamilyHomepage() {
        return crawlerFamilyHomepage;
    }
    public void setCrawlerFamilyHomepage(String crawlerFamilyHomepage) {
        this.crawlerFamilyHomepage = crawlerFamilyHomepage;
    }
    public String getCrawlerFamilyVendor() {
        return crawlerFamilyVendor;
    }
    public void setCrawlerFamilyVendor(String crawlerFamilyVendor) {
        this.crawlerFamilyVendor = crawlerFamilyVendor;
    }
    public String getCrawlerFamilyVendorCode() {
        return crawlerFamilyVendorCode;
    }
    public void setCrawlerFamilyVendorCode(String crawlerFamilyVendorCode) {
        this.crawlerFamilyVendorCode = crawlerFamilyVendorCode;
    }
    public String getCrawlerFamilyVendorHomepage() {
        return crawlerFamilyVendorHomepage;
    }
    public void setCrawlerFamilyVendorHomepage(String crawlerFamilyVendorHomepage) {
        this.crawlerFamilyVendorHomepage = crawlerFamilyVendorHomepage;
    }
    public String getCrawlerFamilyIcon() {
        return crawlerFamilyIcon;
    }
    public void setCrawlerFamilyIcon(String crawlerFamilyIcon) {
        this.crawlerFamilyIcon = crawlerFamilyIcon;
    }
    public String getCrawlerFamilyInfoUrl() {
        return crawlerFamilyInfoUrl;
    }
    public void setCrawlerFamilyInfoUrl(String crawlerFamilyInfoUrl) {
        this.crawlerFamilyInfoUrl = crawlerFamilyInfoUrl;
    }
    public String getCrawlerLastSeen() {
        return crawlerLastSeen;
    }
    public void setCrawlerLastSeen(String crawlerLastSeen) {
        this.crawlerLastSeen = crawlerLastSeen;
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
    public String getCrawlerRespectRobotstxt() {
        return crawlerRespectRobotstxt;
    }
    public void setCrawlerRespectRobotstxt(String crawlerRespectRobotstxt) {
        this.crawlerRespectRobotstxt = crawlerRespectRobotstxt;
    }
    public String getDataCenterName() {
        return dataCenterName;
    }
    public void setDataCenterName(String dataCenterName) {
        this.dataCenterName = dataCenterName;
    }
    public String getDataCenterNameCode() {
        return dataCenterNameCode;
    }
    public void setDataCenterNameCode(String dataCenterNameCode) {
        this.dataCenterNameCode = dataCenterNameCode;
    }
    public String getDataCenterHomePage() {
        return dataCenterHomePage;
    }
    public void setDataCenterHomePage(String dataCenterHomePage) {
        this.dataCenterHomePage = dataCenterHomePage;
    }

}

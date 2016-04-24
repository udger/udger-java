/*
  Udger-update - Data updater for udger local and cloud parser

  author     The Udger.com Team (info@udger.com)
  copyright  Copyright (c) Udger s.r.o.
  license    GNU Lesser General Public License
  link       https://udger.com/products
*/
package org.udger.parser;

import java.io.Serializable;

public class UdgerUa implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer clientId;
    private Integer classId;
    private String uaClass;
    private String uaClassCode;
    private String ua;
    private String uaEngine;
    private String uaVersion;
    private String uaVersionMajor;
    private String crawlerLastSeen;
    private String crawlerRespectRobotstxt;
    private String crawlerCategory;
    private String crawlerCategoryCode;
    private String uaUptodateCurrentVersion;
    private String uaFamily;
    private String uaFamilyCode;
    private String uaFamilyHomepage;
    private String uaFamilyIcon;
    private String uaFamilyIconBig;
    private String uaFamilyVendor;
    private String uaFamilyVendorCode;
    private String uaFamilyVendorHomepage;
    private String uaFamilyInfoUrl;

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
}

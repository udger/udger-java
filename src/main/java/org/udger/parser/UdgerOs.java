/*
  Udger-update - Data updater for udger local and cloud parser

  author     The Udger.com Team (info@udger.com)
  copyright  Copyright (c) Udger s.r.o.
  license    GNU Lesser General Public License
  link       https://udger.com/products
*/
package org.udger.parser;

import java.io.Serializable;

public class UdgerOs implements Serializable {

    private static final long serialVersionUID = 1L;

    private String family;
    private String osFamilyCode;
    private String os;
    private String osCode;
    private String osHomePage;
    private String osIcon;
    private String osIconBig;
    private String osFamilyVendor;
    private String osFamilyVendorCode;
    private String osFamilyVedorHomepage;
    private String osInfoUrl;

    public String getFamily() {
        return family;
    }
    public void setFamily(String family) {
        this.family = family;
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
}

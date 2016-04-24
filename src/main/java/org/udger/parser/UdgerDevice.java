/*
  Udger-update - Data updater for udger local and cloud parser

  author     The Udger.com Team (info@udger.com)
  copyright  Copyright (c) Udger s.r.o.
  license    GNU Lesser General Public License
  link       https://udger.com/products
*/
package org.udger.parser;

import java.io.Serializable;

public class UdgerDevice implements Serializable {
    private static final long serialVersionUID = 1L;

    private String deviceClass;
    private String deviceClassCode;
    private String deviceClassIcon;
    private String deviceClassIconBig;
    private String deviceClassInfoUrl;

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

}

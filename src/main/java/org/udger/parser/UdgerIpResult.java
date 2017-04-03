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
    private final String ip;
    private int ipVer = 0;
    private String ipClassification = "";
    private String ipClassificationCode = "";
    private String ipLastSeen = "";
    private String ipHostname = "";
    private String ipCountry = "";
    private String ipCountryCode = "";
    private String ipCity = "";
    private String crawlerName = "";
    private String crawlerVer = "";
    private String crawlerVerMajor = "";
    private String crawlerFamily = "";
    private String crawlerFamilyCode = "";
    private String crawlerFamilyHomepage = "";
    private String crawlerFamilyVendor = "";
    private String crawlerFamilyVendorCode = "";
    private String crawlerFamilyVendorHomepage = "";
    private String crawlerFamilyIcon = "";
    private String crawlerFamilyInfoUrl = "";
    private String crawlerLastSeen = "";
    private String crawlerCategory = "";
    private String crawlerCategoryCode = "";
    private String crawlerRespectRobotstxt = "";

    // DATACENTER
    private String dataCenterName = "";
    private String dataCenterNameCode = "";
    private String dataCenterHomePage = "";

    public UdgerIpResult(String ipString) {
        this.ip = ipString;
    }

    public String getIp() {
        return ip;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((crawlerCategory == null) ? 0 : crawlerCategory.hashCode());
        result = prime * result + ((crawlerCategoryCode == null) ? 0 : crawlerCategoryCode.hashCode());
        result = prime * result + ((crawlerFamily == null) ? 0 : crawlerFamily.hashCode());
        result = prime * result + ((crawlerFamilyCode == null) ? 0 : crawlerFamilyCode.hashCode());
        result = prime * result + ((crawlerFamilyHomepage == null) ? 0 : crawlerFamilyHomepage.hashCode());
        result = prime * result + ((crawlerFamilyIcon == null) ? 0 : crawlerFamilyIcon.hashCode());
        result = prime * result + ((crawlerFamilyInfoUrl == null) ? 0 : crawlerFamilyInfoUrl.hashCode());
        result = prime * result + ((crawlerFamilyVendor == null) ? 0 : crawlerFamilyVendor.hashCode());
        result = prime * result + ((crawlerFamilyVendorCode == null) ? 0 : crawlerFamilyVendorCode.hashCode());
        result = prime * result + ((crawlerFamilyVendorHomepage == null) ? 0 : crawlerFamilyVendorHomepage.hashCode());
        result = prime * result + ((crawlerLastSeen == null) ? 0 : crawlerLastSeen.hashCode());
        result = prime * result + ((crawlerName == null) ? 0 : crawlerName.hashCode());
        result = prime * result + ((crawlerRespectRobotstxt == null) ? 0 : crawlerRespectRobotstxt.hashCode());
        result = prime * result + ((crawlerVer == null) ? 0 : crawlerVer.hashCode());
        result = prime * result + ((crawlerVerMajor == null) ? 0 : crawlerVerMajor.hashCode());
        result = prime * result + ((dataCenterHomePage == null) ? 0 : dataCenterHomePage.hashCode());
        result = prime * result + ((dataCenterName == null) ? 0 : dataCenterName.hashCode());
        result = prime * result + ((dataCenterNameCode == null) ? 0 : dataCenterNameCode.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        result = prime * result + ((ipCity == null) ? 0 : ipCity.hashCode());
        result = prime * result + ((ipClassification == null) ? 0 : ipClassification.hashCode());
        result = prime * result + ((ipClassificationCode == null) ? 0 : ipClassificationCode.hashCode());
        result = prime * result + ((ipCountry == null) ? 0 : ipCountry.hashCode());
        result = prime * result + ((ipCountryCode == null) ? 0 : ipCountryCode.hashCode());
        result = prime * result + ((ipHostname == null) ? 0 : ipHostname.hashCode());
        result = prime * result + ((ipLastSeen == null) ? 0 : ipLastSeen.hashCode());
        result = prime * result + ipVer;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UdgerIpResult other = (UdgerIpResult) obj;
        if (crawlerCategory == null) {
            if (other.crawlerCategory != null)
                return false;
        } else if (!crawlerCategory.equals(other.crawlerCategory))
            return false;
        if (crawlerCategoryCode == null) {
            if (other.crawlerCategoryCode != null)
                return false;
        } else if (!crawlerCategoryCode.equals(other.crawlerCategoryCode))
            return false;
        if (crawlerFamily == null) {
            if (other.crawlerFamily != null)
                return false;
        } else if (!crawlerFamily.equals(other.crawlerFamily))
            return false;
        if (crawlerFamilyCode == null) {
            if (other.crawlerFamilyCode != null)
                return false;
        } else if (!crawlerFamilyCode.equals(other.crawlerFamilyCode))
            return false;
        if (crawlerFamilyHomepage == null) {
            if (other.crawlerFamilyHomepage != null)
                return false;
        } else if (!crawlerFamilyHomepage.equals(other.crawlerFamilyHomepage))
            return false;
        if (crawlerFamilyIcon == null) {
            if (other.crawlerFamilyIcon != null)
                return false;
        } else if (!crawlerFamilyIcon.equals(other.crawlerFamilyIcon))
            return false;
        if (crawlerFamilyInfoUrl == null) {
            if (other.crawlerFamilyInfoUrl != null)
                return false;
        } else if (!crawlerFamilyInfoUrl.equals(other.crawlerFamilyInfoUrl))
            return false;
        if (crawlerFamilyVendor == null) {
            if (other.crawlerFamilyVendor != null)
                return false;
        } else if (!crawlerFamilyVendor.equals(other.crawlerFamilyVendor))
            return false;
        if (crawlerFamilyVendorCode == null) {
            if (other.crawlerFamilyVendorCode != null)
                return false;
        } else if (!crawlerFamilyVendorCode.equals(other.crawlerFamilyVendorCode))
            return false;
        if (crawlerFamilyVendorHomepage == null) {
            if (other.crawlerFamilyVendorHomepage != null)
                return false;
        } else if (!crawlerFamilyVendorHomepage.equals(other.crawlerFamilyVendorHomepage))
            return false;
        if (crawlerLastSeen == null) {
            if (other.crawlerLastSeen != null)
                return false;
        } else if (!crawlerLastSeen.equals(other.crawlerLastSeen))
            return false;
        if (crawlerName == null) {
            if (other.crawlerName != null)
                return false;
        } else if (!crawlerName.equals(other.crawlerName))
            return false;
        if (crawlerRespectRobotstxt == null) {
            if (other.crawlerRespectRobotstxt != null)
                return false;
        } else if (!crawlerRespectRobotstxt.equals(other.crawlerRespectRobotstxt))
            return false;
        if (crawlerVer == null) {
            if (other.crawlerVer != null)
                return false;
        } else if (!crawlerVer.equals(other.crawlerVer))
            return false;
        if (crawlerVerMajor == null) {
            if (other.crawlerVerMajor != null)
                return false;
        } else if (!crawlerVerMajor.equals(other.crawlerVerMajor))
            return false;
        if (dataCenterHomePage == null) {
            if (other.dataCenterHomePage != null)
                return false;
        } else if (!dataCenterHomePage.equals(other.dataCenterHomePage))
            return false;
        if (dataCenterName == null) {
            if (other.dataCenterName != null)
                return false;
        } else if (!dataCenterName.equals(other.dataCenterName))
            return false;
        if (dataCenterNameCode == null) {
            if (other.dataCenterNameCode != null)
                return false;
        } else if (!dataCenterNameCode.equals(other.dataCenterNameCode))
            return false;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        if (ipCity == null) {
            if (other.ipCity != null)
                return false;
        } else if (!ipCity.equals(other.ipCity))
            return false;
        if (ipClassification == null) {
            if (other.ipClassification != null)
                return false;
        } else if (!ipClassification.equals(other.ipClassification))
            return false;
        if (ipClassificationCode == null) {
            if (other.ipClassificationCode != null)
                return false;
        } else if (!ipClassificationCode.equals(other.ipClassificationCode))
            return false;
        if (ipCountry == null) {
            if (other.ipCountry != null)
                return false;
        } else if (!ipCountry.equals(other.ipCountry))
            return false;
        if (ipCountryCode == null) {
            if (other.ipCountryCode != null)
                return false;
        } else if (!ipCountryCode.equals(other.ipCountryCode))
            return false;
        if (ipHostname == null) {
            if (other.ipHostname != null)
                return false;
        } else if (!ipHostname.equals(other.ipHostname))
            return false;
        if (ipLastSeen == null) {
            if (other.ipLastSeen != null)
                return false;
        } else if (!ipLastSeen.equals(other.ipLastSeen))
            return false;
        if (ipVer != other.ipVer)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UdgerIpResult [" +
                "ip=" + ip +
                ", ipVer=" + ipVer +
                ", ipClassification=" + ipClassification +
                ", ipClassificationCode=" + ipClassificationCode +
                ", ipLastSeen=" + ipLastSeen +
                ", ipHostname=" + ipHostname +
                ", ipCountry=" + ipCountry +
                ", ipCountryCode=" + ipCountryCode +
                ", ipCity=" + ipCity +
                ", crawlerName=" + crawlerName +
                ", crawlerVer=" + crawlerVer +
                ", crawlerVerMajor=" + crawlerVerMajor +
                ", crawlerFamily=" + crawlerFamily +
                ", crawlerFamilyCode=" + crawlerFamilyCode +
                ", crawlerFamilyHomepage=" + crawlerFamilyHomepage +
                ", crawlerFamilyVendor=" + crawlerFamilyVendor +
                ", crawlerFamilyVendorCode=" + crawlerFamilyVendorCode +
                ", crawlerFamilyVendorHomepage=" + crawlerFamilyVendorHomepage +
                ", crawlerFamilyIcon=" + crawlerFamilyIcon +
                ", crawlerFamilyInfoUrl=" + crawlerFamilyInfoUrl +
                ", crawlerLastSeen=" + crawlerLastSeen +
                ", crawlerCategory=" + crawlerCategory +
                ", crawlerCategoryCode=" + crawlerCategoryCode +
                ", crawlerRespectRobotstxt=" + crawlerRespectRobotstxt +
                ", dataCenterName=" + dataCenterName +
                ", dataCenterNameCode=" + dataCenterNameCode +
                ", dataCenterHomePage=" + dataCenterHomePage +
                "]";
    }

}

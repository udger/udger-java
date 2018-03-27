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
    private String osFamilyVendorHomepage = "";
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
    public String getOsFamilyVendorHomepage() {
        return osFamilyVendorHomepage;
    }
    public void setOsFamilyVendorHomepage(String osFamilyVendorHomepage) {
        this.osFamilyVendorHomepage = osFamilyVendorHomepage;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classId == null) ? 0 : classId.hashCode());
        result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
        result = prime * result + ((crawlerCategory == null) ? 0 : crawlerCategory.hashCode());
        result = prime * result + ((crawlerCategoryCode == null) ? 0 : crawlerCategoryCode.hashCode());
        result = prime * result + ((crawlerLastSeen == null) ? 0 : crawlerLastSeen.hashCode());
        result = prime * result + ((crawlerRespectRobotstxt == null) ? 0 : crawlerRespectRobotstxt.hashCode());
        result = prime * result + ((deviceBrand == null) ? 0 : deviceBrand.hashCode());
        result = prime * result + ((deviceBrandCode == null) ? 0 : deviceBrandCode.hashCode());
        result = prime * result + ((deviceBrandHomepage == null) ? 0 : deviceBrandHomepage.hashCode());
        result = prime * result + ((deviceBrandIcon == null) ? 0 : deviceBrandIcon.hashCode());
        result = prime * result + ((deviceBrandIconBig == null) ? 0 : deviceBrandIconBig.hashCode());
        result = prime * result + ((deviceBrandInfoUrl == null) ? 0 : deviceBrandInfoUrl.hashCode());
        result = prime * result + ((deviceClass == null) ? 0 : deviceClass.hashCode());
        result = prime * result + ((deviceClassCode == null) ? 0 : deviceClassCode.hashCode());
        result = prime * result + ((deviceClassIcon == null) ? 0 : deviceClassIcon.hashCode());
        result = prime * result + ((deviceClassIconBig == null) ? 0 : deviceClassIconBig.hashCode());
        result = prime * result + ((deviceClassInfoUrl == null) ? 0 : deviceClassInfoUrl.hashCode());
        result = prime * result + ((deviceMarketname == null) ? 0 : deviceMarketname.hashCode());
        result = prime * result + ((os == null) ? 0 : os.hashCode());
        result = prime * result + ((osCode == null) ? 0 : osCode.hashCode());
        result = prime * result + ((osFamily == null) ? 0 : osFamily.hashCode());
        result = prime * result + ((osFamilyCode == null) ? 0 : osFamilyCode.hashCode());
        result = prime * result + ((osFamilyVendorHomepage == null) ? 0 : osFamilyVendorHomepage.hashCode());
        result = prime * result + ((osFamilyVendor == null) ? 0 : osFamilyVendor.hashCode());
        result = prime * result + ((osFamilyVendorCode == null) ? 0 : osFamilyVendorCode.hashCode());
        result = prime * result + ((osHomePage == null) ? 0 : osHomePage.hashCode());
        result = prime * result + ((osIcon == null) ? 0 : osIcon.hashCode());
        result = prime * result + ((osIconBig == null) ? 0 : osIconBig.hashCode());
        result = prime * result + ((osInfoUrl == null) ? 0 : osInfoUrl.hashCode());
        result = prime * result + ((ua == null) ? 0 : ua.hashCode());
        result = prime * result + ((uaClass == null) ? 0 : uaClass.hashCode());
        result = prime * result + ((uaClassCode == null) ? 0 : uaClassCode.hashCode());
        result = prime * result + ((uaEngine == null) ? 0 : uaEngine.hashCode());
        result = prime * result + ((uaFamily == null) ? 0 : uaFamily.hashCode());
        result = prime * result + ((uaFamilyCode == null) ? 0 : uaFamilyCode.hashCode());
        result = prime * result + ((uaFamilyHomepage == null) ? 0 : uaFamilyHomepage.hashCode());
        result = prime * result + ((uaFamilyIcon == null) ? 0 : uaFamilyIcon.hashCode());
        result = prime * result + ((uaFamilyIconBig == null) ? 0 : uaFamilyIconBig.hashCode());
        result = prime * result + ((uaFamilyInfoUrl == null) ? 0 : uaFamilyInfoUrl.hashCode());
        result = prime * result + ((uaFamilyVendor == null) ? 0 : uaFamilyVendor.hashCode());
        result = prime * result + ((uaFamilyVendorCode == null) ? 0 : uaFamilyVendorCode.hashCode());
        result = prime * result + ((uaFamilyVendorHomepage == null) ? 0 : uaFamilyVendorHomepage.hashCode());
        result = prime * result + ((uaString == null) ? 0 : uaString.hashCode());
        result = prime * result + ((uaUptodateCurrentVersion == null) ? 0 : uaUptodateCurrentVersion.hashCode());
        result = prime * result + ((uaVersion == null) ? 0 : uaVersion.hashCode());
        result = prime * result + ((uaVersionMajor == null) ? 0 : uaVersionMajor.hashCode());
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
        UdgerUaResult other = (UdgerUaResult) obj;
        if (classId == null) {
            if (other.classId != null)
                return false;
        } else if (!classId.equals(other.classId))
            return false;
        if (clientId == null) {
            if (other.clientId != null)
                return false;
        } else if (!clientId.equals(other.clientId))
            return false;
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
        if (crawlerLastSeen == null) {
            if (other.crawlerLastSeen != null)
                return false;
        } else if (!crawlerLastSeen.equals(other.crawlerLastSeen))
            return false;
        if (crawlerRespectRobotstxt == null) {
            if (other.crawlerRespectRobotstxt != null)
                return false;
        } else if (!crawlerRespectRobotstxt.equals(other.crawlerRespectRobotstxt))
            return false;
        if (deviceBrand == null) {
            if (other.deviceBrand != null)
                return false;
        } else if (!deviceBrand.equals(other.deviceBrand))
            return false;
        if (deviceBrandCode == null) {
            if (other.deviceBrandCode != null)
                return false;
        } else if (!deviceBrandCode.equals(other.deviceBrandCode))
            return false;
        if (deviceBrandHomepage == null) {
            if (other.deviceBrandHomepage != null)
                return false;
        } else if (!deviceBrandHomepage.equals(other.deviceBrandHomepage))
            return false;
        if (deviceBrandIcon == null) {
            if (other.deviceBrandIcon != null)
                return false;
        } else if (!deviceBrandIcon.equals(other.deviceBrandIcon))
            return false;
        if (deviceBrandIconBig == null) {
            if (other.deviceBrandIconBig != null)
                return false;
        } else if (!deviceBrandIconBig.equals(other.deviceBrandIconBig))
            return false;
        if (deviceBrandInfoUrl == null) {
            if (other.deviceBrandInfoUrl != null)
                return false;
        } else if (!deviceBrandInfoUrl.equals(other.deviceBrandInfoUrl))
            return false;
        if (deviceClass == null) {
            if (other.deviceClass != null)
                return false;
        } else if (!deviceClass.equals(other.deviceClass))
            return false;
        if (deviceClassCode == null) {
            if (other.deviceClassCode != null)
                return false;
        } else if (!deviceClassCode.equals(other.deviceClassCode))
            return false;
        if (deviceClassIcon == null) {
            if (other.deviceClassIcon != null)
                return false;
        } else if (!deviceClassIcon.equals(other.deviceClassIcon))
            return false;
        if (deviceClassIconBig == null) {
            if (other.deviceClassIconBig != null)
                return false;
        } else if (!deviceClassIconBig.equals(other.deviceClassIconBig))
            return false;
        if (deviceClassInfoUrl == null) {
            if (other.deviceClassInfoUrl != null)
                return false;
        } else if (!deviceClassInfoUrl.equals(other.deviceClassInfoUrl))
            return false;
        if (deviceMarketname == null) {
            if (other.deviceMarketname != null)
                return false;
        } else if (!deviceMarketname.equals(other.deviceMarketname))
            return false;
        if (os == null) {
            if (other.os != null)
                return false;
        } else if (!os.equals(other.os))
            return false;
        if (osCode == null) {
            if (other.osCode != null)
                return false;
        } else if (!osCode.equals(other.osCode))
            return false;
        if (osFamily == null) {
            if (other.osFamily != null)
                return false;
        } else if (!osFamily.equals(other.osFamily))
            return false;
        if (osFamilyCode == null) {
            if (other.osFamilyCode != null)
                return false;
        } else if (!osFamilyCode.equals(other.osFamilyCode))
            return false;
        if (osFamilyVendorHomepage == null) {
            if (other.osFamilyVendorHomepage != null)
                return false;
        } else if (!osFamilyVendorHomepage.equals(other.osFamilyVendorHomepage))
            return false;
        if (osFamilyVendor == null) {
            if (other.osFamilyVendor != null)
                return false;
        } else if (!osFamilyVendor.equals(other.osFamilyVendor))
            return false;
        if (osFamilyVendorCode == null) {
            if (other.osFamilyVendorCode != null)
                return false;
        } else if (!osFamilyVendorCode.equals(other.osFamilyVendorCode))
            return false;
        if (osHomePage == null) {
            if (other.osHomePage != null)
                return false;
        } else if (!osHomePage.equals(other.osHomePage))
            return false;
        if (osIcon == null) {
            if (other.osIcon != null)
                return false;
        } else if (!osIcon.equals(other.osIcon))
            return false;
        if (osIconBig == null) {
            if (other.osIconBig != null)
                return false;
        } else if (!osIconBig.equals(other.osIconBig))
            return false;
        if (osInfoUrl == null) {
            if (other.osInfoUrl != null)
                return false;
        } else if (!osInfoUrl.equals(other.osInfoUrl))
            return false;
        if (ua == null) {
            if (other.ua != null)
                return false;
        } else if (!ua.equals(other.ua))
            return false;
        if (uaClass == null) {
            if (other.uaClass != null)
                return false;
        } else if (!uaClass.equals(other.uaClass))
            return false;
        if (uaClassCode == null) {
            if (other.uaClassCode != null)
                return false;
        } else if (!uaClassCode.equals(other.uaClassCode))
            return false;
        if (uaEngine == null) {
            if (other.uaEngine != null)
                return false;
        } else if (!uaEngine.equals(other.uaEngine))
            return false;
        if (uaFamily == null) {
            if (other.uaFamily != null)
                return false;
        } else if (!uaFamily.equals(other.uaFamily))
            return false;
        if (uaFamilyCode == null) {
            if (other.uaFamilyCode != null)
                return false;
        } else if (!uaFamilyCode.equals(other.uaFamilyCode))
            return false;
        if (uaFamilyHomepage == null) {
            if (other.uaFamilyHomepage != null)
                return false;
        } else if (!uaFamilyHomepage.equals(other.uaFamilyHomepage))
            return false;
        if (uaFamilyIcon == null) {
            if (other.uaFamilyIcon != null)
                return false;
        } else if (!uaFamilyIcon.equals(other.uaFamilyIcon))
            return false;
        if (uaFamilyIconBig == null) {
            if (other.uaFamilyIconBig != null)
                return false;
        } else if (!uaFamilyIconBig.equals(other.uaFamilyIconBig))
            return false;
        if (uaFamilyInfoUrl == null) {
            if (other.uaFamilyInfoUrl != null)
                return false;
        } else if (!uaFamilyInfoUrl.equals(other.uaFamilyInfoUrl))
            return false;
        if (uaFamilyVendor == null) {
            if (other.uaFamilyVendor != null)
                return false;
        } else if (!uaFamilyVendor.equals(other.uaFamilyVendor))
            return false;
        if (uaFamilyVendorCode == null) {
            if (other.uaFamilyVendorCode != null)
                return false;
        } else if (!uaFamilyVendorCode.equals(other.uaFamilyVendorCode))
            return false;
        if (uaFamilyVendorHomepage == null) {
            if (other.uaFamilyVendorHomepage != null)
                return false;
        } else if (!uaFamilyVendorHomepage.equals(other.uaFamilyVendorHomepage))
            return false;
        if (uaString == null) {
            if (other.uaString != null)
                return false;
        } else if (!uaString.equals(other.uaString))
            return false;
        if (uaUptodateCurrentVersion == null) {
            if (other.uaUptodateCurrentVersion != null)
                return false;
        } else if (!uaUptodateCurrentVersion.equals(other.uaUptodateCurrentVersion))
            return false;
        if (uaVersion == null) {
            if (other.uaVersion != null)
                return false;
        } else if (!uaVersion.equals(other.uaVersion))
            return false;
        if (uaVersionMajor == null) {
            if (other.uaVersionMajor != null)
                return false;
        } else if (!uaVersionMajor.equals(other.uaVersionMajor))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UdgerUaResult [" +
                "uaString=" + uaString +
                ", clientId=" + clientId +
                ", classId=" + classId +
                ", uaClass=" + uaClass +
                ", uaClassCode=" + uaClassCode +
                ", ua=" + ua +
                ", uaEngine=" + uaEngine +
                ", uaVersion=" + uaVersion +
                ", uaVersionMajor=" + uaVersionMajor +
                ", crawlerLastSeen=" + crawlerLastSeen +
                ", crawlerRespectRobotstxt=" + crawlerRespectRobotstxt +
                ", crawlerCategory=" + crawlerCategory +
                ", crawlerCategoryCode=" + crawlerCategoryCode +
                ", uaUptodateCurrentVersion=" + uaUptodateCurrentVersion +
                ", uaFamily=" + uaFamily +
                ", uaFamilyCode=" + uaFamilyCode +
                ", uaFamilyHomepage=" + uaFamilyHomepage +
                ", uaFamilyIcon=" + uaFamilyIcon +
                ", uaFamilyIconBig=" + uaFamilyIconBig +
                ", uaFamilyVendor=" + uaFamilyVendor +
                ", uaFamilyVendorCode=" + uaFamilyVendorCode +
                ", uaFamilyVendorHomepage=" + uaFamilyVendorHomepage +
                ", uaFamilyInfoUrl=" + uaFamilyInfoUrl +
                ", osFamily=" + osFamily +
                ", osFamilyCode=" + osFamilyCode +
                ", os=" + os +
                ", osCode=" + osCode +
                ", osHomePage=" + osHomePage +
                ", osIcon=" + osIcon +
                ", osIconBig=" + osIconBig +
                ", osFamilyVendor=" + osFamilyVendor +
                ", osFamilyVendorCode=" + osFamilyVendorCode +
                ", osFamilyVendorHomepage=" + osFamilyVendorHomepage +
                ", osInfoUrl=" + osInfoUrl +
                ", deviceClass=" + deviceClass +
                ", deviceClassCode=" + deviceClassCode +
                ", deviceClassIcon=" + deviceClassIcon +
                ", deviceClassIconBig=" + deviceClassIconBig +
                ", deviceClassInfoUrl=" + deviceClassInfoUrl +
                ", deviceMarketname=" + deviceMarketname +
                ", deviceBrand=" + deviceBrand +
                ", deviceBrandCode=" + deviceBrandCode +
                ", deviceBrandHomepage=" + deviceBrandHomepage +
                ", deviceBrandIcon=" + deviceBrandIcon +
                ", deviceBrandIconBig=" + deviceBrandIconBig +
                ", deviceBrandInfoUrl=" + deviceBrandInfoUrl +
                "]";
    }

}

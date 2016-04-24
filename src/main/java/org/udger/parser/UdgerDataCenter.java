package org.udger.parser;

import java.io.Serializable;

public class UdgerDataCenter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String dataCenterName;
    private String dataCenterNameCode;
    private String dataCenterHomePage;

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

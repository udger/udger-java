/*
  Udger-update - Data updater for udger local and cloud parser

  author     The Udger.com Team (info@udger.com)
  copyright  Copyright (c) Udger s.r.o.
  license    GNU Lesser General Public License
  link       https://udger.com/products
*/
package org.udger.parser;

import java.io.Serializable;

public class UdgerUaQueryResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private UdgerUa userAgent;

    private UdgerOs os;

    private UdgerDevice device;

    public UdgerUa getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(UdgerUa userAgent) {
        this.userAgent = userAgent;
    }

    public UdgerOs getOs() {
        return os;
    }

    public void setOs(UdgerOs os) {
        this.os = os;
    }

    public UdgerDevice getDevice() {
        return device;
    }

    public void setDevice(UdgerDevice device) {
        this.device = device;
    }

}

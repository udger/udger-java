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

    private UdgerUa ua;

    private UdgerOs os;

    private UdgerDevice device;

    public UdgerUa getUa() {
        return ua;
    }

    public void setUa(UdgerUa ua) {
        this.ua = ua;
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

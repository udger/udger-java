/*
  Udger-update - Data updater for udger local and cloud parser

  author     The Udger.com Team (info@udger.com)
  copyright  Copyright (c) Udger s.r.o.
  license    GNU Lesser General Public License
  link       https://udger.com/products
*/
package org.udger.parser;

import java.io.Serializable;

public class UdgerIpQueryResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private UdgerIp ip;

    public UdgerIp getIp() {
        return ip;
    }

    public void setIp(UdgerIp ip) {
        this.ip = ip;
    }
}

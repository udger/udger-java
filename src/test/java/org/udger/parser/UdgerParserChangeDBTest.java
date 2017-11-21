package org.udger.parser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import org.junit.Test;

public class UdgerParserChangeDBTest {

    @Test
    public void testUaString1() throws SQLException, IOException {
        String uaQuery = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0";
        URL resource = this.getClass().getClassLoader().getResource("udgerdb_test_v3.dat");
        try (UdgerParser parser = new UdgerParser(resource.getFile())) {

          UdgerUaResult qr = parser.parseUa(uaQuery);
          assertEquals(qr.getUaUptodateCurrentVersion(), "50");
        }

        UdgerParser.resetParser();

        URL resource2 = this.getClass().getClassLoader().getResource("udgerdb_test_v3_switch.dat");
        try (UdgerParser parser2 = new UdgerParser(resource2.getFile())) {
          UdgerUaResult qr2 = parser2.parseUa(uaQuery);
          assertEquals(qr2.getUaUptodateCurrentVersion(), "50X");
        }
    }
}

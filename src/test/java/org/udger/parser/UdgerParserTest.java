package org.udger.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UdgerParserTest {

    private UdgerParser parser;
    private UdgerParser inMemoryParser;
    private UdgerParser.ParserDbData parserDbData;

    @Before
    public void initialize() throws SQLException {
        URL resource = this.getClass().getClassLoader().getResource("udgerdb_test_v3.dat");
        parserDbData = new UdgerParser.ParserDbData(resource.getFile());
        parser = new UdgerParser(parserDbData);
        inMemoryParser = new UdgerParser(parserDbData, true, 0); // no cache
    }

    @After
    public void close() throws IOException {
        parser.close();
    }

    @Test
    public void testUaString1() throws SQLException {
        String uaQuery = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0";
        UdgerUaResult qr = parser.parseUa(uaQuery);
        assertEquals(qr.getUa(), "Firefox 40.0");
        assertEquals(qr.getOs(), "Windows 10");
        assertEquals(qr.getUaFamily(), "Firefox");
    }

    @Test
    public void testIp() throws SQLException, UnknownHostException {
        String ipQuery = "108.61.199.93";
        UdgerIpResult qr = parser.parseIp(ipQuery);
        assertEquals(qr.getIpClassificationCode(), "crawler");
    }

    @Test
    public void testUaStringInMemoryParser() throws SQLException {
        String uaQuery = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0";
        UdgerUaResult qr = inMemoryParser.parseUa(uaQuery);
        assertEquals(qr.getUa(), "Firefox 40.0");
        assertEquals(qr.getOs(), "Windows 10");
        assertEquals(qr.getUaFamily(), "Firefox");
    }

    @Test
    public void testIpInMemoryParser() throws SQLException, UnknownHostException {
        String ipQuery = "108.61.199.93";
        UdgerIpResult qr = inMemoryParser.parseIp(ipQuery);
        assertEquals(qr.getIpClassificationCode(), "crawler");
    }

    @Test
    public void testParserDbDataThreadSafety() throws Throwable {
        final int numThreads = 500;
        final String uaQuery = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:40.0) Gecko/20100101 Firefox/40.0";

        final CyclicBarrier gate = new CyclicBarrier(numThreads);
        final ConcurrentLinkedQueue<Throwable> failures = new ConcurrentLinkedQueue<>();

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    UdgerParser threadParser = new UdgerParser(parserDbData);
                    try {
                        gate.await();
                        for (int j = 0; j < 100; j++) {
                            UdgerUaResult qr = threadParser.parseUa(uaQuery);
                            assertEquals(qr.getUa(), "Firefox 40.0");
                            assertEquals(qr.getOs(), "Windows 10");
                            assertEquals(qr.getUaFamily(), "Firefox");
                        }
                    } catch (Throwable t) {
                        failures.add(t);
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        if (!failures.isEmpty()) {
            for (Throwable throwable : failures) {
                throwable.printStackTrace();
            }

            fail("Parsing threads failed, see printed exceptions");
        }
    }
}

package org.udger.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class UdgerPerformanceTest {

    private static JsonArray jsonArray;
    private static UdgerParser[] POOL;

    private static void createPool() {
        POOL = new UdgerParser[10];
        for (int i=0; i<=9; i++) {
            POOL[i] = new UdgerParser(new UdgerParser.ParserDbData("udgerdb_v3_" + i + ".dat"), -1);
        }
    }

    private static void closePool() throws IOException {
        for (int i=0; i<=9; i++) {
            if (POOL[i] != null) {
                POOL[i].close();
            }
        }
    }

    public static void main(String args[]) {
        testUaTxt();
    }

    private static void testUaTxt() {
        InputStream is = UdgerUaTest.class.getResourceAsStream("random_ua.txt");

        List<String> uaStringList = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                uaStringList.add(line);
            }
        } catch (IOException e) {
            System.out.println("There was a problem: " + e);
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
        for (int i=0; i<2; i++) {
            doTestUaTxt(uaStringList);
        }
    }

    private static void doTestUaTxt(List<String> uaStringList) {
        UdgerParser up = null;
        try {
            up = new UdgerParser(new UdgerParser.ParserDbData("/home/lada/Download/udgerdb_v3.dat"), -1);
            long tm = 0;
            long tmPrev = 0;
            int x = 0;
            for (String query : uaStringList) {
                try {
                    long prev = System.nanoTime();
                    UdgerUaResult ret = up.parseUa(query);
                    tm += System.nanoTime() - prev;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                x ++;
                if (x % 1000 == 0) {
                    up.printPerformanceData();
                    up.clearPerformanceData();
                    long t = tm - tmPrev;
                    System.out.println("TOTAL Queries: " + 1000 + " time : " + t / 1000000 + "ms AVG : " + 1000000000 * 1000.0 / t + "/s");
                    tmPrev = tm;
                }
            }
//            long numQueries = uaStringList.size();
//            System.out.println("TOTAL Queries: " + numQueries + " time : " + tm / 1000000 + "ms AVG : " + 1000000000 * numQueries / (float) tm + "/s");
//            up.printPerformanceData();
        } finally {
            if (up != null) {
                try {
                    up.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void testJson() {
        InputStream is = UdgerUaTest.class.getResourceAsStream("test_ua.json");
        JsonReader jsonReader = javax.json.Json.createReader(is);
        jsonArray = jsonReader.readArray();
        for (int i=0; i<10; i++) {
            System.out.println("### Test : " + (i+1));
            testSerial();
        }
    }

    private static void testSerial() {
        UdgerParser up = null;
        try {
            up = new UdgerParser(new UdgerParser.ParserDbData("/home/lada/Download/udgerdb_v3.dat"), -1);
            long tm = 0;
            for (int j=0; j<100; j++) {
                for (int i=0; i < jsonArray.size(); i++) {
                    JsonObject jar = jsonArray.getJsonObject(i);
                    String query = jar.getJsonObject("test").getString("teststring");
                    try {
                        long prev = System.nanoTime();
                        UdgerUaResult ret = up.parseUa(query);
                        tm += System.nanoTime() - prev;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            long numQueries = 100 * jsonArray.size();
            System.out.println("TOTAL Queries: " + numQueries + " time : " + tm / 1000000 + "ms AVG : " + 1000000000 * numQueries / (float) tm + "/s");
            up.printPerformanceData();
        } finally {
            if (up != null) {
                try {
                    up.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void testParallel() {
        try {
            createPool();
            long tm = 0;
            System.out.println("TOTAL Queries: " + 100 * jsonArray.size() + " time : " + tm + " AVG : " + 1000 * 100 * jsonArray.size() / (float) tm + "/s");
        } finally {
                try {
                    closePool();
                } catch (IOException e) {
                }
        }
    }

}

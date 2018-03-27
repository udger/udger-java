package org.udger.parser;

import java.util.HashMap;
import java.util.Map;

public class PerformanceData {

    private long callCount;
    private long findWordsTime;
    private long substrCheckCount;
    private long regexArrayParams;
    private int regexCount;

    private final Map<String, Long> countMap = new HashMap<>();
    private final Map<String, Long> timeMap = new HashMap<>();

    public void incCallCount() {
        callCount ++;
    }

    public void incRegexCount() {
        regexCount ++;
    }

    public void incSubstrChecks() {
        substrCheckCount ++;
    }

    public void addFindWordsTime(long addTime) {
        findWordsTime += addTime;
    }

    public void addRegexArrayParams(long addParams) {
        regexArrayParams += addParams;
    }

    public int getRegexCount() {
        return regexCount;
    }

    public void print(int regexCacheSize) {
        long tm = 0;
        tm += printPreparedStmtTime("sqlCrawler");
        tm += printPreparedStmtTime("sqlClient");
        tm += printPreparedStmtTime("sqlOs");
        tm += printPreparedStmtTime("sqlClientOs");
        tm += printPreparedStmtTime("sqlDevice");
        tm += printPreparedStmtTime("sqlClientClass");
        tm += printPreparedStmtTime("sqlIp");
        tm += printPreparedStmtTime("sqlDataCenter");
        tm += printPreparedStmtTime("SqlDataCenterRange6");
        tm += printPreparedStmtTime("sqlDeviceName");
        System.out.println("SQL total : " + tm + "ns");
        System.out.println("REGEX Count :" + regexCount);
        System.out.println("REGEX Cache size :" + regexCacheSize);
        System.out.println("Call count : " + callCount);
        System.out.println("Find words : " + findWordsTime / (float) callCount + "ns");
        System.out.println("SubstrChecks : " + substrCheckCount);
        System.out.println("Regex params : " + regexArrayParams);
    }

    private long printPreparedStmtTime(String sqlId) {
        Long time = timeMap.get(sqlId);
        if (time == null)
            time = 0L;
        Long count = countMap.get(sqlId);
        if (count == null)
            count = 0L;
        double avg = 0.0;
        if (count != 0)
            avg = time / (double) count;
        System.out.println(String.format("%.1f", avg) + "ns /" + count + "/" + time + " : "+ sqlId);
        return time;
    }

    public void addTime(String sqlId, long time) {
        Long total = timeMap.get(sqlId);
        total = (total != null) ? (total + time) : time;
        timeMap.put(sqlId, total);
    }

    public void incCount(String sqlId) {
        Long count = countMap.get(sqlId);
        countMap.put(sqlId, (count != null ? count + 1 : 1));
    }

    public void clear() {
        timeMap.clear();
        countMap.clear();
        findWordsTime = 0;
        callCount = 0;
        regexCount = 0;
    }

}

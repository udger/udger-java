package org.udger.parser;

import java.util.HashMap;
import java.util.Map;

public class PerformanceData {

    private long callCount;
    private long findWordsTime;
    private long substrCheckCount;
    private long regexArrayParams;

    private final Map<String, Long> countMap = new HashMap<>();
    private final Map<String, Long> timeMap = new HashMap<>();

    public void incCallCount() {
        callCount ++;
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

    public void print(int regexCount, int regexCacheSize) {
        long tm = 0;
        tm += printPreparedStmtTime("sqlCrawler");
        tm += printPreparedStmtTime("sqlClient");
        tm += printPreparedStmtTime("sqlOs");
        tm += printPreparedStmtTime("sqlClientOs");
        tm += printPreparedStmtTime("sqlDevice");
        tm += printPreparedStmtTime("sqlClientClass");
        tm += printPreparedStmtTime("sqlIp");
        tm += printPreparedStmtTime("sqlDataCenter");
        System.out.println("SQL total : " + tm);
        System.out.println("REGEX Count :" + regexCount);
        System.out.println("REGEX Cache size :" + regexCacheSize);
        System.out.println("Call count : " + callCount);
        System.out.println("Find words : " + findWordsTime / (float) callCount);
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
        System.out.println(avg + "ns : " + sqlId);
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

}

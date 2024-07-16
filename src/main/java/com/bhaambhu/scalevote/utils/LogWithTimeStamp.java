package com.bhaambhu.scalevote.utils;

public class LogWithTimeStamp {
    private Long actionTS = null;
    private Long overallTS = null;

    public LogWithTimeStamp() {
        actionTS = System.currentTimeMillis();
        overallTS = System.currentTimeMillis();
    }

    public LogWithTimeStamp(String action) {
        actionTS = System.currentTimeMillis();
        overallTS = System.currentTimeMillis();
        System.out.println(action);
    }

    public LogWithTimeStamp(String action, int level) {
        actionTS = System.currentTimeMillis();
        overallTS = System.currentTimeMillis();
        for (int i = 0; i < level; i++) {
            action = "\t" + action;
        }
        System.out.println(action);
    }

    public void log(String action) {
        System.out.println(action + " : took " + (System.currentTimeMillis() - actionTS) + " ms, overall "
                + (System.currentTimeMillis() - overallTS));
        actionTS = System.currentTimeMillis();
    }

    public void log(String action, int level) {
        for (int i = 0; i < level; i++) {
            action = "\t" + action;
        }
        log(action);
    }
}

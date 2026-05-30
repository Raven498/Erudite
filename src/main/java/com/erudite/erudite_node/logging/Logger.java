package com.erudite.erudite_node.logging;

import com.erudite.erudite_node.model.Environment;
import com.erudite.erudite_node.model.Knowledge;
import java.util.ArrayList;
import java.io.*;
import java.util.List;

public class Logger {
    private static ArrayList<String> logs = new ArrayList<>();
    private static int logFileNumber = 1;
    private static final String LOG_FILE_NAME_STEM = "src\\main\\java\\com\\erudite\\erudite_node\\logging\\logs\\log-%d.txt";

    private static final String ITERATION_START = """
           ROUND: %d
           CURRENT TIME: %d MIN
           TIME DIFF: %d MIN
           DELAY: %b
           """;

    private static final String DELAY = """
            DELAYING DUE TO 418 TEAPOT:
            CURRENT TIME: %d MIN
            TIME DIFF: %d MIN
            DELAY FOR: %d MIN
            """;

    private static final String OVER = """
            OVER
            ROUND: %d
            CURRENT TIME: %d MIN
            DELAY: %b
            # TODO: add stats from Director, EpsilonManager, AccumulationManager, AKB
            """;

    private static final String SELECTIVE_ENV_REPORT_START = """
            ------->SELECTIVE ENV REPORT:
            """;

    private static final String SELECTIVE_ENV_REPORT_END = """
            ------->END SELECTIVE ENV REPORT:
            """;

    private static final String FULL_ENV_REPORT_START = """
            ------->FULL ENV REPORT:
            """;

    private static final String FULL_ENV_REPORT_END = """
            ------->END FULL ENV REPORT:
            """;

    private static final String ENV_REPORT_LOG_STEM = """
            K%d (env ID %s): %s
            """;

    public static void printIterationStart(int round, int currentTime, int timeDiff, boolean delay){
        System.out.println(String.format(ITERATION_START, round, currentTime, timeDiff, delay));
    }

    public static void logIterationStart(int round, int currentTime, int timeDiff, boolean delay){
        logs.add(String.format(ITERATION_START, round, currentTime, timeDiff, delay));
    }

    public static void printDelay(int currentTime, int timeDiff, int delayTime){
        System.out.println(String.format(ITERATION_START, currentTime, timeDiff, timeDiff, delayTime));
    }

    public static void logDelay(int currentTime, int timeDiff, int delayTime){
        logs.add(String.format(ITERATION_START, currentTime, timeDiff, timeDiff, delayTime));
    }

    public static void printLogs(){
        for(String log : logs){
            System.out.println(log);
        }
    }

    public static void flushLogs() {
        logs.clear();
    }

    // full env report
    public static void envReport(Environment env) {
        logs.add(FULL_ENV_REPORT_START);
        for (Knowledge k : env.getKnowledge()) {
            logs.add(String.format(ENV_REPORT_LOG_STEM, env.getKnowledge().indexOf(k), k.content, k.toString()));
        }
        logs.add(FULL_ENV_REPORT_END);
    }

    // selective env report
    public static void envReport(Environment env, List<String> selections) {
        logs.add(SELECTIVE_ENV_REPORT_START);
        for (Knowledge k : env.getKnowledge()) {
            if (selections.contains(k.content)) {
                logs.add(String.format(ENV_REPORT_LOG_STEM, env.getKnowledge().indexOf(k), k.content, k.toString()));
            }
        }
        logs.add(SELECTIVE_ENV_REPORT_END);
    }

    // Write to a file
    public static void saveLogs(){
        System.out.println(new File(".").getAbsolutePath());
        try(PrintWriter pw = new PrintWriter(new FileOutputStream(String.format(LOG_FILE_NAME_STEM, logFileNumber)))){ // TODO: need to parse timestamp to remove prohibited characters (ex. colons)
            for(String log : logs){
                pw.write(log);
            }
            pw.flush();
            logFileNumber += 1;
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}

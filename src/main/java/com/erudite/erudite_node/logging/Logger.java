package com.erudite.erudite_node.logging;

import java.time.LocalTime;
import java.util.ArrayList;
import java.io.*;
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

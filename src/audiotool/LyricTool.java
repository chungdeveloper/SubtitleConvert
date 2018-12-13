/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package audiotool;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSi-Gaming
 */
public class LyricTool {

    private List<File> mFiles;
    private List<String> mCurrentLines;
    private Thread mMainThread;
    private OnLyricListener listener;

    public interface OnLyricListener {

        void onSuccess(String lyric);

        void onFail(String msg);
    }

    public LyricTool() {
        mFiles = new ArrayList<>();
        mCurrentLines = new ArrayList<>();
    }

    public void processInputFile(List<File> files, OnLyricListener listener) {
        this.listener = listener;
        mFiles.clear();
        mFiles.addAll(files);

        mMainThread = new Thread(mMainRunnable);
        mMainThread.start();
    }

    private Runnable mMainRunnable = new Runnable() {
        @Override
        public void run() {
            for (File file : mFiles) {
                processFile(file);
            }
        }
    };

    private void processFile(File file) {
        try {
            listener.onSuccess(readFile(file));
        } catch (Exception ex) {
            listener.onFail(file.getName() + " - fail: " + ex.toString());
        }
    }

    private String readFile(File file) throws IOException {
        String uri;
        mCurrentLines.clear();
        mCurrentLines.addAll(Files.readAllLines(file.toPath(), Charset.forName("UTF-8")));
//        mCurrentLines.add("Dialogue: 0,0:00:40.83,0:00:42.05,Default,,0,0,0,,{\\k22}F: và {\\k31}đôi {\\k30}mắt {\\k39}em");

        File dir = new File(file.getParent() + "/lyricTrans");
        if (!dir.exists()) {
            dir.mkdir();
        }
        uri = file.getParent() + "/lyricTrans/" + file.getName().replace(".ass", ".lrc");
        PrintWriter writer = new PrintWriter(uri, "UTF-8");
        boolean hasMissing = false;
        for (String line : mCurrentLines) {
            if (!line.startsWith("Dialogue")) {
                continue;
            }

            hasMissing = hasMissing ? hasMissing : (hasMissing = hasMissing(line));
            writer.println(processLine(line));
        }
        writer.close();
        return uri + (hasMissing ? CONTENT_TAG : "");
    }

    public static final String CONTENT_TAG = "[missing expected chungld]";

    private boolean hasMissing(String line) {
        return line.split(",").length > 10;
    }

    private int startTime;

    private String processLine(String line) {
        String result;
        String[] entry = line.split(",");
        startTime = getTimeFromString(entry[1]);
        String content = getContentString(entry).replace("\\k", "");// entry[9].replace("\\k", "");
        String[] contentEntry = content.split("\\{");
        result = packageStartTime(getTimeFromInt(startTime));
        for (String string : contentEntry) {
            result += processEntryWord(string);
        }
        System.out.println("result: " + result);
        return result;
    }

    private String getContentString(String[] entry) {
        if (entry.length < 10) {
            return "";
        }
        String content = entry[9];
        for (int i = 10; i < entry.length; i++) {
            content += "," + entry[i];
        }
        return content;
    }

    private String processEntryWord(String entry) {
        String[] entries = entry.split("\\}");
        if (!entries[0].equals("")) {
            startTime += convertStringToInt(entries[0]) * 10;
        }

        System.out.println("StartTime: " + startTime);

        if (entries.length < 2) {
            return "";
        }

        return entries[1] + packageTimeEntry(getTimeFromInt(startTime));
    }

    private String packageString(String value, String symbolS, String symbolE) {
        return symbolS + value + symbolE;
    }

    private String packageStartTime(String value) {
        return packageString(value, "[", "]");
    }

    private String packageTimeEntry(String value) {
        return packageString(value, "<", ">");
    }

    private int getTimeFromString(String time) {
        String[] times = time.split(":");
        return convertStringToInt(times[0]) * 60 * 60 * 1000 + convertStringToInt(times[1]) * 60 * 1000 + convertStringToInt(times[2].replace(".", "") + "0");
    }

    private String getTimeFromInt(int time) {
        float second = time * 1.0F / 1000;
        int millis = time % 1000;
        int minutes = (int) (second / 60);

        second = second % 60;
        return String.format("%02d:%02.03f", minutes, second/*, millis /*< 100 ? millis * 10 : millis*/);
    }

    private int convertStringToInt(String value) {
        return Integer.parseInt(value);
    }
}

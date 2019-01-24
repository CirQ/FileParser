package me.cirq.entity;

import me.cirq.util.FileReader;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Entity for crash stack based on text input
 */
public class CrashStack implements Iterable<SimpleFrame> {
    private static final Pattern EXCEPTION_PATTERN = Pattern.compile(
            "^(.*?) ?((?:\\w+\\.)+\\w+(?:Exception|Error)): (.*?)$");
    private static final Pattern FRAME_PATTERN = Pattern.compile(
            "^\\s+at ((?:[\\w\\$]+\\.)+[\\w\\$]+)\\((\\w+\\.java):(\\d+)\\)$");

    private String exception;
    private String message;

    private List<SimpleFrame> frames;

    private void initCrashStack(Iterator<String> it){
        String first_line = it.next();
        Matcher exception_matcher = EXCEPTION_PATTERN.matcher(first_line);
        if(exception_matcher.find()) {
            exception = exception_matcher.group(2);
            message = exception_matcher.group(3);
        }

        SimpleFrame previous = null;
        SimpleFrame current = null;

        while(it.hasNext()){
            String frame_text = it.next();
            Matcher frame_matcher = FRAME_PATTERN.matcher(frame_text);
            if(frame_matcher.find()) {
                String method = frame_matcher.group(1);
                String filename = frame_matcher.group(2);
                String line_number = frame_matcher.group(3);
                current = new SimpleFrame(method, filename, Integer.valueOf(line_number));
                frames.add(current);
                if(previous != null){
                    previous.setNext(current);
                    current.setPrev(previous);
                }
                previous = current;
            }
        }
    }

    public CrashStack(String stackTextFile) throws IOException {
        frames = new LinkedList<>();
        Iterable<String> it = FileReader.readAllLine(stackTextFile);
        initCrashStack(it.iterator());
    }

    public String getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public Iterator<SimpleFrame> iterator() {
        return frames.iterator();
    }

}

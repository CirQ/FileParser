package me.cirq;

import me.cirq.entity.CrashStack;
import me.cirq.entity.SimpleFrame;
import me.cirq.entity.SuspiciousBlock;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * For static analysis on control flow graph
 */
public class StackTracer {
    private CrashStack stack;
    private static Map<SimpleFrame, SuspiciousBlock> map;

    public StackTracer(CrashStack stack){
        this.stack = stack;
        map = new TreeMap<>();
    }

    public StackTracer(String stackTextFilename) throws IOException {
        this.stack = new CrashStack(stackTextFilename);
        map = new TreeMap<>();
    }

    public void analysis(){
        for(SimpleFrame frame: stack){
            SuspiciousBlock sb = new SuspiciousBlock(frame);
            map.put(frame, sb);
        }
        System.out.println(map);
    }




}

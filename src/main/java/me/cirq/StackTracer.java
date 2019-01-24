package me.cirq;

import me.cirq.entity.CrashStack;

import java.io.IOException;

/**
 * For static analysis on control flow graph
 */
public class StackTracer {
    CrashStack stack;

    public StackTracer(CrashStack stack){
        this.stack = stack;
    }

    public StackTracer(String stackTextFilename) throws IOException {
        this.stack = new CrashStack(stackTextFilename);
    }





}

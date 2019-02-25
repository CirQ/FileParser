package me.cirq;

import me.cirq.entity.CrashStack;
import me.cirq.entity.SimpleFrame;
import me.cirq.entity.SuspiciousBlock;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.InvokeExpr;
import soot.jimple.internal.JInvokeStmt;
import soot.toolkits.graph.Block;

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

        for(SuspiciousBlock sBlock: map.values()){
            Block block = sBlock.getBlock();
            for(Unit unit: block){
                if(unit instanceof JInvokeStmt){
                    InvokeExpr expr = ((JInvokeStmt)unit).getInvokeExpr();
                    SootMethod invokedMethod = expr.getMethod();
                    System.out.println(invokedMethod);
                }

            }
        }
    }




}

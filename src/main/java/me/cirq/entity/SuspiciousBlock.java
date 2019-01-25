package me.cirq.entity;

import me.cirq.GraphConstructor;
import soot.SootMethod;
import soot.toolkits.graph.Block;

public class SuspiciousBlock {
    private static GraphConstructor grapher = GraphConstructor.getInstance();

    private Block block;
    private SootMethod method;

    public SuspiciousBlock(SimpleFrame frame){
        block = grapher.getBlock(frame);
        method = grapher.getMethod(frame);
    }

    public Block getBlock() {
        return block;
    }

    public SootMethod getMethod() {
        return method;
    }

}

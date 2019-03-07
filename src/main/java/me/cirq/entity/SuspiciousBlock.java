package me.cirq.entity;

import me.cirq.GraphConstructor;
import soot.SootMethod;
import soot.toolkits.graph.Block;

import java.util.*;

/**
 * Contains the information of a method and a block that crash
 */
public class SuspiciousBlock {
    private static GraphConstructor grapher = GraphConstructor.getInstance();

    private Block block;
    private SootMethod method;
    private LinkedHashSet<Block> invokeChain;

    private void backwardReference(){
        LinkedList<Block> queue = new LinkedList<>();
        HashSet<Block> visited = new HashSet<>();
        LinkedList<Block> localChain = new LinkedList<>();
        queue.addLast(block);
        localChain.addFirst(block);
        while(true){
            Block thisBlock = queue.pollFirst();
            if(thisBlock == null)
                break;
            visited.add(thisBlock);

            ArrayList<Block> arr = new ArrayList<>(thisBlock.getPreds());
            ListIterator<Block> list = arr.listIterator(arr.size());
            while(list.hasPrevious()) {
                Block b = list.previous();
                localChain.addFirst(b);
                if(!visited.contains(b))
                    queue.addLast(b);
            }
        }
        invokeChain.addAll(localChain);
    }

    private void calculateFeatures(){
        // todo: calculate
    }

    public SuspiciousBlock(SimpleFrame frame){
        block = grapher.getBlock(frame);
        method = grapher.getMethod(frame);
        invokeChain = new LinkedHashSet<>();
        backwardReference();
        calculateFeatures();
    }

    public Block getBlock() {
        return block;
    }

    public SootMethod getMethod() {
        return method;
    }

    public LinkedHashSet<Block> getInvokeChain() {
        return invokeChain;
    }
}

package me.cirq.entity;

import me.cirq.GraphConstructor;
import soot.SootMethod;
import soot.toolkits.graph.Block;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

/**
 * Contains the information of a method and a block that crash
 */
public class SuspiciousBlock {
    private static GraphConstructor grapher = GraphConstructor.getInstance();

    private Block block;
    private SootMethod method;
    private LinkedHashSet<Block> predecessor;

    private void backwardReference(){
        LinkedList<Block> queue = new LinkedList<>();
        HashSet<Block> visited = new HashSet<>();
        queue.addLast(block);
        while(true){
            Block thisBlock = queue.pollFirst();
            if(thisBlock == null)
                break;
            visited.add(thisBlock);
            for(Block b: thisBlock.getPreds()){
                predecessor.add(b);
                if(!visited.contains(b))
                    queue.addLast(b);
            }
        }
    }

    public SuspiciousBlock(SimpleFrame frame){
        block = grapher.getBlock(frame);
        method = grapher.getMethod(frame);
        predecessor = new LinkedHashSet<>();
        backwardReference();
    }

    public Block getBlock() {
        return block;
    }

    public SootMethod getMethod() {
        return method;
    }

}

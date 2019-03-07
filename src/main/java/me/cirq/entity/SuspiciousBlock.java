package me.cirq.entity;

import me.cirq.GraphConstructor;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.internal.JimpleLocal;
import soot.tagkit.LineNumberTag;
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

    private void backwardControlflow(){
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

    public void backwordSlicing(int line){
        Unit targetUnit = null;
        for(Unit unit: block){
            LineNumberTag tag = (LineNumberTag)unit.getTag("LineNumberTag");
            if(tag!=null && tag.getLineNumber()==line){
                targetUnit = unit;
            }
        }

        HashSet<JimpleLocal> uses = new HashSet<>();
        for(ValueBox value: targetUnit.getUseBoxes()){
            Value v = value.getValue();
            if(v instanceof JimpleLocal)
                uses.add((JimpleLocal)v);
        }
        int lastSize = uses.size();
        while(true){
            for(Unit unit: block) {
                for(ValueBox value: unit.getUseBoxes()) {
                    Value v = value.getValue();
                    if (v instanceof JimpleLocal)
                        uses.add((JimpleLocal) v);
                }
            }
            if(uses.size() == lastSize)
                break;
            lastSize = uses.size();
        }

        LinkedList<Unit> unitList = new LinkedList<>();
        for(Unit unit: block)
            unitList.add(unit);
        loopBlock: for(Unit unit: unitList){
            if(unit.equals(targetUnit))
                continue;
            for(ValueBox value: unit.getUseAndDefBoxes()){
                Value v = value.getValue();
                if(v instanceof JimpleLocal){
                    if(uses.contains(v))
                        continue loopBlock;
                }
            }

            // it mean this block is never def/use desired locals
            block.remove(unit);
        }
    }

    private void calculateFeatures(){
        // todo: calculate
    }

    public SuspiciousBlock(SimpleFrame frame){
        block = grapher.getBlock(frame);
        method = grapher.getMethod(frame);
        invokeChain = new LinkedHashSet<>();
        backwardControlflow();
        backwordSlicing(frame.getLineNumber());
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

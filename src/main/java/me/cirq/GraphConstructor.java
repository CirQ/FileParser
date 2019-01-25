package me.cirq;

import me.cirq.entity.SimpleFrame;
import soot.*;
import soot.options.Options;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * For generating SootMethod and Control graph
 */
public class GraphConstructor implements Config {
    private static GraphConstructor instance;
    static {
        Options.v().set_keep_line_number(true);
        Options.v().set_whole_program(true);
        Options.v().set_ignore_resolving_levels(true);  // fuck
        addSootLoadPath(PATH_HANDLER.getProjectClassPath());
        addSootLoadPath(PATH_HANDLER.getProjectTestClassPath());
    }

    private static final Class GraphType = ExceptionalBlockGraph.class;

    private HashMap<String, SootClass> sootClassMap;            // class name to sootclass
    private HashMap<String, List<SootMethod>> sootMethodMap;    // method name to sootmethod
    private HashMap<SootMethod, BlockGraph> sootGraphMap;       // method to control graph

    private GraphConstructor(){
        sootClassMap = new HashMap<>();
        sootMethodMap = new HashMap<>();
        sootGraphMap = new HashMap<>();
    }


    public static void addSootLoadPath(String path){
        String sootClassPath = Scene.v().getSootClassPath();
        sootClassPath = String.join(";", sootClassPath, path);
        Scene.v().setSootClassPath(sootClassPath);
    }

    public static GraphConstructor getInstance(){
        if(instance == null)
            instance = new GraphConstructor();
        return instance;
    }

//    private SootClass loadClass(String fullClassName){
//        SootClass sc;
//        try{
//            sc = Scene.v().loadClassAndSupport(fullClassName);
//        } catch (RuntimeException e){
//            sc = Scene.v().getSootClass(fullClassName);
//            sc.checkLevel(SootClass.BODIES);
//            sc.setResolvingLevel(SootClass.BODIES);
//        }
//        Scene.v().loadNecessaryClasses();
//        return sc;
//    }

    private SootClass getClass(String fullClassName){
        SootClass sc;
        if(sootClassMap.containsKey(fullClassName))
            sc = sootClassMap.get(fullClassName);
        else{
            sc = Scene.v().loadClassAndSupport(fullClassName);
            Scene.v().loadNecessaryClasses();
            sootClassMap.put(fullClassName, sc);
        }
        return sc;
    }

    private List<SootMethod> getMethods(String fullClassName, String methodName){
        List<SootMethod> lsm;
        String fullMethodName = String.join(".", fullClassName, methodName);
        if(sootMethodMap.containsKey(fullMethodName))
            lsm = sootMethodMap.get(fullMethodName);
        else{
            lsm = new LinkedList<>();
            SootClass sc = getClass(fullClassName);
            Iterator<SootMethod> it = sc.methodIterator();
            while(it.hasNext()){
                SootMethod sm = it.next();
                if(methodName.equals(sm.getName())){
                    sm.retrieveActiveBody();
                    lsm.add(sm);
                }
            }
            sootMethodMap.put(fullClassName, lsm);
        }
        return lsm;
    }

    private int[] getMethodLineInterval(UnitGraph graph){
        int start = Integer.MAX_VALUE, end = 0;
        Iterator<Unit> it = graph.iterator();
        while(it.hasNext()){
            Unit unit = it.next();
            LineNumberTag tag = (LineNumberTag) unit.getTag("LineNumberTag");
            if(tag != null){
                start = Math.min(start, tag.getLineNumber());
                end = Math.max(end, tag.getLineNumber());
            }
        }
        return new int[]{start, end};
    }

    private SootMethod getMethod(String fullClassName, String methodName, int lineNumber){
        List<SootMethod> lsm = getMethods(fullClassName, methodName);
        for(SootMethod method: lsm){
            Body body = method.getActiveBody();
            UnitGraph graph = new ExceptionalUnitGraph(body);
//            sootGraphMap.put(method, graph);
            int[] interval = getMethodLineInterval(graph);
            if(lineNumber>=interval[0] && lineNumber<=interval[1]) {
                return method;
            }
        }
        return null;
    }

    public SootMethod getMethod(SimpleFrame frame){
        String fullName = frame.getQualifiedName();
        String methodName = frame.getMethodName();
        int lineNumber = frame.getLineNumber();
        return this.getMethod(fullName, methodName, lineNumber);
    }


    private int[] getBlockLineInterval(Block block){
        int start = Integer.MAX_VALUE, end = 0;
        for(Unit unit: block){
            LineNumberTag tag = (LineNumberTag) unit.getTag("LineNumberTag");
            if(tag != null){
                start = Math.min(start, tag.getLineNumber());
                end = Math.max(end, tag.getLineNumber());
            }
        }
        return new int[]{start, end};
    }

    private Block getBlock(String fullClassName, String methodName, int lineNumber){
        SootMethod method = getMethod(fullClassName, methodName, lineNumber);
        Body body = method.getActiveBody();
        BlockGraph graph = new ExceptionalBlockGraph(body);
        for(Block block: graph){
            int[] interval = getBlockLineInterval(block);
            if(lineNumber>=interval[0] && lineNumber<=interval[1]) {
                return block;
            }
        }
        return null;
    }

    public Block getBlock(SimpleFrame frame){
        String fullName = frame.getQualifiedName();
        String methodName = frame.getMethodName();
        int lineNumber = frame.getLineNumber();
        return this.getBlock(fullName, methodName, lineNumber);
    }




}

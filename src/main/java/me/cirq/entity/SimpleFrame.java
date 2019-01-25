package me.cirq.entity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple text-based frame in a crash stack
 */
public class SimpleFrame implements Comparable<SimpleFrame>{
    private String packageName;
    private String className;
    private String methodName;
    private String qualifiedName;
    private String fileName;
    private int lineNumber;
    private int depth;      // the depth from the crash point

    private SimpleFrame prev, next;

    private String[] parseMethodName(String methodName){
        List<String> s = Arrays.asList(methodName.split("\\."));
        LinkedList<String> list = new LinkedList<>(s);
        String method = list.remove(list.size()-1);
        String class_ = list.remove(list.size()-1);
        String package_ = String.join(".", list);
        return new String[]{package_, class_, method};
    }

    public SimpleFrame(String methodName, String fileName, int lineNumber, int depth) {
        String[] splited = parseMethodName(methodName);
        this.packageName = splited[0];
        this.className = splited[1];
        this.methodName = splited[2];
        this.qualifiedName = String.join(".", packageName, className);
        this.fileName = fileName;
        this.lineNumber = lineNumber;
        this.depth = depth;
        this.prev = this.next = null;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getDepth() {
        return depth;
    }


    @Override
    public int hashCode(){      // genius!
        return depth;
    }

    @Override
    public int compareTo(SimpleFrame that){
        return depth - that.depth;
    }


    public SimpleFrame getPrev() {
        return prev;
    }

    public SimpleFrame getNext() {
        return next;
    }

    public void setPrev(SimpleFrame prev){
        if(this.prev == null)
            this.prev = prev;
    }
    public void setNext(SimpleFrame next){
        if(this.next == null)
            this.next = next;
    }

}

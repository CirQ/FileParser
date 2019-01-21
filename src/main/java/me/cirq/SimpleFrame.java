package me.cirq;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SimpleFrame {
    private String packageName;
    private String className;
    private String methodName;
    private String qualifiedName;
    private String fileName;
    private int lineNumber;

    private SimpleFrame prev, next;

    private String[] parseMethodName(String methodName){
        List<String> s = Arrays.asList(methodName.split("\\."));
        LinkedList<String> list = new LinkedList<>(s);
        String method = list.remove(list.size()-1);
        String class_ = list.remove(list.size()-1);
        String package_ = String.join(".", list);
        return new String[]{package_, class_, method};
    }

    public SimpleFrame(String methodName, String fileName, int lineNumber) {
        String[] splited = parseMethodName(methodName);
        this.packageName = splited[0];
        this.className = splited[1];
        this.methodName = splited[2];
        this.qualifiedName = String.join(".", packageName, className);
        this.fileName = fileName;
        this.lineNumber = lineNumber;
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

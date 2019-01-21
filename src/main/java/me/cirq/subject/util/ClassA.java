package me.cirq.subject.util;

import me.cirq.subject.ClassB;
import me.cirq.subject.util.ClassE;

public class ClassA {   // file 4
    public static void f1(){
        ClassB.f3();
        ClassE.f4("main entry");
    }

    static void f5(){
        ClassE.f4("never used");
    }
}

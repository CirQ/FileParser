package me.cirq.subject;

import me.cirq.subject.util.ClassE;

public class ClassC {   // file 2
    static void f12(int i){
        if(i > 1)
            ClassE.f4("nothing to be done");
        else
            ClassD.f11();
        System.out.println("f12 is complete");
    }

    static void f12(int i, int j){
        System.out.println("heer is overloaded but never invoked");
    }

    static void f2(){
        ClassB.f6();
    }
}

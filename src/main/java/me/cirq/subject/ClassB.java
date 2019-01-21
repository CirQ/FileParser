package me.cirq.subject;

public class ClassB {   // file 3
    public static void f3(){
        char i = f6();
        int j = f7(i);
        ClassC.f12(j);
    }

    static char f6(){
        return '0';
    }

    static int f7(char c){
        return Integer.parseInt(Character.toString(c));
    }
}

package me.cirq.subject;

public class ClassD {
    static void f11(){
        int h = 32;
        while(h >= 0){
            h--;
        }
        if(h > 1){
            f14();
        }
        else{
            int a = f15();
            int b = f16();
            int[] c = f17(b);
            int crashhhhhhhh = c[h];
        }
    }

    static void f10(){
        f14();
    }

    static void f14(){
        // safe code
    }

    static int f15(){
        return 1;
    }

    static int f16(){
        return 5;
    }

    static int[] f17(int num){
        return new int[num];
    }
}

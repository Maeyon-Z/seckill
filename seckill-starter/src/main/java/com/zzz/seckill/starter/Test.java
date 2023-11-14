package com.zzz.seckill.starter;

import java.math.BigInteger;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        BigInteger[] a = new BigInteger[n];

        for(int i = 0; i < n; i ++){
            a[i] = new BigInteger(sc.next());
        }

        BigInteger pre = a[0], cur = a[0];
        BigInteger sum = new BigInteger("0");
        for(int i = 1; i < n; i++){
            sum = sum.add(a[i].multiply(pre));
            cur = cur.add(a[i]);
            pre = pre.add(cur);
        }

        System.out.println(sum);
    }
}

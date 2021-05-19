package com.ailin.concurrence;

/**
 * 继承Thread类
 */
public class MyThread extends Thread {

    @Override
    public void run() {

        for (int i = 0; i < 100; i++) {
            System.out.println(currentThread().getName() + "===========" + i);
        }
    }
}

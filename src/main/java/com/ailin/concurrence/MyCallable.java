package com.ailin.concurrence;

import java.util.concurrent.Callable;

public class MyCallable implements Callable {

    @Override
    public Object call() throws Exception {

        Thread thread = Thread.currentThread();
        int n = 0;
        for (int i=0; i < 100; i++){
            System.out.println(thread.getName() + "=============" + i);
            n = i;
        }
        return n;
    }
}

package com.ailin.concurrence.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadWriteLockClient {

    public static void main(String[] args) {

        final ShareData shareData = new ShareData(10);

        ExecutorService threadPool = Executors.newFixedThreadPool(5);

        threadPool.submit(new WorkRead(shareData));
        threadPool.submit(new WorkRead(shareData));
        threadPool.submit(new WorkRead(shareData));
        threadPool.submit(new WorkRead(shareData));

        threadPool.submit(new WorkWrite(shareData,"adlfjoaijdfijadofijao"));
        threadPool.submit(new WorkWrite(shareData,"164841984198149814981"));
    }
}

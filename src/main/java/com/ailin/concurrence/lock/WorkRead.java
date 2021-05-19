package com.ailin.concurrence.lock;

/**
 * 多线程读数据
 */
public class WorkRead implements Runnable {

    private final ShareData shareData;


    public WorkRead(ShareData shareData) {
        this.shareData = shareData;
    }

    @Override
    public void run() {

        while (true){
            try {
                char[] read = shareData.read();
                System.out.println(Thread.currentThread().getName() + "读取数据是：" + String.valueOf(read));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

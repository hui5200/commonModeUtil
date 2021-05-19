package com.ailin.concurrence.lock;

import java.util.Random;

/**
 * 多线程写数据
 */
public class WorkWrite implements Runnable {

    private static final Random random = new Random(System.currentTimeMillis());

    private final ShareData shareData;

    private final String str;

    private int index = 0;

    public WorkWrite(ShareData shareData, String str){
        this.shareData = shareData;
        this.str = str;
    }

    @Override
    public void run() {

        while (true){
            try {
                char c = nextChar();
                if(index == 0){
                    return;
                }
                System.out.println(Thread.currentThread().getName() + "线程写入数据：" + c);
                shareData.write(c);
                Thread.sleep(random.nextInt(1_000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private char nextChar(){
        char c = str.charAt(index);
        index++;
        if(index >= str.length()){
            index = 0;
        }
        return c;
    }
}

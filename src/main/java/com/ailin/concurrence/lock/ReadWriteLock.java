package com.ailin.concurrence.lock;

/**
 * 可以并行多线读，如果是写，只能单线程写
 */
public class ReadWriteLock {

    //正在读
    private int readingReaders = 0;

    //等待读
    private int waitingReaders = 0;

    //正在写
    private int writingWriters = 0;

    //等待写
    private int waitingWriters = 0;

    private boolean preWrite = true;

    public ReadWriteLock(){
        this(true);
    }

    public ReadWriteLock(boolean preWrite) {
        this.preWrite = preWrite;
    }

    /**
     * 读锁，如果有线程准备写，那么等待
     * @throws InterruptedException
     */
    public synchronized void readLock() throws InterruptedException {
        //有线程准备读
        this.waitingReaders++;
        try {
            //如果有线程准备写，那么等待
            while (writingWriters > 0 && (preWrite && waitingWriters > 0)){
                this.wait();
            }
            //正在读
            this.readingReaders++;
        } finally {
            //线程读完
            this.waitingReaders--;
        }
    }

    /**
     * 释放锁
     */
    public synchronized void readUnLock(){
        this.readingReaders--;
        this.notifyAll();
    }

    //写锁

    /**
     * 写锁，如果有线程准备读，等待
     * @throws InterruptedException
     */
    public synchronized void writeLock() throws InterruptedException {
        //有线程准备写
        this.writingWriters++;

        try {
            //如果有线程准备读，等待
            while (readingReaders > 0 && waitingReaders > 0){
                this.wait();
            }
            //正在写
            this.waitingWriters++;
        } finally {
            //线程写完
            this.waitingWriters--;
        }
    }

    /**
     * 释放写锁
     */
    public synchronized void writeUnLock(){
        this.writingWriters--;
        this.notifyAll();
    }
}

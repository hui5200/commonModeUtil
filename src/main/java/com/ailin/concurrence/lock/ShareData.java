package com.ailin.concurrence.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 定义共享数据缓冲区buffer,有线程从缓存读数据，有线程从缓存写数据
 */
public class ShareData {

    private static final Logger log = LoggerFactory.getLogger(ShareData.class);

    private final char[] buffer;

    private byte[] bufferBytes;

    private final ReadWriteLock readWriteLock = new ReadWriteLock();

    public ShareData(int size){
        this.buffer = new char[size];
        bufferBytes = new byte[size];
        Arrays.fill(buffer, '*');
        Arrays.fill(bufferBytes, (byte) 0);
    }

    public char[] read() throws InterruptedException {

        try {
            this.readWriteLock.readLock();
            return this.doRead();
        } finally {
            this.readWriteLock.readUnLock();
        }
    }

    public void write(char w) throws InterruptedException {

        try {
            this.readWriteLock.writeLock();
            this.doWrite(w);
        } finally {
            this.readWriteLock.writeUnLock();
        }
    }

    private void doWrite(char w) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = w;
            slowly(10);
        }

        Charset charset = StandardCharsets.UTF_8;
        CharBuffer charBuffer = CharBuffer.allocate(buffer.length);
        charBuffer.put(buffer);
        charBuffer.flip();
        ByteBuffer encode = charset.encode(charBuffer);
        bufferBytes = encode.array();

        File file = new File("F:\\Files\\test.txt");

        if(!file.exists()){
            try {
                boolean newFile = file.createNewFile();
                if(newFile) log.info("创建成功,路径：{}",file.getPath() + "." + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(bufferBytes);

            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private char[] doRead(){
        char[] newBuf = null;
        File file = new File("F:\\Files\\test.txt");

        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            int read = inputStreamReader.read(buffer);
            if(read == -1){
                log.info("数据读取结束");
            }

            newBuf = new char[buffer.length];
            System.arraycopy(buffer, 0, newBuf, 0, newBuf.length);

            inputStream.close();
            inputStreamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.slowly(50);
        return newBuf;
    }

    private void slowly(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

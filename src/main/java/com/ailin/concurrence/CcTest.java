package com.ailin.concurrence;

import com.ailin.util.ImageConverterUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

public class CcTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(10);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 50, 3600,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(1024),threadFactory);
        threadPoolExecutor.submit(new MyThread());
//        executor.submit(new MyThread());
//
//        Thread.sleep(3000);

//        executor.submit(new MyRunnable());
//        executor.submit(new MyRunnable());

//        Future future = executor.submit(new MyCallable());
//
//        int n = (int)future.get();
//        System.out.println("获取执行结果为：" + n);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
//            executor.submit(new MyRunnable());
            test();
        }
        System.out.println("总耗时：" +  (System.currentTimeMillis() - start) + "毫秒");
    }

    public static void test(){
        //自定义缓存冲区
        byte[] pdfBytes = new byte[1024];
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream("F:\\Files\\test55.pdf"));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);

            while ( (bufferedInputStream.read(pdfBytes,0, 1024)) != -1){
                bufferedOutputStream.write(pdfBytes);
            }

            //刷新输出流，强制写出所有缓冲流
            bufferedOutputStream.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(bytes).trim();
//            System.out.println(encode);

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] decodeBuffer = decoder.decodeBuffer(encode);

            long start = System.currentTimeMillis();
            List<String> image = ImageConverterUtil.Pdf2Image(decodeBuffer);

            BASE64Decoder base64Decoder = new BASE64Decoder();
            ImageConverterUtil.byte2File(base64Decoder.decodeBuffer(image.get(0)),"F:\\Files\\test", System.currentTimeMillis()+ ".jpg");
            System.out.println(Thread.currentThread().getName() + "耗时：" +  (System.currentTimeMillis() - start) + "毫秒");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if(bufferedOutputStream != null){
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

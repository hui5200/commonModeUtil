package com.ailin.util;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.convert.Convert;
import com.google.gson.Gson;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ImageUtils {

    public static void main(String[] args) throws IOException, InterruptedException {
        byte[] bytes;
        InputStream in = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\test\\55.txt"));
        bytes = new byte[in.available()];
        in.read(bytes);
        String imageString = new String(bytes);
        System.out.println("读取图片数据========");
        System.out.println(imageString);

        List<String> list = new ArrayList<>();
        list.add(imageString);
        addImageCache(list, "aa");
        Thread.sleep(1000);
        List<String> imageCacheList = getImageCacheList("aa");
        System.out.println("读取缓存图片数据========");
        System.out.println(imageCacheList.get(0));
    }

    private static final Logger log = LoggerFactory.getLogger(ImageUtils.class);

    private static final String IMAGE_DIR = "./imagesCache";

    private static final long IMAGE_DATE = 24 * 60 * 60 * 1000;

    private static final Gson gson = new Gson();

    //将图片缓存到磁盘
    public static void addImageCache(List<String> images, String fileName) {

        Executors.newSingleThreadExecutor().submit(new CacheTask(images,fileName));
    }

    private static class CacheTask implements Runnable {

        private List<String> images;

        private String fileName;

        public CacheTask(List<String> images, String fileName) {
            this.images = images;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            //压缩图片
            images = resizeImage(images);
            //在集合末尾添加创建时间
            images.add(String.valueOf(System.currentTimeMillis()));
            byte[] imagesBytes = gson.toJson(images).getBytes();

            InputStream imageByteInputStream = new ByteArrayInputStream(imagesBytes);
            Path path = Paths.get(IMAGE_DIR, fileName);
            boolean exists = Files.exists(path);
            if (!exists) {
                try {
                    if (!Files.exists(Paths.get(IMAGE_DIR))) {
                        Files.createDirectories(Paths.get(IMAGE_DIR));
                    }
                    Files.copy(imageByteInputStream, path);
                    imageByteInputStream.close();
                } catch (IOException e) {
                    log.error("图片文件写入失败", e);
                }
            }
        }
    }

    //从磁盘里获取图片缓存
    public static byte[] getImageCache(String fileName) {

        byte[] bytes = null;
        try {
            InputStream inputStream = new FileInputStream(IMAGE_DIR + "/" + fileName);
            bytes = new byte[inputStream.available()];
            int read = inputStream.read(bytes);
            if (read == -1) {
                log.info("数据读取结束");
            }
            inputStream.close();
        } catch (IOException e) {
            log.error("图片文件获取失败", e);
        }
        return bytes;
    }

    //判断缓存图片是否过期，过期删除
    public static List<String> getImageCacheList(String fileName) {
        byte[] imageCache = getImageCache(fileName);
        String imageString = new String(imageCache);
        @SuppressWarnings("unchecked")
        List<String> images = gson.fromJson(imageString, List.class);

        long date = 0;
        if (images != null && images.size() > 0) {
            //取出集合末尾的创建日期
            date = Long.parseLong(images.get(images.size() - 1));
            //去掉日期
            images.remove(images.size() - 1);
        }
        if (System.currentTimeMillis() - date < IMAGE_DATE) {
            return images;
        }

        //删除缓存
        Path path = Paths.get(IMAGE_DIR, fileName);
        try {
            boolean isDelete = Files.deleteIfExists(path);
            log.debug("删除缓存图片" + fileName + ": " + isDelete);
        } catch (IOException e) {
            log.error("图片缓存删除失败", e);
        }
        return new ArrayList<>();
    }

    public static List<String> resizeImage(List<String> base64Images) {
        List<String> result = new ArrayList<>();
        try {
            for (String base64Image : base64Images) {
                BufferedImage src = toBufferedImage(base64Image);
                //压缩图片质量
                BufferedImage output = Thumbnails.of(src).scale(1f).outputQuality(0.5f).asBufferedImage();
                result.add(imageToBase64(output));
            }
        } catch (Exception e) {
            log.error("压缩图片失败",e);
            return result;
        }
        return result;
    }

    public static BufferedImage base64String2BufferedImage(String base64string) {
        BufferedImage image = null;
        try {
            InputStream stream = BaseToInputStream(base64string);
            image = ImageIO.read(stream);
        } catch (IOException e) {
            log.error("base64进行解码，转byte失败",e);
        }
        return image;
    }

    public static BufferedImage toBufferedImage(String base64string) {
        BASE64Decoder decoder = new BASE64Decoder();
        Image image = null;
        try {
            byte[] bytes = decoder.decodeBuffer(base64string);
            image = Toolkit.getDefaultToolkit().createImage(bytes);
        } catch (IOException e) {
            log.error("base64转byte[]",e);
        }
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        //这段代码确保加载了图像中的所有像素
        assert image != null;
        image = new ImageIcon(image).getImage();
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            int transparency = Transparency.OPAQUE;
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // 系统没有屏幕
        }
        if (bimage == null) {
            // 创建一个使用默认颜色模型的缓冲图像
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }
        // 复制图像到缓冲图像
        Graphics g = bimage.createGraphics();
        // 将图像绘制到缓冲图像上
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }

    private static InputStream BaseToInputStream(String base64string) {
        ByteArrayInputStream stream = null;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes1 = decoder.decodeBuffer(base64string);
            stream = new ByteArrayInputStream(bytes1);
        } catch (Exception e) {
            log.error("base64进行解码，转byte失败",e);
        }
        return stream;
    }

    public static String imageToBase64(BufferedImage bufferedImage) {
        BASE64Encoder encoder = new BASE64Encoder();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
        } catch (IOException e) {
            log.error("二进制图片转字符串",e);
        }
        return encoder.encode((baos.toByteArray()));
    }
}

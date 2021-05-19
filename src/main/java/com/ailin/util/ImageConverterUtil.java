package com.ailin.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ImageConverterUtil {

    private final static Logger log = LoggerFactory.getLogger(ImageConverterUtil.class.getComponentType());


    public static List<String> Pdf2Image(byte[] bytes){
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream byteArrayOutputStream = null;
        List<String> result = new ArrayList<>();

        try {
//            inputStream = new BufferedInputStream(new FileInputStream(path));
            PDDocument doc = PDDocument.load(inputStream);
            PDFRenderer pdfRenderer = new PDFRenderer(doc);

            PDPageTree pages = doc.getPages();
            int count = pages.getCount();
            for (int i = 0; i < count; i++) {
                BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(i, 200);
                byteArrayOutputStream = new ByteArrayOutputStream();

                ImageIO.write(bufferedImage,"jpg", byteArrayOutputStream);

                byte[] data = byteArrayOutputStream.toByteArray();
                Base64.Encoder encoder = Base64.getEncoder();
                result.add(encoder.encodeToString(data));
            }
            doc.close();
        } catch (IOException e) {
            log.error("pdf转换异常",e);
        }finally {
            try {
                inputStream.close();
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据byte数组，生成文件
     * @param bfile 文件数组
     * @param filePath 文件存放路径
     * @param fileName 文件名称
     */
    public static void byte2File(byte[] bfile,String filePath,String fileName){
        BufferedOutputStream bos=null;
        FileOutputStream fos=null;
        File file=null;
        try{
            File dir=new File(filePath);
            if(!dir.exists() && !dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file=new File(filePath + "\\" + fileName);
            fos=new FileOutputStream(file);
            bos=new BufferedOutputStream(fos);
            bos.write(bfile);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        finally{
            try{
                if(bos != null){
                    bos.close();
                }
                if(fos != null){
                    fos.close();
                }
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

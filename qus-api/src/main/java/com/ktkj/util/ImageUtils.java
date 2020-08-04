package com.ktkj.util;

//import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

public class ImageUtils {

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
        String imgPath= "/Users/luonana/IdeaProjects/ring/ring-shop/src/main/webapp/tianan/";
        String fileName = "tianan_1568776762092.png";
        String firstFileName = "photos.png";
        ImageUtils.changeSize(258,350,imgPath+firstFileName);
        String finalImgName = ImageUtils.exportImg2(imgPath+fileName,imgPath+firstFileName,imgPath,0,0);
    }


    public static void exportImg1(){
        int width = 100;
        int height = 100;
        String s = "你好";

        File file = new File("d:/image.jpg");

        Font font = new Font("Serif", Font.BOLD, 10);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)bi.getGraphics();
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.RED);

        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(s, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = -bounds.getY();
        double baseY = y + ascent;

        g2.drawString(s, (int)x, (int)baseY);

        try {
            ImageIO.write(bi, "jpg", file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void base64ToFile(String base64, String fileName) {
        File file = null;
        //创建文件目录
        base64 = base64.replaceAll("data:image/jpeg;base64,","");
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            file=new File(fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static boolean  changeSize(int newWidth, int newHeight, String path) {
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(path));

            //字节流转图片对象
            Image bi = ImageIO.read(in);
            //构建图片流
            BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            //绘制改变尺寸后的图
            tag.getGraphics().drawImage(bi, 0, 0, newWidth, newHeight, null);
            //输出流
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
            //JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            //encoder.encode(tag);
            ImageIO.write(tag, "jpeg", out);
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void changePngToJpeg(String pngPath,String jpegPath){
        BufferedImage bufferedImage;

        try {

            //read image file
            bufferedImage = ImageIO.read(new File(pngPath));

            // create a blank, RGB, same width and height, and a white background
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            //TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位

            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

            // write to jpeg file
            ImageIO.write(newBufferedImage, "jpg", new File(jpegPath));

            System.out.println("Done");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


    public static String exportImg2(String headImg,String firstImg,String filePath,int x,int y){
        String fileName = "";
        try {
            //1.jpg是你的 主图片的路径
            InputStream is = new FileInputStream(firstImg);


            //通过JPEG图象流创建JPEG数据流解码器
            JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
            //解码当前JPEG数据流，返回BufferedImage对象
            BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
            //得到画笔对象
            Graphics g = buffImg.getGraphics();

            //创建你要附加的图象。
            //小图片的路径
            ImageIcon imgIcon = new ImageIcon(headImg);

            //得到Image对象。
            Image img = imgIcon.getImage();

            //将小图片绘到大图片上。
            //5,300 .表示你的小图片在大图片上的位置。
            g.drawImage(img,x,y,null);

            //设置颜色。
            g.setColor(Color.BLACK);


            //最后一个参数用来设置字体的大小
            //Font f = new Font("宋体",Font.PLAIN,25);
            //Color mycolor = Color.red;//new Color(0, 0, 255);
            //g.setColor(mycolor);
            //g.setFont(f);

            //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
            //g.drawString(username,0,0);

            g.dispose();


            OutputStream os;
            fileName = "tianan_"+(new Date()).getTime()+".png";
            //os = new FileOutputStream("d:/union.jpg");
            String shareFileName = filePath+fileName;
            os = new FileOutputStream(shareFileName);
            //创键编码器，用于编码内存中的图象数据。
            JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
            en.encode(buffImg);

            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } //catch (ImageFormatException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        //}
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileName;

    }

    /**
     *param qrcodePath : 最后图片保存路劲
     */
    public static void overlapImage(String qrcodePath) {
        try {
            BufferedImage big = new BufferedImage(1080, 1920, BufferedImage.TYPE_INT_RGB);
            Graphics2D gd = big.createGraphics();
            //gd.setBackground(Color.white);
            gd.setColor(Color.white);
            gd.dispose();
            //BufferedImage big = ImageIO.read(new File(screenPath));
            //BufferedImage small = ImageIO.read(new File(qrcodePath));
            BufferedImage small = new BufferedImage(540, 540,BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = big.createGraphics();
            int x = (big.getWidth() - small.getWidth()) / 2;
            int y = 200 ;
            g.drawImage(small, x, y, small.getWidth(), small.getHeight(), null);
            g.dispose();
            ImageIO.write(big, "jpg", new File(qrcodePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
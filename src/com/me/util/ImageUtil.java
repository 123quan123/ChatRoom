package com.me.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.me.section.FileSection;

public class ImageUtil {
	/**
           * 读取图片  返回一个图片的字节数组
      * @param path
      * @return
      * 
    */
     public static byte[] imgArray(String path) {
         //字节输入流
         InputStream inputStream = null;
         //字节缓冲流数组
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         try {
             inputStream = new FileInputStream(path);
             byte[] b = new byte[1024];
             int len = -1;
            //循环读取
             while ((len = inputStream.read(b)) != -1) {
//            	 System.out.println(len);
                byteArrayOutputStream.write(b, 0, len);
             }
             System.out.println("读入数组完成");
             //返回byteArrayOutputStream数组
             return byteArrayOutputStream.toByteArray();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             try {
                 //关闭流
                 inputStream.close();
            } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         return null;
     }
    public static void writeImg(byte[]array,String path){
         //创建一个字节输出流
         DataOutputStream dataOutputStream = null;
         try {
             dataOutputStream = new DataOutputStream(new FileOutputStream(path));
             //将字节数组
             dataOutputStream.write(array);
         } catch (IOException e) {
             e.printStackTrace();
         }finally {
             try {
                 //关闭
                 dataOutputStream.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
      }
   
      /**
       * 读取二进制保存的图片  放到数组里
      * @param path
      * @return
      */
     public static byte[] imageIn(String path){
         //创建一个字节输出流
         DataInputStream dataInputStream = null;
         try {
             dataInputStream = new DataInputStream(new FileInputStream(path));
             //创建一个字节数组  byte的长度等于二进制图片的返回的实际字节数
             byte[] b = new byte[dataInputStream.available()];
             //读取图片信息放入这个b数组
             dataInputStream.read(b);
             return b;
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }finally {
             try {
                 //关闭流
                 dataInputStream.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         return null;
     }
 
     /**
      * 读取二进制保存的图片 输出图片
      * @param img
      * @param path
      */
     public static void writImg(byte[]img,String path){
         //创建一个字节输出流
         OutputStream outputStream = null;
         try {
             outputStream = new FileOutputStream(path);
             //将图片输处到流中
             outputStream.write(img);
             //刷新
             outputStream.flush();
         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }finally {
             try {
                 //关闭
                 outputStream.close();
             } catch (IOException e) {
	                  e.printStackTrace();
             }
         }
     }
 
     /**
      * main方法
      * @param args
     * @throws Exception 
      */
     public static void main(String[] args) throws Exception {
		//
		//String readFormats[] = ImageIO.getReaderFormatNames();
		//String writeFormats[] = ImageIO.getWriterFormatNames();
		//System.out.println("Readers: " + Arrays.asList(readFormats));
		//System.out.println("Writers: " + Arrays.asList(writeFormats));
         //获取图片  将图片信息把存到数组b中
//         byte[] b = ImageUtil.imgArray("C:\\Users\\Lenovo\\Desktop\\20190719104705151.bmp");
     
//         通过数组B写到文件中
//         String encode = Base64.getEncoder().encodeToString(b);
//         
//         byte[] decode = Base64.getDecoder().decode(encode);
         
//         ImageUtil.writImg(b,"F:\\ChatRoom的图片文件夹\\copyimg.txt");
//         //读取二进制文件保存到一个数组中
//         byte[] c = ImageUtil.imageIn("F:\\ChatRoom的图片文件夹\\copyimg.txt");
         //通过数组c  输出图片
//         System.out.println("e:\n" + ByteString.bytesToString(ByteString.StringToBytes(decrypt)));
//         ByteArrayInputStream in = new ByteArrayInputStream(ByteString.StringToBytes(string));
//         BufferedImage image = ImageIO.read(in);  
    	 
//    	    BufferedImage im = ImageIO.read(new File("C:\\Users\\Lenovo\\Desktop\\org.bmp"));
//    		 ByteArrayOutputStream bs = new ByteArrayOutputStream(); 
//            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
//            ImageIO.write(im, "bmp", imOut);
//            ByteArrayInputStream is = new ByteArrayInputStream(bs.toByteArray());
//            System.out.println("bs : \n" + ByteString.bytesToInt(bs.toByteArray()));
//            String string = DataTools.InputStreamTOString(is);
//            string = new String(string.getBytes());
            String key = "MIGfMA0GCSqGSIb3";
	        byte[] b = ImageUtil.imgArray("C:\\Users\\Lenovo\\Desktop\\org.bmp");
	        System.out.println(b.length);
//	        System.out.println("before\n" + ByteString.bytesToString(b));
	        FileSection fileSection = new FileSection((long) b.length);
	        fileSection.setValue(b);
	        System.out.println(fileSection.toString().length());
            System.out.println(fileSection.toString());
	        String json = ArgumentMaker.gson.toJson(fileSection);
	        System.out.println(json.length());
//	        String encode = Base64.getEncoder().encodeToString(b);
            String encrypt = AESUtil.encrypt(json, key);
            System.out.println(encrypt.length());
            String decrypt = AESUtil.decrypt(encrypt, key);

//            byte[] decode = Base64.getDecoder().decode(decrypt);
//            System.out.println(new String(b).equals(decrypt));
            System.out.println(json.equals(decrypt));
            FileSection fromJson = ArgumentMaker.gson.fromJson(decrypt, FileSection.class);
            System.out.println(fromJson.toString());
//            System.out.println("after\n" + decrypt);
//            System.out.println("after\n" + ByteString.bytesToString(decrypt.getBytes()));
//            String path = "F:\\ChatRoom的图片文件夹\\test.bmp";
//            CreateFileUtil.createFile(path);
//            ImageUtil.writeImg(decrypt.getBytes(), path);
     }
}

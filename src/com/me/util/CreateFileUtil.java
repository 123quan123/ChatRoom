 package com.me.util;

import java.io.File;
import java.io.IOException;

public class CreateFileUtil {
     public static File createFile(String destFileName) {
         File file = new File(destFileName);
         if(file.exists()) {
             System.out.println("文件" + destFileName + "已存在");
             return null;
             }
         if (destFileName.endsWith(File.separator)) {
             System.out.println("此目录" + destFileName + "不是文件");
             return null;        }
         System.out.println(file);
         System.out.println(file.getParentFile());
         if(!file.getParentFile().exists()) {
             System.out.println("父文件节点存在");
             if(!file.getParentFile().mkdirs()) {
                 System.out.println("创建父文件节点失败");
                 return null;
                 }
             }
         try {
             if (file.createNewFile()) {
                 System.out.println("创建" + destFileName + "成果");
                 return file;
                 } else {
                     System.out.println("创建" + destFileName + "失败");
                     return null;
                     }
             } catch (IOException e) {
                 e.printStackTrace();
                 System.out.println("创建" + destFileName + "失败" + e.getMessage());
                 return null;
                 }
         }
     public static boolean createDir(String destDirName) {
         File dir = new File(destDirName);
         if (dir.exists()) {
             System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�Ŀ��Ŀ¼�Ѿ�����");
             return false;
             }
         if (!destDirName.endsWith(File.separator)) {
             destDirName = destDirName + File.separator;
             }
         //����Ŀ¼
         if (dir.mkdirs()) {
             System.out.println("����Ŀ¼" + destDirName + "�ɹ���");
             return true;
             } else {
                 System.out.println("����Ŀ¼" + destDirName + "ʧ�ܣ�");
                 return false;
                 }
         }
     public static String createTempFile(String prefix, String suffix, String dirName) {
         File tempFile = null;
         if (dirName == null) {
             try{
                 //��Ĭ���ļ����´�����ʱ�ļ�
                 tempFile = File.createTempFile(prefix, suffix);
                 //������ʱ�ļ���·��
                 return tempFile.getCanonicalPath();
                 } catch (IOException e) {
                     e.printStackTrace();
                     System.out.println("������ʱ�ļ�ʧ�ܣ�" + e.getMessage());
                     return null;
                     }
             } else {
                 File dir = new File(dirName);
                 //�����ʱ�ļ�����Ŀ¼�����ڣ����ȴ���
                 if (!dir.exists()) {
                     if (!CreateFileUtil.createDir(dirName)) {
                         System.out.println("������ʱ�ļ�ʧ�ܣ����ܴ�����ʱ�ļ����ڵ�Ŀ¼��");
                         return null;
                         }
                     }
                 try {
                     //��ָ��Ŀ¼�´�����ʱ�ļ�
                     tempFile = File.createTempFile(prefix, suffix, dir); 
                     return tempFile.getCanonicalPath();
                     } catch (IOException e) {
                         e.printStackTrace();
                         System.out.println("������ʱ�ļ�ʧ�ܣ�" + e.getMessage());
                         return null;
                         }
                 }
         }  
 }

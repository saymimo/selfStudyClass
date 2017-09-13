package com.ssc.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUpload
{
  public static String perview = "/perview/";
  public static String panF = "/";
  public static String filePath = "/Download/";
  public static String fileManagePath = "/fileManage/";
  
  public static String fileUp(MultipartFile file, String filePath, String fileName)
  {
    String extName = "";
    try
    {
      if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
        extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
      }
      copyFile(file.getInputStream(), filePath, fileName + extName).replaceAll("-", "");
    }
    catch (IOException e)
    {
      System.out.println(e);
    }
    return fileName + extName;
  }
  
  public static String fileUpExtName(MultipartFile file, String filePath, String fileName)
  {
    String extName = "";
    try
    {
      if (file.getOriginalFilename().lastIndexOf(".") >= 0) {
        extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
      }
      if ((extName.toLowerCase().equals(".jpg")) || (extName.toLowerCase().equals(".png")) || (extName.toLowerCase().equals(".gif")) || (extName.toLowerCase().equals(".jpeg"))) {
        extName = ".pdf";
      }
      copyFile(file.getInputStream(), filePath, fileName + extName).replaceAll("-", "");
    }
    catch (IOException e)
    {
      System.out.println(e);
    }
    return fileName + extName;
  }
  
  private static String copyFile(InputStream in, String dir, String realName)
    throws IOException
  {
    File file = new File(dir, realName);
    if (!file.exists())
    {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      file.createNewFile();
    }
    FileUtils.copyInputStreamToFile(in, file);
    return realName;
  }
  
  public static boolean delAllFile(String path)
  {
    boolean flag = false;
    File file = new File(path);
    if (!file.exists()) {
      return flag;
    }
    if (!file.isDirectory()) {
      return flag;
    }
    String[] tempList = file.list();
    File temp = null;
    for (int i = 0; i < tempList.length; i++)
    {
      if (path.endsWith(File.separator)) {
        temp = new File(path + tempList[i]);
      } else {
        temp = new File(path + File.separator + tempList[i]);
      }
      if (temp.isFile()) {
        temp.delete();
      }
      if (temp.isDirectory())
      {
        delAllFile(path + "/" + tempList[i]);
        flag = true;
      }
    }
    return flag;
  }
}

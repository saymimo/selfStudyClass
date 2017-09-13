package com.ssc.util;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class ImageAnd64Binary
{
  public static void main(String[] args)
  {
    String imgSrcPath = "d:/abc/123.jpg";
    String imgCreatePath = "E:\\apache-tomcat-6.0.37\\webapps/pro/ueditor2/jsp/upload1/20140318/480ace2bfc6e44608595bd4adbdeb067.jpg";
    imgCreatePath = imgCreatePath.replaceAll("\\\\", "/");
    System.out.println(imgCreatePath);
    String strImg = getImageStr(imgSrcPath);
    System.out.println(strImg);
    generateImage(strImg, imgCreatePath);
  }
  
  public static String getImageStr(String imgSrcPath)
  {
    InputStream in = null;
    byte[] data = (byte[])null;
    try
    {
      in = new FileInputStream(imgSrcPath);
      data = new byte[in.available()];
      in.read(data);
      in.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    BASE64Encoder encoder = new BASE64Encoder();
    
    return encoder.encode(data);
  }
  
  public static boolean generateImage(String imgStr, String imgCreatePath)
  {
    if (imgStr == null) {
      return false;
    }
    BASE64Decoder decoder = new BASE64Decoder();
    try
    {
      byte[] b = decoder.decodeBuffer(imgStr);
      for (int i = 0; i < b.length; i++) {
        if (b[i] < 0)
        {
          int tmp36_34 = i; byte[] tmp36_33 = b;tmp36_33[tmp36_34] = ((byte)(tmp36_33[tmp36_34] + 256));
        }
      }
      OutputStream out = new FileOutputStream(imgCreatePath);
      out.write(b);
      out.flush();
      out.close();
      return true;
    }
    catch (Exception e) {}
    return false;
  }
}

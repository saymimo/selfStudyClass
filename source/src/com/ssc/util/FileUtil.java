package com.ssc.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class FileUtil
{
  public static void main(String[] args)
  {
    String dirName = "d:/FH/topic/";
    createDir(dirName);
  }
  
  public static boolean createDir(String destDirName)
  {
    File dir = new File(destDirName);
    if (dir.exists()) {
      return false;
    }
    if (!destDirName.endsWith(File.separator)) {
      destDirName = destDirName + File.separator;
    }
    if (dir.mkdirs()) {
      return true;
    }
    return false;
  }
  
  public static void delFile(String filePathAndName)
  {
    try
    {
      String filePath = filePathAndName;
      filePath = filePath.toString();
      File myDelFile = new File(filePath);
      myDelFile.delete();
    }
    catch (Exception e)
    {
      System.out.println("删除文件操作出错");
      e.printStackTrace();
    }
  }
  
  public static void updateFilePath(String startFilePath, String endFilePath)
  {
    try
    {
      File afile = new File(startFilePath);
      if (afile.renameTo(new File(endFilePath + afile.getName()))) {
        System.out.println("移动文件成功");
      } else {
        System.out.println("移动文件失败!");
      }
    }
    catch (Exception e)
    {
      System.out.println("文件移动操作出错");
      e.printStackTrace();
    }
  }
  
  public static void CopyFile(String startFilePath, String endFilePath)
    throws IOException
  {
    FileInputStream input = null;
    FileOutputStream output = null;
    try
    {
      input = new FileInputStream(startFilePath);
      output = new FileOutputStream(endFilePath);
      int in = input.read();
      while (in != -1)
      {
        output.write(in);
        in = input.read();
      }
    }
    catch (IOException e)
    {
      System.out.println(e.toString());
    }
    finally
    {
      input.close();
      output.close();
    }
  }
  
  public static byte[] getContent(String filePath)
    throws IOException
  {
    File file = new File(filePath);
    long fileSize = file.length();
    if (fileSize > 2147483647L)
    {
      System.out.println("file too big...");
      return null;
    }
    FileInputStream fi = new FileInputStream(file);
    byte[] buffer = new byte[(int)fileSize];
    int offset = 0;
    int numRead = 0;
    while ((offset < buffer.length) && 
      ((numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0)) {
      offset += numRead;
    }
    if (offset != buffer.length) {
      throw new IOException("Could not completely read file " + 
        file.getName());
    }
    fi.close();
    return buffer;
  }
  
  public static byte[] toByteArray(String filePath)
    throws IOException
  {
    File f = new File(filePath);
    if (!f.exists()) {
      throw new FileNotFoundException(filePath);
    }
    ByteArrayOutputStream bos = new ByteArrayOutputStream((int)f.length());
    BufferedInputStream in = null;
    try
    {
      in = new BufferedInputStream(new FileInputStream(f));
      int buf_size = 1024;
      byte[] buffer = new byte[buf_size];
      int len = 0;
      while (-1 != (len = in.read(buffer, 0, buf_size))) {
        bos.write(buffer, 0, len);
      }
      return bos.toByteArray();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        in.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      bos.close();
    }
  }
  
  public static byte[] toByteArray2(String filePath)
    throws IOException
  {
    File f = new File(filePath);
    if (!f.exists()) {
      throw new FileNotFoundException(filePath);
    }
    FileChannel channel = null;
    FileInputStream fs = null;
    try
    {
      fs = new FileInputStream(f);
      channel = fs.getChannel();
      ByteBuffer byteBuffer = ByteBuffer.allocate((int)channel.size());
      while (channel.read(byteBuffer) > 0) {}
      return byteBuffer.array();
    }
    catch (IOException e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        channel.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      try
      {
        fs.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
  
  public static byte[] toByteArray3(String filePath)
    throws IOException
  {
    FileChannel fc = null;
    RandomAccessFile rf = null;
    try
    {
      rf = new RandomAccessFile(filePath, "r");
      fc = rf.getChannel();
      MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0L, 
        fc.size()).load();
      
      byte[] result = new byte[(int)fc.size()];
      if (byteBuffer.remaining() > 0) {
        byteBuffer.get(result, 0, byteBuffer.remaining());
      }
      return result;
    }
    catch (IOException e)
    {
      e.printStackTrace();
      throw e;
    }
    finally
    {
      try
      {
        rf.close();
        fc.close();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}

package com.ssc.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

public class Freemarker
{
  public static void print(String ftlName, Map<String, Object> root, String ftlPath)
    throws Exception
  {
    try
    {
      Template temp = getTemplate(ftlName, ftlPath);
      temp.process(root, new PrintWriter(System.out));
    }
    catch (TemplateException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void printFile(String ftlName, Map<String, Object> root, String outFile, String filePath, String ftlPath)
    throws Exception
  {
    try
    {
      File file = new File(PathUtil.getClasspath() + filePath + outFile);
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
      Template template = getTemplate(ftlName, ftlPath);
      template.process(root, out);
      out.flush();
      out.close();
    }
    catch (TemplateException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static Template getTemplate(String ftlName, String ftlPath)
    throws Exception
  {
    try
    {
      Configuration cfg = new Configuration();
      cfg.setEncoding(Locale.CHINA, "utf-8");
      cfg.setDirectoryForTemplateLoading(new File(PathUtil.getClassResources() + "/ftl/" + ftlPath));
      return cfg.getTemplate(ftlName);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}

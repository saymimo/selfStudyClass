package com.ssc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ItemExcelReport
  extends AbstractExcelView
{
  protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    Date date = new Date();
    String filename = Tools.date2Str(date, "yyyyMMddHHmmss");
    
    String excelName = (String)model.get("excelName");
    
    String perview = (String)model.get("perview");
    String year = (String)model.get("year");
    String chanzhi = (String)model.get("chanzhi");
    

    String name = "工程情况月报表";
    if ((year != null) && (!"".equals(year)))
    {
      if ((chanzhi != null) && (!"".equals(chanzhi))) {
        name = "工程产值年报表";
      } else {
        name = "工程情况年报表";
      }
    }
    else if ((chanzhi != null) && (!"".equals(chanzhi))) {
      name = "工程产值月报表";
    } else {
      name = "工程情况月报表";
    }
    name = new String(name.toString().getBytes(), "ISO8859-1");
    filename = name + filename;
    

    response.setContentType("application/octet-stream");
    response.setHeader("Content-Disposition", "attachment;filename=" + filename + ".xls");
    HSSFSheet sheet = workbook.createSheet("sheet1");
    
    List<String> titles = (List)model.get("titles");
    int len = titles.size();
    HSSFCellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setAlignment((short)2);
    headerStyle.setVerticalAlignment((short)1);
    HSSFFont headerFont = workbook.createFont();
    headerFont.setBoldweight((short)700);
    headerFont.setFontHeightInPoints((short)11);
    headerStyle.setFont(headerFont);
    short width = 20;short height = 500;
    sheet.setDefaultColumnWidth(width);
    for (int i = 0; i < len; i++) {
      if (i == 0)
      {
        String title = (String)titles.get(i);
        HSSFCell cell = getCell(sheet, 0, i);
        cell.setCellStyle(headerStyle);
        setText(cell, title);
        if ((chanzhi != null) && (!"".equals(chanzhi))) {
          sheet.addMergedRegion(new Region(0, (short)0, 0, (short)6));
        } else {
          sheet.addMergedRegion(new Region(0, (short)0, 0, (short)16));
        }
      }
      else
      {
        String title = (String)titles.get(i);
        HSSFCell cell = getCell(sheet, 1, i - 1);
        cell.setCellStyle(headerStyle);
        setText(cell, title);
      }
    }
    sheet.getRow(0).setHeight(height);
    
    HSSFCellStyle contentStyle = workbook.createCellStyle();
    contentStyle.setAlignment((short)2);
    contentStyle.setWrapText(true);
    
    HSSFCellStyle contentStyleLeft = workbook.createCellStyle();
    contentStyleLeft.setAlignment((short)1);
    contentStyleLeft.setWrapText(true);
    List<PageData> varList = (List)model.get("varList");
    int varCount = varList.size();
    for (int i = 0; i < varCount; i++)
    {
      PageData vpd = (PageData)varList.get(i);
      for (int j = 0; j < len - 1; j++)
      {
        String varstr = vpd.getString("var" + (j + 1)) != null ? vpd.getString("var" + (j + 1)) : "";
        HSSFCell cell = getCell(sheet, i + 2, j);
        cell.setCellStyle(contentStyle);
        setText(cell, varstr);
      }
    }
    if ((!"".equals(perview)) && (perview != null))
    {
      FileOutputStream fout = null;
      String filePath = (String)model.get("filePathPerview");
      try
      {
        Map<String, Object> retMap = new HashMap();
        




        File file = new File(filePath);
        if (!file.exists()) {
          try
          {
            file.createNewFile();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
        fout = new FileOutputStream(file);
        workbook.write(fout);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        fout.close();
      }
    }
  }
}

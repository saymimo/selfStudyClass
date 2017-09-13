package com.ssc.quartz;

import com.ssc.util.FileUpload;
import java.io.PrintStream;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class RemoveFilePerview
  extends QuartzJobBean
{
  protected void executeInternal(JobExecutionContext arg0)
    throws JobExecutionException
  {
    System.out.println("开始删除文件");
    FileUpload.delAllFile(FileUpload.perview);
    System.out.println("删除文件结束");
  }
}

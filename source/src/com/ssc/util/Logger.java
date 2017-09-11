package com.ssc.util;

public class Logger
{
  private org.apache.log4j.Logger logger;
  
  private Logger(org.apache.log4j.Logger log4jLogger)
  {
    this.logger = log4jLogger;
  }
  
  public static Logger getLogger(Class classObject)
  {
    return new Logger(org.apache.log4j.Logger.getLogger(classObject));
  }
  
  public static Logger getLogger(String loggerName)
  {
    return new Logger(org.apache.log4j.Logger.getLogger(loggerName));
  }
  
  public void debug(Object object)
  {
    this.logger.debug(object);
  }
  
  public void debug(Object object, Throwable e)
  {
    this.logger.debug(object, e);
  }
  
  public void info(Object object)
  {
    this.logger.info(object);
  }
  
  public void info(Object object, Throwable e)
  {
    this.logger.info(object, e);
  }
  
  public void warn(Object object)
  {
    this.logger.warn(object);
  }
  
  public void warn(Object object, Throwable e)
  {
    this.logger.warn(object, e);
  }
  
  public void error(Object object)
  {
    this.logger.error(object);
  }
  
  public void error(Object object, Throwable e)
  {
    this.logger.error(object, e);
  }
  
  public void fatal(Object object)
  {
    this.logger.fatal(object);
  }
  
  public String getName()
  {
    return this.logger.getName();
  }
  
  public org.apache.log4j.Logger getLog4jLogger()
  {
    return this.logger;
  }
  
  public boolean equals(Logger newLogger)
  {
    return this.logger.equals(newLogger.getLog4jLogger());
  }
}

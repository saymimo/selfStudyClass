package PluSoft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import oracle.sql.CLOB;

public class DBUtil
{
  public static String driver = "com.mysql.jdbc.Driver";
  public static String url = "jdbc:mysql://127.0.0.1:3306/zxk?useUnicode=true&characterEncoding=utf-8";
  public static String user = "root";
  public static String pwd = "root";
  
  private int toInt(Object o)
  {
    if (o == null) {
      return 0;
    }
    double d = Double.parseDouble(o.toString());
    int i = 0;
    i = (int)(i - d);
    return -i;
  }
  
  private float toFloat(Object o)
  {
    if (o == null) {
      return 0.0F;
    }
    float d = Float.parseFloat(o.toString());
    float i = 0.0F;
    i -= d;
    return -i;
  }
  
  private Connection getConn()
    throws Exception
  {
    Class.forName(driver).newInstance();
    Connection conn = null;
    if ((user == null) || (user.equals(""))) {
      conn = DriverManager.getConnection(url);
    } else {
      conn = DriverManager.getConnection(url, user, pwd);
    }
    return conn;
  }
  
  private static ArrayList ResultSetToList(ResultSet rs)
    throws Exception
  {
    ResultSetMetaData md = rs.getMetaData();
    int columnCount = md.getColumnCount();
    ArrayList list = new ArrayList();
    while (rs.next())
    {
      Map rowData = new HashMap(columnCount);
      for (int i = 1; i <= columnCount; i++)
      {
        Object v = rs.getObject(i);
        if ((v != null) && ((v.getClass() == java.util.Date.class) || (v.getClass() == java.sql.Date.class)))
        {
          Timestamp ts = rs.getTimestamp(i);
          v = new java.util.Date(ts.getTime());
        }
        else if ((v != null) && (v.getClass() == CLOB.class))
        {
          v = clob2String((CLOB)v);
        }
        rowData.put(md.getColumnName(i), v);
      }
      list.add(rowData);
    }
    return list;
  }
  
  private static String clob2String(CLOB clob)
    throws Exception
  {
    return clob != null ? clob.getSubString(1L, (int)clob.length()) : null;
  }
}

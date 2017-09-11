package com.ssc.entity;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BaseBody
  implements Serializable
{
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this);
  }
}

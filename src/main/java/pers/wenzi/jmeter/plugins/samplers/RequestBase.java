package pers.wenzi.jmeter.plugins.samplers;

import java.io.Serializable;

public class RequestBase
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private String appKey;
  private Object bizContent;
  private String signValue;
  private String timestamp;
  private String version;
  
  public String getAppKey()
  {
    return this.appKey;
  }
  
  public void setAppKey(String appKey)
  {
    this.appKey = appKey;
  }
  
  public Object getBizContent()
  {
    return this.bizContent;
  }
  
  public void setBizContent(Object bizContent)
  {
    this.bizContent = bizContent;
  }
  
  public String getSignValue()
  {
    return this.signValue;
  }
  
  public void setSignValue(String signValue)
  {
    this.signValue = signValue;
  }
  
  public String getTimestamp()
  {
    return this.timestamp;
  }
  
  public void setTimestamp(String timestamp)
  {
    this.timestamp = timestamp;
  }
  
  public String getVersion()
  {
    return this.version;
  }
  
  public void setVersion(String version)
  {
    this.version = version;
  }
}
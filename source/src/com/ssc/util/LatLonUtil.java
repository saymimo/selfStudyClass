package com.ssc.util;

import java.io.PrintStream;

public class LatLonUtil
{
  private static final double PI = 3.14159265D;
  private static final double EARTH_RADIUS = 6378137.0D;
  private static final double RAD = 0.0174532925199433D;
  
  public static double[] getAround(double lat, double lon, int raidus)
  {
    Double latitude = Double.valueOf(lat);
    Double longitude = Double.valueOf(lon);
    
    Double degree = Double.valueOf(111293.63611111112D);
    double raidusMile = raidus;
    
    Double dpmLat = Double.valueOf(1.0D / degree.doubleValue());
    Double radiusLat = Double.valueOf(dpmLat.doubleValue() * raidusMile);
    
    Double minLat = Double.valueOf(latitude.doubleValue() - radiusLat.doubleValue());
    
    Double maxLat = Double.valueOf(latitude.doubleValue() + radiusLat.doubleValue());
    
    Double mpdLng = Double.valueOf(degree.doubleValue() * Math.cos(latitude.doubleValue() * 0.0174532925D));
    Double dpmLng = Double.valueOf(1.0D / mpdLng.doubleValue());
    Double radiusLng = Double.valueOf(dpmLng.doubleValue() * raidusMile);
    
    Double minLng = Double.valueOf(longitude.doubleValue() - radiusLng.doubleValue());
    
    Double maxLng = Double.valueOf(longitude.doubleValue() + radiusLng.doubleValue());
    
    System.out.println("jingdu" + minLat + "weidu" + minLng + "zuidajingdu" + 
      maxLat + "zuidaweidu" + maxLng);
    
    return new double[] { minLat.doubleValue(), minLng.doubleValue(), maxLat.doubleValue(), maxLng.doubleValue() };
  }
  
  public static void main(String[] src)
  {
    getAround(36.68027D, 117.12744000000001D, 1000);
  }
}

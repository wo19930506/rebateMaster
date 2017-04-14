package com.edm.utils;

public class StreamToString {
	
	public static String getStringByUTF8(String object){
		try{
		   if(object != null && object !="" ){
			   byte source [] = object.getBytes("iso8859-1");//得到form提交的原始数据(默认采用iso8859-1编码)
			   object = new String (source,"UTF-8");//解决乱码
			   return object;
		   }	
		   return null;
		}catch(Exception e){
		   e.getMessage();	
		   return null;
		}
		
	}
}
package com.edm.utils;

import java.util.Date;

public class DateUtil {
	
	    public static int compareMills(Date afterDate,Date beforeDate){

		     long intervalMilli = afterDate.getTime() - beforeDate.getTime();
		     return (int) (intervalMilli /(1000));	 
		}
	    
	    public static int compareDay(Date afterDate,Date beforeDate){

		     long intervalMilli = afterDate.getTime() - beforeDate.getTime();
		     return (int) (intervalMilli / (24*60*60*(1000)));	 
		}

}

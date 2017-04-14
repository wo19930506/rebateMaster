package com.edm.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;

import com.edm.utils.except.Errors;
import com.edm.utils.web.R;
import com.edm.utils.web.Validator;

public class Ntps {
	
/*
    public static DateTime get() {
		try {
			NTPUDPClient client = new NTPUDPClient();
			InetAddress address = InetAddress.getByName("time.windows.com");
			TimeStamp time = client.getTime(address).getMessage().getTransmitTimeStamp();
			return new DateTime(time.getDate());
		} catch (Exception e) {
			throw new Errors("(Ntps:net) error: ", e);
		}
	}
 */
	public static DateTime get() {
		HttpClient http = new DefaultHttpClient();
		try {
			HttpGet get = new HttpGet("http://203.88.210.79/sj.php");
			HttpResponse response = http.execute(get);
			String line = response.getStatusLine().toString();
			get.abort();
			
			String millis = "";
			if (StringUtils.startsWith(line, HttpCode._200)) {
				millis = StringUtils.trim(EntityUtils.toString(response.getEntity()));
			}
			millis = (String) Validator.validate(millis, R.CLEAN, R.REQUIRED, R.LONG);
			
			if (StringUtils.isBlank(millis)) {
				throw new Error("millis is null");
			}
			return new DateTime(Long.valueOf(millis));
		} catch (Exception e) {
			throw new Errors("Ntps time is error", e);
		} finally {
			http.getConnectionManager().shutdown();
		}
	}
}

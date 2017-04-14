package com.edm.utils.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edm.modules.utils.Property;
import com.edm.utils.Converts;
import com.edm.utils.except.Errors;
import com.edm.utils.file.Files;

public class JoinApi {

    private static final Logger logger = LoggerFactory.getLogger(JoinApi.class);
    
    public static String snatch(String filePath) {
        HttpClient client = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            String name = StringUtils.substring(filePath, StringUtils.lastIndexOf(filePath, "/") + 1);
            
            String[] arr = Converts._toStrings(StringUtils.replace(name, ".", ","));
            name = arr[0] + "." + arr[1] + "." + arr[2] + ".txt";
            
            client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://" + "XXXX" + filePath);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
                writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("XXXX"), "UTF-8")));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.write("\n");
                }
                writer.flush();
            }
            get.abort();
            return name;
        } catch (Exception e) {
            logger.warn("(JoinApi:snatch) filePath: " + filePath + ", warn: ", e);
            throw new Errors("抓取API封装文件失败");
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
            client.getConnectionManager().shutdown();
        }
    }
    
	public static String read(String filePath) {
		String path = null;
		PrintWriter writer = null;
		try {
			path = Property.getStr("XXXX");
			String name = StringUtils.substring(filePath, StringUtils.lastIndexOf(filePath, "/") + 1);

			String[] arr = Converts._toStrings(StringUtils.replace(name, ".", ","));
			name = arr[0] + "." + arr[1] + "." + arr[2] + ".txt";
			String content = Files.get(path, filePath);
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("XXXX"), "UTF-8")));
			writer.write(content);
			writer.flush();
			
			return name;
		} catch (Exception e) {
			 logger.warn("(JoinApi:read) filePath: " + path + filePath + ", warn: ", e);
			throw new Errors("读取API封装文件失败");
		} finally {
			 IOUtils.closeQuietly(writer);
		}
    }
	
}

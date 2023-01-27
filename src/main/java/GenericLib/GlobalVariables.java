package GenericLib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import lombok.extern.log4j.Log4j2;

		// Read the data from the config file



@Log4j2
public class GlobalVariables {
	
	public static RequestSpecBuilder builder = new RequestSpecBuilder();
	public static String endPoint = null;
	public static String API_KEY = null;
	public static String Uselog = null;
	public static String Query = null;
	public static String Authorization = null;
	
	//Initialize key variable from ini file
	public static String readIniValue(String key) {
		String temp = "";
		Boolean keyExist = false;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("./src/main/resources/config.ini"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Enumeration<?> enumeration = prop.propertyNames();
		while(enumeration.hasMoreElements()) {
			String tempKey = (String) enumeration.nextElement();
			if(tempKey.equals(key)) {
				temp=prop.getProperty(key);
				keyExist = true;
			}
		}
		
		if(!keyExist) {
			log.error("Key:"+key+" is not specified in the Ini file");
		}
		return temp;		
	}
	
	public static String getEndPoint(String endPnt) {
		endPoint = readIniValue(endPnt);
		return endPoint;
	}
	
	public static String getReqBody(String req) {
		String reqBody = readIniValue(req);
		return reqBody;
	}
	
	public static String getApiKey() {
		API_KEY = readIniValue("api_key");
		return API_KEY;
	}
	
	public static String getUselog() {
		Uselog = readIniValue("uselog");
		return Uselog;
	}
	
	public static String getQuery(String query) {
		Query = readIniValue(query);
		return Query;
	}
	
	public static String getAuthorization() {
		Authorization = readIniValue("authorization");
		return Authorization;
	}

}

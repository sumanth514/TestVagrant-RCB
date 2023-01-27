package GenericLib;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

import static GenericLib.GlobalVariables.getReqBody;
import static GenericLib.Utilities.*;
import static org.junit.Assert.fail;

import io.restassured.path.json.JsonPath;
import java.util.ArrayList;

import static GenericLib.GlobalVariables.*;



	//Read the Json file

@Log4j2
public class PayLoads {
			
	public static JsonPath jsonPathEvaluator;
           public static String jsonBody=null;
           public static String savedReq=null;
	public static HashMap<String, String> createListPayloadWithExcelAttributes1(String filename,String excel_sheet,String testcase){
		HashMap<String, String> requestBody = new HashMap<String, String>();
		 ObjectMapper mapper = new ObjectMapper();
		 String json=null;
		try {
		    FileInputStream inputStream = new FileInputStream(new File(filename));
		    Workbook workbook = new XSSFWorkbook(inputStream);
		    Sheet sheet = workbook.getSheet(excel_sheet);
		    int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
		    log.info("Excel Sheet "+excel_sheet+" fetched");
		    for (int i = 1; i < rowCount+1; i++) {
		        Row row = sheet.getRow(i);
		        	if(NumberToTextConverter.toText(row.getCell(0).getNumericCellValue()).contains(testcase)) {
		        		 log.info("Found Testcase:"+NumberToTextConverter.toText(row.getCell(0).getNumericCellValue()));
		        		 json=new String(row.getCell(1).getStringCellValue());
		        		 break;
		        }
		    } 
		  
			requestBody = mapper.readValue(json, HashMap.class);
			log.info("RequestBody:"+requestBody);
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.toString());
		}
		return requestBody;
	}
	
	
	public static HashMap<String, String> createListPayloadWithExcelAttributes(){
		HashMap<String, String> requestBody = new HashMap<String, String>();
		 ObjectMapper mapper = new ObjectMapper();
		 String json=null;
		
		    json=new String(getData(getReqBody("requestBody")));
				if(json.contains(".json")){
					String requestFile = System.getProperty("user.dir") + json;
					StringBuilder builder = new StringBuilder();
					try (BufferedReader buffer = new BufferedReader(
							new FileReader(requestFile))) {
						String str;
						while ((str = buffer.readLine()) != null) {
							builder.append(str).append("\n");
						}
					}catch (IOException  e){
						log.error(e.toString());
						fail();
					}
					json = builder.toString();
				}
		    jsonBody=json;
			try {
				requestBody = mapper.readValue(json, HashMap.class);
			} catch (JsonMappingException e) {
				log.error(e.toString());
				fail();
			} catch (JsonProcessingException e) {
				log.error(e.toString());
				fail();
			}
			log.info("RequestBody extracted");
		 
		return requestBody;
	}
	
	public static String createListPayloadWithExcelAttributesasString(){
		 jsonBody=new String(getData(getReqBody("requestBody")));
			log.info("RequestBody extracted");
		return jsonBody;
	}
	

public static JsonPath reqBody(){
	JsonPath jp= new JsonPath(jsonBody);
	return jp;
}

public static JsonPath jsonReader() throws IOException {
	
	 File f = new File("//src//main//resources");
	 JsonPath json = null;
     if (f.exists()){
         InputStream is = new FileInputStream("rcbjson.json");
         String jsonTxt = IOUtils.toString(is, "UTF-8");
         log.info("Now the reterived jsontext is:"+jsonTxt);
          json = new JsonPath(jsonTxt); 
	
}
	return json;
}



}


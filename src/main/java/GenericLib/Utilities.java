package GenericLib;

import static GenericLib.GlobalVariables.*;
import static GenericLib.PayLoads.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.containsString;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.junit.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.RequestSpecification;
import lombok.extern.log4j.Log4j2;


		// Common Utilities 
	
@Log4j2
public class Utilities {

    public static Response response;
    public static JsonPath jsonPathEvaluator;
    public static RequestSpecification request;
    public static HashMap<String, String> requestBody;
    public static String path;
    public static String jsonBody;
    public static List<String> timestamps;
    public static List<String> segments;
    public static FileInputStream inputStream;
    public static Workbook workbook;
    public static Sheet sheet;
    public static int testcaseIndex;
    public static String retrieveObjectKey;
    public static JSONObject retrievedObjectValue;
    public static String resBody;

    public static void setURI(String endpoint, String path) {
        builder = new RequestSpecBuilder();
        builder.setBaseUri(getEndPoint(endpoint) + path);
        builder.setRelaxedHTTPSValidation();
        log.info("URI:" + getEndPoint(endpoint) + path);
    }

    public static void setAuthorization() {
        builder.addHeader("Authorization", getAuthorization());
        log.info("Authorization set");
    }

    public static void setContentType(String contentType) {
        try {
            switch (contentType) {
                case "application/json":
                    builder.setContentType(ContentType.JSON);
                    log.info("ContentType set:" + contentType);
                    break;
                case "application/xml":
                    builder.setContentType(ContentType.XML);
                    log.info("ContentType set" + contentType);
                    break;
                default:
                    log.info("Provide a valid content type");
            }

            builder.setRelaxedHTTPSValidation();

        } catch (Exception error) {
            log.error(error);
        }

    }

    public static void setXSessionId(String sessionId) {
        builder.addHeader("X-SessionId", sessionId);
        builder.setRelaxedHTTPSValidation();
        log.info("Header X-SessionId set");

    }

    public static void setHeader(String header, String data) {
        builder.addHeader(header, data);
        builder.setRelaxedHTTPSValidation();
        log.info("Header " + header + " set with value " + data);
    }

    public static void removeHeader(String header) {
        FilterableRequestSpecification filterableRequestSpecification = (FilterableRequestSpecification) request;
        filterableRequestSpecification.removeHeader(header);
        builder = new RequestSpecBuilder();
        builder.addRequestSpecification(filterableRequestSpecification);
    }

    public static void setQueryAccessAPI() {
        builder.addQueryParam("api_key", getApiKey());
        log.info("Query AccessAPI set");
    }

    public static void setQueryUselog() {
        builder.addQueryParam("uselog", getUselog());
        log.info("Query Uselog set");
    }

    public static void setQuery(String query, String data) {
        builder.addQueryParam(query, data);
        log.info("Query parameter " + query + " set");
    }

    public static void generateExcelPayload() {
        try {
            requestBody = createListPayloadWithExcelAttributes();
        } catch (Exception error) {
            log.error(error);
        }
    }

    public static void generateExcelPayloadasString() {
        try {
            jsonBody = createListPayloadWithExcelAttributesasString();
        } catch (Exception error) {
            log.error(error);
            fail();
        }
    }

    public static void generatePayloadasString(String reqBody) {
        try {
            jsonBody = reqBody;
        } catch (Exception error) {
            log.error(error);
            fail();
        }
    }

    public static void httpMethod(String method) {
        try {
            RequestSpecification requestSpec = builder.build();
            request = RestAssured.given().spec(requestSpec);
            switch (method) {
                case "POST":
                    response = request.body(requestBody).post();
                    log.info("POST Request sent");
                    break;
                case "GET":
                    response = request.get();
                    log.info("GET Request sent");
                    break;
                case "POST_STRING":
                    response = request.body(jsonBody).post();
                    log.info("POST Request sent");
                    break;
                default:
                    log.info("Provide a valid http method");
            }
        } catch (Exception error) {
            log.error(error);
            fail();
        }
    }


    public static void statusCode(String statusCode) {
        try {
            Assert.assertEquals(statusCode, Integer.toString(response.getStatusCode()));
            log.info("Status code:" + response.getStatusCode());
        } catch (AssertionError error) {
            log.error(error);
            error.printStackTrace();
            fail();
        } catch (Exception error) {
            log.error(error);
            error.printStackTrace();
            fail();
        }
    }



    public static void extractJson() {

        try {
            jsonPathEvaluator = response.jsonPath();
            log.info("Response Json data extracted");
            resBody = response.asString();
            log.info(response.statusLine());
        } catch (Exception error) {
            log.error(error);
            fail();
        }

    }

    
    public static void extract() throws IOException {
    	
    	 jsonPathEvaluator = jsonReader();
    }


    public static void setPath(String pathh) {
        path = pathh;
    }

    public static String getPath() {
        return path;
    }


    public static void setAuth(String token) {

        builder.addHeader("Authorization", "Bearer " + token);
        log.info("Token is set");

    }

    public static void setAccept(String accept) {
        builder.setAccept(accept);
        log.info("Accept set:" + accept);
    }

    public static void setExcelSheet(String excelname) throws IOException {
        try {
            inputStream = new FileInputStream(new File("src/test/resources/TestDatas/" + excelname));
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheet("UserStory");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Sheet getExcelSheet() {
        return sheet;
    }

    public static int getColumnIndex(String colname) {
        int index = -1;
        try {
            Row colName = getExcelSheet().getRow(0);
            for (int i = 0; i < colName.getLastCellNum(); i++) {
                if (colName.getCell(i).getStringCellValue().contentEquals(colname)) {
                    index = i;
                    break;
                }

            }
        } catch (Exception error) {
            log.error("Column not found");
            fail("Column not found");
        }
        return index;
    }

    public static void setTestcaseRowIndex(int index) {
        testcaseIndex = index;
    }

    public static int getTestcaseRowIndex() {
        return testcaseIndex;
    }

    public static void setTestCase(String testcase) {
        int rowCount = getExcelSheet().getLastRowNum() - getExcelSheet().getFirstRowNum();
        int headerColNum = getColumnIndex("Test_Case_Id");

        for (int i = 1; i < rowCount + 1; i++) {
            Row row = getExcelSheet().getRow(i);
            if (row.getCell(headerColNum).getStringCellValue().contentEquals(testcase)) {
                setTestcaseRowIndex(i);
                log.info("Found Testcase:" + row.getCell(headerColNum).getStringCellValue());
                break;
            }
        }
    }

    public static String getData(String colname) {
        int headerColNum = getColumnIndex(colname);
        Row row1 = getExcelSheet().getRow(getTestcaseRowIndex());
        String data = null;
        if (colname.equals("Status_Code")) {
            int scode = (int) row1.getCell(headerColNum).getNumericCellValue();

            data = Integer.toString(scode);
        } else {
            data = row1.getCell(headerColNum).getStringCellValue();
        }
        return data;
    }
   
      public static void noSessionId() {
        Object formData = jsonPathEvaluator.get("routing.settingsAnchor.formData");
        Assert.assertEquals(formData, containsString("{}"));
        log.info("No SessionId");
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void retrieveJSON(String object) {
        retrieveObjectKey = object;
        retrievedObjectValue = new JSONObject(new HashMap<Object, Object>());
        System.out.println("RetrievedData:" + retrievedObjectValue);
        log.info(object + " retrieved");
    }



    public static void addRetrieveData() {
        String updatedJson1;
        if (retrieveObjectKey.contains(".")) {
            List<String> keys = Arrays.asList(retrieveObjectKey.split("\\."));
            int position = retrieveObjectKey.lastIndexOf(".");
            String path = retrieveObjectKey.substring(0, position);
            updatedJson1 = com.jayway.jsonpath.JsonPath.parse(requestBody).put(com.jayway.jsonpath.JsonPath.compile("$." + path), keys.get(keys.size() - 1), retrievedObjectValue.toMap()).jsonString();
        } else {
            updatedJson1 = com.jayway.jsonpath.JsonPath.parse(requestBody).put(com.jayway.jsonpath.JsonPath.compile("$"), retrieveObjectKey, retrievedObjectValue.toMap()).jsonString();
        }
        try {
            requestBody = new ObjectMapper().readValue(updatedJson1, HashMap.class);
            log.info("RetrievedData added in request");
        } catch (JsonMappingException error) {
            log.error(error);
        } catch (JsonProcessingException error) {
            log.error(error);
        }
    }

    private static final Configuration configuration = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .build();
 	

}

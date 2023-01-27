package GenericLib;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


   //Generate the reports

public class JsonReader {

	private static final InfluxDB INFLUXDB = InfluxDBFactory.connect("http://localhost:8086", "root", "root");
	private static final String DATABASE = "rwatestsuite";
	
	static {
		INFLUXDB.setDatabase(DATABASE);
	}
	
	public static void send(final Point point) {
		INFLUXDB.write(point);
	}
	
	public static void main(String[] args) {
		JSONParser parser = new JSONParser();
		String scenario = null;
		String feature = null;
		
		try {
			Object obj = parser.parse(new java.io.FileReader("target/report/regression/json-report.json"));
			JSONArray rootArray = (JSONArray) obj;
			for(int i=0;i<rootArray.size();i++) {
				JSONObject rootAttrbutes = (JSONObject) rootArray.get(i);
				feature = (String) rootAttrbutes.get("name");
				
				JSONArray elementArray = (JSONArray) rootAttrbutes.get("elements");
				for(int j=0;j<elementArray.size();j++) {
					JSONObject elements = (JSONObject) elementArray.get(j);
					if(elements.get("type").toString().equals("background")) {
						System.out.println("Print type: "+elements.get("type"));
						scenario = "background";
					}else if(elements.get("type").toString().equals("scenario")) {
						scenario = (String) elements.get("name");
						
						Point point = Point.measurement("scenario").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
								.addField("featureName", feature)
								.addField("scenarioName", elements.get("name").toString())
								.addField("scenarioTimeStamp", elements.get("start_timestamp").toString())
								.build();
						send(point);
					}
					
					JSONArray stepsArray = (JSONArray) elements.get("steps");
					for(int k=0;k<stepsArray.size();k++) {
						JSONObject steps = (JSONObject) stepsArray.get(k);
						JSONObject result = (JSONObject) steps.get("result");
						
						Point point = Point.measurement("step").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
								.addField("scenarioName", scenario)
								.addField("stepName", steps.get("name").toString())
								.addField("stepStatus", result.get("status").toString())
								.build();
						send(point);
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}

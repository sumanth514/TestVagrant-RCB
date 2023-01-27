package StepDefinitions;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.Assert;

import GenericLib.PayLoads;
import GenericLib.Utilities.*;
import io.cucumber.java.en.And;
import io.restassured.path.json.JsonPath;

public class steps_RCB {
	
	
	
	@And("Verify team has {int} foreign players")
	public void foreignPlayers(int foreignCount) throws IOException {
		int count=0;
		
		JsonPath jsonPathEvaluator=PayLoads.jsonReader();
	ArrayList<String> teamPlayers=jsonPathEvaluator.get("player");
	
	for(int index=0;index<teamPlayers.size();index++) {
		
		String country=jsonPathEvaluator.get("player["+index+"].country");
		
		if(country != "India") {
			count++;
		}
		
	}
	
		Assert.assertEquals(foreignCount, count);
		
	}
	
	@And("Verify team has atleast {int} wicket keeper")
	public void  wicketKeeper(int KeeperCount) throws IOException {
		
		int count=0;
		JsonPath jsonPathEvaluator=PayLoads.jsonReader();
		ArrayList<String> teamPlayers=jsonPathEvaluator.get("player");
		
		for(int index=0;index<teamPlayers.size();index++) {
			
			String role=jsonPathEvaluator.get("player["+index+"].role");
			
			if(role.equals("Wicket-keeper")) {
				count++;
			}
			
		}
		Assert.assertTrue("In team wicket keeper count is one or more ",
				count >= KeeperCount);
		
	}
	

}

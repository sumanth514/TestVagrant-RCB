package runConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

@RunWith(Cucumber.class)
@CucumberOptions(
		features = {"src/test/resources/RCB/RCB.feature"},
		tags = "@US-1234", 
		glue = "",
		plugin = {
				"html:target/testReport/html",
				"json:target/testReport/cucumber-report.json",
				"junit:target/testReport/cucumber-report.xml",
				"rerun:target/failedTests/failed_scenarios.txt"}
)

public class TestRunner {

	@AfterClass
	public static void extendedReport() {
		File reportOutputDirectory = new File("target/testReport/extendedReport");
		List<String> jsonFiles = new ArrayList<>();
		jsonFiles.add("target/testReport/cucumber-report.json");
		String buildNumber = "0";
		String projectName = "Regression Run";

		Configuration configuration = new Configuration(reportOutputDirectory, projectName);
		configuration.setBuildNumber(buildNumber);
		configuration.addClassifications("Platform", "none");
		configuration.addClassifications("Branch", "none");

		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		reportBuilder.generateReports();
	}

}
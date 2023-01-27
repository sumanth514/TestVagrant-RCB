# Introduction 
This section contains the automated test cases for RCB(Royal Challengers Bangalore)

# Build and Test
TODO: Describe and show how to build your code and run the tests. 

# Getting Started
TODO: Guide users through getting your code up and running on their own system. In this section you can talk about:
1.	Installation process
2.	Software dependencies

# Framework Properties
1. Programming Language : JAVA
2. Test Runner : JUNIT
3. Build Tool : Maven
4. Reporting: JUNIT XML report through ADO Test Plans OR Cucumber HTML report or Cucumber Extent report
5. API Library : Rest Assurred
6. Version Control : GIT


# Executing Tests on local machine
1. Place all the env properties/variables along with all client secrets and ids in "src/main/resources/config.ini" file 
2. Use the method "readIniValue" in "src/main/java/GenericLib/GlobalVariables" file. This method uses the config file to read env variables.
3. To run tests execute the "src/test/java/runConfig/TestRunner" file or run the following command in terminal "mvn test -Prun -Dcucumber.filter.tags="$(cucumberTags)"" 

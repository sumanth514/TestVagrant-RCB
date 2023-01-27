@US-1234 @Test_Execution
Feature: Default

		#Test case to verify the RCB team has only four foreign players
        @US1234-1 
	    Scenario:US1234-1-verify the RCB team has only four foreign players
		#Given Set "env" api end point with path "specificPath"
		#And Create a payload from "rcbjson.json" for testcase "1234-1"
		#And Create and set header "XSessionId"
		#And Set header "Content_Type"
		#And Set header "Accept"
		#And Set header "X-Trackingid"
		#When Send "POST" request
		#And Retrieve JSON response data
		#Then Verify "Status_Code" 
		And Verify team has "4" foreign players
		
		
		#Test case to verify RCB team has atleast one wicket keeper
        @US1234-2 
	    Scenario:US1234-1-verify the RCB team has atleast one wicket keeper
		#Given Set "env" api end point with path "specificPath"
		#And Create a payload from "rcbjson.json" for testcase "1234-2"
		#And Create and set header "XSessionId"
		#And Set header "Content_Type"
		#And Set header "Accept"
		#And Set header "X-Trackingid"
		#When Send "POST" request
		#And Retrieve JSON response data
		#Then Verify "Status_Code" 
		And Verify team has atleast "1" wicket keeper
		
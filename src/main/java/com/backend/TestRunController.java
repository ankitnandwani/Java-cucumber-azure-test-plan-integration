package com.backend;

import com.Entities.Response.GetTestResult;
import com.google.gson.Gson;
import com.Entities.Request.AddTestResult;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import com.Entities.Response.CreateTestRunResponse;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TestRunController {

    public static final Logger log = LogManager.getLogger(TestRunController.class);
    Gson gson = new Gson();
    RequestUtils requestUtils = new RequestUtils();

    public CreateTestRunResponse createNewTestRun(String url, String body){
        log.info("Body : " + body);
        Response response = requestUtils.sendPostWithBodyAndPath(url, getAzureHeaders(), getPathParams(), body);
        String responseString = response.getBody().asString();
        log.info("CreateTestRunResponse : " + responseString);
        Assert.assertEquals(response.getStatusCode(), 200);
        return gson.fromJson(responseString, CreateTestRunResponse.class);
    }

    public AddTestResult updateTestCaseStatus(String url, String runId, String body){
        log.info("Body : " + body);
        Response response = requestUtils.sendPatchWithBodyAndPath(url, getAzureHeaders(), getPathWithRunId(runId), body);
        String responseString = response.getBody().asString();
        log.info("response : " + responseString);
        Assert.assertEquals(response.getStatusCode(), 200);
        return gson.fromJson(responseString, AddTestResult.class);
    }

    public GetTestResult getAllTestResults(String url, String runId){
        Response response = requestUtils.sendGetWithPathParams(url, getAzureHeaders(), getPathWithRunId(runId));
        String responseString = response.getBody().asString();
        log.info("response : " + responseString);
        Assert.assertEquals(response.getStatusCode(), 200);
        return gson.fromJson(responseString, GetTestResult.class);
    }

    public void updateTestRunStatus(String url, String runId, String body){
        log.info("Body : " + body);
        Response response = requestUtils.sendPatchWithBodyAndPath(url, getAzureHeaders(), getPathWithRunId(runId), body);
        String responseString = response.getBody().asString();
        System.out.println("response : " + responseString);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    private Map<String, ?> getPathParams(){
        Map<String, String> params = new HashMap<>();
        params.put("organization", Constants.ORGANIZATION);
        params.put("project", Constants.PROJECT);
        return params;
    }

    private Map<String, ?> getPathWithRunId(String runId){
        Map<String, String> params = (Map<String, String>) getPathParams();
        params.put("runId", runId);
        return params;
    }

    private Map<String, ?> getAzureHeaders(){
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Basic " + Base64.getEncoder().encodeToString((":" + Constants.AZURE_PAT).getBytes()));
        return headers;
    }
}

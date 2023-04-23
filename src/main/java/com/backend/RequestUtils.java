package com.backend;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class RequestUtils {

    public Response sendGetWithPathParams(String url, Map<String, ?> headers, Map<String, ?> params){
        return RestAssured.given().headers(headers).log().all().pathParams(params).get(url);
    }

    public Response sendPostWithBodyAndPath(String url, Map<String, ?> headers, Map<String, ?> params, String body){
        return RestAssured.given().headers(headers).log().all().pathParams(params).body(body).post(url);
    }

    public Response sendPatchWithBodyAndPath(String url, Map<String, ?> headers, Map<String, ?> params, String body){
        return RestAssured.given().headers(headers).log().all().pathParams(params).body(body).patch(url);
    }
}

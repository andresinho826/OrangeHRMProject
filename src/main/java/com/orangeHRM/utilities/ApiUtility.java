package com.orangeHRM.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class ApiUtility {

    // method to send Get request
    public static Response sendGetRequest(String endPoint) {
        return RestAssured.get(endPoint);
    }

    // method to send Post request
    public static Response sendPostRequest(String endPoint, String body) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(body)
                .post(endPoint);
    }

    // Method to validate the response status code
    public static boolean validateStatusCode(Response response, int expectedStatusCode) {
        return response.getStatusCode() == expectedStatusCode;
    }

    // Method to extract value from Json response using Json path
    public static String extractValueFromResponse(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }
}

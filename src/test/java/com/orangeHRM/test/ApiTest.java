package com.orangeHRM.test;

import com.orangeHRM.utilities.ApiUtility;
import com.orangeHRM.utilities.ExtentManager;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class ApiTest {

    @Test
    public void verifyGetUserApi() {

        SoftAssert softAssert = new SoftAssert();

        // step 1: define the endpoint
        String endPoint = "https://jsonplaceholder.typicode.com/users/1";
        ExtentManager.logStep("API Endpoint: " + endPoint);

        // step 2: send the GET request
        ExtentManager.logStep("Sending GET request to the endpoint");
        Response response = ApiUtility.sendGetRequest(endPoint);

        // step 3: validate the status code
        ExtentManager.logStep("Validating the response status code");
        boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 201);
        softAssert.assertTrue(isStatusCodeValid, "Expected status code 200 but got " + response.getStatusCode());

        if (isStatusCodeValid) {
            ExtentManager.logStepValidationForAPI("Status code is valid: " + response.getStatusCode());
        } else {
            ExtentManager.logFailureAPI("Status code is invalid: " + response.getStatusCode());
        }

        // step 4: extract and validate the user name from the response
        ExtentManager.logStep("Extracting user name from the response");
        String userName = ApiUtility.extractValueFromResponse(response, "username");
        boolean isUserNameValid = "Bret".equals(userName);
        softAssert.assertTrue(isUserNameValid, "UserName is not valid " + userName);

        if (isUserNameValid) {
            ExtentManager.logStepValidationForAPI("User name is valid: " + userName);
        } else {
            ExtentManager.logFailureAPI("User name is invalid: " + userName);
        }

        // step 5: extract and validate the email from the response
        ExtentManager.logStep("Extracting user name from the response");
        String email = ApiUtility.extractValueFromResponse(response, "email");
        boolean isEmailNameValid = "Sincere@april.biz".equals(email);
        softAssert.assertTrue(isEmailNameValid, "Email is not valid " + email);

        if (isEmailNameValid) {
            ExtentManager.logStepValidationForAPI("Email is valid: " + email);
        } else {
            ExtentManager.logFailureAPI("Email is invalid: " + email);
        }

        softAssert.assertAll();
    }
}

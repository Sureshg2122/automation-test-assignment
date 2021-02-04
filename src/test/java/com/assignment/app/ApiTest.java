package com.assignment.app;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.module.jsv.JsonSchemaValidator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiTest {

    WebDriver driver;
    InputStream propertiesFile;
    Properties prop = new Properties();

    @BeforeClass
    public void setBaseURI() throws IOException {
        // load properties file from resources
        propertiesFile = getClass().getClassLoader().getResourceAsStream("test.properties");
        if (propertiesFile != null) {
            prop.load(propertiesFile);
        } else {
            throw new FileNotFoundException("Property file test.properties not found in the classpath");
        }

        RestAssured.baseURI = prop.getProperty("base-uri");
    }


    @Test(dataProvider = "getData")
    public void eulaVerification(String data) throws IOException, ParseException {


        // verify health status code is 200
        RestAssured.given().get("/healthz").then().statusCode(200);


        // get eulaResponse and compare json schema. Assert eulaResponse is not null and compare with JSON schema
        String eulaResponse = RestAssured.given()
                .queryParam("tenant", data)
                .when()
                .get("/api/v3/rest/default/eula")
                .then()
                .statusCode(200)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("apiresponseschema.json"))
                .body("isEmpty()", Matchers.is(false))
                .extract().
                        response().
                        getBody().asString();

        Assert.assertNotNull(eulaResponse);

    }

    @DataProvider
    public Object[] getData() throws IOException, ParseException {
        FileReader reader = new FileReader(getClass().getClassLoader().getResource("apidata.json").getFile());

        if (reader == null) {
            throw new FileNotFoundException("api data file apidata.json not found in the classpath");
        }

        JSONParser jsonParser = new JSONParser();

        Object obj = jsonParser.parse(reader);

        JSONArray jsonArray = (JSONArray) obj;

        Object[] data = new Object[jsonArray.size()];

        int i = 0;

        for (Object jsonObject : jsonArray) {
            data[i] = ((JSONObject) jsonObject).get("tenantid");
            i++;
        }

        return data;
    }
}

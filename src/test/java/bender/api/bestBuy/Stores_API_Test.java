package bender.api.bestBuy;

import bender.testData.TestData;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import javax.json.Json;
import javax.json.JsonObject;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by A.Bartolome 11/01/2019
 *
 * STORES ENDPOINT
 *
 */
public class Stores_API_Test {

    private String endpoint = TestData.STORES_ENDPOINT;

    /**
     * GET /stores - 200
     * Returns all stores that match the given filter criteria. If no filters are included, defaults to returning a paginated list of all stores.
     */
    @Test
    public void GET_200() {
        RestAssured.baseURI = TestData.baseURI;

        given()
                .contentType("application/json; charset=utf-8")
                .expect()
                .statusCode(200)
                .log().all().request().and().response()

                .when()
                .get(endpoint);
    }

    /**
     * POST /store - 200
     * Creates a new store
     */
    @Test
    public void POST_200() {
        RestAssured.baseURI = TestData.baseURI;

        JsonObject request = Json.createObjectBuilder()
                .add("name",  TestData.TEST_NAME)
                .add("type", "Test type")
                .add("address", "Test type")
                .add("address2", "99.9")
                .add("city", "cartoons")
                .add("state", "1.2")
                .add("zip", "Test type")
                .add("lat", 0)
                .add("lng", 0)
                .add("hours", "1.2")
                .add("services", Json.createObjectBuilder())
                .build();

        JsonPath response = given()
                .contentType("application/json")
                .body(request.toString())
                .expect()
                .statusCode(201)
                .body("name", equalTo( TestData.TEST_NAME))
                .log().all().request().and().response()

                .when()
                .post(endpoint).jsonPath();
    }

    /**
     * POST /store - 400 - Invalid Parameters
     * Creates a new store
     * Missing or empty field(s) will return Invalid Parameters
     */
    @Test
    public void POST_400() {
        RestAssured.baseURI = TestData.baseURI;

        JsonObject request = Json.createObjectBuilder()
                // Missing "name" will return 400
                //.add("name",  TestData.TEST_NAME)
                .add("type", "Test type")
                .add("address", "Test type")
                .add("address2", "99.9")
                .add("city", "cartoons")
                .add("state", "1.2")
                .add("zip", "Test type")
                .add("lat", 0)
                .add("lng", 0)
                .add("hours", "1.2")
                .add("services", Json.createObjectBuilder())
                .build();

        JsonPath response = given()
                .contentType("application/json")
                .body(request.toString())
                .expect()
                .statusCode(400)
                .body("message", equalTo("Invalid Parameters"))
                .log().all().request().and().response()

                .when()
                .post(endpoint).jsonPath();
    }


    /**
     * DELETE /store/{id} - 200
     * Deletes a single store based on the ID supplied
     * First will create a new store to get an ID, then Delete it
     */
    @Test
    public void DELETE_200() {
        RestAssured.baseURI = TestData.baseURI;

        JsonObject request = Json.createObjectBuilder()
                .add("name",  TestData.TEST_NAME)
                .add("type", "Test type")
                .add("address", "Test type")
                .add("address2", "99.9")
                .add("city", "cartoons")
                .add("state", "1.2")
                .add("zip", "Test type")
                .add("lat", 0)
                .add("lng", 0)
                .add("hours", "1.2")
                .add("services", Json.createObjectBuilder())
                .build();

        // Create a Store
        JsonPath response = given()
                .contentType("application/json")
                .body(request.toString())
                .expect()
                .statusCode(201)
                .body("name", equalTo( TestData.TEST_NAME))
                .log().all().request().and().response()

                .when()
                .post(endpoint).jsonPath();

        // Delete the Store
        given()
                .contentType("application/json; charset=utf-8")
                .expect()
                .statusCode(200)
                .body("name", equalTo( TestData.TEST_NAME))
                .log().all().request().and().response()

                .when()
                .delete(endpoint + response.get("id"));
    }

    /**
     * DELETE /stores/{id} - 404
     * Deletes a non-existing store, response will be 404
     */
    @Test
    public void DELETE_404() {
        RestAssured.baseURI = TestData.baseURI;

        given()
                .contentType("application/json; charset=utf-8")
                .expect()
                .statusCode(404)
                .log().all().request().and().response()

                .when()
                .delete(endpoint + TestData.NON_EXISTING_ID);
    }

    /**
     * GET /stores/{id} - 200
     * Returns a store based on an ID,
     *
     */
    @Test
    public void GET_ID_200() {
        RestAssured.baseURI = TestData.baseURI;

        given()
                .contentType("application/json; charset=utf-8")
                .expect()
                .statusCode(200)
                .body("id", equalTo(TestData.STORE_ID),
                        "name", equalTo(TestData.STORE_NAME))
                .log().all().request().and().response()

                .when()
                .get(endpoint + TestData.STORE_ID);
    }

    /**
     * GET /stores/{id} - 404
     * Returns 404 when a non-existing store
     */
    @Test
    public void GET_ID_404() {
        RestAssured.baseURI = TestData.baseURI;

        given()
                .contentType("application/json; charset=utf-8")
                .expect()
                .statusCode(404)
                .body("name", equalTo("NotFound"))
                .log().all().request().and().response()

                .when()
                .get(endpoint + TestData.NON_EXISTING_ID);
    }

    /**
     * PATCH /stores/{id} - 200
     * Updates a store based on an ID
     */
    @Test
    public void PATCH_200() {
        RestAssured.baseURI = TestData.baseURI;

        JsonObject request = Json.createObjectBuilder()
                .add("name",  TestData.TEST_NAME)
                .add("type", "Test type")
                .add("address", "Test type")
                .add("address2", "99.9")
                .add("city", "cartoons")
                .add("state", "1.2")
                .add("zip", "Test type")
                .add("lat", 0)
                .add("lng", 0)
                .add("hours", "1.2")
                .add("services", Json.createObjectBuilder())
                .build();

        // Create a store
        JsonPath response = given()
                .contentType("application/json")
                .body(request.toString())
                .expect()
                .statusCode(201)
                .log().all().request().and().response()

                .when()
                .post(endpoint).jsonPath();

        JsonObject request_updated = Json.createObjectBuilder()
                .add("name",  TestData.TEST_NAME)
                .add("type", "Test type")
                .add("address", "Test type")
                .add("address2", "99.9")
                .add("city", "cartoons")
                .add("state", "1.2")

                // model value now is different
                .add("zip", "New Zip Code")
                .add("lat", 0)
                .add("lng", 0)
                .add("hours", "1.2")
                .add("services", Json.createObjectBuilder())
                .build();

        // Update the store - model to different value
        given()
                .contentType("application/json")
                .body(request_updated.toString())
                .expect()
                .statusCode(200)
                .body("zip", equalTo("New Zip Code"))
                .log().all().request().and().response()

                .when()
                .patch(endpoint + response.get("id"));
    }

    /**
     * PATCH /stores/{id} - 404
     * non-existing ID
     */
    @Test
    public void PATCH_404() {
        RestAssured.baseURI = TestData.baseURI;

        JsonObject request = Json.createObjectBuilder()
                .add("name",  TestData.TEST_NAME)
                .add("type", "Test type")
                .add("address", "Test type")
                .add("address2", "99.9")
                .add("city", "cartoons")
                .add("state", "1.2")
                .add("zip", "Test type")
                .add("lat", 0)
                .add("lng", 0)
                .add("hours", "1.2")
                .add("services", Json.createObjectBuilder())
                .build();

        // Update the product for a non-existing ID
        given()
                .contentType("application/json")
                .body(request.toString())
                .expect()
                .statusCode(404)
                .body("name", equalTo("NotFound"))
                .body("message", equalTo("No record found for id '" + TestData.NON_EXISTING_ID + "'"))
                .log().all().request().and().response()

                .when()
                .patch(endpoint + TestData.NON_EXISTING_ID);
    }
}
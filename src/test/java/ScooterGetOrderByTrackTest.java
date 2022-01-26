import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ScooterGetOrderByTrackTest {


    private int randomNotExist = RandomUtils.nextInt(10000000, 99999999);
    private int trackId;
    static private String randomString = RandomStringUtils.randomAlphabetic(10);
    static private int randomInt = RandomUtils.nextInt(1, 11);
    static private String randomDate = "2020-06-" + RandomUtils.nextInt(11, 30);


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        CreateOrderPOJO createOrderPOJO = new CreateOrderPOJO(randomString, randomString, randomString, randomString,
                randomString, randomInt, randomDate, randomString);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrderPOJO)
                .when()
                .post("/api/v1/orders");
        String responseString = response.asString();
        JsonPath jsonPath = new JsonPath(responseString);
        trackId = jsonPath.getInt("track");
    }

    @After
    public void tearDown() {
        String cancelBody = "{\"track\":" + trackId + "}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(cancelBody)
                .when()
                .put("/api/v1/orders/cancel");
    }

    @Test
    @DisplayName("Check success body of /api/v1/orders/track/t?")
    public void checkSuccessBodyGetByTrackOrderListWhenDataIsValid() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("t", trackId)
                    .get("api/v1/orders/track");
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Check error body of /api/v1/orders/track/t? when track is missing")
    public void checkErrorBodyGetByTrackOrderListWhenTrackMissing() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("t")
                .get("api/v1/orders/track");
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Check error body of /api/v1/orders/track/t? when track is non-exist")
    public void checkErrorBodyGetByTrackOrderListWhenTrackNonExist() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("t", randomNotExist)
                .get("api/v1/orders/track");
        response.then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Заказ не найден"));
    }

}
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ScooterGetOrderListTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check status code and order list of GET /api/v1/orders")
    public void checkStatusCodeGetOrderList() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Check order list not null of GET /api/v1/orders")
    public void checkBodyGetOrderList() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
        response.then()
                .assertThat()
                .body("orders", notNullValue());
    }
}
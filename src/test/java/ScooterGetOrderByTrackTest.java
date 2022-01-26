import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class ScooterApplyOrderByCourierTest {


    private int randomNotExist = RandomUtils.nextInt(10000000, 99999999);
    private int courierId;
    private int orderId;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> loginPass = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        RegisterCourierPOJO registerCourierLogin = new RegisterCourierPOJO(loginPass.get(0), loginPass.get(1));
        String response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierLogin)
                .when()
                .post("/api/v1/courier/login")
                .asString();
        JsonPath jsonPath = new JsonPath(response);
        courierId = jsonPath.getInt("id");
        String responseOrders = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .asString();
        JsonPath jsonPathOfOrderId = new JsonPath(responseOrders);
        orderId = jsonPathOfOrderId.getInt("orders[0].id");
    }

    @After
    public void tearDown() {
        delete("/api/v1/courier/" + courierId);
    }

    @Test
    @DisplayName("Check success body of /api/v1/courier/accept/:id?courierId")
    public void checkSuccessBodyApplyByCourierWhenDataIsValid() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId", courierId)
                    .put("api/v1/orders/accept/" + orderId);
        response.then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check error body of /api/v1/courier/accept/:id?courierId when orderID missing")
    public void checkErrorBodyApplyByCourierWhenOrderIdMissing() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId", courierId)
                .put("api/v1/orders/accept/");
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Check error body of /api/v1/courier/accept/:id?courierId when courierId missing")
    public void checkErrorBodyApplyByCourierWhenCourierIdMissing() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId")
                .put("api/v1/orders/accept/" + orderId);
        response.then()
                .assertThat()
                .statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Check error body of /api/v1/courier/accept/:id?courierId when courierId non-exist")
    public void checkErrorBodyApplyByCourierWhenCourierIdNonExist() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId", randomNotExist)
                .put("api/v1/orders/accept/" + orderId);
        response.then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Check error body of /api/v1/courier/accept/:id?courierId when orderId non-exist")
    public void checkErrorBodyApplyByCourierWhenOrderIdNonExist() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId", courierId)
                .put("api/v1/orders/accept/" + randomNotExist);
        response.then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }



}
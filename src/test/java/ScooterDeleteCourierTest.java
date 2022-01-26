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

public class ScooterDeleteCourierTest {


    private int randomNotExist = RandomUtils.nextInt(10000000, 99999999);
    private String courierId;


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
        courierId = jsonPath.getString("id");
    }

    @After
    public void tearDown() {
        delete("/api/v1/courier/" + courierId);
    }

    @Test
    @DisplayName("Check message error of /api/v1/courier/:id when id is non-exist")
    public void checkMessageErrorDeleteCourierWhenIdNonExist() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + randomNotExist);
        response.then()
                .assertThat()
                .body("message", equalTo("Курьера с таким id нет"));
    }

    @Test
    @DisplayName("Check body of /api/v1/courier/:id when id is valid")
    public void checkMessageDeleteCourierWhenDataIsValid() {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + courierId);
        response.then()
                .assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check message error of /api/v1/courier/:id when id is missing")
    public void checkMessageErrorDeleteCourierWhenIdMissing() {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body("{}")
                .when()
                .delete("/api/v1/courier/");
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

}
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ScooterLoginCourierTest {


    private String login;
    private String password;
    private String random = RandomStringUtils.randomAlphabetic(10);


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> loginPass = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        login = loginPass.get(0);
        password = loginPass.get(1);
    }

    @After
    public void tearDown() {
        RegisterCourierPOJO registerCourierLogin = new RegisterCourierPOJO(login, password);
        String response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierLogin)
                .when()
                .post("/api/v1/courier/login")
                .asString();
        JsonPath jsonPath = new JsonPath(response);
        String userId = jsonPath.getString("id");
        delete("/api/v1/courier/" + userId);
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier/login when data is valid")
    public void checkStatusCodeLoginCourierWithValidData() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(login, password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check id of /api/v1/courier/login when data is valid")
    public void checkIdAfterLoginCourierWithValidData() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(login, password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier/login when login is valid, but password is invalid")
    public void checkStatusCodeLoginCourierWithIncorrectPassword() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(login, random);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier/login when login is valid, but password is invalid")
    public void checkMessageLoginCourierWithIncorrectPassword() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(login, random);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier/login when login is invalid, but password is valid")
    public void checkStatusCodeLoginCourierWithIncorrectLogin() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(random, password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier/login when login is invalid, but password is valid")
    public void checkMessageLoginCourierWithIncorrectLogin() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(random, password);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier/login when login is invalid, but password is invalid")
    public void checkStatusCodeLoginWithNonExistCourier() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(random, random);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier/login when login is invalid, but password is invalid")
    public void checkMessageLoginWithNonExistCourier() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(random, random);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .body("message", equalTo("Учетная запись не найдена"));
    }


    @Test
    @DisplayName("Check status code of /api/v1/courier/login when data is invalid, missing password field")
    public  void checkStatusCodeLoginCourierWithoutFillInPassword() {
        String registerBody = "{\"login\":\"" + login + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier/login when data is invalid, missing password field")
    public  void checkMessageLoginCourierWithoutFillInPassword() {
        String registerBody = "{\"login\":\"" + login + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier/login when data is invalid, missing login field")
    public  void checkStatusCodeLoginCourierWithoutFillInLogin() {
        String registerBody = "{\"password\":\"" + password + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier/login when data is invalid, missing login field")
    public  void checkMessageLoginCourierWithoutFillInLogin() {
        String registerBody = "{\"password\":\"" + password + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier/login");
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

}
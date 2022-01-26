import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;

import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class ScooterRegisterCourierTest {

    final static private String login = RandomStringUtils.randomAlphabetic(10);
    final static private String password = RandomStringUtils.randomAlphabetic(10);
    final static private String firstName = RandomStringUtils.randomAlphabetic(10);


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
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
    @DisplayName("Check status code of /api/v1/courier when data is valid")
    public void checkStatusCodeCreateCourierWithValidData() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(login, password,firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .statusCode(201);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier when data is valid")
    public void checkFlagOkAfterCreateCourierWithValidData() {
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(login, password,firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier when data is valid, but courier exist")
    public void checkStatusCodeCreateDuplicateCourier() {
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> loginPass = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(loginPass.get(0), loginPass.get(1));
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .statusCode(409);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier when data is valid, but courier exist")
    public void checkMessageCreateDuplicateCourier() {
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> loginPass = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        RegisterCourierPOJO registerCourierPOJO = new RegisterCourierPOJO(loginPass.get(0), loginPass.get(1));
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerCourierPOJO)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier when data is invalid, missing password field")
    public  void checkStatusCodeCreateCourierWithoutFillInPassword() {
        String registerBody = "{\"login\":\"" + login + "\","
                + "\"firstName\":\"" + firstName + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier when data is invalid, missing password field")
    public  void checkMessageCreateCourierWithoutFillInPassword() {
        String registerBody = "{\"login\":\"" + login + "\","
                + "\"firstName\":\"" + firstName + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier when data is invalid, missing login field")
    public  void checkStatusCodeCreateCourierWithoutFillInLogin() {
        String registerBody = "{\"password\":\"" + password + "\","
                + "\"firstName\":\"" + firstName + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier when data is invalid, missing login field")
    public  void checkMessageCreateCourierWithoutFillInLogin() {
        String registerBody = "{\"password\":\"" + password + "\","
                + "\"firstName\":\"" + firstName + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Check status code of /api/v1/courier when data is invalid, missing firstName field")
    public  void checkStatusCodeCreateCourierWithoutFillInFirstName() {
        String registerBody = "{\"login\":\"" + login + "\","
                + "\"password\":\"" + password + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @DisplayName("Check message of /api/v1/courier when data is invalid, missing firstName field")
    public  void checkMessageCreateCourierWithoutFillInFirstName() {
        String registerBody = "{\"login\":\"" + login + "\","
                + "\"password\":\"" + password + "\"}";
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerBody)
                .when()
                .post("/api/v1/courier");
        response.then()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

}
import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ScooterLoginCourierTest extends BaseTest {


    private String login;
    private String password;
    private String random = RandomStringUtils.randomAlphabetic(10);


    @Before
    public void setUp() {
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> loginPass = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        login = loginPass.get(0);
        password = loginPass.get(1);
    }

    @After
    public void tearDown() {
        Courier registerCourierLogin = new Courier(login, password);
        String response = given()
                .body(registerCourierLogin)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .asString();
        JsonPath jsonPath = new JsonPath(response);
        String userId = jsonPath.getString("id");
        delete(EndPoints.COURIER_REGISTER_OR_DELETE + userId);
    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier/login when data is valid")
    public void checkStatusCodeLoginCourierWithValidData() {
        Courier courier = new Courier(login, password);
        given()
                .body(courier)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier/login when login is valid, but password is invalid")
    public void checkStatusCodeBodyLoginCourierWithIncorrectPassword() {
        Courier courier = new Courier(login, random);
        given()
                .body(courier)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier/login when login is invalid, but password is valid")
    public void checkStatusCodeBodyLoginCourierWithIncorrectLogin() {
        Courier courier = new Courier(random, password);
        given()
                .body(courier)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier/login when login and password invalid")
    public void checkStatusCodeBodyLoginWithNonExistCourier() {
        Courier courier = new Courier(random, random);
        given()
                .body(courier)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
    }

//    @Test
//    @DisplayName("Check status code and body of /api/v1/courier/login when password field is missing")
//    public  void checkStatusCodeBodyLoginCourierWithoutFillInPassword() {
//        Courier courier = new Courier(login);
//        given()
//                .body(courier)
//                .when()
//                .post(EndPoints.COURIER_LOGIN)
//                .then()
//                .assertThat()
//                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
//                .and()
//                .body("message", equalTo("Недостаточно данных для входа"));
//    }

    @Test
    @DisplayName("Check status code and body of /api/v1/courier/login when login field is missing")
    public  void checkStatusCodeBodyLoginCourierWithoutFillInLogin() {
        String registerBody = "{\"password\":\"" + password + "\"}";
        given()
                .body(registerBody)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
    }

}
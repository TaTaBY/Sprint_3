import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.*;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class ScooterRegisterCourierTest extends BaseTest {
    final private String login = RandomStringUtils.randomAlphabetic(10);
    final private String password = RandomStringUtils.randomAlphabetic(10);
    final private String firstName = RandomStringUtils.randomAlphabetic(10);

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
    @DisplayName("Check status code and body of /api/v1/courier when data is valid")
    public void checkStatusCodeBodyCreateCourierWithValidData() {
        Courier courier = new Courier(login, password,firstName);
        given()
                .body(courier)
                .when()
                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

//    @Test
//    @DisplayName("Check status code of /api/v1/courier for duplicate courier")
//    public void checkStatusCodeBodyCreateDuplicateCourier() {
//        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
//        ArrayList<String> loginPass = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
//        Courier courier = new Courier(loginPass.get(0), loginPass.get(1));
//        given()
//                .body(courier)
//                .when()
//                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
//                .then()
//                .assertThat()
//                .statusCode(HttpURLConnection.HTTP_CONFLICT)
//                .and()
//                .body("message", equalTo("Этот логин уже используется"));
//    }


    @Test
    @DisplayName("Check status code and body of /api/v1/courier when password field is missing")
    public  void checkStatusCodeBodyCreateCourierWithoutFillInPassword() {
        String registerBody = "{\"login\":\"" + login + "\","
                + "\"firstName\":\"" + firstName + "\"}";
        given()
                .body(registerBody)
                .when()
                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Test
    @DisplayName("Check status code and body of /api/v1/courier when login field is missing")
    public  void checkStatusCodeBodyCreateCourierWithoutFillInLogin() {
        String registerBody = "{\"password\":\"" + password + "\","
                + "\"firstName\":\"" + firstName + "\"}";
        given()
                .body(registerBody)
                .when()
                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

//    @Test
//    @DisplayName("Check status code and body of /api/v1/courier when firstName field is missing")
//    public  void checkStatusCodeBodyCreateCourierWithoutFillInFirstName() {
//        String registerBody = "{\"login\":\"" + login + "\","
//                + "\"password\":\"" + password + "\"}";
//        given()
//                .body(registerBody)
//                .when()
//                .post(EndPoints.COURIER_REGISTER_OR_DELETE)
//                .then()
//                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
//                .and()
//                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
//    }

}
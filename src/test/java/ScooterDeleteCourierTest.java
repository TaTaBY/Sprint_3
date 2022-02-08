import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ScooterDeleteCourierTest extends BaseTest {


    private int invalidId = RandomUtils.nextInt(10000000, 99999999);
    private String courierId;


    @Before
    public void setUp() {
        ScooterRegisterCourier scooterRegisterCourier = new ScooterRegisterCourier();
        ArrayList<String> loginPass = scooterRegisterCourier.registerNewCourierAndReturnLoginPassword();
        Courier registerCourierLogin = new Courier(loginPass.get(0), loginPass.get(1));
        String response = given()
                .body(registerCourierLogin)
                .when()
                .post(EndPoints.COURIER_LOGIN)
                .asString();
        JsonPath jsonPath = new JsonPath(response);
        courierId = jsonPath.getString("id");
    }

    @After
    public void tearDown() {
        delete(EndPoints.COURIER_REGISTER_OR_DELETE + courierId);
    }


//    @Test
//    @DisplayName("Check message error of /api/v1/courier/:id when id is non-exist")
//    public void checkMessageErrorDeleteCourierWhenIdNonExist() {
//        given()
//                .when()
//                .delete(EndPoints.COURIER_REGISTER_OR_DELETE + invalidId)
//                .then()
//                .assertThat()
//                .body("message", equalTo("Курьера с таким id нет"));
//    }


    @Test
    @DisplayName("Check body of /api/v1/courier/:id when id is valid")
    public void checkMessageDeleteCourierWhenDataIsValid() {
        given()
                .when()
                .delete(EndPoints.COURIER_REGISTER_OR_DELETE + courierId)
                .then()
                .assertThat()
                .body("ok", equalTo(true));
    }

//    @Test
//    @DisplayName("Check message error of /api/v1/courier/:id when id is missing")
//    public void checkMessageErrorDeleteCourierWhenIdMissing() {
//        given()
//                .when()
//                .delete(EndPoints.COURIER_REGISTER_OR_DELETE)
//                .then()
//                .assertThat()
//                .body("message", equalTo("Недостаточно данных для удаления курьера"));
//    }


}
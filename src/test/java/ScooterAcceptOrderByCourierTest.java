import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class ScooterAcceptOrderByCourierTest extends  BaseTest {


    private int invalidId = RandomUtils.nextInt(10000000, 99999999);
    private int courierId;
    private int orderId;


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
        courierId = jsonPath.getInt("id");
        String responseOrders = given()
                .when()
                .get(EndPoints.ORDER_CREATE_OR_GET)
                .asString();
        JsonPath jsonPathOfOrderId = new JsonPath(responseOrders);
        orderId = jsonPathOfOrderId.getInt("orders[0].id");
    }

    @After
    public void tearDown() {
        delete(EndPoints.COURIER_REGISTER_OR_DELETE + courierId);
    }

    @Test
    @DisplayName("Check success body of /api/v1/orders/accept/:id?courierId")
    public void checkSuccessBodyApplyByCourierWhenDataIsValid() {
        given()
                .when()
                .queryParam("courierId", courierId)
                .put(EndPoints.ORDER_ACCEPT + orderId)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("ok", equalTo(true));
    }

//    @Test
//    @DisplayName("Check error body of /api/v1/orders/accept/:id?courierId when orderID missing")
//    public void checkErrorBodyApplyByCourierWhenOrderIdMissing() {
//        given()
//                .when()
//                .queryParam("courierId", courierId)
//                .put(EndPoints.ORDER_ACCEPT)
//                .then()
//                .assertThat()
//                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
//                .and()
//                .body("message", equalTo("Недостаточно данных для поиска"));
//    }

    @Test
    @DisplayName("Check error body of /api/v1/orders/accept/:id?courierId when courierId missing")
    public void checkErrorBodyApplyByCourierWhenCourierIdMissing() {
        given()
                .when()
                .queryParam("courierId")
                .put(EndPoints.ORDER_ACCEPT + orderId)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Check error body of /api/v1/orders/accept/:id?courierId when courierId non-exist")
    public void checkErrorBodyApplyByCourierWhenCourierIdNonExist() {
        given()
                .when()
                .queryParam("courierId", invalidId)
                .put(EndPoints.ORDER_ACCEPT + orderId)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Check error body of /api/v1/orders/accept/:id?courierId when orderId non-exist")
    public void checkErrorBodyApplyByCourierWhenOrderIdNonExist() {
        given()
                .when()
                .queryParam("courierId", courierId)
                .put(EndPoints.ORDER_ACCEPT + invalidId)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказа с таким id не существует"));
    }

}
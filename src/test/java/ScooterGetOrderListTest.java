import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ScooterGetOrderListTest extends BaseTest {

    @Test
    @DisplayName("Check status code and body of GET /api/v1/orders")
    public void checkStatusCodeGetOrderList() {
        given()
                .when()
                .get(EndPoints.ORDER_CREATE_OR_GET)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("orders", notNullValue());
    }
}
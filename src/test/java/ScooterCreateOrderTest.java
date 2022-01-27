import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class ScooterCreateOrderTest extends BaseTest {

    private String[] color;
    private int track;

    public ScooterCreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][]{
                { new String[] {"BLACK"}},
                { new String[] {"GREY"}},
                { new String[] {"BLACK", "GREY"}},
                { new String[] {}}
        };
    }

    @After
    public void tearDown() {
        String cancelBody = "{\"track\":" + track + "}";
        given()
                .body(cancelBody)
                .when()
                .put(EndPoints.ORDER_CANCEL);
    }

    @Test
    @DisplayName("Check status code and track of /api/v1/orders when data is valid (full check for field color)")
    public void checkStatusCodeAndBodyCreateOrderWithValidData() {
        Order order = new Order(color);
        Response response = given()
                .body(order)
                .when()
                .post(EndPoints.ORDER_CREATE_OR_GET);
        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_CREATED)
                .and()
                .body("track", notNullValue());
        String responseString = response.asString();
        JsonPath jsonPath = new JsonPath(responseString);
        track = jsonPath.getInt("track");
    }
}
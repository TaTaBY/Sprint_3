import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ScooterGetOrderByTrackTest extends BaseTest {


    private int invalidId = RandomUtils.nextInt(10000000, 99999999);
    private int trackId;
    static private String randomString = RandomStringUtils.randomAlphabetic(10);
    static private int randomInt = RandomUtils.nextInt(1, 11);
    static private String randomDate = LocalDate.now().plusDays(RandomUtils.nextInt(1,31)).toString();


    @Before
    public void setUp() {
        Order order = new Order(randomString, randomString, randomString, randomString,
                randomString, randomInt, randomDate, randomString);
        String response = given()
                .body(order)
                .when()
                .post(EndPoints.ORDER_CREATE_OR_GET)
                .asString();
        JsonPath jsonPath = new JsonPath(response);
        trackId = jsonPath.getInt("track");
    }

    @After
    public void tearDown() {
        String cancelBody = "{\"track\":" + trackId + "}";
        given()
                .body(cancelBody)
                .when()
                .put(EndPoints.ORDER_CANCEL);
    }

    @Test
    @DisplayName("Check success body of /api/v1/orders/track/t?")
    public void checkSuccessBodyGetByTrackOrderListWhenDataIsValid() {
        given()
                .when()
                .queryParam("t", trackId)
                .get(EndPoints.ORDER_GET_BY_TRACK)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .and()
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Check error body of /api/v1/orders/track/t? when track is missing")
    public void checkErrorBodyGetByTrackOrderListWhenTrackMissing() {
        given()
                .when()
                .queryParam("t")
                .get(EndPoints.ORDER_GET_BY_TRACK)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Check error body of /api/v1/orders/track/t? when track is non-exist")
    public void checkErrorBodyGetByTrackOrderListWhenTrackNonExist() {
        given()
                .when()
                .queryParam("t", invalidId)
                .get(EndPoints.ORDER_GET_BY_TRACK)
                .then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo("Заказ не найден"));
    }

}
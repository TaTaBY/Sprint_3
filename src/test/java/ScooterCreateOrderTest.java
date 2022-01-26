import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class ScooterCreateOrderTest {

    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private int rentTime;
    private String deliveryDate;
    private String comment;
    private String[] color;
    static private String randomString = RandomStringUtils.randomAlphabetic(10);
    static private int randomInt = RandomUtils.nextInt(1, 11);
    static private String randomDate = "2020-06-" + RandomUtils.nextInt(11, 30);
    private int track;

    public ScooterCreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone,
                                  int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }



    @Parameterized.Parameters
    public static Object[][] getOrderData() {
        return new Object[][]{
                {randomString, randomString, randomString, randomString, randomString, randomInt, randomDate,
                        randomString, new String[] {"BLACK"}},
                {randomString, randomString, randomString, randomString, randomString, randomInt, randomDate,
                        randomString, new String[] {"GREY"}},
                {randomString, randomString, randomString, randomString, randomString, randomInt, randomDate,
                        randomString, new String[] {"BLACK", "GREY"}},
                {randomString, randomString, randomString, randomString, randomString, randomInt, randomDate,
                        randomString, new String[] {}}
        };
    }




    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @After
    public void tearDown() {
        String cancelBody = "{\"track\":" + track + "}";
        given()
                .header("Content-type", "application/json")
                .and()
                .body(cancelBody)
                .when()
                .put("/api/v1/orders/cancel");
    }

    @Test
    @DisplayName("Check status code and track of /api/v1/orders when data is valid (full check for field color)")
    public void checkStatusCodeAndBodyCreateOrderWithValidData() {
        CreateOrderPOJO createOrderPOJO = new CreateOrderPOJO(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(createOrderPOJO)
                .when()
                .post("/api/v1/orders");
        response.then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("track", notNullValue());
        String responseString = response.asString();
        JsonPath jsonPath = new JsonPath(responseString);
        track = jsonPath.getInt("track");
    }
}
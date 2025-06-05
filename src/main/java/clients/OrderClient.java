package clients;

import models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String ORDERS_URL = BASE_URL + "/api/orders";

    @Step("Создание заказа")
    public Response create(Order order, String accessToken) {
        if (accessToken == null) {
            return given()
                    .header("Content-type", "application/json")
                    .body(order)
                    .when()
                    .post(ORDERS_URL);
        }
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS_URL);
    }

    @Step("Получение заказов пользователя")
    public Response getUserOrders(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS_URL);
    }
}
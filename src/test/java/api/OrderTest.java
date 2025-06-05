package api;

import clients.AuthClient;
import clients.OrderClient;
import models.Order;
import models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.RandomDataGenerator;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;

public class OrderTest {
    private AuthClient authClient;
    private OrderClient orderClient;
    private User user;
    private String accessToken;
    private final List<String> ingredients = Arrays.asList(
            "61c0c5a71d1f82001bdaaa6d",
            "61c0c5a71d1f82001bdaaa6f"
    );

    @Before
    public void setUp() {
        authClient = new AuthClient();
        orderClient = new OrderClient();
        user = User.create(
                RandomDataGenerator.getRandomEmail(),
                RandomDataGenerator.getRandomPassword(),
                RandomDataGenerator.getRandomName()
        );
        Response response = authClient.register(user);
        accessToken = response.path("accessToken");
    }

    @After
    public void tearDown() {
        authClient.delete(accessToken);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuth() {
        Order order = Order.create(ingredients);
        Response response = orderClient.create(order, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        Order order = new Order(ingredients);
        Response response = orderClient.create(order, null);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        Order order = Order.create(null);
        Response response = orderClient.create(order, accessToken);
        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Получение заказов пользователя")
    public void getUserOrders() {
        orderClient.create(new Order(ingredients), accessToken);
        Response response = orderClient.getUserOrders(accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", not(empty()));
    }
}
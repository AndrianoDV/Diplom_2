package api;

import clients.AuthClient;
import models.User;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.RandomDataGenerator;

import static org.hamcrest.Matchers.*;

public class UserTest {
    private AuthClient authClient;
    private User user;
    private String accessToken;

    private static final String NEW_NAME = "New Name";
    private static final String NEW_EMAIL = "newemail@yandex.ru";

    @Before
    public void setUp() {
        authClient = new AuthClient();
        user = User.create(
                RandomDataGenerator.getRandomEmail(),
                RandomDataGenerator.getRandomPassword(),
                RandomDataGenerator.getRandomName()
        );
        Response response = authClient.register(user);
        response.then().statusCode(200);
        accessToken = response.path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            Response response = authClient.delete(accessToken);
            response.then().statusCode(202);
        }
    }

    @Test
    @DisplayName("Обновление имени пользователя")
    public void updateUserName() {
        user.setName(NEW_NAME);
        Response response = authClient.update(user, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(NEW_NAME));
    }

    @Test
    @DisplayName("Обновление email пользователя")
    public void updateUserEmail() {
        user.setEmail(NEW_EMAIL);
        Response response = authClient.update(user, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(NEW_EMAIL));
    }

    @Test
    @DisplayName("Обновление данных без авторизации")
    public void updateUserWithoutAuth() {
        user.setName(NEW_NAME);
        Response response = authClient.update(user, null);
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", containsString("authorised"));
    }
}
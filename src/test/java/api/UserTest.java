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

    @Before
    public void setUp() {
        authClient = new AuthClient();
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
    @DisplayName("Обновление имени пользователя")
    public void updateUserName() {
        user.setName("New Name");
        Response response = authClient.update(user, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo("New Name"));
    }

    @Test
    @DisplayName("Обновление email пользователя")
    public void updateUserEmail() {
        user.setEmail("newemail@example.com");
        Response response = authClient.update(user, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo("newemail@example.com"));
    }

    @Test
    @DisplayName("Обновление данных без авторизации")
    public void updateUserWithoutAuth() {
        user.setName("New Name");
        Response response = authClient.update(user, null);
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}
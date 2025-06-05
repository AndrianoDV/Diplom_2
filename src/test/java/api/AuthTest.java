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

public class AuthTest {
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
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            authClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void successfulRegistration() {
        Response response = authClient.register(user);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Регистрация уже существующего пользователя")
    public void registerExistingUser() {
        authClient.register(user);
        accessToken = authClient.login(user).path("accessToken");

        Response response = authClient.register(user);
        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Авторизация с валидными данными")
    public void successfulLogin() {
        authClient.register(user);
        Response response = authClient.login(user);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
        accessToken = response.path("accessToken");
    }

    @Test
    @DisplayName("Авторизация с неверными данными")
    public void loginWithInvalidCredentials() {
        user.setPassword("wrong_password");
        Response response = authClient.login(user);
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
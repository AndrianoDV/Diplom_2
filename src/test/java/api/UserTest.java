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
        String newName = "NewName-" + RandomDataGenerator.randomString(5);
        user.setName(newName);
        Response response = authClient.update(user, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Обновление email пользователя")
    public void updateUserEmail() {
        String newEmail = RandomDataGenerator.getRandomEmail();
        user.setEmail(newEmail);
        Response response = authClient.update(user, accessToken);
        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Попытка обновления данных без авторизации")
    public void updateUserWithoutAuth() {
        String newName = "Unauthorized-" + RandomDataGenerator.randomString(3);
        user.setName(newName);
        Response response = authClient.update(user, "");
        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", containsString("authorised"));
    }
}
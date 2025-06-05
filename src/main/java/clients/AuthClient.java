package clients;

import models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String REGISTER_URL = BASE_URL + "/api/auth/register";
    private static final String LOGIN_URL = BASE_URL + "/api/auth/login";
    private static final String USER_URL = BASE_URL + "/api/auth/user";

    @Step("Регистрация пользователя")
    public Response register(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(REGISTER_URL);
    }

    @Step("Авторизация пользователя")
    public Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(LOGIN_URL);
    }

    @Step("Удаление пользователя")
    public Response delete(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_URL);
    }

    @Step("Обновление данных пользователя")
    public Response update(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_URL);
    }
}
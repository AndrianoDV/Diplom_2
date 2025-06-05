package models;

import io.qameta.allure.Step;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    private List<String> ingredients;

    @Step("Создание заказа с ингредиентами: {ingredients}")
    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
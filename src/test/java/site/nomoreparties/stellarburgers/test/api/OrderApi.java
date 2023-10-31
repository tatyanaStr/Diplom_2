package site.nomoreparties.stellarburgers.test.api;

import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.json.order.IngredientsRequest;

import static io.restassured.RestAssured.given;

public class OrderApi {

    private final String ingredients = "/api/ingredients";
    private final String userOrders = "/api/orders";
    private final String allOrders = "/api/orders/all";

    public Response getIngredients() {
        return given()
                .get(ingredients);
    }

    public Response createOrder(String token, IngredientsRequest ingredients) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization",token)
                .and()
                .body(ingredients)
                .post(userOrders);
    }

    public Response getAllOrders() {
        return given()
                .get(allOrders);
    }

    public Response getUserOrders(String token) {
        return given()
                .header("Authorization",token)
                .get(userOrders);
    }

}

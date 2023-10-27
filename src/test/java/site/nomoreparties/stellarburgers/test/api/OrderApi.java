package site.nomoreparties.stellarburgers.test.api;

import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.json.order.IngredientsRequest;

import static io.restassured.RestAssured.given;

public class OrderApi {

    public Response getIngredients() {
        return given()
                .get("/api/ingredients");
    }

    public Response createOrder(String token, IngredientsRequest ingredients) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization",token)
                .and()
                .body(ingredients)
                .post("/api/orders");
    }

    public Response getAllOrders() {
        return given()
                .get("/api/orders/all");
    }

    public Response getUserOrders(String token) {
        return given()
                .header("Authorization",token)
                .get("/api/orders");
    }

}

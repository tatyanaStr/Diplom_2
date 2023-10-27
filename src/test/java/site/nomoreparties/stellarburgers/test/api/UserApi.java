package site.nomoreparties.stellarburgers.test.api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApi {

    public Response registerUser(site.nomoreparties.stellarburgers.json.user.UserRequest user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/register");
    }

    public Response loginUser(site.nomoreparties.stellarburgers.json.user.UserRequest user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post("/api/auth/login");
    }

    public Response getUser(String token) {
        return given()
                .header("Authorization",token)
                .get("/api/auth/user");
    }

    public Response patchUser(site.nomoreparties.stellarburgers.json.user.UserRequest reqBody, String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .body(reqBody)
                .patch("/api/auth/user");
    }

    public Response deleteUser(String token) {
        return given()
                .header("Authorization",token)
                .delete("/api/auth/user");
    }

}

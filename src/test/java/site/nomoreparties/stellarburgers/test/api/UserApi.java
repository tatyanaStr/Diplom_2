package site.nomoreparties.stellarburgers.test.api;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserApi {

    private final String register = "/api/auth/register";
    private final String login = "/api/auth/login";
    private final String authUser = "/api/auth/user";


    public Response registerUser(site.nomoreparties.stellarburgers.json.user.UserRequest user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(register);
    }

    public Response loginUser(site.nomoreparties.stellarburgers.json.user.UserRequest user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(login);
    }

    public Response getUser(String token) {
        return given()
                .header("Authorization",token)
                .get(authUser);
    }

    public Response patchUser(site.nomoreparties.stellarburgers.json.user.UserRequest reqBody, String token) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .header("Authorization", token)
                .body(reqBody)
                .patch(authUser);
    }

    public Response deleteUser(String token) {
        return given()
                .header("Authorization",token)
                .delete(authUser);
    }

}

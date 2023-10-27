package site.nomoreparties.stellarburgers.test.api_tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.additional.LoginAndDeleteUser;
import site.nomoreparties.stellarburgers.additional.RandomValue;
import site.nomoreparties.stellarburgers.json.user.UserRequest;
import site.nomoreparties.stellarburgers.test.api.UserApi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginTest {

    UserApi userApi = new UserApi();
    LoginAndDeleteUser del = new LoginAndDeleteUser();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void loginUserTest(){
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        userApi.registerUser(user);
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        var response = userApi.loginUser(loginUser);
        response.then().assertThat().body("success", is(true))
                .and()
                .body("user.email", is(user.getEmail()))
                .and()
                .body("user.name", is(user.getName()))
                .and()
                .body("accessToken", notNullValue())
                .and()
                .body("refreshToken", notNullValue())
                .and()
                .statusCode(200);
        System.out.println(response.body().asString());
        del.deleteUser(loginUser);
    }

    @Test
    @DisplayName("Логин с неверным логином и паролем")
    public void loginWithIncorrectUserCredTest(){
        RandomValue value = new RandomValue();
        UserRequest loginUser = new UserRequest(value.email(), value.password(6), null);
        var response = userApi.loginUser(loginUser);
        response.then().assertThat().body("success", is(false))
                .and()
                .body("message", is("email or password are incorrect"))
                .and()
                .statusCode(401);
        System.out.println(response.body().asString());
    }
}

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

public class CreateUserTest {

    UserApi userApi = new UserApi();
    LoginAndDeleteUser del = new LoginAndDeleteUser();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserTest(){
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        var response = userApi.registerUser(user);
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
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        del.deleteUser(loginUser);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createUserWithSameEmailTest(){
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        userApi.registerUser(user);
        var response = userApi.registerUser(user);
        response.then().assertThat().body("success", is(false))
                .and()
                .body("message", is("User already exists"))
                .and()
                .statusCode(403);
        System.out.println(response.body().asString());
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        del.deleteUser(loginUser);
    }

    @Test
    @DisplayName("Создание пользователя, без заполнения пароля")
    public void createUserWithoutPasswordTest(){
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), null, value.name(6));
        var response = userApi.registerUser(user);
        response.then().assertThat().body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"))
                .and()
                .statusCode(403);
        System.out.println(response.body().asString());
    }
}

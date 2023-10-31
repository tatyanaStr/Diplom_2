package site.nomoreparties.stellarburgers.test.api_tests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.additional.LoginAndDeleteUser;
import site.nomoreparties.stellarburgers.json.user.UserRequest;
import site.nomoreparties.stellarburgers.test.api.CustomRequestSpecification;
import site.nomoreparties.stellarburgers.test.api.UserApi;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest {

    UserApi userApi = new UserApi();
    LoginAndDeleteUser del = new LoginAndDeleteUser();
    UserRequest loginUser = new UserRequest();

    @Before
    public void setUp() {
        RestAssured.requestSpecification = CustomRequestSpecification.requestSpec;
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserTest() {
        Faker value = new Faker();
        UserRequest user = new UserRequest(value.internet().emailAddress(), value.internet().password(6, 10), value.name().firstName());
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

        loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createUserWithSameEmailTest() {
        Faker value = new Faker();
        UserRequest user = new UserRequest(value.internet().emailAddress(), value.internet().password(6, 10), value.name().firstName());
        userApi.registerUser(user);
        var response = userApi.registerUser(user);
        response.then().assertThat().body("success", is(false))
                .and()
                .body("message", is("User already exists"))
                .and()
                .statusCode(403);

        loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
    }

    @Test
    @DisplayName("Создание пользователя, без заполнения пароля")
    public void createUserWithoutPasswordTest() {
        Faker value = new Faker();
        UserRequest user = new UserRequest(value.internet().emailAddress(), null, value.name().firstName());
        var response = userApi.registerUser(user);
        response.then().assertThat().body("success", is(false))
                .and()
                .body("message", is("Email, password and name are required fields"))
                .and()
                .statusCode(403);

    }

    @After
    public void deleteUser() {
        try {
            del.deleteUser(loginUser);
        } catch (Exception e) {

        }
    }
}

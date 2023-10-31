package site.nomoreparties.stellarburgers.test.api_tests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.additional.LoginAndDeleteUser;
import site.nomoreparties.stellarburgers.json.user.PatchUserResponse;
import site.nomoreparties.stellarburgers.json.user.UserResponse;
import site.nomoreparties.stellarburgers.json.user.UserRequest;
import site.nomoreparties.stellarburgers.test.api.CustomRequestSpecification;
import site.nomoreparties.stellarburgers.test.api.UserApi;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ChangeUserDataTest {
    private final String email;
    private final String password;
    private final String name;
    UserApi userApi = new UserApi();
    LoginAndDeleteUser del = new LoginAndDeleteUser();


    public ChangeUserDataTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Before
    public void setUp() {
        RestAssured.requestSpecification = CustomRequestSpecification.requestSpec;
    }

    @Parameterized.Parameters
    public static Object[][] dataToChange() {
        Faker value = new Faker();
        return new Object[][]{
                {value.internet().emailAddress(), value.internet().password(6, 10), value.name().firstName()},
                {value.internet().emailAddress(), value.internet().password(6, 10), null},
                {null, value.internet().password(6, 10), value.name().firstName()},
                {value.internet().emailAddress(), null, value.name().firstName()},
                {value.internet().emailAddress(), null, null},
                {null, value.internet().password(6, 10), null},
                {null, null, value.name().firstName()}
        };
    }

    @Test
    @DisplayName("Изменение данных пользователя (пользователь авторизован)")
    public void changeUserDataWithAuthTest() {
        Faker value = new Faker();
        UserRequest user = new UserRequest(value.internet().emailAddress(), value.internet().password(6, 10), value.name().firstName());
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        UserRequest reqBody = new UserRequest(email, password, name);

        userApi.registerUser(user);
        var resp = userApi.loginUser(loginUser);
        var token = resp.body().as(UserResponse.class).getAccessToken();

        Response patchResponse = userApi.patchUser(reqBody, token);
        patchResponse.then().assertThat().body("success", is(true))
                .and()
                .statusCode(200);

        var newUserData = new UserRequest(email != null ? email : loginUser.getEmail(),
                password != null ? password : loginUser.getPassword(),
                null);
        Response passwordCheckResponse = userApi.loginUser(newUserData);
        var success = passwordCheckResponse.body().as(UserResponse.class).getSuccess();
        var newToken = passwordCheckResponse.body().as(UserResponse.class).getAccessToken();

        Response newDataResponse = userApi.getUser(newToken);
        var email = newDataResponse.body().as(PatchUserResponse.class).getUser().getEmail();
        var name = newDataResponse.body().as(PatchUserResponse.class).getUser().getName();

        assertEquals("Email не был изменен", reqBody.getEmail() != null ? reqBody.getEmail() : user.getEmail(), email);
        assertEquals("Name не был изменен", reqBody.getName() != null ? reqBody.getName() : user.getName(), name);
        assertTrue("Password не был изменен", success);
        System.out.println(patchResponse.body().asString());

        del.deleteUser(newUserData);
    }

    @Test
    @DisplayName("Изменение данных пользователя (пользователь не авторизован)")
    public void changeUserDataWithoutAuthTest() {
        Faker value = new Faker();
        UserRequest user = new UserRequest(value.internet().emailAddress(), value.internet().password(6, 10), value.name().firstName());
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        UserRequest reqBody = new UserRequest(email, password, name);
        userApi.registerUser(user);
        var token = "";

        Response patchResponse = userApi.patchUser(reqBody, token);
        patchResponse.then().assertThat().body("success", is(false))
                .and()
                .body("message", is("You should be authorised"))
                .and()
                .statusCode(401);

        System.out.println(patchResponse.body().asString());
        del.deleteUser(loginUser);
    }
}

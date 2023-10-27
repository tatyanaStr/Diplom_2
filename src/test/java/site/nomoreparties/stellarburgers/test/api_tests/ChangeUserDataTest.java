package site.nomoreparties.stellarburgers.test.api_tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.additional.LoginAndDeleteUser;
import site.nomoreparties.stellarburgers.additional.RandomValue;
import site.nomoreparties.stellarburgers.json.user.PatchUserResponse;
import site.nomoreparties.stellarburgers.json.user.UserResponse;
import site.nomoreparties.stellarburgers.json.user.UserRequest;
import site.nomoreparties.stellarburgers.test.api.UserApi;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class ChangeUserDataTest {
    private final String EMAIL;
    private final String PASSWORD;
    private final String NAME;
    UserApi userApi = new UserApi();
    LoginAndDeleteUser del = new LoginAndDeleteUser();

    public ChangeUserDataTest(String email, String password, String name) {
        this.EMAIL = email;
        this.PASSWORD = password;
        this.NAME = name;
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Parameterized.Parameters
    public static Object[][] dataToChange() {
        RandomValue value = new RandomValue();
        return new Object[][]{
                {value.email(), value.password(10), value.name(10)},
                {value.email(), value.password(10), null},
                {null, value.password(10), value.name(10)},
                {value.email(), null, value.name(10)},
                {value.email(), null, null},
                {null, value.password(10), null},
                {null, null, value.name(10)}
        };
    }

    @Test
    @DisplayName("Изменение данных пользователя (пользователь авторизован)")
    public void changeUserDataWithAuthTest() {
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        UserRequest reqBody = new UserRequest(EMAIL, PASSWORD, NAME);

        userApi.registerUser(user);
        var resp = userApi.loginUser(loginUser);
        var token = resp.body().as(UserResponse.class).getAccessToken();

        Response patchResponse = userApi.patchUser(reqBody, token);
        patchResponse.then().assertThat().body("success", is(true))
                .and()
                .statusCode(200);

        var newUserData = new UserRequest(EMAIL != null ? EMAIL : loginUser.getEmail(),
                PASSWORD != null ? PASSWORD : loginUser.getPassword(),
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
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        UserRequest reqBody = new UserRequest(EMAIL, PASSWORD, NAME);
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

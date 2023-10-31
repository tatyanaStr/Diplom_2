package site.nomoreparties.stellarburgers.test.api_tests;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.additional.LoginAndDeleteUser;
import site.nomoreparties.stellarburgers.json.order.GetIngredientsResponse;
import site.nomoreparties.stellarburgers.json.order.IngredientsRequest;
import site.nomoreparties.stellarburgers.json.user.UserResponse;
import site.nomoreparties.stellarburgers.json.user.UserRequest;
import site.nomoreparties.stellarburgers.test.api.CustomRequestSpecification;
import site.nomoreparties.stellarburgers.test.api.OrderApi;
import site.nomoreparties.stellarburgers.test.api.UserApi;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest {
    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();
    LoginAndDeleteUser del = new LoginAndDeleteUser();
    UserRequest loginUser = new UserRequest();

    @Before
    public void setUp() {
        RestAssured.requestSpecification = CustomRequestSpecification.requestSpec;
    }

    @Test
    @DisplayName("Список всех заказов")
    public void getAllOrdersTest() {
        Response response = orderApi.getAllOrders();
        response.then().assertThat().body("success", is(true))
                .and()
                .body("orders", notNullValue())
                .and()
                .body("total", notNullValue())
                .and()
                .body("totalToday", notNullValue())
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Список всех заказов пользователя")
    public void getUserOrdersTest() {
        Faker value = new Faker();
        UserRequest user = new UserRequest(value.internet().emailAddress(), value.internet().password(6, 10), value.name().firstName());
        loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        userApi.registerUser(user);
        var resp = userApi.loginUser(loginUser);
        var token = resp.body().as(UserResponse.class).getAccessToken();
        var ingredientsResponse = orderApi.getIngredients();
        var ingredientsData = ingredientsResponse.body().as(GetIngredientsResponse.class).getData();

        String[] ingredientsIds = new String[ingredientsData.length];
        for (int i = 0; i < ingredientsData.length; i++) {
            var id = ingredientsData[i].get_id();
            ingredientsIds[i] = id;
        }

        var ingredients = new IngredientsRequest(ingredientsIds);
        orderApi.createOrder(token, ingredients);
        Response response = orderApi.getUserOrders(token);

        response.then().assertThat().body("success", is(true))
                .and()
                .body("orders", notNullValue())
                .and()
                .body("total", is(1))
                .and()
                .body("totalToday", is(1))
                .and()
                .statusCode(200);
    }

    @After
    public void deleteUser() {
        try {
            del.deleteUser(loginUser);
        } catch (Exception e) {

        }
    }
}

package site.nomoreparties.stellarburgers.test.api_tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.additional.LoginAndDeleteUser;
import site.nomoreparties.stellarburgers.additional.RandomValue;
import site.nomoreparties.stellarburgers.json.order.GetIngredientsResponse;
import site.nomoreparties.stellarburgers.json.order.IngredientsRequest;
import site.nomoreparties.stellarburgers.json.user.UserResponse;
import site.nomoreparties.stellarburgers.json.user.UserRequest;
import site.nomoreparties.stellarburgers.test.api.OrderApi;
import site.nomoreparties.stellarburgers.test.api.UserApi;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {

    UserApi userApi = new UserApi();
    OrderApi orderApi = new OrderApi();
    LoginAndDeleteUser del = new LoginAndDeleteUser();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами (авторизован)")
    public void createOrderWithIngredientsWithAuthTest(){
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        userApi.registerUser(user);
        var resp = userApi.loginUser(loginUser);
        var token = resp.body().as(UserResponse.class).getAccessToken();
        var ingredientsResponse = orderApi.getIngredients();
        var ingredientsData = ingredientsResponse.body().as(GetIngredientsResponse.class).getData();

        String[] ingredientsIds = new String[ingredientsData.length];
        for (int i  = 0; i < ingredientsData.length; i++){
            var id = ingredientsData[i].get_id();
            ingredientsIds[i] = id;
        }

        var ingredients = new IngredientsRequest(ingredientsIds);

        Response response = orderApi.createOrder(token, ingredients);
        response.then().assertThat().body("success", is(true))
                .and()
                .body("name", notNullValue())
                .and()
                .body("order", notNullValue())
                .and()
                .statusCode(200);
        del.deleteUser(loginUser);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами (без авторизации)")
    public void createOrderWithIngredientsWithoutAuthTest(){
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        userApi.registerUser(user);
        var token = "";

        var ingredientsResponse = orderApi.getIngredients();

        var ingredientsData = ingredientsResponse.body().as(GetIngredientsResponse.class).getData();

        String[] ingredientsIds = new String[ingredientsData.length];
        for (int i  = 0; i < ingredientsData.length; i++){
            var id = ingredientsData[i].get_id();
            ingredientsIds[i] = id;
        }

        var ingredients = new IngredientsRequest(ingredientsIds);

        Response response = orderApi.createOrder(token, ingredients);
        response.then().assertThat().body("success", is(true))
                .and()
                .body("name", notNullValue())
                .and()
                .body("order", notNullValue())
                .and()
                .statusCode(200);

        del.deleteUser(loginUser);

    }

    @Test
    @DisplayName("Создание заказа без ингредиентов (авторизован)")
    public void createOrderWithoutIngredientsWithAuthTest(){
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        userApi.registerUser(user);
        var resp = userApi.loginUser(loginUser);
        var token = resp.body().as(UserResponse.class).getAccessToken();
        var ingredients = new IngredientsRequest(new String[]{});

        Response response = orderApi.createOrder(token, ingredients);
        response.then().assertThat().body("success", is(false))
                .and()
                .body("message", is("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
        del.deleteUser(loginUser);

    }

    @Test
    @DisplayName("Создание заказа c неверным хэшем (авторизован)")
    public void createOrderIncorrectIngredientsWithAuthTest(){
        RandomValue value = new RandomValue();
        UserRequest user = new UserRequest(value.email(), value.password(6), value.name(6));
        UserRequest loginUser = new UserRequest(user.getEmail(), user.getPassword(), null);
        userApi.registerUser(user);
        var resp = userApi.loginUser(loginUser);
        var token = resp.body().as(UserResponse.class).getAccessToken();
        var ingredients = new IngredientsRequest(new String[]{value.name(5)});

        Response response = orderApi.createOrder(token, ingredients);
        response.then().assertThat()
                .statusCode(500);
        del.deleteUser(loginUser);

    }
}

package site.nomoreparties.stellarburgers.additional;

import site.nomoreparties.stellarburgers.json.user.UserRequest;
import site.nomoreparties.stellarburgers.json.user.UserResponse;
import site.nomoreparties.stellarburgers.test.api.UserApi;

public class LoginAndDeleteUser {

    UserApi userApi = new UserApi();
    public void deleteUser(UserRequest user) {
        var response = userApi.loginUser(user);
        var token = response.body().as(UserResponse.class).getAccessToken();
        userApi.deleteUser(token);
    }
}

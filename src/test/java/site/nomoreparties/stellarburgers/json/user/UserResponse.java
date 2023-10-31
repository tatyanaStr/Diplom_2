package site.nomoreparties.stellarburgers.json.user;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {

    @Getter
    @Setter
    private Boolean success;

    @Getter
    @Setter
    private String accessToken;

    @Getter
    @Setter
    private String refreshToken;

    @Getter
    @Setter
    private UserInfoResponse user;
}

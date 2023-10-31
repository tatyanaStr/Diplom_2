package site.nomoreparties.stellarburgers.json.user;

import lombok.Getter;
import lombok.Setter;

public class PatchUserResponse {

    @Getter
    @Setter
    private Boolean success;

    @Getter
    @Setter
    private PatchUserInfoResponse user;

}

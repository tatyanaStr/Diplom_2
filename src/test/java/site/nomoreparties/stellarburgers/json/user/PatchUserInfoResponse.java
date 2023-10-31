package site.nomoreparties.stellarburgers.json.user;

import lombok.Getter;
import lombok.Setter;

public class PatchUserInfoResponse {

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String name;
}


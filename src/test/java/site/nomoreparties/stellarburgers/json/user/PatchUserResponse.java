package site.nomoreparties.stellarburgers.json.user;

public class PatchUserResponse {
    private Boolean success;
    private PatchUserInfoResponse user;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public PatchUserInfoResponse getUser() {
        return user;
    }

    public void setUser(PatchUserInfoResponse user) {
        this.user = user;
    }
}

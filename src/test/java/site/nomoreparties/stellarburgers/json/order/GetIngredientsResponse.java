package site.nomoreparties.stellarburgers.json.order;

public class GetIngredientsResponse {
    private Boolean success;
    private GetDataIngredientsResponse[] data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public GetDataIngredientsResponse[] getData() {
        return data;
    }

    public void setData(GetDataIngredientsResponse[] data) {
        this.data = data;
    }
}

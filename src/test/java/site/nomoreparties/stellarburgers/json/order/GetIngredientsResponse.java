package site.nomoreparties.stellarburgers.json.order;

import lombok.Getter;
import lombok.Setter;

public class GetIngredientsResponse {

    @Getter
    @Setter
    private Boolean success;

    @Getter
    @Setter
    private GetDataIngredientsResponse[] data;
}

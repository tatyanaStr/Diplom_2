package site.nomoreparties.stellarburgers.json.order;

import lombok.Getter;
import lombok.Setter;

public class IngredientsRequest {

    @Getter
    @Setter
    private String[] ingredients;

    public IngredientsRequest(String[] ingredients) {
        this.ingredients = ingredients;
    }
    public IngredientsRequest(){}
}


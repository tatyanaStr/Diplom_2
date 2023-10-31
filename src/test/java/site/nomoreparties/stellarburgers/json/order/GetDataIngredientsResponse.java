package site.nomoreparties.stellarburgers.json.order;

import lombok.Getter;
import lombok.Setter;

public class GetDataIngredientsResponse {

    @Getter
    @Setter
    private String _id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private int proteins;

    @Getter
    @Setter
    private int fat;

    @Getter
    @Setter
    private int carbohydrates;

    @Getter
    @Setter
    private int calories;

    @Getter
    @Setter
    private int price;

    @Getter
    @Setter
    private String image;

    @Getter
    @Setter
    private String image_mobile;

    @Getter
    @Setter
    private String image_large;

    @Getter
    @Setter
    private String __v;
}

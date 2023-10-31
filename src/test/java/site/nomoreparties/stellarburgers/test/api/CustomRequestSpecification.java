package site.nomoreparties.stellarburgers.test.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class CustomRequestSpecification {
    public static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("https://stellarburgers.nomoreparties.site/").build();
}

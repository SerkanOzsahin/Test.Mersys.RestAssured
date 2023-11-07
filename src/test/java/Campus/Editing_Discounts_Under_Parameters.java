package Campus;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class Editing_Discounts_Under_Parameters extends Hooks {

    String descriptionId;
    String descriptionName;

    @Test
    public void createDiscounts() {
        descriptionName = randomFaker.name().firstName();
        Map<String, String> newDiscounts = new HashMap<>();
        newDiscounts.put("description", descriptionName);
        newDiscounts.put("code", randomFaker.code().asin());
        newDiscounts.put("active", "true");
        newDiscounts.put("priority", "5");

        descriptionId =
                given()
                        .spec(reqSpec)
                        .body(newDiscounts)
                        .when()
                        .post("school-service/api/discounts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createDiscounts")
    public void createDiscountsNegative() {
        Map<String, String> newDiscounts = new HashMap<>();
        newDiscounts.put("description", descriptionName);
        newDiscounts.put("code", randomFaker.code().asin());
        newDiscounts.put("active", "true");
        newDiscounts.put("priority", "5");

        given()
                .spec(reqSpec)
                .body(newDiscounts)
                .when()
                .post("school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));
    }

    @Test(dependsOnMethods = "createDiscountsNegative")
    public void updateDiscounts() {
        Map<String, String> newDiscounts = new HashMap<>();
        newDiscounts.put("id", descriptionId);
        newDiscounts.put("description", "grup3test");
        newDiscounts.put("code", randomFaker.code().asin());
        newDiscounts.put("active", "true");
        newDiscounts.put("priority", "7");

        given()
                .spec(reqSpec)
                .body(newDiscounts)
                .when()
                .put("school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "createDiscountsNegative")
    public void updateDiscountsNegative() {
        Map<String, String> newDiscounts = new HashMap<>();
        newDiscounts.put("description", "grup3test");
        newDiscounts.put("code", randomFaker.code().asin());
        newDiscounts.put("active", "true");
        newDiscounts.put("priority", "7");

        given()
                .spec(reqSpec)
                .body(newDiscounts)
                .when()
                .post("school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));
    }

    @Test(dependsOnMethods = "updateDiscountsNegative")
    public void deleteDiscounts() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/discounts/" + descriptionId)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteDiscounts")
    public void deleteDiscountsNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/discounts/" + descriptionId)
                .then()
                .statusCode(400)
                .body("message", containsString("not found"));
    }
}

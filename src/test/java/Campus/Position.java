package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Position extends Hooks {

    String positionID = "";

    private String positionName;

    @Test
    public void createPosition() {
        String rndPositionName = randomFaker.name().firstName();
        positionName = rndPositionName;

        Map<String, String> newPosition = new HashMap<>();
        newPosition.put("name", rndPositionName);
        positionID =
                given()
                        .spec(reqSpec)
                        .body(newPosition)
                        .when()
                        .post("school-service/api/position-category")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createPosition")
    public void createPositionNegative() {
        Map<String, String> newPosition = new HashMap<>();
        newPosition.put("name", positionName);
        given()
                .spec(reqSpec)
                .body(newPosition)
                .when()
                .post("school-service/api/position-category")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(400);
    }

    @Test(dependsOnMethods = "createPositionNegative")
    public void updatePosition() {
        Map<String, String> updatePosition = new HashMap<>();
        updatePosition.put("id", positionID);
        updatePosition.put("name", "team3edit800");

        given()
                .spec(reqSpec)
                .body(updatePosition)
                .when()
                .put("school-service/api/position-category")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("team3edit800"));
    }

    @Test(dependsOnMethods = "updatePosition")
    public void deletePosition() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/position-category/" + positionID)
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deletePosition")
    public void deletePositionNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/position-category/" + positionID)
                .then()
                .statusCode(400)
                .log().body();
    }
}

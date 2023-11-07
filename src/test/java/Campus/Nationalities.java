package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Nationalities extends Hooks {

    private String nationalityName = "";
    String nationalityID = "";

    @Test
    public void createNationality() {
        String rndNationalityName = randomFaker.country().name() + "12340";
        nationalityName = rndNationalityName;

        Map<String, String> newNationality = new HashMap<>();
        newNationality.put("name", rndNationalityName);

        nationalityID =
                given()
                        .spec(reqSpec)
                        .body(newNationality)
                        .when()
                        .post("school-service/api/nationality")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createNationality")
    public void createNationalityNegative() {
        Map<String, String> newNationality = new HashMap<>();
        newNationality.put("name", nationalityName);

        given()
                .spec(reqSpec)
                .body(newNationality)
                .when()
                .post("school-service/api/nationality")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .body("message", containsString("already exists"));
    }

    @Test(dependsOnMethods = "createNationalityNegative")
    public void updateNationality() {
        Map<String, String> updateNationality = new HashMap<>();
        updateNationality.put("id", nationalityID);
        updateNationality.put("name", "editNationalityName1230");

        given()
                .spec(reqSpec)
                .body(updateNationality)
                .when()
                .put("school-service/api/nationality")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("editNationalityName1230"));
    }

    @Test(dependsOnMethods = "updateNationality")
    public void deleteNationality() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/nationality/" + nationalityID)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteNationality")
    public void deleteNationalityNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/nationality/" + nationalityID)
                .then()
                .statusCode(400)
                .body("message", containsString("not  found"));
    }
}

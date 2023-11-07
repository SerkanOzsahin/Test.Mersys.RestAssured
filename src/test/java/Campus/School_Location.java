package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class School_Location extends Hooks {

    private String schoolName = "";
    private String schoolShortName = "";
    String schoolID = "";

    @Test
    public void createSchoolLocation() {
        String rndSchoolName = randomFaker.name().firstName();
        String rndSchoolShortName = randomFaker.name().title();

        schoolName = rndSchoolName;
        schoolShortName = rndSchoolShortName;

        Map<String, Object> newSchoolLocation = new HashMap<>();
        newSchoolLocation.put("name", rndSchoolName);
        newSchoolLocation.put("shortName", rndSchoolShortName);
        newSchoolLocation.put("active", true);
        newSchoolLocation.put("capacity", "5");
        newSchoolLocation.put("type", "CLASS");
        newSchoolLocation.put("school", "646cbb07acf2ee0d37c6d984");

        schoolID =
                given()
                        .spec(reqSpec)
                        .body(newSchoolLocation)
                        .when()
                        .post("school-service/api/location")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createSchoolLocation")
    public void createSchoolLocationNegative() {
        Map<String, Object> newSchoolLocation = new HashMap<>();
        newSchoolLocation.put("name", schoolName);
        newSchoolLocation.put("shortName", schoolShortName);
        newSchoolLocation.put("active", true);
        newSchoolLocation.put("capacity", "5");
        newSchoolLocation.put("type", "CLASS");
        newSchoolLocation.put("school", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(newSchoolLocation)
                .when()
                .post("school-service/api/location")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .body("message", containsString("already exists"));
    }

    @Test(dependsOnMethods = "createSchoolLocationNegative")
    public void updateSchoolLocation() {
        Map<String, Object> updateSchoolLocation = new HashMap<>();
        updateSchoolLocation.put("id", schoolID);
        updateSchoolLocation.put("name", "editSchoolLocation123");
        updateSchoolLocation.put("shortName", schoolShortName);
        updateSchoolLocation.put("active", true);
        updateSchoolLocation.put("capacity", "5");
        updateSchoolLocation.put("type", "CLASS");
        updateSchoolLocation.put("school", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(updateSchoolLocation)
                .when()
                .put("school-service/api/location")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("editSchoolLocation123"));
    }

    @Test(dependsOnMethods = "updateSchoolLocation")
    public void deleteSchoolLocation() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/location/" + schoolID)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteSchoolLocation")
    public void deleteSchoolLocationNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/location/" + schoolID)
                .then()
                .statusCode(400)
                .body("message", containsString("not found"));
    }
}

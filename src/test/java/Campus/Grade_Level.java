package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Grade_Level extends Hooks{
    String gradeLevelID = "";

    private String gradeLevelName;

    private String gradeLevelShortName;



    @Test
    public void createGradeLevel() {
        String rndGradeLevelName = randomFaker.name().firstName();
        String rndGradeLevelShortName = randomFaker.name().title();

        gradeLevelName = rndGradeLevelName;
        gradeLevelShortName = rndGradeLevelShortName;

        Map<String, Object> newGradeLevel = new HashMap<>();
        newGradeLevel.put("name", rndGradeLevelName);
        newGradeLevel.put("shortName",rndGradeLevelShortName);
        newGradeLevel.put("nextGradeLevel",null);
        newGradeLevel.put("order", "143");
        newGradeLevel.put("maxApplicationCount",55);
        newGradeLevel.put("active", true);
        newGradeLevel.put("schoolIds", new String[]{"646cbb07acf2ee0d37c6d984"});
        newGradeLevel.put("showToAllSchools", false);
        newGradeLevel.put("enableForSelectedSchools", true);


        gradeLevelID =

                given()
                        .spec(reqSpec)
                        .body(newGradeLevel)

                        .when()
                        .post("school-service/api/grade-levels")

                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createGradeLevel")
    public void createGradeLevelNegative() {
        Map<String, Object> newGradeLevel = new HashMap<>();
        newGradeLevel.put("name", gradeLevelName);
        newGradeLevel.put("shortName",gradeLevelShortName);
        newGradeLevel.put("nextGradeLevel",null);
        newGradeLevel.put("order", "143");
        newGradeLevel.put("maxApplicationCount",55);
        newGradeLevel.put("active", true);
        newGradeLevel.put("schoolIds", new String[]{"646cbb07acf2ee0d37c6d984"});
        newGradeLevel.put("showToAllSchools", false);
        newGradeLevel.put("enableForSelectedSchools", true);

        given()
                .spec(reqSpec)
                .body(newGradeLevel)

                .when()
                .post("school-service/api/grade-levels")

                .then()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .log().body();
    }


    @Test(dependsOnMethods = "createGradeLevelNegative")
    public void updateGradeLevel() {
        Map<String, Object> updateGradeLevel = new HashMap<>();
        updateGradeLevel.put("id", gradeLevelID);
        updateGradeLevel.put("name", "blair waldorf");
        updateGradeLevel.put("shortName","bw");
        updateGradeLevel.put("nextGradeLevel",null);
        updateGradeLevel.put("order", "143");
        updateGradeLevel.put("maxApplicationCount",55);
        updateGradeLevel.put("active", true);
        updateGradeLevel.put("schoolIds", new String[]{"646cbb07acf2ee0d37c6d984"});
        updateGradeLevel.put("showToAllSchools", false);
        updateGradeLevel.put("enableForSelectedSchools", true);


        given()
                .spec(reqSpec)
                .body(updateGradeLevel)

                .when()
                .put("school-service/api/grade-levels")

                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name",equalTo("blair waldorf"));
    }

    @Test(dependsOnMethods = "updateGradeLevel")
    public void deleteGradeLevel() {
        given()
                .spec(reqSpec)

                .when()
                .delete("school-service/api/grade-levels/" + gradeLevelID)

                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteGradeLevel")
    public void deleteGradeLevelNegative() {
        given()
                .spec(reqSpec)

                .when()
                .delete("school-service/api/grade-levels/" + gradeLevelID)

                .then()
                .statusCode(400)
                .log().body();
    }
}




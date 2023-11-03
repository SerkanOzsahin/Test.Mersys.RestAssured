package Campus;

import com.github.javafaker.Faker;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class Configuring_Training_Subject_Categories extends Hooks {
    Faker randomData = new Faker();
    String subjectId;

    String subjectName;

    @Test
    public void createSubject() {
        subjectName = randomData.name().fullName();
        Map<String, String> newSubject = new HashMap<>();
        newSubject.put("name", subjectName);
        newSubject.put("code", randomData.code().asin());
        subjectId =
                given()
                        .spec(reqSpec)
                        .body(newSubject)
                        .when()
                        .post("school-service/api/subject-categories")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createSubject")
    public void createSubjectNegative() {
        Map<String, String> newSubject = new HashMap<>();
        newSubject.put("name", subjectName);
        newSubject.put("code", randomData.code().asin());


        given()
                .spec(reqSpec)
                .body(newSubject)
                .when()
                .post("school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));

    }

    @Test(dependsOnMethods = "createSubjectNegative")
    public void updateSubject() {
        Map<String, String> newSubject = new HashMap<>();
        newSubject.put("code", randomData.code().asin());
        newSubject.put("name", "grup3test");
        newSubject.put("id", subjectId);
        given()
                .spec(reqSpec)
                .body(newSubject)
                .when()
                .put("school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "updateSubject")
    public void updateSubjectNegative() {
        Map<String, String> newSubject = new HashMap<>();
        newSubject.put("code", randomData.code().asin());
        newSubject.put("name", "grup3test");
        given()
                .spec(reqSpec)
                .body(newSubject)
                .when()
                .post("school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));
    }

    @Test(dependsOnMethods = "updateSubjectNegative")
    public void deleteSubject() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/subject-categories/" + subjectId)
                .then()
                .statusCode(200);

    }

    @Test(dependsOnMethods = "deleteSubject")
    public void deleteSubjectNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/subject-categories" + subjectId)
                .then()
                .statusCode(404)
                .body("message", containsString("not found"));

    }
}

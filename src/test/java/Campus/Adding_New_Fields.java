package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

public class Adding_New_Fields extends Hooks {

    private String fieldsID = "";
    private String fieldsName = "";

    @Test
    public void createFields() {
        String rndFieldsName = randomFaker.name().firstName();
        fieldsName = rndFieldsName;

        Map<String, Object> newFields = new HashMap<>();
        newFields.put("name", rndFieldsName);
        newFields.put("active", true);
        newFields.put("code", 111);
        newFields.put("type", "STRING");
        newFields.put("systemField", false);
        newFields.put("systemFieldName", null);
        newFields.put("schoolId", "646cbb07acf2ee0d37c6d984");

        fieldsID =
                given()
                        .spec(reqSpec)
                        .body(newFields)
                        .when()
                        .post("/school-service/api/entity-field")
                        .then()
                        .contentType(ContentType.JSON)
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createFields")
    public void createFieldsNegative() {
        Map<String, Object> newFields = new HashMap<>();
        newFields.put("name", fieldsName);
        newFields.put("active", true);
        newFields.put("code", 111);
        newFields.put("type", "STRING");
        newFields.put("systemField", false);
        newFields.put("systemFieldName", null);
        newFields.put("schoolId", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(newFields)
                .when()
                .post("/school-service/api/entity-field")
                .then()
                .contentType(ContentType.JSON)
                .log().body()
                .statusCode(400)
                .body("message", containsString("already"));
    }

    @Test(dependsOnMethods = "createFieldsNegative")
    public void updateFields() {
        Map<String, Object> updateNewFields = new HashMap<>();
        updateNewFields.put("id", fieldsID);
        updateNewFields.put("name", "team3Edit110");
        updateNewFields.put("active", true);
        updateNewFields.put("code", 111);
        updateNewFields.put("type", "STRING");
        updateNewFields.put("systemField", false);
        updateNewFields.put("systemFieldName", null);
        updateNewFields.put("schoolId", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(updateNewFields)
                .when()
                .put("/school-service/api/entity-field")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("team3Edit110"));
    }

    @Test(dependsOnMethods = "updateFields")
    public void deleteFields() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/entity-field/" + fieldsID)
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteFields")
    public void deleteFieldsNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/entity-field/" + fieldsID)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("EntityField not found"));
    }
}

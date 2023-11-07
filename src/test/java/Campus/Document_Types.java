package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Document_Types extends Hooks {

    private String documentName = "";
    private String documentDescription = "";
    String documentID = "";

    @Test
    public void createDocumentType() {
        String rndDocumentName = randomFaker.name().firstName();
        String rndDocumentDescription = randomFaker.name().title();

        documentName = rndDocumentName;
        documentDescription = rndDocumentDescription;

        Map<String, Object> newDocument = new HashMap<>();
        newDocument.put("name", rndDocumentName);
        newDocument.put("description", rndDocumentDescription);
        newDocument.put("attachmentStages", new ArrayList<>(Arrays.asList("STUDENT_REGISTRATION")));
        newDocument.put("active", true);
        newDocument.put("required", true);
        newDocument.put("useCamera", false);
        newDocument.put("translateName", new ArrayList<>());
        newDocument.put("schoolId", "646cbb07acf2ee0d37c6d984");

        documentID =
                given()
                        .spec(reqSpec)
                        .body(newDocument)
                        .when()
                        .post("school-service/api/attachments/create")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createDocumentType")
    public void createDocumentTypeNegative() {
        Map<String, Object> newDocument = new HashMap<>();
        newDocument.put("id", documentID);
        newDocument.put("name", documentName);
        newDocument.put("description", documentDescription);
        newDocument.put("attachmentStages", new ArrayList<>(Arrays.asList("STUDENT_REGISTRATION")));
        newDocument.put("active", true);
        newDocument.put("required", true);
        newDocument.put("useCamera", false);
        newDocument.put("translateName", new ArrayList<>());
        newDocument.put("schoolId", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(newDocument)
                .when()
                .post("school-service/api/attachments/create")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .body("message", containsString("already created"));
    }

    @Test(dependsOnMethods = "createDocumentTypeNegative")
    public void updateDocumentType() {
        Map<String, Object> updateDocument = new HashMap<>();
        updateDocument.put("id", documentID);
        updateDocument.put("name", "editDocumentName");
        updateDocument.put("description", documentDescription);
        updateDocument.put("attachmentStages", new ArrayList<>(Arrays.asList("STUDENT_REGISTRATION")));
        updateDocument.put("active", true);
        updateDocument.put("required", true);
        updateDocument.put("useCamera", false);
        updateDocument.put("translateName", new ArrayList<>());
        updateDocument.put("schoolId", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(updateDocument)
                .when()
                .put("school-service/api/attachments")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("editDocumentName"));
    }

    @Test(dependsOnMethods = "updateDocumentType")
    public void deleteDocumentType() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/attachments/" + documentID)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteDocumentType")
    public void deleteDocumentTypeNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/attachments/" + documentID)
                .then()
                .statusCode(400)
                .body("message", containsString("not found"));
    }
}

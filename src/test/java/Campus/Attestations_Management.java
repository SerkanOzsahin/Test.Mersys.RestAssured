package Campus;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;


public class Attestations_Management extends Hooks {
    Faker randomData = new Faker();
    String attestationsId;
    String attestationsName;

    @Test
    public void createAttestation() {
        attestationsName = randomData.name().fullName();
        Map<String, String> newAttestation = new HashMap<>();
        newAttestation.put("name", attestationsName);

        attestationsId =
                given()
                        .spec(reqSpec)
                        .body(newAttestation)
                        .when()
                        .post("school-service/api/attestation")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createAttestation")
    public void createAttestationNegative() {
        Map<String, String> newAttestation = new HashMap<>();
        newAttestation.put("name", attestationsName);

        given()
                .spec(reqSpec)
                .body(newAttestation)
                .when()
                .post("school-service/api/attestation")
                .then()
                .log().body()
                .statusCode(400)
                .body("message", containsString("already exists"));

    }

    @Test(dependsOnMethods = "createAttestationNegative")
    public void updateAttestation() {
        Map<String, String> newAttestation = new HashMap<>();
        newAttestation.put("id", attestationsId);
        newAttestation.put("name", "grup3 test");

        given()
                .spec(reqSpec)
                .body(newAttestation)
                .when()
                .put("school-service/api/attestation")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
                .statusCode(200);
    }

    @Test(dependsOnMethods = "updateAttestation")
    public void updateAttestationNegative() {
        Map<String, String> newAttestation = new HashMap<>();
        newAttestation.put("name", "grup3 test");

        given()
                .spec(reqSpec)
                .body(newAttestation)
                .when()
                .post("school-service/api/attestation")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
                .statusCode(400)
                .body("message", containsString("already exists"));
    }

    @Test(dependsOnMethods = "updateAttestationNegative")
    public void deleteAttestation() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/attestation/" + attestationsId)
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "deleteAttestation")
    public void deleteAttestationNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/attestation/" + attestationsId)
                .then()
                .statusCode(400)
                .body("message", containsString("not found"));
    }
}

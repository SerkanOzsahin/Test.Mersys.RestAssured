package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

    public class Positions extends Hooks {

        String positionID = "";

        private String positionName;

        private String positionShortName;

        @Test
        public void createPosition() {
            String rndPositionName = randomFaker.name().firstName();
            String rndPositionShortName = randomFaker.name().title();

            positionName = rndPositionName;
            positionShortName = rndPositionShortName;

            Map<String, Object> newPosition = new HashMap<>();
            newPosition.put("name", rndPositionName);
            newPosition.put("shortName",rndPositionShortName);
            newPosition.put("tenantId","646cb816433c0f46e7d44cb0");
            newPosition.put("active", true);

            positionID =

                    given()
                            .spec(reqSpec)
                            .body(newPosition)

                            .when()
                            .post("school-service/api/employee-position")

                            .then()
                            .contentType(ContentType.JSON)
                            .statusCode(201)
                            .extract().path("id");
        }

        @Test(dependsOnMethods = "createPosition")
        public void createPositionNegative() {
            Map<String, Object> newPosition = new HashMap<>();
            newPosition.put("name", positionName);
            newPosition.put("shortName",positionShortName);
            newPosition.put("tenantId","646cb816433c0f46e7d44cb0");
            newPosition.put("active", true);

            given()
                    .spec(reqSpec)
                    .body(newPosition)

                    .when()
                    .post("school-service/api/employee-position")

                    .then()
                    .contentType(ContentType.JSON)
                    .statusCode(400)
                    .log().body();
        }


        @Test(dependsOnMethods = "createPositionNegative")
        public void updatePosition() {
            Map<String, Object> updatePosition = new HashMap<>();
            updatePosition.put("id", positionID);
            updatePosition.put("name", "welder");
            updatePosition.put("shortName", positionShortName);
            updatePosition.put("tenantId","646cb816433c0f46e7d44cb0");
            updatePosition.put("active", true);

            given()
                    .spec(reqSpec)
                    .body(updatePosition)

                    .when()
                    .put("school-service/api/employee-position")

                    .then()
                    .contentType(ContentType.JSON)
                    .statusCode(200)
                    .body("name",equalTo("welder"));
        }

        @Test(dependsOnMethods = "updatePosition")
        public void deletePosition() {
            given()
                    .spec(reqSpec)

                    .when()
                    .delete("school-service/api/employee-position/" + positionID)

                    .then()
                    .statusCode(204);
        }

        @Test(dependsOnMethods = "deletePosition")
        public void deletePositionNegative() {
            given()
                    .spec(reqSpec)

                    .when()
                    .delete("school-service/api/employee-position/" + positionID)

                    .then()
                    .statusCode(204)
                    .log().body();
        }
    }


package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
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

            Map<String, String> newPosition = new HashMap<>();
            newPosition.put("name", rndPositionName);
            newPosition.put("shortName",rndPositionShortName);

            positionID =

                    given()
                            .spec(reqSpec)
                            .body(newPosition)

                            .when()
                            .post("school-service/api/employee-position")

                            .then()
                            .contentType(ContentType.JSON)
                            .statusCode(400)
                            .extract().path("id");
        }

        @Test(dependsOnMethods = "createPosition")
        public void createPositionNegative() {
            Map<String, String> newPosition = new HashMap<>();
            newPosition.put("name", positionName);
            newPosition.put("shortName",positionShortName);
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
            Map<String, String> updatePosition = new HashMap<>();
            updatePosition.put("id", positionID);
            updatePosition.put("name", "welder");
            updatePosition.put("shortName", positionShortName);

            given()
                    .spec(reqSpec)
                    .body(updatePosition)

                    .when()
                    .put("school-service/api/employee-position")

                    .then()
                    .contentType(ContentType.JSON)
                    .statusCode(400)
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
                    .statusCode(400)
                    .body("message", containsString("not found"));;
        }
    }


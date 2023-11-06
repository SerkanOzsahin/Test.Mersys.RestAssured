package Campus;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

public class Adding_New_Fields extends Hooks {
    Faker randomFaker = new Faker();
    String fieldsID = "";
    private String fieldsName;

    @Test
    public void createFields() {

        String rndFieldsName = randomFaker.name().firstName();

        fieldsName = rndFieldsName;

        Map<String, Object> newFields = new HashMap<>();
        newFields.put("name", rndFieldsName);
        newFields.put("active", true);
        newFields.put("code", 411);
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
                        .extract().path("id")
        ;

    }

}

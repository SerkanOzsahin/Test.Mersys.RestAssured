package Campus;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class Editing_Bank_Accounts extends Hooks {

    String bankID = "";
    private String bankName = "";
    private String bankIban = "";

    @Test
    public void createBankAccount() {
        String rndBankName = randomFaker.name().firstName();
        String rndBankIban = randomFaker.number().digits(3);

        bankName = rndBankName;
        bankIban = rndBankIban;

        Map<String, Object> newBank = new HashMap<>();
        newBank.put("name", rndBankName);
        newBank.put("iban", rndBankIban);
        newBank.put("integrationCode", "sff");
        newBank.put("currency", "TRY");
        newBank.put("active", true);
        newBank.put("schoolId", "646cbb07acf2ee0d37c6d984");

        bankID =
                given()
                        .spec(reqSpec)
                        .body(newBank)
                        .when()
                        .post("school-service/api/bank-accounts")
                        .then()
                        .contentType(ContentType.JSON)
                        .statusCode(201)
                        .extract().path("id");
    }

    @Test(dependsOnMethods = "createBankAccount")
    public void createBankAccountNegative() {
        Map<String, Object> newBank = new HashMap<>();
        newBank.put("id", bankID);
        newBank.put("name", bankName);
        newBank.put("iban", bankIban);
        newBank.put("integrationCode", "sff");
        newBank.put("currency", "TRY");
        newBank.put("active", true);
        newBank.put("schoolId", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(newBank)
                .when()
                .post("school-service/api/bank-accounts")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(400);
    }

    @Test(dependsOnMethods = "createBankAccountNegative")
    public void updateBankAccount() {
        Map<String, Object> updateNewBank = new HashMap<>();
        updateNewBank.put("id", bankID);
        updateNewBank.put("name", "team3Edit0000000000115");
        updateNewBank.put("iban", bankIban);
        updateNewBank.put("integrationCode", "sff");
        updateNewBank.put("currency", "TRY");
        updateNewBank.put("active", true);
        updateNewBank.put("schoolId", "646cbb07acf2ee0d37c6d984");

        given()
                .spec(reqSpec)
                .body(updateNewBank)
                .when()
                .put("school-service/api/bank-accounts")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("name", equalTo("team3Edit0000000000115"));
    }

    @Test(dependsOnMethods = "updateBankAccount")
    public void deleteBankAccount() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/bank-accounts/" + bankID)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteBankAccount")
    public void deleteBankAccountNegative() {
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/bank-accounts/" + bankID)
                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Please, bank account must be exist"));
    }
}

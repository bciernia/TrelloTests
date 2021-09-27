package Organizations;

import Base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

public class OrganizationsTest extends BaseTest
{
    private String methodNameUsingClassInstance;

    private static String organizationId;

    public void deleteOrganization()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void createOrganization()
    {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "First organization")
                .queryParam("name", "organization_name")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(anyOf(is(HttpStatus.SC_OK), is(HttpStatus.SC_CREATED)))
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        organizationId = json.getString("id");

        assertThat(json.getString("displayName")).isEqualTo("First organization");
        assertThat(json.getString("name")).isEqualTo("organization_name");

        deleteOrganization();
    }

    @Test
    public void organizationNameWithUppercases()
    {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "First organization")
                .queryParam("name", "ORGANIZATION_NAME")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();
        JsonPath json = response.jsonPath();

        organizationId = json.getString("id");

        methodNameUsingClassInstance =
                new OrganizationsTest() {}.getClass().getEnclosingMethod().getName();

        try
        {
            assertThat(json.getString("name")).isEqualTo("ORGANIZATION_NAME");
        }
        catch(AssertionError e)
        {
            System.out.println("Error from method " + methodNameUsingClassInstance + " :" + e);
        }

        deleteOrganization();
    }


    @Test
    public void organizationNameWithLessThanThreeChars()
    {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "First organization")
                .queryParam("name", "N")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        organizationId = json.getString("id");

        methodNameUsingClassInstance =
                new OrganizationsTest() {}.getClass().getEnclosingMethod().getName();

        try
        {
            assertThat(json.getString("name")).isEqualTo("N");
        }
        catch(AssertionError e)
        {
            System.out.println("Error from method " + methodNameUsingClassInstance + " :" + e);
        }

        deleteOrganization();
    }

    @Test
    public void organizationNameAsEmptySpace()
    {
        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "First organization")
                .queryParam("name", " ")
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        organizationId = json.getString("id");

        methodNameUsingClassInstance =
                new OrganizationsTest() {}.getClass().getEnclosingMethod().getName();

        try
        {
            assertThat(json.getString("name")).isEqualTo(" ");
        }
        catch(AssertionError e)
        {
            System.out.println("Error from method " + methodNameUsingClassInstance + " :" + e);
        }

        deleteOrganization();
    }

    @Test
    public void createOrganizationWithoutDisplayName()
    {
        given()
                .spec(reqSpec)
                .when()
                .post(BASE_URL + ORGANIZATIONS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}

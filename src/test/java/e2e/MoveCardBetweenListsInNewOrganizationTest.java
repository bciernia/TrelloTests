package e2e;

import Base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoveCardBetweenListsInNewOrganizationTest extends BaseTest
{

    public static String organizationId;
    public static String boardId;
    public static String firstListId;
    public static String secondListId;
    public static String cardId;

    @Test
    @Order(1)
    public void createOrganization() {
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
    }

    @Test
    @Order(2)
    public void createBoard() {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "First board")
                .queryParam("idOrganization", organizationId)
                .when()
                .post(BASE_URL + BOARDS)
                .then()
                .statusCode(anyOf(is(HttpStatus.SC_OK), is(HttpStatus.SC_CREATED)))
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        boardId = json.getString("id");

        assertThat(json.getString("name")).isEqualTo("First board");
    }

    @Test
    @Order(3)
    public void createFirstListInBoard()
    {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "First list in board")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(anyOf(is(HttpStatus.SC_OK), is(HttpStatus.SC_CREATED)))
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        firstListId = json.getString("id");

        assertThat(json.getString("name")).isEqualTo("First list in board");
    }

    @Test
    @Order(4)
    public void createSecondListInBoard()
    {
        Response response = given()
                .spec(reqSpec)
                .queryParam("name", "Second list in board")
                .queryParam("idBoard", boardId)
                .when()
                .post(BASE_URL + LISTS)
                .then()
                .statusCode(anyOf(is(HttpStatus.SC_OK), is(HttpStatus.SC_CREATED)))
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        secondListId = json.getString("id");

        assertThat(json.getString("name")).isEqualTo("Second list in board");
    }

    @Test
    @Order(5)
    public void addCardToFirstList()
    {
        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", firstListId)
                .queryParam("name", "First card")
                .when()
                .post(BASE_URL + CARDS)
                .then()
                .statusCode(anyOf(is(HttpStatus.SC_OK), is(HttpStatus.SC_CREATED)))
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        cardId = json.getString("id");

        assertThat(json.getString("name")).isEqualTo("First card");
    }

    @Test
    @Order(6)
    public void updateCardDescription()
    {
        Response response = given()
                .spec(reqSpec)
                .queryParam("desc", "New description")
                .when()
                .put(BASE_URL + CARDS + "/" + cardId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        assertThat(json.getString("desc")).isEqualTo("New description");
    }

    @Test
    @Order(7)
    public void moveCardFromFirstToSecondList()
    {
        Response response = given()
                .spec(reqSpec)
                .queryParam("idList", secondListId)
                .when()
                .put(BASE_URL + CARDS + "/" + cardId)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        assertThat(json.getString("idList")).isEqualTo(secondListId);
    }


    @Test
    @Order(8)
    public void deleteBoard()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + BOARDS + "/" + boardId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Order(9)
    public void deleteOrganization()
    {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + ORGANIZATIONS + "/" + organizationId)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }
}

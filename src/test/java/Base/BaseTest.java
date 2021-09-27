package Base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest
{

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    protected static final String KEY = "4cd7a086f19064a485334a78e641b766";
    protected static final String TOKEN = "09b66e4afdcbf26abc4d0483cea26b9d9584192d22bb29c6c662893c44cb0b06";

    protected static final String BASE_URL = "https://api.trello.com/1";
    protected static final String BOARDS = "/boards";
    protected static final String LISTS = "/lists";
    protected static final String CARDS = "/cards";
    protected static final String ORGANIZATIONS = "/organizations";

    @BeforeAll
    public static void beforeAll()
    {
        reqBuilder = new RequestSpecBuilder();

        reqBuilder.setContentType(ContentType.JSON);
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);

        reqSpec = reqBuilder.build();
    }
}

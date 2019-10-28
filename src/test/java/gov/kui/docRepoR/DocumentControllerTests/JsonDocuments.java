package gov.kui.docRepoR.DocumentControllerTests;

public enum JsonDocuments {
    JSON_GOOD( "{ " +
                    "\"id\": 1," +
                    "\"number\": \"123456789\"," +
                    "\"docDate\": \"2019-01-01\"," +
                    "\"title\": \"Заголовок\"," +
                    "\"content\": \"qwee\"," +
                    "\"doctype\": {" +
                    "\"id\": 6, " +
                    "\"title\": \"Анонимка\"" +
                    "}," +
                    "\"senders\": [ " +
                    "{" +
                    "\"id\": 2, " +
                    "\"title\": \"ООО \\\"Рога и Копыта\\\"\"" +
                    " }" +
                    "]" +
                    "}"
    ),
    JSON_GOOD_2_SENDERS( "{ " +
            "\"id\": 10," +
            "\"number\": \"987654321\"," +
            "\"docDate\": \"2019-10-27\"," +
            "\"title\": \"Заголовок123\"," +
            "\"content\": \"qwerty\"," +
            "\"doctype\": {" +
            "\"id\": 6, " +
            "\"title\": \"Анонимка\"" +
            "}," +
            "\"senders\": [ " +
            "{" +
            "\"id\": 2, " +
            "\"title\": \"ООО \\\"Рога и Копыта\\\"\"" +
            " }," +
            "{" +
            "\"id\": 1, " +
            "\"title\": \"Автор неизвестен\"" +
            " }"+
            "]" +
            "}"
    ),
    JSON_NULL("{}"),
    JSON_NO_REQURED_FIELDS( "{ " +
            "\"id\": 1," +
            "\"number\": \"\"," +
            "\"docDate\": \"\"," +
            "\"title\": \"\"," +
            "\"content\": \"qwee\"," +
            "\"doctype\": {}," +
            "\"senders\": [ " +
            "{" +
            "\"id\": 2, " +
            "\"title\": \"ООО \\\"Рога и Копыта\\\"\"" +
            " }" +
            "]" +
            "}"
    ),
    ;

    private final String text;

    JsonDocuments(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

package gov.kui.docRepoR.SenderControllerTests;

public enum JsonSenders {
    JSON_GOOD( "{ " +
            "\"id\": 200," +
            "\"title\": \"Отправитель JSON_GOOD\""+
            "}"
    ),
    JSON_NULL("{}"),
    JSON_NO_REQURED_FIELDS( "{ " +
            "\"id\": 200," +
            "\"title\": \" \""+
            "}"
    );

    private final String text;

    JsonSenders(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

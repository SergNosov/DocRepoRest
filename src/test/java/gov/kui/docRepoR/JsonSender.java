package gov.kui.docRepoR;

public enum JsonSender {
    JSON_GOOD( "{ " +
            "\"id\": 200," +
            "\"title\": \"Отправитель JSON_GOOD\""+
            "}"
    ),
    JSON_ZERO_ID( "{ " +
            "\"id\": 0," +
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

    JsonSender(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

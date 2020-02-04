package gov.kui.docRepoR;

public enum JsonDoctype {
    JSON_GOOD( "{ " +
            "\"id\": 400," +
            "\"title\": \"Тип документа JSON_GOOD\""+
            "}"
    ),
    JSON_ZERO_ID( "{ " +
            "\"id\": 0," +
            "\"title\": \"Тип документа JSON_GOOD\""+
            "}"
    ),
    JSON_NULL("{}"),
    JSON_NO_REQURED_FIELDS( "{ " +
            "\"id\": 200," +
            "\"title\": \" \""+
            "}"
    );

    private final String text;

    JsonDoctype(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

package gov.kui.docRepoR;

public enum JsonUser {
    JSON_GOOD( "{ " +
            "\"username\": john," +
            "\"password\": \"fun123\""+
            "}"
    ),
    JSON_NULL("{}"),
    JSON_NO_REQURED_FIELDS( "{ " +
            "\"username\": john," +
            "\"password\": \" \""+
            "}"
    );

    private final String text;

    JsonUser(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

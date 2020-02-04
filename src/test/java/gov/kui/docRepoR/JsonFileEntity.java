package gov.kui.docRepoR;

public enum JsonFileEntity {
    JSON_GOOD( "{ " +
            "\"id\": 200," +
            "\"filename\": \"newUploadFile.pdf\","+
            "\"contentType\": \"pdf\","+
            "\"fileSize\": \"100\","+
            "\"documentId\": \"21\""+
            "}"
    ),
    JSON_NULL("{}"),
    JSON_NO_REQURED_FIELDS(  "{ " +
            "\"id\": 200," +
            "\"filename\": \" \""+
            "\"contentType\": \" \""+
            "\"fileSize\": \"0\""+
            "\"documentId\": \"0\""+
            "}"
    );

    private final String text;

    JsonFileEntity(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

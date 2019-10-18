package gov.kui.docRepoR;

public enum DocRepoURL {
    DOCUMENTS_URL("http://localhost:8080/api/documents"),
    DOCTYPES_URL("http://localhost:8080/api/doctypes"),
    SENDERS_URL("http://localhost:8080/api/senders"),
    ;

    private final String text;

    DocRepoURL(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

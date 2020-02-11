package gov.kui.docRepoR;

public enum DocRepoURL {
    DOCUMENTS_LOCALHOST("http://localhost:8080/api/documents"),
    DOCUMENTS("/api/documents"),
    DOCTYPES_LOCALHOST("http://localhost:8080/api/doctypes"),
    DOCTYPES("/api/doctypes"),
    SENDERS_LOCALHOST("http://localhost:8080/api/senders"),
    SENDERS("/api/senders"),
    FILE_LOCALHOST("http://localhost:8080/api/files"),
    TOKEN_LOCALHOST("http://localhost:8080/token/generate-token");

    private final String text;

    DocRepoURL(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

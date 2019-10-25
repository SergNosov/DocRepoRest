package gov.kui.docRepoR;

public enum DocRepoURL {
    DOCUMENTS("http://localhost:8080/api/documents"),
    DOCTYPES("http://localhost:8080/api/doctypes"),
    SENDERS("http://localhost:8080/api/senders"),
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

package Enums;

public  enum COMPLETION_STATUS {
    IN_PROGRESS("IN PROGRESS"),
    FINALIZED("FINALIZED"),
    OVERDUE("OVERDUE");

    public final String label;

    private COMPLETION_STATUS(String label) {
        this.label = label;
    }
}

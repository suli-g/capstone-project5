package Interfaces;

public interface Queries {
    String DATE_FORMAT_REGEX = "\\d{4}-\\d{2}-\\d{2}",
        PHONE_NUMBER_REGEX = "\\d{10}";
    enum JOIN_TYPE {
        CROSS_JOIN("CROSS"),
        LEFT_JOIN("LEFT"),
        RIGHT_JOIN("RIGHT");

        private String value;

        private JOIN_TYPE(String value) {
            this.value = value;
        }

        public StringBuilder getValue() {
            return new StringBuilder(value).append(" JOIN ");
        }
    }
    /**
     * Represents the completion status of Projects to list.
     */
    enum PROJECT_VIEW {
        /**
         * All projects.
         */
        ALL("projects"),
        /**
         * Projects that are not finalized.
         */
        INCOMPLETE("incomplete_projects"),
        /**
         * Projects that are finalized.
         */
        FINALIZED("finalized_projects"),
        /**
         * Projects that are outstanding (due date passed).
         */
        OUTSTANDING("outstanding_projects");

        public final String LABEL;

        private PROJECT_VIEW(String tableName) {
            this.LABEL = tableName;
        }
    };
}
package config;

public interface Queries {

    String QUERY_WHERE_ID_IS = "WHERE project_id = ?";
    /**
     * The string to be used when querying all projects.
     */
    String QUERY_ALL_PROJECTS = "SELECT * FROM projects",
            /**
             * To be used when querying all complete projects.
             */
            QUERY_COMPLETE_PROJECTS = "SELECT * FROM complete_projects",
            /**
             * To be used when querying all incomplete projects.
             */
            QUERY_INCOMPLETE_PROJECTS = "SELECT * FROM incomplete_projects",
            /**
             * To be used when querying all finalized projects.
             */
            QUERY_FINALIZED_PROJECTS = "SELECT * FROM finalized_projects",
            /**
             * To be used when querying all outstanding projects.
             */
            QUERY_OUTSTANDING_PROJECTS = "SELECT * FROM outstanding_projects",
            /**
             * To be used when querying all people.
             */
            QUERY_ALL_PEOPLE = "SELECT * FROM people",
            /**
             * To be used when querying all project roles.
             */
            QUERY_ALL_ROLES = "SELECT * FROM roles",
            PARTICPANT_QUERY_DETAILS[] = new String[]{"name_initial", "last_name", "email_address", "phone_number"},
            QUERY_PROJECT_PARTICIPANTS = "SELECT %1$s FROM participant",
            QUERY_CROSS_JOIN_ON = "CROSS JOIN contacts %1$s ON %1$s.phone_number = participant.%1$s",
            /**
             * To be used if a query returns no results.
             */
            QUERY_GETS_NO_RESULTS = """
                    ====
                        No results found.
                    ====
                    """;

    /**
     * Represents the completion status of Projects to list.
     */
    enum PROJECT_STATUS {
        /**
         * All projects.
         */
        ANY(""),
        /**
         * Projects that are not finalized.
         */
        COMPLETE("-c"),
        /**
         * Projects that are not finalized.
         */
        INCOMPLETE("-i"),
        /**
         * Projects that are finalized.
         */
        FINALIZED("-f"),
        /**
         * Projects that are outstanding (due date passed).
         */
        OUTSTANDING("-o");

        public final String label;

        public static PROJECT_STATUS fromString(String str) {
            for (PROJECT_STATUS value : PROJECT_STATUS.values()) {
                if (value.label.equals(str)) {
                    return value;
                }
            }
            return null;
        }

        private PROJECT_STATUS(String label) {
            this.label = label;
        }
    };
}
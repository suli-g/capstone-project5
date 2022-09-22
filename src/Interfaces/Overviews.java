package Interfaces;

public interface Overviews extends Config {
    String SUCCESS_MESSAGE = "%s HAS BEEN CREATED SUCCESSFULLY";
    /**
     * The format used when showing each project in a list.
     * 
     * @param project_type    String
     * @param erf_number      int
     * @param project_address String
     * @param date_due        Date
     * @param date_finalized  Date
     */
    String PROJECT_OVERVIEW_FORMAT = """
            ------------------------
                Project Number:     %d
                Project Name:       %s
                Project Address:    %s
                Project Type:       %s
                ERF Number:         %d
            """;
    /**
     * The format used when showing the progress of a project.
     * 
     * @param date_due       String
     * @param date_finalized String
     */
    String PROGRESS_OVERVIEW_FORMAT = """
            ------------------------
                Due Date:           %s
                Date Finalized:     %s
                """;
    /**
     * The format used when showing a person's details.
     * 
     * @param first_name String
     * @param last_name String
     * @param email_address String
     * @param phone_number String
     * @param physical_address String
     */
    String PERSON_OVERVIEW_FORMAT = """
            ------------------------
                First Name:          %-8s
                Last Name:           %-8s
                Email Address:       %-8s
                Phone Number:        %-8s
                Physical Address:    %-8s
            """;
    /**
     * @param amount_due int
     * @param amount_paid int
     */
    String ACCOUNT_OVERVIEW_FORMAT = """
            ------------------------
                Amount Due:         R%.2f
                Amount Paid:        R%.2f
            """;
    /**
     * The format used when showing an address.
     * 
     * @param street_address String
     * @param suburb String
     * @param city String
     * @param province String
     * @param post_code String
     */
    String ADDRESS_OVERVIEW_FORMAT = """

                                    %-8s
                                    %-8s
                                    %-8s
                                    %-8s
                                    %-8s
            """;
    String PARTICIPANT_FORMAT = """
            ------------------------
                %s
                %s
                """;

}

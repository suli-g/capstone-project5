package config;

public interface Overviews {
    /**
     * To be used with {@link String#format} to display a project overview in a list from a ResultSet.
     * 
     * <dl>
     *  <dt>arguments</dt>
     *  <dd><ol>
     *  <li>project_id</li>
     *  <li>project_name</li>
     *  <li>project_type</li>
     *  <li>erf_number</li>
     *  <li>project_address</li>
     *  <li>amount_due</li>
     *  <li>amount_paid</li>
     *  <li>date_due</li>
     *  <li>date_finalized</li>
     * </ol></dd>
     * </dl>
     */
    String PROJECT_LIST_FORMAT = """
            [PROJECT DETAILS]
                Project Number:     %d
                Project Name:       %s
                Project Address:    %s
                Project Type:       %s
                ERF Number:         %d

            [PROJECT ACCOUNT]
                Amount Due:         R%.2f
                Amount Paid:        R%.2f

            [PROJECT PROGRESS]
                Due Date:           %s
                Date Finalized:     %s

            """,
            PERSON_OVERVIEW_FORMAT = """
                    ====%s
                        Name:                       %-8s
                        Phone Number:               %-8s
                    """
            ;
}

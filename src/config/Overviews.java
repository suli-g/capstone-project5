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
            Project ID:             %-8d
            Project Name:           %-8s
            Project Type:           %-8s
            Erf Number:             %-8d
            Project Address:        %-8s
            Total Cost:             %-8s
            Total Paid:             %-7.2f
            Date Due:               %-8s
            Date Finalized:         %-8s
            """,
            PERSON_OVERVIEW_FORMAT = """
                    ====%s
                        Name:                       %-8s
                        Phone Number:               %-8s
                    """
            ;
}

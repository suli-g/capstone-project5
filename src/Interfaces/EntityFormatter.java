package Interfaces;

/**
 * Contains Strings to be used to print Entity overviews to the terminal.
 * <strong>Should only be modified if the database structure changes.</strong>
 */
public interface EntityFormatter {
    /**
     * The format used when showing each project in a list,
     * to be used with a Formatter like in {@see String#format(String, Object...)}.
     * 
     * <table>
     * <caption>Parameter indices in formatter</caption>
     *  <thead>
     *      <tr><th>index</th><th>specifier</th><th>parameter</th></tr>
     * </thead>
     * <tbody>
     *      <tr><td>0</td><td>%d</td><td>project number</td></tr>
     *      <tr><td>1</td><td>%s</td><td>project name</td></tr>
     *      <tr><td>2</td><td>%s</td><td>project address</td></tr>
     *      <tr><td>3</td><td>%s</td><td>project type</td></tr>
     *      <tr><td>4</td><td>%d</td><td>project ERF number</td></tr>
     * </tbody>
     * </table>
     */
    String PROJECT_OVERVIEW_FORMAT = """
                Project Number:     %d
                Project Name:       %s
                Project Address:    %s
                Project Type:       %s
                ERF Number:         %d
            """;
    /**
     * The format used when showing the progress of a project,
     * to be used with a Formatter like in {@see String#format(String, Object...)}.
     * 
     * <table>
     *  <caption>Parameter indices in formatter</caption>
     *  <thead>
     *      <tr><th>index</th><th>specifier</th><th>parameter</th></tr>
     * </thead>
     * <tbody>
     *      <tr><td>0</td><td>%s</td><td>date due/td></tr>
     *      <tr><td>1</td><td>%s</td><td>date finalized</td></tr>
     * </tbody>
     * </table>
     */
    String PROGRESS_OVERVIEW_FORMAT = """
                Due Date:           %s
                Date Finalized:     %s
                """;
    /**
     * The format used when showing a person's details,
     * to be used with a Formatter like in {@see String#format(String, Object...)}.
     * 
     * <table>
     *  <caption>Parameter indices in formatter</caption>
     *  <thead>
     *      <tr><th>index</th><th>specifier</th><th>parameter</th></tr>
     * </thead>
     * <tbody>
     *      <tr><td>0</td><td>%d</td><td>first name</td></tr>
     *      <tr><td>1</td><td>%s</td><td>last name</td></tr>
     *      <tr><td>2</td><td>%s</td><td>email address</td></tr>
     *      <tr><td>3</td><td>%s</td><td>phone number</td></tr>
     *      <tr><td>4</td><td>%d</td><td>physical address</td></tr>
     * </tbody>
     * </table>
     */
    String PERSON_OVERVIEW_FORMAT = """
                First Name:          %-8s
                Last Name:           %-8s
                Email Address:       %-8s
                Phone Number:        %-8s
                Physical Address:    %-8s
            """;
    /**
     * The format used when showing project account details,
     * to be used with a Formatter like in {@see String#format(String, Object...)}.
     * <table>
     *  <caption>Parameter indices in formatter</caption>
     *  <thead>
     *      <tr><th>index</th><th>specifier</th><th>parameter</th></tr>
     * </thead>
     * <tbody>
     *      <tr><td>0</td><td>%f</td><td>amount due/td></tr>
     *      <tr><td>1</td><td>%f</td><td>amount paid</td></tr>
     * </tbody>
     * </table>
     */
    String ACCOUNT_OVERVIEW_FORMAT = """
                Amount Due:         R%.2f
                Amount Paid:        R%.2f
            """;
    /**
     * The format used when showing an address,
     * to be used with a Formatter like in {@see String#format(String, Object...)}.
     * 
     * <table>
     * <caption>Parameter indices in formatter</caption>
     *  <thead>
     *      <tr><th>index</th><th>specifier</th><th>parameter</th></tr>
     * </thead>
     * <tbody>
     *      <tr><td>0</td><td>%s</td><td>street address</td></tr>
     *      <tr><td>1</td><td>%s</td><td>suburb</td></tr>
     *      <tr><td>2</td><td>%s</td><td>city</td></tr>
     *      <tr><td>3</td><td>%s</td><td>province</td></tr>
     *      <tr><td>4</td><td>%s</td><td>post code</td></tr>
     * </tbody>
     * </table>
     */
    String ADDRESS_OVERVIEW_FORMAT = """
                                    %-8s
                                    %-8s
                                    %-8s
                                    %-8s
                                    %-8s
            """;
}

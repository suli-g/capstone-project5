package Components.DataEntry;

/**
 * Represents an EntityDataEntry
 */
public class EntityEntryBuilder implements DelimitedValueString {
    protected static StringBuilder entryString;

    /**
     * Initializes {@link #entryString}.
     */
    public EntityEntryBuilder() {
        entryString = new StringBuilder();
    }

    /**
     * Appends {@code data} to {@link #entryString} along with {@link #VALUE_DELIMITER}.
     * 
     * @param object the to be appended as a {@link String}.
     * @return this EntityDataBuilder.
     */
    public EntityEntryBuilder append(Object object) {
        entryString.append(object).append(VALUE_DELIMITER);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return entryString.toString();
    }
}
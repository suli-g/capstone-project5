package Components.DataEntry;

/**
 * Represents an EntityEntryBuilder
 */
public class EntityEntryBuilder implements DelimitedValueString {
    private StringBuilder entryBuilder;
    private static EntityEntryBuilder builderInstance;
    protected EntityEntryBuilder(){
        entryBuilder = new StringBuilder();
    }

    public static EntityEntryBuilder getInstance() {
        if (builderInstance == null) {
            builderInstance = new EntityEntryBuilder();
        }
        return builderInstance;
    }
    /**
     * Appends {@code data} to {@link #entryBuilder} along with {@link #VALUE_DELIMITER}.
     * 
     * @param object the to be appended as a {@link String}.
     * @return this EntityDataBuilder.
     */
    public EntityEntryBuilder append(Object object) {
        if (entryBuilder == null) {
            entryBuilder = new StringBuilder();
        }
        entryBuilder.append(object).append(VALUE_DELIMITER);
        return builderInstance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String value = entryBuilder.toString();
        entryBuilder = null;
        return value;
    }
}
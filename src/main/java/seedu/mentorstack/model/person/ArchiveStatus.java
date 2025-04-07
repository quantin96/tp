package seedu.mentorstack.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's archive status in Mentorstack.
 * Guarantees: immutable;
 */
public class ArchiveStatus {


    public static final String MESSAGE_CONSTRAINTS =
            "Archive Status should either be true or false";
    public final String isArchived;

    /**
     * Constructs a {@code ArchiveStatus}.
     *
     * @param isArchived A boolean value.
     */
    public ArchiveStatus(String isArchived) {
        requireNonNull(isArchived);
        this.isArchived = isArchived;
    }

    public static boolean isValidArchiveStatus(String test) {
        return test.equals("true") || test.equals("false");
    }

    @Override
    public String toString() {
        return isArchived;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ArchiveStatus)) {
            return false;
        }

        ArchiveStatus otherArchiveStatus = (ArchiveStatus) other;
        return isArchived.equals(otherArchiveStatus.isArchived);
    }

    @Override
    public int hashCode() {
        return isArchived.hashCode();
    }

    public String getStatus() {
        return this.isArchived;
    }

    public Boolean testStatus() {
        return this.isArchived.equals("true");
    }
}

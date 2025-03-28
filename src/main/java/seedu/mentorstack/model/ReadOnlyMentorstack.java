package seedu.mentorstack.model;

import javafx.collections.ObservableList;
import seedu.mentorstack.model.person.Person;

/**
 * Unmodifiable view of Mentorstack
 */
public interface ReadOnlyMentorstack {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getPersonList();

    /**
     * Returns an unmodifiable view of the sorted persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<Person> getSortedPersonList();

}

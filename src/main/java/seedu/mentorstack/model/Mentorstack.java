package seedu.mentorstack.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.mentorstack.commons.util.ToStringBuilder;
import seedu.mentorstack.model.person.Person;
import seedu.mentorstack.model.person.UniquePersonList;

/**
 * Wraps all data at the Mentorstack level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class Mentorstack implements ReadOnlyMentorstack {

    private final UniquePersonList persons;
    private final UniquePersonList sortedPersons;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        sortedPersons = new UniquePersonList();
    }

    public Mentorstack() {}

    /**
     * Creates a Mentorstack using the Persons in the {@code toBeCopied}
     */
    public Mentorstack(ReadOnlyMentorstack toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
        this.sortedPersons.setPersons(persons);
        this.sortedPersons.sortPersons();
    }

    /**
     * Resets the existing data of this {@code Mentorstack} with {@code newData}.
     */
    public void resetData(ReadOnlyMentorstack newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in Mentorstack.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to Mentorstack.
     * The person must not already exist in Mentorstack.
     */
    public void addPerson(Person p) {
        persons.add(p);
        sortedPersons.add(p);
        sortedPersons.sortPersons();
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in Mentorstack.
     * The person identity of {@code editedPerson} must not be the same as another existing person in Mentorstack.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
        sortedPersons.setPerson(target, editedPerson);
        sortedPersons.sortPersons();
    }

    /**
     * Removes {@code key} from this {@code Mentorstack}.
     * {@code key} must exist in Mentorstack.
     */
    public void removePerson(Person key) {
        persons.remove(key);
        sortedPersons.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .toString();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Person> getSortedPersonList() {
        return sortedPersons.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Mentorstack)) {
            return false;
        }

        Mentorstack otherMentorstack = (Mentorstack) other;
        return persons.equals(otherMentorstack.persons);
    }

    @Override
    public int hashCode() {
        return persons.hashCode();
    }

    /**
     * Removes {@code personToArchive} from this {@code Mentorstack}.
     * {@code personToArchive} must exist in Mentorstack.
     */
    public void archive(Person personToArchive, Person archived) {
        persons.archivePerson(personToArchive, archived);
        sortedPersons.archivePerson(personToArchive, archived);
        sortedPersons.sortPersons();
    }

    public void unarchive(Person personToUnarchive, Person unarchived) {
        persons.unarchivePerson(personToUnarchive, unarchived);
        sortedPersons.unarchivePerson(personToUnarchive, unarchived);
        sortedPersons.sortPersons();
    }

    public void mark(Person target, Person marked) {
        persons.mark(target, marked);
    }

    public void unmark(Person target, Person unmarked) {
        persons.unmark(target, unmarked);
    }
}

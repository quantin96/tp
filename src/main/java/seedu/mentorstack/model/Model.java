package seedu.mentorstack.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.mentorstack.commons.core.GuiSettings;
import seedu.mentorstack.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = person -> !person.getIsArchived().isArchived.equals("true");
    Predicate<Person> PREDICATE_SHOW_ALL_ARCHIVED_PERSONS = person -> person.getIsArchived().isArchived.equals("true");

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' address book file path.
     */
    Path getMentorstackFilePath();

    /**
     * Sets the user prefs' address book file path.
     */
    void setMentorstackFilePath(Path mentorstackFilePath);

    /**
     * Replaces address book data with the data in {@code mentorstack}.
     */
    void setMentorstack(ReadOnlyMentorstack mentorstack);

    /** Returns the Mentorstack */
    ReadOnlyMentorstack getMentorstack();

    /**
     * Returns true if a person with the same identity as {@code person} exists in Mentorstack.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in Mentorstack.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in Mentorstack.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in Mentorstack.
     * The person identity of {@code editedPerson} must not be the same as another existing person in Mentorstack.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /** Returns an unmodifiable view of the sorted person list */
    ObservableList<Person> getSortedPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Archives the given person.
     * {@code person} must not already exist in Mentorstack.
     */
    void archivePerson(Person person, Person archived);

    /**
     * Unarchives the given person.
     * {@code person} must not already exist in Mentorstack.
     */
    void unarchivePerson(Person personToUnarchive, Person unarchived);

    /**
     * Marks the given person.
     */
    void markPerson(Person target, Person marked);

    /**
     * Unmarks the given person.
     */
    void unmarkPerson(Person target, Person unmarked);

    /** Returns an indication of current undoable states */
    boolean canUndo();

    /**
     * Undo the last command by reverting to the last historical state.
     */
    void undo();
}

package seedu.mentorstack.model;

import static java.util.Objects.requireNonNull;
import static seedu.mentorstack.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.mentorstack.commons.core.GuiSettings;
import seedu.mentorstack.commons.core.LogsCenter;
import seedu.mentorstack.model.person.Person;

/**
 * Represents the in-memory model of the Mentorstack data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Mentorstack mentorstack;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final FilteredList<Person> sortedPersons;
    private final Deque<ReadOnlyMentorstack> history = new ArrayDeque<>();

    /**
     * Initializes a ModelManager with the given mentorstack and userPrefs.
     */
    public ModelManager(ReadOnlyMentorstack mentorstack, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(mentorstack, userPrefs);

        logger.fine("Initializing with mentorstack: " + mentorstack + " and user prefs " + userPrefs);

        this.mentorstack = new Mentorstack(mentorstack);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.mentorstack.getPersonList());
<<<<<<< Updated upstream
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
=======
        sortedPersons = new FilteredList<>(this.mentorstack.getSortedPersonList());
>>>>>>> Stashed changes
    }

    public ModelManager() {
        this(new Mentorstack(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getMentorstackFilePath() {
        return userPrefs.getMentorstackFilePath();
    }

    @Override
    public void setMentorstackFilePath(Path mentorstackFilePath) {
        requireNonNull(mentorstackFilePath);
        userPrefs.setMentorstackFilePath(mentorstackFilePath);
    }

    //=========== Mentorstack ================================================================================

    @Override
    public void setMentorstack(ReadOnlyMentorstack mentorstack) {
        history.push(new Mentorstack(this.mentorstack));
        this.mentorstack.resetData(mentorstack);
    }

    @Override
    public ReadOnlyMentorstack getMentorstack() {
        return mentorstack;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return mentorstack.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        history.push(new Mentorstack(this.mentorstack));
        mentorstack.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        history.push(new Mentorstack(this.mentorstack));
        mentorstack.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        history.push(new Mentorstack(this.mentorstack));
        mentorstack.setPerson(target, editedPerson);
    }

    @Override
    public boolean canUndo() {
        return !history.isEmpty();
    }

    @Override
    public void undo() {
        if (!canUndo()) {
            throw new NoSuchElementException("No previous state to undo.");
        }
        this.mentorstack.resetData(history.pop()); // Restore previous state
    }

    @Override
    public void archivePerson(Person personToArchive, Person archived) {
        requireAllNonNull(personToArchive, archived);
        mentorstack.archive(personToArchive, archived);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void unarchivePerson(Person personToUnarchive, Person unarchived) {
        requireAllNonNull(personToUnarchive, unarchived);
        mentorstack.unarchive(personToUnarchive, unarchived);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_ARCHIVED_PERSONS);
    }

    @Override
    public void markPerson(Person target, Person marked) {
        requireAllNonNull(target, marked);
        mentorstack.mark(target, marked);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void unmarkPerson(Person target, Person unmarked) {
        requireAllNonNull(target, unmarked);
        mentorstack.unmark(target, unmarked);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedMentorstack}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Person> getSortedPersonList() {
        return sortedPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
        sortedPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return mentorstack.equals(otherModelManager.mentorstack)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}

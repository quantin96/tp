package seedu.mentorstack.model.person;

import static seedu.mentorstack.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.mentorstack.commons.util.ToStringBuilder;

/**
 * Represents a Person in Mentorstack.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person implements Comparable<Person>{

    // Identity fields
    private final Name name;
    private final Gender gender;
    private final Phone phone;
    private final Email email;
    private final ArchiveStatus isArchived;
    private final boolean isMarked;

    // Data fields
    private final Set<Subject> subject = new HashSet<>();
    private final Set<Subject> finishedSubject = new HashSet<>();

    /**
     * Every field must be present and not null, new student is default to have archive status to be false.
     */
    public Person(Name name, Gender gender, Phone phone, Email email,
                  Set<Subject> subject) {
        requireAllNonNull(name, phone, email, subject);
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.subject.addAll(subject);
        this.isArchived = new ArchiveStatus("false");
        this.isMarked = false;
    }

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Gender gender, Phone phone, Email email,
                  Set<Subject> subject, Set<Subject> finishedSubjects,
                  ArchiveStatus isArchived, boolean isMarked) {
        requireAllNonNull(name, phone, email, subject, isArchived);
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.subject.addAll(subject);
        this.finishedSubject.addAll(finishedSubjects);
        this.isArchived = isArchived;
        this.isMarked = isMarked;
    }

    public Name getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    /**
     * Returns an immutable subject set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Subject> getSubjects() {
        return Collections.unmodifiableSet(subject);
    }

    public Set<Subject> getFinishedSubjects() {
        return Collections.unmodifiableSet(finishedSubject);
    }

    public ArchiveStatus getIsArchived() {
        return isArchived;
    }

    public boolean getIsMarked() {
        return isMarked;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && gender.equals(otherPerson.gender)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && subject.equals(otherPerson.subject)
                && finishedSubject.equals(otherPerson.finishedSubject)
                && isArchived.equals(otherPerson.isArchived)
                && isMarked == otherPerson.isMarked;
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, gender, phone, email, subject, finishedSubject, isArchived, isMarked);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("gender", gender)
                .add("phone", phone)
                .add("email", email)
                .add("subject", subject)
                .add("finished subject", finishedSubject)
                .add("archive status", isArchived)
                .add("marked", isMarked)
                .toString();
    }

    @Override
    public int compareTo(Person other) {
        return this.getName().compareTo(other.getName()); // Case-insensitive sorting
    }

    /**
     * Returns a person that is identical to the person except it is archived.
     */
    public Person archived() {
        return new Person(this.name, this.gender, this.phone, this.email,
                this.subject, this.finishedSubject, new ArchiveStatus("true"), this.isMarked);
    }

    /**
     * Returns a person that is identical to the person except it is unarchived.
     */
    public Person unarchived() {
        return new Person(this.name, this.gender, this.phone, this.email,
                this.subject, this.finishedSubject, new ArchiveStatus("false"), this.isMarked);
    }

    /**
     * Returns a person that is identical to the person except it is marked.
     */
    public Person marked() {
        return new Person(this.name, this.gender, this.phone, this.email,
                this.subject, this.finishedSubject, this.isArchived, true);
    }

    /**
     * Returns a person that is identical to the person except it is unmarked.
     */
    public Person unmarked() {
        return new Person(this.name, this.gender, this.phone, this.email,
                this.subject, this.finishedSubject, this.isArchived, false);
    }

}

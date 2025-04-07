package seedu.mentorstack.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.mentorstack.commons.exceptions.IllegalValueException;
import seedu.mentorstack.model.person.ArchiveStatus;
import seedu.mentorstack.model.person.Email;
import seedu.mentorstack.model.person.Gender;
import seedu.mentorstack.model.person.Name;
import seedu.mentorstack.model.person.Person;
import seedu.mentorstack.model.person.Phone;
import seedu.mentorstack.model.person.Subject;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String gender;
    private final String phone;
    private final String email;
    private final List<JsonAdaptedSubject> subject = new ArrayList<>();
    private final List<JsonAdaptedSubject> finishedSubject = new ArrayList<>();
    private final String isArchived;
    private final boolean isMarked;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("gender") String gender,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("subject") List<JsonAdaptedSubject> subject) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        if (subject != null) {
            this.subject.addAll(subject);
        }
        this.isArchived = "false";
        this.isMarked = false;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        gender = source.getGender().value;
        email = source.getEmail().value;
        subject.addAll(source.getSubjects().stream()
                .map(JsonAdaptedSubject::new)
                .collect(Collectors.toList()));
        finishedSubject.addAll(source.getFinishedSubjects().stream()
                .map(JsonAdaptedSubject::new)
                .collect(Collectors.toList()));
        isArchived = source.getIsArchived().getStatus();
        isMarked = source.getIsMarked();
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Subject> personSubject = new ArrayList<>();
        final List<Subject> personFinishedSubject = new ArrayList<>();

        for (JsonAdaptedSubject sub : subject) {
            personSubject.add(sub.toModelType());
        }

        for (JsonAdaptedSubject sub : finishedSubject) {
            personFinishedSubject.add(sub.toModelType());
        }

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (gender == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Gender.class.getSimpleName()));
        }
        if (!Gender.isValidGender(gender)) {
            throw new IllegalValueException(Gender.MESSAGE_CONSTRAINTS);
        }
        final Gender modelGender = new Gender(gender);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (!ArchiveStatus.isValidArchiveStatus(isArchived)) {
            throw new IllegalValueException(ArchiveStatus.MESSAGE_CONSTRAINTS);
        }

        final Set<Subject> modelSubject = new HashSet<>(personSubject);
        final Set<Subject> modelFinishedSubject = new HashSet<>(personFinishedSubject);
        final ArchiveStatus modelArchiveStatus = new ArchiveStatus(isArchived);
        return new Person(modelName, modelGender, modelPhone, modelEmail,
                modelSubject, modelFinishedSubject, modelArchiveStatus, isMarked);
    }

}

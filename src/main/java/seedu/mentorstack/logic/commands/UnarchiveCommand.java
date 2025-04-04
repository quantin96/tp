package seedu.mentorstack.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.mentorstack.commons.core.index.Index;
import seedu.mentorstack.commons.util.ToStringBuilder;
import seedu.mentorstack.logic.Messages;
import seedu.mentorstack.logic.commands.exceptions.CommandException;
import seedu.mentorstack.model.Model;
import seedu.mentorstack.model.person.Person;

/**
 * Unarchives a person identified using it's displayed index from Mentorstack.
 */
public class UnarchiveCommand extends Command {

    public static final String COMMAND_WORD = "unarchive";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unarchives the persons identified by the index numbers used in the displayed person list.\n"
            + "Parameters: INDEX1 INDEX2 ... (each must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_UNARCHIVE_PERSON_SUCCESS = "Unarchived Persons: %1$s\n"
            + "Here is the list of all archived persons:";

    public static final String MESSAGE_DUPLICATE_UNARCHIVE = "This person is already unarchived: ";

    private final Set<Index> targetIndices;

    public UnarchiveCommand(Set<Index> targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        // Ensure all indices are valid before unarchive
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        for (Index index : targetIndices) {
            if (!lastShownList.get(index.getZeroBased()).getIsArchived().testStatus()) {
                throw new CommandException(MESSAGE_DUPLICATE_UNARCHIVE
                        + Messages.format(lastShownList.get(index.getZeroBased())));
            }
        }

        ArrayList<Person> personsToUnarchive = new ArrayList<Person>();
        for (Index index : targetIndices) {
            Person personToUnarchive = lastShownList.get(index.getZeroBased());
            personsToUnarchive.add(personToUnarchive);
        }

        // Perform unarchiving
        model.rememberMentorstack(); // save the state for undo
        StringBuilder unarchivedPersons = new StringBuilder();
        for (Person personToUnarchive : personsToUnarchive) {
            model.unarchivePerson(personToUnarchive, personToUnarchive.unarchived());
            unarchivedPersons.append(Messages.format(personToUnarchive)).append("\n");
        }
        return new CommandResult(String.format(MESSAGE_UNARCHIVE_PERSON_SUCCESS, unarchivedPersons.toString().trim()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnarchiveCommand)) {
            return false;
        }

        UnarchiveCommand otherUnarchiveCommand = (UnarchiveCommand) other;
        return targetIndices.equals(otherUnarchiveCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndices)
                .toString();
    }
}

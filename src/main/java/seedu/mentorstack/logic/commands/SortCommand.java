package seedu.mentorstack.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.mentorstack.model.Model;

public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS = "Sorted current persons";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.getSortedPersonList();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}

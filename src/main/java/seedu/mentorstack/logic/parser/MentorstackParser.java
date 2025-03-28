package seedu.mentorstack.logic.parser;

import static seedu.mentorstack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.mentorstack.logic.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.mentorstack.commons.core.LogsCenter;
import seedu.mentorstack.logic.commands.AddCommand;
import seedu.mentorstack.logic.commands.ArchiveCommand;
import seedu.mentorstack.logic.commands.ClearCommand;
import seedu.mentorstack.logic.commands.Command;
import seedu.mentorstack.logic.commands.DeleteCommand;
import seedu.mentorstack.logic.commands.EditCommand;
import seedu.mentorstack.logic.commands.ExitCommand;
import seedu.mentorstack.logic.commands.FindCommand;
import seedu.mentorstack.logic.commands.FinishCommand;
import seedu.mentorstack.logic.commands.HelpCommand;
import seedu.mentorstack.logic.commands.ListCommand;
<<<<<<< Updated upstream
import seedu.mentorstack.logic.commands.MarkCommand;
import seedu.mentorstack.logic.commands.ShowArchiveCommand;
=======
import seedu.mentorstack.logic.commands.SortCommand;
>>>>>>> Stashed changes
import seedu.mentorstack.logic.commands.StatsCommand;
import seedu.mentorstack.logic.commands.UnarchiveCommand;
import seedu.mentorstack.logic.commands.UndoCommand;
import seedu.mentorstack.logic.commands.UnfinishCommand;
import seedu.mentorstack.logic.commands.UnmarkCommand;
import seedu.mentorstack.logic.commands.ViewCommand;
import seedu.mentorstack.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class MentorstackParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");
    private static final Logger logger = LogsCenter.getLogger(MentorstackParser.class);

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        // Note to developers: Change the log level in config.json to enable lower level (i.e., FINE, FINER and lower)
        // log messages such as the one below.
        // Lower level log messages are used sparingly to minimize noise in the code.
        logger.fine("Command word: " + commandWord + "; Arguments: " + arguments);

        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ViewCommand.COMMAND_WORD:
            return new ViewCommandParser().parse(arguments);

        case ArchiveCommand.COMMAND_WORD:
            return new ArchiveCommandParser().parse(arguments);

        case UnarchiveCommand.COMMAND_WORD:
            return new UnarchiveCommandParser().parse(arguments);

        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case StatsCommand.COMMAND_WORD:
            return new StatsCommandParser().parse(arguments);

<<<<<<< Updated upstream
        case MarkCommand.COMMAND_WORD:
            return new MarkCommandParser().parse(arguments);

        case UnmarkCommand.COMMAND_WORD:
            return new UnmarkCommandParser().parse(arguments);

        case ShowArchiveCommand.COMMAND_WORD:
            return new ShowArchiveCommand();

        case FinishCommand.COMMAND_WORD:
            return new FinishCommandParser().parse(arguments);

        case UnfinishCommand.COMMAND_WORD:
            return new UnfinishCommandParser().parse(arguments);
=======
        case SortCommand.COMMAND_WORD:
            return new SortCommand();
>>>>>>> Stashed changes

        default:
            logger.finer("This user input caused a ParseException: " + userInput);
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}

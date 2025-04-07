package seedu.mentorstack.logic.parser;

import static seedu.mentorstack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import seedu.mentorstack.commons.core.index.Index;
import seedu.mentorstack.logic.commands.DeleteCommand;
import seedu.mentorstack.logic.parser.exceptions.ParseException;
import seedu.mentorstack.logic.parser.exceptions.ParseWithHintException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser extends CommandParser implements Parser<DeleteCommand> {

    private static final Logger logger = Logger.getLogger(DeleteCommandParser.class.getName());

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException, ParseWithHintException {
        logger.log(Level.INFO, "Parsing DeleteCommand with arguments: {0}", args);
        try {
            Set<Index> indices = ParserUtil.parseIndexes(args);
            logger.log(Level.INFO, "Successfully parsed indices: {0}", indices);
            return new DeleteCommand(indices);
        } catch (ParseException pe) {
            logger.log(Level.WARNING, "Failed to parse DeleteCommand arguments", pe);
            throw new ParseWithHintException(
                    String.format(
                        MESSAGE_INVALID_COMMAND_FORMAT,
                        DeleteCommand.MESSAGE_USAGE),
                        pe,
                        "INDEX [INDEX] [INDEX]"
                    );
        }
    }
}

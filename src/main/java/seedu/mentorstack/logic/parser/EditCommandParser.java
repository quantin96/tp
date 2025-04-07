package seedu.mentorstack.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.mentorstack.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.mentorstack.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.mentorstack.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.mentorstack.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.mentorstack.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.mentorstack.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.mentorstack.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import seedu.mentorstack.commons.core.index.Index;
import seedu.mentorstack.logic.commands.EditCommand;
import seedu.mentorstack.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.mentorstack.logic.parser.exceptions.ParseException;
import seedu.mentorstack.logic.parser.exceptions.ParseWithHintException;
import seedu.mentorstack.model.person.Subject;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser extends CommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditCommand parse(String args) throws ParseException, ParseWithHintException {

        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args,
                        PREFIX_NAME, PREFIX_GENDER, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_SUBJECT);

        Index index;

        Map<String, String> ideal = new HashMap<>();
        ideal.put("[n/", "John Doe]");
        ideal.put("[e/", "johnd@example.com]");
        ideal.put("[p/", "98765432]");
        ideal.put("[s/", "CS2103]");
        ideal.put("[g/", "F]");

        String missing = super.getMissingArgs(argMultimap, ideal);

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseWithHintException(String.format(
                MESSAGE_INVALID_COMMAND_FORMAT,
                EditCommand.MESSAGE_USAGE),
                pe,
                "INDEX " + missing
            );
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_GENDER, PREFIX_PHONE, PREFIX_EMAIL);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editPersonDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_GENDER).isPresent()) {
            editPersonDescriptor.setGender(ParserUtil.parseGender(argMultimap.getValue(PREFIX_GENDER).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editPersonDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editPersonDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        parseSubjectsForEdit(argMultimap.getAllValues(PREFIX_SUBJECT)).ifPresent(editPersonDescriptor::setSubjects);

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseWithHintException(EditCommand.MESSAGE_NOT_EDITED, missing);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> subjects} into a {@code Set<Subjects>} if {@code subjects} is non-empty.
     * If {@code subjects} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Subjects>} containing zero subjects.
     */
    private Optional<Set<Subject>> parseSubjectsForEdit(Collection<String> subjects) throws ParseException {
        assert subjects != null;

        if (subjects.isEmpty()) {
            return Optional.empty();
        }

        Collection<String> subjectSet = subjects.size() == 1 && subjects.contains("")
                ? Collections.emptySet() : subjects;
        return Optional.of(ParserUtil.parseSubjects(subjectSet));
    }

}

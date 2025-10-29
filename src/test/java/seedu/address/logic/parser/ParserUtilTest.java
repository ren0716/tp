package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_INDEX_RANGE;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_LEVEL = " ";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_LEVEL = "2";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseMultipleIndex_singleIndex_success() throws Exception {
        // Single index without whitespace
        List<Index> expected = Arrays.asList(INDEX_FIRST_PERSON);
        assertEquals(expected, ParserUtil.parseMultipleIndex("1"));

        // Single index with whitespace
        assertEquals(expected, ParserUtil.parseMultipleIndex("  1  "));
    }

    @Test
    public void parseMultipleIndex_validRange_success() throws Exception {
        // Simple range
        List<Index> expected = Arrays.asList(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                Index.fromOneBased(3)
        );
        assertEquals(expected, ParserUtil.parseMultipleIndex("1-3"));

        // Range with whitespace
        assertEquals(expected, ParserUtil.parseMultipleIndex("1-3"));
    }

    @Test
    public void parseMultipleIndex_mixIndexType_success() throws Exception {
        // Simple range
        List<Index> expected = Arrays.asList(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                Index.fromOneBased(3)
        );
        assertEquals(expected, ParserUtil.parseMultipleIndex("1 2-3"));

        // Range with whitespace
        assertEquals(expected, ParserUtil.parseMultipleIndex("1-2       3"));
    }

    @Test
    public void parseMultipleIndex_invalidRange_throwsParseException() {
        // End less than start
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX_RANGE, () ->
                ParserUtil.parseMultipleIndex("3-1"));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseLevel_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseLevel((String) null));
    }

    @Test
    public void parseLevel_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseLevel(INVALID_LEVEL));
    }

    @Test
    public void parseLevel_validValueWithoutWhitespace_returnsLevel() throws Exception {
        Level expectedLevel = new Level(VALID_LEVEL);
        assertEquals(expectedLevel, ParserUtil.parseLevel(VALID_LEVEL));
    }

    @Test
    public void parseLevel_validValueWithWhitespace_returnsTrimmedLevel() throws Exception {
        String levelWithWhitespace = WHITESPACE + VALID_LEVEL + WHITESPACE;
        Level expectedLevel = new Level(VALID_LEVEL);
        assertEquals(expectedLevel, ParserUtil.parseLevel(levelWithWhitespace));
    }


    @Test
    public void parseIndexFromPreamble_nullPreamble_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndexFromPreamble(null, "usage"));
    }

    @Test
    public void parseIndexFromPreamble_blankPreamble_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndexFromPreamble("   ", "usage"));
    }

    @Test
    public void parseIndexFromPreamble_multipleTokens_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndexFromPreamble("1 2", "usage"));
    }

    @Test
    public void parseIndexFromPreamble_invalidIndex_passesThroughIndexMessage() {
        assertThrows(ParseException.class, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, ()
                -> ParserUtil.parseIndexFromPreamble("abc", "usage"));
    }

    @Test
    public void parseIndexFromPreamble_validIndex_success() throws Exception {
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndexFromPreamble(" 1 ", "usage"));
    }

    @Test
    public void parseOptionalAssignments_emptyCollection_returnsEmptyOptional() throws Exception {
        Optional<Set<Assignment>> result = ParserUtil.parseOptionalAssignments(Collections.emptyList(), "cs101");
        assertFalse(result.isPresent());
    }

    @Test
    public void parseOptionalAssignments_singleEmptyToken_returnsPresentEmptySet() throws Exception {
        Optional<Set<Assignment>> result = ParserUtil.parseOptionalAssignments(java.util.Arrays.asList(""), "CS101");
        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    public void parseOptionalAssignments_validAssignments_returnsParsedSet() throws Exception {
        Optional<Set<Assignment>> result = ParserUtil.parseOptionalAssignments(
                java.util.Arrays.asList("Homework1", "Lab2"), "CS101");
        assertTrue(result.isPresent());
        Set<Assignment> set = result.get();
        assertEquals(2, set.size());
        assertTrue(set.contains(new Assignment("homework1", "cs101")));
        assertTrue(set.contains(new Assignment("lab2", "cs101")));
    }

    @Test
    public void parseOptionalAssignments_invalidAssignment_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.parseOptionalAssignments(java.util.Arrays.asList("!badname"), "CS101"));
    }

    @Test
    public void parseOptionalClassGroups_emptyCollection_returnsEmptyOptional() throws Exception {
        Optional<Set<ClassGroup>> result = ParserUtil.parseOptionalClassGroups(Collections.emptyList());
        assertFalse(result.isPresent());
    }

    @Test
    public void parseOptionalClassGroups_singleEmptyToken_returnsPresentEmptySet() throws Exception {
        Optional<Set<ClassGroup>> result = ParserUtil.parseOptionalClassGroups(java.util.Arrays.asList(""));
        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }

    @Test
    public void parseOptionalClassGroups_validClassGroups_returnsParsedSet() throws Exception {
        Optional<Set<ClassGroup>> result = ParserUtil.parseOptionalClassGroups(
                java.util.Arrays.asList("CS101", "maths"));
        assertTrue(result.isPresent());
        Set<ClassGroup> set = result.get();
        assertEquals(2, set.size());
        assertTrue(set.contains(new ClassGroup("cs101")));
        assertTrue(set.contains(new ClassGroup("maths")));
    }

    @Test
    public void parseClassGroup_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseClassGroup(null));
    }

    @Test
    public void parseClassGroup_invalidName_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseClassGroup("!"));
    }

}

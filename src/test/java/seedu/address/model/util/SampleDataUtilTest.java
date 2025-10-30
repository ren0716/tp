package seedu.address.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Person;

/**
 * Contains unit tests for {@code SampleDataUtil}.
 */
public class SampleDataUtilTest {

    @Test
    public void getSamplePersons_notNull() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertNotNull(persons);
        assertTrue(persons.length > 0);
    }

    @Test
    public void getSamplePersons_correctNumberOfPersons() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        assertEquals(6, persons.length);
    }

    @Test
    public void getSamplePersons_allPersonsHaveValidData() {
        Person[] persons = SampleDataUtil.getSamplePersons();
        for (Person person : persons) {
            assertNotNull(person.getName());
            assertNotNull(person.getPhone());
            assertNotNull(person.getLevel());
            assertNotNull(person.getClassGroups());
            assertNotNull(person.getAssignments());
        }
    }

    @Test
    public void getSampleAddressBook_notNull() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertNotNull(addressBook);
    }

    @Test
    public void getSampleAddressBook_containsSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        assertEquals(6, addressBook.getPersonList().size());
    }

    @Test
    public void getSampleAddressBook_personsMatchGetSamplePersons() {
        ReadOnlyAddressBook addressBook = SampleDataUtil.getSampleAddressBook();
        Person[] samplePersons = SampleDataUtil.getSamplePersons();

        assertEquals(samplePersons.length, addressBook.getPersonList().size());
    }

    @Test
    public void getClassGroup_singleClassGroup() {
        Set<ClassGroup> classGroups = SampleDataUtil.getClassGroup("physics-1800");
        assertNotNull(classGroups);
        assertEquals(1, classGroups.size());
        assertTrue(classGroups.stream()
                .anyMatch(cg -> cg.getClassGroupName().equals("physics-1800")));
    }

    @Test
    public void getClassGroup_multipleClassGroups() {
        Set<ClassGroup> classGroups = SampleDataUtil.getClassGroup("physics-1800", "math-2000", "chem-1200");
        assertNotNull(classGroups);
        assertEquals(3, classGroups.size());
    }

    @Test
    public void getClassGroup_emptyArray() {
        Set<ClassGroup> classGroups = SampleDataUtil.getClassGroup();
        assertNotNull(classGroups);
        assertEquals(0, classGroups.size());
    }

    @Test
    public void getAssignmentSet_singleAssignment() {
        Set<Assignment> assignments = SampleDataUtil.getAssignmentSet("physics-1800", "math homework");
        assertNotNull(assignments);
        assertEquals(1, assignments.size());

        Assignment assignment = assignments.iterator().next();
        assertEquals("math homework", assignment.getAssignmentName());
        assertEquals("physics-1800", assignment.getClassGroupName());
    }

    @Test
    public void getAssignmentSet_multipleAssignments() {
        Set<Assignment> assignments = SampleDataUtil.getAssignmentSet(
                "physics-1800", "math homework", "science project", "english essay");
        assertNotNull(assignments);
        assertEquals(3, assignments.size());

        // Verify all assignments have the correct class group
        assertTrue(assignments.stream()
                .allMatch(a -> a.getClassGroupName().equals("physics-1800")));
    }

    @Test
    public void getAssignmentSet_emptyAssignments() {
        Set<Assignment> assignments = SampleDataUtil.getAssignmentSet("test-class");
        assertNotNull(assignments);
        assertEquals(0, assignments.size());
    }

    @Test
    public void getAssignmentSet_assignmentsHaveCorrectClassGroup() {
        String classGroup = "math-2000";
        Set<Assignment> assignments = SampleDataUtil.getAssignmentSet(classGroup, "HW1", "HW2");

        for (Assignment assignment : assignments) {
            assertEquals(classGroup, assignment.getClassGroupName());
        }
    }
}


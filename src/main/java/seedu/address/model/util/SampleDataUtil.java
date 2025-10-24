package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.classgroup.ClassGroup;
import seedu.address.model.person.Level;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"),
                new Level("1"),
                getClassGroup("physics-1800"),
                getAssignmentSet("physics-1800", "math homework", "science project")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"),
                new Level("2"),
                getClassGroup("physics-1800", "math-2000"),
                getAssignmentSet("physics-1800", "english essay")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                new Level("3"),
                getClassGroup("chem-1200"),
                getAssignmentSet("chem-1200", "history presentation", "art portfolio")),
            new Person(new Name("David Li"), new Phone("91031282"),
                new Level("4"),
                getClassGroup("chem-1300"),
                getAssignmentSet("chem-1300", "geography report")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
                new Level("1"),
                getClassGroup("math-2000"),
                getAssignmentSet("math-2000", "computer science lab")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
                new Level("2"),
                getClassGroup("math-2000"),
                getAssignmentSet("math-2000", "physics experiment"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a classGroup set containing the list of strings given.
     */
    public static Set<ClassGroup> getClassGroup(String... strings) {
        return Arrays.stream(strings)
                .map(ClassGroup::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns an assignment set containing the list of strings given.
     * The first string is the class group name, and the rest are assignment names.
     */
    public static Set<Assignment> getAssignmentSet(String classGroupName, String... assignmentNames) {
        return Arrays.stream(assignmentNames)
                .map(assignmentName -> new Assignment(assignmentName, classGroupName))
                .collect(Collectors.toSet());
    }

}

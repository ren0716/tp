package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.assignment.Assignment;
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
                getClassGroup("Physics-1800"),
                getAssignmentSet("Math Homework", "Science Project")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"),
                new Level("2"),
                getClassGroup("Physics-1800", "Math-2000"),
                getAssignmentSet("English Essay")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                new Level("3"),
                getClassGroup("Chem-1200"),
                getAssignmentSet("History Presentation", "Art Portfolio")),
            new Person(new Name("David Li"), new Phone("91031282"),
                new Level("4"),
                getClassGroup("Chem-1300"),
                getAssignmentSet("Geography Report")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"),
                new Level("1"),
                getClassGroup("Math-2000"),
                getAssignmentSet("Computer Science Lab")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"),
                new Level("2"),
                getClassGroup("Math-2000"),
                getAssignmentSet("Physics Experiment"))
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
    public static Set<String> getClassGroup(String... strings) {
        return Arrays.stream(strings)
                .map(String::new)
                .collect(Collectors.toSet());
    }

    /**
     * Returns an assignment set containing the list of strings given.
     */
    public static Set<Assignment> getAssignmentSet(String... strings) {
        return Arrays.stream(strings)
                .map(Assignment::new)
                .collect(Collectors.toSet());
    }

}

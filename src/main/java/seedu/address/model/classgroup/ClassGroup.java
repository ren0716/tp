package seedu.address.model.classgroup;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;

import seedu.address.model.person.Person;

/**
 * Represents a group of students with a specific class schedule.
 */
public class ClassGroup {
    private ArrayList<Person> studentList;
    private TimeSlot timeSlot;

    //edit these 2 classes
    /** Subject taught to this class group. */
    private String Subject;
    /** List of assignment names for this class group. */
    private ArrayList<String> assignmentList;
    //

    /**
     * Constructs a {@code ClassGroup} with a specific {@code TimeSlot}.
     *
     * @param day        the day of the week
     * @param startTime  the start time (inclusive)
     * @param endTime    the end time (exclusive)
     */
    public ClassGroup(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.studentList = new ArrayList<>();
        this.timeSlot = new TimeSlot(day, startTime, endTime);
    }

}

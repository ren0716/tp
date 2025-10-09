package seedu.address.model.classGroup;

import seedu.address.model.person.Person;
import seedu.address.model.classGroup.TimeSlot;

import java.util.ArrayList;
import java.time.LocalTime;
import java.time.DayOfWeek;


public class ClassGroup {
    private ArrayList<Person> studentList;
    private TimeSlot timeSlot;

    //edit these 2 classes
    private String Subject;
    private ArrayList<String> assignmentList;
    //

    public ClassGroup(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.studentList = new ArrayList<>();
        this.timeSlot = new TimeSlot(day, startTime, endTime);
    }

}

package seedu.address.model.classgroup;

import java.time.DayOfWeek;
import java.time.LocalTime;


/**
 * Represents a recurring weekly time slot (e.g., "Wednesday 14:00–15:00").
 * This class is immutable.
 */
public class TimeSlot {
    private final DayOfWeek day;
    private final LocalTime startTime;
    private final LocalTime endTime;


    /**
     * Constructs a {@code TimeSlot}.
     *
     * @param day        the day of the week
     * @param startTime  the start time (inclusive)
     * @param endTime    the end time (exclusive)
     */
    public TimeSlot(DayOfWeek day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("%s %s–%s",
                day,
                startTime,
                endTime);
    }
}


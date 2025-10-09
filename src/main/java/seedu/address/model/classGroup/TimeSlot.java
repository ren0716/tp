package seedu.address.model.classGroup;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class TimeSlot {
    private final DayOfWeek day;
    private final LocalTime startTime;
    private final LocalTime endTime;

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


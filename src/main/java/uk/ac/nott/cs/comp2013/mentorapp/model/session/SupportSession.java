package uk.ac.nott.cs.comp2013.mentorapp.model.session;

import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import java.time.LocalDate;

public class SupportSession {
    private Mentor mentor;
    private Mentee mentee;
    private LocalDate startDate;
    private LocalDate endDate;

    public SupportSession(Mentor mentor, Mentee mentee, LocalDate startDate) {
        this(mentor, mentee, startDate, startDate.plusMonths(6));
    }

    public SupportSession(Mentor mentor, Mentee mentee, LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start and end dates cannot be null.");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be earlier than start date.");
        }

        this.mentor = mentor;
        this.mentee = mentee;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public Mentee getMentee() {
        return mentee;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isSessionActive() {
        LocalDate today = LocalDate.now();
        return today.isAfter(startDate) && today.isBefore(endDate);
    }
}

package uk.ac.nott.cs.comp2013.mentorapp.model.session;


import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SupportSession {
    private Mentor mentor;
    private Mentee mentee;
    private LocalDate startDate;
    private LocalDate endDate;

    public SupportSession(Mentor mentor, Mentee mentee, LocalDate startDate) {
        this.mentor = mentor;
        this.mentee = mentee;
        this.startDate = startDate;
        this.endDate = startDate.plusMonths(6);
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
        return LocalDate.now().isBefore(endDate);
    }

    public long getRemainingMonths() {
        return ChronoUnit.MONTHS.between(LocalDate.now(), endDate);
    }
}

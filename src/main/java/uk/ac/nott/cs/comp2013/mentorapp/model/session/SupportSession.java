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

    public void extendSession(int months) {
        if (months <= 0) {
            throw new IllegalArgumentException("Extension months must be positive.");
        }
        this.endDate = this.endDate.plusMonths(months);
    }

    public boolean isSessionActive() {
        LocalDate today = LocalDate.now();
        return today.isAfter(startDate) && today.isBefore(endDate);
    }

    public long getRemainingMonths() {
        long months = ChronoUnit.MONTHS.between(LocalDate.now(), endDate);
        return Math.max(months, 0); // 确保不返回负值
    }

    public SessionStatus getSessionStatus() {
        LocalDate today = LocalDate.now();
        if (today.isBefore(startDate)) {
            return SessionStatus.NOT_STARTED;
        } else if (today.isBefore(endDate)) {
            return SessionStatus.ACTIVE;
        } else {
            return SessionStatus.COMPLETED;
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Session [Mentor: %s, Mentee: %s, Start: %s, End: %s, Status: %s]",
                mentor.getUsername(),
                mentee.getUsername(),
                startDate,
                endDate,
                getSessionStatus()
        );
    }

    public enum SessionStatus {
        NOT_STARTED,
        ACTIVE,
        COMPLETED
    }
}

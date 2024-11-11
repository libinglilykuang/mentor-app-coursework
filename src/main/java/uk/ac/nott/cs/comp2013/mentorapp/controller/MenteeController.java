package uk.ac.nott.cs.comp2013.mentorapp.controller;

import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

import java.util.List;

public class MenteeController {
    private final Mentee mentee;

    public MenteeController(Mentee mentee) {
        this.mentee = mentee;
    }

    public void requestSupport(SupportType supportType) {
        mentee.setSupportRequest(supportType);
    }

    public Mentee getMentee() {
        return mentee;
    }

    public List<Mentor> getAssignedMentors() {
        return List.of(
                new Mentor("MentorA", "password123"),
                new Mentor("MentorB", "password456")
        );
    }

}
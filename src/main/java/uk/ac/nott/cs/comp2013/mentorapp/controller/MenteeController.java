package uk.ac.nott.cs.comp2013.mentorapp.controller;

import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;

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
}
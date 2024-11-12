package uk.ac.nott.cs.comp2013.mentorapp.controller;

import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;

public class MentorController {
    private final Mentor mentor;

    public MentorController(Mentor mentor) {
        this.mentor = mentor;
    }

    public Mentor getMentor() {
        return mentor;
    }
}

package uk.ac.nott.cs.comp2013.mentorapp.controller;

import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

/**
 * The MentorController handles logic and data manipulation for a mentor.
 */
public class MentorController {
    private Repository<User, String> mentorRepository; // Repository to manage mentor data
    private final Mentor mentor; // The mentor associated with this controller

    /**
     * Constructor with only a mentor object.
     *
     * @param mentor The mentor to be managed by this controller.
     */
    public MentorController(Mentor mentor) {
        this.mentor = mentor;
    }

    /**
     * Constructor with both a repository and a mentor object.
     *
     * @param mentorRepository The repository to manage mentor data.
     * @param mentor           The mentor to be managed by this controller.
     *                         If the mentor is null, a default mentor is created.
     */
    public MentorController(Repository<User, String> mentorRepository, Mentor mentor) {
        this.mentorRepository = mentorRepository;
        // Assign a default mentor if none is provided
        if (mentor == null) {
            this.mentor = new Mentor("default_mentor", "password", null, null);
        } else {
            this.mentor = mentor;
        }
    }

    /**
     * Gets the mentor managed by this controller.
     *
     * @return The mentor object.
     */
    public Mentor getMentor() {
        return this.mentor;
    }

    /**
     * Gets the repository managing mentor data.
     *
     * @return The mentor repository.
     */
    public Repository<User, String> getRepository() {
        return mentorRepository;
    }
}

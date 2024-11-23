package uk.ac.nott.cs.comp2013.mentorapp.controller;

import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

/**
 * Controller class to manage mentee-related actions and data.
 */
public class MenteeController {
    private Mentee mentee;
    private final Repository<User, String> menteeRepository;

    /**
     * Constructor for MenteeController.
     *
     * @param menteeRepository The repository managing mentee data.
     * @param mentee           The mentee to be managed by this controller.
     */
    public MenteeController(Repository<User, String> menteeRepository, Mentee mentee) {
        this.menteeRepository = menteeRepository;
        this.mentee = mentee;
    }

    /**
     * Submits a support request for the currently managed mentee.
     *
     * @param supportType The type of support requested by the mentee.
     */
    public void requestSupport(SupportType supportType) {
        mentee.setSupportRequest(supportType); // Update the mentee's support request
        menteeRepository.update(mentee); // Save the updated mentee data in the repository
    }

    /**
     * Updates the mentee being managed by this controller.
     *
     * @param mentee The new mentee to manage.
     */
    public void setMentee(Mentee mentee) {
        this.mentee = mentee;
    }

    /**
     * Retrieves the currently managed mentee.
     *
     * @return The current mentee.
     */
    public Mentee getMentee() {
        return mentee;
    }

    /**
     * Retrieves the repository managing mentee data.
     *
     * @return The mentee repository.
     */
    public Repository<User, String> getRepository() {
        return menteeRepository;
    }
}

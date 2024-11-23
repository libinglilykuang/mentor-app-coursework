package uk.ac.nott.cs.comp2013.mentorapp.controller;

import javafx.util.Pair;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportSession;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Administrator;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller class for admin operations such as pairing mentees with mentors,
 * retrieving user information, and managing support sessions.
 */
public class AdminController {
    private Repository<User, String> userRepository;
    private List<SupportSession> sessions = new ArrayList<>();
    private Administrator currentAdmin;

    /**
     * Constructs the AdminController with a user repository.
     *
     * @param userRepository The repository containing user data.
     */
    public AdminController(Repository<User, String> userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Sets the currently logged-in admin.
     *
     * @param admin The admin to set as current.
     */
    public void setCurrentAdmin(Administrator admin) {
        this.currentAdmin = admin;
    }

    /**
     * Retrieves the currently logged-in admin.
     *
     * @return The current admin.
     * @throws IllegalStateException If no admin is logged in.
     */
    public Administrator getAdmin() {
        if (currentAdmin != null) {
            return currentAdmin;
        } else {
            throw new IllegalStateException("No admin is currently logged in.");
        }
    }

    /**
     * Pairs a mentee with a mentor and creates a support session if successful.
     *
     * @param menteeId The ID of the mentee.
     * @param mentorId The ID of the mentor.
     * @return True if pairing is successful; false otherwise.
     */
    public boolean pairMenteeWithMentor(String menteeId, String mentorId) {
        Optional<User> menteeOpt = userRepository.selectById(menteeId);
        Optional<User> mentorOpt = userRepository.selectById(mentorId);
        if (menteeOpt.isPresent() && mentorOpt.isPresent()
                && menteeOpt.get() instanceof Mentee mentee
                && mentorOpt.get() instanceof Mentor mentor) {
            if (!mentor.isAvailable()) return false;
            if (mentor.assignMentee(mentee)) {
                mentee.assignMentor(mentor);
                createSupportSession(mentor, mentee);
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new support session between a mentor and a mentee.
     *
     * @param mentor The mentor involved in the session.
     * @param mentee The mentee involved in the session.
     * @return The created SupportSession object.
     */
    private SupportSession createSupportSession(Mentor mentor, Mentee mentee) {
        SupportSession session = new SupportSession(mentor, mentee, LocalDate.now());
        sessions.add(session);
        return session;
    }

    /**
     * Retrieves a list of all unpaired mentees.
     *
     * @return A list of unpaired mentees.
     */
    public List<Mentee> getAllUnpairedMentees() {
        return getAllMentees().stream()
                .filter(mentee -> mentee.getAssignedMentor() == null)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all available mentors.
     *
     * @return A list of available mentors.
     */
    public List<Mentor> getAvailableMentors() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentor)
                .map(user -> (Mentor) user)
                .filter(Mentor::isAvailable)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of all mentees.
     *
     * @return A list of all mentees.
     */
    public List<Mentee> getAllMentees() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentee)
                .map(user -> (Mentee) user)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of assigned mentor-mentee pairs.
     *
     * @return A list of mentor-mentee pairs.
     */
    public List<Pair<Mentor, Mentee>> getAssignedPairs() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentee)
                .map(user -> (Mentee) user)
                .filter(mentee -> mentee.getAssignedMentor() != null)
                .map(mentee -> new Pair<>(mentee.getAssignedMentor(), mentee))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the mentor type by the mentor's name.
     *
     * @param mentorName The name of the mentor.
     * @return The mentor type description or an empty string if not found.
     */
    public String getMentorTypeByName(String mentorName) {
        Optional<User> mentorOpt = userRepository.selectById(mentorName);
        if (mentorOpt.isPresent() && mentorOpt.get() instanceof Mentor mentor) {
            return mentor.getMentorType() != null ? mentor.getMentorType().getDescription() : "";
        }
        return "";
    }

    /**
     * Retrieves the availability of a mentor by their name.
     *
     * @param mentorName The name of the mentor.
     * @return The availability in a formatted string or a message if unavailable.
     */
    public String getMentorAvailabilityByName(String mentorName) {
        Optional<User> mentorOpt = userRepository.selectById(mentorName); // Retrieve mentor by name
        if (mentorOpt.isPresent() && mentorOpt.get() instanceof Mentor mentor) {
            if (mentor.getStartAvailability() != null && mentor.getEndAvailability() != null) {
                return mentor.getStartAvailability().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " - " +
                        mentor.getEndAvailability().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); // Format availability
            }
            return "Not available"; // Availability not set
        }
        return "Not found"; // Mentor not found
    }

    /**
     * Retrieves the CV text of a mentee by their name.
     *
     * @param menteeName The name of the mentee.
     * @return The CV text of the mentee or null if not found.
     */
    public String getMenteeCVByName(String menteeName) {
        Optional<User> menteeOpt = userRepository.selectById(menteeName); // Retrieve mentee by name
        if (menteeOpt.isPresent() && menteeOpt.get() instanceof Mentee mentee) {
            return mentee.getCvText(); // Return CV text
        }
        return null; // Mentee not found
    }
}

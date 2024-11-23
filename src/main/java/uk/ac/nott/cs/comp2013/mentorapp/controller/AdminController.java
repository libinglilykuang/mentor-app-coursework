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

public class AdminController {
    private Repository<User, String> userRepository;
    private List<SupportSession> sessions = new ArrayList<>();
    private Administrator currentAdmin;

    public AdminController(Repository<User, String> userRepository) {
        this.userRepository = userRepository;
    }

    public void setCurrentAdmin(Administrator admin) {
        this.currentAdmin = admin;
    }

    public Administrator getAdmin() {
        if (currentAdmin != null) {
            return currentAdmin;
        } else {
            throw new IllegalStateException("No admin is currently logged in.");
        }
    }

    public boolean pairMenteeWithMentor(String menteeId, String mentorId) {
        Optional<User> menteeOpt = userRepository.selectById(menteeId);
        Optional<User> mentorOpt = userRepository.selectById(mentorId);

        if (menteeOpt.isPresent() && mentorOpt.isPresent()
                && menteeOpt.get() instanceof Mentee mentee
                && mentorOpt.get() instanceof Mentor mentor) {

            if (!mentor.isAvailable()) {
                System.out.println("This mentor is already assigned.");
                return false;
            }

            if (mentor.assignMentee(mentee)) {
                mentee.assignMentor(mentor);
                createSupportSession(mentor, mentee);
                System.out.println("Mentee " + menteeId + " successfully paired with Mentor " + mentorId);
                return true;
            }
        }
        System.out.println("Mentee or Mentor not found or invalid type.");
        return false;
    }

    private SupportSession createSupportSession(Mentor mentor, Mentee mentee) {
        SupportSession session = new SupportSession(mentor, mentee, LocalDate.now());
        sessions.add(session);
        return session;
    }

    public List<Mentee> getAllUnpairedMentees() {
        return getAllMentees().stream()
                .filter(mentee -> mentee.getAssignedMentor() == null)
                .collect(Collectors.toList());
    }



    public List<Mentor> getAvailableMentors() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentor)
                .map(user -> (Mentor) user)
                .filter(Mentor::isAvailable)
                .collect(Collectors.toList());
    }

    public List<Mentee> getAllMentees() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentee)
                .map(user -> (Mentee) user)
                .collect(Collectors.toList());
    }

    public List<Pair<Mentor, Mentee>> getAssignedPairs() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentee)
                .map(user -> (Mentee) user)
                .filter(mentee -> mentee.getAssignedMentor() != null)
                .map(mentee -> new Pair<>(mentee.getAssignedMentor(), mentee))
                .collect(Collectors.toList());
    }

    public String getMentorTypeByName(String mentorName) {
        Optional<User> mentorOpt = userRepository.selectById(mentorName);
        if (mentorOpt.isPresent() && mentorOpt.get() instanceof Mentor mentor) {
            return mentor.getMentorType() != null ? mentor.getMentorType().getDescription() : "";
        }
        return "";
    }

    public String getMentorAvailabilityByName(String mentorName) {
        Optional<User> mentorOpt = userRepository.selectById(mentorName);
        if (mentorOpt.isPresent() && mentorOpt.get() instanceof Mentor mentor) {
            if (mentor.getStartAvailability() != null && mentor.getEndAvailability() != null) {
                return mentor.getStartAvailability().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " - " +
                        mentor.getEndAvailability().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            return "Not available";
        }
        return "Not find";
    }

    public String getMenteeCVByName(String menteeName) {
        Optional<User> menteeOpt = userRepository.selectById(menteeName);
        if (menteeOpt.isPresent() && menteeOpt.get() instanceof Mentee) {
            Mentee mentee = (Mentee) menteeOpt.get();
            return mentee.getCvText();
        }
        return null;
    }

}

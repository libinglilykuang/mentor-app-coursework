/*package uk.ac.nott.cs.comp2013.mentorapp.controller;

import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportSession;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.util.Pair;

public class AdminController {
    private Repository<User, String> userRepository;
    private List<SupportSession> sessions = new ArrayList<>();

    public AdminController(Repository<User, String> userRepository) {
        this.userRepository = userRepository;
    }

    public boolean pairMenteeWithMentor(String menteeId, String mentorId) {
        Optional<User> menteeOpt = userRepository.selectById(menteeId);
        Optional<User> mentorOpt = userRepository.selectById(mentorId);

        if (menteeOpt.isPresent() && mentorOpt.isPresent() &&
                menteeOpt.get() instanceof Mentee && mentorOpt.get() instanceof Mentor) {

            Mentee mentee = (Mentee) menteeOpt.get();
            Mentor mentor = (Mentor) mentorOpt.get();

            if (mentor.isAvailable()) {
                if (mentor.assignMentee(mentee)) {
                    mentee.assignMentor(mentor);
                    createSupportSession(mentor, mentee);
                    System.out.println("Mentee " + menteeId + " successfully paired with Mentor " + mentorId);
                    return true;
                }
            } else {
                System.out.println("This mentor is already assigned to a mentee for the current period.");
            }
        } else {
            System.out.println("Mentee or Mentor not found or invalid type");
        }
        return false;
    }

    private SupportSession createSupportSession(Mentor mentor, Mentee mentee) {
        SupportSession session = new SupportSession(mentor, mentee, LocalDate.now());
        sessions.add(session);
        return session;
    }

    public List<SupportSession> getActiveSessions() {
        List<SupportSession> activeSessions = new ArrayList<>();
        for (SupportSession session : sessions) {
            if (session.isSessionActive()) {
                activeSessions.add(session);
            }
        }
        return activeSessions;
    }

    public List<Mentee> getAllUnpairedMentees() {
        List<Mentee> unpairedMentees = new ArrayList<>();
        for (User user : userRepository.selectAll()) {
            if (user instanceof Mentee mentee && mentee.getAssignedMentor() == null && mentee.getSupportRequest() != null) {
                unpairedMentees.add(mentee);
            }
        }
        return unpairedMentees;
    }

    public List<Mentor> getAvailableMentors() {
        List<Mentor> availableMentors = new ArrayList<>();
        for (User user : userRepository.selectAll()) {
            if (user instanceof Mentor mentor && mentor.isAvailable()) {
                availableMentors.add(mentor);
                //System.out.println("Adding available mentor: " + mentor.getUsername());
            }
        }
        //System.out.println("Total available mentors: " + availableMentors.size());
        return availableMentors;
    }

    public List<Pair<Mentor, Mentee>> getAssignedPairs() {
        List<Pair<Mentor, Mentee>> assignedPairs = new ArrayList<>();
        for (SupportSession session : sessions) {
            if (session.isSessionActive()) {
                assignedPairs.add(new Pair<>(session.getMentor(), session.getMentee()));
            }
        }
        return assignedPairs;
    }

}*/
package uk.ac.nott.cs.comp2013.mentorapp.controller;

import javafx.util.Pair;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportSession;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminController {
    private Repository<User, String> userRepository;
    private List<SupportSession> sessions = new ArrayList<>();

    public AdminController(Repository<User, String> userRepository) {
        this.userRepository = userRepository;
    }

    public boolean pairMenteeWithMentor(String menteeId, String mentorId) {
        Optional<User> menteeOpt = userRepository.selectById(menteeId);
        Optional<User> mentorOpt = userRepository.selectById(mentorId);

        if (menteeOpt.isPresent() && mentorOpt.isPresent()
                && menteeOpt.get() instanceof Mentee mentee
                && mentorOpt.get() instanceof Mentor mentor) {

            if (mentor.isAvailable()) {
                if (mentor.assignMentee(mentee)) {
                    mentee.assignMentor(mentor);
                    createSupportSession(mentor, mentee);
                    System.out.println("Mentee " + menteeId + " successfully paired with Mentor " + mentorId);
                    return true;
                }
            } else {
                System.out.println("This mentor is already assigned to a mentee for the current period.");
            }
        } else {
            System.out.println("Mentee or Mentor not found or invalid type");
        }
        return false;
    }

    private SupportSession createSupportSession(Mentor mentor, Mentee mentee) {
        SupportSession session = new SupportSession(mentor, mentee, LocalDate.now());
        sessions.add(session);
        return session;
    }

    public List<SupportSession> getActiveSessions() {
        List<SupportSession> activeSessions = new ArrayList<>();
        for (SupportSession session : sessions) {
            if (session.isSessionActive()) {
                activeSessions.add(session);
            }
        }
        return activeSessions;
    }

    public List<Mentee> getAllUnpairedMentees() {
        return getAllMentees().stream()
                .filter(mentee -> mentee.getAssignedMentor() == null && mentee.getSupportRequest() != null)
                .collect(Collectors.toList());
    }

    public List<Mentor> getAvailableMentors() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentor)
                .map(user -> (Mentor) user)
                .collect(Collectors.toList());
    }

    /*public List<Mentee> getAllUnpairedMentees() {
        List<Mentee> unpairedMentees = new ArrayList<>();
        List<User> users = userRepository.selectAll();
        if (users != null) {
            for (User user : users) {
                if (user instanceof Mentee mentee && mentee.getAssignedMentor() == null && mentee.getSupportRequest() != null) {
                    unpairedMentees.add(mentee);
                }
            }
        }
        return unpairedMentees;
    }*/

    public List<Mentee> getAllMentees() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentee)
                .map(user -> (Mentee) user)
                .collect(Collectors.toList());
    }
    public List<Mentor> getAllMentors() {
        return userRepository.selectAll().stream()
                .filter(user -> user instanceof Mentor)
                .map(user -> (Mentor) user)
                .collect(Collectors.toList());
    }


    /*public List<Mentor> getAvailableMentors() {
        List<Mentor> availableMentors = new ArrayList<>();
        List<User> users = userRepository.selectAll();
        if (users != null) {
            for (User user : users) {
                if (user instanceof Mentor mentor && mentor.isAvailable()) {
                    availableMentors.add(mentor);
                }
            }
        }
        return availableMentors;
    }*/


    public List<Pair<Mentor, Mentee>> getAssignedPairs() {
        List<Pair<Mentor, Mentee>> assignedPairs = new ArrayList<>();
        for (SupportSession session : sessions) {
            if (session.isSessionActive()) {
                assignedPairs.add(new Pair<>(session.getMentor(), session.getMentee()));
            }
        }
        return assignedPairs;
    }
}


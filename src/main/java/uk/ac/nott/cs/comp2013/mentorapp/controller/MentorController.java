package uk.ac.nott.cs.comp2013.mentorapp.controller;

import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

public class MentorController {
    private Repository<User, String> mentorRepository;
    private final Mentor mentor;

    public MentorController(Mentor mentor) {
        this.mentor = mentor;
    }

    public MentorController(Repository<User, String> mentorRepository, Mentor mentor) {
        this.mentorRepository = mentorRepository;
        if (mentor == null) {
            this.mentor = new Mentor("default_mentor", "password", null, null);
        } else {
            this.mentor = mentor;
        }
    }

    public Mentor getMentor() {
        return this.mentor;
    }

    public Repository<User, String> getRepository() {
        return mentorRepository;
    }

    public boolean addMentor(Mentor newMentor) {
        if (mentorRepository.selectById(newMentor.getUsername()).isPresent()) {
            return false;
        }
        mentorRepository.insert(newMentor);
        return true;
    }
}

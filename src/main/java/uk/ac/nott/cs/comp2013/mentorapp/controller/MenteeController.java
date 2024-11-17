package uk.ac.nott.cs.comp2013.mentorapp.controller;

import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;

public class MenteeController {
    private Mentee mentee;
    private final Repository<User, String> menteeRepository;

    public MenteeController(Repository<User, String> menteeRepository, Mentee mentee) {
        this.menteeRepository = menteeRepository;
        this.mentee = mentee;
    }

    public void requestSupport(SupportType supportType) {
        mentee.setSupportRequest(supportType);
        menteeRepository.update(mentee);
    }

    public void setMentee(Mentee mentee) {
        this.mentee = mentee;
    }

    public Mentee getMentee() {
        return mentee;
    }

    public boolean addNewMentee(Mentee newMentee) {
        if (menteeRepository.selectById(newMentee.getUsername()).isPresent()) {
            return false;
        }
        menteeRepository.insert(newMentee);
        return true;
    }

    public Repository<User, String> getRepository() {
        return menteeRepository;
    }

    public boolean addMentee(Mentee newMentee) {
        if (menteeRepository.selectById(newMentee.getUsername()).isPresent()) {
            return false;
        }
        menteeRepository.insert(newMentee);
        return true;
    }
}
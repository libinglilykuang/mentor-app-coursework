package uk.ac.nott.cs.comp2013.mentorapp;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.*;
import uk.ac.nott.cs.comp2013.mentorapp.model.*;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.*;
import uk.ac.nott.cs.comp2013.mentorapp.view.*;

public class MentorApp extends Application {
    private Repository<User, String> repo;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            initializeRepository();

            if (repo == null || repo.selectAll().isEmpty()) {
                throw new IllegalStateException("Repository is not properly initialized or contains no users.");
            }

            ViewManager viewManager = new ViewManager(stage);
            setupHomePage(viewManager);

            stage.setWidth(1024);
            stage.setHeight(768);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.setTitle("Mentor App");
            stage.show();
        } catch (Exception e) {
            System.err.println("Error during application start:");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the user repository by processing the CSV file.
     */
    private void initializeRepository() {
        String csvPath = "src/main/resources/MOCK_DATA.csv";
        try {
            repo = RepositoryFactory.createRepositoryFromCsv(csvPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the homepage view and links to other views via ViewManager.
     */
    private void setupHomePage(ViewManager viewManager) {
        HomepageView homepageView = new HomepageView(() -> {
            IdentitySelectionView identitySelectionView = createIdentitySelectionView(viewManager);
            viewManager.addView(ViewManager.IDENTITY_SELECTION, identitySelectionView);
            viewManager.setStageView(ViewManager.IDENTITY_SELECTION);
        });

        viewManager.addView("HOMEPAGE", homepageView);
        viewManager.setStageView("HOMEPAGE");
    }

    /**
     * Creates the IdentitySelectionView and sets up navigation to the LoginView.
     */
    private IdentitySelectionView createIdentitySelectionView(ViewManager viewManager) {
        return new IdentitySelectionView(userType -> {
            LoginController loginController = new LoginController(repo);
            LoginView loginView = new LoginView(loginController);
            viewManager.addView(ViewManager.LOGIN, loginView);

            setupUserSpecificViews(viewManager);
            viewManager.setStageView(ViewManager.LOGIN);
        });
    }

    /**
     * Sets up the views for mentees, mentors, and admins, and adds them to the ViewManager.
     */
    private void setupUserSpecificViews(ViewManager viewManager) {
        addMenteeView(viewManager);
        addMentorView(viewManager);
        addAdminView(viewManager);
    }

    /**
     * Adds the mentee view to the ViewManager.
     */
    private void addMenteeView(ViewManager viewManager) {
        Mentee mentee = findFirstUserOfType(Mentee.class);
        if (mentee != null) {
            MenteeController menteeController = new MenteeController(repo, mentee);
            if (mentee.getSupportRequest() == null) {
                menteeController.requestSupport(SupportType.CV_REVIEW);
            }
            MenteeView menteeView = new MenteeView(menteeController);
            viewManager.addView(ViewManager.MENTEE, menteeView);
        }
    }

    /**
     * Adds the mentor view to the ViewManager.
     */
    private void addMentorView(ViewManager viewManager) {
        Mentor mentor = findFirstUserOfType(Mentor.class);
        if (mentor != null) {
            MentorController mentorController = new MentorController(repo, mentor);
            MentorView mentorView = new MentorView(mentorController);
            viewManager.addView(ViewManager.MENTOR, mentorView);
        }
    }

    /**
     * Adds the admin view to the ViewManager.
     */
    private void addAdminView(ViewManager viewManager) {
        AdminController adminController = new AdminController(repo);
        AdminView adminView = new AdminView(adminController);
        viewManager.addView(ViewManager.ADMIN, adminView);
    }

    /**
     * Finds the first user of the specified type in the repository.
     */
    private <T extends User> T findFirstUserOfType(Class<T> userType) {
        return repo.selectAll()
                .stream()
                .filter(userType::isInstance)
                .map(userType::cast)
                .findFirst()
                .orElse(null);
    }
}

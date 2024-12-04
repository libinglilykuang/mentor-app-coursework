package uk.ac.nott.cs.comp2013.mentorapp;

import javafx.application.Application;
import javafx.stage.Stage;

import uk.ac.nott.cs.comp2013.mentorapp.controller.*;
import uk.ac.nott.cs.comp2013.mentorapp.model.*;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.*;
import uk.ac.nott.cs.comp2013.mentorapp.view.*;

/**
 * The MentorApp class serves as the main entry point for the Mentor-Mentee Management System.
 * It initializes the application, sets up repositories, and manages the views.
 */
public class MentorApp extends Application {
    private Repository<User, String> repo; // Repository to manage users.

    /**
     * Launches the application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Starts the application, initializing repositories and the view manager.
     *
     * @param stage The primary stage for the application.
     */
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
            stage.setAlwaysOnTop(true);
            stage.setAlwaysOnTop(false);
        } catch (Exception e) {
            System.err.println("Error during application start:");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the user repository by processing the CSV file.
     */
    private void initializeRepository() {
        String csvPath = "src/main/resources/data/MOCK_DATA.csv";
        try {
            repo = RepositoryFactory.createRepositoryFromCsv(csvPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up the homepage and configures the ViewManager to navigate to it.
     *
     * @param viewManager The ViewManager instance to manage views.
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
     * Creates the IdentitySelectionView and links it to the login process.
     *
     * @param viewManager The ViewManager instance to manage views.
     * @return An instance of IdentitySelectionView.
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
     * Configures views for mentees, mentors, and admins and adds them to the ViewManager.
     *
     * @param viewManager The ViewManager instance to manage views.
     */
    private void setupUserSpecificViews(ViewManager viewManager) {
        addMenteeView(viewManager);
        addMentorView(viewManager);
        addAdminView(viewManager);
    }

    /**
     * Adds a mentee view to the ViewManager if a mentee exists in the repository.
     *
     * @param viewManager The ViewManager instance to manage views.
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
     * Adds a mentor view to the ViewManager if a mentor exists in the repository.
     *
     * @param viewManager The ViewManager instance to manage views.
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
     * Adds an admin view to the ViewManager.
     *
     * @param viewManager The ViewManager instance to manage views.
     */
    private void addAdminView(ViewManager viewManager) {
        AdminController adminController = new AdminController(repo);
        AdminView adminView = new AdminView(adminController);
        viewManager.addView(ViewManager.ADMIN, adminView);
    }

    /**
     * Finds the first user of a specified type in the repository.
     *
     * @param userType The class type of the user to search for.
     * @param <T>      The type of the user.
     * @return The first user of the specified type, or null if none are found.
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

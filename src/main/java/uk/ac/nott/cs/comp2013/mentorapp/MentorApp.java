package uk.ac.nott.cs.comp2013.mentorapp;

import javafx.application.Application;
import javafx.stage.Stage;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.model.Repository;
import uk.ac.nott.cs.comp2013.mentorapp.model.RepositoryFactory;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;
import uk.ac.nott.cs.comp2013.mentorapp.view.*;

public class MentorApp extends Application {
    Repository<User, String> repo;

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Initializes the user repository by processing the CSV files.
     */
    private void createRepository() {
        String csvPath = "src/main/resources/MOCK_DATA.csv";
        try {
            repo = RepositoryFactory.createRepositoryFromCsv(csvPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the login view with appropriate event handling.
     *
     * @return the initialized LoginView instance
     */
    private LoginView createLoginView() {
        LoginController controller = new LoginController(repo);
        LoginView loginView = new LoginView(controller);

        loginView.setOnViewChange(event -> {
            User currentUser = controller.getCurrentUser();
            if (currentUser != null) {
                String viewKey = switch (currentUser.getRole()) {
                    case MENTEE -> ViewManager.MENTEE;
                    case MENTOR -> ViewManager.MENTOR;
                    case ADMIN -> ViewManager.ADMIN;
                };
                ViewManager viewManager = (ViewManager) event.getSource();
                viewManager.setStageView(viewKey);
            }
        });

        return loginView;
    }

    @Override
    public void start(Stage stage) {
        try {
            createRepository();
            if (repo == null || repo.selectAll().isEmpty()) {
                throw new IllegalStateException("Repository is not properly initialized or contains no users.");
            }

            ViewManager sm = new ViewManager(stage);

            // Create homepage
            HomepageView homepageView = new HomepageView(() -> {
                // Action after clicking "Verify Identity"
                IdentitySelectionView identitySelectionView = new IdentitySelectionView(userType -> {
                    LoginController loginController = new LoginController(repo);
                    LoginView loginView = new LoginView(loginController);
                    sm.addView(ViewManager.LOGIN, loginView);
                    addViews(sm);
                    sm.setStageView(ViewManager.LOGIN);
                });
                sm.addView(ViewManager.IDENTITY_SELECTION, identitySelectionView);
                sm.setStageView(ViewManager.IDENTITY_SELECTION);
            });

            // Add homepage to ViewManager
            sm.addView("HOMEPAGE", homepageView);
            sm.setStageView("HOMEPAGE");

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

    private void addViews(ViewManager sm) {
        Mentee mentee = repo.selectAll()
                .stream()
                .filter(user -> user instanceof Mentee)
                .map(user -> (Mentee) user)
                .findFirst()
                .orElse(null);
        if (mentee != null) {
            MenteeController menteeController = new MenteeController(repo, mentee);
            if (mentee.getSupportRequest() == null) {
                menteeController.requestSupport(SupportType.CV_REVIEW);
            }
            MenteeView menteeView = new MenteeView(menteeController);
            sm.addView(ViewManager.MENTEE, menteeView);
        }

        Mentor mentor = repo.selectAll()
                .stream()
                .filter(user -> user instanceof Mentor)
                .map(user -> (Mentor) user)
                .findFirst()
                .orElse(null);
        if (mentor != null) {
            MentorController mentorController = new MentorController(repo, mentor);
            MentorView mentorView = new MentorView(mentorController);
            sm.addView(ViewManager.MENTOR, mentorView);
        }

        AdminController adminController = new AdminController(repo);
        AdminView adminView = new AdminView(adminController);
        sm.addView(ViewManager.ADMIN, adminView);
    }
}

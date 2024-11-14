package uk.ac.nott.cs.comp2013.mentorapp;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.FileReader;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import uk.ac.nott.cs.comp2013.mentorapp.controller.AdminController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.LoginController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MenteeController;
import uk.ac.nott.cs.comp2013.mentorapp.controller.MentorController;
import uk.ac.nott.cs.comp2013.mentorapp.model.HashMapRepository;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.MentorType;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Administrator;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentee;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.Mentor;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.User;
import uk.ac.nott.cs.comp2013.mentorapp.util.CsvCleaner;
import uk.ac.nott.cs.comp2013.mentorapp.view.*;
import com.opencsv.CSVReader;

public class MentorApp extends Application {
  private HashMapRepository<User, String> repo;

  public static void main(String[] args) {
    Application.launch(args);
  }

    private void createRepository() {
        String rawCsvPath = "src/main/resources/MOCK_DATA.csv";
        String cleanedCsvPath = "src/main/resources/CLEANED_MOCK_DATA.csv";
        CsvCleaner.cleanCsv(rawCsvPath, cleanedCsvPath);

        repo = new HashMapRepository<>();
        try (CSVReader reader = new CSVReader(new FileReader(cleanedCsvPath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                String username = record[0];
                String password = record[1];
                String role = record[2];
                String extraInfo = record.length > 3 ? record[3] : "";
                String startDate = record.length > 4 ? record[4] : null;
                String endDate = record.length > 5 ? record[5] : null;
                String activityType = record.length > 6 ? record[6] : "";
                String mentorType = record.length > 7 ? record[7] : "";

                // Skip header rows
                if ("ROLE".equalsIgnoreCase(role) || role.isEmpty()) {
                    continue;
                }

                SupportType supportType = parseSupportType(activityType);
                MentorType mentorType1=parseMentorType(mentorType);

                switch (role) {
                    case "MENTEE" ->{
                        if (supportType != null) {
                            if(mentorType!=null){
                                repo.insert(new Mentee(username, password, extraInfo, supportType,mentorType1));
                            }
                            repo.insert(new Mentee(username, password, extraInfo, supportType));
                        } else {
                            repo.insert(new Mentee(username, password, extraInfo));
                        }
                    }
                    case "MENTOR" -> {
                        LocalDateTime start = parseDateSafely(startDate);
                        LocalDateTime end = parseDateSafely(endDate);
                        MentorType type = parseMentorType(mentorType);
                        repo.insert(new Mentor(username, password, start, end,type));
                    }
                    case "ADMIN" -> repo.insert(new Administrator(username, password));
                }
            }
        } catch (Exception e) {}
    }

    /**
     * Parses the activity type from CSV into the SupportType enum.
     *
     * @param activityType The activity type as a string.
     * @return The corresponding SupportType, or null if no match is found.
     */
    private SupportType parseSupportType(String activityType) {
        for (SupportType type : SupportType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(activityType)) {
                return type;
            }
        }
        return null;
    }

    private MentorType parseMentorType(String mentorType) {
        if (mentorType == null || mentorType.trim().isEmpty()) {
            return null;
        }
        for (MentorType type : MentorType.values()) {
            if (type.getDescription().equalsIgnoreCase(mentorType) || type.name().equalsIgnoreCase(mentorType)) {
                return type;
            }
        }
        System.err.println("Unknown mentor type: " + mentorType);
        return null;
    }

    // Utility method to parse dates safely without throwing exceptions
    private LocalDateTime parseDateSafely(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.ofInstant(Instant.parse(date), ZoneId.systemDefault());
        } catch (Exception e) {
            return null; // Return null if the date is invalid
        }
    }

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
  public void start(Stage stage) throws Exception {
    try {
        createRepository();

        // Ensure repository is initialized and has users
        if (repo == null || repo.selectAll().isEmpty()) {
          throw new IllegalStateException("Repository is not properly initialized or contains no users.");
        }

        ViewManager sm = new ViewManager(stage);
        sm.addView(ViewManager.LOGIN, createLoginView());

        Mentee mentee = repo.selectAll()
                .stream()
                .filter(user -> user instanceof Mentee)
                .map(user -> (Mentee) user)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No Mentee found in the repository."));

        MenteeController menteeController = new MenteeController(mentee);

        if (mentee.getSupportRequest() == null) {
            menteeController.requestSupport(SupportType.CV_REVIEW);
        }
        MenteeView menteeView = new MenteeView(menteeController);
        sm.addView(ViewManager.MENTEE, menteeView);

        Mentor mentor = repo.selectAll()
                .stream()
                .filter(user -> user instanceof Mentor)
                .map(user -> (Mentor) user)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No Mentee found in the repository."));
        MentorController mentorController = new MentorController(mentor);
        sm.addView(ViewManager.MENTOR, new MentorView(mentorController));

        AdminController adminController = new AdminController(repo);
        sm.addView(ViewManager.ADMIN, new AdminView(adminController));

        sm.setStageView(ViewManager.LOGIN);
        stage.setTitle("Mentor App");
        stage.show();
      } catch (Exception e) {
        System.err.println("Error during application start:");
        e.printStackTrace();
      }
  }
}

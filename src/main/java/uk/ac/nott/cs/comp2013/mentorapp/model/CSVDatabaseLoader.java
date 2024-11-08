package uk.ac.nott.cs.comp2013.mentorapp.model;

import uk.ac.nott.cs.comp2013.mentorapp.model.user.*;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVDatabaseLoader {

    private final String csvFilePath;

    public CSVDatabaseLoader(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 3) continue;

                String username = values[0];
                String password = values[1];
                String role = values[2].trim().toUpperCase();

                switch (role) {
                    case "MENTEE":
                        SupportType supportRequest = values.length > 3 ? SupportType.valueOf(values[3].trim().toUpperCase()) : null;
                        users.add(new Mentee(username, password, "Default CV Text"));
                        break;

                    case "MENTOR":
                        users.add(new Mentor(username, password));
                        break;

                    case "ADMIN":
                        users.add(new Administrator(username, password));
                        break;

                    default:
                        System.err.println("Unknown role: " + role);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}

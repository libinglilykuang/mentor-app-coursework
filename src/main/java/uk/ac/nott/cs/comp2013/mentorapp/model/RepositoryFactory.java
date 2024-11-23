package uk.ac.nott.cs.comp2013.mentorapp.model;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import uk.ac.nott.cs.comp2013.mentorapp.model.session.SupportType;
import uk.ac.nott.cs.comp2013.mentorapp.model.user.*;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class RepositoryFactory {
    public static HashMapRepository<User, String> createRepositoryFromCsv(String csvFilePath) throws IOException {
        HashMapRepository<User, String> repo = new HashMapRepository<>();

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            List<String[]> records = reader.readAll();
            // Skip the header row
            records.removeFirst();

            for (String[] record : records) {
                User user = parseUser(record);
                if (user != null) {
                    repo.insert(user);
                }
            }
        } catch (CsvException e) {
            throw new IOException("Failed to parse CSV file.", e);
        }

        return repo;
    }

    private static User parseUser(String[] record) {
        if (record.length < 3) {
            return null;
        }

        String username = record[0];
        String password = record[1];
        String role = record[2];
        String extraInfo = record.length > 3 ? record[3] : "";
        String startDate = record.length > 4 ? record[4] : null;
        String endDate = record.length > 5 ? record[5] : null;
        String activityType = record.length > 6 ? record[6] : "";
        String mentorType = record.length > 7 ? record[7] : "";
        String menteeType = record.length > 8 ? record[8] : "";

        return switch (role.toUpperCase()) {
            case "MENTEE" -> parseMentee(username, password, extraInfo, activityType, mentorType, menteeType);
            case "MENTOR" -> parseMentor(username, password, startDate, endDate, mentorType);
            case "ADMIN" -> new Administrator(username, password);
            default -> null;
        };
    }

    private static Mentee parseMentee(String username, String password, String extraInfo, String activityType, String mentorType, String menteeType) {
        SupportType supportType = parseSupportType(activityType);
        MentorType mentorTypeEnum = parseMentorType(mentorType);
        MenteeType menteeTypeEnum = parseMenteeType(menteeType);

        if (supportType != null && mentorTypeEnum != null) {
            return new Mentee(username, password, extraInfo, supportType, mentorTypeEnum, menteeTypeEnum);
        } else if (supportType != null) {
            return new Mentee(username, password, extraInfo, supportType);
        } else {
            return new Mentee(username, password, extraInfo);
        }
    }

    private static Mentor parseMentor(String username, String password, String startDate, String endDate, String mentorType) {
        LocalDateTime start = parseDateSafely(startDate);
        LocalDateTime end = parseDateSafely(endDate);
        if (start != null && end != null && start.isAfter(end)) {
            start = null;
            end = null;
        }
        MentorType type = parseMentorType(mentorType);
        return new Mentor(username, password, start, end, type);
    }

    private static SupportType parseSupportType(String activityType) {
        for (SupportType type : SupportType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(activityType)) {
                return type;
            }
        }
        return null;
    }

    private static MentorType parseMentorType(String mentorType) {
        if (mentorType == null || mentorType.trim().isEmpty()) {
            return null;
        }
        for (MentorType type : MentorType.values()) {
            if (type.getDescription().equalsIgnoreCase(mentorType) || type.name().equalsIgnoreCase(mentorType)) {
                return type;
            }
        }
        return null;
    }

    private static MenteeType parseMenteeType(String menteeType) {
        if (menteeType == null || menteeType.trim().isEmpty()) {
            return null;
        }
        for (MenteeType type : MenteeType.values()) {
            if (type.getDescription().equalsIgnoreCase(menteeType) || type.name().equalsIgnoreCase(menteeType)) {
                return type;
            }
        }
        return null;
    }

    private static LocalDateTime parseDateSafely(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.ofInstant(Instant.parse(date), ZoneId.systemDefault());
        } catch (Exception e) {
            System.out.println("Skipping record with invalid date: " + date);
            return null;
        }
    }
}
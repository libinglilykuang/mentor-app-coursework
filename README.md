# Mentor App Coursework

## Overview

The Mentor-Mentee Management System is designed to streamline the pairing and management of mentors and mentees for educational and professional development purposes. The system provides role-based functionality for administrators, mentors, and mentees, ensuring a seamless user experience for all stakeholders.

---

## Key Features

### 1. Admin Functionalities
- **Mentor-Mentee Pairing**:
   - Use a rule-based recommendation system to suggest suitable mentors for mentees based on availability and preferences.
   - *Implemented in `AdminPairingView`*.
- **View Pairing Reports**:
   - Access detailed reports of mentor-mentee pairings, including mentor types, availability, and mentee preferences.
   - *Implemented in `AssignedReportView`*.

### 2. Mentor Functionalities
- **Set Availability**:
   - Specify available dates and times for mentoring sessions.
   - *Implemented in `MentorAvailabilityView`*.
- **View Assigned Mentees**:
   - Check mentees assigned to the mentor and their corresponding session details.
   - *Implemented in `AssignedMenteeView`*.

### 3. Mentee Functionalities
- **Sign Up for Activities**:
   - Register for activities and view mentor assignment details.
   - *Implemented in `ActivitySignUpView`*.
- **View Assigned Mentor**:
   - Access mentor details and session availability.
   - *Implemented in `MenteeView`*.

### 4. General Functionalities
- **Role-Based Login**:
   - Supports role-specific interfaces for admins, mentors, and mentees after login.
   - *Implemented in `LoginView`*.
- **Profile Management**:
   - Allows users to edit profile details such as username, password, and real name.
   - *Implemented in `EditProfileView`*.

---

## Architecture and Design

The project is built using the MVC model with the following key components:
- **View Layer**:
   - Handles the user interface and interaction logic, implemented using JavaFX.
- **Controller Layer**:
   - Manages application logic and mediates between the view and model layers.
- **Model Layer**:
   - Represents the data and business logic of the system, including mentor and mentee entities.

### Technology Stack
- **Frontend**: JavaFX for dynamic UI development.
- **Backend**: Java-based logic with mock database integration.
- **Styling**: CSS for consistent and responsive design.

---

## Usage

### Prerequisites
- Java 17 or higher.
- An IDE such as IntelliJ IDEA or Eclipse.
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

The project is built using the **MVC (Model-View-Controller) Architecture** with the following key components:

- **View Layer**:
  - Handles the user interface (UI) and user interaction logic. It is responsible for rendering the interface and responding to user actions (e.g., button clicks, form submissions).
  - Implemented using **JavaFX**, which provides dynamic and responsive UI elements. The View layer listens for events from the user and sends them to the Controller for processing.

- **Controller Layer**:
  - Manages the application logic and mediates between the View and Model layers. The Controller listens for user input (via the View) and updates the Model accordingly. It then updates the View with the latest data or state changes.
  - For example, the **AdminController** handles pairing logic, while the **MenteeController** manages activity sign-ups.

- **Model Layer**:
  - Represents the core data and business logic of the system. It handles data management, such as storing and retrieving information about mentors, mentees, and their interactions.
  - The Model includes entities like **Mentor** and **Mentee**, along with any business rules or validation logic (e.g., mentor availability, mentee preferences).
  - The **Recommendation Engine** is a key component of the Model layer. It uses a **rule-based recommendation algorithm** to match mentees with mentors based on specific criteria such as mentor availability, mentee preferences, and mentor expertise.
  - The Model is also responsible for communicating with the mock database or any storage solutions, ensuring that data is kept consistent and updated across the system.

## Concurrency Management
The system leverages Java concurrency to handle long-running tasks and ensure responsive user interfaces:

- **ThreadPoolManager**:
Implements a global thread pool using the Singleton Pattern for centralized task management.
Handles concurrent tasks such as pairing logic and report generation efficiently.

- **JavaFX Task**:
Used for background processing (e.g., pairing mentees with mentors and refreshing reports).
Offloads these tasks from the JavaFX Application thread, avoiding UI blocking and enhancing user experience.

---

## Usage

### Prerequisites
- **Java**: Version 21 or higher.
- **IDE**: IntelliJ IDEA or another Java-compatible IDE.
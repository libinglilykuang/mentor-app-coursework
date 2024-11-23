package uk.ac.nott.cs.comp2013.mentorapp.model.session;

public enum SupportType {
    ASSESSMENT_CENTRE_PREPARATION(
            "Assessment Centre Preparation",
            "The mentor will help you prepare for assessment center processes and contents"
    ),
    CAREER_CHOICE_NO_IDEA(
            "Career Choice - No idea what to do",
            "The mentor will help you explore career options"
    ),
    CAREER_CHOICE_WITH_DEGREE(
            "Career Choice - What can I do with my degree?",
            "Discusses the connection between a degree and career opportunities"
    ),
    CAREER_CHOICE_ENTER_FIELD(
            "Career Choice: How to enter a specific career field",
            "The mentor will help you to understand what your career field requires, how it recruits and when, and how to approach the job market"
    ),
    CAREER_CHOICE_DECISIONS(
            "Career Choice: Making decisions about different ideas",
            "The mentor will help you to evaluate your ideas so that you can make informed choices"
    ),
    COVER_LETTER(
            "Cover Letter",
            "The mentor will guide you in writing compelling cover letters"
    ),
    CV_REVIEW(
            "CV Review",
            "The mentor will help you optimize your CV"
    ),
    FINDING_WORK_EXPERIENCE(
            "Finding Work Experience or Part-Time Jobs",
            "The mentor will assist you in finding internships or part-time jobs"
    ),
    INTERVIEW_PREPARATION(
            "Interview Preparation",
            "The mentor will provide training and advice for interviews, with mentors conducting mock interviews and giving feedback"
    ),
    JOB_APPLICATION(
            "Job Application",
            "The mentor will help you understand application processes and strategies"
    ),
    LINKEDIN_REVIEW(
            "LinkedIn Review",
            "The mentor will guide you in optimizing your LinkedIn profile"
    ),
    POSTGRADUATE_STUDY_APPLICATION(
            "Postgraduate Study Application",
            "The mentor will offer advice for postgraduate applications, with mentors guiding you on school selection and preparation materials"
    );

    private final String displayName;
    private final String description;

    SupportType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

}
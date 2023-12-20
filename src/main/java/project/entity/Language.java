package project.entity;

public enum Language {
    ENG("Англійська"),
    UKR("Українська"),
    SPA("Іспанська");

    Language(String languageName) {
        this.languageName = languageName;
    }

    private String languageName;

    public String getLanguageName() {
        return languageName;
    }
}

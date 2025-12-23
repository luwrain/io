package org.luwrain.io.api.github.models;

public class Issue {
    private int number;
    private String title;
    private String body;
    private User user;

    public static class User {
        private String login;
        public String getLogin() { return login; }
    }

    public int getNumber() { return number; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getAuthor() { return (user != null) ? user.getLogin() : "Anon"; }

    @Override
    public String toString() {
        return "[#" + number + "] " + title + " (от " + getAuthor() + ")";
    }
}
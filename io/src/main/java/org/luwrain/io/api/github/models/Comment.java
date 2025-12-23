package org.luwrain.io.api.github.models;

public class Comment {
    private long id;
    private String body;
    private User user;

    public static class User {
        private String login;
        public String getLogin() { return login; }
    }

    public long getId() { return id; }
    public String getBody() { return body; }
    public String getAuthor() { return (user != null) ? user.getLogin() : "Anon"; }

    @Override
    public String toString() {
        return "[" + getAuthor() + "]: " + body;
    }
}

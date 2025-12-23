package org.luwrain.io.api.github.models;

public class Repo
{
    private String name;
    private String full_name; // Например "facebook/react"
    private String description;
    private int stargazers_count; // Количество подписчиков

    public String getFullName() { return full_name; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getStars() { return stargazers_count; }

    @Override
    public String toString() {
        return full_name + " (⭐ " + stargazers_count + ")";
    }
}

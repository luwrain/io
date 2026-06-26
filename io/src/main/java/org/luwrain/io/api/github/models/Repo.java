package org.luwrain.io.api.github.models;

/**
 * Represents a GitHub repository.
 */
public class Repo
{
    private String name;
    private String full_name;
    private String description;
    private int stargazers_count;

    /**
     * Returns the full name of the repository in "owner/repo" format.
     * @return the full repository name
     */
    public String getFullName() { return full_name; }

    /**
     * Returns the short name of the repository.
     * @return the repository name
     */
    public String getName() { return name; }

    /**
     * Returns the repository description.
     * @return the description, or null if not set
     */
    public String getDescription() { return description; }

    /**
     * Returns the number of stars (stargazers) for this repository.
     * @return the star count
     */
    public int getStars() { return stargazers_count; }

    @Override
    public String toString() {
        return full_name + " (\u2B50 " + stargazers_count + ")";
    }
}

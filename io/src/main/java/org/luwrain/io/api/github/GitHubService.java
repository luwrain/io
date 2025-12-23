package org.luwrain.io.api.github;

import org.luwrain.io.api.github.models.Comment;
import org.luwrain.io.api.github.models.Issue;
import org.luwrain.io.api.github.models.Repo;
import java.util.List;

/**
 * Основной интерфейс для взаимодействия с GitHub API.
 */
public interface GitHubService {

    /**
     * Проверяет соединение и валидность токена.
     * @return true, если вход успешен.
     */
    boolean connect();

    /**
     * Возвращает список репозиториев текущего пользователя.
     */
    List<Repo> getMyRepos();

    /**
     * Ищет репозитории по запросу.
     * @param query поисковый запрос (например, "java tetris")
     */
    List<Repo> search(String query);

    /**
     * Подписаться на репозиторий (поставить Star).
     * @param repoFullName имя в формате "owner/repo"
     */
    void joinRepo(String repoFullName);

    /**
     * Отписаться от репозитория (убрать Star).
     */
    void leaveRepo(String repoFullName);

    /**
     * Получить список обсуждений (Issues) в репозитории.
     */
    List<Issue> getIssues(String repoFullName);

    /**
     * Создать новое обсуждение (Issue).
     */
    void createIssue(String repoFullName, String title, String text);

    /**
     * Закрыть обсуждение (Issue).
     */
    void closeIssue(String repoFullName, int issueNumber);

    /**
     * Получить комментарии внутри конкретного обсуждения.
     */
    List<Comment> getComments(String repoFullName, int issueNumber);

    /**
     * Написать комментарий (ответ) в обсуждение.
     */
    void postComment(String repoFullName, int issueNumber, String text);
}
package io.github.lhannest.bloggerific.data;

/**
 * Created by root on 05/03/17.
 */

public class Note {
    public final long localId;
    public final String title;
    public final String content;
    public final String bloggerId;
    public final long dateCreated;
    public final long dateLastModified;

    protected Note(long localId, String title, String content, long dateCreated, long dateLastModified, String bloggerId) {
        this.localId = localId;
        this.title = title;
        this.content = content;
        this.bloggerId = bloggerId;
        this.dateCreated = dateCreated;
        this.dateLastModified = dateLastModified;
    }

    protected Note(long localId, String title, String content, long dateCreated, long dateLastModified) {
        this(localId, title, content, dateCreated, dateLastModified, null);
    }

    public boolean isContentLoaded() {
        return content != null;
    }

    public boolean isBloggerPost() {
        return this.bloggerId != null;
    }
}

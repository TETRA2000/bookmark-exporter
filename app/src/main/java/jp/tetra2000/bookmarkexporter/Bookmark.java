package jp.tetra2000.bookmarkexporter;

/**
 * Created by Takahiko on 2015/01/05.
 */
public class Bookmark {
    public String title;
    public String URL;
    public String imgData;

    public Bookmark(String title, String URL) {
        this(title, URL, null);
    }

    public Bookmark(String title, String URL, String imgData) {
        this.title = title;
        this.URL = URL;
        this.imgData = imgData;
    }
}

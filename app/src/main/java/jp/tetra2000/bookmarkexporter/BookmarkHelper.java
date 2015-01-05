package jp.tetra2000.bookmarkexporter;

import java.util.ArrayList;
import java.util.List;


class BookmarkHelper {
    private static final String TEMPLATE_PRE =
            "<!DOCTYPE NETSCAPE-Bookmark-file-1>\n" +
            "\t<HTML>\n" +
            "\t<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n" +
            "\t<Title>Bookmarks</Title>\n" +
            "\t<H1>Bookmarks</H1>\n";
    private static final String TEMPLATE_SUF = "</HTML>\n";

    private List<Bookmark> mBookmarks = new ArrayList<>();

    public void add(Bookmark bookmark) {
        mBookmarks.add(bookmark);
    }

    public String toHTML() {

        StringBuilder builder = new StringBuilder();
        builder.append(TEMPLATE_PRE);

        for(Bookmark bookmark : mBookmarks) {
            // <DT><A HREF=\"http://www.google.com/jp/\">Google</A>
            builder.append("\t\t<DT><A HREF=\"");
            builder.append(bookmark.URL);
            builder.append("\">");
            builder.append(bookmark.title);
            builder.append("</A>\n");
        }

        builder.append(TEMPLATE_SUF);

        return builder.toString();
    }
}

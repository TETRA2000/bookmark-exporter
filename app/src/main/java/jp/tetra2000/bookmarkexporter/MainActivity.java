package jp.tetra2000.bookmarkexporter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Browser;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int BOOKMARK_LOADER = 0;
    private static final String[] BOOKMARK_PROJECTION = {
            Browser.BookmarkColumns.BOOKMARK, // whether bookmark or history
            Browser.BookmarkColumns.TITLE,
            Browser.BookmarkColumns.URL,
            Browser.BookmarkColumns.FAVICON,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportLoaderManager().initLoader(BOOKMARK_LOADER, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch(loaderID) {
            case BOOKMARK_LOADER:
                return new CursorLoader(
                        MainActivity.this,   // Parent activity context
                        Browser.BOOKMARKS_URI,        // Table to query
                        BOOKMARK_PROJECTION,         // Projection to return
                        null,            // No selection clause
                        null,            // No selection arguments
                        null             // Default sort order
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        int titleIndex = cursor.getColumnIndex(Browser.BookmarkColumns.TITLE);
        int urlIndex = cursor.getColumnIndex(Browser.BookmarkColumns.URL);
        int bookmarkIndex = cursor.getColumnIndex(Browser.BookmarkColumns.BOOKMARK);

        BookmarkHelper bookmarkHelper = new BookmarkHelper();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String title = cursor.getString(titleIndex);
            String URL = cursor.getString(urlIndex);
            boolean isBookmark = cursor.getInt(bookmarkIndex) == 1;

            if(isBookmark)
                bookmarkHelper.add(new Bookmark(title, URL));

            cursor.moveToNext();
        }

        shareBookmarkFile(bookmarkHelper.toHTML());

        Log.d("bookmark", bookmarkHelper.toHTML());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void shareBookmarkFile(String src) {
        new AsyncTask<String, Void, Uri>() {

            @Override
            protected Uri doInBackground(String... params) {
                String dirPath = Environment.getExternalStorageDirectory().getPath();
                DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String filePath = dirPath + File.separator + "bookmark_" + format.format(new Date()) + ".html";
                File file = new File(filePath);
                try {
                    file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    String src = params[0];
                    bos.write(src.getBytes());
                    bos.flush();
                    bos.close();

                    return Uri.fromFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Uri uriToFile) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToFile);
                shareIntent.setType("text/html");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            }
        }.execute(src);
    }
}

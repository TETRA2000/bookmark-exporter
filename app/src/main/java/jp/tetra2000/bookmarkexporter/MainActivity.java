package jp.tetra2000.bookmarkexporter;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Menu;
import android.view.MenuItem;


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

        while (!cursor.isAfterLast()) {
            String title = cursor.getColumnName(titleIndex);

            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
}

package movies.udacity.com.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import movies.udacity.com.popularmovies.network.MovieDetail;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by mangesh on 28/3/16.
 */
public class MovieDetailsHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "MOVIES";
    private static final int DATABASE_VERSION = 1;

    public MovieDetailsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static {
        cupboard().register(MovieDetail.class);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }
}

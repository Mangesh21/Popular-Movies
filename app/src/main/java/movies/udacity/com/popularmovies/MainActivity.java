package movies.udacity.com.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import movies.udacity.com.popularmovies.network.APICallBack;
import movies.udacity.com.popularmovies.network.APIError;
import movies.udacity.com.popularmovies.network.BaseClient;
import movies.udacity.com.popularmovies.network.MovieList;
import movies.udacity.com.popularmovies.uiutils.MovieDetailAdapter;
import movies.udacity.com.popularmovies.uiutils.SpacesItemDecoration;

public class MainActivity extends AppCompatActivity {

    BaseClient baseClient = null;

    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getMovieList();

        mRecyclerView = (RecyclerView) findViewById(R.id.moivies_grid);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


    }

    private void getMovieList() {
        baseClient = new BaseClient();
        baseClient.init();
        baseClient.getMoviesList(new APICallBack<MovieList>() {
            @Override
            public void success(MovieList movieList) {
                MovieDetailAdapter adapter = new MovieDetailAdapter(MainActivity.this, movieList);
                mRecyclerView.setAdapter(adapter);
                SpacesItemDecoration decoration = new SpacesItemDecoration(10, 2);
                mRecyclerView.addItemDecoration(decoration);
            }

            @Override
            public void error(APIError error) {
                Toast.makeText(MainActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
}
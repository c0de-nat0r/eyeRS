package com.github.eyers.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.github.eyers.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

/**
 * This class includes a navigation drawer and will display the main home activity of the app
 * once a user has successfully logged in.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Used to declare the search view bar.
     */
    private MaterialSearchView searchView;

    /**
     * Other declarations
     */
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * Create the ActionBarDrawerToggle
         */
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /**
             * Called when a drawer has settled in a completely closed state
             * @param view
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            /**
             * Called when a drawer has settled in a completely open state
             * @param drawerView
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawer.setDrawerListener(toggle);
        this.searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed()    {

            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()  {
            @Override
            public boolean onQueryTextSubmit(String query)  {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)    {
                return false; // functions of the database should be entere in here. Once the text changes in the search bar The items should appear.
            }
        });
    }

    /**
     * Sync the state of the ActionBarDrawerToggle with the state of the drawer
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    /**
     * Pass details of any configuration changes to the ActionBarDrawerToggle
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    /**
     * Called when we call invalidateOptionsMenu()
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        /**
         * If the drawer is open, hide related items to the content view
         */
        boolean drawerOpen = drawer.isDrawerOpen(navigationView);
        /**
         * Set the visibility of the menu items when the Drawer is opened or closed
         */
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen); //Hide the action settings when drawer is open
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    /**
     * Add items in the menu resource file to the action bar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * Inflate the menu; this adds items to the action bar if it is present.
         */
        getMenuInflater().inflate(R.menu.main, menu);

        /**
         * Creates search menu bar in the action bar
         */
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem item = menu.findItem(R.id.action_search);

        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * Handle action bar item clicks here. The action bar will
         * automatically handle clicks on the Home/Up button, so long
         * as you specify a parent activity in AndroidManifest.xml
         */

        /**
         * If the ActionBarDrawerToggle is clicked, let it handle what happens
         */
        if (item.getItemId() == R.id.action_settings) {
            super.startActivity(new Intent(this, AppSettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Method called when the nav_send button is clicked
     */
    public void sendIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Details of the catalog item to be sent will be attached here");
        String chooserTitle = getString(R.string.chooser); //Get the chooser title
        Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
        startActivity(chosenIntent); //Start the activity that the user selected
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_help:
                super.startActivity(new Intent(this, HelpActivity.class)); //starts the Help & Tips activity
                break;
            case R.id.nav_new_item:
                super.startActivity(new Intent(this, NewItemActivity.class)); //starts the New Item activity
                break;
            case R.id.nav_new_category:
                super.startActivity(new Intent(this, NewCategoryActivity.class)); //starts the New Category activity
                break;
            case R.id.nav_settings:
                super.startActivity(new Intent(this, AppSettingsActivity.class)); //starts the App Settings activity
                break;
            case R.id.nav_about:
                super.startActivity(new Intent(this, AboutActivity.class)); //starts the About activity
                break;
            case R.id.nav_slideshow:
                super.startActivity(new Intent(this, SlideshowActivity.class)); //starts the Slideshow activity
                break;
            case R.id.nav_share:
            case R.id.nav_trade:
                Toast.makeText(this, "TODO", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_exit:
                exit();
                break;
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//        }
        Toast.makeText(this, "TODO Button: " + v.getId(), Toast.LENGTH_LONG).show();
    }

    /**
     * A callback method invoked by the loader when initLoader() is called
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    /**
     * A callback method, invoked after the requested content provider returns all the data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

} //end class MainActivity

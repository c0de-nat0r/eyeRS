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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.eyers.EyeRS;
import com.github.eyers.ItemLabel;
import com.github.eyers.LabelAdapter;
import com.github.eyers.R;
import com.github.eyers.activities.todo.TradeActivity;
import com.github.eyers.wrapper.ItemWrapper;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class includes a navigation drawer and will display the main home activity of the app
 * once a user has successfully logged in.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, OnItemClickListener {

    /**
     * Fields & other declarations
     */
    public static String STATE = "main";
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ListView listView;

    /**
     * Used to declare the search view bar.
     */
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
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

        final ArrayList<ItemLabel> items = new ArrayList<>();
        /**
         * Populate the list view
         */
        try {
            listView = (ListView) findViewById(R.id.main_listView);

            if (STATE.equals("main")) {
                for (ItemLabel category : EyeRS.getCategoriesList(this)) {
                    items.add(category);
                }
            } else {
                for (ItemWrapper item : EyeRS.getItems(STATE, this)) {
                    items.add(new ItemLabel(item.getName(), item.getImage(), item.getDescription()));
                }
            }

            LabelAdapter adapter = new LabelAdapter(this, items);
            listView.setAdapter(adapter);

        } catch (Exception ex) {

            Toast.makeText(this, "Unable to view items", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity list view", ex.getMessage(), ex);
        }

        listView.setOnItemClickListener(this);
        getIntent().setAction("Already created");

        this.searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setOnClickListener(this);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null & !newText.isEmpty()) {
                    List<String> lstFound = new ArrayList<String>();
                    for (ItemLabel item : items) {
                        if (item.getName().toLowerCase().contains(newText.toLowerCase())) {
                            lstFound.add(item.getName());
                        }
                        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, lstFound);
                        listView.setAdapter(adapter);
                    }
                }
                return true;
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
     * Called when we call invalidateOptionsMenu().
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            /**
             * If the drawer is open, hide related items to the content view
             */
            boolean drawerOpen = drawer.isDrawerOpen(navigationView);
            /**
             * Set the visibility of the menu items when the Drawer is opened or closed
             */
            menu.findItem(R.id.action_settings).setVisible(!drawerOpen); //Hide the action settings when drawer is open
        } catch (Exception ex) {
            Log.e("Navigation Drawer", ex.getMessage(), ex);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        MainActivity.STATE = "main";
        super.startActivity(new Intent(this, MainActivity.class));
        super.finish();
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

            try {
                super.startActivity(new Intent(this, AppSettingsActivity.class));
                return true;

            } catch (Exception ex) {
                Log.e("Action settings", ex.getMessage(), ex);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        try {
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
                case R.id.nav_share: {
                    startActivity(new Intent(this, ShareActivity.class));
                }
                break;
                case R.id.nav_trade: {
                    startActivity(new Intent(this, TradeActivity.class));
                }
                break;
                case R.id.nav_exit:
                    exit();
                    break;
            }

        } catch (Exception ex) {
            Log.e("Navigation drawer", ex.getMessage(), ex);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Closes the app.
     */
    private void exit() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception ex) {
            Log.e("Exit feature", ex.getMessage(), ex);
        }
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
//        Toast.makeText(this, "TODO Button: " + v.getId(), Toast.LENGTH_LONG).show();
    }

    /**
     * A callback method invoked by the loader when initLoader() is called.
     *
     * @return null
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

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (STATE.equals("main")) {
                STATE = listView.getItemAtPosition(position).toString(); // Retrieves the selected category
                startActivity(new Intent(this, MainActivity.class));
            } else {
                ItemLabel lblItem = (ItemLabel) listView.getItemAtPosition(position);
                ViewItemActivity.ITEM = new ItemWrapper(lblItem.getName(), lblItem.getImage(), lblItem.getDescription());

                STATE = listView.getItemAtPosition(position).toString(); // Retrieves the selected item
                startActivity(new Intent(this, ViewItemActivity.class));
            }
        } catch (ClassCastException cce) {
            String itemName = listView.getItemAtPosition(position).toString();
            for (ItemLabel lbl : EyeRS.getCategoriesList(this)) {
                String category = lbl.getName();
                for (ItemWrapper item : EyeRS.getItems(category, this)) {
                    if (itemName.equals(itemName)) {
                        ViewItemActivity.ITEM = item;
                        startActivity(new Intent(this, ViewItemActivity.class));
                        finish();
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(this, STATE + " not found.", Toast.LENGTH_LONG).show();
            Log.e("", "", ex);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ECLAIR
                    && keyCode == KeyEvent.KEYCODE_BACK
                    && event.getRepeatCount() == 0) {
                // Take care of calling this method on earlier versions of
                // the platform where it doesn't exist.
                onBackPressed();
            }

        } catch (Exception ex) {
            Log.e("MainActivity key_down", ex.getMessage(), ex);
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        if (action == null || !action.equals("Already created")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            getIntent().setAction(null);
        }
        super.onResume();
    }
} //end class MainActivity

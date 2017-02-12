package com.devhack.taskglide.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.devhack.taskglide.R;
import com.devhack.taskglide.dialogs.ImagePreviewDialog;
import com.devhack.taskglide.pubnub.PubNubUtils;
import com.devhack.taskglide.ui.TaskGlidePagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout) DrawerLayout activityDrawer;
    @BindView(R.id.nav_view) NavigationView activityNavigationView;
    @BindView(R.id.activity_main_tabs) TabLayout activityTabLayout;
    @BindView(R.id.toolbar) Toolbar activityToolbar;
    @BindView(R.id.activity_main_viewpager) ViewPager activityViewPager;

    /** ACTIVITY LIFECYCLE METHODS _____________________________________________________________ **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        PubNubUtils.initPubNub(this); // Initializes PubNub.
    }

    /** ACTIVITY OVERRIDE METHODS ______________________________________________________________ **/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tasks) {
            setCurrentPage(0);
        } else if (id == R.id.nav_messages) {
            setCurrentPage(1);
        } else if (id == R.id.nav_files) {
            setCurrentPage(2);
        } else if (id == R.id.nav_account) {

        } else if (id == R.id.nav_logout) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /** LAYOUT METHODS _________________________________________________________________________ **/

    private void initView() {
        initToolbar();
        initDrawer();
        initViewPager();
        initTabs();
    }

    private void initToolbar() {
        activityToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.toolbarColor));
        setSupportActionBar(activityToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, activityDrawer, activityToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        activityDrawer.setDrawerListener(toggle);
        toggle.syncState();

        activityNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initTabs() {
        activityTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(LOG_TAG, "onTabSelected(): " + tab.getPosition());
                setCurrentPage(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void initViewPager() {
        PagerAdapter bossPagerAdapter = new TaskGlidePagerAdapter(getSupportFragmentManager(), this);
        activityViewPager.setAdapter(bossPagerAdapter);

        ViewPager.SimpleOnPageChangeListener pageListener = new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO: Do something.
            }
        };
        activityViewPager.addOnPageChangeListener(pageListener);
        activityTabLayout.setupWithViewPager(activityViewPager);
    }

    private void setCurrentPage(int page) {
        activityViewPager.setCurrentItem(page, false);
    }

    /** DIALOG METHODS _________________________________________________________________________ **/

    public void displayImagePreview(int imageResource) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ImagePreviewDialog imagePreviewDialog = ImagePreviewDialog.newInstance(imageResource);
        imagePreviewDialog.show(fragmentManager, ImagePreviewDialog.class.getSimpleName());
    }

    public void displayBottomDialog() {

    }
}

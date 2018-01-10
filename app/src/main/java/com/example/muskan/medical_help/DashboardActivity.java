package com.example.muskan.medical_help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Toolbar toolbar;
    Drawer result;
    public static ActionBarDrawerToggle toggle;
    public static AppCompatActivity activity;
    public static FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        initToolbar();
        mAuth = FirebaseAuth.getInstance();
        activity = DashboardActivity.this;
        manager = getSupportFragmentManager();

        /*GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }*/
        FirebaseUser user = mAuth.getCurrentUser();

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav_menu_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(user.getPhotoUrl())
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.NavHome).withIcon(R.drawable.ic_home_black_24dp),
                        new PrimaryDrawerItem().withName(R.string.NavEmergency).withIcon(R.drawable.ic_local_hospital_black_24dp),
                        new PrimaryDrawerItem().withName(R.string.NavSettings).withIcon(R.drawable.ic_settings_black_24dp),
                        new SecondaryDrawerItem().withName(R.string.NavAboutUs),
                        new SecondaryDrawerItem().withName(R.string.NavLogout)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Log.v("Position", "" + position);

                        switch (position) {
                            case 1:

                                break;
                            case 2:
                                Intent EmergencyIntent = new Intent(DashboardActivity.this, EmergencyActivity.class);
                                startActivity(EmergencyIntent);
                                break;
                            case 3:
                                Intent SettingsIntent = new Intent(DashboardActivity.this, SettingsActivity.class);
                                startActivity(SettingsIntent);
                                break;
                            case 4:
                                Intent AboutUsIntent = new Intent(DashboardActivity.this, AboutUsActivity.class);
                                startActivity(AboutUsIntent);
                                break;
                            case 5:
                                Intent LogoutIntent = new Intent(DashboardActivity.this, LogoutActivity.class);
                                startActivity(LogoutIntent);
                                break;
                        }

                        result.getDrawerLayout().closeDrawers();
                        return false;

                    }
                }).build();

        toggle = new ActionBarDrawerToggle(this, result.getDrawerLayout(), R.string.NavigationDrawerOpen, R.string.NavigationDrawerClose);
        DashboardActivity.activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DashboardActivity.toggle.syncState();

        LinearLayout addmedicine_layout = (LinearLayout) findViewById(R.id.addMedicine);
        addmedicine_layout.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View view) {
                Intent addMedicineIntent = new Intent(DashboardActivity.this, AddMedicineActivity.class);
                startActivity(addMedicineIntent);

            }
        });

        LinearLayout mymedicine_layout = (LinearLayout) findViewById(R.id.myMedicine);
        mymedicine_layout.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Intent myMedicineIntent = new Intent(DashboardActivity.this, MyMedicineActivity.class);
                startActivity(myMedicineIntent);
            }
        });

        LinearLayout reminders_layout = (LinearLayout) findViewById(R.id.reminder);
        reminders_layout.setOnClickListener(new View.OnClickListener()

        {

            @Override
            public void onClick(View view) {
                Intent reminderIntent = new Intent(DashboardActivity.this, RemindersMainActivity.class);
                startActivity(reminderIntent);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_logout) {
            Intent logoutIntent = new Intent(DashboardActivity.this, LogoutActivity.class);
            startActivity(logoutIntent);
            finish();
        }

        if (id == android.R.id.home) {
            result.getDrawerLayout().openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Intent startIntent = new Intent(DashboardActivity.this, MainActivity.class);
            startActivity(startIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar_dashboard);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
}

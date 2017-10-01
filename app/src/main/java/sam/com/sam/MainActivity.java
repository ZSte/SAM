package sam.com.sam;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static sam.com.sam.R.id.map;
import static sam.com.sam.R.id.start;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    int RC_SIGN_IN = 1;
    private ChildEventListener childEventListener;

    private TextView textViewName;
    private TextView textViewEMail;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    //User is signed in
                    firebaseAddChildEventListener();
                }
                else {
                    //User is not signed in
                    startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        textViewName = (TextView) headerView.findViewById(R.id.textView_name);
        textViewEMail = (TextView) headerView.findViewById(R.id.textView_eMail);

        /*if the App is restarted but a user is still logged in his name will be reset in the
        navigation drawer*/
        if (firebaseAuth.getCurrentUser() != null) {
            textViewName.setText(firebaseAuth.getCurrentUser().getDisplayName());
            textViewEMail.setText(firebaseAuth.getCurrentUser().getEmail());
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //User is succesfully signed in
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                User u = new User(firebaseUser.getDisplayName(), -1, firebaseUser.getEmail(), null, null, 10, 10);
                databaseReference.child(firebaseUser.getUid()).setValue(u);

                textViewName.setText(firebaseAuth.getCurrentUser().getDisplayName());
                textViewEMail.setText(firebaseAuth.getCurrentUser().getEmail());
            } else if (resultCode == 0) {
                //User was not signed in

            }
        }

    }

    public void signOut() {
        AuthUI.getInstance().signOut(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
        if(childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.log_out) {
            signOut();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            sendEMail();
        } else if (id == R.id.item_lang_spoken) {
            Intent i = new Intent(this, SetSpokenActivity.class/*STest.classSetLanguagesActivity.class*/);
            startActivity(i);
        } else if (id == R.id.item_lang_learn) {
            Intent i = new Intent(this, SetLearnActivity.class);
            startActivity(i);
        } else if (id == R.id.place_pick) {
            chooseLocation();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClick(View view) {
        /*if(view.getId() == R.id.signout) {
            Log.e("CLICKED", "aaaaaaaaaaaaaa");
            signOut();
        }
        else {

        }*/
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;

        /*map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));*/
        firebaseAddChildEventListener();

        googleMap.setOnInfoWindowClickListener(this);
    }

    private void sendEMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","abc@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    public void chooseLocation() {
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void firebaseAddChildEventListener() {
        if(childEventListener == null) {
            final List<User> userList = new ArrayList<>();

            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                    //userList.add(dataSnapshot.getValue(User.class));
                    Log.e("AAAA1", user==null?"NULL": "AAAA");
                    Log.e("AAAA", user.getLocation()==null?"NULL": "AAAA");

                    Log.e("GOOGLE", googleMap == null?"NULL":"AAAA");

                    //LatLng location = dataSnapshot.child(/*"users/" + */firebaseAuth.getCurrentUser().getUid() + "/location").getValue(LatLng.class);
                    MapManager.addUserMarker(googleMap, user);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();

    }
}

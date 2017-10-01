package sam.com.sam;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;

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
    int PLACE_PICKER_REQUEST = 2;
    private ChildEventListener childEventListener;

    private TextView textViewName;
    private TextView textViewEMail;

    private GoogleMap googleMap;

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        users = new ArrayList<>();

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

        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    for (User user : users
                            ) {
                        MapManager.addUserMarker(googleMap, user);
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //User is succesfully signed in
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                User u = new User(firebaseUser.getDisplayName(), -1, firebaseUser.getEmail(), null, null, 0.0/*null*/, 0.0/*null*/);
                databaseReference.child(firebaseUser.getUid()).setValue(u);

                if (!users.contains(u)) {
                    users.add(u);
                }

                textViewName.setText(firebaseAuth.getCurrentUser().getDisplayName());
                textViewEMail.setText(firebaseAuth.getCurrentUser().getEmail());
            } else if (resultCode == 0) {
                //User was not signed in

            }
        }
        else if(requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                LatLng location = place.getLatLng();
                Log.e("LAT", location.latitude + "");
                //databaseReference.child(firebaseAuth.getCurrentUser().getUid() + "/location/latitude").setValue(location.latitude);
                //databaseReference.child(firebaseAuth.getCurrentUser().getUid() + "/location/longitude").setValue(location.longitude);
                databaseReference.child(firebaseAuth.getCurrentUser().getUid() + "/location").setValue(location);
                //firebaseAddChildEventListener();
                //MapManager.addUserMarker(googleMap, new User(firebaseAuth.getCurrentUser().getDisplayName(), -1, firebaseAuth.getCurrentUser().getEmail(), null, null, /*firebaseAuth.getCurrentUser().getUid(),*/ location.longitude, location.latitude));
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
        if(googleMap != null) {
            //googleMap.clear();
            firebaseAddChildEventListener();
        }
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
                "mailto","", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    private void sendEMailTo(String email) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", email, null));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body));
        startActivity(Intent.createChooser(intent, "Send email..."));
    }

    public void chooseLocation() {
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

            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);
                    //userList.add(user);
                    users.add(user);
                    //userList.add(dataSnapshot.getValue(User.class));
                    Log.e("AAAA1", user==null?"NULL": "AAAA");
                    //Log.e("AAAA", user.getLocation()==null?"NULL": "AAAA");
                    //Log.e("LIST", userList.size() + "");
                    MapManager.addUserMarker(googleMap, user);

                    Log.e("GOOGLE", googleMap == null?"NULL":"AAAA");

                    //LatLng location = dataSnapshot.child(/*"users/" + */firebaseAuth.getCurrentUser().getUid() + "/location").getValue(LatLng.class);

                    //MapManager.addUserMarker(googleMap, user);
                    /*map*//*googleMap.addMarker(new MarkerOptions()
                            .position(user.getLocation())
                            .snippet(user.geteMailAdr())
                            .title(user.getUsername() + " -- Skill Level: " + user.getSkillLvl()));
                    */
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Log.e("CHANGED", "");
                    User user = dataSnapshot.getValue(User.class);

                    users.remove(user);
                    users.add(user);

                    //userList.add(dataSnapshot.getValue(User.class));
                    Log.e("AAAA1", user==null?"NULL": "AAAA");
                    //Log.e("AAAA", user.getLocation()==null?"NULL": "AAAA");
                    MapManager.addUserMarker(googleMap, user);
                    //Log.e("LIST", userList.size() + "");

                    Log.e("GOOGLE", googleMap == null?"NULL":"AAAA");

                    //LatLng location = dataSnapshot.child(/*"users/" + */firebaseAuth.getCurrentUser().getUid() + "/location").getValue(LatLng.class);
                    //MapManager.addUserMarker(googleMap, user);
                    //onMapReady(googleMap);
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
            MapManager.addUserMarker(googleMap, users);
             Log.e("LIST1", users.size() + "");
        }
    }


    @Override
    public void onInfoWindowClick(final Marker marker) {
        /*Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();*/

        Log.e("do", "it");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact");
        builder.setPositiveButton("Send Mail", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sendEMailTo(marker.getSnippet());
            }
        });
        builder.show();
    }
}

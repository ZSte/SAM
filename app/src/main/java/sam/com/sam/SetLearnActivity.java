package sam.com.sam;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class SetLearnActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener mChildEventListener;
    private List<String> learnLanguages;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_learn);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        firebaseAuth = FirebaseAuth.getInstance();

        ListView listView = (ListView) findViewById(R.id.learn_list_view);
        final String[] languages = getResources().getStringArray(R.array.languages);
        List<String> list = Arrays.asList(languages);

        learnLanguages = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        attachDatabaseReadListener();

        //Log.e("LIST", learnLanguages.toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        switch(i) {
            case 0:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("english");

                break;
            case 1:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("german");
                break;
            case 2:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("french");
                break;
            case 3:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("chinese");
                break;
            case 4:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("hindi");
                break;
            case 5:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("arabic");
                break;
            case 6:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("russian");
                break;
            case 7:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("portuguese");
                break;
            case 8:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("japanese");
                break;
            case 9:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("korean");
                break;
            /*case 10:
                databaseReference.child(firebaseUser.getUid() + "/learn/" + Integer.toString(i)).setValue("hindi");
                break;*/
        }
        //set color for picked languages
        view.setBackgroundColor(Color.RED);
    }

    private void attachDatabaseReadListener() {
        if(mChildEventListener == null) {
            List<String> languages/* = new ArrayList<>()*/;
            EventListener e = new EventListener();
            mChildEventListener = e;/*new ChildEventListener()*/
            learnLanguages = e.getLearnLanguages();
        }

    }

    public class EventListener implements ChildEventListener {

        List<String> languages1;

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Log.e("LIST", ".");
            languages1 = new ArrayList<>();
            languages1 = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid() + "/learn").getValue(List.class);
            Log.e("LIST", languages1.toString());
            //languages = new ArrayList<>(languages1);
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

        public List<String> getLearnLanguages() {
            return languages1;
        }
    }


}

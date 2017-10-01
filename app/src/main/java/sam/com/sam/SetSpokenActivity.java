package sam.com.sam;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetSpokenActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_spoken);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        firebaseAuth = FirebaseAuth.getInstance();

        ListView listView = (ListView) findViewById(R.id.spoken_list_view);
        final String[] languages = getResources().getStringArray(R.array.languages);

        Log.e("AAAA", languages[0]);

        List<String> list = Arrays.asList(languages);
        /*for(int i = 0; i < list.size(); i++) {
            list.add(languages[i]);
        }*/
        Log.e("AAAA", list.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        switch(i) {
            case 0:
                databaseReference.child(firebaseUser.getUid() + "/spoken/" + Integer.toString(i)).setValue("english");
                break;
            case 1:
                databaseReference.child(firebaseUser.getUid() + "/spoken/" + Integer.toString(i)).setValue("german");
                break;
            case 2:
                databaseReference.child(firebaseUser.getUid() + "/spoken/" + Integer.toString(i)).setValue("french");
                break;
            case 3:
                databaseReference.child(firebaseUser.getUid() + "/spoken/" + Integer.toString(i)).setValue("chinese");
                break;
            case 4:
                databaseReference.child(firebaseUser.getUid() + "/spoken/" + Integer.toString(i)).setValue("hindi");
                break;
        }
        view.setBackgroundColor(Color.RED);
    }
}

package sam.com.sam;

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

public class SetLearnActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
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
        }
    }


}

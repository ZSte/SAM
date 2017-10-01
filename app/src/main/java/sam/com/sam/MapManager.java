package sam.com.sam;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Iterator;
import java.util.List;

/**
 * Created by TheBigE on 01.10.2017.
 */

public class MapManager {

    private MapManager() {}


    public static void addUserMarker(GoogleMap map, User user) {
        if(user.getLat() == null || user.getLng() == null) {
        //    Log.e("NULL", "1");
            return;
        }
        Log.e("NOTNULL", "");
        //aaaa
        map.addMarker(new MarkerOptions()
                .snippet("Skill-Level: " + user.getSkillLvl())
                .position(user.getLocation())
                .title(user.getUsername()));
    }


    public static void addUserMarker(GoogleMap map, List<User> users) {
        Iterator<User> iterator = users.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            addUserMarker(map, user);
        }
    }


    /*public void addHomeMarker(Context context, GoogleMap map, User me) {
        map.addMarker(new MarkerOptions()
                .position(me.getLocation())
                .title("Home")
                .icon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_home)));
    }*/
}

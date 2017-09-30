package sam.com.sam;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Nicole on 30.09.2017.
 */

public class Filter {

    List<User> findPeopleWhoSpeak(String language, List<User> userList) {
        List<User> peopleWhoSpeak = new LinkedList<User>();
        for (User u: userList) {
            if (u.getSpokenLanguages().contains(language)) {
                peopleWhoSpeak.add(u);
            }
        }
        return peopleWhoSpeak;
    }

    List<User> findPeopleWhoLearn(String language, List<User> userList) {
        List<User> peopleWhoLearn = new LinkedList<>();
        for (User u: userList) {
            if (u.getLearningLanguages().contains(language)) {
                peopleWhoLearn.add(u);
            }
        }
        return peopleWhoLearn;
    }

}

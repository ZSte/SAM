package sam.com.sam;

/**
 * Created by TheBigE on 30.09.2017.
 */

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * This class represents a user of the App.
 * Created by Alexander Weidemeier
 */
public class User {
    private String username;
    private int skillLvl;
    //TODO: Location variable for a user. To be used as his default home location.
    //TODO: Variable for E-Mail Adress of the User for "first contact".
    private String eMailAdr;

    private List<String> spokenLanguages;
    private List<String> learningLanguages;

    private LatLng location;


    /**
     * Constructor of the User Class
     */
    public User(String name, int skill, String eMail, List<String> spokenLanguages, List<String> learningLanguages) {
        this.username = name;
        this.skillLvl = skill;
        this.eMailAdr = eMail;
        this.spokenLanguages = spokenLanguages;
        this.learningLanguages = learningLanguages;
    }

    public String getUsername() {
        return username;
    }

    public int getSkillLvl() {
        return skillLvl;
    }

    public List<String> getSpokenLanguages() {
        return spokenLanguages;
    }

    public List<String> getLearningLanguages() {
        return learningLanguages;
    }

    public String geteMailAdr() {
        return eMailAdr;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void setSpokenLanguages(List<String> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public void setLearningLanguages(List<String> learningLanguages) {
        this.learningLanguages = learningLanguages;
    }
}

//Just a simple line

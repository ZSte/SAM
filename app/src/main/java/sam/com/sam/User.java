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

    //private LatLng location;
    private Double lat;
    private Double lng;


    /**
     * Constructor of the User Class
     */
    public User(String name, int skill, String eMail, List<String> spokenLanguages, List<String> learningLanguages, Double lat, Double lng) {
        this.username = name;
        this.skillLvl = skill;
        this.eMailAdr = eMail;
        this.spokenLanguages = spokenLanguages;
        this.learningLanguages = learningLanguages;
        this.lat = lat;
        this.lng = lng;
    }

    public User() {
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
        return new LatLng(lat, lng);
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
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

    @Override
    public boolean equals(Object other) {
        if (other instanceof User) {
            return ((User) other).geteMailAdr().equals(this.geteMailAdr());
        }
        return false;
    }
}

//Just a simple line

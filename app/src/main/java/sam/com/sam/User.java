package sam.com.sam;

/**
 * Created by TheBigE on 30.09.2017.
 */

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


    /**
     * Constructor of the User Class
     */
    public User(String name, int skill, String eMail) {
        this.username = name;
        this.skillLvl = skill;
        this.eMailAdr = eMail;
    }

    public String getUsername() {
        return username;
    }

    public int getSkillLvl() {
        return skillLvl;
    }

    public String geteMailAdr() {
        return eMailAdr;
    }

    public void setUsername(String name) {
        this.username = name;
    }
}

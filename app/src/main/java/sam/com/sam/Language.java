package sam.com.sam;

/**
 * Created by Steffi on 30.09.2017.
 */

public class Language {
    //boolean chosen;
    String language;

    public Language(/*boolean chosen, */String language) {
        //this.chosen = chosen;
        this.language = language;
    }

    /*public boolean isChosen() {
        return chosen;
    }*/

    public String getLanguage() {
        return language;
    }

    /*public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }*/

    public void setLanguage(String language) {
        this.language = language;
    }
}

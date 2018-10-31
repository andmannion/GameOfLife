package ie.ucd.engac.messaging;

import java.util.ArrayList;

public class ChooseableString implements Chooseable {
    private String string;

    public ChooseableString(String string){
        this.string = string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public String displayChoiceDetails() {
        return string;
    }

    // TODO: move this to a more appropriate place
    public static ArrayList<Chooseable> convertToChooseableArray(ArrayList<String> stringArray){
        ArrayList<Chooseable> chooseableArray = new ArrayList<>();

        for(String str : stringArray) {
            chooseableArray.add(new ChooseableString(str));
        }

        return chooseableArray;
    }
}

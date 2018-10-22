package ie.ucd.engac.messaging;

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
}

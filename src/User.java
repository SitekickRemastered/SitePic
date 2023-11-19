import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class User {
    private final String Name;
    private String location, eyePic;
    private Paint bodyColour, customEyeColour;
    private final ArrayList<Paint> paintList, sectionList;
    private boolean multiColourGradient, isDeadkick;
    private int rotateAmount, zoomAmount, flipType;
    private int[] translateAmount;
    private final HashMap<String, Message> botMessages;
    private Message helpMessage;

    public User(String Name) {
        this.Name = Name;
        location = "";
        bodyColour = new Color(255, 204, 0);
        multiColourGradient = false;
        eyePic = "PurpleEyes.png";
        customEyeColour = new Color(0, 0, 0);
        isDeadkick = false;
        paintList = new ArrayList<>();
        sectionList = new ArrayList<>();
        rotateAmount = 0;
        zoomAmount = 0;
        translateAmount = new int[]{ 0, 0 };
        flipType = 0;
        botMessages = new HashMap<>();
        helpMessage = null;
    }

    public String getName() {return Name;}

    public Paint getBodyColour() {return bodyColour;}

    public void setBodyColour(Paint newColour) {bodyColour = newColour;}

    public ArrayList<Paint> getPL() {return paintList;}

    public void addToPL(Paint newColour) {paintList.add(newColour);}

    public void clearPL() {paintList.clear();}

    public ArrayList<Paint> getSL() {return sectionList;}

    public void clearSL() {sectionList.clear();}

    public void addToSL(Paint newColour) {sectionList.add(newColour);}

    public boolean getMCG() {return multiColourGradient;}

    public void setMCG(boolean newMCG) {multiColourGradient = newMCG;}

    public String getEyePic() {return eyePic;}

    public void setEyePic(String newEyePic) {eyePic = newEyePic;}

    public Paint getCustomEyeColour() {return customEyeColour;}

    public void setCustomEyeColour(Paint customEyeColour) {
        this.setEyePic("CustomEyes.png");
        this.customEyeColour = customEyeColour;
    }

    public boolean getDeadkick()  { return isDeadkick; }
    public void setDeadkick(boolean deadkick) { this.isDeadkick = deadkick; }

    public int getRA() {
        return rotateAmount;
    }

    public void setRA(int newRA) {
        rotateAmount = newRA;
    }

    public int getZA() {return zoomAmount;}

    public void setZA(int newZA) {
        zoomAmount = newZA;
    }

    public int[] getTA() {
        return translateAmount;
    }

    public void setTA(int x, int y) {
        translateAmount = new int[]{ x, y };
    }

    public void setFlipType(int direction) {flipType = direction;}

    public int getFlipType() {return flipType;}

    public void resetDefault() {
        bodyColour = new Color(255, 204, 0);
        eyePic = "PurpleEyes.png";
        customEyeColour = new Color(0, 0, 0);
        clearPL();
        clearSL();
        rotateAmount = 0;
        zoomAmount = 0;
        translateAmount = new int[]{ 0, 0 };
        flipType = 0;
    }

    public String getLocation() {return location;}

    public void setLocation(String newLocation) {location = newLocation;}

    public HashMap<String, Message> getBotMessages() {return botMessages;}

    public boolean messageExists(Message m) {return botMessages.containsKey(m.getId());}

    public void addBotMessage(Message m) {botMessages.put(m.getId(), m);}

    public void clearBotMessages() {botMessages.clear();}

    public Message getHelpMessage() {return helpMessage;}

    public void setHelpMessage(Message m) {helpMessage = m;}

    public void clearHelpMessage() {helpMessage = null;}

}

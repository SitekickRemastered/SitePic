import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class V {
    public static UserController uc = new UserController();
    public static User currentUser;
    public static Hashtable<String, Message> lastEmbed = new Hashtable<>();
    public static float imageSize = 512.0f;
    public static String[] bodyReactions = {
        "default:768644817378476044", "ytvgreen:768644817881923635", "ytvpurple:768644818208555028",
        "white:768644817907220501", "silver:768644817642586112", "black:768644817617551410",
        "red:768644817604968498", "maroon:768644817700913162", "orange:769023013143838755",
        "yellow:768644818259148820", "olive:768644817587535902", "lime:768644817281744917",
        "green:768644817248059433", "aqua:768644817562632192", "blurple:768644817394728982",
        "teal:768644817671553024", "blue:768644817579540500", "navy:768644817630527558",
        "fuchsia:768644817848238132", "purple:771441164090867752"
    };

    public static Color[] colourArray = {
        new Color(255, 204, 0),
        new Color(104,194,8),
        new Color(74,42,169),
        new Color(255,255,255),
        new Color(192,192,192),
        new Color(50,50,50),
        new Color(255,0,0),
        new Color(128,0,0),
        new Color(254,94,0),
        new Color(255,255,0),
        new Color(128,128,0),
        new Color(0,255,0),
        new Color(0,128,0),
        new Color(0,255,255),
        new Color(144,137,218),
        new Color(0,128,128),
        new Color(0,0,255),
        new Color(0,0,128),
        new Color(255,0,255),
        new Color(128,0,128)
    };

    public static List<LayoutComponent> mainMenuOptions = new ArrayList<>(List.of(
        ActionRow.of(
            createButton("Body:768627497423339542", "Body Menu"),
            createButton("PurpleEyes:768624726275784704", "Eyes Menu")
        )
    ));

    public static String[] traversalMenuStrings = {"Back", "Main Menu", "Body Menu", "Eyes Menu", "Help"};
    public static List<LayoutComponent> bodyMenuOptions = new ArrayList<>(Arrays.asList(
        ActionRow.of(
            createButton("default:768644817378476044", "Default"),
            createButton("ytvgreen:768644817881923635", "YTV Green"),
            createButton("ytvpurple:768644818208555028", "YTV Purple"),
            createButton("white:768644817907220501", "White"),
            createButton("silver:768644817642586112", "Silver")
        ),
        ActionRow.of(
            createButton("black:768644817617551410", "Black"),
            createButton("red:768644817604968498", "Red"),
            createButton("maroon:768644817700913162", "Maroon"),
            createButton("orange:769023013143838755", "Orange"),
            createButton("yellow:768644818259148820", "Yellow")
        ),
        ActionRow.of(
            createButton("olive:768644817587535902", "Olive"),
            createButton("lime:768644817281744917", "Lime"),
            createButton("green:768644817248059433", "Green"),
            createButton("aqua:768644817562632192", "Aqua"),
            createButton("blurple:768644817394728982", "Blurple")
        ),
        ActionRow.of(
            createButton("teal:768644817671553024", "Teal"),
            createButton("blue:768644817579540500", "Blue"),
            createButton("navy:768644817630527558", "Navy"),
            createButton("fuchsia:768644817848238132", "Fuchsia"),
            createButton("purple:771441164090867752", "Purple")
        )
    ));
    public static List<LayoutComponent> eyeMenuOptions = new ArrayList<>(Arrays.asList(
        ActionRow.of(
            createButton("PurpleEyes:768624726275784704", "Purple Eyes"),
            createButton("BlackEyes:768567817476767806", "Black Eyes"),
            createButton("WhiteEyes:768567817913499689", "White Eyes"),
            createButton("OrangeEyes:768567817820700763", "Orange Eyes"),
            createButton("GoldenEyes:768567817841934356", "Golden Eyes")
        ),
        ActionRow.of(
            createButton("ForestEyes:768567817590013953", "Forest Eyes"),
            createButton("GreenEyes:768567817620422758", "Green Eyes"),
            createButton("AquaEyes:768567817615573022", "Aqua Eyes"),
            createButton("BabyBlues:768567817422372895", "Baby Blues"),
            createButton("BlueEyes:768567817821618236", "Blue Eyes")
        ),
        ActionRow.of(
            createButton("VioletEyes:768567818219159573", "Forest Eyes"),
            createButton("PinkEyes:768567818253107290", "Pink Eyes"),
            createButton("RedEyes:768567818248650752", "Red Eyes"),
            createButton("CrimsonEyes:768567817694871623", "Crimson Eyes"),
            createButton("GreedyEyes:768567817976676432", "Greedy Eyes")
        ),
        ActionRow.of(
            createButton("KissyEyes:768567818249437244", "Kissy Eyes"),
            createButton("HypnoEyes:768567818245242880", "Hypno Eyes"),
            createButton("DinoEyes:768567817816506398", "Dino Eyes"),
            createButton("HollowEyes:768567818211164200", "Hollow Blues"),
            createButton("XEyes:768567662065221662", "X-Eyes")
        )
    ));

    public static List<LayoutComponent> helpTraversalOptions = new ArrayList<>(List.of(
        ActionRow.of(
            createButton("sipik:1036834657850900530", "Main Menu"),
            createButton("Body:768627497423339542", "Body Menu"),
            createButton("PurpleEyes:768624726275784704", "Eyes Menu")
        )
    ));
    public static Button createButton(String id, String label) {
        return Button.primary(id, label).withEmoji(Emoji.fromFormatted("<:" + id + ">"));
    }
}

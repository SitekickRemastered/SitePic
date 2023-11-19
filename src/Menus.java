import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class Menus {

    /**
     * Builds and sends the main menu. Allows users to traverse to the body and eyes menu
     *
     * @param e - The interaction event that caused the user to enter the main menu.
     */
    public static void menu(GenericInteractionCreateEvent e) {
        V.currentUser.setLocation("menu");
        e.getMessageChannel().sendFiles(FileUpload.fromData(new File("logo.png"), "Logo.png")).queue();
        e.getMessageChannel().sendMessage("Welcome to the Profile Picture Builder!\n\nClick an action below to choose the corresponding option!").addComponents(V.mainMenuOptions).queue();
    }

    /**
     * Builds and sends the body menu.
     *
     * @param e - The Button interaction event that caused the user to enter the body menu.
     */
    public static void body(ButtonInteractionEvent e) throws Exception {
        V.currentUser.setLocation("body");
        String message = "Select a colour below to change your avatar color.";
        SendPFP.sendImage(e, message, V.bodyMenuOptions);
    }

    /**
     * Builds and sends the eyes menu.
     *
     * @param e - The Button interaction event that caused the user to enter the eyes menu.
     */
    public static void eyes(ButtonInteractionEvent e) throws Exception {
        V.currentUser.setLocation("eyes");
        String message = "Select an eye-moji below to change your Sitekick's eyes!";
        SendPFP.sendImage(e, message, V.eyeMenuOptions);
    }

    /**
     * Builds and sends the help menu while also setting the current user's help message.
     *
     * @param e - A Message event listener. If the user types anything and the help message doesn't exist, this function will execute.
     */
    public static void help(MessageReceivedEvent e) throws ExecutionException, InterruptedException {
        V.currentUser.setHelpMessage(e.getChannel().sendMessageEmbeds(createHelpMessage().build()).addComponents(V.helpTraversalOptions).submit().get());
    }

    /**
     * Builds and sends the help menu while also setting the current user's help message.
     *
     * @param e - The Interaction event that sent us to this menu.
     */
    public static void help(GenericInteractionCreateEvent e) throws ExecutionException, InterruptedException {
        V.currentUser.setHelpMessage(e.getMessageChannel().sendMessageEmbeds(createHelpMessage().build()).addComponents(V.helpTraversalOptions).submit().get());
    }

    /** Builds the help menu embed. */
    public static EmbedBuilder createHelpMessage() {
        String message = """
                **General Commands:**
                `/help` - Opens this menu. If it already exists, a link to this message will be posted.
                `/menu` - Opens the main menu.
                `/clear` - Clears 100 bot messages. *Note: This might take a while*.
                `/reset` - Resets your profile picture to the default Sitekick.
                                
                **Image Editing Commands:**
                `/fill` - Allows you to input an RGB or HEX value for the body or eyes.
                `/linear_gradient` - Set the colour of the body or eyes to a linear gradient.
                `/bilinear_gradient` - Set the colour of the body or eyes to a bilinear gradient.
                `/sections` - Allows you to customize each of the five sections of a Sitekick.
                `/heterochromia` - Let's you make a Sitekick with two different colour eyes.
                `/rotate` - Rotates the profile picture.
                `/zoom` - Allows you to zoom into the profile picture.
                `/translate` - Allows you to shift the profile picture.
                `/flip` - Allows you to flip the profile picture... looks weird.
                
                **Presets:**
                `/kablooey` - Changes the profile picture to Kablooey.
                `/authicer` - Changes the profile picture to Authicer.
                `/paul` - Changes the profile picture to Paul.
                `/deadkick` - Changes the profile picture to a deadkick.

                **Brief Tutorial:**
                Use the buttons below to navigate and make changes to your own Sitekick avatar.
                After you finish your avatar, you can download it and upload it to Discord.

                [**Discord Avatar Change Tutorial**](https://wiki.sitekickremastered.com/en/Home/Sitekick/Tutorials/SitePic)
                """;
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(0xE1306C); //Set colour of sidebar
        eb.setDescription(message); //Set up the message
        eb.setTimestamp(new Date().toInstant()); //Set up the date
        return eb;
    }
}

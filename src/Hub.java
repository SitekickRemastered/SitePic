import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Hub extends ListenerAdapter {
    Dotenv dotenv = Dotenv.configure().filename(".env").load();
    //These are for checking Sitepic's status
    String statusURL = dotenv.get("SITEPIC_PING_LINK");
    ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    /**
     * This function runs when the bot is ready. It gets the Status URL of the bot, and then
     * gets the URL every 45 seconds. If this fails, another bot will alert us so it can be fixed.
     *
     * @param e - The readyevent listener. Activates when the bot is ready / starts up
     */
    public void onReady(ReadyEvent e) {
        List<CommandData> commandData = new ArrayList<>();
        OptionData section1 = new OptionData(OptionType.STRING, "section1", "Upper Left Section Colour", true);
        OptionData section2 = new OptionData(OptionType.STRING, "section2", "Upper Right Section Colour", true);
        OptionData section3 = new OptionData(OptionType.STRING, "section3", "Middle Right Section Colour", true);
        OptionData section4 = new OptionData(OptionType.STRING, "section4", "Lower Left Section Colour", true);
        OptionData section5 = new OptionData(OptionType.STRING, "section5", "Lower Right Section Colour", true);
        OptionData lines = new OptionData(OptionType.STRING, "lines", "The lines of the Sitekick", true);
        OptionData fill = new OptionData(OptionType.STRING, "hex_or_rgb", "Hex or RGB Code", true);
        OptionData colour1 = new OptionData(OptionType.STRING, "colour1", "First Colour", true);
        OptionData colour2 = new OptionData(OptionType.STRING, "colour2", "Second Colour", true);
        OptionData colour3 = new OptionData(OptionType.STRING, "colour3", "Third Colour", true);
        OptionData colour4 = new OptionData(OptionType.STRING, "colour4", "Fourth Colour", false);
        OptionData angle = new OptionData(OptionType.INTEGER, "angle", "What angle do you want the gradient (in degrees)?", false);
        OptionData angle2 = new OptionData(OptionType.INTEGER, "rotate_amount", "How much do you want to rotate by (in degrees)?", true);
        OptionData zoom = new OptionData(OptionType.INTEGER, "zoom_amount", "What percentage do you want to zoom in?", true);
        OptionData translateX = new OptionData(OptionType.INTEGER, "x", "How much do you want to move in the x direction?", true);
        OptionData translateY = new OptionData(OptionType.INTEGER, "y", "How much do you want to move in the y direction?", true);
        OptionData flipDirection = new OptionData(OptionType.INTEGER, "flip_direction", "Which axis to flip on?", true).addChoice("Horizontal", 1).addChoice("Vertical", 2);
        commandData.add(Commands.slash("menu", "Opens the main menu.").setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        commandData.add(Commands.slash("help", "Opens the help menu.").setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        commandData.add(Commands.slash("clear", "Deletes 100 Bot Messages (This might take a while).").setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        commandData.add(Commands.slash("reset", "Resets your Profile Picture back to default.").setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        commandData.add(Commands.slash("fill", "Allows you to add colour based on Hex or RGB values.").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(fill));
        commandData.add(Commands.slash("linear_gradient", "Set the colour to a linear gradient.").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(colour1, colour2, angle));
        commandData.add(Commands.slash("bilinear_gradient", "Set the colour to a bilinear gradient.").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(colour1, colour2, colour3, colour4));
        commandData.add(Commands.slash("sections", "Choose a different colour for every section.").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(section1, section2, section3, section4, section5, lines));
        commandData.add(Commands.slash("heterochromia", "Choose a different colour for each eye.").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(colour1, colour2));
        commandData.add(Commands.slash("rotate", "Rotate your profile picture.").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(angle2));
        commandData.add(Commands.slash("zoom", "Zoom into your profile picture.").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(zoom));
        commandData.add(Commands.slash("translate", "Shift your profile picture (Default Position is 0, 0).").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(translateX, translateY));
        commandData.add(Commands.slash("flip", "Flip your profile picture.").setDefaultPermissions(DefaultMemberPermissions.DISABLED).addOptions(flipDirection));
        commandData.add(Commands.slash("kablooey", "Changes the profile picture to Kablooey.").setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        commandData.add(Commands.slash("authicer", "Changes the profile picture to Authicer.").setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        commandData.add(Commands.slash("paul", "Changes the profile picture to Paul.").setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        commandData.add(Commands.slash("deadkick", "Changes the your profile picture a deadkick.").setDefaultPermissions(DefaultMemberPermissions.DISABLED));
        e.getJDA().updateCommands().addCommands(commandData).queue();

        threadPool.scheduleWithFixedDelay(() -> {
            try {
                URLConnection conn = new URL(statusURL).openConnection();
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                InputStream response = conn.getInputStream();
                response.close();
            }
            catch (IOException ex) { ex.printStackTrace(); }
        }, 0, 45, TimeUnit.SECONDS);
    }

    /** The hub for all slash commands for SitePic.
     *  @param e - The SlashCommandInteractionEvent listener. Activates this function whenever it hears a slash command
     */
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)  {

        // This function runs through the private message.
        // Each private command that the user receives either sends them to a menu or modifies the profile picture in some way.
        if (e.getChannelType() == ChannelType.PRIVATE) {
            boolean sendEdit = false;

            // If the user is not in the user list add then
            if (!V.uc.userExists(V.uc.getUser(e.getUser().getName()))) {
                User user = new User(e.getUser().getName());
                V.uc.addUser(user);
            }
            // Set the current user
            V.currentUser = V.uc.getUser(e.getUser().getName());

            // Send the menu to the user
            if (e.getName().equals("menu")) {

                // Change the help message button colour if it exists.
                if (V.currentUser.getHelpMessage() != null && !V.currentUser.getHelpMessage().getActionRows().isEmpty())
                    V.currentUser.getHelpMessage().editMessageEmbeds(V.currentUser.getHelpMessage().getEmbeds().get(0)).setComponents().queue();

                SendPFP.deleteMessages(e);
                try { Menus.menu(e); }
                catch (Exception exception) { exception.printStackTrace(); }
                e.deferReply().setEphemeral(true).queue(m -> m.deleteOriginal().queue());
            }

            // Send the help message to the user
            if (e.getName().equals("help")) {

                // Only send if it doesn't exist
                if (V.currentUser.getHelpMessage() == null){
                    SendPFP.deleteMessages(e);
                    try { Menus.help(e); }
                    catch (ExecutionException | InterruptedException executionException) { executionException.printStackTrace(); }
                    e.deferReply().setEphemeral(true).queue(m -> m.deleteOriginal().queue());
                }
                else
                    e.reply("Help Message already exists! Link: " + V.currentUser.getHelpMessage().getJumpUrl()).setEphemeral(true).queue();
            }

            // Deletes 100 past messages is the command is clear.
            if (e.getName().equals("clear")) {
                for (Message m : e.getChannel().getHistory().retrievePast(100).complete()) {
                    if (m.getAuthor().isBot())
                        m.delete().queue();
                }
                V.currentUser.clearHelpMessage();
                V.currentUser.clearBotMessages();
                e.reply("Deleting 100 Bot Messages (Please be patient. Discord has rate limits, so this might take a while)!").setEphemeral(true).queue();
            }

            // ----- Start of PFP modification commands -----
            // Each of these commands will set a value or global variable that will be used in the CreatePFP functions to
            // modify the user's profile picture.

            // Reset the profile picture and alert the user.
            if (e.getName().equals("reset")) {
                V.currentUser.resetDefault();
                e.reply("Profile Picture Reset!").setEphemeral(true).queue();
                sendEdit = true;
            }

            // Sets the colour of the body or eyes based on HEX or RGB inputs
            if (e.getName().equals("fill")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {

                    // Split the HEX / RGB up to make sure it's valid
                    Color nc = convertHex(e.getOption("hex_or_rgb").getAsString().split(" "));
                    if (nc == null)
                        e.reply("Command or colour code not recognized. Please input either RGB or HEX formatting.").setEphemeral(true).queue();

                    // Set the body colour or custom eye colour if valid
                    else {
                        if (V.currentUser.getLocation().equals("body"))
                            V.currentUser.setBodyColour(nc);
                        else
                            V.currentUser.setCustomEyeColour(nc);
                        sendEdit = true;
                    }
                }
                else
                    e.reply("Please use /fill in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // Sets the colour of the body or eyes to a linear gradient
            if (e.getName().equals("linear_gradient")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {

                    // Get all arguments for the command
                    Color g1 = convertHex(e.getOption("colour1").getAsString().split(" "));
                    Color g2 = convertHex(e.getOption("colour2").getAsString().split(" "));
                    int angle = e.getOption("angle") != null ? e.getOption("angle").getAsInt() : 0;

                    // If not valid, send an error
                    if (g1 == null || g2 == null)
                        e.reply("Command or colour code not recognized. Please input either RGB or HEX formatting.").setEphemeral(true).queue();

                    // Otherwise get a new paint using getGradientColour(), and set the body or custom eye colour
                    else {
                        Paint newColour = getGradientColour("linear", g1, g2, null, null, angle);
                        if (V.currentUser.getLocation().equals("body"))
                            V.currentUser.setBodyColour(newColour);
                        else
                            V.currentUser.setCustomEyeColour(newColour);
                        sendEdit = true;
                    }
                }
                else
                    e.reply("Please use /gradient in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // Sets the colour of the body or eyes to a bilinear gradient
            if (e.getName().equals("bilinear_gradient")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {

                    // Get all arguments for the command
                    Color g1 = convertHex(e.getOption("colour1").getAsString().split(" "));
                    Color g2 = convertHex(e.getOption("colour2").getAsString().split(" "));
                    Color g3 = convertHex(e.getOption("colour3").getAsString().split(" "));
                    Color g4 = e.getOption("colour4") != null ? convertHex(e.getOption("colour4").getAsString().split(" ")) : null;

                    // Check validity of inputs
                    if (g1 == null || g2 == null || g3 == null || (e.getOption("colour4") != null && g4 == null))
                        e.reply("Command or colour code not recognized. Please input either RGB or HEX formatting.").setEphemeral(true).queue();

                    // If not valid, send an error
                    else {
                        Paint newColour = getGradientColour("bilinear", g1, g2, g3, g4, 0);
                        if (V.currentUser.getLocation().equals("body"))
                            V.currentUser.setBodyColour(newColour);
                        else
                            V.currentUser.setCustomEyeColour(newColour);
                        sendEdit = true;
                    }
                }
                else
                    e.reply("Please use /gradient in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // Sets a different colour for each section of the sitekick
            if (e.getName().equals("sections")) {
                if (V.currentUser.getLocation().equals("body")) {

                    // Get all arguments for the command
                    Color g1 = convertHex(e.getOption("section1").getAsString().split(" "));
                    Color g2 = convertHex(e.getOption("section2").getAsString().split(" "));
                    Color g3 = convertHex(e.getOption("section3").getAsString().split(" "));
                    Color g4 = convertHex(e.getOption("section4").getAsString().split(" "));
                    Color g5 = convertHex(e.getOption("section5").getAsString().split(" "));
                    Color lines = convertHex(e.getOption("lines").getAsString().split(" "));

                    // Check validity
                    if (g1 == null || g2 == null || g3 == null || g4 == null || g5 == null || lines == null)
                        e.reply("Command or colour code not recognized. Please input either RGB or HEX formatting.").setEphemeral(true).queue();

                    // If  valid, add each colour to a list that will be used to set the colour to each section.
                    else {
                        V.currentUser.addToSL(g1);
                        V.currentUser.addToSL(g2);
                        V.currentUser.addToSL(g3);
                        V.currentUser.addToSL(g4);
                        V.currentUser.addToSL(g5);
                        V.currentUser.addToSL(lines);
                        sendEdit = true;
                    }
                }
                else
                    e.reply("Please use /sections in either the Body Menu!").setEphemeral(true).queue();
            }

            // Sets a different colour to each eye.
            if (e.getName().equals("heterochromia")) {
                if (V.currentUser.getLocation().equals("eyes")) {

                    // Get all arguments for the command
                    Color g1 = convertHex(e.getOption("colour1").getAsString().split(" "));
                    Color g2 = convertHex(e.getOption("colour2").getAsString().split(" "));

                    // Check validity
                    if (g1 == null || g2 == null)
                        e.reply("Command or colour code not recognized. Please input either RGB or HEX formatting.").setEphemeral(true).queue();

                    // If valid, set a 50 / 50 gradient across the entire body that will make the eyes be different colours
                    else {
                        Point2D start = new Point2D.Float((V.imageSize / 2) + 4, 0.0f);
                        Point2D end = new Point2D.Float((V.imageSize / 2) + 8, 0.0f);
                        Paint newColour = new GradientPaint(start, g1, end, g2);
                        V.currentUser.setCustomEyeColour(newColour);
                        sendEdit = true;
                    }
                }
                else
                    e.reply("Please use /heterochromia in the Eyes Menu!").setEphemeral(true).queue();
            }

            // Rotates the sitekick/
            if (e.getName().equals("rotate")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {
                    int angle = e.getOption("rotate_amount").getAsInt();
                    V.currentUser.setRA(angle);
                    sendEdit = true;
                }
                else
                    e.reply("Please use /rotate in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // Zooms in on the sitekick.
            if (e.getName().equals("zoom")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {
                    int zoom = e.getOption("zoom_amount").getAsInt();
                    V.currentUser.setZA(zoom);
                    sendEdit = true;
                }
                else
                    e.reply("Please use /zoom in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // Moves the profile picture x / y amount from the centre.
            if (e.getName().equals("translate")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {
                    int x = e.getOption("x").getAsInt();
                    int y = e.getOption("y").getAsInt();
                    V.currentUser.setTA(x, y);
                    sendEdit = true;
                }
                else
                    e.reply("Please use /translate in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // FLips the image
            if (e.getName().equals("flip")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {
                    int userOption = e.getOption("flip_direction").getAsInt();

                    // Flip horizontally and vertically.
                    if (V.currentUser.getFlipType() == 1 && userOption == 2 || V.currentUser.getFlipType() == 2 && userOption == 1)
                        V.currentUser.setFlipType(3);

                    // Flipped vertically
                    else if (V.currentUser.getFlipType() == 3 && userOption == 1)
                        V.currentUser.setFlipType(2);

                    // Flip horizontally
                    else if (V.currentUser.getFlipType() == 3 && userOption == 2)
                        V.currentUser.setFlipType(1);

                    // No flip
                    else if (V.currentUser.getFlipType() != 0)
                        V.currentUser.setFlipType(0);

                    // Otherwise normally (Fliptype is already 0)
                    else
                        V.currentUser.setFlipType(userOption);
                    sendEdit = true;
                }
                else
                    e.reply("Please use /flip in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // If the user uses the preset Kablooey, set the body and eyes to Kablooey's
            if (e.getName().equals("kablooey")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {
                    V.currentUser.setBodyColour(new Color(0,122, 254));
                    V.currentUser.setEyePic("GreenEyes.png");
                    sendEdit = true;
                }
                else
                    e.reply("Please use /kablooey in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // If the user uses the preset Authicer, set the body and eyes to Authicer's
            if (e.getName().equals("authicer")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {
                    V.currentUser.setBodyColour(new Color(101,75, 174));
                    V.currentUser.setCustomEyeColour(new Color(163, 235, 219));
                    sendEdit = true;
                }
                else
                    e.reply("Please use /authicer in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // If the user uses the preset Paul, set the body and eyes to Paul's
            if (e.getName().equals("paul")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {
                    V.currentUser.setBodyColour(new Color(233,138, 3));
                    V.currentUser.setCustomEyeColour(new Color(24, 158, 170));
                    sendEdit = true;
                }
                else
                    e.reply("Please use /paul in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // If the user uses the preset deadkick, set the body and eyes to a deadkick's
            // Since a deadkick's is unique, each section is set separately.
            if (e.getName().equals("deadkick")) {
                if (V.currentUser.getLocation().equals("body") || V.currentUser.getLocation().equals("eyes")) {
                    V.currentUser.setEyePic("SquintEyes.png");
                    V.currentUser.addToSL(new Color(220,209, 175));
                    V.currentUser.addToSL(new Color(220,209, 175));
                    V.currentUser.addToSL(new Color(220,209, 175));
                    V.currentUser.addToSL(new Color(220,209, 175));
                    V.currentUser.addToSL(new Color(220,209, 175));
                    V.currentUser.addToSL(new Color(163, 140, 71));
                    V.currentUser.setDeadkick(true);
                    sendEdit = true;
                }
                else
                    e.reply("Please use /deadkick in either the Body Menu or Eyes Menu!").setEphemeral(true).queue();
            }

            // If the user's not going to another menu / is modifying the pfp, then we edit the embed instead.
            if (sendEdit) {
                try {
                    SendPFP.editEmbed(e);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        else
            e.reply("Hey, " + e.getUser().getAsMention() + "! DM me to use that command!").setEphemeral(true).queue();
    }

    /** Function that converts a String array of arguments into a HEX value.
     * @param args - A String array of the user's input
     * @return a Color that will be used for a custom Body or Eye colour.
     */
    public Color convertHex(String[] args) {

        // If the user typed only 1 argument (i.e. a HEX value or and RGB value all-together).
        if (args.length == 1) {
            if (args[0].matches("^(#|)((?:[A-Fa-f0-9]{3}){1,2})$")) {
                String colourInput = "";

                // Get rid of any # if they're there. It just makes things more annoying.
                if (args[0].startsWith("#"))
                    args[0] = args[0].substring(1);

                // If the user inputted RGB values.
                if (args[0].length() == 3)
                    colourInput = "" + args[0].charAt(0) + args[0].charAt(0) + args[0].charAt(1) + args[0].charAt(1) + args[0].charAt(2) + args[0].charAt(2);

                // If the argument is already a valid hex
                if (args[0].length() == 6)
                    colourInput = args[0];
                return Color.decode("#" + colourInput);
            }
        }
        // If the user input was an RGB string.
        if (args.length == 3) {
            if (args[0].matches("\\d{1,3} \\d{1,3} \\d{1,3}") && Integer.parseInt(args[0]) <= 255 && Integer.parseInt(args[1]) <= 255 && Integer.parseInt(args[2]) <= 255)
                return new Color(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }

        return null;
    }

    /**
     * A function that converts a user's colour inputs into a gradient.
     * @param type - The type of gradient the user wants to use
     * @param g1 - The first colour of the gradient
     * @param g2 - The second colour of the gradient
     * @param g3 - The (optional) third colour of the gradient
     * @param g4 - The (optional) fourth colour of the gradient
     * @param angle - The angle the user wants the gradient to be
     * @return a Paint that will be the gradient of the Body or Eyes.
     */
    public Paint getGradientColour(String type, Color g1, Color g2, Color g3, Color g4, int angle) {
        switch (type) {

            // If the user wants a linear gradient, we get the points from the getAngle function and create a gradient
            // using only the first two colours
            case "linear" -> {
                Point2D[] points = getAngle(angle);
                getAngle(angle);
                return new GradientPaint(points[0], g1, points[1], g2);
            }

            // If the user wants a bilinear gradient, we add each colour to an array and get a new gradient paint
            // from the number of colours the user put in.
            case "bilinear" -> {
                V.currentUser.setMCG(true);
                V.currentUser.addToPL(g1);
                V.currentUser.addToPL(g2);
                V.currentUser.addToPL(g3);
                if (g4 != null)
                    V.currentUser.addToPL(g4);
                return new GradientPaint(V.imageSize, 0f, g1, 0f, V.imageSize, g2);
            }
        }
        return null;
    }

    /**
     * This function takes an angle and calculates the starting and end points for a gradient.
     *
     * @param angle - A given angle in degrees.
     * @return - Returns an array that includes the start and end points.
     */
    public Point2D[] getAngle(int angle){

        // Get the angle within a range of 360 degrees, and get the centre of the image.
        angle = angle % 360;
        float centre = V.imageSize / 2;

        // If the angle is facing left or right,
        if (angle <= 45 || angle >= 135 && angle <= 225 || angle >= 315) {

            // Get the y coord using the centre and the tan function.
            float yCoord = (float) (centre * Math.tan(Math.toRadians(angle)));

            // The x coord will either be all the way left or all the way right depending on whether the gradient
            // is facing left or right.
            if (angle >= 135 && angle <= 225) {
                return new Point2D[]{
                    new Point2D.Float(V.imageSize, centre - yCoord),
                    new Point2D.Float(0.0f, centre + yCoord)
                };
            }
            else {
                return new Point2D[]{
                    new Point2D.Float(0.0f, centre + yCoord),
                    new Point2D.Float(V.imageSize, centre - yCoord)
                };
            }
        }

        // If the angle is facing up or down.
        else {

            // The xcoord will be the full image size if the gradient is fully up or down
            // Otherwise, it can be calculated using tan.
            float xCoord = (angle == 90 || angle == 270) ? V.imageSize : (float) (centre + (V.imageSize / (2 * Math.tan(Math.toRadians(angle)))));

            // If the angle is directly up or down, we can just make the gradient go from one corner to the adjacent corner
            if (angle == 90) {
                return new Point2D[]{
                    new Point2D.Float(0.0f, V.imageSize),
                    new Point2D.Float(0.0f, 0.0f)
                };
            }
            else if (angle == 270) {
                return new Point2D[]{
                    new Point2D.Float(0.0f, 0.0f),
                    new Point2D.Float(0.0f, V.imageSize)
                };
            }

            //Otherwise, we do what we normally do .
            else if (angle <= 135) {
                return new Point2D[]{
                    new Point2D.Float(V.imageSize - xCoord, V.imageSize),
                    new Point2D.Float(0.0f + xCoord, 0.0f)
                };
            }
            else {
                return new Point2D[]{
                    new Point2D.Float(0.0f + xCoord, 0.0f),
                    new Point2D.Float(V.imageSize - xCoord, V.imageSize)
                };
            }
        }
    }

    /**
     * The bot seems to change message's IDs if the message is edited.
     * This function makes sure that a message is included if it's edited.
     *
     * @param e - Message Update Event Listener.
     */
    public void onMessageUpdate(MessageUpdateEvent e) {
        if (e.getAuthor().isBot() && !e.getMessage().isEphemeral() && !V.currentUser.messageExists(e.getMessage()))
            V.currentUser.addBotMessage(e.getMessage());
    }

    /**
     * This is the Message Listener. It logs the bot's message, so they can be deleted later.
     * If you send anything that isn't a command, it will give you a help message (assuming it doesn't already exist).
     * Type menu to go to the menu function, Type back in the body
     *
     * @param e - Message Event Listener.
     */
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getChannelType() == ChannelType.PRIVATE) {

            // Add the bot message in case the user wants to delete it laster.
            if (e.getAuthor().isBot() && !e.getMessage().isEphemeral() && !V.currentUser.messageExists(e.getMessage()))
                V.currentUser.addBotMessage(e.getMessage());

            // If the user messages
            else {
                //Save the user if they don't exist in the userList
                if (!V.uc.userExists(V.uc.getUser(e.getAuthor().getName()))) {
                    User user = new User(e.getAuthor().getName());
                    V.uc.addUser(user);
                }
                //Set the currnet user
                V.currentUser = V.uc.getUser(e.getAuthor().getName());

                //If the help message doesn't exist then delete the bot messages before and send the help message
                if (V.currentUser.getHelpMessage() == null) {
                    if (!V.currentUser.getBotMessages().isEmpty()) {
                        for (String mId : V.currentUser.getBotMessages().keySet()) {
                            e.getChannel().deleteMessageById(mId).queue();
                        }
                        V.currentUser.clearBotMessages();
                    }
                    try {
                        Menus.help(e);
                    } catch (ExecutionException | InterruptedException executionException) {
                        executionException.printStackTrace();
                    }
                } else {
                    e.getChannel().sendMessage("Help Message already exists! Link: " + V.currentUser.getHelpMessage().getJumpUrl()).queue();
                }
            }
        }
    }


    /** This is a separate function 'cause I didn't want to put try{} catch{} around everything */
    public void onButtonInteraction(ButtonInteractionEvent e) {
        try { OBI(e); }
        catch (Exception ex) { throw new RuntimeException(ex); }
    }

    /**
     * This function sends the user to wherever they want to go.
     *
     * @param e - The Button Event Listener.
     */
    public void OBI(ButtonInteractionEvent e) throws Exception {
        boolean editEmbed = false;
        String reactionName = e.getButton().getEmoji().getName();
        String fullReaction = e.getButton().getEmoji().getAsReactionCode();

        if (!Objects.requireNonNull(e.getUser()).isBot()) {
            V.currentUser = V.uc.getUser(e.getUser().getName());

            // If the button we clicked is a traversal option, we can only go to the Main Menu, Body Menu, Eyes Menu, or Help Menu
            if (Arrays.asList(V.traversalMenuStrings).contains(e.getButton().getLabel())) {
                SendPFP.deleteMessages(e);

                if (e.getButton().getLabel().equals("Back") || e.getButton().getLabel().equals("Main Menu")) {
                    Menus.menu(e);
                    e.deferReply().setEphemeral(true).queue(m -> m.deleteOriginal().queue());
                }

                if (e.getButton().getLabel().equals("Body Menu"))
                    Menus.body(e);

                if (e.getButton().getLabel().equals("Eyes Menu"))
                    Menus.eyes(e);

                if (e.getButton().getLabel().equals("Help"))
                    Menus.help(e);
            }
            // Otherwise the button was a preset, so we have to set the BodyColour or EyePic depending on the current location.
            else {
                if (V.currentUser.getLocation().equals("body"))
                    V.currentUser.setBodyColour(V.colourArray[Arrays.asList(V.bodyReactions).indexOf(fullReaction)]);

                if (V.currentUser.getLocation().equals("eyes"))
                    V.currentUser.setEyePic(reactionName + ".png");
                editEmbed = true;
            }
            if (editEmbed)
                SendPFP.editEmbed(e);
        }

        // If the button we clicked is attached to the help message,
        // delete the buttons on the help menu so the user doesn't break anything.
        if (e.getMessage().equals(V.currentUser.getHelpMessage()))
            e.getMessage().editMessageEmbeds(e.getMessage().getEmbeds()).setComponents().queue();
    }
}
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class CreatePFP {

    /**
     * This function builds the image. It applies all modifications step by step.
     *
     * @return - Returns the completed picture
     */
    public static BufferedImage buildImage() throws Exception {
        Paint colour = V.currentUser.getBodyColour();
        String eyeImage = V.currentUser.getEyePic();
        Paint customEyeColour = V.currentUser.getCustomEyeColour();

        // Get all the images needed
        BufferedImage bg = ImageIO.read(new File("pfpImages/", "background.png"));
        BufferedImage body = ImageIO.read(new File("pfpImages/", "body.png"));
        BufferedImage eyes = ImageIO.read(new File("pfpImages/eyes/", eyeImage));
        BufferedImage ti = ImageIO.read(new File("pfpImages/", "top_image.png"));
        BufferedImage pfp = new BufferedImage(bg.getWidth(), bg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = pfp.createGraphics();
        AffineTransform old = g.getTransform();
        g.drawImage(bg, 0, 0, null);

        //Translate first so the inputted values aren't modified by the rotation
        if (V.currentUser.getTA()[0] != 0 || V.currentUser.getTA()[1] != 0)
            g.translate(V.currentUser.getTA()[0], V.currentUser.getTA()[1]);

        // Apply rotation
        if (V.currentUser.getRA() != 0 && V.currentUser.getZA() == 0)
            g.rotate(Math.toRadians(360 - (V.currentUser.getRA() % 360)), V.imageSize / 2, V.imageSize / 2);

        // Apply zoom
        if (V.currentUser.getZA() != 0) {
            double imageScale = (1 + (V.currentUser.getZA() * 0.01));
            double newImageSize = V.imageSize * imageScale;
            int xyDifference = (int) ((V.imageSize - newImageSize) / 2);
            g.scale(imageScale, imageScale);
            g.translate(xyDifference, xyDifference);

            if (V.currentUser.getRA() != 0)
                g.rotate(Math.toRadians(360 - (V.currentUser.getRA() % 360)), V.imageSize / 2, V.imageSize / 2);
        }

        // Apply flip
        if (V.currentUser.getFlipType() != 0) {
            //Flip Horizontally
            if (V.currentUser.getFlipType() == 1) {
                g.translate(V.imageSize, 0);
                g.scale(-1, 1);
            }
            //Flip Vertically
            else if (V.currentUser.getFlipType() == 2) {
                g.translate(0, V.imageSize);
                g.scale(1, -1);
            }
            //Flip Both Horizontally and Vertically
            else {
                g.translate(V.imageSize, V.imageSize);
                g.scale(-1, -1);
            }
        }

        //If the sectionlist isn't empty, then we build the image using the sections instead of dyeing the body.
        if (!V.currentUser.getSL().isEmpty()) {
            BufferedImage s1 = ImageIO.read(new File("pfpImages/", "s1.png"));
            BufferedImage s2 = ImageIO.read(new File("pfpImages/", "s2.png"));
            BufferedImage s3 = ImageIO.read(new File("pfpImages/", "s3.png"));
            BufferedImage s4 = ImageIO.read(new File("pfpImages/", "s4.png"));
            BufferedImage s5 = ImageIO.read(new File("pfpImages/", "s5.png"));
            BufferedImage lines = ImageIO.read(new File("pfpImages/", "lines.png"));
            BufferedImage outline = ImageIO.read(new File("pfpImages/", "outline.png"));
            s1 = fixShit(s1, dyeImage(s1, V.currentUser.getSL().get(0), BlendComposite.Subtract));
            s2 = fixShit(s2, dyeImage(s2, V.currentUser.getSL().get(1), BlendComposite.Subtract));
            s3 = fixShit(s3, dyeImage(s3, V.currentUser.getSL().get(2), BlendComposite.Subtract));
            s4 = fixShit(s4, dyeImage(s4, V.currentUser.getSL().get(3), BlendComposite.Subtract));
            s5 = fixShit(s5, dyeImage(s5, V.currentUser.getSL().get(4), BlendComposite.Subtract));
            lines = fixShit(lines, dyeImage(lines, V.currentUser.getSL().get(5), BlendComposite.Subtract));
            g.drawImage(body, 0, 0, null);
            g.drawImage(s1, 0, 0, null);
            g.drawImage(s2, 0, 0, null);
            g.drawImage(s3, 0, 0, null);
            g.drawImage(s4, 0, 0, null);
            g.drawImage(s5, 0, 0, null);

            // This is for deadkick ONLY
            if (V.currentUser.getDeadkick()) {
                lines = ImageIO.read(new File("pfpImages/", "deadkickLines.png"));
                BufferedImage ear = ImageIO.read(new File("pfpImages/", "deadkickEar.png"));
                BufferedImage shadow = ImageIO.read(new File("pfpImages/", "deadkickShadow.png"));
                g.drawImage(shadow, 0, 0, null);
                g.drawImage(ear, 0, 0, null);
            }

            g.drawImage(lines, 0, 0, null);
            g.drawImage(outline, 0, 0, null);
        }
        else {
            body = dyeImage(body, colour, BlendComposite.Subtract);
            g.drawImage(body, 0, 0, null);
        }

        // If the current eyes are hollow eyes, get rid of messed up transparency stuff and multiply instead of burn
        if (eyeImage.equals("HollowEyes.png"))
            eyes = fixShit(eyes, dyeImage(eyes, colour, BlendComposite.Multiply));

        // If the user uses the deadkick preset, fix the eyes and subtract a specific colour
        if (V.currentUser.getDeadkick())
            eyes = fixShit(eyes, dyeImage(eyes, new Color(136, 117, 60), BlendComposite.Subtract));

        // If the current eyes are custom eyes, get rid of messed up transparency and use linear burn
        if (eyeImage.equals("CustomEyes.png"))
            eyes = fixShit(eyes, dyeImage(eyes, customEyeColour, BlendComposite.Subtract));

        g.drawImage(eyes, 0, 0, null);

        // Add the shine to the custom eyes
        if (eyeImage.equals("CustomEyes.png"))
            g.drawImage(ImageIO.read(new File("pfpImages/eyes/Shine.png")), 0, 0, null);

        // Apply the old transformation information so the next transformations are easier to do.
        g.setTransform(old);
        g.drawImage(ti, 0, 0, null);

        // Reset stuff
        if (V.currentUser.getMCG())
            V.currentUser.setMCG(false);

        if (V.currentUser.getDeadkick()) {
            V.currentUser.resetDefault();
            V.currentUser.setDeadkick(false);
        }

        if (!V.currentUser.getSL().isEmpty())
            V.currentUser.clearSL();
        return pfp;
    }

    /**
     * This function dyes the image, so it has the correct colour given.
     * If we're dyeing a body, we use Linear Burn, otherwise we use Multiply.
     *
     * @param image - The image that is to be coloured
     * @param colour - The color that the image will be changed to
     * @param type - The type of Blend Composite we will use - either Linear Burn or Multiply
     * @return - returns the dyed image.
     */
    public static BufferedImage dyeImage(BufferedImage image, Paint colour, BlendComposite type) {
        BufferedImage dyed = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.setComposite(type);
        if (V.currentUser.getMCG()) {
            BiLinearGradientPaint BGP;
            if (V.currentUser.getPL().size() == 3)
                BGP = new BiLinearGradientPaint(image, V.currentUser.getPL().get(0), V.currentUser.getPL().get(1), V.currentUser.getPL().get(2));
            else
                BGP = new BiLinearGradientPaint(image, V.currentUser.getPL().get(0), V.currentUser.getPL().get(1), V.currentUser.getPL().get(2), V.currentUser.getPL().get(3));
            V.currentUser.setBodyColour(BGP);
            g.setPaint(BGP);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
            V.currentUser.clearPL();
        }
        else {
            g.setPaint(colour);
            g.fillRect(0, 0, image.getWidth(), image.getHeight());
        }

        g.dispose();
        return dyed;
    }

    /**
     * This function takes two images, and copies the transparent pixels from one to the other.
     * This is so hollow eyes and sections don't break and look bad.
     *
     * @param mask - The image you are copying transparent pixels from
     * @param img - The image you want to put transparent pixels in
     * @return - Returns the image with transparent pixels.
     */
    public static BufferedImage fixShit(BufferedImage mask, BufferedImage img) {
        int width = mask.getWidth();
        int height = mask.getHeight();

        BufferedImage processed = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = mask.getRGB(x, y);
                int maskAlpha = alpha(rgb);
                int imgColor = img.getRGB(x, y);
                if (maskAlpha < 255)
                    processed.setRGB(x, y, maskAlpha(imgColor, maskAlpha));
                else
                    processed.setRGB(x, y, imgColor);
            }
        }

        return processed;
    }

    public static int maskAlpha(int rgb, int alpha) {
        int colour = (0x00ffffff & rgb);
        return colour + ((alpha) << 24);
    }

    public static int alpha(int rgb) {
        return (0xff & (rgb >> 24));
    }

}


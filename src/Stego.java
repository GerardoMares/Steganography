import java.io.File;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Scanner;
import javax.imageio.ImageIO;

/*
A basic Steganography program that will hide a message inside of an image.

Program rquires hiding messages in PNG files only.

##############################################################################################
compile with "javac Stego.java"

run with "java Stego"

##############################################################################################
The program will ask for an option to encrypt ord decrypt a message in a PNG file.

##############################################################################################
if chose Encrypt:

you will be asked to provide your secret message and a path to the PNG file you want to
use to hide your message then saves the new image with hidden message as Output.png

##############################################################################################
if choose Decrypt:

the program will look for a file named Output.png and get the message hidden in the picture.

assumes encrypt was run and there exists an image named Output.png with a message hidden there.

##############################################################################################

 */


public class Stego {


    public static void main(String[] args) {
        BufferedImage image;
        String message;
        String file;
        Scanner keyboard = new Scanner(System.in);

        try {
            System.out.print("What would you like to do? \n(1) Encrypt , (2) Decrypt: ");

            String input = keyboard.nextLine();
            if("1".equals(input)) {
                System.out.print("Enter your message: ");
                message = keyboard.nextLine();
                System.out.print("Enter image path: ");
                file = keyboard.nextLine();
                image = ImageIO.read(new File(file));
                encode(image,message);
                System.out.println("Done hiding message in " + file + " and written to Output.png");
            } else if ("2".equals(input)) {
                decode();
            } else {
                System.out.println("Invalid command");
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
    private static void encode(BufferedImage image_orig, String message) {
        BufferedImage image = makeImageReadyForEdits(image_orig);
        image = startAddingText(image,message);
        try {
            ImageIO.write(image,"png",new File("Output.png"));
        } catch(Exception e) {
           e.printStackTrace();
        }
    }

    private static void  decode() {
        byte[] decodedBytes;
        try {
            BufferedImage image = makeImageReadyForEdits(ImageIO.read(new File("Output.png")));
            decodedBytes = getMessage(getImageBytes(image));
            System.out.println(new String(decodedBytes));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage startAddingText(BufferedImage image, String text) {
        byte img[]  = getImageBytes(image);
        byte msg[] = text.getBytes();
        byte len[]  = {0,0,0,(byte)(msg.length & 0x000000FF)};
        try {
            hideLength(img, len);
            hideMessage(img, msg);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private static BufferedImage makeImageReadyForEdits(BufferedImage image) {
        BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D	graphics = new_img.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return new_img;
    }

    private static byte[] getImageBytes(BufferedImage image) {
        DataBufferByte buffer = (DataBufferByte)image.getRaster().getDataBuffer();
        return buffer.getData();
    }

    private static void hideLength(byte[] image, byte[] length) {
        int pos = 0;
        for (byte lengthByte : length) {
            for (int bit = 7; bit >= 0; --bit, ++pos) {
                int b = (lengthByte >>> bit) & 1;
                image[pos] = (byte) ((image[pos] & 0xFE) | b);
            }
        }
    }

    private static void hideMessage(byte[] image, byte[] message) {
        int offset = 32;
        for (byte messageByte : message) {
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (messageByte >>> bit) & 1;
                image[offset] = (byte) ((image[offset] & 0xFE) | b);
            }
        }
    }

    private static int getLength(byte[] image) {
        int length = 0;
        for(int i=0; i< 32; ++i) {
            length = (length << 1) | (image[i] & 1);
        }
        return length;
    }

    private static byte[] getMessage(byte[] image) {
        int offset  = 32;
        byte[] result = new byte[getLength(image)];

        for(int b=0; b<result.length; ++b ) {
            for(int i=0; i<8; ++i, ++offset) {
                result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }
}

import java.io.File;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.Scanner;
import javax.imageio.ImageIO;
import java.util.*;

/*
* A basic Steganography program that will hide a message inside of an image using LSB.
*
* Program rquires hiding messages in PNG files only.
*/

public class Stego {

  public static void main(String[] args) {
    BufferedImage image;
    String message;
    String file;
    String outputfile;
    byte[] decodedBytes;

    Scanner keyboard = new Scanner(System.in);

    if(args.length == 2 && "--mode".equals(args[0])) {
      try {
        String mode = args[1];

        if("ENCRYPT".equalsIgnoreCase(mode)) {
          System.out.println("\nStarting Encrypt\n");
          System.out.println("----------------");

          System.out.print("Enter your message: ");
          message = keyboard.nextLine();

          System.out.print("Enter image path: ");
          file = keyboard.nextLine();
          image = ImageIO.read(new File(file));

          System.out.print("Enter output image path: ");
          outputfile = keyboard.nextLine();

          if(outputfile.indexOf(".png") == -1) {
            System.out.print("Only PNG output files are supported, your image will be saved as ");
            outputfile = outputfile.substring(0, outputfile.length() - 4) + ".png";
            System.out.println(outputfile + " instead.");
          }

          encode(image, message, outputfile);

          System.out.println("Done hiding message in " + file + " and written to " + outputfile);
        }
        else if ("DECRYPT".equalsIgnoreCase(mode)) {
          System.out.println("\nStarting Decrypt\n");
          System.out.println("----------------");

          System.out.print("Enter image path: ");
          file = keyboard.nextLine();

          int index = file.indexOf(".png");
          if(index == -1) {
            System.out.println("Only PNG files are supported.");
          }
          else {
            decodedBytes = decode(file);

            if(decodedBytes != null) {
              message = new String(decodedBytes);

              System.out.println("The message is: ");
              System.out.println(message);
            }
            else {
              System.out.println("Unable to get bytes from image");
            }
          }
        }
        else {
          System.out.println("Invalid mode.");
        }
      }
      catch(Exception ex) {
        ex.printStackTrace();
      }
    }
    else {
      System.out.println("Invalid arguments");
    }


  }
  private static void encode(BufferedImage image_orig, String message, String outputfile) {
    BufferedImage image = makeImageReadyForEdits(image_orig);
    image = startAddingText(image,message);
    try {
      ImageIO.write(image,"png",new File(outputfile));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private static byte[] decode(String file) {
    byte[] decodedBytes;
    try {
      BufferedImage image = makeImageReadyForEdits(ImageIO.read(new File(file)));
      return getMessage(getImageBytes(image));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private static BufferedImage startAddingText(BufferedImage image, String text) {
    byte img[]  = getImageBytes(image);
    byte msg[] = text.getBytes();
    byte len[]  = {0,0,0,(byte)(msg.length & 0x000000FF)};
    try {
      hideLength(img, len);
      hideMessage(img, msg);
    }
    catch(Exception e) {
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

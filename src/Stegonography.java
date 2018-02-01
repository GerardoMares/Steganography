import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;

public class Stegonography {

    public static void main(String[] args) {
      BufferedImage img = null;
      File f = null;

      //read image
      try{
        f = new File("Output.jpg");
        img = ImageIO.read(f);
      }catch(IOException e){
        System.out.println(e);
      }

      // //get image width and height
      // int width = img.getWidth();
      // int height = img.getHeight();
      // String message = "Test Message";
      // int msgLen = (message.length() * 8);
      //
      // int length = 300;
      // String lengthInBits = String.format("%32s", Integer.toBinaryString(length)).replace(' ', '0');
      //

        //System.out.println(lengthInBits);
        getLength(img);
    //   for(int i = 0; i < 8; i++) {
    //     int pixel = img.getRGB(i,0);
    //     img.setRGB(i,0, editPixel(pixel,lengthInBits.substring(4*i, 4*(i + 1))));
    //
    //   }
    //
    // //  int length = 0;
    //
    //   for(int j = 1; j < height && length > 0; j++) {
    //     for(int k = 0; k < width && length > 0; k++) {
    //
    //     }
    //   }
    //
    //   try{
    //     f = new File("Output.jpg");
    //     ImageIO.write(img, "jpg", f);
    //   }catch(IOException e){
    //     System.out.println(e);
    //   }





    }

    public static void readWritePixel(int x, int y, int pixel, char curr) {

      //img.setRGB()

    }

    public static int editPixel(int pixel, String bits) {
      int a = (pixel >> 24) & 0xFF;
      int r = (pixel >> 16) & 0xFF;
      int g = (pixel >> 8) & 0xFF;
      int b = pixel & 0xFF;

      a = convert(a,bits.charAt(0) );
      r = convert(r,bits.charAt(1) );
      g = convert(g,bits.charAt(2) );
      b = convert(b,bits.charAt(3) );

      return (a<<24) | (r<<16) | (g<<8) | b;
    }

    public static int convert(int value, char bit) {

      //System.out.println("converting: " + value);
      //System.out.println("binary: " + String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0') );

      if(bit == '1') {
        int ret =  value | 1;
        //System.out.println("after: " + String.format("%8s", Integer.toBinaryString(ret)).replace(' ', '0') );
        return ret;
      } else {
        int ret2 = value & 0xFE;
        //System.out.println("after: " + String.format("%8s", Integer.toBinaryString(ret2)).replace(' ', '0') );
        return ret2;
      }
    }

    public static void getLength(BufferedImage img) {

      String bits = "";
      for(int i = 0; i < 8; i++) {
        int pixel =  img.getRGB(i, 0);
        int a = (pixel >> 24) & 0x01;
        System.out.println(a);
        bits += a;
        int r = (pixel >> 16) & 0x01;
        System.out.println(r);
        bits += r;
        int g = (pixel >> 8) & 0x01;
        System.out.println(g);
        bits += g;
        int b = pixel & 0x01;
        System.out.println(b);
        bits += b;
        //System.out.println(String.format("%32s", Integer.toBinaryString(pixel)).replace(' ', '0'));
      }

      //System.out.println(bits);

    }



}

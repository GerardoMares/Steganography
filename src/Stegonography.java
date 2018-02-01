import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.*;

public class Stegonography {

    public static void main(String[] args) {

      encrypt();
      //decrypt();

    }

    public static void decrypt() {
      BufferedImage img = null;
      File f = null;

      //read image
      try{
        f = new File("Output.jpg");
        img = ImageIO.read(f);
      }catch(IOException e){
        System.out.println(e);
      }

      getLength(img);
    }

    public static void encrypt() {
      BufferedImage img = null;
      File f = null;

      //read image
      try{
        f = new File("Sample.jpg");
        img = ImageIO.read(f);
      }catch(IOException e){
        System.out.println(e);
      }

      //get image width and height
      int width = img.getWidth();
      int height = img.getHeight();
      String message = "Test Message";
      int msgLen = (message.length() * 8);

      int length = 300;
      String lengthInBits = String.format("%32s", Integer.toBinaryString(length)).replace(' ', '0');

      System.out.println(lengthInBits);

      for(int i = 0; i < 8; i++) {
        int pixel = img.getRGB(i,0);
        //System.out.println(String.format("%32s", Integer.toBinaryString(pixel)).replace(' ', '0'));
        img.setRGB(i,0, editPixel(pixel,lengthInBits.substring(4*i, 4*(i + 1))));

        //pixel = img.getRGB(i,0);
        //System.out.println(String.format("%32s", Integer.toBinaryString(pixel)).replace(' ', '0'));

      }


      try{
        f = new File("Output.jpg");
        ImageIO.write(img, "jpg", f);
      }catch(IOException e){
        System.out.println(e);
      }
    }

    public static int editPixel(int pixel, String bits) {
      int a = (pixel >> 24) & 0xFF;
      int r = (pixel >> 16) & 0xFF;
      int g = (pixel >> 8) & 0xFF;
      int b = pixel & 0xFF;

      a = (convert(a,bits.charAt(0)) << 24);
      //printBinary(a);
      r = (convert(r,bits.charAt(1)) << 16);
      //printBinary(r);
      g = (convert(g,bits.charAt(2)) << 8);
      //printBinary(g);
      b = convert(b,bits.charAt(3));
      //printBinary(b);

      int retval = (a | r | g | b);

      //printBinary(retval);


      return retval;
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
        int pixel = img.getRGB(i, 0);
        int a = (pixel >> 24);
        String as = printBinary(a);
        bits += as.charAt(as.length() - 1);

        int r = (pixel >> 16);
        String rs = printBinary(r);
        bits += rs.charAt(rs.length() - 1);

        int g = (pixel >> 8);
        String gs = printBinary(g);
        bits += gs.charAt(gs.length() - 1);

        int b = pixel;
        String bs = printBinary(b);
        bits += bs.charAt(bs.length() - 1);
      }

      System.out.println(integerfrmbinary(bits));

    }


    public static String printBinary(int value) {
      return  String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    public static int integerfrmbinary(String str){
    double j=0;
    for(int i=0;i<str.length();i++){
        if(str.charAt(i)== '1'){
         j=j+ Math.pow(2,str.length()-1-i);
     }

    }
    return (int) j;
}



}

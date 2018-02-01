import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Stegonography {

    public static void main(String[] args) {

        encrypt();
        //decrypt();

    }

    private static void decrypt() {
        BufferedImage img = null;
        File f = null;

        //read image
        try {
            f = new File("Output.jpg");
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }

        System.out.println(getLength(img));
    }

    private static void encrypt() {
        BufferedImage img = null;
        File f = null;

        //read image
        try {
            f = new File("Sample.jpg");
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }

        int length = 300;
        String lengthInBits = String.format("%32s", Integer.toBinaryString(length)).replace(' ', '0');
        writeMessageLength(img, lengthInBits);

        //get image width and height
        String message = "Test Message";
        writeMessage(img, message);


        try {
            f = new File("Output.jpg");
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeMessageLength(BufferedImage img, String lengthInBits) {
        for (int i = 0; i < 8; i++) {
            int pixel = img.getRGB(i, 0);
            img.setRGB(i, 0, editPixel(pixel, lengthInBits.substring(4 * i, 4 * (i + 1))));
        }
    }

    private static void writeMessage(BufferedImage img, String message) {
        int width = img.getWidth();
        int height = img.getHeight();
        String messageBits = toBinaryString(message);
        System.out.println(messageBits);
        int k = 0;

        for (int i = 1; i < width ; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = img.getRGB(i,j);
                img.setRGB(i,j,editPixel(pixel, messageBits.substring(4*k, 4*(k + 1))));
                k++;
            }
        }
    }

    private static String toBinaryString(String s) {

        char[] cArray=s.toCharArray();

        StringBuilder sb=new StringBuilder();

        for(char c:cArray)
        {
            String cBinaryString=Integer.toBinaryString((int)c);
            sb.append(cBinaryString);
        }

        return sb.toString();
    }

    private static int editPixel(int pixel, String bits) {
        int a = (pixel >> 24) & 0xFF;
        int r = (pixel >> 16) & 0xFF;
        int g = (pixel >> 8) & 0xFF;
        int b = pixel & 0xFF;

        a = (convert(a, bits.charAt(0)) << 24);
        r = (convert(r, bits.charAt(1)) << 16);
        g = (convert(g, bits.charAt(2)) << 8);
        b = convert(b, bits.charAt(3));

        return (a | r | g | b);
    }

    private static int convert(int value, char bit) {
        if (bit == '1') {
            int ret = value | 1;
            return ret;
        } else {
            int ret2 = value & 0xFE;
            return ret2;
        }
    }

    private static int getLength(BufferedImage img) {

        StringBuilder bits = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int pixel = img.getRGB(i, 0);

            int a = (pixel >> 24);
            String as = printBinary(a);
            bits.append(as.charAt(as.length() - 1));

            int r = (pixel >> 16);
            String rs = printBinary(r);
            bits.append(rs.charAt(rs.length() - 1));

            int g = (pixel >> 8);
            String gs = printBinary(g);
            bits.append(gs.charAt(gs.length() - 1));

            int b = pixel;
            String bs = printBinary(b);
            bits.append(bs.charAt(bs.length() - 1));
        }

        return integerFromBinaryString(bits.toString());
    }

    private static String printBinary(int value) {
        return String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0');
    }

    private static int integerFromBinaryString(String str) {
        double j = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '1') {
                j = j + Math.pow(2, str.length() - 1 - i);
            }

        }
        return (int) j;
    }


}

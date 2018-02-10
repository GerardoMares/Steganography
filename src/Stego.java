import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by gerardomares on 2/9/18.
 */
public class Stego {

    public static void main(String[] args) {
        encrypt();
    }



    public static void encrypt() {
        BufferedImage original =null;
        try {
            original = ImageIO.read(new File("Sample.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = getBytes(original);

        assert (bytes != null);
        for (int i = 20; i < bytes.length; i++) {
            byte aByte = bytes[i];
            String current = Integer.toBinaryString(aByte & 0xFF);
            System.out.println(current);
            //current = current.substring(0,current.length()-1) + 0;
            //byte newByte = Byte.parseByte(current, 2);
            byte newByte = (byte) (aByte | 0x1);

            bytes[i] = newByte;
        }

//        try {
//            InputStream in = new ByteArrayInputStream(bytes);
//            BufferedImage bImageFromConvert = ImageIO.read(in);
//            ImageIO.write(bImageFromConvert, "PNG", new File("Output.png"));
//        } catch (Exception e ) {
//            e.printStackTrace();
//        }
    }

    static byte[] getBytes(BufferedImage img) {
        return ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
    }


    public static void enc() {
        try {
            BufferedImage inputImage = ImageIO.read(new File("Sample.png"));

            BufferedImage outputImage = new BufferedImage( inputImage.getWidth(), inputImage.getHeight(),  inputImage.getType());

            String length = "00000000000000000000000000000011";
            int k = 0;
            for (int x = 0; x < inputImage.getWidth(); x++) {
                for (int y = 0; y < inputImage.getHeight(); y++) {
                    int rgb = inputImage.getRGB(x, y);

                    int red = 0x0000ff & (rgb >> 16);
                    int green = 0x0000ff & (rgb >> 8);
                    int blue = 0x0000ff & rgb;
                    int res;
                    if(k < 32)  {
//                        char curr = length.charAt(k);
//
//                        if(curr == '1') {
//                            blue = blue | 0x1;
//                        } else {
//                            blue = blue & 0xFE;
//                        }
                        //String bits = Integer.toBinaryString(blue);
                        res = blue | (green << 8) | (red << 16);
                        System.out.println(Integer.toUnsignedString(res,2));
//                        System.out.print(bits.charAt(bits.length()-1));
                        k++;
                    }

                    res = blue | (green << 8) | (red << 16);


                    outputImage.setRGB(x, y, res);
                }
            }
            ImageIO.write(outputImage, "png", new File("Output.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

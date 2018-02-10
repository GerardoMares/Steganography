import java.io.File;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import javax.imageio.ImageIO;

/*
 *Class Steganography
 */
public class Stego {


    public static void main(String[] args) {
        BufferedImage image;
        String message = args[0];
        String file = args[1];
        String out = args[2];

        try {
             image = ImageIO.read(new File(file));
             encode(image,message);
             decode(out);

        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }
    private static void encode(BufferedImage image_orig, String message) {
        BufferedImage image = user_space(image_orig);
        image = add_text(image,message);
        try {
            ImageIO.write(image,"png",new File("Output.png"));
        } catch(Exception e) {
           e.printStackTrace();
        }
    }

    private static void  decode(String file) {
        byte[] decodedBytes;
        try {
            BufferedImage image = user_space(ImageIO.read(new File(file)));
            decodedBytes = getMessage(get_byte_data(image));
            System.out.println(new String(decodedBytes));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage add_text(BufferedImage image, String text) {
        byte img[]  = get_byte_data(image);
        byte msg[] = text.getBytes();
        byte len[]  = bit_conversion(msg.length);
        try {
            hideMessage(img, len,  0);
            hideMessage(img, msg, 32);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private  static BufferedImage user_space(BufferedImage image) {
        BufferedImage new_img  = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D	graphics = new_img.createGraphics();
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return new_img;
    }

    private static byte[] get_byte_data(BufferedImage image) {
        WritableRaster raster   = image.getRaster();
        DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
        return buffer.getData();
    }

    private static byte[] bit_conversion(int i) {
        byte byte0 = (byte)((i & 0x000000FF));
        return(new byte[]{0,0,0,byte0});
    }

    private static void hideMessage(byte[] image, byte[] message, int offset) {
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

    private  static byte[] getMessage(byte[] image) {
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

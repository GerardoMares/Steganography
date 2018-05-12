# Computer Security Stegonography Assignment

#### A basic Steganography program that will hide a message inside of an image.

>**Only PNG files are supported as of now**

> You may choose either JPEG or PNG as your image to use but the output image with your hidden message will be converted into a PNG file.
>
> Only PNG files may be used to decrypt.


compile:

```bash
javac Stego.java
```

run:

```bash
java Stego --mode $MODE
```
> $MODE is either "ENCRYPT" or "DECRYPT" not case sensitive.


### Description


> The program will take in arguments that will determine the mode, which will either be encryption or decryption and then ask for a message, filename and output file name.


##### Encrypt:

> You will be asked to provide your secret message and a path to the file you want to use to hide your message and the path to the new image that will be created containing your message.


##### Decrypt:

> The program will ask for the file name of an image that contains a message and get the message hidden in the picture if any.
>
> File must be a PNG file.

# Computer Security Stegonography Assignment

#### A basic Steganography program that will hide a message inside of an image.

> _PNG and JPEG files are supported as of now._


compile:

```bash
javac Stego.java
```

run:

```bash
java Stego
```


### Description


> The program will ask for an option to encrypt ord decrypt a message in a PNG file.


##### Encrypt:

> You will be asked to provide your secret message and a path to the PNG file you want to use to hide your message then saves the new image with hidden message as Output.png


##### Decrypt:

> The program will look for a file named Output.png and get the message hidden in the picture.
> assumes encrypt was run and there exists an image named Output.png with a message hidden there.

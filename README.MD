## About
This is a scala version of a previous project I did in python.

This app is actually usable, unlike the previous one, which was basically just demonstration.

It's slow for really big images, but obviously there is a limit to what you can reasonably represent with text. Just keep images to reasonable size and you should be alright.


Examples are in the pictures directory.

## Using it

Running the app is obviously done with java -jar, or as I was doing in testing, run from the sbt shell.
To build the app `sbt assembly`.

```
Usage: scala-ascii-art [options] <file>

  -i, --invert <bool>      
  -c, --compression <compression factor>
                           
  -l, --lightness <lightness modifier>
                           
  -w, --write <output file>
                           
  --help                   prints this usage text
  <file>            
```

Images are inverted by default, `-i false` will disable this inversion.

The text produced by this is obviously compressed `-c 2` will compress to 1/2, `-c 8` will compress to 1/8th, etc.
A higher number means less detail, lower numbers are more intensive to run and produce bigger chunks of text.

Optionally, you can offset the lightness of the pixels in the image across the board (positive or negative) with `-l <integer>`. This will change the selection of characters.

If you want to save the output to a file, `-w <file path>` will save the text to your specified file if it does not already exist.

Finally, there is a required file argument for the image input.

## Example command and output.

```
java -jar scala-ascii-art.jar -c 8 pictures/star.png -w pictures/star.txt
                                                  
                      ______                      
                    ____PP____                    
                    __}}  }}__                    
                  !!__      __!!                  
                  ____      ____                  
                  __SS      44__                  
                ::__          __::                
  __________________          __________________  
;;__??AAKK&&QQ                      QQ&&KKAA??__;;
____                                          ____
pp____                                      ____PP
  JJ____@@                              @@____JJ  
      ____??                          ??____      
        ______                      ______        
          **__oo                  oo__\\          
          vv__                      __vv          
          ____                      ____          
          __rr                      rr__          
        qq__          ::__::          __qq        
        ____        __________        ____        
        ____    !!____@@  @@____!!    ____        
        ____GG____,,          ,,____GG____        
        00______GG              qq______00        
                                           

```
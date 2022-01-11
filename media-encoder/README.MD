# MediaEncoder

## Encoding gif file
1. Convert gif to zip
    - use https://ezgif.com/split
    - or use ffmpeg + manually zip png <br>
make sure that image names in that file start with `frame_{frameNumber}`
2. Run this program:
    - java -jar mediaencoder.jar (input zip) (output file)
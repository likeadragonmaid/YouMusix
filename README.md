# YouMusix [![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

A YouTube to MP3 player. Enter a YouTube video URL and stream or download it as MP3. Listen to latest music while saving your precious bandwidth!

<p align="center">
  <img src="https://github.com/gibcheesepuffs/YouMusix/raw/master/Screenshots/YouMusix.png">
</p>

## Features:

* Easy to use
* Video duration limit - Approx. 1 hour
* Highest sound quality (usually 256 kbps, but depends on video quality)
* Download stream as MP3 with IDv3 tags and album art
* Check MP3 stream size without downloading it
* Save a lot of bandwidth

## Building:

1. Add all the jar files present in lib directory to class path or build path
2. Export project as runnable JAR

If you are using UNIX-Like OS (GNU/Linux, MacOS, BSD, etc.) make sure the JAR file is set as executable before launching it for the first time on your system.

## Possible Issues:

* Elapsed duration is known to work only when running project within IDE and does not work in exported runnable JAR.
* Downloads cannot be cancelled.
* Due to limitations imposed by server, sometimes downloading/streaming won't work. Try using another YouTube URL or click stop and play button respectively 4-5 times or even quit the application and relaunch it.
* If you get shuttering sound while streaming, it is due to slow internet connection, try pressing pause button and wait for few seconds to let the stream buffer and then finally press the play button.

### You can help me by fixing these issues and introducing more features by forking the repository and submitting a pull request.

## This is hacky code! WILL EAT YOUR CAT.

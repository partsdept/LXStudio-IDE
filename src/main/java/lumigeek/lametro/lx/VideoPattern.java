package lumigeek.lametro.lx;

import heronarts.lx.LX;
import heronarts.lx.LXCategory;
import heronarts.lx.color.LXColor;
import heronarts.lx.model.LXPoint;
import heronarts.lx.pattern.LXPattern;
import heronarts.lx.utils.LXUtils;
import processing.core.PApplet;
import processing.core.PImage;
import processing.video.*;

@LXCategory("Form")
public final class VideoPattern extends LXPattern {

    PImage img;
    Movie movie;
    int[][] delay;
    int delayCounter = 0;

    public static PImage imageHack = null;

    public VideoPattern(LX lx) {
        super(lx);
        img = imageHack;

        //addParameter("axis", this.axis);
        //addParameter("pos", this.pos);
        //addParameter("width", this.wth);

//        movie = new Movie(applet, "fgs1.mov");
//        movie.loop();
//        movie.speed(0.5);
//
//        while (!(movie.available())) {
//            System.out.println("Movie ready: " + movie.available());
//            delay(5);
//        }
//        System.out.println("Movie ready: " + movie.available());
//        movie.play();
//        movie.read();
//        movie.loadPixels();
//        while (!(movie.available())) {
//            System.out.println("Movie ready: " + movie.available());
//            delay(5);
//        }
//
//        delay = new int[Fixture.ZSIZE][movie.pixels.length];
//
//        System.out.println("DELAY: " + Fixture.ZSIZE + "::" + movie.pixels.length);

//    img = loadImage("grad.jpg");
//    img.loadPixels();
    }


    public void run(double deltaMs) {
        for (LXPoint p : model.points) {
            int c = mappedColorSampleFromPoint(p.x,p.y,p.z);
            int r = (int) LXColor.red(c);
            int g = (int) LXColor.green(c);
            int b = (int) LXColor.blue(c);
            colors[p.index] = LXColor.rgb(r,g,b);
        }
        delayCounter++;
    }


    public int mappedColorSampleFromPoint(float x, float y , float z) {
            int result = 0;
            img.loadPixels();
            int ix = (int) Math.floor(LXUtils.clamp(PApplet.map(x,-100,190,0,img.width - 1),0,img.width - 1));  //TODO: get rid of magic numbers for uvgrid+overpass and put into proper params somewhere
            int iy = (int) Math.floor(LXUtils.clamp(PApplet.map(z,-880,680,0,img.height - 1),0,img.height - 1));
            //ix = (img.width-1) - ix; // flip x lookup
            iy = (img.height-1) - iy; // flip y lookup
            //LX.log("map from: " + x + "," + y + "," + z + " to :" + ix + "," + iy);
            try {result = img.pixels[ img.width * iy + ix ]; }
            catch (ArrayIndexOutOfBoundsException e) {LX.error("Array out of bounds for: " + ix + "," + iy);}
            return result;
    }


//    public int imageLookup(float x, float y , float z) {
//        if (movie.available()) {
//            //System.out.println("Movie is: " + movie.width + " by " + movie.height);
//            movie.read();
//            movie.loadPixels();
//            delay[delayCounter % Fixture.ZSIZE] = movie.pixels;
//            int ix = floor(x * (movie.width - 1));
//            int iy = floor(y * (movie.height - 1));
//            return delay[((delayCounter % Fixture.ZSIZE) + (int) map(z,0,1,0,Fixture.ZSIZE-1)) % Fixture.ZSIZE][movie.width * iy + ix];
//            //return movie.pixels[ movie.width * iy + ix ];
//        } else {
//            return 0;
//        }
//    }

}


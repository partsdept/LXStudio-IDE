package lumigeek.lametro.p3lx;

import heronarts.lx.*;
import heronarts.lx.model.LXPoint;
import heronarts.lx.studio.LXStudio;
import heronarts.p3lx.ui.UI;
import heronarts.p3lx.ui.UI3dComponent;
import lumigeek.lametro.lx.VideoPattern;
import org.kabeja.dxf.DXFLine;
import org.kabeja.dxf.DXFPoint;
import org.kabeja.dxf.DXFPolyline;
import org.kabeja.dxf.DXFVertex;
import processing.video.*;
import processing.core.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UIVideoPlane extends UI3dComponent  {

    // @LXCategory("Form")
    // public final class VideoPlayer2 extends LXPattern {

        PImage img;
        Movie movie;
        int[] pixels;

    private File file;

    private int idCounter = 1;  // for LX json export



    public String name() {
        return file.getName();
    }

        public UIVideoPlane(LXStudio lx, LXStudio.UI ui,File f) {
            super();
            file = f;

            //addParameter("axis", this.axis);
            //addParameter("pos", this.pos);
            //addParameter("width", this.wth);
            //img = new PImage(movie.width, movie.height);

            LX.log("About to load: " + f.getName());
            img = ui.applet.loadImage(f.getName());

            VideoPattern.imageHack = img;
       }

       public void loadImage(UI ui, File f) {
//           java.io.InputStream in = null;
//           try {
//               in = new java.io.FileInputStream(f);
//               LX.log("===== FILE: " + f.getName() + " =====");
//               //in = lx.ui.applet.createInput(fn);  // use this for Processing to access files in the data/ directory
//           } catch (Exception e) {
//               LX.error("Could not open DXF file." + e);
//           }

       }



        public Movie loadMovie(UI ui, PGraphics pg, String fn) {




            //movie = new Movie(pg.parent, fn);

            if (movie == null) {

                LX.error(fn + " not found");

            } else {

                movie.loop();
                movie.speed(0.5f);

                while (!(movie.available())) {
                    LX.log("Movie check 1: " + movie.available());
                    pg.parent.delay(5);
                }

                LX.log("Movie check 2: " + movie.available());

                movie.play();
                movie.read();
                movie.loadPixels();

                while (!(movie.available())) {
                    LX.log("Movie check 3: " + movie.available());
                    pg.parent.delay(5);
                }

                // old code to test with a static image instead of a video
                //img = new PImage(movie.width, movie.height);
                //    img = loadImage("grad.jpg");
                //    img.loadPixels();
            }
            return movie;
        }

    public void onDraw(UI ui, PGraphics pg) {

        pg.beginShape();
        pg.texture(img);
        pg.vertex(-100,0,700,0,0);
        pg.vertex(-100,0,-900,0,img.height - 1);
        pg.vertex(200,0,-900,img.width - 1, img.height - 1);
        pg.vertex(200,0,700, img.width - 1, 0);
        pg.endShape();

//        if (movie == null) {
//            //movie = loadMovie(ui,pg,"fgs1.mov");
//        } else {
//            if (visible.getValueb()) {
//                //pg.image(new PImage(movie.getImage()), 0, 0, img.width * 100, img.height * 100);
//            }
//        }
    }


    //TODO: kick this old .pde code out to the forthcoming LXPattern

//    public int videoColorSampleForPoint(LXPoint p) {
//        if (movie.available()) {
//            //LX.log("Movie is: " + movie.width + " by " + movie.height);
//            movie.read();
//            movie.loadPixels();
//            pixels = movie.pixels;
//            //TODO figure out the math to resize and rotate the image and then sample the video
//            int ix = (int) Math.floor(p.x * (movie.width - 1));
//            int iy = (int) Math.floor(p.y * (movie.height - 1));
//            return pixels[movie.width * iy + ix];
//        } else {
//            return 0;
//        }
//    }

//        public void run(double deltaMs) {
//            for (LXPoint p : model.points) {
//                int c = imageLookup(p.zn,p.yn,p.xn);
//                int r = (int) red(c);
//                int g = (int) green(c);
//                int b = (int) blue(c);
//                colors[p.index] = LXColor.rgb(r,g,b);
//            }
//        }


    }
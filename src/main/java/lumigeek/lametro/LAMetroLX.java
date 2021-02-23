/**
 * Copyright 2020- Mark C. Slee, Heron Arts LLC
 *
 * This file is part of the LX Studio software library. By using
 * LX, you agree to the terms of the LX Studio Software License
 * and Distribution Agreement, available at: http://lx.studio/license
 *
 * Please note that the LX license is not open-source. The license
 * allows for free, non-commercial use.
 *
 * HERON ARTS MAKES NO WARRANTY, EXPRESS, IMPLIED, STATUTORY, OR
 * OTHERWISE, AND SPECIFICALLY DISCLAIMS ANY WARRANTY OF
 * MERCHANTABILITY, NON-INFRINGEMENT, OR FITNESS FOR A PARTICULAR
 * PURPOSE, WITH RESPECT TO THE SOFTWARE.
 *
 * @author Mark C. Slee <mark@heronarts.com>
 */

package lumigeek.lametro;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.sun.media.jfxmedia.events.VideoRendererListener;
import heronarts.lx.LX;
import heronarts.lx.LXPlugin;
import heronarts.lx.model.LXModel;
import heronarts.lx.model.LXPoint;
import heronarts.lx.structure.LXFixture;
import heronarts.lx.studio.LXStudio;
import lumigeek.lametro.lx.VisibilityParameterHelper;
import processing.core.PApplet;
import lumigeek.lametro.p3lx.*;


/**
 * This is an example top-level class to build and run an LX Studio
 * application via an IDE. The main() method of this class can be
 * invoked with arguments to either run with a full Processing 3 UI
 * or as a headless command-line only engine.
 */
public class LAMetroLX extends PApplet implements LXPlugin {

    private static final String WINDOW_TITLE = "LX Studio";

    private static int WIDTH = 1280;
    private static int HEIGHT = 800;
    private static boolean FULLSCREEN = false;


    VisibilityParameterHelper visibilityHelper = null;

    private List<File> objFiles = null;
    private List<File> dxfFiles = null;

    @Override
    public void settings() {
        if (FULLSCREEN) {
          fullScreen(PApplet.P3D);
        } else {
          size(WIDTH, HEIGHT, PApplet.P3D);
        }
    }


    List<LXPoint> points = new ArrayList<LXPoint>();
    List<LXFixture> fixtures = new ArrayList<LXFixture>();

    @Override
    public void setup() {
        LXStudio.Flags flags = new LXStudio.Flags(this);
        flags.resizable = true;
        flags.useGLPointCloud = false;
        flags.startMultiThreaded = true;

        //LXModel model = new LXModel();
        //new LXStudio(this, flags,model);

        new LXStudio(this, flags);
        this.surface.setTitle(WINDOW_TITLE);
    }

    @Override
    public void initialize(LX lx) {
      // Here is where you should register any custom components or make modifications
      // to the LX engine or hierarchy. This is also used in headless mode, so note that
      // you cannot assume you are working with an LXStudio class or that any UI will be
      // available.

      // look for data/ directory and get lists of OBJs and DXFs
      // DXFs can be parsed here since they might be used when headless
      // OBJs will be handled below

//      boolean headless = false;  //TODO: REPLACE THIS WITH THE REAL CHECK FOR HEADLESS OR NOT
//      if (!headless) {
          Path workingDirectory = Paths.get("");
          LX.log("Working Directory is: " + workingDirectory);
          LX.log("user.dir is: " + System.getProperty("user.dir"));

          File dataDirectory = new File(System.getProperty("user.dir") + File.separator + "data");
          if (dataDirectory == null) {  //TODO: THIS DOES NOT WORK AS EXPECTED WHEN data DOES NOT EXIST
              LX.error(" directory does not exist in root director");
              objFiles = new ArrayList<>();  // create empty lists to avoid null pointer later
              dxfFiles = new ArrayList<>();
          } else {
              objFiles = getFilesWithExtension(dataDirectory, ".obj");
              dxfFiles = getFilesWithExtension(dataDirectory, ".dxf");
          }



//      }

        visibilityHelper = new VisibilityParameterHelper();
        visibilityHelper.addFoundFiles(objFiles);
        visibilityHelper.addFoundFiles(dxfFiles);

        lx.engine.registerComponent("visibilityHelper",visibilityHelper);

        // Register custom pattern and effect types
        lx.registry.addPattern(heronarts.lx.app.pattern.AppPattern.class);
        lx.registry.addEffect(heronarts.lx.app.effect.AppEffect.class);

    }

    public void initializeUI(LXStudio lx, LXStudio.UI ui) {
        // Here is where you may modify the initial settings of the UI before it is fully
        // built. Note that this will not be called in headless mode. Anything required
        // for headless mode should go in the raw initialize method above.
    }

    public void onUIReady(LXStudio lx, LXStudio.UI ui) {
      UIPropList pl = new UIPropList(lx,ui,ui.leftPane.model.getWidth(),objFiles,visibilityHelper);
      pl.addToContainer(ui.leftPane.model);

      UIDxfList dl = new UIDxfList(lx,ui,ui.leftPane.model.getWidth(),dxfFiles,visibilityHelper);
      dl.addToContainer(ui.leftPane.model);

      ui.preview.setBackgroundColor(lx.applet.color(50));
    }

    private List<File> getFilesWithExtension(File file, String suffix) {
        if (file.isDirectory()) {
            String names[] = file.list();
            List<File> files = new ArrayList<File>();
            for (int i = 0 ; i < names.length ; i++) {
                if (names[i].endsWith(suffix.toLowerCase()) || names[i].endsWith(suffix.toUpperCase())) {
                    files.add(new File((file.getAbsolutePath() + File.separator + names[i])));
                }
            }
            return files;
        } else {
            LX.error("path passed into getFilesWithExtension is not a directory");
            return null;
        }
    }

    @Override
    public void draw() {
        // All handled by core LX engine, do not modify, method exists only so that Processing
        // will run a draw-loop.
    }

    /**
    * Main interface into the program. Two modes are supported, if the --headless
    * flag is supplied then a raw CLI version of LX is used. If not, then we embed
    * in a Processing 3 applet and run as such.
    *
    * @param args Command-line arguments
    */
    public static void main(String[] args) {
        LX.log("Initializing LX version " + LXStudio.VERSION);
        boolean headless = false;
        File projectFile = null;
        for (int i = 0; i < args.length; ++i) {
          if ("--help".equals(args[i]) || "-h".equals(args[i])) {
          } else if ("--headless".equals(args[i])) {
            headless = true;
          } else if ("--fullscreen".equals(args[i]) || "-f".equals(args[i])) {
            FULLSCREEN = true;
          } else if ("--width".equals(args[i]) || "-w".equals(args[i])) {
            try {
              WIDTH = Integer.parseInt(args[++i]);
            } catch (Exception x ) {
              LX.error("Width command-line argument must be followed by integer");
            }
          } else if ("--height".equals(args[i]) || "-h".equals(args[i])) {
            try {
              HEIGHT = Integer.parseInt(args[++i]);
            } catch (Exception x ) {
              LX.error("Height command-line argument must be followed by integer");
            }
          } else if (args[i].endsWith(".lxp")) {
            try {
              projectFile = new File(args[i]);
            } catch (Exception x) {
              LX.error(x, "Command-line project file path invalid: " + args[i]);
            }
          }
        }
        if (headless) {
          // We're not actually going to run this as a PApplet, but we need to explicitly
          // construct and set the initialize callback so that any custom components
          // will be run
          LX.Flags flags = new LX.Flags();
          flags.initialize = new LAMetroLX();
          if (projectFile == null) {
            LX.log("WARNING: No project filename was specified for headless mode!");
          }
          LX.headless(flags, projectFile);
        } else {
          PApplet.main("lumigeek.lametro.LAMetroLX", args);
        }
    }

}

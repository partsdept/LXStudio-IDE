package lumigeek.lametro.p3lx;

import com.google.gson.*;
import heronarts.lx.LX;
import heronarts.lx.model.LXPoint;
import heronarts.lx.parameter.BooleanParameter;
import heronarts.lx.parameter.BoundedParameter;
import heronarts.lx.parameter.DiscreteParameter;
import heronarts.lx.transform.LXVector;
import heronarts.p3lx.ui.UI;
import heronarts.p3lx.ui.UI3dComponent;
import org.kabeja.dxf.*;
import org.kabeja.dxf.helpers.Point;
import org.kabeja.parser.DXFParser;
import processing.core.PGraphics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import lumigeek.lametro.lx.JsonStripFixtureHelper;

public class UIDxf extends UI3dComponent {

// THIS ISN'T NECESSARY BECAUSE visible IS INHERITED FROM UI3dComponent
//    public final BooleanParameter dxfVisible =
//            new BooleanParameter("Visible", true)
//                    .setDescription("Whether dxf is visible in the simulation");


    public final BooleanParameter pixelMode =
            new BooleanParameter("Mode", true)
                    .setDescription("Whether pixels are drawn by count or by spacing ");

    public final DiscreteParameter pixelCount =
            new DiscreteParameter("Count",30,2,99)
                    .setDescription("Count of pixels on a given DXF line");

    public final BoundedParameter pixelSpacing =
            new BoundedParameter("Spacing", 4.0,1.0,100.0)
                    .setDescription("Spacing of pixels on a given DXF line");

    public int pixelCountToFit = 1;

    private File file;

    private int idCounter = 1;  // for LX json export

    private List<DXFLine> lines = new ArrayList<DXFLine>();
    private List<DXFPolyline> polylines = new ArrayList<DXFPolyline>();

    public UIDxf(File f) {
        super();
        file = f;
        parseFile(file);
    }

    public String name() {
        return file.getName();
    }

    public void onDraw(UI ui, PGraphics pg) {
        if (visible.getValueb()) {
            super.onDraw(ui,pg);

            for(int i = 0 ; i < lines.size() ; i++) {
                DXFLine dl = lines.get(i);
                // draw lines of DXF for debugging.  note the Y/Z axis swap
                pg.stroke(pg.color(255,0,255));
                pg.strokeWeight(1);
                LXPoint lxStartPoint = convertDXFCoordinatestoLXPoint(dl.getStartPoint());
                LXPoint lxEndPoint = convertDXFCoordinatestoLXPoint(dl.getEndPoint());
                pg.line( lxStartPoint.x ,lxStartPoint.y,lxStartPoint.z ,lxEndPoint.x  ,lxEndPoint.y,lxEndPoint.z);
                pg.noStroke();
                pg.fill(pg.color(255,0,255));
                LXVector startVector = new LXVector(lxStartPoint);
                LXVector endVector = new LXVector(lxEndPoint);
                LXVector directionVector = endVector.sub(startVector);
                LXVector spacingVector;

                pixelCountToFit = (int) Math.floor(directionVector.mag() / pixelSpacing.getValuef());

                pg.push();
                pg.fill(pg.color(255,255,0));
                pg.translate(startVector.x,startVector.y,startVector.z);
                pg.sphere(0.5f);
                if (pixelMode.getValueb()) {
                    spacingVector = directionVector.div((float) (pixelCount.getValuei() - 1));
                    // do nothing to the spacing vector
                    for(int j = 0 ; j < pixelCount.getValuei() - 1 ; j++) {
                        pg.translate(spacingVector.x,spacingVector.y,spacingVector.z);
                        pg.sphere(0.5f);
                    }
                } else {
                    spacingVector = directionVector.normalize();
                    spacingVector = spacingVector.mult(pixelSpacing.getValuef());
                    for(int j = 0 ; j < pixelCountToFit ; j++) {
                        pg.translate(spacingVector.x,spacingVector.y,spacingVector.z);
                        pg.sphere(0.5f);
                    }
                }
                pg.pop();

                //LXFixture f = new StripFixture(ui.lx);

            }

            pg.stroke(pg.color(0,255,255));
            for(int i = 0 ; i < polylines.size() ; i++) {
                DXFPolyline dl = polylines.get(i);
                Iterator<DXFVertex> verticies = dl.getVertexIterator();
                DXFVertex startVertex = verticies.next();
                //LX.log("polyline start: " + startVertex.getPoint());
                DXFVertex prevVertex = startVertex;
                DXFVertex currentVertex = startVertex;
                DXFVertex endVertex = startVertex;
                while (verticies.hasNext()) {
                    currentVertex = verticies.next();
                    pg.line((float) prevVertex.getX(),(float) prevVertex.getZ(),(float) prevVertex.getY() * -1.0f ,(float) currentVertex.getX(),(float) currentVertex.getZ(),(float) currentVertex.getY() * -1.0f);
                    endVertex = currentVertex;
                    if (verticies.hasNext()) {
                        //LX.log("polyline next: " + currentVertex.getPoint());
                        prevVertex = currentVertex;
                    }
                }
                //LX.log("polyline end: " + endVertex.getPoint());
            }
        }
    }

    public void exportJSON(String filename) {
        Gson gson = new Gson();
        JsonArray ja = new JsonArray(lines.size());

        Iterator li = lines.iterator();
        while (li.hasNext()) {
            DXFLine dl = (DXFLine) li.next();
            LXPoint sp = convertDXFCoordinatestoLXPoint(dl.getStartPoint());
            LXPoint ep = convertDXFCoordinatestoLXPoint(dl.getEndPoint());
            double yaw = -1.0f * Math.atan2(ep.z - sp.z, (ep.x - sp.x));
            double pitch = Math.atan2(ep.y - sp.y, Math.sqrt(Math.pow(ep.x - sp.x,2) + Math.pow(ep.z - sp.z,2)));
            double roll = 0; // it's effectively irrelevant for a line-vector
            LX.log("== LINE:  " + sp.x + "," + sp.y + "," + sp.z + "  rotation: " + Math.toDegrees(yaw) + "," + Math.toDegrees(pitch) + "," + Math.toDegrees(roll)  + " count: " + pixelCountToFit + " spacing: " + pixelSpacing.getValuef());

            LXVector startVector = new LXVector(sp);
            LXVector endVector = new LXVector(ep);
            LXVector directionVector = endVector.sub(startVector);
            LXVector spacingVector;

            pixelCountToFit = (int) Math.floor(directionVector.mag() / pixelSpacing.getValuef());

            JsonStripFixtureHelper jfh = new JsonStripFixtureHelper();
            jfh.id = "" + idCounter++;
            jfh.parameters.label = "Strip Fixture " + jfh.id;
            jfh.parameters.x = "" + sp.x;
            jfh.parameters.y = "" + sp.y;
            jfh.parameters.z = "" + sp.z;
            jfh.parameters.yaw = "" + Math.toDegrees(yaw);
            jfh.parameters.pitch = "" + Math.toDegrees(pitch);
            jfh.parameters.roll = "" + Math.toDegrees(roll);
            jfh.parameters.numPoints = "" + pixelCountToFit;
            jfh.parameters.spacing = "" + pixelSpacing.getValuef();

            JsonObject jo = (JsonObject)  gson.toJsonTree(jfh);
            // had to add LX property 'class' this way because reserved could not be an attribute name in the helper
            jo.addProperty("class","heronarts.lx.structure.StripFixture");
            // add fixture helper to array
            ja.add(jo);

        }

        JsonObject topLevelJson = new JsonObject();
        topLevelJson.add("fixtures",ja);
        LX.log(gson.toJson(topLevelJson));

        Path workingDirectory = Paths.get("");
        File modelsDirectory = new File(workingDirectory.toAbsolutePath() + "/Models");
        if (modelsDirectory == null) {
            LX.error(" models directory does not exist in project root directory");
        } else {

            File f = new File(workingDirectory.toAbsolutePath() + "/Models" + "/" + filename);
            try {
                FileWriter out = new FileWriter(f);
                out.write(gson.toJson(topLevelJson));
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private LXPoint convertDXFCoordinatestoLXPoint(Point p) {
        return convertDXFCoordinatestoLXPoint(p.getX(),p.getY(),p.getZ());
    }

    private LXPoint convertDXFCoordinatestoLXPoint(DXFPoint p) {
        return convertDXFCoordinatestoLXPoint(p.getX(),p.getY(),p.getZ());
    }

    private LXPoint convertDXFCoordinatestoLXPoint(DXFVertex v) {
        return convertDXFCoordinatestoLXPoint(v.getX(),v.getY(),v.getZ());
    }

    private LXPoint convertDXFCoordinatestoLXPoint(double x, double y, double z) {
        return new LXPoint(x,z,y * -1.0);
    }

    private void parseFile(File f) {

        java.io.InputStream in = null;
        try {
            in = new java.io.FileInputStream(f);
            //in = lx.ui.applet.createInput(fn);  // use this for Processing to access files in the data/ directory
        } catch (Exception e) {
            System.out.println("Could not open DXF file." + e);
        }

        org.kabeja.parser.Parser parser = org.kabeja.parser.ParserBuilder.createDefaultParser();
        try {
            parser.parse(in, DXFParser.DEFAULT_ENCODING);
            DXFDocument doc = parser.getDocument();
            Iterator<DXFLayer> layers = doc.getDXFLayerIterator();
            //DXFLayer layer = doc.getDXFLayer("03_LED_STRANDS");   // used to just get layer "0" but now iterate across all layers
            while(layers.hasNext()) {
                //get all polylines from the layer
                DXFLayer layer = layers.next();   // was layer "0"
                java.util.List<DXFLine> ls = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_LINE);
                ListIterator<DXFLine> ll = ((ls != null) ? ls.listIterator() : null);
                java.util.List<DXFPolyline> pls = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);
                ListIterator<DXFPolyline> pll = ((pls != null) ? pls.listIterator() : null);

                LX.log("layer: " + layer.getName());
                LX.log("line count: " + ((ls != null) ? ls.size() : "null"));
                LX.log("polyline count: " + ((pls != null) ? pls.size() : "null"));

                if (ll != null) {
                    DXFLine tempLine;
                    org.kabeja.dxf.helpers.Point startPoint;
                    org.kabeja.dxf.helpers.Point endPoint;
                    while (ll.hasNext()) {
                        tempLine = (DXFLine) ll.next();
                        lines.add(tempLine);
                        // do something with start and end points of line...
                        startPoint = tempLine.getStartPoint();
                        endPoint = tempLine.getEndPoint();
                        LX.log("line start: " + startPoint);
                        LX.log("line end: " + endPoint);
                        LXVector slxv = new LXVector((float) startPoint.getX(),(float) startPoint.getY(),(float) startPoint.getZ());
                        LXVector elxv = new LXVector((float) endPoint.getX(),(float) endPoint.getY(),(float) endPoint.getZ());
                    }
                }

                // TODO: CREATE A CUSTOM FIXTURE WITH AN ARRAY OF STRIPS FOR EACH SEGMENT OF A POLYLINE
                if (pll != null) {
                    DXFPolyline tempPolyline = pll.next();
                    polylines.add(tempPolyline);
                    // do something with each point of polyline...
                    Iterator<DXFVertex> verticies = tempPolyline.getVertexIterator();
                    DXFVertex startVertex = verticies.next();
                    LX.log("polyline start: " + startVertex.getPoint());
                    DXFVertex endVertex = startVertex;
                    while (verticies.hasNext()) {
                        DXFVertex nextVertex = verticies.next();
                        endVertex = nextVertex;
                        if (verticies.hasNext()) {
                            LX.log("polyline next: " + nextVertex.getPoint());
                        }
                    }
                    LX.log("polyline end: " + endVertex.getPoint());
                }
            }
        } catch (org.kabeja.parser.ParseException e) {
            e.printStackTrace();
        }
    }
}

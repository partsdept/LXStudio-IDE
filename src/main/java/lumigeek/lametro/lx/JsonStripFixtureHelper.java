package lumigeek.lametro.lx;

public class JsonStripFixtureHelper {

    public String id = "123";
    //String class = "heronarts.lx.structure.StripFixture"; // added this property via json object due to reserved word
    public Parameters parameters = new Parameters();

    public class Parameters {
        public String label = "DXF Strip";
        public String x = "0.0";
        public String y = "0.0";
        public String z = "0.0";
        public String  yaw = "0.0";
        public String  pitch = "0.0";
        public String  roll = "0.0";
        public String  selected = "false";
        public String  deactivate = "false";
        public String  enabled = "true";
        public String  brightness = "1.0";
        public String  identify  = "false";
        public String  mute = "false";
        public String  solo = "false";
        public String  protocol = "0";
        public String  byteOrder = "0";
        public String  transport = "0";
        public String  reverse = "false";
        public String  host = "127.0.0.1";
        public String  port = "7890";
        public String  artNetUniverse = "0";
        public String  opcChannel = "0";
        public String  ddpDataOffset = "0";
        public String  kinetPort = "1";
        public String  numPoints = "30";
        public String  spacing = "10";
    }
}

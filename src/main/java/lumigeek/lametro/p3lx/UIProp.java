package lumigeek.lametro.p3lx;

import heronarts.p3lx.P3LX;
import heronarts.p3lx.ui.component.UIPShape;
import java.io.File;

public class UIProp extends UIPShape {

// THIS ISN'T NECESSARY BECAUSE visible IS INHERITED FROM UI3dComponent
//    public final BooleanParameter propVisible =
//            new BooleanParameter("Visible", true)
//                    .setDescription("Whether prop is visible in the simulation");

    private File file;

    public UIProp(P3LX lx, File f) {
        super(lx,f.getAbsolutePath());
        file = f;

    }

    public String name() {
        return file.getName();
    }

// THIS ISN'T NECESSARY BECAUSE visible == false WILL DISABLE LOOP TASKS LIKE DRAW
//    public void onDraw(UI ui, PGraphics pg) {
//        if (visible.getValueb()) {
//            super.onDraw(ui,pg);
//        }
//    }

}

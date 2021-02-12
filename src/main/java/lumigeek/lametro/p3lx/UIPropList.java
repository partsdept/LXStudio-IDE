package lumigeek.lametro.p3lx;

import heronarts.lx.LX;
import heronarts.lx.studio.LXStudio;
import heronarts.p3lx.P3LX;
import heronarts.p3lx.ui.component.UIButton;
import heronarts.p3lx.ui.component.UICollapsibleSection;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UIPropList extends UICollapsibleSection {

    List<UIProp> props = new ArrayList<UIProp>();

    public UIPropList(P3LX lx, LXStudio.UI ui, float w, List<File> objs) {
        super(ui, 0, 0, w, 32);
        setTitle("OBJ PROPS");
        if (objs != null) {
            for (int i = 0; i < objs.size(); i++) {
                File f = objs.get(i);
                LX.log("Adding prop for file: " + f.getName());
                UIProp p = new UIProp(lx, f);
                props.add(p);
                ui.preview.addComponent(p);
                new UIButton(0, i * 18, w - 8, 16)
                        .setParameter(p.visible)
                        .setLabel(p.name())
                        .setActiveColor(ui.theme.getSecondaryColor())
                        .addToContainer(this);
            }
            setContentHeight(18 * objs.size());
        }
    }
}
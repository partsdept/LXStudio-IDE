package lumigeek.lametro.p3lx;

import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.studio.LXStudio;
import heronarts.p3lx.ui.component.*;
import lumigeek.lametro.lx.VisibilityParameterHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UIDxfList extends UICollapsibleSection {

    List<UIDxf> uidxfs = new ArrayList<UIDxf>();

    public UIDxfList(LXStudio lx, LXStudio.UI ui, float w, List<File> dxfs, VisibilityParameterHelper vh) {
        super(ui, 0, 0, w, 32);
        setTitle("DXF LED STRIPS");
        if (dxfs != null) {
            for (int i = 0; i < dxfs.size(); i++) {
                File f = dxfs.get(i);
                UIDxf d = new UIDxf(f);
                uidxfs.add(d);
                ui.preview.addComponent(d);
                d.setVisible(false);

                int WIDTH_J_BUTTON = 16;
                int WIDTH_PADDING = 2;
                int WIDTH_SPACING_FIELD = 20;
                int WIDTH_COUNT_FIELD = 20;

                UIButton dxfb = (UIButton) new UIButton(0, i * 18, w - (WIDTH_SPACING_FIELD + WIDTH_COUNT_FIELD + WIDTH_J_BUTTON + WIDTH_J_BUTTON + (8 * WIDTH_PADDING)), 16)
                        .setLabel(d.name())
                        .setParameter(d.visible)
                        .setActiveColor(ui.theme.getSecondaryColor())
                        .addToContainer(this);

                UIIntegerBox cb = (UIIntegerBox) new UIIntegerBox(dxfb.getX() + dxfb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_COUNT_FIELD, 16)
                        .setValue(30)
                        .setParameter(d.pixelCount)
                        .addToContainer(this);

                UIDoubleBox sb = (UIDoubleBox) new UIDoubleBox(cb.getX() + cb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_SPACING_FIELD, 16)
                        .setValue(4.0)
                        .setParameter(d.pixelSpacing)
                        .addToContainer(this)
                        .setFontColor(LXColor.rgb(255, 255, 0));

                UIButton mb = (UIButton) new UIButton(sb.getX() + sb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_J_BUTTON, 16) {
                    public void onToggle(boolean b) {
                        if (!d.pixelMode.getValueb()) {
                            cb.setFontColor(LXColor.rgb(255, 255, 0));
                            sb.setFontColor(LXColor.gray(255));
                            cb.setValue(d.pixelCountToFit + 1);
                        } else {
                            sb.setFontColor(LXColor.rgb(255, 255, 0));
                            cb.setFontColor(LXColor.gray(255));
                            sb.setValue(d.pixelSpacing.getValuef());
                        }
                    }
                }
                        .setLabel("C")
                        .setParameter(d.pixelMode)
                        .setActiveColor(ui.theme.getSecondaryColor())
                        .addToContainer(this);

                new UIButton(mb.getX() + mb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_J_BUTTON, 16) {
                    public void onClick() {
                        LX.log("Pressed JSON Export button for: " + f.getName());
                        d.exportJSON(f.getName() + ".lxm");
                    }
                }
                        .setLabel("J")
                        .setMomentary(true)
                        .setActiveColor(ui.theme.getAttentionColor())
                        .setInactiveColor(ui.theme.getAttentionColor())
                        .addToContainer(this);


                vh.attachParameterForFilename(f.getName(),d.visible);

            }
            setContentHeight(18 * dxfs.size());
        }
    }
}
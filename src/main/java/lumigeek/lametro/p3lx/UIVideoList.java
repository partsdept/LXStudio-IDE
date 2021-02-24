package lumigeek.lametro.p3lx;

import heronarts.lx.LX;
import heronarts.lx.color.LXColor;
import heronarts.lx.studio.LXStudio;
import heronarts.p3lx.ui.UI;
import heronarts.p3lx.ui.component.*;
import lumigeek.lametro.lx.VisibilityParameterHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UIVideoList extends UICollapsibleSection {

    List<UIVideoPlane> vids = new ArrayList<UIVideoPlane>();

    public UIVideoList(LXStudio lx, LXStudio.UI ui, float w, List<File> vfiles, VisibilityParameterHelper vh) {
        super(ui, 0, 0, w, 32);
        setTitle("IMAGE FILES");
        if (vfiles != null) {
            for (int i = 0; i < vfiles.size(); i++) {
                File f = vfiles.get(i);
                UIVideoPlane vp = new UIVideoPlane(lx,ui,f);
                vids.add(vp);
                ui.preview.addComponent(vp);
                vp.setVisible(false);

                int WIDTH_J_BUTTON = 64;
                int WIDTH_PADDING = 2;

                UIButton dxfb = (UIButton) new UIButton(0, i * 18, w - (2 * WIDTH_PADDING), 16)
                        .setLabel(vp.name())
                        .setParameter(vp.visible)
                        .setActiveColor(ui.theme.getSecondaryColor())
                        .addToContainer(this);

//                UIIntegerBox cb = (UIIntegerBox) new UIIntegerBox(dxfb.getX() + dxfb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_COUNT_FIELD, 16)
//                        .setValue(30)
//                        .setParameter(d.pixelCount)
//                        .addToContainer(this);
//
//                UIDoubleBox sb = (UIDoubleBox) new UIDoubleBox(cb.getX() + cb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_SPACING_FIELD, 16)
//                        .setValue(4.0)
//                        .setParameter(d.pixelSpacing)
//                        .addToContainer(this)
//                        .setFontColor(LXColor.rgb(255, 255, 0));
//
//                UIButton mb = (UIButton) new UIButton(sb.getX() + sb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_J_BUTTON, 16) {
//                    public void onToggle(boolean b) {
//                        if (!d.pixelMode.getValueb()) {
//                            cb.setFontColor(LXColor.rgb(255, 255, 0));
//                            sb.setFontColor(LXColor.gray(255));
//                            cb.setValue(d.pixelCountToFit + 1);
//                        } else {
//                            sb.setFontColor(LXColor.rgb(255, 255, 0));
//                            cb.setFontColor(LXColor.gray(255));
//                            sb.setValue(d.pixelSpacing.getValuef());
//                        }
//                    }
//                }
//                        .setLabel("C")
//                        .setParameter(d.pixelMode)
//                        .setActiveColor(ui.theme.getSecondaryColor())
//                        .addToContainer(this);

//                new UIButton(dxfb.getX() + dxfb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_J_BUTTON, 16) {
//                    public void onClick() {
//                        LX.log("Pressed JSON Export button for: " + f.getName());
//                        d.exportJsonCustomFixtures(f.getName() + ".lxf");
//                    }
//                }
//                        .setLabel("Export Json")
//                        .setMomentary(true)
//                        .setActiveColor(ui.theme.getAttentionColor())
//                        .setInactiveColor(ui.theme.getAttentionColor())
//                        .addToContainer(this);


//                new UIButton(mb.getX() + mb.getWidth() + WIDTH_PADDING, i * 18, WIDTH_J_BUTTON, 16) {
//                    public void onClick() {
//                        LX.log("Pressed JSON Export button for: " + f.getName());
//                        d.exportJsonStrips(f.getName() + ".lxm");
//                    }
//                }
//                        .setLabel("J")
//                        .setMomentary(true)
//                        .setActiveColor(ui.theme.getAttentionColor())
//                        .setInactiveColor(ui.theme.getAttentionColor())
//                        .addToContainer(this);


                vh.attachParameterForFilename(f.getName(),vp.visible);

            }
            setContentHeight(18 * vfiles.size());
        }
    }


}
/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Container;
import javax.swing.JPanel;

public class PrefsPanel extends JPanel implements UpdateReq
{
    FieldVal width;
    FieldVal height;
    FieldC xylink;
    FieldS oversampling;
    FieldVal fiFramesPrev;
    FieldVal fiFramesRender;
    FieldS alignhorz;
    FieldCol bkcolor;
    ParamC pxylink;
    FieldS exportoption;
    FieldVal duration;
    FieldVal loop;
    FieldC bidir;

    public PrefsPanel(ProfileReader ppr)
    {
        ppr.SetSection("Prefs");
        this.setLayout(null);
        this.setSize(500, 500);
        this.width =
            new FieldVal(this, 20, 20, ppr.ReadString("width", "Width:"), 140, 120, false, 1.0, 512.0, this, 2);
        this.width.SetEditMode(true);
        this.pxylink = new ParamC(1);
        this.xylink = new FieldC(this, 160, 47, ppr.ReadString("link", "Link"), 120, this, 0);
        this.xylink.Setup(this.pxylink);
        this.height =
            new FieldVal(this, 20, 74, ppr.ReadString("height", "Height:"), 140, 120, false, 1.0, 512.0, this, 3);
        this.height.SetEditMode(true);
        this.oversampling = new FieldS(
            (Container)this, 20, 105, 28, ppr.ReadString("oversampling", "OverSampling:"), 140, 120,
            new String[] {ppr.ReadString("x1", "x1"), ppr.ReadString("x2", "x2"), ppr.ReadString("x4", "x4")},
            (UpdateReq)this, 49152);
        this.fiFramesPrev = new FieldVal(this, 20, 140, ppr.ReadString("prevframes", "Preview Frames:"), 140, 120,
                                         false, 1.0, 1000.0, this, 16384);
        this.fiFramesPrev.SetEditMode(true);
        this.fiFramesRender = new FieldVal(this, 20, 170, ppr.ReadString("renderframes", "Render Frames:"), 140, 120,
                                           false, 1.0, 1000.0, this, 16384);
        this.fiFramesRender.SetEditMode(true);
        this.alignhorz = new FieldS(
            (Container)this, 20, 200, 28, ppr.ReadString("framealign", "FrameAlign:"), 140, 120,
            new String[] {ppr.ReadString("vertical", "Vertical"), ppr.ReadString("horizontal", "Horizontal")},
            (UpdateReq)this, 49152);
        this.bkcolor = new FieldCol(this, 20, 230, ppr.ReadString("backcolor", "BackColor:"), this, 4);
        this.exportoption =
            new FieldS((Container)this, 330, 35, 28, ppr.ReadString("exportopt", "Export Option:"), 140, 140,
                       new String[] {ppr.ReadString("stitched", "Stitched Frame"),
                                     ppr.ReadString("individual", "Individual Files"),
                                     ppr.ReadString("anim", "Animation (gif/png)")},
                       (UpdateReq)this, 0);
        this.duration = new FieldVal(this, 330, 70, ppr.ReadString("duration", "Duration(mSec):"), 140, 100, false, 0.0,
                                     2000.0, this, 0);
        this.loop = new FieldVal(this, 330, 100, ppr.ReadString("loop", "Loop(0forInfinite):"), 140, 100, false, 0.0,
                                 200.0, this, 0);
        this.bidir = new FieldC(this, 470, 130, ppr.ReadString("shuttle", "Shuttle Anim"), 220, this, 0);
        this.Setup();
    }

    public void Setup()
    {
        Prefs prefs = Control.getInstance().prefs;
        this.width.Setup(prefs.pwidth);
        this.height.Setup(prefs.pheight);
        this.oversampling.Setup(prefs.oversampling);
        this.fiFramesPrev.Setup(prefs.priFramesPrev);
        this.fiFramesRender.Setup(prefs.priFramesRender);
        this.alignhorz.Setup(prefs.alignhorz);
        this.bkcolor.Setup(prefs.bkcolor);
        this.exportoption.Setup(prefs.exportoption);
        this.duration.Setup(prefs.duration);
        this.loop.Setup(prefs.loop);
        this.bidir.Setup(prefs.bidir);
        if (GUIEditor.getInstance().prevpanel != null)
        {
            GUIEditor.getInstance().prevpanel.SetBg(this.bkcolor.pc.col);
        }
    }

    public void xylinkset()
    {
        if (this.width.pi.val == this.height.pi.val)
        {
            this.pxylink.val = 1;
            this.xylink.Setup(this.pxylink);
        }
        else
        {
            this.pxylink.val = 0;
            this.xylink.Setup(this.pxylink);
        }
    }

    @Override public void Update(int m)
    {
        Control.getInstance().Edit();
        if ((m & 0xC000) == 49152)
        {
            Control.getInstance().Update(Control.UpPrefParam);
            return;
        }
        if ((m & 2) != 0)
        {
            if (this.pxylink.val != 0)
            {
                if ((m & 1) == 0)
                {
                    this.height.pi.val = this.width.pi.val;
                    this.height.Setup(this.height.pi);
                }
                else
                {
                    this.width.pi.val = this.height.pi.val;
                    this.width.Setup(this.width.pi);
                }
            }
            if ((m & 0x8000) != 0)
            {
                Control.getInstance().Update(Control.UpPrefParam);
            }
        }
        if ((m & 4) != 0)
        {
            GUIEditor.getInstance().prevpanel.SetBg(this.bkcolor.pc.col);
        }
        if ((m & 8) != 0)
        {
            Control.getInstance().Update(Control.Up_Render);
        }
    }
}

/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Color;
import java.util.ArrayList;

public class Render extends Thread
{
    public int renderframe = 9999;
    public int renderlayer = 9999;
    Control ctl;
    Prefs prefs;
    int exitflag;
    RenderReq renderreq;
    ArrayList<Layer> layers;

    public Render(Control ctl)
    {
        this.ctl = ctl;
        this.prefs = ctl.prefs;
        this.layers = ctl.layers;
        this.exitflag = 0;
        this.renderreq = ctl.renderreq;
    }

    public void Redraw()
    {
        Layer ly;
        int i;
        for (i = 0; i < this.ctl.iMaxLayer; ++i)
        {
            ly = this.layers.get(i);
            if (ly.pcSolo.val != 0)
                break;
        }
        if (i == this.ctl.iMaxLayer)
        {
            for (i = 0; i < this.ctl.iMaxLayer; ++i)
            {
                ly = this.layers.get(i);
                ly.visible = ly.pcVisible.val != 0 ? 1 : 0;
            }
        }
        else
        {
            for (i = 0; i < this.ctl.iMaxLayer; ++i)
            {
                ly = this.layers.get(i);
                ly.visible = ly.pcSolo.val != 0 ? 1 : 0;
            }
        }
        this.renderreq.RunReq();
    }

    public void Stop()
    {
        this.renderreq.BreakReq();
    }

    public void Exit()
    {
        this.renderframe = 9999;
        this.renderlayer = 9999;
        this.exitflag = 1;
    }

    public void exec()
    {
        while (true)
        {
            this.renderreq.Wait();
            try
            {
                int iExport = Control.getInstance().iExporting;
                Control.getInstance().iExporting = 0;
                for (int frame = 0; frame < this.prefs.frames; ++frame)
                {
                    for (int layer = 0; layer < this.layers.size(); ++layer)
                    {
                        int py;
                        int px;
                        if (this.prefs.alignhorz.val == 1)
                        {
                            px = this.prefs.width * frame;
                            py = 0;
                        }
                        else
                        {
                            px = 0;
                            py = this.prefs.height * frame;
                        }
                        if (layer == 0)
                        {
                            this.ctl.imgRender.ClearRect(px, py, this.prefs.width, this.prefs.height,
                                                         new Color(0, 0, 0, 0));
                        }
                        Layer ly = this.layers.get(layer);
                        if (ly.visible != 0)
                        {
                            ly.RenderFrame(this.ctl.imgRender, true, px, py, this.prefs.width, this.prefs.height, frame,
                                           this.prefs.frames - 1, true);
                        }
                        if (iExport == 0 && this.renderreq.BreakCheck())
                            break;
                    }
                    if (iExport == 0 && this.renderreq.BreakCheck())
                        break;
                    if (this.ctl.progress != null)
                    {
                        this.ctl.progress.SetProgress(frame, this.prefs.frames);
                    }
                    GUIEditor.getInstance().UpdatePreview(this.ctl.imgRender);
                }
                this.ctl.RenderingDone(iExport);
                GUIEditor.getInstance().ticon.Update(this.ctl.imgRender, this.ctl.prefs.width, this.ctl.prefs.height,
                                                     this.ctl.prefs.oversampling.val, this.ctl.prefs.frames,
                                                     this.ctl.prefs.alignhorz.val);
                GUIEditor.getInstance().UpdatePreview(this.ctl.imgRender);
            }
            catch (Exception exception)
            {
                continue;
            }
            break;
        }
    }

    @Override public void run()
    {
        this.exec();
    }
}

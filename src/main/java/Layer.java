/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Graphics;

public class Layer implements UpdateReq
{
    int visible = 1;
    String name;
    Control ctl;
    Prefs prefs;
    Primitive prim;
    Eff eff;
    Bitmap imgprevf;
    Bitmap imgprevt;
    ParamC pcVisible = new ParamC(1);
    ParamC pcSolo = new ParamC(0);
    int iIsAnim;
    PreviewRender pr;
    Thread prth;

    public Layer(Control ctl, boolean bActive)
    {
        this.ctl = ctl;
        this.prefs = ctl.prefs;
        this.prim = new Primitive(this.prefs, this, bActive);
        this.eff = new Eff(ctl, this);
        if (bActive)
        {
            this.imgprevf = new Bitmap(this.prefs.width, this.prefs.height);
            this.imgprevt = new Bitmap(this.prefs.width, this.prefs.height);
        }
        else
        {
            this.imgprevt = null;
            this.imgprevf = null;
        }
        this.name = null;
        this.pr = new PreviewRender();
    }

    public void CopyFrom(Layer ly)
    {
        this.visible = ly.visible;
        this.name = ly.name == null ? null : new String(ly.name);
        this.prim.CopyFrom(ly.prim);
        this.eff.CopyFrom(ly.eff);
        this.pcVisible.val = ly.pcVisible.val;
        this.pcSolo.val = ly.pcSolo.val;
    }

    public Layer Clone(boolean bActive)
    {
        Layer ly = new Layer(this.ctl, bActive);
        ly.visible = this.visible;
        ly.name = this.name == null ? null : new String(this.name);
        ly.prim.CopyFrom(this.prim);
        ly.eff.CopyFrom(this.eff);
        ly.pcVisible.val = this.pcVisible.val;
        ly.pcSolo.val = this.pcSolo.val;
        return ly;
    }

    public void SetSize()
    {
        Prefs prefs = Control.getInstance().prefs;
        this.imgprevf = new Bitmap(prefs.width, prefs.height);
        this.imgprevt = new Bitmap(prefs.width, prefs.height);
        this.prim.SetSize(prefs);
        this.Update(0);
    }

    public void UpdateLayerPreview()
    {
        this.pr.Refresh();
    }

    @Override public void Update(int m)
    {
        this.RenderFrame(this.imgprevf, false, 0, 0, this.prefs.width, this.prefs.height, 0, 1, false);
        if (!this.CheckAnim())
        {
            this.imgprevt.CopyFrom(this.imgprevf, this.prefs.width, this.prefs.height);
        }
        else
        {
            this.RenderFrame(this.imgprevt, false, 0, 0, this.prefs.width, this.prefs.height, 1, 1, false);
        }
        if (this.ctl.GetCurrentLayer() == this)
        {
            GUIEditor.getInstance().Update(m);
        }
    }

    public void LoadImage(String path)
    {
        if (this.prim.framealign.val == 2)
        {
            TextCounter tc = new TextCounter(path);
            int y = 0;
            Bitmap bmp1 = new Bitmap(tc.GetCurrent());
            tc.GetNext();
            if (bmp1.img == null)
            {
                this.prim.bmpImage = null;
                Dlg.Error(GUIEditor.getInstance().GetMsgStr("imgformaterr"));
                return;
            }
            int dy = bmp1.height;
            Bitmap bmp = new Bitmap(bmp1.width, bmp1.height * this.prim.numframe.val);
            Graphics g = bmp.img.getGraphics();
            int i = 0;
            while (true)
            {
                if (bmp1.img != null)
                {
                    g.drawImage(bmp1.img, 0, y, null);
                }
                if (++i >= this.prim.numframe.val)
                    break;
                y += dy;
                bmp1 = new Bitmap(tc.GetCurrent());
                tc.GetNext();
            }
            this.prim.bmpImage = bmp;
        }
        else
        {
            Bitmap bmp = new Bitmap(path);
            if (bmp.img == null)
            {
                bmp = GUIEditor.getInstance().bmpNone;
                Dlg.Error(GUIEditor.getInstance().GetMsgStr("imgformaterr"));
            }
            this.prim.bmpImage = bmp;
        }
        this.prim.SetSize(this.prefs);
    }

    public boolean CheckAnim()
    {
        if (this.prim.CheckAnim())
        {
            return true;
        }
        return this.eff.CheckAnim();
    }

    public synchronized void RenderFrame(Bitmap imgDest, boolean add, int dx, int dy, int dw, int dh, int frame,
                                         int total, boolean framemask)
    {
        int endFrame;
        int startFrame;
        int totalFrame;
        float f;
        if (this.eff.fmaskena.val == 1 &&
            ((double)(f = total > 0 ? (float)(frame * 100) / (float)total : 0.0f) < this.eff.fmaskstart.val ||
             (double)f > this.eff.fmaskstop.val))
        {
            return;
        }
        if (this.eff.fmaskena.val == 2)
        {
            int l = this.eff.fmaskbits.val.length();
            int p = (int)((double)frame / (double)total * (double)l);
            if (p >= l)
            {
                p = l - 1;
            }
            if (p >= 0 && this.eff.fmaskbits.val.charAt(p) == '0')
            {
                return;
            }
        }
        if (this.eff.unfold.val != 0)
        {
            if (this.eff.animstep.val == 0)
            {
                totalFrame = total + 1;
                startFrame = 0;
                endFrame = total;
            }
            else
            {
                totalFrame = this.eff.animstep.val;
                startFrame = 0;
                endFrame = this.eff.animstep.val - 1;
            }
        }
        else if (this.eff.animstep.val == 0)
        {
            totalFrame = total + 1;
            startFrame = endFrame = frame;
        }
        else
        {
            totalFrame = this.eff.animstep.val;
            if (total > 0)
            {
                startFrame = endFrame = frame * (this.eff.animstep.val - 1) / total;
            }
            else
            {
                endFrame = 0;
                startFrame = 0;
            }
        }
        for (frame = startFrame; frame <= endFrame; ++frame)
        {
            if (this.prim.CheckAnim())
            {
                this.prim.Render(frame, totalFrame);
            }
            double r = totalFrame <= 1 ? 0.0 : (double)frame / (double)(totalFrame - 1);
            try
            {
                this.eff.Apply(this.prim.bmp, imgDest, add, this.prim.color.col, dx, dy, dw, dh, r);
                continue;
            }
            catch (Exception e)
            {
                return;
            }
        }
    }

    class PreviewRender implements Runnable
    {
        boolean pause = true;
        int px;
        int py;
        Thread th = new Thread(this);

        @Override public void run()
        {
            while (true)
            {
                try
                {
                    while (this.pause)
                    {
                        Thread.sleep(500L);
                    }
                }
                catch (Exception exception)
                {
                    // empty catch block
                }
                this.pause = true;
                GUIEditor editor = GUIEditor.getInstance();
                if (editor == null || Layer.this.imgprevf == null)
                    continue;
                Layer.this.imgprevf.Clear();
                Layer.this.RenderFrame(Layer.this.imgprevf, false, 0, 0, Layer.this.prefs.width,
                                       Layer.this.prefs.height, 0, 1, false);
                if (Layer.this.eff.centerx.val != 0.0 || Layer.this.eff.centery.val != 0.0)
                {
                    this.px = (int)(Layer.this.eff.centerx.val * 1.28 + 64.0);
                    this.py = (int)(-Layer.this.eff.centery.val * 1.28 + 64.0);
                }
                else
                {
                    this.py = -1;
                    this.px = -1;
                }
                editor.bv1.Setup(Layer.this.imgprevf, this.px, this.py);
                if (!Layer.this.CheckAnim())
                {
                    Layer.this.imgprevt.CopyFrom(Layer.this.imgprevf, Layer.this.prefs.width, Layer.this.prefs.height);
                    editor.bv2.Setup(Layer.this.imgprevt, this.px, this.py);
                    continue;
                }
                Layer.this.imgprevt.Clear();
                Layer.this.RenderFrame(Layer.this.imgprevt, false, 0, 0, Layer.this.prefs.width,
                                       Layer.this.prefs.height, 1, 1, false);
                editor.bv2.Setup(Layer.this.imgprevt, this.px, this.py);
            }
        }

        PreviewRender()
        {
            this.th.start();
        }

        public void Refresh()
        {
            this.pause = false;
            this.th.interrupt();
        }
    }
}

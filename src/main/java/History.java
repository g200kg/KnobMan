/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.util.ArrayList;

public class History
{
    HistoryItem[] journal = new HistoryItem[5];
    int iEndIndex;
    int iStartIndex;
    int iCurrentIndex;

    History()
    {
        for (int i = 0; i < 5; ++i)
        {
            this.journal[i] = new HistoryItem();
        }
        this.iCurrentIndex = 0;
        this.iEndIndex = 0;
        this.iStartIndex = 0;
    }

    void CopyFromJournal(int i)
    {
        Control ctl = Control.getInstance();
        Prefs p = Control.getInstance().prefs;
        HistoryItem hi = this.journal[i];
        p.alignhorz.val = hi.p.alignhorz;
        p.bkcolor.col.Copy(hi.p.bkcolor);
        p.oversampling.val = hi.p.oversampling;
        p.pheight.val = hi.p.pheight;
        p.pwidth.val = hi.p.pwidth;
        p.priFramesPrev.val = hi.p.priFramesPrev;
        p.priFramesRender.val = hi.p.priFramesRender;
        Control.getInstance().Update(Control.UpAll);
    }

    void CopyToJournal(int i)
    {
        Control ctl = Control.getInstance();
        Prefs p = ctl.prefs;
        HistoryItem hi = this.journal[i];
        hi.p = new HPrefs();
        hi.p.alignhorz = p.alignhorz.val;
        hi.p.bkcolor.Copy(p.bkcolor.col);
        hi.p.oversampling = p.oversampling.val;
        hi.p.pheight = p.pheight.val;
        hi.p.pwidth = p.pwidth.val;
        hi.p.priFramesPrev = p.priFramesPrev.val;
        hi.p.priFramesRender = p.priFramesRender.val;
        hi.p.maxlayer = ctl.iMaxLayer;
        hi.ly = new ArrayList();
        for (int j = 0; j < hi.p.maxlayer; ++j)
        {
            HLayer hly = new HLayer();
            Layer ly = ctl.layers.get(j);
            hi.ly.add(hly);
            hly.pcVisible = ly.pcVisible.val;
            hly.pcSolo = ly.pcSolo.val;
            hly.name = new String(ly.name);
            hly.prim = new HPrim();
            hly.prim.bmpImage = ly.prim.bmpImage == null ? null : new Bitmap(ly.prim.bmpImage);
            hly.prim.color = new Col(ly.prim.color.col);
            hly.prim.type = ly.prim.type.val;
            hly.prim.texturefile = ly.prim.texturefile.val;
            hly.prim.transparent = ly.prim.transparent.val;
            hly.prim.font = ly.prim.font.val;
            hly.prim.textalign = ly.prim.textalign.val;
            hly.prim.framealign = ly.prim.framealign.val;
            hly.prim.aspect = ly.prim.aspect.val;
            hly.prim.round = ly.prim.round.val;
            hly.prim.width = ly.prim.width.val;
            hly.prim.length = ly.prim.length.val;
            hly.prim.step = ly.prim.step.val;
            hly.prim.anglestep = ly.prim.anglestep.val;
            hly.prim.emboss = ly.prim.emboss.val;
            hly.prim.embossdiffuse = ly.prim.embossdiffuse.val;
            hly.prim.ambient = ly.prim.ambient.val;
            hly.prim.lightdir = ly.prim.lightdir.val;
            hly.prim.specular = ly.prim.specular.val;
            hly.prim.specularwidth = ly.prim.specularwidth.val;
            hly.prim.texturedepth = ly.prim.texturedepth.val;
            hly.prim.texturezoom = ly.prim.texturezoom.val;
            hly.prim.diffuse = ly.prim.diffuse.val;
            hly.prim.fontsize = ly.prim.fontsize.val;
            hly.prim.file = new String(ly.prim.file.val);
            hly.prim.text = new String(ly.prim.text.val);
            hly.prim.shape = new String(ly.prim.shape.val);
            hly.prim.autofit = ly.prim.autofit.val;
            hly.prim.bold = ly.prim.bold.val;
            hly.prim.italic = ly.prim.italic.val;
            hly.prim.fill = ly.prim.fill.val;
            hly.prim.intellialpha = ly.prim.intellialpha.val;
            hly.prim.numframe = ly.prim.numframe.val;
            hly.prim.texturename = new String(ly.prim.texturename);
            hly.prim.tex0 = ly.prim.tex0 == null ? null : ly.prim.tex0.Clone();
        }
    }

    void Undo()
    {
        if (this.iCurrentIndex != this.iStartIndex)
        {
            if (--this.iCurrentIndex < 0)
            {
                this.iCurrentIndex = 4;
            }
            this.CopyFromJournal(this.iCurrentIndex);
        }
    }

    void Redo()
    {
        if (this.iCurrentIndex != this.iEndIndex)
        {
            if (++this.iCurrentIndex >= 5)
            {
                this.iCurrentIndex = 0;
            }
            this.CopyFromJournal(this.iCurrentIndex);
        }
    }

    void Write()
    {
        this.CopyToJournal(this.iCurrentIndex);
        if (this.iCurrentIndex == this.iEndIndex)
        {
            if (++this.iCurrentIndex >= 5)
            {
                this.iCurrentIndex = 0;
            }
            this.iEndIndex = this.iCurrentIndex;
        }
        else if (++this.iCurrentIndex >= 5)
        {
            this.iCurrentIndex = 0;
        }
        if (this.iEndIndex == this.iStartIndex && ++this.iStartIndex >= 5)
        {
            this.iStartIndex = 0;
        }
    }

    public class HLayer
    {
        String name;
        HPrim prim;
        Eff eff;
        int pcVisible;
        int pcSolo;
    }

    public class HPrefs
    {
        int maxlayer;
        Col bkcolor;
        int pwidth;
        int pheight;
        int priFramesPrev;
        int priFramesRender;
        int oversampling;
        int alignhorz;
    }

    public class HPrim
    {
        Bitmap bmpImage;
        Col color;
        int type;
        int texturefile;
        int transparent;
        int font;
        int textalign;
        int framealign;
        double aspect;
        double round;
        double width;
        double length;
        double step;
        double anglestep;
        double emboss;
        double embossdiffuse;
        double ambient;
        double lightdir;
        double specular;
        double specularwidth;
        double texturedepth;
        double texturezoom;
        double diffuse;
        double fontsize;
        String file;
        String text;
        String shape;
        int autofit;
        int bold;
        int italic;
        int fill;
        int intellialpha;
        int numframe;
        String texturename;
        Tex tex0;
    }

    public class HistoryItem
    {
        HPrefs p = null;
        ArrayList<HLayer> ly = null;

        HistoryItem()
        {
        }
    }
}

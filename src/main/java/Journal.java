/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Component;
import java.util.ArrayList;

public class Journal
{
    int iUndoLevel = 5;
    Object objOnce;
    JournalItem[] journal = new JournalItem[this.iUndoLevel];
    int iEndIndex;
    int iStartIndex;
    int iCurrentIndex;

    Journal()
    {
        for (int i = 0; i < this.iUndoLevel; ++i)
        {
            this.journal[i] = new JournalItem();
        }
        this.iCurrentIndex = 0;
        this.iEndIndex = 0;
        this.iStartIndex = 0;
        this.objOnce = null;
    }

    void CopyFromJournal(int i)
    {
        Control ctl = Control.getInstance();
        Prefs p = Control.getInstance().prefs;
        JournalItem hi = this.journal[i];
        p.alignhorz.val = hi.p.alignhorz;
        p.bkcolor.col.Copy(hi.p.bkcolor);
        p.oversampling.val = hi.p.oversampling;
        p.pheight.val = hi.p.pheight;
        p.pwidth.val = hi.p.pwidth;
        p.priFramesPrev.val = hi.p.priFramesPrev;
        p.priFramesRender.val = hi.p.priFramesRender;
        if (hi.p.maxlayer != ctl.iMaxLayer)
        {
            ctl.layers.clear();
            for (int j = 0; j < hi.p.maxlayer; ++j)
            {
                ctl.layers.add(hi.ly.get(j).Clone(true));
            }
            ctl.iMaxLayer = hi.p.maxlayer;
            ctl.iCurrentLayer = hi.p.currentlayer;
        }
        else
        {
            for (int j = 0; j < ctl.iMaxLayer; ++j)
            {
                ctl.layers.get(j).CopyFrom(hi.ly.get(j));
            }
            ctl.iCurrentLayer = hi.p.currentlayer;
        }
        GUIEditor.getInstance().SetupLayer(ctl.GetCurrentLayer());
        Control.getInstance().Update(Control.UpPrefParam);
        GUIEditor.getInstance().SetupLayer(Control.getInstance().GetCurrentLayer());
        GUIEditor.getInstance().lysplit.setRightComponent(hi.p.cmpCurrent);
        GUIEditor.getInstance().requestFocus();
    }

    void CopyToJournal(int i)
    {
        Control ctl = Control.getInstance();
        Prefs p = ctl.prefs;
        JournalItem hi = this.journal[i];
        hi.p = new HPrefs();
        hi.p.alignhorz = p.alignhorz.val;
        hi.p.bkcolor = new Col(p.bkcolor.col);
        hi.p.oversampling = p.oversampling.val;
        hi.p.pheight = p.pheight.val;
        hi.p.pwidth = p.pwidth.val;
        hi.p.priFramesPrev = p.priFramesPrev.val;
        hi.p.priFramesRender = p.priFramesRender.val;
        hi.p.maxlayer = ctl.iMaxLayer;
        hi.p.currentlayer = ctl.iCurrentLayer;
        hi.p.cmpCurrent = GUIEditor.getInstance().lysplit.getRightComponent();
        hi.ly = new ArrayList();
        for (int j = 0; j < hi.p.maxlayer; ++j)
        {
            Layer hly = ctl.layers.get(j).Clone(false);
            hi.ly.add(hly);
        }
    }

    void Undo()
    {
        if (this.iCurrentIndex != this.iStartIndex)
        {
            if (this.iCurrentIndex == this.iEndIndex)
            {
                this.CopyToJournal(this.iCurrentIndex);
            }
            if (--this.iCurrentIndex < 0)
            {
                this.iCurrentIndex = this.iUndoLevel - 1;
            }
            this.CopyFromJournal(this.iCurrentIndex);
        }
    }

    void Redo()
    {
        if (this.iCurrentIndex != this.iEndIndex)
        {
            if (++this.iCurrentIndex >= this.iUndoLevel)
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
            if (++this.iCurrentIndex >= this.iUndoLevel)
            {
                this.iCurrentIndex = 0;
            }
            this.iEndIndex = this.iCurrentIndex;
        }
        else if (++this.iCurrentIndex >= this.iUndoLevel)
        {
            this.iCurrentIndex = 0;
        }
        if (this.iEndIndex == this.iStartIndex && ++this.iStartIndex >= this.iUndoLevel)
        {
            this.iStartIndex = 0;
        }
        this.ResetOnce();
    }

    void WriteOnce(Object o)
    {
        if (this.objOnce != o)
        {
            this.Write();
            this.objOnce = o;
        }
    }

    void ResetOnce()
    {
        this.objOnce = null;
    }

    public class HPrefs
    {
        int maxlayer;
        int currentlayer;
        Col bkcolor;
        int pwidth;
        int pheight;
        int priFramesPrev;
        int priFramesRender;
        int oversampling;
        int alignhorz;
        Component cmpCurrent;
    }

    public class JournalItem
    {
        HPrefs p = null;
        ArrayList<Layer> ly = null;

        JournalItem()
        {
        }
    }
}

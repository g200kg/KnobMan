/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class Prefs
{
    int width = 64;
    int height = 64;
    int frames = 5;
    ParamCol bkcolor;
    ParamI pwidth;
    ParamI pheight;
    ParamI priFramesPrev;
    ParamI priFramesRender;
    ParamS oversampling;
    ParamS alignhorz = new ParamS(0);
    ParamS exportoption;
    ParamI duration;
    ParamI loop;
    ParamC bidir;
    int rendermode = 0;
    int testindex = 0;

    Prefs()
    {
        this.pwidth = new ParamI(64);
        this.pheight = new ParamI(64);
        this.oversampling = new ParamS(0);
        this.priFramesPrev = new ParamI(5);
        this.priFramesRender = new ParamI(31);
        this.bkcolor = new ParamCol(new Col(255, 255, 255));
        this.exportoption = new ParamS(0);
        this.duration = new ParamI(100);
        this.loop = new ParamI(0);
        this.bidir = new ParamC(0);
    }

    void xylinkset()
    {
        GUIEditor.getInstance().prefspanel.xylinkset();
    }

    void Init()
    {
        this.pwidth.val = 64;
        this.pheight.val = 64;
        this.oversampling.val = 0;
        this.priFramesPrev.val = 5;
        this.priFramesRender.val = 31;
        this.bkcolor.col = new Col(255, 255, 255);
        GUIEditor.getInstance().prefspanel.xylinkset();
        this.rendermode = 0;
        this.testindex = 0;
    }
}

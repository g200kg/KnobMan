/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

public class WinPos
{
    public static Rectangle ScreenSize()
    {
        Rectangle rc = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        for (int j = 0; j < gs.length; ++j)
        {
            GraphicsDevice gd = gs[j];
            GraphicsConfiguration[] gc = gd.getConfigurations();
            for (int i = 0; i < gc.length; ++i)
            {
                rc = rc.union(gc[i].getBounds());
            }
        }
        return rc;
    }

    public static void Limit(Rectangle rc)
    {
        Rectangle rcScr = WinPos.ScreenSize();
        if (rc.width < 40)
        {
            rc.width = 40;
        }
        if (rc.height < 40)
        {
            rc.height = 40;
        }
        if (rc.x > rcScr.width - 40)
        {
            rc.x = rcScr.width - 40;
        }
        if (rc.y > rcScr.height - 40)
        {
            rc.y = rcScr.height - 40;
        }
        if (rc.x + rc.width < 40)
        {
            rc.x = 40 - rc.width;
        }
        if (rc.y + rc.height < 40)
        {
            rc.y = 40 - rc.height;
        }
    }
}

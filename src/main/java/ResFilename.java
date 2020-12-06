/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Image;
import javax.swing.ImageIcon;

public class ResFilename
{
    String str;

    public ResFilename(String strName)
    {
        this.str = strName;
    }

    public String GetString()
    {
        String exepath = System.getProperty("java.application.path");
        String strpath = System.getProperty("java.class.path");
        if (exepath != null)
        {
            return String.valueOf(exepath) + "\\" + this.str;
        }
        int p = strpath.indexOf(";");
        if (p >= 0)
        {
            strpath = strpath.substring(0, p);
        }
        if ((p = strpath.lastIndexOf("\\")) >= 0)
        {
            return String.valueOf(strpath.substring(0, p)) + "\\" + this.str;
        }
        p = strpath.lastIndexOf("/");
        if (p >= 0)
        {
            return String.valueOf(strpath.substring(0, p)) + "/" + this.str;
        }
        return this.str;
    }

    public ImageIcon GetImageIcon()
    {
        return new ImageIcon(this.GetString());
    }

    public Image GetImage()
    {
        ImageIcon icon = this.GetImageIcon();
        if (icon == null)
        {
            return null;
        }
        return icon.getImage();
    }

    public Bitmap GetBitmap()
    {
        return new Bitmap(this.GetImage());
    }
}

/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Font;
import javax.swing.ImageIcon;

class CellItem
{
    public Font font;
    public String text;
    public ImageIcon icon;

    public CellItem(String text, ImageIcon icon)
    {
        this.text = text;
        this.icon = icon;
    }
}

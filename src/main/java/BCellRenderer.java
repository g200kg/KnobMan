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
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class BCellRenderer implements ListCellRenderer
{
    protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

    BCellRenderer()
    {
    }

    public Component getListCellRendererComponent(JList list, Object o, int index, boolean isSelected,
                                                  boolean cellHasFocus)
    {
        CellItem item = (CellItem)o;
        JLabel label = (JLabel)this.defaultRenderer.getListCellRendererComponent((JList<?>)list, item.text, index,
                                                                                 isSelected, isSelected);
        ImageIcon icon = item.icon;
        label.setIcon(icon);
        if (isSelected && index > -1)
        {
            label.setToolTipText(item.text);
        }
        return label;
    }
}

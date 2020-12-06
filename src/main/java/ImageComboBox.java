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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class ImageComboBox extends JComboBox
{
    DefaultComboBoxModel model;
    JComboBox combo;

    ImageComboBox()
    {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        this.combo = new JComboBox(model);
    }

    void AddItem(ImageComboItem item)
    {
        this.model.addElement(item);
    }

    class MyCellRenderer extends JLabel implements ListCellRenderer
    {
        MyCellRenderer()
        {
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                      boolean cellHasFocus)
        {
            ImageComboItem data = (ImageComboItem)value;
            this.setText(data.text);
            this.setIcon(data.icon);
            return this;
        }
    }
}

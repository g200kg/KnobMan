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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class FieldH extends JPanel implements ActionListener
{
    JCheckBox cb;

    public FieldH(Container frame, int x, int y, String s)
    {
        this.setLayout(null);
        this.setBounds(x, y, 340, 22);
        this.setBackground(Color.lightGray);
        this.cb = new JCheckBox(s, GUIEditor.getInstance().iconTreeClose, true);
        this.cb.setOpaque(false);
        this.cb.setSelectedIcon(GUIEditor.getInstance().iconTreeOpen);
        this.cb.setFont(GUIEditor.getInstance().fontUILarge);
        this.cb.setBounds(10, 0, 160, 22);
        this.cb.addActionListener(this);
        this.add(this.cb);
        frame.add(this);
    }

    public void ReLayout(int x, int y)
    {
        this.setBounds(x, y, 340, 22);
    }

    public void Update(double v)
    {
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        GUIEditor.getInstance().effpanel.ReLayout();
    }
}

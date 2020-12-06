/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class FieldC extends JPanel implements ActionListener
{
    JCheckBox cb;
    ParamC pc;
    UpdateReq ur;
    int m;

    public FieldC(Container frame, int x, int y, String name, int w1, UpdateReq ur, int m)
    {
        this.ur = ur;
        this.m = m;
        this.setBounds(x, y, 160, 25);
        this.setLayout(null);
        this.setSize(w1, 22);
        this.cb = new JCheckBox(name);
        this.cb.setFont(GUIEditor.getInstance().fontUI);
        this.add(this.cb);
        this.cb.setBounds(0, 0, w1, 22);
        frame.add(this);
        this.cb.addActionListener(this);
        this.pc = null;
    }

    public void ReLayout(int x, int y)
    {
        this.setBounds(x, y, 160, 25);
    }

    public void Setup(ParamC pc)
    {
        this.pc = pc;
        if (pc != null)
        {
            if (pc.val != 0)
            {
                this.cb.setSelected(true);
            }
            else
            {
                this.cb.setSelected(false);
            }
        }
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        if (this.pc != null)
        {
            Control.getInstance().journal.Write();
            if (((JCheckBox)e.getSource()).isSelected())
            {
                this.pc.Update(1);
            }
            else
            {
                this.pc.Update(0);
            }
            if (this.ur != null)
            {
                this.ur.Update(32768 + this.m);
            }
        }
    }
}

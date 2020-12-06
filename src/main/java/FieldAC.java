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
import javax.swing.JPanel;

public class FieldAC extends JPanel implements ActionListener
{
    FieldC cb;
    FieldVal vf1;
    FieldVal vf2;
    FieldS anim;

    public FieldAC(Container frame, int x, int y, String name, int w1, int w2, double mn, double mx, UpdateReq ur,
                   int m)
    {
        this.setLayout(null);
        this.setBounds(x, y, 340, 25);
        this.cb = new FieldC(this, 0, 0, name, w1 + 25, ur, m | 0x8000);
        this.cb.cb.setHorizontalAlignment(4);
        this.cb.cb.addActionListener(this);
        this.vf1 = new FieldVal(this, w1 + 25, 0, "", 0, w2, true, mn, mx, ur, m);
        this.anim =
            new FieldS((Container)this, w1 + w2 + 27, 0, 22, "", 0, 60, GUIEditor.getInstance().iconAnim, ur, m);
        this.vf2 = new FieldVal(this, w1 + w2 + 91, 0, "", 0, w2, true, mn, mx, ur, m);
        frame.add(this);
        this.anim.ch.addActionListener(this);
    }

    public void ReLayout(int x, int y)
    {
        this.setBounds(x, y, 340, 25);
    }

    public void Setup(ParamC cb, ParamV pv1, ParamV pv2, ParamS ps)
    {
        this.cb.Setup(cb);
        this.vf1.Setup(pv1);
        this.anim.Setup(ps);
        this.vf2.Setup(pv2);
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        if (this.cb.cb.isSelected())
        {
            this.vf1.tf.setEnabled(true);
            this.vf1.bt.setEnabled(true);
            if (this.anim.ch.getSelectedIndex() == 0)
            {
                this.vf2.tf.setEnabled(false);
                this.vf2.bt.setEnabled(false);
            }
            else
            {
                this.vf2.tf.setEnabled(true);
                this.vf2.bt.setEnabled(true);
            }
        }
        else
        {
            this.vf1.tf.setEnabled(false);
            this.vf1.bt.setEnabled(false);
            this.vf2.tf.setEnabled(false);
            this.vf2.bt.setEnabled(false);
        }
    }
}

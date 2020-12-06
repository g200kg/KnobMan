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

public class FieldA extends JPanel implements ActionListener
{
    FieldVal vf1;
    FieldVal vf2;
    FieldS anim;

    public FieldA(Container frame, int x, int y, String name, int w1, int w2, double mn, double mx, UpdateReq ur, int m)
    {
        this.setLayout(null);
        this.setBounds(x, y, 340, 24);
        this.vf1 = new FieldVal(this, 0, 0, name, w1, w2, true, mn, mx, ur, m);
        this.anim = new FieldS((Container)this, w1 + w2 + 2, 0, 22, "", 0, 60, GUIEditor.getInstance().iconAnim, ur, m);
        this.vf2 = new FieldVal(this, w1 + w2 + 66, 0, "", 0, w2, true, mn, mx, ur, m);
        frame.add(this);
        this.anim.ch.addActionListener(this);
    }

    public void ReLayout(int x, int y)
    {
        this.setBounds(x, y, 340, 24);
    }

    public void Setup(ParamV pv1, ParamV pv2, ParamS ps)
    {
        this.vf1.Setup(pv1);
        this.anim.Setup(ps);
        this.vf2.Setup(pv2);
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        if (this.anim.ch.getSelectedIndex() != 0 && this.vf1.tf.isEnabled())
        {
            this.vf2.tf.setEnabled(true);
            this.vf2.bt.setEnabled(true);
        }
        else
        {
            this.vf2.tf.setEnabled(false);
            this.vf2.bt.setEnabled(false);
        }
    }
}

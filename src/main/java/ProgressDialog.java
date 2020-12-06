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
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgressDialog extends JDialog
{
    public JProgressBar pb;
    public JLabel lb;

    public ProgressDialog(String str)
    {
        super((Frame)GUIEditor.getInstance(), true);
        this.setTitle("JKnobMan");
        this.setSize(350, 200);
        this.setLocationRelativeTo(null);
        Container c = this.getContentPane();
        this.setLayout(null);
        this.lb = new JLabel(str);
        this.lb.setBounds(40, 20, 250, 30);
        c.add(this.lb);
        this.setDefaultCloseOperation(2);
        this.pb = new JProgressBar(0, 100);
        this.pb.setBounds(50, 60, 200, 25);
        c.add(this.pb);
        this.SetProgress(0, 0);
    }

    public void Start()
    {
        this.setVisible(true);
    }

    public void SetProgress(int n, int m)
    {
        if (m == 0)
        {
            this.pb.setValue(0);
        }
        else
        {
            this.pb.setValue(n * 100 / m);
        }
        this.lb.setText("Rendering...frame(" + n + "/" + m + ")");
    }
}

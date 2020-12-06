/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class DialogRename extends Dialog implements ActionListener
{
    Label lb;
    TextField tf;
    Button btnok;
    Button btncancel;
    public String ret = null;

    public DialogRename(Frame frame, String str)
    {
        super(frame, "JKnobMan", true);
        this.setSize(300, 110);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.lb = new Label("Rename Layer");
        this.add((Component)this.lb, "North");
        this.tf = new TextField(str);
        this.add((Component)this.tf, "Center");
        this.btnok = new Button("OK");
        this.btncancel = new Button("Cancel");
        Panel panelbtn = new Panel();
        this.add((Component)panelbtn, "South");
        panelbtn.add(this.btnok);
        panelbtn.add(this.btncancel);
        this.addWindowListener(new WindowListener());
        this.btnok.addActionListener(this);
        this.btncancel.addActionListener(this);
        Rectangle rc = frame.getBounds();
        this.setLocation(rc.x + rc.width / 2 - 150, rc.y + rc.height / 2 - 55);
        this.setVisible(true);
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnok)
        {
            this.ret = this.tf.getText();
            this.dispose();
        }
        if (e.getSource() == this.btncancel)
        {
            this.ret = null;
            this.dispose();
        }
    }

    class WindowListener extends WindowAdapter
    {
        WindowListener()
        {
        }

        @Override public void windowClosing(WindowEvent e)
        {
            DialogRename.this.dispose();
        }
    }
}

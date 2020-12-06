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
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FieldT extends JPanel implements FocusListener, DocumentListener, KeyListener
{
    JLabel lb;
    JTextField tf;
    ParamT pt;
    UpdateReq ur;
    Color colDef;
    int m;
    int disable;

    public FieldT(Container frame, int x, int y, String name, int w1, int w2, UpdateReq ur, int m)
    {
        this.ur = ur;
        this.m = m;
        this.setLayout(null);
        this.setBounds(x, y, w1 + w2, 28);
        this.lb = new JLabel(name);
        this.lb.setFont(GUIEditor.getInstance().fontUI);
        this.lb.setHorizontalAlignment(4);
        this.tf = new JTextField("0.0", 8);
        this.tf.setFont(GUIEditor.getInstance().fontUI);
        this.add(this.lb);
        this.add(this.tf);
        this.lb.setBounds(0, 0, w1, 28);
        this.tf.setBounds(w1, 0, w2, 28);
        frame.add(this);
        this.pt = null;
        this.tf.getDocument().addDocumentListener(this);
        this.tf.addKeyListener(this);
        this.tf.addFocusListener(this);
        this.colDef = this.tf.getBackground();
        this.disable = 0;
    }

    public void SetText(String s)
    {
        this.disable = 1;
        this.tf.setText(s);
        if (this.pt != null)
        {
            this.pt.val = s;
        }
        this.disable = 0;
    }

    public void Setup(ParamT pt)
    {
        this.disable = 1;
        this.pt = pt;
        if (pt != null)
        {
            this.tf.setText(pt.val);
        }
        else
        {
            this.tf.setText("");
        }
        this.disable = 0;
    }

    public void ReLayout(int x, int y)
    {
        Rectangle r = this.getBounds();
        this.setBounds(x, y, r.width, r.height);
    }

    public void Reform()
    {
        this.tf.setBackground(this.colDef);
    }

    public void TextChanged()
    {
        if (this.disable == 0 && this.pt != null)
        {
            String s;
            Control.getInstance().journal.WriteOnce(this);
            this.tf.setBackground(Color.yellow);
            this.pt.val = s = this.tf.getText();
            if (this.ur != null)
            {
                this.ur.Update(1);
            }
        }
    }

    @Override public void removeUpdate(DocumentEvent e)
    {
        this.TextChanged();
    }

    @Override public void changedUpdate(DocumentEvent e)
    {
        this.TextChanged();
    }

    @Override public void insertUpdate(DocumentEvent e)
    {
        this.TextChanged();
    }

    @Override public void keyPressed(KeyEvent e)
    {
        int mod = e.getModifiersEx();
        int code = e.getKeyCode();
        switch (code)
        {
        case 10: {
            this.tf.setBackground(this.colDef);
            Control.getInstance().journal.ResetOnce();
            if (this.ur == null)
                break;
            this.ur.Update(32768 + this.m);
        }
        }
    }

    @Override public void keyReleased(KeyEvent e)
    {
    }

    @Override public void keyTyped(KeyEvent e)
    {
    }

    @Override public void focusGained(FocusEvent e)
    {
    }

    @Override public void focusLost(FocusEvent e)
    {
        this.Reform();
    }
}

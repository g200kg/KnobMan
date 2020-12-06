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
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class FieldL extends JPanel implements MouseListener, ActionListener
{
    GUIEditor editor;
    JLabel lb;
    JCheckBox cbV;
    JCheckBox cbS;
    BitmapView icon;
    int state;
    UpdateReq ur;
    public int m;

    public FieldL(Container frame, UpdateReq ur, int m)
    {
        this.ur = ur;
        this.m = m;
        this.setLayout(null);
        this.setSize(150, 24);
        this.editor = GUIEditor.getInstance();
        if (m <= 0)
        {
            this.lb = new JLabel(this.editor.GetMsgStr("preference"));
            this.cbS = null;
            this.cbV = null;
            this.lb.setBounds(49, 1, 117, 22);
            JLabel icon1 = new JLabel(GUIEditor.getInstance().iconVisible);
            icon1.setBounds(1, 1, 22, 22);
            this.add(icon1);
            JLabel icon2 = new JLabel(GUIEditor.getInstance().iconSolo);
            icon2.setBounds(25, 1, 22, 22);
            this.add(icon2);
        }
        else
        {
            String s = String.format(this.editor.GetMsgStr("layer"), m);
            this.lb = new JLabel(s);
            this.lb.setToolTipText(s);
            this.cbV = new JCheckBox("");
            this.cbV.setOpaque(false);
            this.cbV.setBackground(Color.white);
            this.cbV.setFocusable(false);
            this.add(this.cbV);
            this.cbV.setBounds(1, 1, 22, 22);
            this.cbV.addActionListener(this);
            this.cbS = new JCheckBox("");
            this.cbS.setOpaque(false);
            this.cbS.setBackground(Color.white);
            this.cbS.setFocusable(false);
            this.add(this.cbS);
            this.cbS.setBounds(25, 1, 22, 22);
            this.cbS.addActionListener(this);
            this.lb.setBounds(67, 1, 117, 22);
            this.icon = new BitmapView(49, 4, 16, 16, null, null);
            this.add(this.icon);
        }
        this.lb.setTransferHandler(new TransferHandler("text"));
        this.add(this.lb);
        this.setBackground(Color.white);
        frame.add(this);
        this.lb.addMouseListener(this);
    }

    @Override public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (this.m > 0)
        {
            int from = this.editor.lypanel.iDragFrom;
            int to = this.editor.lypanel.iDragTo;
            if (from > 0 && to == this.m)
            {
                g.setColor(Color.blue);
                if (from > to)
                {
                    g.fillRect(50, 0, 300, 2);
                }
                if (from < to)
                {
                    g.fillRect(50, 21, 300, 2);
                }
            }
        }
    }

    public void SetName(String name)
    {
        this.lb.setText(name);
        this.lb.setToolTipText(name);
    }

    public void SetIcon(ImageIcon icon)
    {
        this.icon.Setup(icon);
    }

    public void Update(double v)
    {
    }

    public void Select(int v)
    {
        this.state = v;
        if (v != 0)
        {
            this.SetFocusCol(Color.lightGray);
        }
        else
        {
            this.SetFocusCol(Color.white);
        }
    }

    public void SetFocusCol(Color c)
    {
        this.setBackground(c);
    }

    @Override public void mouseClicked(MouseEvent e)
    {
        if (this.ur != null)
        {
            Control.getInstance().journal.ResetOnce();
            this.ur.Update(this.m);
        }
    }

    @Override public void mousePressed(MouseEvent e)
    {
        this.editor.getContentPane().setCursor(new Cursor(13));
        this.editor.lypanel.DragStart(this.m);
    }

    @Override public void mouseReleased(MouseEvent e)
    {
        this.editor.getContentPane().setCursor(new Cursor(0));
        this.editor.lypanel.DragFinish(this.m);
    }

    @Override public void mouseEntered(MouseEvent e)
    {
        this.editor.lypanel.DragTo(this.m);
        if (this.editor.lypanel.iDragFrom <= 0)
        {
            this.SetFocusCol(Color.lightGray);
        }
        else
        {
            this.repaint();
        }
    }

    @Override public void mouseExited(MouseEvent e)
    {
        this.editor.lypanel.DragTo(-1);
        if (this.state != 0)
        {
            this.SetFocusCol(Color.lightGray);
        }
        else
        {
            this.SetFocusCol(Color.white);
        }
        this.repaint();
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        if (this.ur != null)
        {
            int val = 0x100 | this.m;
            if (this.cbV.isSelected())
            {
                val |= 0x400;
            }
            if (this.cbS.isSelected())
            {
                val |= 0x200;
            }
            this.ur.Update(val);
        }
    }
}

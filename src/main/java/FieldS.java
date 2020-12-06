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
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FieldS extends JPanel implements MouseWheelListener, ItemListener, PopupMenuListener
{
    JLabel lb;
    JComboBox ch;
    ParamS ps;
    UpdateReq ur;
    int dropdownwidth = 0;
    int m;
    int disable;
    boolean widthadjust = false;
    int width1;
    int width2;
    int height;

    public FieldS(Container frame, int x, int y, int h, String name, int w1, int w2, String[] param, UpdateReq ur,
                  int m)
    {
        this.setLayout(null);
        this.setBounds(x, y, w1 + w2, h);
        this.ur = ur;
        this.m = m;
        this.width1 = w1;
        this.width2 = w2;
        this.height = h;
        this.lb = new JLabel(name);
        this.lb.setFont(GUIEditor.getInstance().fontUI);
        this.lb.setHorizontalAlignment(4);
        this.ch = new JComboBox();
        this.ch.setFont(GUIEditor.getInstance().fontUI);
        for (int i = 0; i < param.length; ++i)
        {
            this.ch.addItem(param[i]);
        }
        this.add(this.lb);
        this.add(this.ch);
        this.lb.setBounds(0, 0, w1 - 2, h);
        this.ch.setBounds(w1, 0, w2, h);
        frame.add(this);
        this.ps = null;
        this.ch.addMouseWheelListener(this);
        this.ch.addItemListener(this);
        this.ch.addPopupMenuListener(this);
        this.disable = 0;
    }

    public FieldS(Container frame, int x, int y, int h, String name, int w1, int w2, ImageIcon[] param, UpdateReq ur,
                  int m)
    {
        this.setLayout(null);
        this.setBounds(x, y, w1 + w2, h);
        this.ur = ur;
        this.m = m;
        this.width1 = w1;
        this.width2 = w2;
        this.height = h;
        this.lb = new JLabel(name);
        this.lb.setFont(GUIEditor.getInstance().fontUI);
        this.lb.setHorizontalAlignment(4);
        this.ch = new JComboBox();
        this.ch.setFont(GUIEditor.getInstance().fontUI);
        for (int i = 0; i < param.length; ++i)
        {
            this.ch.addItem(param[i]);
        }
        this.add(this.lb);
        this.add(this.ch);
        this.lb.setBounds(0, 0, w1, h);
        this.ch.setBounds(w1, 0, w2, h);
        frame.add(this);
        this.ps = null;
        this.ch.addMouseWheelListener(this);
        this.ch.addItemListener(this);
        this.ch.addPopupMenuListener(this);
        this.disable = 0;
    }

    public FieldS(Container frame, int x, int y, int h, String name, int w1, int w2, int w3, ImageIcon[] param,
                  String[] param2, UpdateReq ur, int m)
    {
        this.setLayout(null);
        this.setBounds(x, y, w1 + w2, h);
        this.ur = ur;
        this.m = m;
        this.width1 = w1;
        this.width2 = w2;
        this.height = h;
        this.dropdownwidth = w3;
        this.widthadjust = false;
        this.lb = new JLabel(name);
        this.lb.setFont(GUIEditor.getInstance().fontUI);
        this.lb.setHorizontalAlignment(4);
        this.ch = new JComboBox();
        this.ch.setFont(GUIEditor.getInstance().fontUI);
        BCellRenderer ren = new BCellRenderer();
        this.ch.setRenderer(ren);
        for (int i = 0; i < param.length; ++i)
        {
            this.ch.addItem(new CellItem(param2[i], param[i]));
        }
        this.add(this.lb);
        this.add(this.ch);
        this.lb.setBounds(0, 0, w1, h);
        this.ch.setBounds(w1, 0, w2, h);
        frame.add(this);
        this.ps = null;
        this.ch.addMouseWheelListener(this);
        this.ch.addItemListener(this);
        this.ch.addPopupMenuListener(this);
        this.disable = 0;
    }

    public void ReLayout(int x, int y)
    {
        Rectangle r = this.getBounds();
        this.setBounds(x, y, r.width, r.height);
    }

    public void SetItem(int i, String s)
    {
        this.disable = 1;
        int j = this.ch.getSelectedIndex();
        this.ch.removeItemAt(i);
        this.ch.insertItemAt(s, i);
        this.ch.setSelectedIndex(j);
        this.disable = 0;
    }

    public void SetItem(int i, ImageIcon ic, String s)
    {
        this.disable = 1;
        int j = this.ch.getSelectedIndex();
        this.ch.removeItemAt(i);
        this.ch.insertItemAt(new CellItem(s, ic), i);
        this.ch.setSelectedIndex(j);
        this.disable = 0;
    }

    public void Setup(ParamS ps)
    {
        this.disable = 1;
        this.ps = ps;
        if (this.ps != null)
        {
            this.ch.setSelectedIndex(ps.val);
        }
        this.disable = 0;
    }

    @Override public void itemStateChanged(ItemEvent e)
    {
        if (this.disable == 0 && e.getSource() == this.ch && e.getStateChange() == 1 && this.ps != null)
        {
            Control.getInstance().journal.Write();
            this.ps.Update(this.ch.getSelectedIndex());
            if (this.ur != null)
            {
                this.ur.Update(this.m);
            }
        }
    }

    @Override public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (e.getSource() == GUIEditor.getInstance().getFocusOwner())
        {
            if (this.ps != null)
            {
                int iInv = GUIEditor.getInstance().iWheelDir != 0 ? -1 : 1;
                int i = this.ps.val + iInv * e.getWheelRotation();
                if (i < 0)
                {
                    i = 0;
                }
                if (i >= this.ch.getItemCount())
                {
                    i = this.ch.getItemCount() - 1;
                }
                this.ps.val = i;
                this.Setup(this.ps);
                if (this.ur != null)
                {
                    this.ur.Update(this.m);
                }
            }
        }
        else
        {
            this.getParent().dispatchEvent(e);
        }
    }

    @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e)
    {
        if (this.dropdownwidth != 0)
        {
            if (!this.widthadjust)
            {
                this.widthadjust = true;
                this.ch.setSize(this.dropdownwidth, this.height);
                this.ch.showPopup();
            }
            this.widthadjust = false;
        }
    }

    @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
    {
        this.ch.setBounds(this.width1, 0, this.width2, this.height);
    }

    @Override public void popupMenuCanceled(PopupMenuEvent e)
    {
        this.ch.setBounds(this.width1, 0, this.width2, this.height);
    }
}

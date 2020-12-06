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
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class LayerPanel extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, UpdateReq
{
    FieldL[] line;
    JScrollPane scr;
    JPanel panel;
    int iDragFrom;
    int iDragTo;

    public LayerPanel(GUIEditor editor)
    {
        this.setLayout(null);
        this.line = new FieldL[100];
        for (int i = 0; i < 100; ++i)
        {
            this.line[i] = new FieldL(this, this, i);
            this.line[i].setBounds(0, i * 22, 120, 22);
        }
        this.setBackground(Color.white);
        this.addComponentListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.iDragTo = 0;
        this.iDragFrom = 0;
    }

    public void DragStart(int m)
    {
        this.iDragFrom = m;
    }

    public void DragFinish(int m)
    {
        if (this.iDragFrom > 0 && this.iDragTo > 0)
        {
            Control.getInstance().journal.Write();
            Control.getInstance().MoveLayer(this.iDragFrom - 1, this.iDragTo - 1);
            Control.getInstance().SelLayer(this.iDragTo);
        }
        this.iDragTo = 0;
        this.iDragFrom = 0;
    }

    public void DragTo(int n)
    {
        this.iDragTo = n;
    }

    public void Set()
    {
        Control ctl = Control.getInstance();
        int y = 0;
        for (int i = 0; i < 100; ++i)
        {
            if (i >= 1 && i <= ctl.iMaxLayer)
            {
                Layer ly = ctl.layers.get(i - 1);
                if (ly.pcVisible.val != 0)
                {
                    this.line[i].cbV.setSelected(true);
                }
                else
                {
                    this.line[i].cbV.setSelected(false);
                }
                if (ly.pcSolo.val != 0)
                {
                    this.line[i].cbS.setSelected(true);
                }
                else
                {
                    this.line[i].cbS.setSelected(false);
                }
                int t = ly.prim.type.val;
                this.line[i].SetIcon(GUIEditor.getInstance().imgIcon[t]);
                if (ly.name == null || ly.name.equals(""))
                {
                    String s = String.format(GUIEditor.getInstance().GetMsgStr("layer"), i);
                    this.line[i].SetName(s);
                }
                else
                {
                    this.line[i].SetName(ly.name);
                }
            }
            if (i == ctl.iCurrentLayer)
            {
                this.line[i].Select(1);
            }
            else
            {
                this.line[i].Select(0);
            }
            if (i - 1 >= ctl.iMaxLayer)
            {
                this.line[i].setBounds(0, y, 0, 0);
                continue;
            }
            this.line[i].setBounds(0, y, 140, 24);
            y += 24;
        }
        this.setSize(140, y);
        this.setPreferredSize(new Dimension(140, y));
    }

    @Override public void Update(int m)
    {
        if ((m & 0xFF00) != 0)
        {
            int l = m & 0xFF;
            if (l > 0)
            {
                Control.getInstance().journal.Write();
                Layer ly = Control.getInstance().layers.get(l - 1);
                ly.pcSolo.val = 0;
                ly.pcVisible.val = 0;
                if ((m & 0x200) != 0)
                {
                    ly.pcSolo.val = 1;
                }
                if ((m & 0x400) != 0)
                {
                    ly.pcVisible.val = 1;
                }
                Control.getInstance().Update(Control.Up_Render);
            }
        }
        else
        {
            Control.getInstance().SelLayer(m);
            this.Set();
        }
    }

    @Override public void componentHidden(ComponentEvent e)
    {
    }

    @Override public void componentShown(ComponentEvent e)
    {
    }

    @Override public void componentMoved(ComponentEvent e)
    {
    }

    @Override public void componentResized(ComponentEvent e)
    {
    }

    @Override public void mouseEntered(MouseEvent e)
    {
    }

    @Override public void mouseExited(MouseEvent e)
    {
    }

    @Override public void mousePressed(MouseEvent e)
    {
        this.requestFocus();
    }

    @Override public void mouseReleased(MouseEvent e)
    {
    }

    @Override public void mouseClicked(MouseEvent e)
    {
    }

    @Override public void mouseMoved(MouseEvent e)
    {
    }

    @Override public void mouseDragged(MouseEvent e)
    {
    }
}

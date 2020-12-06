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
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BitmapView extends JPanel
{
    public int x;
    public int y;
    public int w;
    public int h;
    Bitmap bmp;
    ImageIcon icon;
    Col bg;
    boolean keepaspect;
    public int cx;
    public int cy;

    BitmapView(int xini, int yini, int wini, int hini, Bitmap bmp, Col bg)
    {
        this.x = xini;
        this.y = yini;
        this.w = wini;
        this.h = hini;
        this.bmp = null;
        this.icon = null;
        this.keepaspect = false;
        this.setBounds(this.x, this.y, this.w, this.h);
        this.bg = bg;
        this.setPreferredSize(new Dimension(this.w, this.h));
        this.cy = -1;
        this.cx = -1;
    }

    public void SetBounds(int x, int y, int w, int h)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        this.repaint();
    }

    public void SetSize(int w, int h)
    {
        this.w = w;
        this.h = h;
        this.setSize(w, h);
        this.setPreferredSize(new Dimension(w, h));
        this.repaint();
    }

    public void SetBg(Col c)
    {
        this.bg = c;
        this.repaint();
    }

    @Override public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        if (this.bmp != null && this.bmp.img != null)
        {
            if (this.keepaspect)
            {
                if (this.bg != null)
                {
                    g.setColor(new Color(100, 100, 100));
                    g.fillRect(0, 0, this.getWidth(), this.getHeight());
                    g.setColor(new Color(this.bg.r, this.bg.g, this.bg.b));
                }
                if (this.bmp.width > this.bmp.height)
                {
                    int y = this.w * this.bmp.height / this.bmp.width;
                    g.fillRect(0, (this.h - y) / 2, this.w, y);
                    g.drawImage(this.bmp.img, 0, (this.h - y) / 2, this.w, (this.h - y) / 2 + y, 0, 0, this.bmp.width,
                                this.bmp.height, null);
                }
                else if (this.bmp.height > this.bmp.width)
                {
                    int x = this.h * this.bmp.width / this.bmp.height;
                    g.fillRect((this.w - x) / 2, 0, x, this.h);
                    g.drawImage(this.bmp.img, (this.w - x) / 2, 0, (this.w - x) / 2 + x, this.h, 0, 0, this.bmp.width,
                                this.bmp.height, null);
                }
                else
                {
                    g.drawImage(this.bmp.img, 0, 0, this.w, this.h, this);
                }
            }
            else
            {
                if (this.bg != null)
                {
                    g.setColor(new Color(this.bg.r, this.bg.g, this.bg.b));
                }
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.drawImage(this.bmp.img, 0, 0, this.w, this.h, this);
            }
        }
        else if (this.icon != null)
        {
            g.drawImage(this.icon.getImage(), 0, 0, null);
        }
        if (this.cx >= 0)
        {
            g.setColor(Color.black);
            g.fillRect(this.cx - 8, this.cy - 1, 16, 3);
            g.fillRect(this.cx - 1, this.cy - 8, 3, 16);
            g.setColor(Color.white);
            g.fillRect(this.cx - 7, this.cy, 14, 1);
            g.fillRect(this.cx, this.cy - 7, 1, 14);
        }
    }

    public void Setup(Bitmap newbmp)
    {
        this.bmp = newbmp;
        this.repaint();
    }

    public void Setup(Bitmap newbmp, int x, int y)
    {
        this.bmp = newbmp;
        this.cx = x;
        this.cy = y;
        this.repaint();
    }

    public void Setup(ImageIcon icon)
    {
        this.icon = icon;
        this.repaint();
    }

    public void SetupCurPos(int x, int y)
    {
        this.cx = x;
        this.cy = y;
        this.repaint();
    }
}

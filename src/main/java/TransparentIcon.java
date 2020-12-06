/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TransparentIcon extends JFrame implements KeyListener, ComponentListener
{
    Rectangle rcWin;
    Image imgBackground;
    Bitmap bmpStrip;
    Bitmap bmpIcon;
    TransparentPanel tpanel;
    int width;
    int height;
    int wout;
    int hout;
    int mod;
    JFrame frame;
    int iIndex;
    int iIndexLast;
    int frames;
    int horz;
    int yStart;
    int iIndexStart;

    public TransparentIcon()
    {
        if (GUIEditor.getInstance().iUseCapture != 0)
        {
            this.UpdateBackground();
        }
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setAlwaysOnTop(true);
        this.setVisible(true);
        this.addKeyListener(this);
        if (GUIEditor.getInstance().iUseCapture != 0)
        {
            this.addComponentListener(this);
        }
        this.tpanel = new TransparentPanel();
        this.getContentPane().add(this.tpanel);
        Rectangle rc = GUIEditor.getInstance().getBounds();
        this.setLocation(rc.x + rc.width / 2 - 150, rc.y + rc.height / 2 - 55);
    }

    public void SetFrame(int i)
    {
        this.tpanel.SetFrame(i);
    }

    public void Update(Bitmap bmp, int w, int h, int oversampling, int frames, int horz)
    {
        this.iIndex = 0;
        this.iIndexLast = -1;
        this.frames = frames;
        this.horz = horz;
        this.wout = w >> oversampling;
        this.hout = h >> oversampling;
        this.frame = this;
        this.bmpStrip = bmp;
        this.bmpIcon = new Bitmap(this.wout, this.hout);
        this.width = w;
        this.height = h;
        this.setSize(this.wout + 32, this.hout + 32);
        this.repaint();
    }

    public void Set(Bitmap bmp, int w, int h, int oversampling, int n)
    {
    }

    public void UpdateBackground()
    {
        try
        {
            Robot rbt = new Robot();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension dim = tk.getScreenSize();
            this.imgBackground =
                rbt.createScreenCapture(new Rectangle(0, 0, (int)dim.getWidth(), (int)dim.getHeight()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override public void componentHidden(ComponentEvent e)
    {
    }

    @Override public void componentResized(ComponentEvent e)
    {
    }

    @Override public void componentShown(ComponentEvent e)
    {
    }

    @Override public void componentMoved(ComponentEvent e)
    {
        Rectangle rc = this.getBounds();
        if (!rc.equals(this.rcWin))
        {
            this.rcWin = new Rectangle(rc);
            this.tpanel.repaint();
        }
    }

    @Override public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == 38)
        {
            ++this.iIndex;
        }
        if (key == 40)
        {
            --this.iIndex;
        }
        if (this.iIndex < 0)
        {
            this.iIndex = 0;
        }
        if (this.iIndex >= this.frames)
        {
            this.iIndex = this.frames - 1;
        }
        this.repaint();
    }

    @Override public void keyReleased(KeyEvent e)
    {
    }

    @Override public void keyTyped(KeyEvent e)
    {
    }

    public class TransparentPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener
    {
        public TransparentPanel()
        {
            this.setBackground(new Color(0, 0, 0, 0));
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.addMouseWheelListener(this);
        }

        @Override public void paintComponent(Graphics g)
        {
            if (TransparentIcon.this.iIndex != TransparentIcon.this.iIndexLast)
            {
                TransparentIcon.this.iIndexLast = TransparentIcon.this.iIndex;
                if (TransparentIcon.this.horz != 0)
                {
                    TransparentIcon.this.bmpStrip.DecimationTo(
                        TransparentIcon.this.bmpIcon, 0, 0, TransparentIcon.this.wout, TransparentIcon.this.hout,
                        TransparentIcon.this.iIndex * TransparentIcon.this.width, 0, TransparentIcon.this.width,
                        TransparentIcon.this.height);
                }
                else
                {
                    TransparentIcon.this.bmpStrip.DecimationTo(
                        TransparentIcon.this.bmpIcon, 0, 0, TransparentIcon.this.wout, TransparentIcon.this.hout, 0,
                        TransparentIcon.this.iIndex * TransparentIcon.this.height, TransparentIcon.this.width,
                        TransparentIcon.this.height);
                }
            }
            Graphics2D g2 = (Graphics2D)g;
            Point pos = this.getLocationOnScreen();
            Point offset = new Point(-pos.x, -pos.y);
            g2.setComposite(AlphaComposite.Src);
            if (GUIEditor.getInstance().iUseCapture != 0)
            {
                g2.drawImage(TransparentIcon.this.imgBackground, offset.x, offset.y, null);
                g2.setComposite(AlphaComposite.SrcOver);
            }
            else
            {
                g2.setBackground(new Color(0, 0, 0, 0));
                g2.clearRect(0, 0, TransparentIcon.this.wout + 32, TransparentIcon.this.hout + 32);
            }
            g2.setComposite(AlphaComposite.SrcOver);
            if (TransparentIcon.this.bmpIcon != null)
            {
                g2.drawImage(TransparentIcon.this.bmpIcon.img, 16, 16, TransparentIcon.this.wout + 16,
                             TransparentIcon.this.hout + 16, 0, 0, TransparentIcon.this.wout, TransparentIcon.this.hout,
                             null);
            }
        }

        @Override public void mouseMoved(MouseEvent e)
        {
        }

        @Override public void mouseDragged(MouseEvent e)
        {
            Point pt = e.getLocationOnScreen();
            if ((TransparentIcon.this.mod & 0x40) != 0)
            {
                int i = TransparentIcon.this.iIndexStart - e.getY() + TransparentIcon.this.yStart;
                GUIEditor.getInstance().prevpanel.SetFrame(i);
                this.SetFrame(i);
            }
            else
            {
                TransparentIcon.this.frame.setBounds(pt.x - (TransparentIcon.this.wout + 32) / 2,
                                                     pt.y - (TransparentIcon.this.hout + 32) / 2,
                                                     TransparentIcon.this.wout + 32, TransparentIcon.this.hout + 32);
            }
        }

        @Override public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() >= 2)
            {
                GUIEditor.getInstance().ticon = null;
                GUIEditor.getInstance().cbmTest.setSelected(false);
                TransparentIcon.this.frame.dispose();
            }
        }

        @Override public void mouseEntered(MouseEvent e)
        {
        }

        @Override public void mouseExited(MouseEvent e)
        {
        }

        @Override public void mousePressed(MouseEvent e)
        {
            TransparentIcon.this.mod = e.getModifiersEx();
            if ((TransparentIcon.this.mod & 0x40) != 0)
            {
                TransparentIcon.this.yStart = e.getY();
                TransparentIcon.this.iIndexStart = TransparentIcon.this.iIndex;
            }
        }

        @Override public void mouseReleased(MouseEvent e)
        {
            if (SwingUtilities.isRightMouseButton(e))
            {
                GUIEditor.getInstance().ticon = null;
                GUIEditor.getInstance().cbmTest.setSelected(false);
                TransparentIcon.this.frame.dispose();
            }
        }

        @Override public void mouseWheelMoved(MouseWheelEvent e)
        {
            int iInv = GUIEditor.getInstance().iWheelDir != 0 ? -1 : 1;
            int i = TransparentIcon.this.iIndex - iInv * e.getWheelRotation();
            GUIEditor.getInstance().prevpanel.SetFrame(i);
            this.SetFrame(i);
        }

        public void SetFrame(int i)
        {
            TransparentIcon.this.iIndex = i;
            if (TransparentIcon.this.iIndex < 0)
            {
                TransparentIcon.this.iIndex = 0;
            }
            if (TransparentIcon.this.iIndex >= TransparentIcon.this.frames)
            {
                TransparentIcon.this.iIndex = TransparentIcon.this.frames - 1;
            }
            this.repaint();
        }
    }
}

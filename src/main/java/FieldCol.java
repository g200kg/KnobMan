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
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class FieldCol extends JPanel implements ActionListener, UpdateReq
{
    JLabel lb;
    FieldVal fih;
    FieldVal fil;
    FieldVal fis;
    FieldVal fir;
    FieldVal fig;
    FieldVal fib;
    ParamCol pc;
    ParamI pih;
    ParamI pil;
    ParamI pis;
    ParamI pir;
    ParamI pig;
    ParamI pib;
    ColMap map;
    PalButton[] btnPal;
    ColorPanel cp;
    Picker pick;
    boolean bDisableLoop = true;
    UpdateReq ur;
    int m;

    public FieldCol(Container frame, int x, int y, String name, UpdateReq ur, int m)
    {
        this.ur = ur;
        this.m = m;
        this.map = null;
        this.setLayout(null);
        this.setBounds(x, y, 300, 220);
        this.lb = new JLabel(name);
        this.lb.setFont(GUIEditor.getInstance().fontUI);
        this.add(this.lb);
        this.btnPal = new PalButton[24];
        for (int i = 0; i < 24; ++i)
        {
            this.btnPal[i] = new PalButton(i);
            this.btnPal[i].setBounds((i & 7) * 20, 160 + i / 8 * 16, 20, 16);
            this.add(this.btnPal[i]);
            this.btnPal[i].addActionListener(this);
        }
        this.fih = new FieldVal(this, x + 140, 22, "H:", 20, 70, false, 0.0, 240.0, this, 0);
        this.fil = new FieldVal(this, x + 140, 44, "L:", 20, 70, false, 0.0, 240.0, this, 0);
        this.fis = new FieldVal(this, x + 140, 66, "S:", 20, 70, false, 0.0, 240.0, this, 0);
        this.fir = new FieldVal(this, x + 140, 88, "R:", 20, 70, false, 0.0, 255.0, this, 1);
        this.fig = new FieldVal(this, x + 140, 110, "G:", 20, 70, false, 0.0, 255.0, this, 1);
        this.fib = new FieldVal(this, x + 140, 132, "B:", 20, 70, false, 0.0, 255.0, this, 1);
        this.pih = new ParamI(0);
        this.pil = new ParamI(0);
        this.pis = new ParamI(0);
        this.pir = new ParamI(0);
        this.pig = new ParamI(0);
        this.pib = new ParamI(0);
        this.fih.Setup(this.pih);
        this.fil.Setup(this.pil);
        this.fis.Setup(this.pis);
        this.fir.Setup(this.pir);
        this.fig.Setup(this.pig);
        this.fib.Setup(this.pib);
        this.map = new ColMap(0, 22);
        this.add(this.map);
        this.lb.setBounds(0, 0, 120, 20);
        this.cp = new ColorPanel();
        this.cp.setBounds(170, 160, 70, 24);
        this.add(this.cp);
        this.pick = new Picker();
        this.pick.setBounds(170, 185, 70, 24);
        this.add(this.pick);
        frame.add(this);
        this.pc = null;
        this.bDisableLoop = false;
    }

    public void Setup(ParamCol pc)
    {
        this.bDisableLoop = true;
        this.pc = pc;
        this.pir.Update(pc.col.r);
        this.pig.Update(pc.col.g);
        this.pib.Update(pc.col.b);
        this.fir.Setup();
        this.fig.Setup();
        this.fib.Setup();
        pc.col.rgbtohls();
        this.pih.Update(pc.col.h);
        this.pil.Update(pc.col.l);
        this.pis.Update(pc.col.s);
        this.fih.Setup();
        this.fil.Setup();
        this.fis.Setup();
        this.cp.SetColor(this.pc.col);
        if (this.map != null)
        {
            this.map.SetHls(pc.col.h, pc.col.l, pc.col.s, true);
        }
        this.bDisableLoop = false;
    }

    @Override public void Update(int m)
    {
        if (this.pc != null && !this.bDisableLoop)
        {
            if ((m & 0xFF) == 0)
            {
                this.pc.col.h = this.pih.val;
                this.pc.col.l = this.pil.val;
                this.pc.col.s = this.pis.val;
                this.map.SetHls(this.pc.col.h, this.pc.col.l, this.pc.col.s, true);
                this.pc.col.hlstorgb();
                this.pir.val = this.pc.col.r;
                this.pig.val = this.pc.col.g;
                this.pib.val = this.pc.col.b;
                this.fir.Setup();
                this.fig.Setup();
                this.fib.Setup();
                this.pc.Update(this.pc.col);
            }
            else
            {
                this.pc.col.r = this.pir.val;
                this.pc.col.g = this.pig.val;
                this.pc.col.b = this.pib.val;
                this.pc.col.rgbtohls();
                this.map.SetHls(this.pc.col.h, this.pc.col.l, this.pc.col.s, false);
                this.pih.val = this.pc.col.h;
                this.pil.val = this.pc.col.l;
                this.pis.val = this.pc.col.s;
                this.fih.Setup();
                this.fil.Setup();
                this.fis.Setup();
                this.pc.Update(this.pc.col);
            }
            if (this.cp != null)
            {
                this.cp.SetColor(this.pc.col);
            }
            if (this.ur != null)
            {
                this.ur.Update(this.m + (m & 0x8000));
            }
        }
    }

    @Override public void actionPerformed(ActionEvent e)
    {
    }

    class ColMap extends BitmapView implements MouseListener, MouseMotionListener
    {
        public Bitmap bmpMap;
        int iDragMode;

        public ColMap(int x, int y)
        {
            super(x, y, 137, 121, null, null);
            this.bmpMap = new Bitmap(137, 121);
            this.SetHls(0, 0, 0, true);
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.iDragMode = 0;
        }

        public void SetCol(Col c, boolean mode)
        {
            Col hls = new Col(c);
            hls.rgbtohls();
            this.SetHls(hls.h, hls.l, hls.s, mode);
        }

        public Col SetHls(int h, int l, int s, boolean mode)
        {
            int x;
            Col col = new Col(0, 0, 0);
            for (int y = 0; y <= 120; ++y)
            {
                this.x = 0;
                while (this.x <= 120)
                {
                    col.SetHls(this.x * 2, 120, 240 - y * 2);
                    this.bmpMap.SetPix(this.x, y, 255, col.r, col.g, col.b);
                    ++this.x;
                }
                for (x = 121; x < 129; ++x)
                {
                    this.bmpMap.SetPix(x, y, 255, 255, 255, 255);
                }
                col.SetHls(h, (120 - y) * 2, s);
                for (x = 129; x < 137; ++x)
                {
                    this.bmpMap.SetPix(x, y, 255, col.r, col.g, col.b);
                }
            }
            int py = 120 - l / 2;
            for (x = 1; x < 6; ++x)
            {
                for (int y = -x; y < x; ++y)
                {
                    if (py + y < 0 || py + y >= 120)
                        continue;
                    this.bmpMap.SetPix(128 - x, py + y, 255, 0, 0, 0);
                }
            }
            int px = h / 2;
            py = 120 - s / 2;
            col.SetHls(h, l, s);
            FieldCol.this.pih.val = h;
            FieldCol.this.pil.val = l;
            FieldCol.this.pis.val = s;
            if (mode)
            {
                this.Setup(this.bmpMap);
            }
            else
            {
                this.Setup(this.bmpMap);
            }
            this.SetupCurPos(px, py);
            return col;
        }

        void SetMap(Point pt, boolean mode)
        {
            int s;
            int h = pt.x * 2;
            if (h < 0)
            {
                h = 0;
            }
            if (h > 240)
            {
                h = 240;
            }
            if ((s = 240 - pt.y * 2) < 0)
            {
                s = 0;
            }
            if (s > 240)
            {
                s = 240;
            }
            this.SetHls(h, FieldCol.this.pil.val, s, mode);
            Col col = new Col(0, 0, 0);
            col.SetHls(h, FieldCol.this.pil.val, s);
            FieldCol.this.pc.col = col;
            FieldCol.this.pir.val = col.r;
            FieldCol.this.pig.val = col.g;
            FieldCol.this.pib.val = col.b;
            FieldCol.this.fir.Setup();
            FieldCol.this.fig.Setup();
            FieldCol.this.fib.Setup();
            FieldCol.this.pih.val = col.h;
            FieldCol.this.pil.val = col.l;
            FieldCol.this.pis.val = col.s;
            FieldCol.this.fih.Setup();
            FieldCol.this.fil.Setup();
            FieldCol.this.fis.Setup();
            if (FieldCol.this.cp != null)
            {
                FieldCol.this.cp.SetColor(col);
            }
        }

        void SetBar(Point pt, boolean mode)
        {
            Col col;
            int l = 240 - pt.y * 2;
            if (l < 0)
            {
                l = 0;
            }
            if (l > 240)
            {
                l = 240;
            }
            FieldCol.this.pc.col = col = this.SetHls(FieldCol.this.pih.val, l, FieldCol.this.pis.val, mode);
            FieldCol.this.pir.val = col.r;
            FieldCol.this.pig.val = col.g;
            FieldCol.this.pib.val = col.b;
            FieldCol.this.fir.Setup();
            FieldCol.this.fig.Setup();
            FieldCol.this.fib.Setup();
            FieldCol.this.pih.val = col.h;
            FieldCol.this.pil.val = col.l;
            FieldCol.this.pis.val = col.s;
            FieldCol.this.fih.Setup();
            FieldCol.this.fil.Setup();
            FieldCol.this.fis.Setup();
            if (FieldCol.this.cp != null)
            {
                FieldCol.this.cp.SetColor(col);
            }
        }

        @Override public void mouseClicked(MouseEvent e)
        {
        }

        @Override public void mousePressed(MouseEvent e)
        {
            Control.getInstance().journal.Write();
            Point pt = e.getPoint();
            if (pt.x <= 120)
            {
                this.iDragMode = 1;
                this.SetMap(pt, true);
            }
            else
            {
                this.iDragMode = 2;
                this.SetBar(pt, true);
            }
            if (FieldCol.this.ur != null)
            {
                FieldCol.this.ur.Update(FieldCol.this.m);
            }
        }

        @Override public void mouseReleased(MouseEvent e)
        {
            this.iDragMode = 0;
        }

        @Override public void mouseEntered(MouseEvent e)
        {
        }

        @Override public void mouseExited(MouseEvent e)
        {
        }

        @Override public void mouseMoved(MouseEvent e)
        {
        }

        @Override public void mouseDragged(MouseEvent e)
        {
            Point pt = e.getPoint();
            if (this.iDragMode == 2)
            {
                this.SetBar(pt, true);
            }
            if (this.iDragMode == 1)
            {
                this.SetMap(pt, true);
            }
            if (FieldCol.this.ur != null)
            {
                FieldCol.this.ur.Update(FieldCol.this.m);
            }
        }
    }

    class ColorPanel extends JTextField implements ActionListener
    {
        ColorPanel()
        {
            this.setOpaque(true);
            this.setHorizontalAlignment(0);
            this.addActionListener(this);
        }

        public void SetColor(Col c)
        {
            this.setBackground(new Color(c.r, c.g, c.b));
            if (c.r * 3 + c.g * 6 + c.b > 1280)
            {
                this.setForeground(Color.black);
            }
            else
            {
                this.setForeground(Color.white);
            }
            this.setText(String.format("#%02x%02x%02x", c.r, c.g, c.b));
        }

        @Override public void actionPerformed(ActionEvent e)
        {
            Control.getInstance().journal.Write();
            String s = this.getText();
            if (s.startsWith("#"))
            {
                s = s.substring(1);
            }
            if (s.startsWith("0x"))
            {
                s = s.substring(2);
            }
            int v = Integer.parseInt(s, 16);
            Col col = new Col(v);
            this.SetColor(col);
            FieldCol.this.map.SetCol(col, true);
            FieldCol.this.pc.col = col;
            FieldCol.this.Setup(FieldCol.this.pc);
            if (FieldCol.this.ur != null)
            {
                FieldCol.this.ur.Update(FieldCol.this.m + (FieldCol.this.m & 0x8000));
            }
        }
    }

    class MyDragGestureListener implements DragGestureListener
    {
        MyDragGestureListener()
        {
        }

        @Override public void dragGestureRecognized(DragGestureEvent dge)
        {
            DragSourceAdapter dsa = new DragSourceAdapter() {
                @Override public void dragDropEnd(DragSourceDropEvent dsde)
                {
                    dsde.getDropSuccess();
                }
            };
            dge.startDrag(DragSource.DefaultMoveDrop, null, dsa);
        }
    }

    class PalButton extends JButton implements MouseListener, ActionListener
    {
        int iPalIndex;
        int iRClick;
        JPopupMenu popup;

        public PalButton(int v)
        {
            this.iPalIndex = v;
            this.popup = new JPopupMenu();
            JMenuItem item = new JMenuItem("Register");
            item.addActionListener(this);
            item.setActionCommand("Reg");
            this.popup.add(item);
            this.setFocusable(false);
            this.addMouseListener(this);
            this.addActionListener(this);
            this.iRClick = 0;
        }

        @Override public void actionPerformed(ActionEvent e)
        {
            Control.getInstance().journal.Write();
            if (e.getActionCommand().equals("Reg"))
            {
                int val;
                Control.getInstance().pal[this.iPalIndex] = val =
                    FieldCol.this.pib.val << 16 | FieldCol.this.pig.val << 8 | FieldCol.this.pir.val;
                this.repaint();
            }
            else if (this.iRClick == 0)
            {
                for (int i = 0; i < 24; ++i)
                {
                    if (e.getSource() != FieldCol.this.btnPal[i])
                        continue;
                    int val = Control.getInstance().pal[FieldCol.this.btnPal[i].iPalIndex];
                    val = val >> 16 & 0xFF | val & 0xFF00 | val << 16 & 0xFF0000;
                    Col col = new Col(val);
                    FieldCol.this.map.SetCol(col, true);
                    FieldCol.this.pc.col = col;
                    FieldCol.this.Setup(FieldCol.this.pc);
                    if (FieldCol.this.cp != null)
                    {
                        FieldCol.this.cp.SetColor(col);
                    }
                    if (FieldCol.this.ur != null)
                    {
                        FieldCol.this.ur.Update(FieldCol.this.m + (FieldCol.this.m & 0x8000));
                    }
                    return;
                }
            }
        }

        @Override public void paintComponent(Graphics g)
        {
            int val = Control.getInstance().pal[this.iPalIndex];
            val = val >> 16 & 0xFF | val & 0xFF00 | val << 16 & 0xFF0000;
            g.setColor(Color.white);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(new Color(val));
            g.fillRect(1, 1, this.getWidth() - 2, this.getHeight() - 2);
        }

        @Override public void mouseClicked(MouseEvent e)
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
            if (SwingUtilities.isRightMouseButton(e))
            {
                this.popup.show(e.getComponent(), e.getX(), e.getY());
                this.iRClick = 1;
            }
            else
            {
                this.iRClick = 0;
            }
        }

        @Override public void mouseReleased(MouseEvent e)
        {
        }
    }

    class Picker extends JPanel implements MouseListener, MouseMotionListener
    {
        BufferedImage img;
        JFrame win;
        PickerCursor pcur;

        Picker()
        {
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.win = new JFrame();
            this.pcur = new PickerCursor();
            this.win.add(this.pcur);
            this.win.setUndecorated(true);
            this.win.setBackground(new Color(0, 0, 0, 0));
            this.win.setAlwaysOnTop(true);
        }

        @Override public void paintComponent(Graphics g)
        {
            Rectangle rc = this.getBounds();
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, rc.width, rc.height);
            g.setColor(Color.white);
            g.fillRect(1, 1, rc.width - 2, rc.height - 2);
            g.drawImage(GUIEditor.getInstance().iconPicker.getImage(), 0, 0, null);
        }

        @Override public void mouseMoved(MouseEvent e)
        {
        }

        @Override public void mouseDragged(MouseEvent e)
        {
            Point pt = e.getLocationOnScreen();
            try
            {
                Robot rbt = new Robot();
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension dim = new Dimension(1, 1);
                this.img = rbt.createScreenCapture(new Rectangle(pt.x - 32, pt.y - 18, 128, 36));
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            this.pcur.repaint();
            this.win.setBounds(pt.x - 32, pt.y - 18, 128, 128);
            int val = this.img.getRGB(18, 18);
            Col col = new Col(val);
            col.rgbtohls();
            FieldCol.this.map.SetCol(col, true);
            FieldCol.this.pc.col = col;
            FieldCol.this.pir.val = col.r;
            FieldCol.this.pig.val = col.g;
            FieldCol.this.pib.val = col.b;
            FieldCol.this.fir.Setup();
            FieldCol.this.fig.Setup();
            FieldCol.this.fib.Setup();
            FieldCol.this.pih.val = col.h;
            FieldCol.this.pil.val = col.l;
            FieldCol.this.pis.val = col.s;
            FieldCol.this.fih.Setup();
            FieldCol.this.fil.Setup();
            FieldCol.this.fis.Setup();
            if (FieldCol.this.cp != null)
            {
                FieldCol.this.cp.SetColor(col);
            }
            if (FieldCol.this.ur != null)
            {
                FieldCol.this.ur.Update(FieldCol.this.m + (FieldCol.this.m & 0x8000));
            }
        }

        @Override public void mousePressed(MouseEvent e)
        {
            Control.getInstance().journal.Write();
            Point pt = e.getLocationOnScreen();
            this.win.setBounds(pt.x - 32, pt.y - 18, 128, 128);
            this.win.setVisible(true);
        }

        @Override public void mouseReleased(MouseEvent e)
        {
            this.win.dispose();
            Point pt = e.getLocationOnScreen();
            try
            {
                Robot rbt = new Robot();
                Toolkit tk = Toolkit.getDefaultToolkit();
                Dimension dim = new Dimension(1, 1);
                this.img = rbt.createScreenCapture(new Rectangle(pt.x, pt.y, 1, 1));
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }
            int val = this.img.getRGB(0, 0);
            FieldCol.this.map.SetCol(new Col(val), true);
        }

        @Override public void mouseEntered(MouseEvent e)
        {
        }

        @Override public void mouseExited(MouseEvent e)
        {
        }

        @Override public void mouseClicked(MouseEvent e)
        {
        }

        class PickerCursor extends JPanel
        {
            PickerCursor()
            {
                Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(
                    GUIEditor.getInstance().iconPickerCursorNull.getImage(), new Point(0, 0), "nullcur");
                this.setCursor(cur);
                this.setBackground(new Color(0, 0, 0, 0));
                this.setOpaque(false);
            }

            @Override public void paintComponent(Graphics g)
            {
                Graphics2D g2 = (Graphics2D)g;
                g2.setComposite(AlphaComposite.Src);
                g2.setBackground(new Color(0, 0, 0, 0));
                g2.clearRect(0, 0, 128, 128);
                g2.setComposite(AlphaComposite.SrcOver);
                g2.drawImage(GUIEditor.getInstance().iconPickerCursor.getImage(), 0, 0, null);
                g2.drawImage(Picker.this.img, 1, 37, 127, 127, 14, 15, 23, 22, null);
                g2.setColor(Color.black);
                g2.fillRect(63, 37, 3, 37);
                g2.fillRect(63, 90, 3, 37);
                g2.fillRect(0, 81, 52, 3);
                g2.fillRect(75, 81, 52, 3);
                g2.setColor(Color.white);
                g2.fillRect(64, 38, 1, 35);
                g2.fillRect(64, 92, 1, 35);
                g2.fillRect(1, 82, 50, 1);
                g2.fillRect(77, 82, 50, 1);
            }
        }
    }
}

/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CurveEditor extends JPanel implements ActionListener, FocusListener, UpdateReq
{
    AnimCurve ac;
    int mouseX;
    int mouseY;
    JButton btnOk;
    JTextField[] tfL;
    JTextField[] tfT;
    JLabel[] lbL;
    JLabel[] lbT;
    Graph graph;
    FieldVal stepreso;
    JLabel lbTitle;
    String labelform;

    public CurveEditor(ProfileReader ppr)
    {
        ppr.SetSection("CurveEditor");
        this.setLayout(null);
        this.labelform = ppr.ReadString("curvelabel", "Curve-%d");
        this.lbTitle = new JLabel("");
        this.lbTitle.setBounds(20, 10, 100, 30);
        this.add(this.lbTitle);
        this.graph = new Graph(this);
        this.graph.setBackground(Color.gray);
        this.graph.setBounds(50, 50, 320, 320);
        this.add(this.graph);
        this.stepreso =
            new FieldVal(this, 20, 390, ppr.ReadString("stepreso", "StepReso:"), 100, 100, false, 0.0, 100.0, this, 0);
        this.tfL = new JTextField[12];
        this.tfT = new JTextField[12];
        this.lbL = new JLabel[12];
        this.lbT = new JLabel[12];
        for (int i = 0; i < 12; ++i)
        {
            if (i >= 1 && i < 11)
            {
                this.lbT[i] = new JLabel("T" + i);
                this.lbT[i].setBounds(420, 50 + i * 25, 30, 25);
                this.lbT[i].setHorizontalAlignment(0);
                this.add(this.lbT[i]);
                this.tfT[i] = new JTextField();
                this.tfT[i].setBounds(460, 50 + i * 25, 40, 25);
                this.tfT[i].setHorizontalAlignment(0);
                this.tfT[i].addActionListener(this);
                this.tfT[i].addFocusListener(this);
                this.add(this.tfT[i]);
            }
            this.lbL[i] = new JLabel("L" + i);
            this.lbL[i].setBounds(520, 50 + i * 25, 30, 25);
            this.lbL[i].setHorizontalAlignment(0);
            this.add(this.lbL[i]);
            this.tfL[i] = new JTextField();
            this.tfL[i].setBounds(560, 50 + i * 25, 40, 25);
            this.tfL[i].setHorizontalAlignment(0);
            this.tfL[i].addActionListener(this);
            this.tfL[i].addFocusListener(this);
            this.add(this.tfL[i]);
        }
        this.btnOk = new JButton(ppr.ReadString("close", "Close"));
        this.btnOk.setBounds(275, 390, 100, 30);
        this.add(this.btnOk);
        this.btnOk.addActionListener(this);
    }

    void Setup(AnimCurve ac, int i)
    {
        this.ac = ac;
        this.stepreso.Setup(ac.stepreso);
        this.lbTitle.setText(String.format(this.labelform, i));
        this.Draw();
    }

    public void Apply()
    {
    }

    public void Draw()
    {
        int i;
        for (i = 0; i < 12; ++i)
        {
            this.tfL[i].setText(Integer.toString(this.ac.lv[i]));
        }
        for (i = 1; i < 11; ++i)
        {
            this.tfT[i].setText(Integer.toString(this.ac.tm[i]));
        }
        Control.getInstance().Update(Control.Up_Render);
    }

    public void UpdateValue(Object o)
    {
        int i;
        for (i = 1; i < 12; ++i)
        {
            if (o != this.tfT[i])
                continue;
            this.ac.tm[i] = Integer.parseInt(this.tfT[i].getText());
            if (this.ac.lv[i] >= 0)
                continue;
            this.ac.lv[i] = 0;
            this.tfL[i].setText("0");
        }
        for (i = 0; i < 12; ++i)
        {
            if (o != this.tfL[i])
                continue;
            this.ac.lv[i] = Integer.parseInt(this.tfL[i].getText());
        }
        this.Draw();
        this.graph.repaint();
    }

    @Override public void focusLost(FocusEvent e)
    {
        this.UpdateValue(e.getSource());
    }

    @Override public void focusGained(FocusEvent e)
    {
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        Object o = e.getSource();
        if (o == this.btnOk)
        {
            GUIEditor.getInstance().SetupLayer(Control.getInstance().GetCurrentLayer());
        }
        else
        {
            this.UpdateValue(o);
        }
    }

    @Override public void Update(int m)
    {
        Control.getInstance().Edit();
        this.Draw();
        this.graph.repaint();
    }

    class Graph extends JPanel implements MouseMotionListener, MouseListener
    {
        int drag;
        CurveEditor ce;

        public Graph(CurveEditor ce)
        {
            this.ce = ce;
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.drag = -1;
            CurveEditor.this.ac = null;
        }

        @Override public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.white);
            g2.fillRect(0, 0, 320, 10);
            g2.fillRect(0, 310, 320, 10);
            g2.fillRect(0, 0, 10, 320);
            g2.fillRect(310, 0, 10, 320);
            g2.setColor(Color.lightGray);
            g2.fillRect(10, 10, 300, 300);
            g2.setColor(Color.gray);
            g2.drawRect(10, 10, 300, 300);
            for (int i = 10; i < 310; i += 30)
            {
                g2.drawLine(10, i, 310, i);
                g2.drawLine(i, 10, i, 310);
            }
            if (CurveEditor.this.ac != null)
            {
                int i;
                int ly;
                if (CurveEditor.this.ac.stepreso.val > 0)
                {
                    g2.setColor(Color.red);
                    ly = 310;
                    for (int x = 0; x < 100; ++x)
                    {
                        double r = CurveEditor.this.ac.GetVal((double)x * 0.01);
                        int y = 310 - (int)(300.0 * r);
                        g2.drawLine(10 + x * 3, ly, 10 + x * 3, y);
                        g2.drawLine(10 + x * 3, y, 10 + x * 3 + 3, y);
                        ly = y;
                    }
                }
                g2.setColor(Color.blue);
                g2.setStroke(new BasicStroke(2.0f));
                for (i = 0; i < 12; ++i)
                {
                    if (CurveEditor.this.ac.tm[i] < 0 || CurveEditor.this.ac.lv[i] < 0)
                        continue;
                    g2.fillRect(CurveEditor.this.ac.tm[i] * 3 + 10 - 3, 310 - CurveEditor.this.ac.lv[i] * 3 - 3, 6, 6);
                }
                int lx = 10;
                ly = 310 - CurveEditor.this.ac.lv[0] * 3;
                for (i = 1; i < 12; ++i)
                {
                    if (CurveEditor.this.ac.tm[i] < 0 || CurveEditor.this.ac.lv[i] < 0)
                        continue;
                    int px = 10 + CurveEditor.this.ac.tm[i] * 3;
                    int py = 310 - CurveEditor.this.ac.lv[i] * 3;
                    g2.drawLine(lx, ly, px, py);
                    lx = px;
                    ly = py;
                }
            }
        }

        void Getxy(MouseEvent e)
        {
            Point pt = e.getPoint();
            CurveEditor.this.mouseX = pt.x;
            CurveEditor.this.mouseY = pt.y;
        }

        double HitTest(int x, int y)
        {
            x = (x - 10) / 3;
            y = 100 - (y - 10) / 3;
            for (int i = 0; i < 12; ++i)
            {
                if (Math.abs(CurveEditor.this.ac.tm[i] - x) + Math.abs(CurveEditor.this.ac.lv[i] - y) >= 8)
                    continue;
                return i;
            }
            int lx = 0;
            int ly = CurveEditor.this.ac.lv[0];
            int li = 0;
            for (int i = 1; i < 12; ++i)
            {
                if (CurveEditor.this.ac.tm[i] < 0)
                    continue;
                if (x > lx && x < CurveEditor.this.ac.tm[i] &&
                    Math.abs(ly + (CurveEditor.this.ac.lv[i] - ly) * (x - lx) / (CurveEditor.this.ac.tm[i] - lx) - y) <
                        8)
                {
                    return (double)li + 0.5;
                }
                li = i;
                lx = CurveEditor.this.ac.tm[i];
                ly = CurveEditor.this.ac.lv[i];
            }
            return -1.0;
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

        @Override public void mousePressed(MouseEvent e)
        {
            this.Getxy(e);
            double t = this.HitTest(CurveEditor.this.mouseX, CurveEditor.this.mouseY);
            if (Math.floor(t) != t)
            {
                if (CurveEditor.this.ac.tm[10] < 0)
                {
                    int i = 10;
                    while ((double)i > Math.floor(t))
                    {
                        CurveEditor.this.ac.tm[i] = CurveEditor.this.ac.tm[i - 1];
                        CurveEditor.this.ac.lv[i] = CurveEditor.this.ac.lv[i - 1];
                        --i;
                    }
                    this.drag = (int)Math.floor(t) + 1;
                }
            }
            else
            {
                this.drag = (int)Math.floor(t);
            }
        }

        @Override public void mouseReleased(MouseEvent e)
        {
            if (this.drag >= 0)
            {
                for (int i = 1; i < 10; ++i)
                {
                    if (CurveEditor.this.ac.tm[i] >= 0)
                        continue;
                    int t = CurveEditor.this.ac.tm[i];
                    CurveEditor.this.ac.tm[i] = CurveEditor.this.ac.tm[i + 1];
                    CurveEditor.this.ac.tm[i + 1] = t;
                    t = CurveEditor.this.ac.lv[i];
                    CurveEditor.this.ac.lv[i] = CurveEditor.this.ac.lv[i + 1];
                    CurveEditor.this.ac.lv[i + 1] = t;
                }
                this.repaint();
            }
            this.drag = -1;
        }

        @Override public void mouseMoved(MouseEvent e)
        {
        }

        @Override public void mouseDragged(MouseEvent e)
        {
            this.Getxy(e);
            if (this.drag >= 0)
            {
                int x = (CurveEditor.this.mouseX - 10) / 3;
                int y = 100 - (CurveEditor.this.mouseY - 10) / 3;
                if (y < 0 || y > 100 || x <= 0 || x >= 100)
                {
                    if (this.drag != 0 && this.drag != 11)
                    {
                        CurveEditor.this.ac.tm[this.drag] = -1;
                        CurveEditor.this.ac.lv[this.drag] = -1;
                    }
                    else
                    {
                        if (y < 0)
                        {
                            CurveEditor.this.ac.lv[this.drag] = 0;
                        }
                        if (y > 100)
                        {
                            CurveEditor.this.ac.lv[this.drag] = 100;
                        }
                    }
                }
                else
                {
                    CurveEditor.this.ac.lv[this.drag] = y;
                    if (this.drag != 0 && this.drag != 11)
                    {
                        int t;
                        CurveEditor.this.ac.tm[this.drag] = x;
                        if (CurveEditor.this.ac.tm[this.drag + 1] >= 0 && x > CurveEditor.this.ac.tm[this.drag + 1])
                        {
                            CurveEditor.this.ac.tm[this.drag] = CurveEditor.this.ac.tm[this.drag + 1];
                            CurveEditor.this.ac.tm[this.drag + 1] = x;
                            t = CurveEditor.this.ac.lv[this.drag];
                            CurveEditor.this.ac.lv[this.drag] = CurveEditor.this.ac.lv[this.drag + 1];
                            CurveEditor.this.ac.lv[this.drag + 1] = t;
                            ++this.drag;
                        }
                        if (x < CurveEditor.this.ac.tm[this.drag - 1])
                        {
                            CurveEditor.this.ac.tm[this.drag] = CurveEditor.this.ac.tm[this.drag - 1];
                            CurveEditor.this.ac.tm[this.drag - 1] = x;
                            t = CurveEditor.this.ac.lv[this.drag];
                            CurveEditor.this.ac.lv[this.drag] = CurveEditor.this.ac.lv[this.drag - 1];
                            CurveEditor.this.ac.lv[this.drag - 1] = t;
                            --this.drag;
                        }
                    }
                }
                Control.getInstance().Edit();
                this.repaint();
                this.ce.Draw();
            }
        }
    }
}

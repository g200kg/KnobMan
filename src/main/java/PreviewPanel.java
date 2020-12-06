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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

public class PreviewPanel
    extends JPanel implements KeyListener, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    Control ctl = Control.getInstance();
    Prefs prefs;
    GUIEditor editor;
    public JScrollPane scr;
    Pane panel;
    JToggleButton btnPrev;
    JToggleButton btnRender;
    JToggleButton btnTest;
    JButton btnZoomOut;
    JButton btnZoom1;
    JButton btnZoomIn;
    JLabel lbFrame;
    int iZoom;
    int iY0;
    int iTest0;

    public PreviewPanel(GUIEditor editor, ProfileReader ppr)
    {
        this.prefs = this.ctl.prefs;
        this.editor = editor;
        this.setLayout(new BorderLayout());
        JPanel toolpanel = new JPanel();
        toolpanel.setLayout(new BoxLayout(toolpanel, 1));
        JPanel row1 = new JPanel();
        row1.setLayout(new BoxLayout(row1, 0));
        row1.setAlignmentX(0.0f);
        JPanel row2 = new JPanel();
        row2.setLayout(new BoxLayout(row2, 0));
        row2.setAlignmentX(0.0f);
        this.add((Component)toolpanel, "North");
        this.panel = new Pane();
        this.panel.setPreferredSize(new Dimension(144, 400));
        this.panel.setBackground(Color.white);
        ButtonGroup group = new ButtonGroup();
        String sPrev = ppr.ReadString("preview", "Preview");
        this.btnPrev = new JToggleButton(PartStr.Get(sPrev, 0));
        this.btnPrev.setFont(editor.fontUISmall);
        this.btnPrev.setIcon(editor.iconPreview);
        this.btnPrev.setToolTipText(PartStr.Get(sPrev, 1));
        String sRender = ppr.ReadString("render", "Render");
        this.btnRender = new JToggleButton(PartStr.Get(sRender, 0));
        this.btnRender.setFont(editor.fontUISmall);
        this.btnRender.setIcon(editor.iconRender);
        this.btnRender.setToolTipText(PartStr.Get(sRender, 1));
        String sTest = ppr.ReadString("test", "Test");
        this.btnTest = new JToggleButton(PartStr.Get(sTest, 0));
        this.btnTest.setFont(editor.fontUISmall);
        this.btnTest.setIcon(editor.iconTest);
        this.btnTest.setToolTipText(PartStr.Get(sTest, 1));
        group.add(this.btnPrev);
        group.add(this.btnRender);
        group.add(this.btnTest);
        String sZO = ppr.ReadString("zoomout", "Zoom Out");
        this.btnZoomOut = new JButton(PartStr.Get(sZO, 0));
        this.btnZoomOut.setFont(editor.fontUISmall);
        this.btnZoomOut.setIcon(editor.iconZoomout);
        this.btnZoomOut.setToolTipText(PartStr.Get(sZO, 1));
        String sZ1 = ppr.ReadString("zoom1", "1:1");
        this.btnZoom1 = new JButton(PartStr.Get(sZ1, 0));
        this.btnZoom1.setFont(editor.fontUISmall);
        this.btnZoom1.setIcon(editor.iconZoom1);
        this.btnZoom1.setToolTipText(PartStr.Get(sZ1, 1));
        String sZI = ppr.ReadString("zoomin", "Zoom In");
        this.btnZoomIn = new JButton(PartStr.Get(sZI, 0));
        this.btnZoomIn.setFont(editor.fontUISmall);
        this.btnZoomIn.setIcon(editor.iconZoomin);
        this.btnZoomIn.setToolTipText(PartStr.Get(sZI, 1));
        this.btnPrev.setSelected(true);
        this.btnPrev.addActionListener(this);
        this.btnRender.addActionListener(this);
        this.btnTest.addActionListener(this);
        this.btnZoomOut.addActionListener(this);
        this.btnZoom1.addActionListener(this);
        this.btnZoomIn.addActionListener(this);
        this.lbFrame = new JLabel("Frame:0/0");
        this.lbFrame.setHorizontalAlignment(0);
        this.lbFrame.setFont(GUIEditor.getInstance().fontUI);
        this.lbFrame.setBounds(251, 27, 80, 25);
        this.lbFrame.setVisible(false);
        toolpanel.add(row1);
        toolpanel.add(row2);
        row1.add(this.btnPrev);
        row1.add(this.btnRender);
        row1.add(this.btnTest);
        row2.add(this.btnZoomOut);
        row2.add(this.btnZoom1);
        row2.add(this.btnZoomIn);
        toolpanel.add(this.lbFrame);
        this.btnTest.addKeyListener(this);
        this.scr = new JScrollPane(this.panel, 22, 32);
        this.scr.getVerticalScrollBar().setUnitIncrement(20);
        this.add((Component)this.scr, "Center");
        Dimension d = this.getSize();
        this.scr.setBounds(0, 0, d.width, d.height);
        this.iZoom = 16;
        this.scr.addMouseWheelListener(this);
        this.scr.addMouseListener(this);
        this.scr.addMouseMotionListener(this);
        this.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e)
            {
                Dimension d = PreviewPanel.this.getSize();
                PreviewPanel.this.scr.setSize(d);
                PreviewPanel.this.scr.doLayout();
            }
        });
        this.setVisible(true);
    }

    public void SetBounds(int x, int y, int w, int h)
    {
        this.setBounds(x, y, w, h);
    }

    public void ReLayout()
    {
    }

    public void SetSize(int w, int h)
    {
        if (this.prefs.rendermode == 2)
        {
            w = this.prefs.width;
            h = this.prefs.height;
        }
        else if (this.prefs.alignhorz.val != 0)
        {
            w = this.prefs.width * this.prefs.frames;
            h = this.prefs.height;
        }
        else
        {
            w = this.prefs.width;
            h = this.prefs.height * this.prefs.frames;
        }
        w = w * this.iZoom / 16;
        h = h * this.iZoom / 16;
        this.panel.setSize(w + 80, h + 80);
        this.panel.setPreferredSize(new Dimension(w + 80, h + 80));
    }

    public void SetBg(Col c)
    {
        Color bg = new Color(c.r, c.g, c.b);
        this.panel.setBackground(bg);
    }

    public void Draw(Bitmap bmp)
    {
        this.repaint();
    }

    public void Update()
    {
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == this.btnZoomOut)
        {
            if ((this.iZoom /= 2) < 2)
            {
                this.iZoom = 2;
            }
            this.editor.SetSize(this.ctl.imgRender.width, this.ctl.imgRender.height);
            this.editor.prevpanel.panel.repaint();
        }
        if (e.getSource() == this.btnZoomIn)
        {
            if ((this.iZoom *= 2) > 128)
            {
                this.iZoom = 128;
            }
            this.editor.SetSize(this.ctl.imgRender.width, this.ctl.imgRender.height);
            this.editor.prevpanel.panel.repaint();
        }
        if (e.getSource() == this.btnZoom1)
        {
            this.iZoom = 16;
            this.editor.SetSize(this.ctl.imgRender.width, this.ctl.imgRender.height);
            this.editor.prevpanel.panel.repaint();
        }
        if (e.getSource() == this.btnPrev && this.ctl.prefs.rendermode != 0)
        {
            this.lbFrame.setVisible(false);
            this.ctl.prefs.rendermode = 0;
            this.ctl.Update(Control.UpPrefParam);
        }
        if (e.getSource() == this.btnRender && this.ctl.prefs.rendermode != 1)
        {
            this.lbFrame.setVisible(false);
            this.ctl.prefs.rendermode = 1;
            this.ctl.Update(Control.UpPrefParam);
        }
        if (e.getSource() == this.btnTest && this.ctl.prefs.rendermode != 2)
        {
            this.lbFrame.setVisible(true);
            this.ctl.prefs.rendermode = 2;
            this.ctl.Update(Control.UpPrefParam);
        }
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
        this.iY0 = e.getY();
        this.iTest0 = this.prefs.testindex;
    }

    @Override public void mouseReleased(MouseEvent e)
    {
    }

    public void SetFrame(int i)
    {
        this.prefs.testindex = i;
        if (this.prefs.testindex < 0)
        {
            this.prefs.testindex = 0;
        }
        if (this.prefs.testindex >= this.prefs.priFramesRender.val)
        {
            this.prefs.testindex = this.prefs.priFramesRender.val - 1;
        }
        if (this.prefs.rendermode == 2)
        {
            this.repaint();
        }
    }

    @Override public void mouseDragged(MouseEvent e)
    {
        if (this.prefs.rendermode == 2)
        {
            this.SetFrame(this.iTest0 - (e.getY() - this.iY0) / 5);
            if (this.editor.ticon != null)
            {
                this.editor.ticon.SetFrame(this.prefs.testindex);
            }
        }
    }

    @Override public void mouseMoved(MouseEvent e)
    {
    }

    @Override public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (this.prefs.rendermode == 2)
        {
            int iInv = GUIEditor.getInstance().iWheelDir != 0 ? -1 : 1;
            this.SetFrame(this.prefs.testindex - iInv * e.getWheelRotation());
            if (this.editor.ticon != null)
            {
                this.editor.ticon.SetFrame(this.prefs.testindex);
            }
        }
    }

    @Override public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == 38)
        {
            ++this.prefs.testindex;
        }
        if (key == 40)
        {
            --this.prefs.testindex;
        }
        if (this.prefs.testindex < 0)
        {
            this.prefs.testindex = 0;
        }
        if (this.prefs.testindex >= this.prefs.priFramesRender.val)
        {
            this.prefs.testindex = this.prefs.priFramesRender.val - 1;
        }
        this.repaint();
    }

    @Override public void keyReleased(KeyEvent e)
    {
    }

    @Override public void keyTyped(KeyEvent e)
    {
    }

    class Pane extends JPanel
    {
        Pane()
        {
        }

        @Override public void paintComponent(Graphics g)
        {
            try
            {
                Graphics2D g2 = (Graphics2D)g;
                Bitmap bmp = PreviewPanel.this.ctl.imgRender;
                int pw = PreviewPanel.this.prefs.width * PreviewPanel.this.iZoom / 16;
                int ph = PreviewPanel.this.prefs.height * PreviewPanel.this.iZoom / 16;
                if (PreviewPanel.this.prefs.rendermode == 2)
                {
                    PreviewPanel.this.lbFrame.setText(String.valueOf(PreviewPanel.this.prefs.testindex + 1) + "/" +
                                                      PreviewPanel.this.prefs.frames);
                    int px = Math.max(40, (this.getWidth() - pw) / 2);
                    int py = Math.max(40, (this.getHeight() - ph) / 2);
                    g2.setColor(this.getBackground());
                    g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                    if (PreviewPanel.this.prefs.alignhorz.val == 0)
                    {
                        int h = PreviewPanel.this.prefs.height * PreviewPanel.this.prefs.testindex;
                        g.drawImage(bmp.img, px, py, px + pw, py + ph, 0, h, PreviewPanel.this.prefs.width,
                                    h + PreviewPanel.this.prefs.height, null);
                    }
                    else
                    {
                        int w = PreviewPanel.this.prefs.width * PreviewPanel.this.prefs.testindex;
                        g.drawImage(bmp.img, px, py, px + pw, py + ph, w, 0, w + PreviewPanel.this.prefs.width,
                                    PreviewPanel.this.prefs.height, null);
                    }
                }
                else
                {
                    int w = this.getWidth();
                    int h = this.getHeight();
                    int bw = bmp.width * PreviewPanel.this.iZoom / 16;
                    int bh = bmp.height * PreviewPanel.this.iZoom / 16;
                    int px = Math.max(40, (w - bw) / 2);
                    int py = Math.max(40, (h - bh) / 2);
                    g2.setColor(this.getBackground());
                    g2.fillRect(0, 0, w, h);
                    g2.drawImage(bmp.img, px, py, bw, bh, null);
                    Col c = PreviewPanel.this.prefs.bkcolor.col;
                    if (c.g * 6 + c.r * 3 + c.b > 1280)
                    {
                        g2.setColor(Color.black);
                    }
                    else
                    {
                        g2.setColor(Color.white);
                    }
                    g2.fillRect(px, py - 8, bw, 1);
                    for (int x = 0; x <= bw; x += pw)
                    {
                        g2.fillRect(px + x, py - 16, 1, 8);
                    }
                    g2.fillRect(px - 8, py, 1, bh);
                    for (int y = 0; y <= bh; y += ph)
                    {
                        g2.fillRect(px - 16, py + y, 8, 1);
                    }
                    for (int f = 0; f < PreviewPanel.this.prefs.frames; ++f)
                    {
                        if (PreviewPanel.this.prefs.alignhorz.val != 0)
                        {
                            g2.drawString(Integer.toString(f + 1), px + f * pw + pw / 2, py - 25);
                            continue;
                        }
                        g2.drawString(Integer.toString(f + 1), px - 30, py + f * ph + ph / 2);
                    }
                }
            }
            catch (Exception exception)
            {
                // empty catch block
            }
        }
    }
}

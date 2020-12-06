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
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

class DialogCanvasSize extends JDialog implements ActionListener
{
    JButton btnOk;
    JButton btnCancel;
    JTextField tfWidth;
    JTextField tfHeight;
    JTextField tfOffX;
    JTextField tfOffY;
    public String ret = null;
    File[] fileLang;
    File[] fileShortcuts;
    GUIEditor editor = GUIEditor.getInstance();

    public DialogCanvasSize(Frame frame)
    {
        super(frame, "Set Layout", true);
        this.setSize(400, 210);
        this.setResizable(false);
        this.setLayout(null);
        Container c = this.getContentPane();
        Control ctl = Control.getInstance();
        JLabel lbSize = new JLabel(this.editor.GetMsgStr("size"));
        lbSize.setBounds(20, 10, 100, 28);
        c.add(lbSize);
        this.tfWidth = new JTextField("0", 8);
        this.tfWidth.setBounds(120, 10, 100, 28);
        c.add(this.tfWidth);
        this.tfHeight = new JTextField("0", 8);
        this.tfHeight.setBounds(240, 10, 100, 28);
        c.add(this.tfHeight);
        this.tfWidth.setText(String.format("%3d", ctl.prefs.pwidth.val));
        this.tfHeight.setText(String.format("%3d", ctl.prefs.pheight.val));
        JLabel lbOffset = new JLabel(this.editor.GetMsgStr("offset"));
        lbOffset.setBounds(20, 50, 100, 28);
        c.add(lbOffset);
        this.tfOffX = new JTextField("0", 8);
        this.tfOffX.setBounds(120, 50, 100, 28);
        c.add(this.tfOffX);
        this.tfOffY = new JTextField("0", 8);
        this.tfOffY.setBounds(240, 50, 100, 28);
        c.add(this.tfOffY);
        this.btnOk = new JButton(this.editor.GetMsgStr("ok"));
        this.btnOk.setBounds(30, 140, 120, 30);
        c.add(this.btnOk);
        this.btnCancel = new JButton(this.editor.GetMsgStr("cancel"));
        this.btnCancel.setBounds(160, 140, 120, 30);
        c.add(this.btnCancel);
        this.btnOk.addActionListener(this);
        this.btnCancel.addActionListener(this);
        Rectangle rc = frame.getBounds();
        this.setLocation(rc.x + rc.width / 2 - 150, rc.y + rc.height / 2 - 55);
        this.setVisible(true);
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        String kb = GUIEditor.getInstance().strKnobBrowser;
        if (e.getSource() == this.btnOk)
        {
            this.dispose();
            Control ctl = Control.getInstance();
            ctl.journal.Write();
            int neww = Parse.Int(this.tfWidth.getText());
            int oldw = ctl.prefs.pwidth.val;
            int newh = Parse.Int(this.tfHeight.getText());
            int oldh = ctl.prefs.pheight.val;
            int offx = Parse.Int(this.tfOffX.getText());
            int offy = Parse.Int(this.tfOffY.getText());
            ctl.prefs.pwidth.Update(neww);
            ctl.prefs.pheight.Update(newh);
        block5:
            for (int i = 0; i < ctl.iMaxLayer; ++i)
            {
                double fx;
                Layer ly = ctl.layers.get(i);
                double fxf = ly.eff.zoomxf.val;
                double fyf = ly.eff.zoomyf.val;
                double fxt = ly.eff.zoomxt.val;
                double fyt = ly.eff.zoomyt.val;
                double fa = ly.prim.aspect.val;
                double f1 = oldw;
                double f2 = oldh;
                if (fa >= 0.0)
                {
                    f1 = f1 * (100.0 - fa) / 100.0;
                }
                else
                {
                    f2 = f2 * (100.0 + fa) / 100.0;
                }
                double fmax = Math.max(f1 /= (double)neww, f2 /= (double)newh);
                if (ly.prim.type.val != 14)
                {
                    ly.eff.zoomxf.Update(fxf * fmax);
                    ly.eff.zoomyf.Update(fyf * fmax);
                    ly.eff.zoomxt.Update(fxt * fmax);
                    ly.eff.zoomyt.Update(fyt * fmax);
                }
                fa = f1 >= f2 ? -(1.0 - f2 / f1) * 100.0 : (1.0 - f1 / f2) * 100.0;
                ly.prim.aspect.Update(fa);
                fxf = ly.eff.offxf.val;
                fyf = ly.eff.offyf.val;
                fxt = ly.eff.offxt.val;
                fyt = ly.eff.offyt.val;
                fxf = (double)oldw * fxf / (double)neww + (double)(offx * 100 / neww);
                fyf = (double)oldh * fyf / (double)newh + (double)(offy * 100 / newh);
                fxt = (double)oldw * fxt / (double)neww + (double)(offx * 100 / neww);
                fyt = (double)oldh * fyt / (double)newh + (double)(offy * 100 / newh);
                ly.eff.offxf.Update(fxf);
                ly.eff.offyf.Update(fyf);
                ly.eff.offxt.Update(fxt);
                ly.eff.offyt.Update(fyt);
                fxf = ly.eff.centerx.val;
                fyf = ly.eff.centery.val;
                fxf = (double)oldw * fxf / (double)neww + (double)(offx * 100 / neww);
                fyf = (double)oldh * fyf / (double)newh + (double)(offy * 100 / newh);
                ly.eff.centerx.Update(fxf);
                ly.eff.centery.Update(fyf);
                if (ly.eff.mask1type.val == 1)
                {
                    fx = ly.eff.mask1startf.val;
                    fx = (double)Math.min(oldw, oldh) * fx / (double)Math.min(neww, newh);
                    ly.eff.mask1startf.Update(fx);
                    fx = ly.eff.mask1startt.val;
                    fx = (double)Math.min(oldw, oldh) * fx / (double)Math.min(neww, newh);
                    ly.eff.mask1startt.Update(fx);
                    fx = ly.eff.mask1stopf.val;
                    fx = (double)Math.min(oldw, oldh) * fx / (double)Math.min(neww, newh);
                    ly.eff.mask1stopf.Update(fx);
                    fx = ly.eff.mask1stopt.val;
                    fx = (double)Math.min(oldw, oldh) * fx / (double)Math.min(neww, newh);
                    ly.eff.mask1stopt.Update(fx);
                }
                if (ly.eff.mask2type.val == 1)
                {
                    fx = ly.eff.mask2startf.val;
                    fx = (double)Math.min(oldw, oldh) * fx / (double)Math.min(neww, newh);
                    ly.eff.mask2startf.Update(fx);
                    fx = ly.eff.mask2startt.val;
                    fx = (double)Math.min(oldw, oldh) * fx / (double)Math.min(neww, newh);
                    ly.eff.mask2startt.Update(fx);
                    fx = ly.eff.mask2stopf.val;
                    fx = (double)Math.min(oldw, oldh) * fx / (double)Math.min(neww, newh);
                    ly.eff.mask2stopf.Update(fx);
                    fx = ly.eff.mask2stopt.val;
                    fx = (double)Math.min(oldw, oldh) * fx / (double)Math.min(neww, newh);
                    ly.eff.mask2stopt.Update(fx);
                }
                switch (ly.prim.type.val)
                {
                case 10: {
                    continue block5;
                }
                case 11: {
                    fx = ly.prim.width.val;
                    ly.prim.width.Update((double)oldw * fx / (double)neww);
                    continue block5;
                }
                case 14: {
                    fx = ly.prim.fontsize.val;
                    ly.prim.fontsize.Update((double)oldh * fx / (double)newh);
                }
                }
            }
            ctl.Update(Control.UpAll);
            ctl.SelLayer(ctl.iCurrentLayer);
        }
        if (e.getSource() == this.btnCancel)
        {
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
            DialogCanvasSize.this.dispose();
        }
    }
}

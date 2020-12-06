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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class EffPanel extends JPanel implements MouseListener, UpdateReq, ActionListener
{
    JPanel panel;
    ProfileReader ppr;
    FieldH grpzoom;
    FieldH grpoffset;
    FieldH grprot;
    FieldH grpcolor;
    FieldH grpmask;
    FieldH grplight;
    FieldH grpshadow;
    FieldH grpishadow;
    FieldH grpedge;
    FieldC antialias;
    FieldC unfold;
    FieldVal animstep;
    FieldC zoomxysepa;
    FieldA zoomx;
    FieldA zoomy;
    FieldA offx;
    FieldA offy;
    FieldA angle;
    FieldC keepdir;
    FieldVal centerx;
    FieldVal centery;
    FieldA alpha;
    FieldA bright;
    FieldA contrast;
    FieldA saturation;
    FieldA hue;
    FieldC mask1ena;
    FieldS mask1type;
    FieldVal mask1grad;
    FieldS mask1graddir;
    FieldA mask1start;
    FieldA mask1stop;
    JPanel maskhr1;
    JPanel maskhr2;
    FieldC mask2ena;
    FieldS mask2op;
    FieldS mask2type;
    FieldVal mask2grad;
    FieldS mask2graddir;
    FieldA mask2start;
    FieldA mask2stop;
    FieldS fmaskena;
    FieldVal fmaskstart;
    FieldVal fmaskstop;
    FieldA slightdir;
    FieldA sdensity;
    FieldAC dlightdir;
    FieldA doffset;
    FieldA ddensity;
    FieldA ddiffuse;
    FieldS dstype;
    FieldVal dsgrad;
    FieldAC ilightdir;
    FieldA ioffset;
    FieldA idensity;
    FieldA idiffuse;
    FieldAC elightdir;
    FieldA eoffset;
    FieldA edensity;
    FieldT fmaskbits;
    JLabel fmaskstartd;
    JLabel fmaskstopd;

    public EffPanel(GUIEditor editor)
    {
        int x = 5;
        int y = 10;
        this.setLayout(null);
        this.ppr = GUIEditor.getInstance().pprLang;
        this.ppr.SetSection("Effects");
        this.antialias = new FieldC(this, x, y, this.ppr.ReadString("antialias", "Antialias"), 180, this, 0);
        this.unfold = new FieldC(this, x, y + 20, this.ppr.ReadString("unfold", "Unfold"), 100, this, 0);
        this.animstep = new FieldVal(this, x + 100, y + 20, this.ppr.ReadString("animstep", "AnimStep :"), 130, 80,
                                     false, 0.0, 50.0, this, 0);
        this.grpzoom = new FieldH(this, 0, y + 50, this.ppr.ReadString("zoom", "Zoom"));
        this.zoomxysepa = new FieldC(this, x, y + 73, this.ppr.ReadString("xysepa", "XY Sepa :"), 200, this, 0);
        this.zoomxysepa.cb.addActionListener(this);
        this.zoomx = new FieldA(this, x, y + 95, this.ppr.ReadString("x", "X :"), 80, 90, 1.0, 1000.0, this, 0);
        this.zoomy = new FieldA(this, x, y + 122, this.ppr.ReadString("y", "Y :"), 80, 90, 1.0, 1000.0, this, 0);
        this.grpoffset = new FieldH(this, 0, y + 155, this.ppr.ReadString("layoutoffset", "Offset"));
        this.offx = new FieldA(this, x, y + 185, this.ppr.ReadString("x", "X :"), 80, 90, -3000.0, 3000.0, this, 0);
        this.offy = new FieldA(this, x, y + 212, this.ppr.ReadString("y", "Y :"), 80, 90, -3000.0, 3000.0, this, 0);
        this.grprot = new FieldH(this, 0, y + 240, this.ppr.ReadString("rotateat", "RotateAt"));
        this.keepdir = new FieldC(this, x, y + 263, this.ppr.ReadString("keepdir", "KeepDir"), 200, this, 0);
        this.centerx = new FieldVal(this, x, y + 285, this.ppr.ReadString("centerx", "Center X :"), 80, 90, true,
                                    -3000.0, 3000.0, this, 0);
        this.centery = new FieldVal(this, x, y + 310, this.ppr.ReadString("centery", "Center Y :"), 80, 90, true,
                                    -3000.0, 3000.0, this, 0);
        this.angle =
            new FieldA(this, x, y + 335, this.ppr.ReadString("angle", "Angle :"), 80, 90, -1800.0, 1800.0, this, 0);
        this.grpcolor = new FieldH(this, 0, y + 365, this.ppr.ReadString("colors", "Colors"));
        this.alpha = new FieldA(this, x, y + 395, this.ppr.ReadString("alpha", "Alpha :"), 80, 90, 0.0, 100.0, this, 0);
        this.bright =
            new FieldA(this, x, y + 422, this.ppr.ReadString("bright", "Brightness :"), 80, 90, -100.0, 100.0, this, 0);
        this.contrast =
            new FieldA(this, x, y + 449, this.ppr.ReadString("contrast", "Contrast :"), 80, 90, -100.0, 100.0, this, 0);
        this.saturation = new FieldA(this, x, y + 476, this.ppr.ReadString("saturation", "Saturation :"), 80, 90,
                                     -100.0, 100.0, this, 0);
        this.hue = new FieldA(this, x, y + 503, this.ppr.ReadString("hue", "Hue :"), 80, 90, -1800.0, 1800.0, this, 0);
        this.grpmask = new FieldH(this, 0, y + 530, this.ppr.ReadString("mask", "Mask"));
        this.mask1ena = new FieldC(this, x, y + 553, this.ppr.ReadString("mask1ena", "Mask1 Enable"), 200, this, 0);
        this.mask1ena.cb.addActionListener(this);
        this.mask1type = new FieldS(
            this, x, y + 580, 22, this.ppr.ReadString("masktype", "Type :"), 80, 120, 120, editor.iconMask,
            new String[] {this.ppr.ReadString("rotate", "Rotate"), this.ppr.ReadString("radius", "Radius"),
                          this.ppr.ReadString("horizontal", "Horizontal"), this.ppr.ReadString("vertical", "Vertical")},
            this, 0);
        this.mask1grad = new FieldVal(this, x, y + 608, this.ppr.ReadString("gradation", "Gradation :"), 80, 90, true,
                                      0.0, 100.0, this, 0);
        this.mask1graddir = new FieldS(
            this, x + 172, y + 608, 22, "", 0, 128, 128, editor.iconGrad,
            new String[] {this.ppr.ReadString("singledir", "SingleDir"), this.ppr.ReadString("bidir", "BiDir")}, this,
            0);
        this.mask1start =
            new FieldA(this, x, y + 637, this.ppr.ReadString("maskstart", "Start :"), 80, 90, -1800.0, 1800.0, this, 0);
        this.mask1stop =
            new FieldA(this, x, y + 664, this.ppr.ReadString("maskstop", "Stop :"), 80, 90, -1800.0, 1800.0, this, 0);
        this.maskhr1 = new JPanel();
        this.add(this.maskhr1);
        this.maskhr1.setBackground(Color.lightGray);
        this.mask2ena = new FieldC(this, x, y + 692, this.ppr.ReadString("mask2ena", "Mask2 Enable"), 200, this, 0);
        this.mask2ena.cb.addActionListener(this);
        this.mask2op =
            new FieldS(this, x, y + 717, 22, this.ppr.ReadString("op", "Op :"), 80, 120, 120, editor.iconOp,
                       new String[] {this.ppr.ReadString("and", "And"), this.ppr.ReadString("or", "Or")}, this, 0);
        this.mask2type = new FieldS(
            this, x, y + 746, 22, this.ppr.ReadString("masktype", "Type :"), 80, 120, 120, editor.iconMask,
            new String[] {this.ppr.ReadString("rotate", "Rotate"), this.ppr.ReadString("radius", "Radius"),
                          this.ppr.ReadString("horizontal", "Horizontal"), this.ppr.ReadString("vertical", "Vertical")},
            this, 0);
        this.mask2grad = new FieldVal(this, x, y + 774, this.ppr.ReadString("gradation", "Gradation :"), 80, 90, true,
                                      0.0, 100.0, this, 0);
        this.mask2graddir = new FieldS(
            this, x + 172, y + 774, 22, "", 0, 128, 128, editor.iconGrad,
            new String[] {this.ppr.ReadString("singledir", "SingleDir"), this.ppr.ReadString("bidir", "BiDir")}, this,
            0);
        this.mask2start =
            new FieldA(this, x, y + 803, this.ppr.ReadString("maskstart", "Start :"), 80, 90, -1800.0, 1800.0, this, 0);
        this.mask2stop =
            new FieldA(this, x, y + 830, this.ppr.ReadString("maskstop", "Stop :"), 80, 90, -1800.0, 1800.0, this, 0);
        this.maskhr2 = new JPanel();
        this.add(this.maskhr2);
        this.maskhr2.setBackground(Color.lightGray);
        this.fmaskena =
            new FieldS((Container)this, x, y + 860, 22, this.ppr.ReadString("framemaskena", "FrameMask :"), 100, 120,
                       new String[] {this.ppr.ReadString("none", "None"), this.ppr.ReadString("range", "Range"),
                                     this.ppr.ReadString("maskbits", "Mask bits")},
                       (UpdateReq)this, 0);
        this.fmaskena.ch.addActionListener(this);
        this.fmaskstart = new FieldVal(this, x, y + 885, this.ppr.ReadString("maskstart", "Start :"), 80, 90, true, 0.0,
                                       100.0, this, 0);
        this.fmaskstop = new FieldVal(this, x, y + 912, this.ppr.ReadString("maskstop", "Stop :"), 80, 90, true, 0.0,
                                      100.0, this, 0);
        this.fmaskstartd = new JLabel("0/0");
        this.fmaskstopd = new JLabel("0/0");
        this.fmaskstartd.setBounds(x + 170, y + 885, 100, 22);
        this.fmaskstopd.setBounds(x + 170, y + 912, 100, 22);
        this.fmaskstartd.setFont(GUIEditor.getInstance().fontUI);
        this.fmaskstartd.setHorizontalAlignment(2);
        this.fmaskstopd.setFont(GUIEditor.getInstance().fontUI);
        this.fmaskstopd.setHorizontalAlignment(2);
        this.add(this.fmaskstartd);
        this.add(this.fmaskstopd);
        this.fmaskbits =
            new FieldT(this, x, y + 910, this.ppr.ReadString("framemaskbits", "Frame mask bits:"), 120, 140, this, 0);
        this.grplight = new FieldH(this, 0, y + 940, this.ppr.ReadString("specular", "Specular"));
        this.slightdir =
            new FieldA(this, x, y + 970, this.ppr.ReadString("lightdir", "Dir :"), 80, 90, -1800.0, 1800.0, this, 0);
        this.sdensity =
            new FieldA(this, x, y + 997, this.ppr.ReadString("density", "Density :"), 80, 90, -100.0, 100.0, this, 0);
        this.grpshadow = new FieldH(this, 0, y + 1027, this.ppr.ReadString("dropshadow", "DropShadow"));
        this.dlightdir =
            new FieldAC(this, 0, y + 1052, this.ppr.ReadString("lightdir", "Dir :"), 65, 90, -1800.0, 1800.0, this, 0);
        this.doffset =
            new FieldA(this, x, y + 1079, this.ppr.ReadString("offset", "Offset :"), 80, 90, -100.0, 100.0, this, 0);
        this.ddensity =
            new FieldA(this, x, y + 1106, this.ppr.ReadString("density", "Density :"), 80, 90, -100.0, 100.0, this, 0);
        this.ddiffuse =
            new FieldA(this, x, y + 1133, this.ppr.ReadString("diffuse", "Diffuse :"), 80, 90, 0.0, 100.0, this, 0);
        this.dstype = new FieldS((Container)this, x, y + 1160, 22, this.ppr.ReadString("shadowtype", "Type :"), 80, 90,
                                 new String[] {"Spot", "Line"}, (UpdateReq)this, 0);
        this.dsgrad = new FieldVal(this, x + 186, y + 1160, this.ppr.ReadString("shadowgrad", "Grad :"), 50, 90, true,
                                   0.0, 100.0, this, 0);
        this.grpishadow = new FieldH(this, 0, y + 1195, this.ppr.ReadString("insideshadow", "Inside Shadow"));
        this.ilightdir =
            new FieldAC(this, 0, y + 1225, this.ppr.ReadString("lightdir", "Dir :"), 65, 90, -1800.0, 1800.0, this, 0);
        this.ioffset =
            new FieldA(this, x, y + 1252, this.ppr.ReadString("offset", "Offset :"), 80, 90, -100.0, 100.0, this, 0);
        this.idensity =
            new FieldA(this, x, y + 1279, this.ppr.ReadString("density", "Density :"), 80, 90, -100.0, 100.0, this, 0);
        this.idiffuse =
            new FieldA(this, x, y + 1306, this.ppr.ReadString("diffuse", "Diffuse :"), 80, 90, 0.0, 100.0, this, 0);
        this.grpedge = new FieldH(this, 0, y + 1336, this.ppr.ReadString("edgehilight", "Edge Hilight"));
        this.elightdir =
            new FieldAC(this, 0, y + 1366, this.ppr.ReadString("lightdir", "Dir :"), 65, 90, -1800.0, 1800.0, this, 0);
        this.eoffset =
            new FieldA(this, x, y + 1393, this.ppr.ReadString("offset", "Offset :"), 80, 90, -100.0, 100.0, this, 0);
        this.edensity =
            new FieldA(this, x, y + 1420, this.ppr.ReadString("density", "Density :"), 80, 90, -100.0, 100.0, this, 0);
        this.setSize(320, 1500);
        this.setPreferredSize(new Dimension(320, 1500));
        this.addMouseListener(this);
        this.ReLayout();
    }

    public void ReLayout()
    {
        int x = 5;
        int y = 60;
        this.grpzoom.ReLayout(0, y);
        if (this.grpzoom.cb.isSelected())
        {
            this.zoomxysepa.ReLayout(x, y + 27);
            this.zoomx.ReLayout(x, y + 54);
            this.zoomy.ReLayout(x, y + 81);
            y += 113;
        }
        else
        {
            this.zoomxysepa.ReLayout(x, -1000);
            this.zoomx.ReLayout(x, -1000);
            this.zoomy.ReLayout(x, -1000);
            y += 25;
        }
        this.grpoffset.ReLayout(0, y);
        if (this.grpoffset.cb.isSelected())
        {
            this.offx.ReLayout(x, y + 27);
            this.offy.ReLayout(x, y + 54);
            y += 86;
        }
        else
        {
            this.offx.ReLayout(x, -1000);
            this.offy.ReLayout(x, -1000);
            y += 25;
        }
        this.grprot.ReLayout(0, y);
        if (this.grprot.cb.isSelected())
        {
            this.keepdir.ReLayout(x, y + 27);
            this.centerx.ReLayout(x, y + 54);
            this.centery.ReLayout(x, y + 81);
            this.angle.ReLayout(x, y + 108);
            y += 140;
        }
        else
        {
            this.keepdir.ReLayout(x, -1000);
            this.centerx.ReLayout(x, -1000);
            this.centery.ReLayout(x, -1000);
            this.angle.ReLayout(x, -1000);
            y += 25;
        }
        this.grpcolor.ReLayout(0, y);
        if (this.grpcolor.cb.isSelected())
        {
            this.alpha.ReLayout(x, y + 27);
            this.bright.ReLayout(x, y + 54);
            this.contrast.ReLayout(x, y + 81);
            this.saturation.ReLayout(x, y + 108);
            this.hue.ReLayout(x, y + 135);
            y += 167;
        }
        else
        {
            this.alpha.ReLayout(x, -1000);
            this.bright.ReLayout(x, -1000);
            this.contrast.ReLayout(x, -1000);
            this.saturation.ReLayout(x, -1000);
            this.hue.ReLayout(x, -1000);
            y += 25;
        }
        this.grpmask.ReLayout(0, y);
        if (this.grpmask.cb.isSelected())
        {
            this.mask1ena.ReLayout(x, y += 27);
            if (this.mask1ena.cb.isSelected())
            {
                this.mask1type.setVisible(true);
                this.mask1type.ReLayout(x, y + 27);
                this.mask1grad.setVisible(true);
                this.mask1grad.ReLayout(x, y + 54);
                this.mask1graddir.setVisible(true);
                this.mask1graddir.ReLayout(x + 175, y + 54);
                this.mask1start.setVisible(true);
                this.mask1start.ReLayout(x, y + 81);
                this.mask1stop.setVisible(true);
                this.mask1stop.ReLayout(x, y + 108);
                this.maskhr1.setBounds(x, y + 138, 340, 2);
                y += 145;
            }
            else
            {
                this.mask1type.setVisible(false);
                this.mask1grad.setVisible(false);
                this.mask1graddir.setVisible(false);
                this.mask1start.setVisible(false);
                this.mask1stop.setVisible(false);
                this.maskhr1.setBounds(x, y + 30, 340, 2);
                y += 37;
            }
            this.mask2ena.ReLayout(x, y);
            if (this.mask2ena.cb.isSelected())
            {
                this.mask2op.setVisible(true);
                this.mask2op.ReLayout(x, y + 27);
                this.mask2type.setVisible(true);
                this.mask2type.ReLayout(x, y + 54);
                this.mask2grad.setVisible(true);
                this.mask2grad.ReLayout(x, y + 81);
                this.mask2graddir.setVisible(true);
                this.mask2graddir.ReLayout(x + 175, y + 81);
                this.mask2start.setVisible(true);
                this.mask2start.ReLayout(x, y + 108);
                this.mask2stop.setVisible(true);
                this.mask2stop.ReLayout(x, y + 138);
                this.maskhr2.setBounds(x, y + 165, 340, 2);
                y += 172;
            }
            else
            {
                this.mask2op.setVisible(false);
                this.mask2type.setVisible(false);
                this.mask2grad.setVisible(false);
                this.mask2graddir.setVisible(false);
                this.mask2start.setVisible(false);
                this.mask2stop.setVisible(false);
                this.maskhr2.setBounds(x, y + 30, 340, 2);
                y += 37;
            }
            this.fmaskena.ReLayout(x, y);
            switch (this.fmaskena.ch.getSelectedIndex())
            {
            case 0: {
                this.fmaskstart.setVisible(false);
                this.fmaskstop.setVisible(false);
                this.fmaskstartd.setVisible(false);
                this.fmaskstopd.setVisible(false);
                this.fmaskbits.setVisible(false);
                y += 32;
                break;
            }
            case 1: {
                this.fmaskstart.setVisible(true);
                this.fmaskstart.ReLayout(x, y + 27);
                this.fmaskstop.setVisible(true);
                this.fmaskstop.ReLayout(x, y + 54);
                this.fmaskstartd.setVisible(true);
                this.fmaskstartd.setBounds(x + 170, y + 27, 100, 22);
                this.fmaskstopd.setVisible(true);
                this.fmaskstopd.setBounds(x + 170, y + 54, 100, 22);
                this.fmaskbits.setVisible(false);
                y += 86;
                break;
            }
            case 2: {
                this.fmaskstart.setVisible(false);
                this.fmaskstop.setVisible(false);
                this.fmaskstartd.setVisible(false);
                this.fmaskstopd.setVisible(false);
                this.fmaskbits.setVisible(true);
                this.fmaskbits.ReLayout(x, y + 27);
                y += 59;
            }
            }
        }
        else
        {
            this.mask1ena.ReLayout(x, -1000);
            this.mask1type.ReLayout(x, -1000);
            this.mask1grad.ReLayout(x, -1000);
            this.mask1graddir.ReLayout(x, -1000);
            this.mask1start.ReLayout(x, -1000);
            this.mask1stop.ReLayout(x, -1000);
            this.maskhr1.setBounds(0, 0, 0, 0);
            this.mask2ena.ReLayout(x, -1000);
            this.mask2op.ReLayout(x, -1000);
            this.mask2type.ReLayout(x, -1000);
            this.mask2grad.ReLayout(x, -1000);
            this.mask2graddir.ReLayout(x, -1000);
            this.mask2start.ReLayout(x, -1000);
            this.mask2stop.ReLayout(x, -1000);
            this.maskhr2.setBounds(0, 0, 0, 0);
            this.fmaskena.ReLayout(x, -1000);
            this.fmaskstart.ReLayout(x, -1000);
            this.fmaskstop.ReLayout(x, -1000);
            this.fmaskstartd.setBounds(x + 170, -1000, 100, 22);
            this.fmaskstopd.setBounds(x + 170, -1000, 100, 22);
            this.fmaskbits.ReLayout(x, -1000);
            y += 25;
        }
        this.grplight.ReLayout(0, y);
        if (this.grplight.cb.isSelected())
        {
            this.slightdir.ReLayout(x, y + 27);
            this.sdensity.ReLayout(x, y + 54);
            y += 86;
        }
        else
        {
            this.slightdir.ReLayout(x, -1000);
            this.sdensity.ReLayout(x, -1000);
            y += 25;
        }
        this.grpshadow.ReLayout(0, y);
        if (this.grpshadow.cb.isSelected())
        {
            this.dlightdir.ReLayout(-5, y + 27);
            this.doffset.ReLayout(x, y + 54);
            this.ddensity.ReLayout(x, y + 81);
            this.ddiffuse.ReLayout(x, y + 108);
            this.dstype.ReLayout(x, y + 135);
            this.dsgrad.ReLayout(x + 185, y + 135);
            y += 167;
        }
        else
        {
            this.dlightdir.ReLayout(0, -1000);
            this.doffset.ReLayout(x, -1000);
            this.ddensity.ReLayout(x, -1000);
            this.ddiffuse.ReLayout(x, -1000);
            this.dstype.ReLayout(x, -1000);
            this.dsgrad.ReLayout(x, -1000);
            y += 25;
        }
        this.grpishadow.ReLayout(0, y);
        if (this.grpishadow.cb.isSelected())
        {
            this.ilightdir.ReLayout(-5, y + 27);
            this.ioffset.ReLayout(x, y + 54);
            this.idensity.ReLayout(x, y + 81);
            this.idiffuse.ReLayout(x, y + 108);
            y += 140;
        }
        else
        {
            this.ilightdir.ReLayout(0, -1000);
            this.ioffset.ReLayout(x, -1000);
            this.idensity.ReLayout(x, -1000);
            this.idiffuse.ReLayout(x, -1000);
            y += 25;
        }
        this.grpedge.ReLayout(0, y);
        if (this.grpedge.cb.isSelected())
        {
            this.elightdir.ReLayout(-5, y + 27);
            this.eoffset.ReLayout(x, y + 54);
            this.edensity.ReLayout(x, y + 81);
            y += 113;
        }
        else
        {
            this.elightdir.ReLayout(0, -1000);
            this.eoffset.ReLayout(x, -1000);
            this.edensity.ReLayout(x, -1000);
            y += 25;
        }
        this.setSize(320, y + 100);
        this.setPreferredSize(new Dimension(320, y + 100));
    }

    public void UpdateFmaskD()
    {
        int total = Control.getInstance().prefs.priFramesRender.val;
        if (total > 0)
        {
            int i1 = (int)(this.fmaskstart.pv.val * 0.01 * (double)(total - 1) + 0.9999) + 1;
            int i2 = (int)(this.fmaskstop.pv.val * 0.01 * (double)(total - 1)) + 1;
            this.fmaskstartd.setText(String.valueOf(String.valueOf(i1)) + "/" + total);
            this.fmaskstopd.setText(String.valueOf(String.valueOf(i2)) + "/" + total);
        }
    }

    public void Setup(Layer ly)
    {
        this.antialias.Setup(ly.eff.antialias);
        this.unfold.Setup(ly.eff.unfold);
        this.animstep.Setup(ly.eff.animstep);
        this.zoomxysepa.Setup(ly.eff.zoomxysepa);
        if (this.zoomxysepa.cb.isSelected())
        {
            this.zoomy.vf1.tf.setEnabled(true);
            this.zoomy.vf1.bt.setEnabled(true);
        }
        else
        {
            this.zoomy.vf1.tf.setEnabled(false);
            this.zoomy.vf1.bt.setEnabled(false);
        }
        this.zoomx.Setup(ly.eff.zoomxf, ly.eff.zoomxt, ly.eff.zoomxanim);
        this.zoomy.Setup(ly.eff.zoomyf, ly.eff.zoomyt, ly.eff.zoomyanim);
        this.offx.Setup(ly.eff.offxf, ly.eff.offxt, ly.eff.offxanim);
        this.offy.Setup(ly.eff.offyf, ly.eff.offyt, ly.eff.offyanim);
        this.keepdir.Setup(ly.eff.keepdir);
        this.centerx.Setup(ly.eff.centerx);
        this.centery.Setup(ly.eff.centery);
        this.angle.Setup(ly.eff.anglef, ly.eff.anglet, ly.eff.angleanim);
        this.alpha.Setup(ly.eff.alphaf, ly.eff.alphat, ly.eff.alphaanim);
        this.bright.Setup(ly.eff.brightf, ly.eff.brightt, ly.eff.brightanim);
        this.contrast.Setup(ly.eff.contrastf, ly.eff.contrastt, ly.eff.contrastanim);
        this.saturation.Setup(ly.eff.saturationf, ly.eff.saturationt, ly.eff.saturationanim);
        this.hue.Setup(ly.eff.huef, ly.eff.huet, ly.eff.hueanim);
        this.mask1ena.Setup(ly.eff.mask1ena);
        this.mask1type.Setup(ly.eff.mask1type);
        this.mask1grad.Setup(ly.eff.mask1grad);
        this.mask1graddir.Setup(ly.eff.mask1graddir);
        this.mask1start.Setup(ly.eff.mask1startf, ly.eff.mask1startt, ly.eff.mask1startanim);
        this.mask1stop.Setup(ly.eff.mask1stopf, ly.eff.mask1stopt, ly.eff.mask1stopanim);
        this.mask2ena.Setup(ly.eff.mask2ena);
        this.mask2op.Setup(ly.eff.mask2op);
        this.mask2type.Setup(ly.eff.mask2type);
        this.mask2grad.Setup(ly.eff.mask2grad);
        this.mask2graddir.Setup(ly.eff.mask2graddir);
        this.mask2start.Setup(ly.eff.mask2startf, ly.eff.mask2startt, ly.eff.mask2startanim);
        this.mask2stop.Setup(ly.eff.mask2stopf, ly.eff.mask2stopt, ly.eff.mask2stopanim);
        this.fmaskena.Setup(ly.eff.fmaskena);
        this.fmaskstart.Setup(ly.eff.fmaskstart);
        this.fmaskstop.Setup(ly.eff.fmaskstop);
        this.fmaskbits.Setup(ly.eff.fmaskbits);
        this.UpdateFmaskD();
        this.slightdir.Setup(ly.eff.slightdirf, ly.eff.slightdirt, ly.eff.slightdiranim);
        this.sdensity.Setup(ly.eff.sdensityf, ly.eff.sdensityt, ly.eff.sdensityanim);
        this.dlightdir.Setup(ly.eff.dlightdirena, ly.eff.dlightdirf, ly.eff.dlightdirt, ly.eff.dlightdiranim);
        this.doffset.Setup(ly.eff.doffsetf, ly.eff.doffsett, ly.eff.doffsetanim);
        this.ddensity.Setup(ly.eff.ddensityf, ly.eff.ddensityt, ly.eff.ddensityanim);
        this.ddiffuse.Setup(ly.eff.ddiffusef, ly.eff.ddiffuset, ly.eff.ddiffuseanim);
        this.dstype.Setup(ly.eff.dstype);
        this.dsgrad.Setup(ly.eff.dsgrad);
        this.ilightdir.Setup(ly.eff.ilightdirena, ly.eff.ilightdirf, ly.eff.ilightdirt, ly.eff.ilightdiranim);
        this.ioffset.Setup(ly.eff.ioffsetf, ly.eff.ioffsett, ly.eff.ioffsetanim);
        this.idensity.Setup(ly.eff.idensityf, ly.eff.idensityt, ly.eff.idensityanim);
        this.idiffuse.Setup(ly.eff.idiffusef, ly.eff.idiffuset, ly.eff.idiffuseanim);
        this.elightdir.Setup(ly.eff.elightdirena, ly.eff.elightdirf, ly.eff.elightdirt, ly.eff.elightdiranim);
        this.eoffset.Setup(ly.eff.eoffsetf, ly.eff.eoffsett, ly.eff.eoffsetanim);
        this.edensity.Setup(ly.eff.edensityf, ly.eff.edensityt, ly.eff.edensityanim);
        this.ReLayout();
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        if (this.zoomxysepa.cb.isSelected())
        {
            this.zoomy.vf1.tf.setEnabled(true);
            this.zoomy.vf1.bt.setEnabled(true);
            if (this.zoomy.anim.ps.val == 0)
            {
                this.zoomy.vf2.tf.setEnabled(false);
                this.zoomy.vf2.bt.setEnabled(false);
            }
            else
            {
                this.zoomy.vf2.tf.setEnabled(true);
                this.zoomy.vf2.bt.setEnabled(true);
            }
        }
        else
        {
            this.zoomy.vf1.tf.setEnabled(false);
            this.zoomy.vf1.bt.setEnabled(false);
            this.zoomy.vf2.tf.setEnabled(false);
            this.zoomy.vf2.bt.setEnabled(false);
        }
        this.ReLayout();
    }

    @Override public void Update(int m)
    {
        this.UpdateFmaskD();
        Control.getInstance().Edit();
        Control.getInstance().Update(Control.UpEffParam);
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
        this.requestFocus();
    }

    @Override public void mouseReleased(MouseEvent e)
    {
    }
}

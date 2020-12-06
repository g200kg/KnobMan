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
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class PrimPanel extends JPanel implements UpdateReq, ActionListener, MouseListener
{
    FieldS type;
    FieldS texturefile;
    FieldS transparent;
    FieldS font;
    FieldS textalign;
    FieldS framealign;
    FieldVal aspect;
    FieldVal round;
    FieldVal width;
    FieldVal length;
    FieldVal step;
    FieldVal anglestep;
    FieldVal emboss;
    FieldVal embossdiffuse;
    FieldVal ambient;
    FieldVal lightdir;
    FieldVal specular;
    FieldVal specularwidth;
    FieldVal texturedepth;
    FieldVal texturezoom;
    FieldVal diffuse;
    FieldVal fontsize;
    FieldVal intellialpha;
    FieldVal numframe;
    FieldT file;
    FieldT text;
    FieldT shape;
    FieldC autofit;
    FieldC bold;
    FieldC italic;
    FieldC fill;
    FieldCol col;
    JButton fileset;
    JButton shapeedit;
    JPanel panel;
    Control ctl = Control.getInstance();
    int typecur;
    ProfileReader ppr;

    public PrimPanel(GUIEditor editor)
    {
        GUIEditor ed = GUIEditor.getInstance();
        this.ppr = ed.pprLang;
        this.ppr.SetSection("Primitive");
        this.setLayout(null);
        this.setSize(250, 500);
        this.type = new FieldS(
            this, 10, 15, 28, this.ppr.ReadString("type", "Type:"), 100, 140, 0, ed.imgIcon,
            new String[] {
                this.ppr.ReadString("typenone", "None"), this.ppr.ReadString("typeimage", "Image"),
                this.ppr.ReadString("typecircle", "Circle"), this.ppr.ReadString("typecirclefill", "CircleFill"),
                this.ppr.ReadString("typemetalcircle", "MetalCircle"),
                this.ppr.ReadString("typewavecircle", "WaveCircle"), this.ppr.ReadString("typesphere", "Sphere"),
                this.ppr.ReadString("typerect", "Rect"), this.ppr.ReadString("typerectfill", "RectFill"),
                this.ppr.ReadString("typetriangle", "Triangle"), this.ppr.ReadString("typeline", "Line"),
                this.ppr.ReadString("typeradiateline", "RadiateLine"), this.ppr.ReadString("typehlines", "HLines"),
                this.ppr.ReadString("typevlines", "VLines"), this.ppr.ReadString("typetext", "Text"),
                this.ppr.ReadString("typeshape", "Shape")},
            this, 16);
        this.type.ch.setMaximumRowCount(16);
        this.panel = new JPanel();
        this.panel.setBounds(0, 60, 280, 650);
        this.panel.setPreferredSize(new Dimension(280, 650));
        this.panel.setLayout(null);
        this.aspect = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("aspect", "Aspect:"), 110, 140, true, -100.0,
                                   100.0, this, 0);
        this.round =
            new FieldVal(this.panel, 0, 0, this.ppr.ReadString("round", "Round:"), 110, 140, true, 0.0, 100.0, this, 0);
        this.width =
            new FieldVal(this.panel, 0, 0, this.ppr.ReadString("width", "Width:"), 110, 140, true, 1.0, 100.0, this, 0);
        this.length = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("length", "Length:"), 110, 140, true, 1.0,
                                   100.0, this, 0);
        this.step =
            new FieldVal(this.panel, 0, 0, this.ppr.ReadString("step", "Step:"), 110, 140, true, 1.0, 100.0, this, 0);
        this.anglestep = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("anglestep", "AngleStep:"), 110, 140, true,
                                      1.0, 135.0, this, 0);
        this.emboss = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("emboss", "Emboss:"), 110, 140, true, -100.0,
                                   100.0, this, 0);
        this.embossdiffuse = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("embossdiffuse", "EmbossDiffuse:"), 110,
                                          140, true, 0.0, 100.0, this, 0);
        this.ambient = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("ambient", "Ambient:"), 110, 140, true, 0.0,
                                    100.0, this, 0);
        this.lightdir = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("lightdir", "LightDir:"), 110, 140, true,
                                     -100.0, 100.0, this, 0);
        this.specular = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("specular", "Specular:"), 110, 140, true,
                                     0.0, 100.0, this, 0);
        this.specularwidth = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("specularwidth", "SpecularWidth:"), 110,
                                          140, true, 0.0, 100.0, this, 0);
        this.texturefile = new FieldS(this.panel, 0, 0, 28, this.ppr.ReadString("texturefile", "TextureFile:"), 110,
                                      140, 240, this.ctl.iconTextures, this.ctl.strTextures, this, 32);
        this.texturefile.ch.setMaximumRowCount(16);
        GUIEditor.getInstance().dtTexture = new DropTarget(this.texturefile, GUIEditor.getInstance());
        this.texturedepth = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("texturedepth", "TextureDepth:"), 110,
                                         140, true, 0.0, 100.0, this, 0);
        this.texturezoom = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("texturezoom", "TextureZoom:"), 110, 140,
                                        true, 1.0, 400.0, this, 0);
        this.diffuse = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("diffuse", "Diffuse:"), 110, 140, true, 0.0,
                                    100.0, this, 0);
        this.file = new FieldT(this.panel, 0, 0, this.ppr.ReadString("file", "File:"), 110, 140, this, 64);
        this.fileset = new JButton(this.ppr.ReadString("fileset", "Set..."));
        GUIEditor.getInstance().dtImage = new DropTarget(this.file.tf, GUIEditor.getInstance());
        this.fileset.setBounds(180, 0, 70, 25);
        this.fileset.addActionListener(this);
        this.panel.add(this.fileset);
        this.transparent = new FieldS(
            (Container)this.panel, 0, 0, 28, this.ppr.ReadString("transparent", "Transparent:"), 110, 140,
            new String[] {this.ppr.ReadString("filealpha", "File's alpha"), this.ppr.ReadString("opaque", "Opaque"),
                          this.ppr.ReadString("1stpixel", "1st pixel")},
            (UpdateReq)this, 0);
        this.intellialpha = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("intellialpha", "IntelligentAlpha:"),
                                         110, 140, false, 0.0, 100.0, this, 128);
        this.autofit = new FieldC(this.panel, 110, 0, this.ppr.ReadString("autofit", "AutoFit to Rect"), 200, this, 16);
        this.numframe = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("multiframe", "MultiFramed:"), 110, 140,
                                     false, 1.0, 256.0, this, 0);
        this.framealign = new FieldS(
            (Container)this.panel, 0, 0, 28, this.ppr.ReadString("framealign", "Frame Align"), 110, 140,
            new String[] {this.ppr.ReadString("vertical", "Vertical"), this.ppr.ReadString("horizontal", "Horizontal"),
                          this.ppr.ReadString("individual", "Individual Files")},
            (UpdateReq)this, 0);
        this.text = new FieldT(this.panel, 0, 0, this.ppr.ReadString("text", "Text:"), 110, 140, this, 0);
        this.font = new FieldS(this.panel, 0, 0, 28, this.ppr.ReadString("font", "Font:"), 110, 140, 240,
                               editor.fontIcons, editor.fonts, this, 0);
        this.fontsize = new FieldVal(this.panel, 0, 0, this.ppr.ReadString("fontsize", "FontSize :"), 110, 140, true,
                                     1.0, 100.0, this, 0);
        this.bold = new FieldC(this.panel, 110, 0, this.ppr.ReadString("bold", "Bold"), 140, this, 0);
        this.italic = new FieldC(this.panel, 110, 0, this.ppr.ReadString("italic", "Italic"), 140, this, 0);
        this.textalign =
            new FieldS((Container)this.panel, 0, 0, 28, this.ppr.ReadString("textalign", "TextAlign :"), 110, 140,
                       new String[] {this.ppr.ReadString("center", "Center"), this.ppr.ReadString("left", "Left"),
                                     this.ppr.ReadString("right", "Right")},
                       (UpdateReq)this, 0);
        this.shape = new FieldT(this.panel, 0, 0, this.ppr.ReadString("shape", "Shape :"), 110, 140, this, 0);
        this.shapeedit = new JButton(this.ppr.ReadString("edit", "Edit..."));
        this.shapeedit.setBounds(180, 0, 70, 25);
        this.shapeedit.addActionListener(this);
        this.panel.add(this.shapeedit);
        this.fill = new FieldC(this.panel, 110, 0, this.ppr.ReadString("fill", "Fill"), 140, this, 0);
        this.col = new FieldCol(this.panel, 10, 0, this.ppr.ReadString("color", "Color :"), this, 0);
        this.typecur = -1;
        this.DispPrimParams(0);
        this.addMouseListener(this);
        this.add(this.panel);
    }

    public void Setup(Layer ly)
    {
        if (ly.prim.tex0 != null)
        {
            this.texturefile.SetItem(0, ly.prim.tex0.GetImageIcon(), "(" + ly.prim.texturename + ")");
        }
        else
        {
            this.texturefile.SetItem(0, null, "()");
        }
        this.type.Setup(ly.prim.type);
        this.aspect.Setup(ly.prim.aspect);
        this.round.Setup(ly.prim.round);
        this.width.Setup(ly.prim.width);
        this.length.Setup(ly.prim.length);
        this.step.Setup(ly.prim.step);
        this.anglestep.Setup(ly.prim.anglestep);
        this.emboss.Setup(ly.prim.emboss);
        this.embossdiffuse.Setup(ly.prim.embossdiffuse);
        this.ambient.Setup(ly.prim.ambient);
        this.lightdir.Setup(ly.prim.lightdir);
        this.specular.Setup(ly.prim.specular);
        this.specularwidth.Setup(ly.prim.specularwidth);
        this.texturefile.Setup(ly.prim.texturefile);
        this.texturedepth.Setup(ly.prim.texturedepth);
        this.texturezoom.Setup(ly.prim.texturezoom);
        this.diffuse.Setup(ly.prim.diffuse);
        this.file.Setup(ly.prim.file);
        this.transparent.Setup(ly.prim.transparent);
        this.intellialpha.Setup(ly.prim.intellialpha);
        this.autofit.Setup(ly.prim.autofit);
        this.numframe.Setup(ly.prim.numframe);
        this.framealign.Setup(ly.prim.framealign);
        this.text.Setup(ly.prim.text);
        this.font.Setup(ly.prim.font);
        this.fontsize.Setup(ly.prim.fontsize);
        this.bold.Setup(ly.prim.bold);
        this.italic.Setup(ly.prim.italic);
        this.textalign.Setup(ly.prim.textalign);
        this.shape.Setup(ly.prim.shape);
        this.fill.Setup(ly.prim.fill);
        this.col.Setup(ly.prim.color);
        this.DispPrimParams(-1);
    }

    public void DispPrimParams(int iLayer)
    {
        JComponent[] primparamids = new JComponent[] {
            this.aspect,      this.round,         this.width,       this.length,   this.step,       this.anglestep,
            this.emboss,      this.embossdiffuse, this.ambient,     this.lightdir, this.specular,   this.specularwidth,
            this.texturefile, this.texturedepth,  this.texturezoom, this.diffuse,  this.file,       this.fileset,
            this.transparent, this.intellialpha,  this.autofit,     this.numframe, this.framealign, this.text,
            this.font,        this.fontsize,      this.bold,        this.italic,   this.textalign,  this.shape,
            this.shapeedit,   this.fill,          this.col};
        int[][] arrarrn = new int[16][];
        arrarrn[0] = new int[33];
        int[] arrn = new int[33];
        arrn[16] = 1;
        arrn[17] = 1;
        arrn[18] = 1;
        arrn[19] = 1;
        arrn[20] = 1;
        arrn[21] = 1;
        arrn[22] = 1;
        arrarrn[1] = arrn;
        int[] arrn2 = new int[33];
        arrn2[0] = 1;
        arrn2[2] = 1;
        arrn2[10] = 1;
        arrn2[15] = 1;
        arrn2[32] = 1;
        arrarrn[2] = arrn2;
        int[] arrn3 = new int[33];
        arrn3[0] = 1;
        arrn3[6] = 1;
        arrn3[7] = 1;
        arrn3[10] = 1;
        arrn3[12] = 1;
        arrn3[13] = 1;
        arrn3[14] = 1;
        arrn3[15] = 1;
        arrn3[32] = 1;
        arrarrn[3] = arrn3;
        int[] arrn4 = new int[33];
        arrn4[0] = 1;
        arrn4[6] = 1;
        arrn4[7] = 1;
        arrn4[8] = 1;
        arrn4[10] = 1;
        arrn4[11] = 1;
        arrn4[12] = 1;
        arrn4[13] = 1;
        arrn4[14] = 1;
        arrn4[15] = 1;
        arrn4[32] = 1;
        arrarrn[4] = arrn4;
        int[] arrn5 = new int[33];
        arrn5[0] = 1;
        arrn5[2] = 1;
        arrn5[5] = 1;
        arrn5[10] = 1;
        arrn5[12] = 1;
        arrn5[13] = 1;
        arrn5[14] = 1;
        arrn5[15] = 1;
        arrn5[32] = 1;
        arrarrn[5] = arrn5;
        int[] arrn6 = new int[33];
        arrn6[0] = 1;
        arrn6[6] = 1;
        arrn6[7] = 1;
        arrn6[8] = 1;
        arrn6[9] = 1;
        arrn6[10] = 1;
        arrn6[11] = 1;
        arrn6[12] = 1;
        arrn6[13] = 1;
        arrn6[14] = 1;
        arrn6[15] = 1;
        arrn6[32] = 1;
        arrarrn[6] = arrn6;
        int[] arrn7 = new int[33];
        arrn7[0] = 1;
        arrn7[1] = 1;
        arrn7[2] = 1;
        arrn7[10] = 1;
        arrn7[15] = 1;
        arrn7[32] = 1;
        arrarrn[7] = arrn7;
        int[] arrn8 = new int[33];
        arrn8[0] = 1;
        arrn8[1] = 1;
        arrn8[6] = 1;
        arrn8[7] = 1;
        arrn8[10] = 1;
        arrn8[12] = 1;
        arrn8[13] = 1;
        arrn8[14] = 1;
        arrn8[15] = 1;
        arrn8[32] = 1;
        arrarrn[8] = arrn8;
        int[] arrn9 = new int[33];
        arrn9[2] = 1;
        arrn9[3] = 1;
        arrn9[10] = 1;
        arrn9[12] = 1;
        arrn9[13] = 1;
        arrn9[14] = 1;
        arrn9[15] = 1;
        arrn9[32] = 1;
        arrarrn[9] = arrn9;
        int[] arrn10 = new int[33];
        arrn10[2] = 1;
        arrn10[3] = 1;
        arrn10[10] = 1;
        arrn10[15] = 1;
        arrn10[32] = 1;
        arrarrn[10] = arrn10;
        int[] arrn11 = new int[33];
        arrn11[0] = 1;
        arrn11[2] = 1;
        arrn11[3] = 1;
        arrn11[5] = 1;
        arrn11[10] = 1;
        arrn11[15] = 1;
        arrn11[32] = 1;
        arrarrn[11] = arrn11;
        int[] arrn12 = new int[33];
        arrn12[0] = 1;
        arrn12[2] = 1;
        arrn12[3] = 1;
        arrn12[4] = 1;
        arrn12[10] = 1;
        arrn12[15] = 1;
        arrn12[32] = 1;
        arrarrn[12] = arrn12;
        int[] arrn13 = new int[33];
        arrn13[0] = 1;
        arrn13[2] = 1;
        arrn13[3] = 1;
        arrn13[4] = 1;
        arrn13[10] = 1;
        arrn13[15] = 1;
        arrn13[32] = 1;
        arrarrn[13] = arrn13;
        int[] arrn14 = new int[33];
        arrn14[23] = 1;
        arrn14[24] = 1;
        arrn14[25] = 1;
        arrn14[26] = 1;
        arrn14[27] = 1;
        arrn14[28] = 1;
        arrn14[32] = 1;
        arrarrn[14] = arrn14;
        int[] arrn15 = new int[33];
        arrn15[2] = 1;
        arrn15[10] = 1;
        arrn15[12] = 1;
        arrn15[13] = 1;
        arrn15[14] = 1;
        arrn15[15] = 1;
        arrn15[29] = 1;
        arrn15[30] = 1;
        arrn15[31] = 1;
        arrn15[32] = 1;
        arrarrn[15] = arrn15;
        int[][] primparams = arrarrn;
        if (iLayer < 0)
        {
            iLayer = Control.getInstance().iCurrentLayer;
        }
        if (iLayer == 0)
        {
            this.typecur = -1;
            for (int i = 0; i < primparamids.length; ++i)
            {
                primparamids[i].setVisible(false);
            }
            return;
        }
        Layer ly = this.ctl.layers.get(iLayer - 1);
        if (ly != null && this.typecur != ly.prim.type.val)
        {
            this.typecur = ly.prim.type.val;
            int y = 0;
            for (int i = 0; i < primparamids.length; ++i)
            {
                Dimension dim = primparamids[i].getSize();
                if (primparams[ly.prim.type.val][i] != 0)
                {
                    primparamids[i].setVisible(true);
                    int dy = primparamids[i].getHeight();
                    Point pt = primparamids[i].getLocation();
                    primparamids[i].setLocation(pt.x, y);
                    y += dy + 2;
                    continue;
                }
                primparamids[i].setVisible(false);
            }
            this.setSize(250, 60 + y);
            this.setPreferredSize(new Dimension(250, 60 + y));
            this.getParent().setSize(300, 60 + y);
        }
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        if (this.ctl.iCurrentLayer < 0)
        {
            return;
        }
        Layer ly = this.ctl.layers.get(this.ctl.iCurrentLayer - 1);
        if (e.getSource() == this.fileset)
        {
            FileDialog f = new FileDialog((Frame)GUIEditor.getInstance(), "Image File", 0);
            f.setDirectory(this.ctl.strImgDir);
            f.setFile("*.*");
            f.setVisible(true);
            if (f.getFile() == null)
            {
                return;
            }
            String path = String.valueOf(f.getDirectory()) + f.getFile();
            this.file.SetText(path);
            ly.LoadImage(path);
            this.Update(1);
            this.ctl.Edit();
        }
        if (e.getSource() == this.shapeedit)
        {
            GUIEditor.getInstance().SetupShapeEditor(ly);
            this.ctl.Edit();
        }
    }

    @Override public void Update(int m)
    {
        Layer ly = this.ctl.GetCurrentLayer();
        if (ly == null)
        {
            return;
        }
        switch (m)
        {
        case 16:
        case 32784: {
            this.ctl.Update(Control.Up_PrimType | Control.Up_LyPanel | Control.UpPrimParam);
            this.ctl.Edit();
            return;
        }
        case 32: {
            int i = ly.prim.texturefile.val;
            if (i == 0)
            {
                ly.prim.tex = ly.prim.tex0;
                break;
            }
            if (i > 0)
            {
                ly.prim.tex = new Tex(this.ctl.fileTextures[i - 1].getPath());
                break;
            }
            ly.prim.tex = null;
            break;
        }
        case 128: {
            return;
        }
        case 32896: {
            this.ctl.Update(Control.UpPrimParam);
            this.ctl.Edit();
            break;
        }
        case 32832: {
            String name = ly.prim.file.val;
            String ext = Pathname.GetExt(name);
            if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("jpg") ||
                ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("gif"))
            {
                this.ctl.journal.Write();
                this.ctl.GetCurrentLayer().LoadImage(name);
                this.ctl.Update(Control.UpPrimParam);
                this.ctl.Edit();
                break;
            }
            Dlg.Error("Should be .png/.bmp/.jpg/.jpeg/.gif");
        }
        }
        this.ctl.Edit();
        this.ctl.Update(Control.UpPrimParam);
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

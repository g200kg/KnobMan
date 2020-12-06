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
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ShapeEditor extends JPanel implements ActionListener, UpdateReq
{
    int curline;
    int curvtx;
    int drag;
    int shiftkey;
    int mouseX;
    int mouseY;
    Layer ly;
    FieldVal fiGrid;
    ParamI piGrid;
    FieldC fcFill;
    ParamT ptShape;
    JCheckBox cbGridEna;
    JCheckBox cbGridVisible;
    Graph graph;
    JToggleButton btnNewPath;
    JButton btnDel;
    JButton btnCurve;
    JButton btnLoadSvg;
    JButton btnSaveSvg;
    JButton btnClose;
    ArrayList<ArrayList<Double>> vtx;
    XYMatrix mxSvg;

    public ShapeEditor(ProfileReader ppr)
    {
        ppr.SetSection("ShapeEditor");
        this.setLayout(null);
        this.curline = -1;
        this.curvtx = -4;
        this.drag = 0;
        this.shiftkey = 0;
        this.vtx = new ArrayList();
        this.graph = new Graph();
        this.graph.setBackground(Color.gray);
        this.graph.setBounds(20, 20, 512, 512);
        this.add(this.graph);
        this.piGrid = new ParamI(16);
        this.fiGrid = new FieldVal(this, 540, 100, ppr.ReadString("grid", "Grid:"), 70, 70, false, 0.0, 100.0, this, 0);
        this.fiGrid.Setup(this.piGrid);
        this.cbGridEna = new JCheckBox(ppr.ReadString("gridenable", "Grid Enable"));
        this.cbGridEna.setBounds(540, 130, 140, 28);
        this.cbGridEna.setSelected(true);
        this.add(this.cbGridEna);
        this.cbGridVisible = new JCheckBox(ppr.ReadString("gridvisible", "Grid Visible"));
        this.cbGridVisible.setBounds(540, 160, 140, 28);
        this.cbGridVisible.setSelected(true);
        this.add(this.cbGridVisible);
        this.cbGridVisible.addActionListener(this);
        this.fcFill = new FieldC(this, 540, 200, ppr.ReadString("fill", "Fill"), 140, this, 0);
        this.btnNewPath = new JToggleButton(ppr.ReadString("newpath", "New Path"));
        this.btnNewPath.setBounds(540, 230, 120, 30);
        this.add(this.btnNewPath);
        this.btnDel = new JButton(ppr.ReadString("delpoint", "Del Point"));
        this.btnDel.setBounds(540, 260, 120, 30);
        this.add(this.btnDel);
        this.btnDel.addActionListener(this);
        this.btnCurve = new JButton(ppr.ReadString("pickhandle", "PickHandle"));
        this.btnCurve.setBounds(540, 290, 120, 30);
        this.add(this.btnCurve);
        this.btnCurve.addActionListener(this);
        this.btnLoadSvg = new JButton(ppr.ReadString("loadsvg", "LoadSVG..."));
        this.btnLoadSvg.setBounds(540, 350, 120, 30);
        this.add(this.btnLoadSvg);
        this.btnLoadSvg.addActionListener(this);
        this.btnSaveSvg = new JButton(ppr.ReadString("savesvg", "SaveAsSVG..."));
        this.btnSaveSvg.setBounds(540, 380, 120, 30);
        this.add(this.btnSaveSvg);
        this.btnSaveSvg.addActionListener(this);
        this.btnClose = new JButton(ppr.ReadString("close", "Close"));
        this.btnClose.setBounds(540, 440, 120, 30);
        this.add(this.btnClose);
        this.btnClose.addActionListener(this);
    }

    public void Setup(Layer ly)
    {
        this.ly = ly;
        this.vtx = ShapeEditor.GetVertex(ly.prim.shape.val);
        this.fcFill.Setup(ly.prim.fill);
        this.ptShape = ly.prim.shape;
        this.curline = this.vtx.size() - 1;
        this.curvtx = this.curline >= 0 ? this.vtx.get(this.curline).size() - 4 : -4;
    }

    public void AddPoint(int x, int y)
    {
        if (this.curline < 0 || this.btnNewPath.isSelected())
        {
            this.curline = this.vtx.size();
            this.vtx.add(this.curline, new ArrayList());
            this.curvtx = 0;
            this.btnNewPath.setSelected(false);
        }
        else
        {
            this.curvtx = (this.curvtx + 6) / 6 * 6;
        }
        for (int i = 0; i < 3; ++i)
        {
            this.vtx.get(this.curline).add(this.curvtx, Double.valueOf(x));
            this.vtx.get(this.curline).add(this.curvtx + 1, Double.valueOf(y));
            this.curvtx += 2;
        }
        this.curvtx -= 2;
        this.graph.repaint();
    }

    public void DelPoint()
    {
        if (this.curline >= 0 && this.curvtx >= 0)
        {
            this.curvtx = this.curvtx / 6 * 6;
            for (int i = 0; i < 6; ++i)
            {
                this.vtx.get(this.curline).remove(this.curvtx);
            }
            this.curvtx -= 4;
            if (this.curvtx < 0)
            {
                if (this.vtx.get(this.curline).size() > 0)
                {
                    this.curvtx = 2;
                }
                else
                {
                    this.vtx.remove(this.curline);
                    this.curvtx = --this.curline < 0 ? -4 : 2;
                }
            }
            this.graph.repaint();
        }
    }

    public void Curve()
    {
        if (this.curline >= 0 && this.curvtx >= 0)
        {
            this.curvtx = this.curvtx / 6 * 6;
            ArrayList<Double> v = this.vtx.get(this.curline);
            int px = v.get(this.curvtx + 2).intValue();
            int py = v.get(this.curvtx + 3).intValue();
            if (v.get(this.curvtx) == (double)px && v.get(this.curvtx + 1) == (double)py &&
                v.get(this.curvtx + 4) == (double)px && v.get(this.curvtx + 5) == (double)py)
            {
                v.set(this.curvtx, (double)px - 8.0);
                v.set(this.curvtx + 4, (double)px + 8.0);
                this.graph.repaint();
            }
        }
    }

    public void Reform(ArrayList<Double> ar, int fill)
    {
        if (ar == null)
        {
            return;
        }
        int sz = ar.size();
        if (sz > 6)
        {
            ar.set(0, ar.get(2));
            ar.set(1, ar.get(3));
            double x = ar.get(sz - 2);
            double y = ar.get(sz - 1);
            ar.add(x);
            ar.add(y);
        }
    }

    public String GetAttribute(Node n, String att)
    {
        if (n.hasAttributes())
        {
            NamedNodeMap nnm = n.getAttributes();
            Node a = nnm.getNamedItem(att);
            if (a != null)
            {
                return a.getNodeValue();
            }
            return null;
        }
        return null;
    }

    public void MakeMat(String s)
    {
        XYMatrix mx2 = new XYMatrix();
        if (s != null)
        {
            StringTokenizer stk = new StringTokenizer(s, "()");
            while (stk.hasMoreTokens())
            {
                double y;
                XYMatrix mx3;
                String cmd = stk.nextToken().trim();
                if (!stk.hasMoreTokens())
                    break;
                StringTokenizer stk2 = new StringTokenizer(stk.nextToken(), "(), ");
                if (cmd.equals("matrix"))
                {
                    mx3 = new XYMatrix();
                    for (int i = 0; i < 6; ++i)
                    {
                        mx3.Set(i, Double.parseDouble(stk2.nextToken()));
                    }
                    mx2.Mul(mx3);
                    continue;
                }
                if (cmd.equals("translate"))
                {
                    double x = Double.parseDouble(stk2.nextToken());
                    y = stk2.hasMoreTokens() ? Double.parseDouble(stk2.nextToken()) : x;
                    XYMatrix mx32 = new XYMatrix();
                    mx32.Translate(x, y);
                    mx2.Mul(mx32);
                    continue;
                }
                if (cmd.equals("scale"))
                {
                    double x = Double.parseDouble(stk2.nextToken());
                    y = stk2.hasMoreTokens() ? Double.parseDouble(stk2.nextToken()) : x;
                    XYMatrix mx33 = new XYMatrix();
                    mx33.Scale(x, y);
                    mx2.Mul(mx33);
                    continue;
                }
                if (cmd.equals("rotate"))
                {
                    double th = Double.parseDouble(stk2.nextToken());
                    double dx = 0.0;
                    double dy = 0.0;
                    if (stk2.hasMoreTokens())
                    {
                        dx = Double.parseDouble(stk2.nextToken());
                        dy = Double.parseDouble(stk2.nextToken());
                    }
                    XYMatrix mx34 = new XYMatrix();
                    mx34.RotateAt(-th, dx, dy);
                    mx2.Mul(mx34);
                    continue;
                }
                if (cmd.equals("skewX"))
                {
                    mx3 = new XYMatrix();
                    mx3.SkewX(Double.parseDouble(stk2.nextToken()));
                    mx2.Mul(mx3);
                    continue;
                }
                if (!cmd.equals("skewY"))
                    continue;
                mx3 = new XYMatrix();
                mx3.SkewY(Double.parseDouble(stk2.nextToken()));
                mx2.Mul(mx3);
            }
            mx2.Mul(this.mxSvg);
            this.mxSvg.Copy(mx2);
        }
    }

    public void AddPoint(double x, double y, ArrayList<Double> vtx)
    {
        double[] pt = new double[] {x, y};
        this.mxSvg.Transform(pt);
        vtx.add(pt[0] + 128.0);
        vtx.add(pt[1] + 128.0);
    }

    public void LoadSvg()
    {
        Control ctl = Control.getInstance();
        ctl.journal.Write();
        String strFile = "*.svg";
        FileDialog f = new FileDialog((Frame)GUIEditor.getInstance(), "Load Shape from SVG", 0);
        f.setDirectory(ctl.strImgDir);
        f.setFile(strFile);
        f.setVisible(true);
        if (f.getFile() == null)
        {
            return;
        }
        ctl.strImgDir = f.getDirectory();
        strFile = String.valueOf(f.getDirectory()) + f.getFile();
        if (Pathname.GetExt(strFile).equals(""))
        {
            strFile = String.valueOf(strFile) + ".svg";
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            File file = new File(strFile);
            if (!file.exists())
            {
                JOptionPane.showMessageDialog(null, "File Not Found\n[" + strFile + "]", "JKnobMan", 0);
                return;
            }
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            NodeList nl = doc.getElementsByTagName("path");
            this.vtx = new ArrayList();
            int je = nl.getLength();
            for (int j = 0; j < je; ++j)
            {
                Node n = nl.item(j);
                NamedNodeMap nnm = nl.item(j).getAttributes();
                String strShape = nnm.getNamedItem("d").getNodeValue();
                String strTrans = null;
                int iFill = -1;
                this.mxSvg = new XYMatrix();
                do
                {
                    String sFill = this.GetAttribute(n, "fill");
                    if (iFill < 0 && sFill != null)
                    {
                        iFill = sFill.equalsIgnoreCase("none") ? 0 : 1;
                    }
                    String sStyle = this.GetAttribute(n, "style");
                    if (iFill < 0 && sStyle != null && sStyle.indexOf("fill:") >= 0)
                    {
                        iFill = sStyle.indexOf("fill:none") >= 0 ? 0 : 1;
                    }
                    strTrans = this.GetAttribute(n, "transform");
                    this.MakeMat(strTrans);
                } while ((n = n.getParentNode()) != null);
                if (iFill < 0)
                {
                    iFill = 0;
                }
                this.ly.prim.fill.val = iFill;
                this.fcFill.cb.setSelected(iFill != 0);
                int cmd = 0;
                ArrayList<Double> vtx0 = null;
                int fcnt = 0;
                int prev = 0;
                double lx = 0.0;
                double ly = 0.0;
                double sx = 0.0;
                double sy = 0.0;
                double llx = Double.NaN;
                double lly = Double.NaN;
                double[] vn = new double[6];
                int p = 0;
                int e = strShape.length();
                while (p < e)
                {
                    int c = strShape.charAt(p);
                    if (c >= 65 && c <= 90 || c >= 97 && c <= 122)
                    {
                        cmd = c;
                        ++p;
                        switch (cmd)
                        {
                        case 77:
                        case 109: {
                            this.Reform(vtx0, iFill);
                            if (vtx0 != null)
                            {
                                this.vtx.add(vtx0);
                            }
                            vtx0 = new ArrayList<Double>();
                            vtx0.add(0.0);
                            vtx0.add(0.0);
                            break;
                        }
                        case 90:
                        case 122: {
                            this.AddPoint(lx, ly, vtx0);
                            lx = sx;
                            ly = sy;
                            this.AddPoint(lx, ly, vtx0);
                            this.AddPoint(lx, ly, vtx0);
                        }
                        }
                        continue;
                    }
                    if (c >= 48 && c <= 57 || c == 45 || c == 46)
                    {
                        int p2 = p;
                        if (c == 45)
                        {
                            ++p2;
                        }
                        char c2 = strShape.charAt(p2);
                        while (p2 < e && (c2 == '.' || c2 >= '0' && c2 <= '9'))
                        {
                            if (++p2 >= e || (c2 = strShape.charAt(p2)) != 'e' && c2 != 'E' || ++p2 >= e ||
                                (c2 = strShape.charAt(p2)) != '-' || ++p2 >= e)
                                continue;
                            c2 = strShape.charAt(p2);
                        }
                        if (fcnt < 6)
                        {
                            vn[fcnt] = Double.parseDouble(strShape.substring(p, p2));
                        }
                        ++fcnt;
                        switch (cmd)
                        {
                        case 90:
                        case 122: {
                            fcnt = 0;
                            prev = 0;
                            break;
                        }
                        case 77: {
                            if (fcnt < 2)
                                break;
                            fcnt = 0;
                            prev = 0;
                            sx = lx = vn[0];
                            sy = ly = vn[1];
                            cmd = 76;
                            this.AddPoint(lx, ly, vtx0);
                            break;
                        }
                        case 109: {
                            if (fcnt < 2)
                                break;
                            fcnt = 0;
                            prev = 0;
                            sx = lx += vn[0];
                            sy = ly += vn[1];
                            cmd = 108;
                            this.AddPoint(lx, ly, vtx0);
                            break;
                        }
                        case 67: {
                            if (fcnt < 6)
                                break;
                            prev = 3;
                            fcnt = 0;
                            this.AddPoint(vn[0], vn[1], vtx0);
                            this.AddPoint(vn[2], vn[3], vtx0);
                            this.AddPoint(vn[4], vn[5], vtx0);
                            lx = vn[4];
                            ly = vn[5];
                            llx = vn[2];
                            lly = vn[3];
                            break;
                        }
                        case 99: {
                            if (fcnt < 6)
                                break;
                            prev = 3;
                            fcnt = 0;
                            this.AddPoint(lx + vn[0], ly + vn[1], vtx0);
                            this.AddPoint(lx + vn[2], ly + vn[3], vtx0);
                            this.AddPoint(lx + vn[4], ly + vn[5], vtx0);
                            llx = lx + vn[2];
                            lly = ly + vn[3];
                            lx += vn[4];
                            ly += vn[5];
                            break;
                        }
                        case 83: {
                            if (fcnt < 4)
                                break;
                            prev = 3;
                            fcnt = 0;
                            if (prev != 3)
                            {
                                this.AddPoint(lx, ly, vtx0);
                            }
                            else
                            {
                                this.AddPoint(lx + lx - llx, ly + ly - lly, vtx0);
                            }
                            this.AddPoint(vn[0], vn[1], vtx0);
                            this.AddPoint(vn[2], vn[3], vtx0);
                            llx = vn[0];
                            lly = vn[1];
                            lx = vn[2];
                            ly = vn[3];
                            break;
                        }
                        case 115: {
                            if (fcnt < 4)
                                break;
                            prev = 3;
                            fcnt = 0;
                            if (prev != 3)
                            {
                                this.AddPoint(lx, ly, vtx0);
                            }
                            else
                            {
                                this.AddPoint(lx + lx - llx, ly + ly - lly, vtx0);
                            }
                            this.AddPoint(lx + vn[0], ly + vn[1], vtx0);
                            this.AddPoint(lx + vn[2], ly + vn[3], vtx0);
                            llx = lx + vn[0];
                            lly = ly + vn[1];
                            lx += vn[2];
                            ly += vn[3];
                            break;
                        }
                        case 81: {
                            if (fcnt < 4)
                                break;
                            prev = 2;
                            fcnt = 0;
                            this.AddPoint(lx + (vn[0] - lx) * 2.0 / 3.0, ly + (vn[1] - ly) * 2.0 / 3.0, vtx0);
                            this.AddPoint(vn[2] + (vn[0] - vn[2]) * 2.0 / 3.0, vn[3] + (vn[1] - vn[3]) * 2.0 / 3.0,
                                          vtx0);
                            this.AddPoint(vn[2], vn[3], vtx0);
                            lx = vn[2];
                            ly = vn[3];
                            llx = vn[0];
                            lly = vn[1];
                            break;
                        }
                        case 113: {
                            if (fcnt < 4)
                                break;
                            prev = 2;
                            fcnt = 0;
                            this.AddPoint(lx + vn[0] * 2.0 / 3.0, ly + vn[1] * 2.0 / 3.0, vtx0);
                            this.AddPoint(lx + vn[2] + (vn[0] - vn[2]) * 2.0 / 3.0,
                                          ly + vn[3] + (vn[1] - vn[3]) * 2.0 / 3.0, vtx0);
                            this.AddPoint(lx + vn[2], ly + vn[3], vtx0);
                            llx = lx + vn[0];
                            lly = ly + vn[1];
                            lx += vn[2];
                            ly += vn[3];
                            break;
                        }
                        case 84: {
                            if (fcnt < 2)
                                break;
                            prev = 2;
                            fcnt = 0;
                            double px = lx;
                            double py = ly;
                            if (prev == 2)
                            {
                                px = lx + lx - llx;
                                py = ly + ly - lly;
                            }
                            llx = px;
                            lly = py;
                            this.AddPoint(lx + (px - lx) * 2.0 / 3.0, ly + (py - ly) * 2.0 / 3.0, vtx0);
                            lx = vn[0];
                            ly = vn[1];
                            this.AddPoint(lx + (llx - lx) * 2.0 / 3.0, ly + (lly - ly) * 2.0 / 3.0, vtx0);
                            this.AddPoint(lx, ly, vtx0);
                            break;
                        }
                        case 116: {
                            if (fcnt < 2)
                                break;
                            prev = 2;
                            fcnt = 0;
                            double px = lx;
                            double py = ly;
                            if (prev == 2)
                            {
                                px = lx + lx - llx;
                                py = ly + ly - lly;
                            }
                            llx = px;
                            lly = py;
                            this.AddPoint(lx + (llx - lx) * 2.0 / 3.0, ly + (lly - ly) * 2.0 / 3.0, vtx0);
                            this.AddPoint((lx += vn[0]) + (llx - lx) * 2.0 / 3.0,
                                          (ly += vn[1]) + (lly - ly) * 2.0 / 3.0, vtx0);
                            this.AddPoint(lx, ly, vtx0);
                            break;
                        }
                        case 76: {
                            if (fcnt < 2)
                                break;
                            fcnt = 0;
                            prev = 0;
                            this.AddPoint(lx, ly, vtx0);
                            lx = vn[0];
                            ly = vn[1];
                            this.AddPoint(lx, ly, vtx0);
                            this.AddPoint(lx, ly, vtx0);
                            lx = vn[0];
                            ly = vn[1];
                            break;
                        }
                        case 108: {
                            if (fcnt < 2)
                                break;
                            fcnt = 0;
                            prev = 0;
                            this.AddPoint(lx, ly, vtx0);
                            this.AddPoint(lx += vn[0], ly += vn[1], vtx0);
                            this.AddPoint(lx, ly, vtx0);
                            break;
                        }
                        case 72: {
                            if (fcnt < 1)
                                break;
                            fcnt = 0;
                            prev = 0;
                            this.AddPoint(lx, ly, vtx0);
                            lx = vn[0];
                            this.AddPoint(lx, ly, vtx0);
                            this.AddPoint(lx, ly, vtx0);
                            break;
                        }
                        case 104: {
                            if (fcnt < 1)
                                break;
                            fcnt = 0;
                            prev = 0;
                            this.AddPoint(lx, ly, vtx0);
                            this.AddPoint(lx += vn[0], ly, vtx0);
                            this.AddPoint(lx, ly, vtx0);
                            break;
                        }
                        case 86: {
                            if (fcnt < 1)
                                break;
                            fcnt = 0;
                            prev = 0;
                            this.AddPoint(lx, ly, vtx0);
                            ly = vn[0];
                            this.AddPoint(lx, ly, vtx0);
                            this.AddPoint(lx, ly, vtx0);
                            break;
                        }
                        case 118: {
                            if (fcnt < 1)
                                break;
                            fcnt = 0;
                            prev = 0;
                            this.AddPoint(lx, ly, vtx0);
                            this.AddPoint(lx, ly += vn[0], vtx0);
                            this.AddPoint(lx, ly, vtx0);
                        }
                        }
                        p = p2;
                        continue;
                    }
                    ++p;
                }
                this.Reform(vtx0, iFill);
                if (vtx0 == null)
                    continue;
                this.vtx.add(vtx0);
            }
            this.graph.repaint();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void SaveSvg()
    {
        int r;
        Control ctl = Control.getInstance();
        FileDialog f = new FileDialog((Frame)GUIEditor.getInstance(), "Save Shape as SVG", 1);
        f.setDirectory(ctl.strImgDir);
        f.setFile("Shape.svg");
        f.setVisible(true);
        if (f.getFile() == null)
        {
            return;
        }
        ctl.strImgDir = f.getDirectory();
        String strFile = String.valueOf(f.getDirectory()) + f.getFile();
        if (Pathname.GetExt(strFile).equals("") && new File(strFile = String.valueOf(strFile) + ".svg").exists() &&
            (r = JOptionPane.showConfirmDialog(null, "File already exist. Overwrite?\n[" + strFile + "]", "JKnobMan",
                                               2)) == 2)
        {
            return;
        }
        try
        {
            FileOutputStream os = new FileOutputStream(strFile);
            OutputStreamWriter fw = new OutputStreamWriter((OutputStream)os, "UTF-8");
            String str = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n";
            str = String.valueOf(str) + "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"256\" height=\"256\">\n";
            str =
                this.fcFill.pc.val != 0
                    ? String.valueOf(str) +
                          "<path id=\"Shape\" fill-rule=\"evenodd\" fill=\"red\" stroke=\"black\" stroke-width=\"1\"\nd=\""
                    : String.valueOf(str) +
                          "<path id=\"Shape\" fill=\"none\" stroke=\"black\" stroke-width=\"1\"\nd=\"";
            for (int i = 0; i < this.vtx.size(); ++i)
            {
                int j;
                str = String.valueOf(str) +
                      String.format("M %.2f,%.2f\n   ", this.vtx.get(i).get(2) - 128.0, this.vtx.get(i).get(3) - 128.0);
                for (j = 6; j < this.vtx.get(i).size(); j += 6)
                {
                    str = String.valueOf(str) +
                          String.format("C %.2f,%.2f %.2f,%.2f %.2f,%.2f\n   ", this.vtx.get(i).get(j - 2) - 128.0,
                                        this.vtx.get(i).get(j - 1) - 128.0, this.vtx.get(i).get(j) - 128.0,
                                        this.vtx.get(i).get(j + 1) - 128.0, this.vtx.get(i).get(j + 2) - 128.0,
                                        this.vtx.get(i).get(j + 3) - 128.0);
                }
                if (this.fcFill.pc.val == 0)
                    continue;
                str = String.valueOf(str) +
                      String.format("C %.2f,%.2f %.2f,%.2f %.2f,%.2f\n   ", this.vtx.get(i).get(j - 2) - 128.0,
                                    this.vtx.get(i).get(j - 1) - 128.0, this.vtx.get(i).get(0) - 128.0,
                                    this.vtx.get(i).get(1) - 128.0, this.vtx.get(i).get(2) - 128.0,
                                    this.vtx.get(i).get(3) - 128.0);
            }
            str = String.valueOf(str) + "\"/>\n</svg>\n";
            fw.write(str);
            ((Writer)fw).close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static ArrayList<ArrayList<Double>> GetVertex(String str)
    {
        ArrayList<ArrayList<Double>> vtx = new ArrayList<ArrayList<Double>>();
        String[] pstr = str.split("/");
        for (int i = 1; i < pstr.length; ++i)
        {
            vtx.add(new ArrayList());
            String[] ppstr = pstr[i].split(":");
            for (int j = 0; j < ppstr.length; ++j)
            {
                String[] pppstr = ppstr[j].split(",");
                for (int k = 0; k < 6; ++k)
                {
                    vtx.get(i - 1).add(Double.parseDouble(pppstr[k]));
                }
            }
        }
        return vtx;
    }

    public String GetStr()
    {
        String str = "";
        for (int i = 0; i < this.vtx.size(); ++i)
        {
            int j = 0;
            while (j + 5 < this.vtx.get(i).size())
            {
                str = j == 0 ? String.valueOf(str) + "/" : String.valueOf(str) + ":";
                str = String.valueOf(str) + this.vtx.get(i).get(j) + "," + this.vtx.get(i).get(j + 1) + "," +
                      this.vtx.get(i).get(j + 2) + "," + this.vtx.get(i).get(j + 3) + "," + this.vtx.get(i).get(j + 4) +
                      "," + this.vtx.get(i).get(j + 5);
                j += 6;
            }
        }
        return str;
    }

    static double scalex(double x)
    {
        return (x - 128.0) / 256.0 * (double)Control.getInstance().prefs.width;
    }

    static double scaley(double y)
    {
        return (y - 128.0) / 256.0 * (double)Control.getInstance().prefs.height;
    }

    static Path2D MakePath(String str, int f, int scale, int i)
    {
        return ShapeEditor.MakePath(ShapeEditor.GetVertex(str), f, scale, i);
    }

    static Path2D MakePath(ArrayList<ArrayList<Double>> vtx, int f, int scale, int i)
    {
        Path2D.Double path = new Path2D.Double();
        path.setWindingRule(0);
        path.reset();
        if (scale != 0)
        {
            if (i == -1)
            {
                for (i = 0; i < vtx.size(); ++i)
                {
                    int j = 0;
                    while (j + 5 < vtx.get(i).size())
                    {
                        if (j == 0)
                        {
                            ((Path2D)path)
                                .moveTo(ShapeEditor.scalex(vtx.get(i).get(j + 2)),
                                        ShapeEditor.scaley(vtx.get(i).get(j + 3)));
                        }
                        else
                        {
                            ((Path2D)path)
                                .curveTo(ShapeEditor.scalex(vtx.get(i).get(j - 2)),
                                         ShapeEditor.scaley(vtx.get(i).get(j - 1)),
                                         ShapeEditor.scalex(vtx.get(i).get(j)),
                                         ShapeEditor.scaley(vtx.get(i).get(j + 1)),
                                         ShapeEditor.scalex(vtx.get(i).get(j + 2)),
                                         ShapeEditor.scaley(vtx.get(i).get(j + 3)));
                        }
                        j += 6;
                    }
                    if (f == 0)
                        continue;
                    ((Path2D)path)
                        .curveTo(ShapeEditor.scalex(vtx.get(i).get(j - 2)), ShapeEditor.scaley(vtx.get(i).get(j - 1)),
                                 ShapeEditor.scalex(vtx.get(i).get(0)), ShapeEditor.scaley(vtx.get(i).get(1)),
                                 ShapeEditor.scalex(vtx.get(i).get(2)), ShapeEditor.scaley(vtx.get(i).get(3)));
                }
                return path;
            }
            int j = 0;
            while (j + 5 < vtx.get(i).size())
            {
                if (j == 0)
                {
                    ((Path2D)path)
                        .moveTo(ShapeEditor.scalex(vtx.get(i).get(j + 2)), ShapeEditor.scaley(vtx.get(i).get(j + 3)));
                }
                else
                {
                    ((Path2D)path)
                        .curveTo(ShapeEditor.scalex(vtx.get(i).get(j - 2)), ShapeEditor.scaley(vtx.get(i).get(j - 1)),
                                 ShapeEditor.scalex(vtx.get(i).get(j)), ShapeEditor.scaley(vtx.get(i).get(j + 1)),
                                 ShapeEditor.scalex(vtx.get(i).get(j + 2)), ShapeEditor.scaley(vtx.get(i).get(j + 3)));
                }
                j += 6;
            }
            if (f != 0)
            {
                ((Path2D)path)
                    .curveTo(ShapeEditor.scalex(vtx.get(i).get(j - 2)), ShapeEditor.scaley(vtx.get(i).get(j - 1)),
                             ShapeEditor.scalex(vtx.get(i).get(0)), ShapeEditor.scaley(vtx.get(i).get(1)),
                             ShapeEditor.scalex(vtx.get(i).get(2)), ShapeEditor.scaley(vtx.get(i).get(3)));
            }
            return path;
        }
        if (i == -1)
        {
            for (i = 0; i < vtx.size(); ++i)
            {
                int j = 0;
                while (j + 5 < vtx.get(i).size())
                {
                    if (j == 0)
                    {
                        ((Path2D)path).moveTo(vtx.get(i).get(j + 2), vtx.get(i).get(j + 3));
                    }
                    else
                    {
                        ((Path2D)path)
                            .curveTo(vtx.get(i).get(j - 2), vtx.get(i).get(j - 1), vtx.get(i).get(j),
                                     vtx.get(i).get(j + 1), vtx.get(i).get(j + 2), vtx.get(i).get(j + 3));
                    }
                    j += 6;
                }
                if (f == 0)
                    continue;
                ((Path2D)path)
                    .curveTo(vtx.get(i).get(j - 2), vtx.get(i).get(j - 1), vtx.get(i).get(0), vtx.get(i).get(1),
                             vtx.get(i).get(2), vtx.get(i).get(3));
            }
            return path;
        }
        int j = 0;
        while (j + 5 < vtx.get(i).size())
        {
            if (j == 0)
            {
                ((Path2D)path).moveTo(vtx.get(i).get(j + 2), vtx.get(i).get(j + 3));
            }
            else
            {
                ((Path2D)path)
                    .curveTo(vtx.get(i).get(j - 2), vtx.get(i).get(j - 1), vtx.get(i).get(j), vtx.get(i).get(j + 1),
                             vtx.get(i).get(j + 2), vtx.get(i).get(j + 3));
            }
            j += 6;
        }
        if (f != 0)
        {
            ((Path2D)path)
                .curveTo(vtx.get(i).get(j - 2), vtx.get(i).get(j - 1), vtx.get(i).get(0), vtx.get(i).get(1),
                         vtx.get(i).get(2), vtx.get(i).get(3));
        }
        return path;
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        Control.getInstance().journal.Write();
        Object s = e.getSource();
        if (s == this.btnClose)
        {
            GUIEditor.getInstance().SetupLayer(Control.getInstance().GetCurrentLayer());
        }
        if (s == this.btnDel)
        {
            this.DelPoint();
        }
        if (s == this.btnCurve)
        {
            this.Curve();
        }
        if (s == this.cbGridVisible)
        {
            this.graph.repaint();
        }
        if (s == this.btnLoadSvg)
        {
            this.LoadSvg();
        }
        if (s == this.btnSaveSvg)
        {
            this.SaveSvg();
        }
    }

    @Override public void Update(int m)
    {
        this.graph.repaint();
    }

    class Graph extends JPanel implements MouseMotionListener, MouseListener
    {
        Graph()
        {
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            ShapeEditor.this.drag = -1;
        }

        @Override public void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.lightGray);
            g2.fillRect(0, 0, 512, 512);
            g2.setColor(Color.gray);
            g2.drawRect(0, 0, 511, 511);
            g2.drawRect(128, 128, 256, 256);
            g2.drawOval(128, 128, 256, 256);
            g2.drawRect(256, 0, 256, 512);
            g2.drawRect(0, 256, 512, 256);
            Path2D path = ShapeEditor.MakePath(ShapeEditor.this.vtx, ShapeEditor.this.fcFill.pc.val, 0, -1);
            if (ShapeEditor.this.fcFill.pc.val != 0)
            {
                g2.setColor(new Color(255, 192, 192, 128));
                g2.fill(path);
            }
            g2.setColor(Color.blue);
            g2.setStroke(new BasicStroke(2.0f));
            g2.draw(path);
            for (int i = 0; i < ShapeEditor.this.vtx.size(); ++i)
            {
                for (int j = 2; j < ShapeEditor.this.vtx.get(i).size(); j += 6)
                {
                    if (i == ShapeEditor.this.curline && j / 6 == ShapeEditor.this.curvtx / 6)
                    {
                        g2.setColor(Color.red);
                        g2.setStroke(new BasicStroke(2.0f));
                        int lx = ShapeEditor.this.vtx.get(i).get(j).intValue();
                        int ly = ShapeEditor.this.vtx.get(i).get(j + 1).intValue();
                        g2.drawLine(ShapeEditor.this.vtx.get(i).get(j - 2).intValue(),
                                    ShapeEditor.this.vtx.get(i).get(j - 1).intValue(), lx, ly);
                        g2.drawLine(lx, ly, ShapeEditor.this.vtx.get(i).get(j + 2).intValue(),
                                    ShapeEditor.this.vtx.get(i).get(j + 3).intValue());
                        this.DrawHandle(g2, i, j - 2, 1, Color.red);
                        this.DrawHandle(g2, i, j + 2, 1, Color.red);
                        if (j == 2)
                        {
                            this.DrawHandle(g2, i, j, 0, Color.red);
                            continue;
                        }
                        this.DrawHandle(g2, i, j, 1, Color.red);
                        continue;
                    }
                    if (j == 2)
                    {
                        this.DrawHandle(g2, i, j, 0, Color.blue);
                        continue;
                    }
                    this.DrawHandle(g2, i, j, 1, Color.blue);
                }
            }
            g2.setColor(Color.black);
            if (ShapeEditor.this.piGrid.val > 2 && ShapeEditor.this.cbGridVisible.isSelected())
            {
                for (int y = 0; y < 256; y += ShapeEditor.this.piGrid.val)
                {
                    for (int x = 0; x < 256; x += ShapeEditor.this.piGrid.val)
                    {
                        g2.fillRect(256 + x, 256 + y, 1, 1);
                        g2.fillRect(256 + x, 256 - y, 1, 1);
                        g2.fillRect(256 - x, 256 + y, 1, 1);
                        g2.fillRect(256 - x, 256 - y, 1, 1);
                    }
                }
            }
            ShapeEditor.this.ptShape.val = ShapeEditor.this.GetStr();
            Control.getInstance().Update(Control.UpPrimParam);
        }

        public void DrawHandle(Graphics2D g2, int i, int j, int t, Color c)
        {
            int x = ShapeEditor.this.vtx.get(i).get(j).intValue();
            int y = ShapeEditor.this.vtx.get(i).get(j + 1).intValue();
            g2.setColor(c);
            if (t == 0)
            {
                g2.fillRect(x - 4, y - 4, 8, 8);
                g2.setColor(Color.white);
                g2.fillRect(x - 2, y - 2, 4, 4);
            }
            else
            {
                g2.fillOval(x - 4, y - 4, 8, 8);
                g2.setColor(Color.white);
                g2.fillOval(x - 2, y - 2, 4, 4);
            }
        }

        void Getxy(MouseEvent e)
        {
            Point pt = e.getPoint();
            ShapeEditor.this.mouseX = pt.x;
            ShapeEditor.this.mouseY = pt.y;
        }

        int GridXY(int x)
        {
            if (!ShapeEditor.this.cbGridEna.isSelected())
            {
                return x;
            }
            if (x >= 256)
            {
                return 256 + (x - 256 + ShapeEditor.this.piGrid.val / 2) / ShapeEditor.this.piGrid.val *
                                 ShapeEditor.this.piGrid.val;
            }
            return 256 +
                (x - 256 - ShapeEditor.this.piGrid.val / 2) / ShapeEditor.this.piGrid.val * ShapeEditor.this.piGrid.val;
        }

        @Override public void mouseMoved(MouseEvent e)
        {
        }

        @Override public void mouseDragged(MouseEvent e)
        {
            this.Getxy(e);
            int px = this.GridXY(ShapeEditor.this.mouseX);
            int py = this.GridXY(ShapeEditor.this.mouseY);
            if (ShapeEditor.this.drag != 0)
            {
                int dx =
                    px - ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx).intValue();
                int dy =
                    py - ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx + 1).intValue();
                ShapeEditor.this.vtx.get(ShapeEditor.this.curline).set(ShapeEditor.this.curvtx, Double.valueOf(px));
                ShapeEditor.this.vtx.get(ShapeEditor.this.curline).set(ShapeEditor.this.curvtx + 1, Double.valueOf(py));
                if (ShapeEditor.this.curvtx % 6 == 2)
                {
                    ShapeEditor.this.vtx.get(ShapeEditor.this.curline)
                        .set(ShapeEditor.this.curvtx - 2,
                             ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx - 2) +
                                 (double)dx);
                    ShapeEditor.this.vtx.get(ShapeEditor.this.curline)
                        .set(ShapeEditor.this.curvtx - 1,
                             ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx - 1) +
                                 (double)dy);
                    ShapeEditor.this.vtx.get(ShapeEditor.this.curline)
                        .set(ShapeEditor.this.curvtx + 2,
                             ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx + 2) +
                                 (double)dx);
                    ShapeEditor.this.vtx.get(ShapeEditor.this.curline)
                        .set(ShapeEditor.this.curvtx + 3,
                             ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx + 3) +
                                 (double)dy);
                }
                else if (ShapeEditor.this.curvtx % 6 == 0)
                {
                    if (!e.isShiftDown())
                    {
                        ShapeEditor.this.vtx.get(ShapeEditor.this.curline)
                            .set(ShapeEditor.this.curvtx + 4,
                                 ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx + 2) *
                                         2.0 -
                                     (double)px);
                        ShapeEditor.this.vtx.get(ShapeEditor.this.curline)
                            .set(ShapeEditor.this.curvtx + 5,
                                 ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx + 3) *
                                         2.0 -
                                     (double)py);
                    }
                }
                else if (!e.isShiftDown())
                {
                    ShapeEditor.this.vtx.get(ShapeEditor.this.curline)
                        .set(ShapeEditor.this.curvtx - 4,
                             ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx - 2) * 2.0 -
                                 (double)px);
                    ShapeEditor.this.vtx.get(ShapeEditor.this.curline)
                        .set(ShapeEditor.this.curvtx - 3,
                             ShapeEditor.this.vtx.get(ShapeEditor.this.curline).get(ShapeEditor.this.curvtx - 1) * 2.0 -
                                 (double)py);
                }
                this.repaint();
            }
        }

        @Override public void mousePressed(MouseEvent e)
        {
            this.Getxy(e);
            int h = this.HitTest(ShapeEditor.this.mouseX, ShapeEditor.this.mouseY);
            if (h >= 0)
            {
                int l = h >> 8;
                int v = h & 0xFF;
                if (v % 6 == 2 || l == ShapeEditor.this.curline && v / 6 == ShapeEditor.this.curvtx / 6)
                {
                    ShapeEditor.this.curline = l;
                    ShapeEditor.this.curvtx = v;
                    ShapeEditor.this.drag = 1;
                    this.repaint();
                }
            }
            else
            {
                ShapeEditor.this.AddPoint(this.GridXY(ShapeEditor.this.mouseX), this.GridXY(ShapeEditor.this.mouseY));
                ShapeEditor.this.drag = 1;
            }
        }

        @Override public void mouseReleased(MouseEvent e)
        {
            ShapeEditor.this.drag = 0;
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

        public int HitTest(int x, int y)
        {
            int r = -1;
            for (int i = 0; i < ShapeEditor.this.vtx.size(); ++i)
            {
                for (int j = 0; j < ShapeEditor.this.vtx.get(i).size(); j += 2)
                {
                    if (!(Math.abs((double)x - ShapeEditor.this.vtx.get(i).get(j)) < 5.0) ||
                        !(Math.abs((double)y - ShapeEditor.this.vtx.get(i).get(j + 1)) < 5.0))
                        continue;
                    r = (i << 8) + j;
                    if (ShapeEditor.this.shiftkey != 0 || j % 6 != 2)
                        continue;
                    return r;
                }
            }
            return r;
        }
    }
}

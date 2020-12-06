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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

public class Primitive
{
    Bitmap bmp;
    Bitmap bmpImage;
    ParamCol color;
    ParamS type;
    ParamS texturefile;
    ParamS transparent;
    ParamS font;
    ParamS textalign;
    ParamS framealign;
    ParamV aspect;
    ParamV round;
    ParamV width;
    ParamV length;
    ParamV step;
    ParamV anglestep;
    ParamV emboss;
    ParamV embossdiffuse;
    ParamV ambient;
    ParamV lightdir;
    ParamV specular;
    ParamV specularwidth;
    ParamV texturedepth;
    ParamV texturezoom;
    ParamV diffuse;
    ParamV fontsize;
    ParamT file;
    ParamT text;
    ParamT shape;
    ParamC autofit;
    ParamC bold;
    ParamC italic;
    ParamC fill;
    ParamI intellialpha;
    ParamI numframe;
    String texturename;
    Prefs prefs;
    Layer ly;
    Tex tex;
    Tex tex0;

    public Primitive(Prefs prefs, Layer ly, boolean bActive)
    {
        this.ly = ly;
        this.prefs = prefs;
        this.bmpImage = null;
        this.bmp = bActive ? new Bitmap(prefs.width, prefs.height) : null;
        this.tex0 = null;
        this.tex = null;
        this.texturename = "";
        this.type = new ParamS(0);
        this.aspect = new ParamV(0.0);
        this.round = new ParamV(0.0);
        this.width = new ParamV(10.0);
        this.length = new ParamV(50.0);
        this.step = new ParamV(20.0);
        this.anglestep = new ParamV(45.0);
        this.emboss = new ParamV(0.0);
        this.embossdiffuse = new ParamV(0.0);
        this.ambient = new ParamV(50.0);
        this.lightdir = new ParamV(-50.0);
        this.specular = new ParamV(0.0);
        this.specularwidth = new ParamV(50.0);
        this.texturefile = new ParamS(0);
        this.texturedepth = new ParamV(0.0);
        this.texturezoom = new ParamV(100.0);
        this.diffuse = new ParamV(0.0);
        this.file = new ParamT("");
        this.transparent = new ParamS(0);
        this.intellialpha = new ParamI(0);
        this.autofit = new ParamC(1);
        this.numframe = new ParamI(1);
        this.framealign = new ParamS(0);
        this.text = new ParamT("(1:99)");
        this.font = new ParamS(0);
        this.fontsize = new ParamV(50.0);
        this.bold = new ParamC(0);
        this.italic = new ParamC(0);
        this.textalign = new ParamS(0);
        this.shape = new ParamT("");
        this.fill = new ParamC(1);
        this.color = new ParamCol(new Col(255, 0, 0));
        this.tex = null;
    }

    public void SetSize(Prefs prefs)
    {
        if (this.type.val == 1 && this.autofit.val == 0)
        {
            if (this.bmpImage != null)
            {
                int isw = this.bmpImage.width;
                int ish = this.bmpImage.height;
                int i = this.numframe.val;
                if (i > 1)
                {
                    if (this.framealign.val == 1)
                    {
                        isw /= i;
                    }
                    else
                    {
                        ish /= i;
                    }
                }
                this.bmp = new Bitmap(isw, ish);
                this.Render(0, 2);
            }
        }
        else
        {
            this.bmp = new Bitmap(prefs.width, prefs.height);
            this.Render(0, 2);
        }
    }

    public void CopyFrom(Primitive pr)
    {
        if (pr.bmpImage == null)
        {
            this.bmpImage = null;
        }
        else
        {
            this.bmpImage = new Bitmap(pr.bmpImage.width, pr.bmpImage.height);
            this.bmpImage.CopyFrom(pr.bmpImage, pr.bmpImage.width, pr.bmpImage.height);
        }
        this.type.val = pr.type.val;
        this.aspect.val = pr.aspect.val;
        this.round.val = pr.round.val;
        this.width.val = pr.width.val;
        this.length.val = pr.length.val;
        this.step.val = pr.step.val;
        this.anglestep.val = pr.anglestep.val;
        this.emboss.val = pr.emboss.val;
        this.embossdiffuse.val = pr.embossdiffuse.val;
        this.ambient.val = pr.ambient.val;
        this.lightdir.val = pr.lightdir.val;
        this.specular.val = pr.specular.val;
        this.specularwidth.val = pr.specularwidth.val;
        this.texturefile.val = pr.texturefile.val;
        this.texturedepth.val = pr.texturedepth.val;
        this.texturezoom.val = pr.texturezoom.val;
        this.diffuse.val = pr.diffuse.val;
        this.file.val = new String(pr.file.val);
        this.transparent.val = pr.transparent.val;
        this.intellialpha.val = pr.intellialpha.val;
        this.autofit.val = pr.autofit.val;
        this.numframe.val = pr.numframe.val;
        this.framealign.val = pr.framealign.val;
        this.text.val = new String(pr.text.val);
        this.font.val = pr.font.val;
        this.fontsize.val = pr.fontsize.val;
        this.bold.val = pr.bold.val;
        this.italic.val = pr.italic.val;
        this.textalign.val = pr.textalign.val;
        this.shape.val = new String(pr.shape.val);
        this.fill.val = pr.fill.val;
        this.color.col = new Col(pr.color.col);
        this.texturename = new String(pr.texturename);
        if (pr.tex == null)
        {
            this.tex0 = null;
            this.tex = null;
        }
        else
        {
            this.tex = pr.tex.Clone();
        }
    }

    public void Test()
    {
        for (int y = 0; y < this.bmp.img.getHeight(); ++y)
        {
            for (int x = 0; x < this.bmp.img.getWidth(); ++x)
            {
                this.bmp.img.setRGB(x, y, -2145189991);
            }
        }
    }

    public void Update()
    {
        if (!this.CheckAnim())
        {
            this.Render(0, 1);
        }
    }

    public boolean CheckAnim()
    {
        if (this.type.val == 1 && this.numframe.val > 1)
        {
            return true;
        }
        return this.type.val == 14;
    }

    public void RenderNone()
    {
        Col col = new Col(0, 0, 0, 0);
        for (int y = 0; y < this.prefs.height; ++y)
        {
            for (int x = 0; x < this.prefs.width; ++x)
            {
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderImage(int frame, int total)
    {
        int defcolor = 0;
        Col col = new Col(0, 0, 0, 0);
        int i = this.numframe.val;
        for (int y = 0; y < this.bmp.height; ++y)
        {
            for (int x = 0; x < this.bmp.width; ++x)
            {
                this.bmp.SetPix(x, y, col);
            }
        }
        if (i == 0 || this.bmpImage == null)
        {
            return;
        }
        int iOffx = 0;
        int iOffy = 0;
        int isw = this.bmpImage.width;
        int ish = this.bmpImage.height;
        if (total > 1)
        {
            if (this.framealign.val == 1)
            {
                iOffx = (isw /= i) * Math.min(i - 1, i * frame / (total - 1));
            }
            else
            {
                iOffy = (ish /= i) * Math.min(i - 1, i * frame / (total - 1));
            }
        }
        if (this.bmpImage != null && this.bmpImage.width > 0 && this.bmpImage.height > 0)
        {
            Bitmap b2 = new Bitmap(this.bmpImage);
            defcolor = b2.GetPix(0, 0);
            IntelliAlpha.Process(b2, defcolor, this.intellialpha.val, this.transparent.val);
            if (this.autofit.val != 0)
            {
                b2.DecimationTo(this.bmp, 0, 0, this.prefs.width, this.prefs.height, iOffx, iOffy, isw, ish);
            }
            else
            {
                Graphics2D g2 = (Graphics2D)this.bmp.img.getGraphics();
                g2.drawImage(b2.img, 0, 0, isw, ish, iOffx, iOffy, iOffx + isw, iOffy + ish, null);
            }
        }
    }

    public void RenderRect()
    {
        double rCX = (double)this.prefs.width * 0.5;
        double rCY = (double)this.prefs.height * 0.5;
        double rXRO = rCX + 0.5;
        double rYRO = rCY + 0.5;
        if (this.aspect.val > 0.0)
        {
            rXRO = rXRO * (100.0 - Math.min(this.aspect.val, 99.0)) / 100.0;
        }
        if (this.aspect.val < 0.0)
        {
            rYRO = rYRO * (100.0 + Math.max(this.aspect.val, -99.0)) / 100.0;
        }
        double rWidth = Math.min(rCX, rCY) * this.width.val / 100.0 + 1.0;
        double rWidth2 = rWidth * 0.5;
        double rD = rWidth2 * this.diffuse.val / 100.0;
        double rXRC = rXRO - rWidth2;
        double rYRC = rYRO - rWidth2;
        double rXRI = rXRO - rWidth;
        double rYRI = rYRO - rWidth;
        double rRoundO = this.round.val * Math.min(rCX, rCY) / 100.0;
        double rRoundI = rRoundO - rWidth;
        double rXCC = rXRO - rRoundO;
        double rYCC = rYRO - rRoundO;
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rY = -((double)y + 0.5 - rCY);
            double rYN = rY / rCY;
            double rYA = Math.abs(rY);
            for (int x = 0; x < this.prefs.width; ++x)
            {
                double r;
                double rX = -((double)x + 0.5 - rCX);
                double rXN = rX / rCX;
                double rXA = Math.abs(rX);
                double rAlpha = 1.0;
                double rSpec = rWidth;
                if (rXA > rXRO)
                {
                    rAlpha = 0.0;
                }
                else if (rXA > rXRO - 1.0 - rD)
                {
                    rAlpha = (rXRO - rXA) / (1.0 + rD);
                }
                if (rYA > rYRO)
                {
                    rAlpha = 0.0;
                }
                else if (rYA > rYRO - 1.0 - rD)
                {
                    rAlpha = rAlpha * (rYRO - rYA) / (1.0 + rD);
                }
                if (rXA >= rXCC && rYA >= rYCC)
                {
                    double rdx = rXA - rXCC;
                    double rdy = rYA - rYCC;
                    r = Math.sqrt(rdx * rdx + rdy * rdy);
                    if (r > rRoundO)
                    {
                        rAlpha = 0.0;
                    }
                    if (r > rRoundO - 1.0 - rD)
                    {
                        rAlpha = rAlpha * (rRoundO - r) / (1.0 + rD);
                    }
                    if (r < rRoundI)
                    {
                        rAlpha = 0.0;
                    }
                    if (r < rRoundI + 1.0)
                    {
                        rAlpha *= r - rRoundI;
                    }
                }
                else if (rXA < rXRI && rYA < rYRI)
                {
                    rAlpha = 0.0;
                }
                else if (rXA < rXRI + 1.0 + rD && rYA < rYRI + 1.0 + rD)
                {
                    rAlpha *= Math.max(rYA - rYRI, rXA - rXRI) / (1.0 + rD);
                }
                if (rYA < Math.min(rYRI, rYCC))
                {
                    rSpec = Math.max(0.0, Math.abs(rXA - rXRC));
                }
                else if (rXA < Math.min(rXRI, rXCC))
                {
                    rSpec = Math.max(0.0, Math.abs(rYA - rYRC));
                }
                else if (rXA >= rXCC && rYA >= rYCC)
                {
                    r = Math.sqrt((rXA - rXCC) * (rXA - rXCC) + (rYA - rYCC) * (rYA - rYCC));
                    rSpec = r + (rWidth2 - rRoundO);
                    rSpec = Math.abs(rSpec);
                }
                else
                {
                    rSpec = rYA - rYRI > rXA - rXRI ? Math.abs(rYA - rYRC) : Math.abs(rXA - rXRC);
                }
                int iSpec = (int)((1.0 - rSpec / rWidth2) * 255.0 * this.specular.val / 100.0);
                Col col = new Col(this.color);
                col.a = (int)(rAlpha * 255.0);
                col.ChangeBrightness(iSpec);
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderRectFill()
    {
        int iRB;
        int iTL;
        double rLZ;
        double rLY = rLZ = Math.sqrt(0.3333333333333333);
        double rLX = rLZ;
        double ROOT2 = Math.sqrt(2.0);
        double rMin = Math.min(this.prefs.width, this.prefs.height);
        double rRound = this.round.val * rMin / 200.0;
        int iEm = (int)Math.abs(this.emboss.val);
        double rEmbossEdge = (100.0 - this.embossdiffuse.val) / 100.0;
        double rCX = (double)this.prefs.width * 0.5;
        double rCY = (double)this.prefs.height * 0.5;
        double rWidth = rCX;
        double rHeight = rCY;
        if (this.aspect.val > 0.0)
        {
            rWidth = rWidth * (100.0 - Math.min(this.aspect.val, 99.0)) / 100.0;
        }
        if (this.aspect.val < 0.0)
        {
            rHeight = rHeight * (100.0 + Math.max(this.aspect.val, -99.0)) / 100.0;
        }
        double rXD = rWidth * (100.0 - this.diffuse.val) / 100.0;
        double rYD = rHeight * (100.0 - this.diffuse.val) / 100.0;
        double rXE = rWidth + 1.0 - rMin * (double)iEm / 200.0 * rEmbossEdge;
        double rXEM = rWidth - rMin * (double)iEm / 200.0;
        double rYE = rHeight + 1.0 - rMin * (double)iEm / 200.0 * rEmbossEdge;
        double rYEM = rHeight - rMin * (double)iEm / 200.0;
        if (this.emboss.val > 0.0)
        {
            iTL = 100;
            iRB = -100;
        }
        else
        {
            iTL = -100;
            iRB = 100;
        }
        double txd = this.texturedepth.val * 0.01;
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rY = -((double)y + 0.5 - rCY);
            double rYN = rY / rCY;
            double rYA = Math.abs(rY);
            double rPY = (double)y - rCY + 0.5;
            for (int x = 0; x < this.prefs.width; ++x)
            {
                double rR2;
                double rYR;
                double rYRM;
                double rXR;
                double rXRM;
                int lumi;
                double rPX = (double)x - rCX + 0.5;
                int alpha = 255;
                if (this.tex != null)
                {
                    int pix = this.tex.Get(rPX, rPY, this.texturezoom.val);
                    lumi = (int)((double)((pix & 0xFF) - 128) * txd);
                    alpha = 255 - (int)((double)(255 - (pix >> 24 & 0xFF)) * txd);
                }
                else
                {
                    lumi = 0;
                }
                double rX = -((double)x + 0.5 - rCX);
                double rXN = rX / rCX;
                double rXA = Math.abs(rX);
                if (rYA > rHeight || rXA > rWidth)
                {
                    alpha = 0;
                }
                if (rXA > rXD)
                {
                    alpha = (int)Math.max(0.0, (double)alpha * (1.0 - (rXA - rXD) / (rWidth - rXD)));
                }
                if (rYA > rYD)
                {
                    alpha = (int)Math.max(0.0, (double)alpha * (1.0 - (rYA - rYD) / (rHeight - rYD)));
                }
                if (rXA > rWidth - rRound && rYA > rHeight - rRound &&
                    (rXRM = (rXR = rXA - rWidth + rRound) + 1.0) * rXRM +
                            (rYRM = (rYR = rYA - rHeight + rRound) + 1.0) * rYRM >=
                        (rR2 = rRound * rRound))
                {
                    if (rXR * rXR + rYR * rYR >= rR2)
                    {
                        alpha = 0;
                    }
                    else
                    {
                        double b = (rXR + rYR) * 2.0;
                        double c = rXR * rXR + rYR * rYR - rR2;
                        double v = (Math.sqrt(b * b - 8.0 * c) - b) * 0.25;
                        alpha = (int)((double)alpha * v);
                    }
                }
                double rRound2 = rRound * Math.min(rXE, rYE) / Math.min(rWidth, rHeight);
                boolean iA = false;
                rXR = 0.0;
                rYR = 0.0;
                int iEmbossMode = 0;
                double rXYR = 0.0;
                if (rXA >= rXEM)
                {
                    rXR = rX / (rXA * ROOT2);
                    rYR = 0.0;
                    iEmbossMode = 1;
                }
                if (rYA >= rYEM)
                {
                    rXR = 0.0;
                    rYR = rY / (rYA * ROOT2);
                    iEmbossMode = 2;
                }
                if (rXA >= rXEM - rRound2 && rYA >= rYEM - rRound2)
                {
                    rXR = rX > 0.0 ? rXA - rXEM + rRound2 : -(rXA - rXEM + rRound2);
                    rYR = rY > 0.0 ? rYA - rYEM + rRound2 : -(rYA - rYEM + rRound2);
                    rXYR = rXR * rXR + rYR * rYR;
                    double r2 = rRound2 * rRound2;
                    if (rXYR >= rRound2 * rRound2)
                    {
                        double r = Math.sqrt(2.0 * rXYR);
                        rXR /= r;
                        rYR /= r;
                        iEmbossMode = 3;
                    }
                }
                Col col = new Col(this.color);
                col.ChangeBrightness((rXN + rYN) * 128.0 * this.specular.val / 100.0 + (double)lumi);
                if (iEmbossMode != 0)
                {
                    double a;
                    double rTZ = 1.0 / ROOT2;
                    double r =
                        this.emboss.val > 0.0 ? rXR * rLX + rYR * rLY + rTZ * rLZ : -rXR * rLX - rYR * rLY + rTZ * rLZ;
                    if (r < 0.0)
                    {
                        r = 0.0;
                    }
                    Col colE = new Col(this.color);
                    colE.ChangeBrightness(r * 255.0 - 128.0);
                    if (iEmbossMode == 1)
                    {
                        a = 255.0 * (rXA - rXEM) / (rXE - rXEM);
                        a = Math.min(255.0, Math.max(0.0, Math.abs(a)));
                        col.BlendTo(colE.r, colE.g, colE.b, (int)a);
                    }
                    else if (iEmbossMode == 2)
                    {
                        a = 255.0 * (rYA - rYEM) / (rYE - rYEM);
                        a = Math.min(255.0, Math.max(0.0, Math.abs(a)));
                        col.BlendTo(colE.r, colE.g, colE.b, (int)a);
                    }
                    else if (iEmbossMode == 3)
                    {
                        a = 255.0 * (Math.sqrt(rXYR) - rRound2) / (rXE - rXEM);
                        a = Math.min(255.0, Math.max(0.0, Math.abs(a)));
                        col.BlendTo(colE.r, colE.g, colE.b, (int)a);
                    }
                }
                col.a = alpha;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderWaveCircle()
    {
        double lumi = 0.0;
        double rStep = 180.0 / this.anglestep.val;
        double rDepth = this.width.val * 0.01;
        double rCX = (double)this.prefs.width * 0.5;
        double rCY = (double)this.prefs.height * 0.5;
        double rAY = 1.0;
        double rAX = 1.0;
        if (this.aspect.val > 0.0)
        {
            rAX = (100.0 - Math.min(this.aspect.val, 99.0)) * 0.01;
        }
        if (this.aspect.val < 0.0)
        {
            rAY = (100.0 + Math.max(this.aspect.val, -99.0)) * 0.01;
        }
        double rMax = Math.sqrt(Math.min(rCX, rCY) * Math.min(rCX, rCY));
        double rD = 1.0 - this.diffuse.val * 0.01;
        double txd = this.texturedepth.val * 0.01;
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rPY = (double)y - rCY + 0.5;
            double rY = rPY / rAY;
            for (int x = 0; x < this.prefs.width; ++x)
            {
                double rPX = (double)x - rCX + 0.5;
                int alpha = 255;
                if (this.tex != null)
                {
                    int pix = this.tex.Get(rPX, rPY, this.texturezoom.val);
                    lumi = (int)((double)((pix & 0xFF) - 128) * txd);
                    alpha = 255 - (int)((double)(255 - (pix >> 24 & 0xFF)) * txd);
                }
                else
                {
                    lumi = 0.0;
                }
                double rX = rPX / rAX;
                double rTh = Math.abs(Math.atan2(rX, rY));
                double rCos = Math.abs(Math.sin(rTh * rStep));
                double rR = Math.sqrt(rX * rX + rY * rY);
                double rMax2 = rMax * (1.0 - rCos * rDepth);
                Col col = new Col(this.color);
                col.ChangeBrightness((int)((-rX / rCX - rY / rCY) * 128.0 * this.specular.val * 0.01 + lumi));
                if (rR < rMax2)
                {
                    double rMax2M = (rMax2 - 1.0) * rD;
                    if (rR >= rMax2M)
                    {
                        alpha = (int)((double)alpha - (rR - rMax2M) * 255.0 / (rMax2 - rMax2M));
                    }
                }
                else
                {
                    alpha = 0;
                }
                col.a = alpha;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderCircle()
    {
        Col col = new Col(0, 0, 0);
        double rCX = (double)this.prefs.width * 0.5;
        double rCY = (double)this.prefs.height * 0.5;
        double rMinSize = Math.min(rCX, rCY) * this.width.val * 0.01;
        double dRW = 1.0 - this.width.val * 0.01;
        double dRW2 = dRW * dRW;
        double dRC = (dRW + 1.0) * 0.5;
        double dRD1 = 1.0 - (1.0 - dRC) * this.diffuse.val * 0.01;
        double dRD12 = dRD1 * dRD1;
        double dRD2 = dRW + (dRC - dRW) * this.diffuse.val * 0.01;
        double rAY = 1.0;
        double rAX = 1.0;
        if (this.aspect.val > 0.0)
        {
            rAX = 100.0 / (100.0 - Math.min(this.aspect.val, 99.0));
        }
        if (this.aspect.val < 0.0)
        {
            rAY = 100.0 / (100.0 + Math.max(this.aspect.val, -99.0));
        }
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rPY = -((double)y + 0.5 - rCY) * rAY;
            double rY = rPY / rCY;
            double rY2 = rY * rY;
            double rYM = rPY / (rCY - rAY);
            double rYM2 = rYM * rYM;
            double rYC = rPY / Math.max(0.1, rCY - rMinSize * 0.5 * rAY);
            double rYC2 = rYC * rYC;
            double rYEM = rPY / Math.max(0.1, rCY - rMinSize * rAY + rAY);
            double rYEM2 = rYEM * rYEM;
            double rYD1 = rPY / Math.max(0.1, rCY - rMinSize * 0.5 * this.diffuse.val / 100.0 * rAY);
            double rYD12 = rYD1 * rYD1;
            double rYD2 = rPY / Math.max(0.1, rCY - rMinSize * rAY + rMinSize * 0.5 * this.diffuse.val / 100.0 * rAY);
            double rYD22 = rYD2 * rYD2;
            double rYE = rPY / Math.max(0.1, rCY - rMinSize * rAY);
            double rYE2 = rYE * rYE;
            for (int x = 0; x < this.prefs.width; ++x)
            {
                double rPX = -((double)x + 0.5 - rCX) * rAX;
                double rX = rPX / rCX;
                double rXM = rPX / (rCX - rAX);
                double rXC = rPX / Math.max(0.1, rCX - rMinSize * 0.5 * rAX);
                double rXEM = rPX / Math.max(0.1, rCX - rMinSize * rAX + rAX);
                double rXE = rPX / Math.max(0.1, rCX - rMinSize * rAX);
                double rXD1 = rPX / Math.max(0.1, rCX - rMinSize * 0.5 * this.diffuse.val * 0.01 * rAX);
                double rXD2 =
                    rPX / Math.max(0.1, rCX - rMinSize * rAX + rMinSize * 0.5 * this.diffuse.val * 0.01 * rAX);
                double r = rX * rX + rY2;
                double rM = rXM * rXM + rYM2;
                double rC = rXC * rXC + rYC2;
                double rEM = rXEM * rXEM + rYEM2;
                double rE = rXE * rXE + rYE2;
                double rD1 = rXD1 * rXD1 + rYD12;
                double rD2 = rXD2 * rXD2 + rYD22;
                col.r = this.color.col.r;
                col.g = this.color.col.g;
                col.b = this.color.col.b;
                double alpha = 255.0;
                if (rE < 1.0 || r > 1.0)
                {
                    alpha = 0.0;
                }
                else
                {
                    if (rE >= 1.0 && rEM <= 1.0)
                    {
                        alpha *= (1.0 - rE) / (rEM - rE);
                    }
                    if (r < 1.0 && rM >= 1.0)
                    {
                        alpha *= (1.0 - r) / (rM - r);
                    }
                    if (rD1 >= 1.0 && r < 1.0)
                    {
                        alpha *= (1.0 - r) / (rD1 - r);
                    }
                    else if (rE >= 1.0 && rD2 < 1.0)
                    {
                        alpha *= (rE - 1.0) / (rE - rD2) * (rE - 1.0) / (rE - rD2);
                    }
                    if (this.specular.val != 0.0)
                    {
                        double v1 = 1.0 / Math.sqrt(r);
                        double v2 = 1.0 / Math.sqrt(rE);
                        double v = 2.0 * (1.0 - v2) / (v1 - v2);
                        if (v > 1.0)
                        {
                            v = 2.0 - v;
                        }
                        col.ChangeBrightness((int)(v *= this.specular.val * 2.55));
                    }
                }
                this.bmp.SetPix(x, y, (int)alpha, col.r, col.g, col.b);
            }
        }
    }

    public void RenderCircleFill(int spectype)
    {
        double rSY;
        double dMetalSpecularWidth;
        double rLZ;
        double cy;
        double cx;
        double ax = cx = (double)this.prefs.width * 0.5;
        double ay = cy = (double)this.prefs.height * 0.5;
        if (this.aspect.val > 0.0)
        {
            ax = (100.0 - Math.min(this.aspect.val, 99.0)) / 100.0 * cx;
        }
        if (this.aspect.val < 0.0)
        {
            ay = (100.0 + Math.max(this.aspect.val, -99.0)) / 100.0 * cy;
        }
        double p = 0.0;
        double rthick = Math.abs(this.emboss.val) / 100.0;
        if (rthick == 1.0)
        {
            rthick = 0.99f;
        }
        double rEmbossEdge = (100.0 - this.embossdiffuse.val) / 100.0;
        double rD = 1.0 - this.diffuse.val / 100.0;
        double rD2 = rD * rD;
        double rMin = Math.min(ax, ay);
        double rLY = rLZ = Math.sqrt(0.3333333333333333);
        double rLX = rLZ;
        double rTZ = 1.0 / Math.sqrt(2.0);
        int iMetalAmbient = (int)(this.ambient.val * 255.0 / 100.0);
        int iMetalSpecular = (int)(this.specular.val * 255.0 / 100.0);
        if (this.specularwidth.val == 0.0)
        {
            iMetalSpecular = 0;
            dMetalSpecularWidth = 0;
        }
        else
        {
            dMetalSpecularWidth = Math.pow(1.0 / (this.specularwidth.val * 0.01), 3.0);
        }
        double rSX = rSY = -this.lightdir.val;
        double rSZ = 50.0;
        double a = Math.sqrt(rSX * rSX + rSY * rSY + rSZ * rSZ);
        rSX /= a;
        rSY /= a;
        rSZ /= a;
        double txd = this.texturedepth.val * 0.01;
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double py = (double)y - cy + 0.5;
            double ry = py / (ay + 0.5);
            double rym = py / (ay - 0.5);
            double rye = py / (ay + 0.5 - rMin * rthick * rEmbossEdge);
            double ryem = py / (ay - rMin * rthick);
            for (int x = 0; x < this.prefs.width; ++x)
            {
                int lumi;
                double px = (double)x - cx + 0.5;
                double rx = px / (ax + 0.5);
                double rxy = rx * rx + ry * ry;
                double rxm = px / (ax - 0.5);
                double rxym = rxm * rxm + rym * rym;
                double rxe = px / (ax + 0.5 - rMin * rthick * rEmbossEdge);
                double rxem = px / (ax - rMin * rthick);
                double rxye = rxe * rxe + rye * rye;
                double rxyem = rxem * rxem + ryem * ryem;
                Col col = new Col(this.color);
                int alpha = 255;
                if (this.tex != null)
                {
                    int pix = this.tex.Get(px, py, this.texturezoom.val);
                    lumi = (int)((double)((pix & 0xFF) - 128) * txd);
                    alpha = 255 - (int)((double)(255 - (pix >> 24 & 0xFF)) * txd);
                }
                else
                {
                    lumi = 0;
                }
                if (rxye < 1.0)
                {
                    switch (spectype)
                    {
                    case 0: {
                        col.ChangeBrightness((int)((-rx - ry) * 128.0 * this.specular.val / 100.0 + (double)lumi));
                        break;
                    }
                    case 1: {
                        double th = Math.atan2(ry, rx);
                        double d = Math.sin(th * 2.0);
                        double d2 = Math.pow((d + 1.0) * 0.5, dMetalSpecularWidth);
                        a = (d + 1.0) * 0.5 * (double)(255 - iMetalAmbient) + (double)iMetalAmbient +
                            d2 * (double)iMetalSpecular + (double)lumi;
                        col.ChangeBrightness((int)(a - 256.0));
                        break;
                    }
                    case 2: {
                        double rZ = Math.sqrt(1.0 - rxy);
                        double d = -rx * rSX - ry * rSY + rZ * rSZ;
                        a = (double)iMetalAmbient + (double)(255 - iMetalAmbient) * d;
                        d = 2.0 * rZ * d - rSZ;
                        d = d <= 0.0 ? 0.0 : Math.exp(Math.log(d) * (110.0 - this.specularwidth.val) / 10.0);
                        a += (double)lumi;
                        double b = (double)iMetalSpecular * d;
                        if (b < 0.0)
                        {
                            b = 0.0;
                        }
                        col.Bright((int)a);
                        col.ChangeBrightness((int)b);
                    }
                    }
                }
                if (rxyem >= 1.0)
                {
                    double rR2 = Math.sqrt(2.0 * rxy);
                    double rTX = -rx / rR2;
                    double rTY = -ry / rR2;
                    Col colE = new Col(this.color);
                    if (this.emboss.val > 0.0)
                    {
                        double r = rTX * rLX + rTY * rLY + rTZ * rLZ;
                        colE.ChangeBrightness((int)(r * 255.0 - 128.0));
                    }
                    else if (this.emboss.val < 0.0)
                    {
                        double r = -rTX * rLX - rTY * rLY + rTZ * rLZ;
                        colE.ChangeBrightness((int)(r * 255.0 - 128.0));
                    }
                    a = Math.min(255.0, Math.max(0.0, 255.0 * Math.abs((1.0 - rxyem) / (rxye - rxyem))));
                    if (rxye < 1.0)
                    {
                        col.BlendTo(colE.r, colE.g, colE.b, (int)a);
                    }
                    else
                    {
                        col.Copy(colE);
                    }
                }
                if (rxy > 1.0)
                {
                    alpha = 0;
                }
                if (rD2 < 1.0 && rxy > rD2)
                {
                    alpha = (int)((double)alpha * (1.0 - (rxy - rD2) / (1.0 - rD2)));
                }
                if (rxym >= 1.0)
                {
                    alpha = (int)((1.0 - rxy) / (rxym - rxy) * (double)alpha);
                }
                col.a = alpha;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderTriangle()
    {
        double rCX = (double)this.prefs.width * 0.5;
        double rCY = (double)this.prefs.height * 0.5;
        double rYLen = (double)this.prefs.height * this.length.val * 0.01;
        double rWidth = (double)this.prefs.width * this.width.val * 0.005;
        double rD = 1.0 - this.diffuse.val * 0.01;
        double txd = this.texturedepth.val * 0.01;
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rPY = (double)y - rCY + 0.5;
            for (int x = 0; x < this.prefs.width; ++x)
            {
                int lumi;
                double rPX = (double)x - rCX + 0.5;
                int alpha = 255;
                if (this.tex != null)
                {
                    int pix = this.tex.Get(rPX, rPY, this.texturezoom.val);
                    lumi = (int)((double)((pix & 0xFF) - 128) * txd);
                    alpha = 255 - (int)((double)(255 - (pix >> 24 & 0xFF)) * txd);
                }
                else
                {
                    lumi = 0;
                }
                Col col = new Col(this.color);
                if ((double)y > rYLen)
                {
                    alpha = 0;
                }
                else
                {
                    double rXLine;
                    double rXLine2;
                    double rX = Math.abs((double)x + 0.5 - rCX);
                    if (!(rX < (rXLine2 = ((rXLine = rWidth * (double)y / rYLen + 1.0) - 1.0) * rD)))
                    {
                        alpha = rX < rXLine ? (int)((1.0 - (rX - rXLine2) / (rXLine - rXLine2)) * (double)alpha) : 0;
                    }
                    int iA = rXLine == 0.0 ? 255 : (int)((255.0 - rX / rXLine * 255.0) * this.specular.val * 0.01);
                    iA += lumi;
                    iA = Math.min(254, Math.max(0, iA));
                    col.ChangeBrightness(iA);
                }
                col.a = alpha;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderLine()
    {
        Col colBase = new Col(this.color);
        Col col = new Col(0, 0, 0);
        double rWidth = (double)this.prefs.width * this.width.val / 400.0;
        double rLenY = (double)this.prefs.height * this.length.val / 100.0 - rWidth;
        if (rLenY < rWidth)
        {
            rLenY = rWidth;
        }
        double rXC = (double)this.prefs.width * 0.5;
        double rD = 1.0 - this.diffuse.val / 100.0;
        double rWidth2 = (rWidth - 1.0) * rD;
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rY = (double)y + 0.5;
            double rYP = rY < rWidth ? rWidth : (rY < rLenY ? rY : rLenY);
            double rDY = rY - rYP;
            for (int x = 0; x < this.prefs.width; ++x)
            {
                int alpha;
                double rX = (double)x + 0.5;
                double rXP = rXC;
                double rDX = rX - rXP;
                double rDistance = Math.sqrt(rDX * rDX + rDY * rDY);
                if (rDistance >= rWidth)
                {
                    col.Copy(colBase);
                    alpha = 0;
                }
                else
                {
                    alpha = 255;
                    int iA = (int)((255.0 - rDistance / rWidth * 255.0) * this.specular.val / 100.0);
                    iA = Math.max(0, Math.min(255, iA));
                    col.Copy(colBase);
                    col.ChangeBrightness(iA);
                    if (rDistance >= rWidth2)
                    {
                        alpha = (int)(255.0 - 255.0 * (rDistance - rWidth2) / (rWidth - rWidth2));
                    }
                }
                col.a = alpha;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    double LinePoint(double x0, double y0, double x1, double y1, double px, double py)
    {
        double dx = x1 - x0;
        double dy = y1 - y0;
        double a = dx * dx + dy * dy;
        if (a == 0.0)
        {
            return Math.sqrt((x0 - px) * (x0 - px) + (y0 - py) * (y0 - py));
        }
        double b = dx * (x0 - px) + dy * (y0 - py);
        double t = -(b / a);
        if (t < 0.0)
        {
            t = 0.0;
        }
        if (t >= 1.0)
        {
            t = 1.0;
        }
        double x = t * dx + x0;
        double y = t * dy + y0;
        return Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
    }

    public void RenderRadiateLine()
    {
        if (this.prefs.width == 0 || this.prefs.height == 0 || this.anglestep.val == 0.0)
        {
            return;
        }
        Vector vC = new Vector((double)this.prefs.width * 0.5, (double)this.prefs.height * 0.5);
        Vector vP = new Vector(0.0, 0.0);
        double rWidth = vC.x * this.width.val / 200.0 + 1.0;
        double rD = 1.0 - this.diffuse.val / 100.0;
        double rWidth2 = (rWidth - 1.0) * rD;
        double rLenY = (double)this.prefs.height * this.length.val / 200.0;
        if (rLenY < rWidth)
        {
            rLenY = rWidth;
        }
        Vector vAspect = new Vector(1.0, 1.0);
        Vector vSize = new Vector(this.prefs.width, this.prefs.height);
        Vector vP1 = new Vector(0.0, -vC.y + rWidth);
        Vector vP2 = new Vector(0.0, -vC.y + rLenY);
        Vector vTC1 = new Vector(0.0, 0.0);
        Vector vTC2 = new Vector(0.0, 0.0);
        Vector vT1 = new Vector(0.0, 0.0);
        Vector vT2 = new Vector(0.0, 0.0);
        if (this.aspect.val > 0.0)
        {
            vAspect.x = (100.0 - Math.min(this.aspect.val, 99.0)) / 100.0;
        }
        if (this.aspect.val < 0.0)
        {
            vAspect.y = (100.0 + Math.max(this.aspect.val, -99.0)) / 100.0;
        }
        vAspect.x *= (double)this.prefs.width / (double)this.prefs.height;
        vTC1.Set(vP1);
        vTC2.Set(vP2);
        vTC1.Mul(vAspect);
        vTC2.Mul(vAspect);
        vTC1.Add(vC);
        vTC2.Add(vC);
        for (int y = 0; y < this.prefs.height; ++y)
        {
            for (int x = 0; x < this.prefs.width; ++x)
            {
                int alpha;
                Vector vv = new Vector(0.0, 0.0);
                vP.x = (double)x + 0.5;
                vP.y = (double)y + 0.5;
                double d = this.LinePoint(vTC1.x, vTC1.y, vTC2.x, vTC2.y, vP.x, vP.y);
                for (double rTh = this.anglestep.val; rTh <= 180.0; rTh += this.anglestep.val)
                {
                    vT1.Set(vP1);
                    vT2.Set(vP2);
                    vT1.Rotate(rTh);
                    vT2.Rotate(rTh);
                    vT1.Mul(vAspect);
                    vT2.Mul(vAspect);
                    vT1.Add(vC);
                    vT2.Add(vC);
                    double d2 = this.LinePoint(vT1.x, vT1.y, vT2.x, vT2.y, vP.x, vP.y);
                    d = Math.min(d, d2);
                    vT1.Set(vP1);
                    vT2.Set(vP2);
                    vT1.Rotate(-rTh);
                    vT2.Rotate(-rTh);
                    vT1.Mul(vAspect);
                    vT2.Mul(vAspect);
                    vT1.Add(vC);
                    vT2.Add(vC);
                    d2 = this.LinePoint(vT1.x, vT1.y, vT2.x, vT2.y, vP.x, vP.y);
                    d = Math.min(d, d2);
                }
                Col col = new Col(this.color);
                if (d < rWidth)
                {
                    col.ChangeBrightness((rWidth - d) / rWidth * 255.0 * this.specular.val / 100.0);
                    alpha = d < rWidth2 ? 255 : (int)(255.0 - (d - rWidth2) / (rWidth - rWidth2) * 255.0);
                }
                else
                {
                    alpha = 0;
                }
                col.a = alpha;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderHLines()
    {
        double rCX = (double)this.prefs.width * 0.5;
        double rCY = (double)this.prefs.height * 0.5;
        double rAY = 1.0;
        double rAX = 1.0;
        if (this.aspect.val > 0.0)
        {
            rAX = 100.0 / (100.0 - Math.min(this.aspect.val, 99.0));
        }
        if (this.aspect.val < 0.0)
        {
            rAY = 100.0 / (100.0 + Math.max(this.aspect.val, -99.0));
        }
        double rLen = rCX / rAX * this.length.val / 100.0;
        double rYArea = rCY / rAY;
        double rWidth = rYArea * this.width.val / 200.0 + 1.0;
        double rWidth2 = (rWidth - 1.0) * (1.0 - this.diffuse.val / 100.0);
        if (this.prefs.width == 0 || this.prefs.height == 0)
        {
            return;
        }
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rY = -((double)y + 0.5 - rCY);
            double rYA = Math.abs(rY);
            for (int x = 0; x < this.prefs.width; ++x)
            {
                double alpha;
                double rX = (double)x + 0.5 - rCX;
                double rXA = Math.abs(rX);
                double rMin = 1.0;
                if (rYA < rYArea)
                {
                    rMin = 100000.0;
                    int yy = 0;
                    while (yy <= 100)
                    {
                        double r;
                        if (rXA < rLen)
                        {
                            r = (double)yy * (rYArea - rWidth) / 100.0;
                            r = Math.abs(r - rYA);
                        }
                        else
                        {
                            double yyy = (double)yy * (rYArea - rWidth) / 100.0;
                            r = Math.sqrt((rXA - rLen) * (rXA - rLen) + (rYA - yyy) * (rYA - yyy));
                        }
                        if (r < rMin)
                        {
                            rMin = r;
                        }
                        if (this.step.val == 0.0)
                            break;
                        yy = (int)((double)yy + this.step.val);
                    }
                    alpha = rMin < rWidth
                                ? (rMin < rWidth2 ? 255.0 : 255.0 - (rMin - rWidth2) / (rWidth - rWidth2) * 255.0)
                                : 0.0;
                }
                else
                {
                    alpha = 0.0;
                }
                Col col = new Col(this.color);
                col.ChangeBrightness((1.0 - rMin / rWidth) * 255.0 * this.specular.val / 100.0);
                col.a = (int)alpha;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderVLines()
    {
        double rCX = (double)this.prefs.width * 0.5;
        double rCY = (double)this.prefs.height * 0.5;
        double rAY = 1.0;
        double rAX = 1.0;
        if (this.aspect.val > 0.0)
        {
            rAX = 100.0 / (100.0 - Math.min(this.aspect.val, 99.0));
        }
        if (this.aspect.val < 0.0)
        {
            rAY = 100.0 / (100.0 + Math.max(this.aspect.val, -99.0));
        }
        double rLen = rCY / rAY * this.length.val / 100.0;
        double rXArea = rCX / rAX;
        double rWidth = rXArea * this.width.val / 200.0 + 1.0;
        double rWidth2 = (rWidth - 1.0) * (1.0 - this.diffuse.val / 100.0);
        if (this.prefs.width == 0 || this.prefs.height == 0)
        {
            return;
        }
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rY = -((double)y + 0.5 - rCY);
            double rYA = Math.abs(rY);
            for (int x = 0; x < this.prefs.width; ++x)
            {
                double alpha;
                double rX = (double)x + 0.5 - rCX;
                double rXA = Math.abs(rX);
                double rMin = 1.0;
                if (rXA < rXArea)
                {
                    rMin = 100000.0;
                    int xx = 0;
                    while (xx <= 100)
                    {
                        double r;
                        if (rYA < rLen)
                        {
                            r = (double)xx * (rXArea - rWidth) / 100.0;
                            r = Math.abs(r - rXA);
                        }
                        else
                        {
                            double xxx = (double)xx * (rXArea - rWidth) / 100.0;
                            r = Math.sqrt((rYA - rLen) * (rYA - rLen) + (rXA - xxx) * (rXA - xxx));
                        }
                        if (r < rMin)
                        {
                            rMin = r;
                        }
                        if (this.step.val == 0.0)
                            break;
                        xx = (int)((double)xx + this.step.val);
                    }
                    alpha = rMin < rWidth
                                ? (rMin < rWidth2 ? 255.0 : 255.0 - (rMin - rWidth2) / (rWidth - rWidth2) * 255.0)
                                : 0.0;
                }
                else
                {
                    alpha = 0.0;
                }
                Col col = new Col(this.color);
                col.ChangeBrightness((1.0 - rMin / rWidth) * 255.0 * this.specular.val / 100.0);
                col.a = (int)alpha;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void RenderText(int frame, int total)
    {
        int sz = Math.min(this.prefs.height, this.prefs.width);
        Graphics g = this.bmp.img.getGraphics();
        Col col = new Col(0, 0, 0, 0);
        for (int y = 0; y < this.prefs.height; ++y)
        {
            for (int x = 0; x < this.prefs.width; ++x)
            {
                this.bmp.SetPix(x, y, col);
            }
        }
        Graphics2D g2 = (Graphics2D)g;
        int id = this.font.val;
        String fontname = GUIEditor.getInstance().fonts[id];
        int size = (int)((double)this.prefs.height * this.fontsize.val * 0.01);
        int style = 0;
        if (this.bold.val != 0)
        {
            style = 1;
        }
        if (this.italic.val != 0)
        {
            style |= 2;
        }
        Font f = new Font(fontname, style, size);
        g2.setFont(f);
        g2.setColor(new Color(this.color.col.r, this.color.col.g, this.color.col.b));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics fm = g.getFontMetrics(f);
        DynamicText dt = new DynamicText(this.text.val);
        String s = dt.Get(frame, total);
        int spw = fm.charWidth(' ');
        int w = fm.stringWidth(s);
        switch (this.textalign.val)
        {
        case 0: {
            g2.drawString(s, (this.prefs.width - w) / 2,
                          (this.prefs.height + fm.getAscent() - fm.getDescent() - fm.getLeading()) / 2);
            break;
        }
        case 1: {
            g2.drawString(s, spw / 2, (this.prefs.height + fm.getAscent() - fm.getDescent() - fm.getLeading()) / 2);
            break;
        }
        case 2: {
            g2.drawString(s, this.prefs.width - w - spw / 2,
                          (this.prefs.height + fm.getAscent() - fm.getDescent() - fm.getLeading()) / 2);
        }
        }
    }

    public void RenderShape()
    {
        Col col = new Col(0, 0, 0, 0);
        for (int y = 0; y < this.prefs.height; ++y)
        {
            for (int x = 0; x < this.prefs.width; ++x)
            {
                this.bmp.SetPix(x, y, col);
            }
        }
        col.Copy(this.color.col);
        Path2D path = ShapeEditor.MakePath(this.shape.val, this.fill.val, 1, -1);
        Graphics2D g2 = (Graphics2D)this.bmp.img.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.blue);
        if (this.fill.val != 0)
        {
            int dw = (int)Math.floor((double)Math.min(this.prefs.width, this.prefs.height) * this.diffuse.val * 0.005);
            if (dw != 0)
            {
                g2.setClip(path);
                g2.fill(path);
                int iColOld = -1;
                for (int i = dw; i > 0; --i)
                {
                    int iCol = i * 255 / (dw + 1);
                    if (iCol == iColOld)
                        continue;
                    g2.setColor(new Color(0, 0, iCol));
                    g2.setStroke(new BasicStroke(i));
                    g2.draw(path);
                    iColOld = iCol;
                }
            }
            else
            {
                g2.fill(path);
            }
        }
        else
        {
            g2.setStroke(new BasicStroke((float)(this.width.val * (double)this.prefs.width * 0.005)));
            g2.draw(path);
        }
        double rCX = (double)this.prefs.width * 0.5;
        double rCY = (double)this.prefs.height * 0.5;
        double txd = this.texturedepth.val * 0.01;
        for (int y = 0; y < this.prefs.height; ++y)
        {
            double rY = -((double)y + 0.5 - rCY);
            double rYN = rY / rCY;
            double rPY = (double)y - rCY + 0.5;
            for (int x = 0; x < this.prefs.width; ++x)
            {
                int lumi;
                double rPX = (double)x - rCX + 0.5;
                double rX = -((double)x + 0.5 - rCX);
                double rXN = rX / rCX;
                int alpha = 255;
                if (this.tex != null)
                {
                    int pix = this.tex.Get(rPX, rPY, this.texturezoom.val);
                    lumi = (int)((double)((pix & 0xFF) - 128) * txd);
                    alpha = 255 - (int)((double)(255 - (pix >> 24 & 0xFF)) * txd);
                }
                else
                {
                    lumi = 0;
                }
                col.r = this.color.col.r;
                col.g = this.color.col.g;
                col.b = this.color.col.b;
                col.ChangeBrightness((rXN + rYN) * 128.0 * this.specular.val * 0.01 + (double)lumi);
                int v = this.bmp.GetPix(x, y);
                col.a = (v & 0xFF) * (v >> 24 & 0xFF) * alpha / 255 / 255;
                this.bmp.SetPix(x, y, col);
            }
        }
    }

    public void Render(int frame, int total)
    {
        switch (this.type.val)
        {
        case 0: {
            this.RenderNone();
            break;
        }
        case 1: {
            this.RenderImage(frame, total);
            break;
        }
        case 2: {
            this.RenderCircle();
            break;
        }
        case 3: {
            this.RenderCircleFill(0);
            break;
        }
        case 4: {
            this.RenderCircleFill(1);
            break;
        }
        case 5: {
            this.RenderWaveCircle();
            break;
        }
        case 6: {
            this.RenderCircleFill(2);
            break;
        }
        case 7: {
            this.RenderRect();
            break;
        }
        case 8: {
            this.RenderRectFill();
            break;
        }
        case 9: {
            this.RenderTriangle();
            break;
        }
        case 10: {
            this.RenderLine();
            break;
        }
        case 11: {
            this.RenderRadiateLine();
            break;
        }
        case 12: {
            this.RenderHLines();
            break;
        }
        case 13: {
            this.RenderVLines();
            break;
        }
        case 14: {
            this.RenderText(frame, total);
            break;
        }
        case 15: {
            this.RenderShape();
        }
        }
    }
}

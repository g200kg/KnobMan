/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.util.Arrays;

public class Eff
{
    public ParamC antialias;
    public ParamC unfold;
    public ParamI animstep;
    public ParamC zoomxysepa;
    public ParamV zoomxf;
    public ParamV zoomxt;
    public ParamV zoomyf;
    public ParamV zoomyt;
    public ParamS zoomxanim;
    public ParamS zoomyanim;
    public ParamV offxf;
    public ParamV offxt;
    public ParamV offyf;
    public ParamV offyt;
    public ParamS offxanim;
    public ParamS offyanim;
    public ParamC keepdir;
    public ParamV centerx;
    public ParamV centery;
    public ParamV anglef;
    public ParamV anglet;
    public ParamS angleanim;
    public ParamV alphaf;
    public ParamV alphat;
    public ParamS alphaanim;
    public ParamV brightf;
    public ParamV brightt;
    public ParamV contrastf;
    public ParamV contrastt;
    public ParamV saturationf;
    public ParamV saturationt;
    public ParamV huef;
    public ParamV huet;
    public ParamS brightanim;
    public ParamS contrastanim;
    public ParamS saturationanim;
    public ParamS hueanim;
    public ParamC mask1ena;
    public ParamC mask2ena;
    public ParamS fmaskena;
    public ParamS mask1type;
    public ParamS mask1graddir;
    public ParamS mask1startanim;
    public ParamS mask1stopanim;
    public ParamS mask2type;
    public ParamS mask2op;
    public ParamS mask2graddir;
    public ParamS mask2startanim;
    public ParamS mask2stopanim;
    public ParamT fmaskbits;
    public ParamV mask1grad;
    public ParamV mask1startf;
    public ParamV mask1startt;
    public ParamV mask1stopf;
    public ParamV mask1stopt;
    public ParamV mask2grad;
    public ParamV mask2startf;
    public ParamV mask2startt;
    public ParamV mask2stopf;
    public ParamV mask2stopt;
    public ParamV fmaskstart;
    public ParamV fmaskstop;
    public ParamV slightdirf;
    public ParamV slightdirt;
    public ParamV sdensityf;
    public ParamV sdensityt;
    public ParamS slightdiranim;
    public ParamS sdensityanim;
    public ParamC dlightdirena;
    public ParamV dlightdirf;
    public ParamV dlightdirt;
    public ParamV doffsetf;
    public ParamV doffsett;
    public ParamV ddensityf;
    public ParamV ddensityt;
    public ParamV ddiffusef;
    public ParamV ddiffuset;
    public ParamV dsgrad;
    public ParamS dlightdiranim;
    public ParamS doffsetanim;
    public ParamS ddensityanim;
    public ParamS ddiffuseanim;
    public ParamS dstype;
    public ParamC ilightdirena;
    public ParamV ilightdirf;
    public ParamV ilightdirt;
    public ParamV ioffsetf;
    public ParamV ioffsett;
    public ParamV idensityf;
    public ParamV idensityt;
    public ParamV idiffusef;
    public ParamV idiffuset;
    public ParamS ilightdiranim;
    public ParamS ioffsetanim;
    public ParamS idensityanim;
    public ParamS idiffuseanim;
    public ParamC elightdirena;
    public ParamV elightdirf;
    public ParamV elightdirt;
    public ParamV eoffsetf;
    public ParamV eoffsett;
    public ParamV edensityf;
    public ParamV edensityt;
    public ParamS elightdiranim;
    public ParamS eoffsetanim;
    public ParamS edensityanim;
    Control ctl;
    Prefs prefs;
    Layer ly;

    public Eff(Control ctl, Layer ly)
    {
        this.ctl = ctl;
        this.prefs = ctl.prefs;
        this.ly = ly;
        this.antialias = new ParamC(1);
        this.unfold = new ParamC(0);
        this.animstep = new ParamI(0);
        this.zoomxysepa = new ParamC(0);
        this.zoomxf = new ParamV(100.0);
        this.zoomxt = new ParamV(100.0);
        this.zoomxanim = new ParamS(0);
        this.zoomyf = new ParamV(100.0);
        this.zoomyt = new ParamV(100.0);
        this.zoomyanim = new ParamS(0);
        this.offxf = new ParamV(0.0);
        this.offxt = new ParamV(0.0);
        this.offxanim = new ParamS(0);
        this.offyf = new ParamV(0.0);
        this.offyt = new ParamV(0.0);
        this.offyanim = new ParamS(0);
        this.keepdir = new ParamC(0);
        this.centerx = new ParamV(0.0);
        this.centery = new ParamV(0.0);
        this.anglef = new ParamV(0.0);
        this.anglet = new ParamV(0.0);
        this.angleanim = new ParamS(0);
        this.alphaf = new ParamV(100.0);
        this.alphat = new ParamV(100.0);
        this.alphaanim = new ParamS(0);
        this.brightf = new ParamV(0.0);
        this.brightt = new ParamV(0.0);
        this.brightanim = new ParamS(0);
        this.contrastf = new ParamV(0.0);
        this.contrastt = new ParamV(0.0);
        this.contrastanim = new ParamS(0);
        this.saturationf = new ParamV(0.0);
        this.saturationt = new ParamV(0.0);
        this.saturationanim = new ParamS(0);
        this.huef = new ParamV(0.0);
        this.huet = new ParamV(0.0);
        this.hueanim = new ParamS(0);
        this.mask1ena = new ParamC(0);
        this.mask1type = new ParamS(0);
        this.mask1grad = new ParamV(0.0);
        this.mask1graddir = new ParamS(0);
        this.mask1startf = new ParamV(-140.0);
        this.mask1startt = new ParamV(-140.0);
        this.mask1startanim = new ParamS(0);
        this.mask1stopf = new ParamV(140.0);
        this.mask1stopt = new ParamV(140.0);
        this.mask1stopanim = new ParamS(0);
        this.mask2ena = new ParamC(0);
        this.mask2op = new ParamS(0);
        this.mask2type = new ParamS(0);
        this.mask2grad = new ParamV(0.0);
        this.mask2graddir = new ParamS(0);
        this.mask2startf = new ParamV(-140.0);
        this.mask2startt = new ParamV(-140.0);
        this.mask2startanim = new ParamS(0);
        this.mask2stopf = new ParamV(140.0);
        this.mask2stopt = new ParamV(140.0);
        this.mask2stopanim = new ParamS(0);
        this.fmaskena = new ParamS(0);
        this.fmaskstart = new ParamV(0.0);
        this.fmaskstop = new ParamV(100.0);
        this.fmaskbits = new ParamT("11111111");
        this.slightdirf = new ParamV(-45.0);
        this.slightdirt = new ParamV(-45.0);
        this.slightdiranim = new ParamS(0);
        this.sdensityf = new ParamV(0.0);
        this.sdensityt = new ParamV(0.0);
        this.sdensityanim = new ParamS(0);
        this.dlightdirena = new ParamC(0);
        this.dlightdirf = new ParamV(-45.0);
        this.dlightdirt = new ParamV(-45.0);
        this.dlightdiranim = new ParamS(0);
        this.doffsetf = new ParamV(5.0);
        this.doffsett = new ParamV(5.0);
        this.doffsetanim = new ParamS(0);
        this.ddensityf = new ParamV(0.0);
        this.ddensityt = new ParamV(0.0);
        this.ddensityanim = new ParamS(0);
        this.ddiffusef = new ParamV(20.0);
        this.ddiffuset = new ParamV(20.0);
        this.ddiffuseanim = new ParamS(0);
        this.dstype = new ParamS(0);
        this.dsgrad = new ParamV(100.0);
        this.ilightdirena = new ParamC(0);
        this.ilightdirf = new ParamV(-45.0);
        this.ilightdirt = new ParamV(-45.0);
        this.ilightdiranim = new ParamS(0);
        this.ioffsetf = new ParamV(5.0);
        this.ioffsett = new ParamV(5.0);
        this.ioffsetanim = new ParamS(0);
        this.idensityf = new ParamV(0.0);
        this.idensityt = new ParamV(0.0);
        this.idensityanim = new ParamS(0);
        this.idiffusef = new ParamV(20.0);
        this.idiffuset = new ParamV(20.0);
        this.idiffuseanim = new ParamS(0);
        this.elightdirena = new ParamC(0);
        this.elightdirf = new ParamV(-45.0);
        this.elightdirt = new ParamV(-45.0);
        this.elightdiranim = new ParamS(0);
        this.eoffsetf = new ParamV(20.0);
        this.eoffsett = new ParamV(20.0);
        this.eoffsetanim = new ParamS(0);
        this.edensityf = new ParamV(0.0);
        this.edensityt = new ParamV(0.0);
        this.edensityanim = new ParamS(0);
    }

    public void CopyFrom(Eff eff)
    {
        this.antialias.val = eff.antialias.val;
        this.unfold.val = eff.unfold.val;
        this.animstep.val = eff.animstep.val;
        this.zoomxysepa.val = eff.zoomxysepa.val;
        this.zoomxf.val = eff.zoomxf.val;
        this.zoomxt.val = eff.zoomxt.val;
        this.zoomxanim.val = eff.zoomxanim.val;
        this.zoomyf.val = eff.zoomyf.val;
        this.zoomyt.val = eff.zoomyt.val;
        this.zoomyanim.val = eff.zoomyanim.val;
        this.offxf.val = eff.offxf.val;
        this.offxt.val = eff.offxt.val;
        this.offxanim.val = eff.offxanim.val;
        this.offyf.val = eff.offyf.val;
        this.offyt.val = eff.offyt.val;
        this.offyanim.val = eff.offyanim.val;
        this.keepdir.val = eff.keepdir.val;
        this.centerx.val = eff.centerx.val;
        this.centery.val = eff.centery.val;
        this.anglef.val = eff.anglef.val;
        this.anglet.val = eff.anglet.val;
        this.angleanim.val = eff.angleanim.val;
        this.alphaf.val = eff.alphaf.val;
        this.alphat.val = eff.alphat.val;
        this.alphaanim.val = eff.alphaanim.val;
        this.brightf.val = eff.brightf.val;
        this.brightt.val = eff.brightt.val;
        this.brightanim.val = eff.brightanim.val;
        this.contrastf.val = eff.contrastf.val;
        this.contrastt.val = eff.contrastt.val;
        this.contrastanim.val = eff.contrastanim.val;
        this.saturationf.val = eff.saturationf.val;
        this.saturationt.val = eff.saturationt.val;
        this.saturationanim.val = eff.saturationanim.val;
        this.huef.val = eff.huef.val;
        this.huet.val = eff.huet.val;
        this.hueanim.val = eff.hueanim.val;
        this.mask1ena.val = eff.mask1ena.val;
        this.mask1type.val = eff.mask1type.val;
        this.mask1grad.val = eff.mask1grad.val;
        this.mask1graddir.val = eff.mask1graddir.val;
        this.mask1startf.val = eff.mask1startf.val;
        this.mask1startt.val = eff.mask1startt.val;
        this.mask1startanim.val = eff.mask1startanim.val;
        this.mask1stopf.val = eff.mask1stopf.val;
        this.mask1stopt.val = eff.mask1stopt.val;
        this.mask1stopanim.val = eff.mask1stopanim.val;
        this.mask2ena.val = eff.mask2ena.val;
        this.mask2op.val = eff.mask2op.val;
        this.mask2type.val = eff.mask2type.val;
        this.mask2grad.val = eff.mask2grad.val;
        this.mask2graddir.val = eff.mask2graddir.val;
        this.mask2startf.val = eff.mask2startf.val;
        this.mask2startt.val = eff.mask2startt.val;
        this.mask2startanim.val = eff.mask2startanim.val;
        this.mask2stopf.val = eff.mask2stopf.val;
        this.mask2stopt.val = eff.mask2stopt.val;
        this.mask2stopanim.val = eff.mask2stopanim.val;
        this.fmaskena.val = eff.fmaskena.val;
        this.fmaskstart.val = eff.fmaskstart.val;
        this.fmaskstop.val = eff.fmaskstop.val;
        this.fmaskbits.val = new String(eff.fmaskbits.val);
        this.slightdirf.val = eff.slightdirf.val;
        this.slightdirt.val = eff.slightdirt.val;
        this.slightdiranim.val = eff.slightdiranim.val;
        this.sdensityf.val = eff.sdensityf.val;
        this.sdensityt.val = eff.sdensityt.val;
        this.sdensityanim.val = eff.sdensityanim.val;
        this.dlightdirena.val = eff.dlightdirena.val;
        this.dlightdirf.val = eff.dlightdirf.val;
        this.dlightdirt.val = eff.dlightdirt.val;
        this.dlightdiranim.val = eff.dlightdiranim.val;
        this.doffsetf.val = eff.doffsetf.val;
        this.doffsett.val = eff.doffsett.val;
        this.doffsetanim.val = eff.doffsetanim.val;
        this.ddensityf.val = eff.ddensityf.val;
        this.ddensityt.val = eff.ddensityt.val;
        this.ddensityanim.val = eff.ddensityanim.val;
        this.ddiffusef.val = eff.ddiffusef.val;
        this.ddiffuset.val = eff.ddiffuset.val;
        this.ddiffuseanim.val = eff.ddiffuseanim.val;
        this.dstype.val = eff.dstype.val;
        this.dsgrad.val = eff.dsgrad.val;
        this.ilightdirena.val = eff.ilightdirena.val;
        this.ilightdirf.val = eff.ilightdirf.val;
        this.ilightdirt.val = eff.ilightdirt.val;
        this.ilightdiranim.val = eff.ilightdiranim.val;
        this.ioffsetf.val = eff.ioffsetf.val;
        this.ioffsett.val = eff.ioffsett.val;
        this.ioffsetanim.val = eff.ioffsetanim.val;
        this.idensityf.val = eff.idensityf.val;
        this.idensityt.val = eff.idensityt.val;
        this.idensityanim.val = eff.idensityanim.val;
        this.idiffusef.val = eff.idiffusef.val;
        this.idiffuset.val = eff.idiffuset.val;
        this.idiffuseanim.val = eff.idiffuseanim.val;
        this.elightdirena.val = eff.elightdirena.val;
        this.elightdirf.val = eff.elightdirf.val;
        this.elightdirt.val = eff.elightdirt.val;
        this.elightdiranim.val = eff.elightdiranim.val;
        this.eoffsetf.val = eff.eoffsetf.val;
        this.eoffsett.val = eff.eoffsett.val;
        this.eoffsetanim.val = eff.eoffsetanim.val;
        this.edensityf.val = eff.edensityf.val;
        this.edensityt.val = eff.edensityt.val;
        this.edensityanim.val = eff.edensityanim.val;
    }

    public void Update()
    {
    }

    public void Diffuse2(Bitmap imgDest, double diff)
    {
    }

    public void Diffuse(Bitmap imgDest, double diff)
    {
        int x;
        int y;
        Bitmap imgTemp = new Bitmap(this.prefs.width, this.prefs.height);
        Col c = new Col(0, 0, 0);
        int wx = (int)((double)(this.prefs.width / 8) * diff / 100.0);
        for (y = 0; y < this.prefs.height; ++y)
        {
            for (x = 0; x < this.prefs.width; ++x)
            {
                c.Reset();
                int xxe = Math.min(this.prefs.width - 1, x + wx);
                for (int xx = Math.max(0, x - wx); xx <= xxe; ++xx)
                {
                    c.AddVal(imgDest, xx, y);
                }
                c.SetAve(imgTemp, x, y);
            }
        }
        for (x = 0; x < this.prefs.width; ++x)
        {
            for (y = 0; y < this.prefs.height; ++y)
            {
                c.Reset();
                int yye = Math.min(this.prefs.height - 1, y + wx);
                for (int yy = Math.max(0, y - wx); yy <= yye; ++yy)
                {
                    c.AddVal(imgTemp, x, yy);
                }
                c.SetAve(imgDest, x, y);
            }
        }
    }

    public void MakeShadow(Bitmap imgDst, Bitmap imgSrc, int inside, int type, double fShadowGradate, double fOffset,
                           double fOffX, double fOffY, int iSD, Col colDef)
    {
        fOffX *= fOffset;
        fOffY *= fOffset;
        int iLoop = type == 0 ? 1 : (int)fOffset;
        if (iLoop == 0)
        {
            iLoop = 1;
        }
        int iSizeX = imgSrc.width;
        int iSizeY = this.prefs.height;
        for (int y = 0; y < iSizeY; ++y)
        {
            for (int x = 0; x < iSizeX; ++x)
            {
                double fValMax = 0.0;
                for (int i = 1; i <= iLoop; ++i)
                {
                    double fYY = (double)y - fOffY * (double)i / (double)iLoop;
                    int yy = (int)fYY;
                    fYY -= (double)yy;
                    double fXX = (double)x - fOffX * (double)i / (double)iLoop;
                    int xx = (int)fXX;
                    fXX -= (double)xx;
                    int iV11 = 0;
                    int iV10 = 0;
                    int iV01 = 0;
                    int iV00 = 0;
                    if (xx >= 0 && xx < iSizeX && yy >= 0 && yy < iSizeY)
                    {
                        iV00 = imgSrc.GetAlpha(xx, yy);
                    }
                    if (++xx >= 0 && xx < iSizeX && yy >= 0 && yy < iSizeY)
                    {
                        iV01 = imgSrc.GetAlpha(xx, yy);
                    }
                    if (--xx >= 0 && xx < iSizeX && ++yy >= 0 && yy < iSizeY)
                    {
                        iV10 = imgSrc.GetAlpha(xx, yy);
                    }
                    if (++xx >= 0 && xx < iSizeX && yy >= 0 && yy < iSizeY)
                    {
                        iV11 = imgSrc.GetAlpha(xx, yy);
                    }
                    double fV0 = (double)iV00 + (double)(iV01 - iV00) * fXX;
                    double fV1 = (double)iV10 + (double)(iV11 - iV10) * fXX;
                    double fVal = fV0 + (fV1 - fV0) * fYY;
                    if (!((fVal =
                               fVal *
                               (fShadowGradate * (double)(iLoop - i + 1) / (double)iLoop + (100.0 - fShadowGradate)) *
                               0.01) > fValMax))
                        continue;
                    fValMax = fVal;
                }
                if (inside != 0)
                {
                    fValMax = 255.0 - fValMax;
                }
                colDef.a = (int)fValMax * iSD / 100;
                imgDst.SetPix(x, y, colDef);
            }
        }
    }

    public double MaskWipe(int iType, double x, double y, int xDest, int yDest, double fStart, double fStop,
                           double fGrad, int iGradDir)
    {
        if (fStart == fStop)
        {
            return 0.0;
        }
        boolean iDir = false;
        if (fStart > fStop)
        {
            double fTemp = fStart;
            fStart = fStop;
            fStop = fTemp;
            iDir = true;
        }
        switch (iType)
        {
        case 0: {
            double rGrad;
            double a;
            if (fStop - fStart >= 360.0)
            {
                return 255.0;
            }
            double xx = x - (double)xDest * 0.5;
            double yy = y - (double)yDest * 0.5;
            double c = Math.sqrt(xx * xx + yy * yy);
            if (c == 0.0)
            {
                return 255.0;
            }
            double rc = 1.0 / c;
            double aStart = fStart * Math.PI / 180.0;
            double aStop = fStop * Math.PI / 180.0;
            double aMin = Math.min(aStart, aStop);
            double aMax = Math.max(aStart, aStop);
            for (a = Math.atan2(xx, -yy) - Math.PI * 2; a < aMin - rc; a += Math.PI * 2)
            {
            }
            while (a > aMax + rc)
            {
                a -= Math.PI * 2;
            }
            double alpha = 255.0;
            if (a >= aMin - rc && a <= aMax + rc)
            {
                if (a < aMin)
                {
                    alpha = alpha * (a - (aMin - rc)) / rc;
                }
                if (a > aMax)
                {
                    alpha = alpha * (aMax + rc - a) / rc;
                }
            }
            else
            {
                return 0.0;
            }
            if (iGradDir != 0)
            {
                rGrad = (a - aMin + rc) / ((aMax - aMin + 2.0 * rc) * fGrad / 100.0);
                double rGrad2 = (a - aMax - rc) / ((aMin - aMax) * fGrad / 100.0);
                rGrad = Math.min(rGrad, rGrad2);
            }
            else
            {
                rGrad = !iDir ? (a - aMin + rc) / ((aMax - aMin + 2.0 * rc) * fGrad / 100.0)
                              : (a - aMax - rc) / ((aMin - aMax) * fGrad / 100.0);
            }
            if (rGrad < 0.0)
            {
                rGrad = 0.0;
            }
            if (rGrad > 1.0)
            {
                rGrad = 1.0;
            }
            return alpha * rGrad;
        }
        case 1: {
            double rGrad;
            double rStart = fStart * (double)Math.min(yDest, xDest) / 200.0;
            double rStop = fStop * (double)Math.min(yDest, xDest) / 200.0;
            double xx = x - (double)(xDest / 2);
            double yy = y - (double)(yDest / 2);
            double c = Math.sqrt(xx * xx + yy * yy);
            double alpha = 255.0;
            if (c < rStart)
            {
                alpha = 0.0;
            }
            if (c < rStart + 1.0)
            {
                alpha = (c - rStart) * alpha;
            }
            if (c > rStop)
            {
                alpha = 0.0;
            }
            if (c > rStop - 1.0)
            {
                alpha = (rStop - c) * alpha;
            }
            if (fGrad == 0.0)
            {
                return alpha;
            }
            if (iGradDir != 0)
            {
                rGrad = (c - rStart) / ((rStop - rStart) * fGrad / 100.0);
                double rGrad2 = (c - rStop) / ((rStart - rStop) * fGrad / 100.0);
                rGrad = Math.min(rGrad, rGrad2);
            }
            else
            {
                rGrad = !iDir ? (c - rStart) / ((rStop - rStart) * fGrad / 100.0)
                              : (c - rStop) / ((rStart - rStop) * fGrad / 100.0);
            }
            if (rGrad < 0.0)
            {
                rGrad = 0.0;
            }
            if (rGrad > 1.0)
            {
                rGrad = 1.0;
            }
            return alpha * rGrad;
        }
        case 2: {
            double rGrad;
            double rStart = (fStart + 100.0) * (double)xDest / 200.0;
            double rStop = (fStop + 100.0) * (double)xDest / 200.0;
            double alpha = 255.0;
            if (x < rStart - 1.0)
            {
                return 0.0;
            }
            if (x < rStart)
            {
                alpha *= x - (rStart - 1.0);
            }
            if (x > rStop + 1.0)
            {
                return 0.0;
            }
            if (x > rStop)
            {
                alpha *= rStop + 1.0 - x;
            }
            if (fGrad == 0.0)
            {
                return alpha;
            }
            if (iGradDir != 0)
            {
                rGrad = (x - rStart) / ((rStop - rStart) * fGrad / 100.0);
                double rGrad2 = (x - rStop) / ((rStart - rStop) * fGrad / 100.0);
                rGrad = Math.min(rGrad, rGrad2);
            }
            else
            {
                rGrad = !iDir ? (x - rStart) / ((rStop - rStart) * fGrad / 100.0)
                              : (x - rStop) / ((rStart - rStop) * fGrad / 100.0);
            }
            if (rGrad < 0.0)
            {
                rGrad = 0.0;
            }
            if (rGrad > 1.0)
            {
                rGrad = 1.0;
            }
            return alpha * rGrad;
        }
        case 3: {
            double rGrad;
            double rStop = (-fStart + 100.0) * (double)yDest / 200.0;
            double rStart = (-fStop + 100.0) * (double)yDest / 200.0;
            double alpha = 255.0;
            if (y < rStart - 1.0)
            {
                return 0.0;
            }
            if (y < rStart)
            {
                alpha *= y - (rStart - 1.0);
            }
            if (y > rStop + 1.0)
            {
                return 0.0;
            }
            if (y > rStop)
            {
                alpha *= rStop + 1.0 - y;
            }
            if (fGrad == 0.0)
            {
                return alpha;
            }
            if (iGradDir != 0)
            {
                rGrad = (y - rStart) / ((rStop - rStart) * fGrad / 100.0);
                double rGrad2 = (y - rStop) / ((rStart - rStop) * fGrad / 100.0);
                rGrad = Math.min(rGrad, rGrad2);
            }
            else
            {
                rGrad = iDir ? (y - rStart) / ((rStop - rStart) * fGrad / 100.0)
                             : (y - rStop) / ((rStart - rStop) * fGrad / 100.0);
            }
            if (rGrad < 0.0)
            {
                rGrad = 0.0;
            }
            if (rGrad > 1.0)
            {
                rGrad = 1.0;
            }
            return alpha * rGrad;
        }
        }
        return 255.0;
    }

    public boolean CheckAnim()
    {
        if (this.zoomxanim.val != 0)
        {
            return true;
        }
        if (this.zoomyanim.val != 0)
        {
            return true;
        }
        if (this.offxanim.val != 0)
        {
            return true;
        }
        if (this.offyanim.val != 0)
        {
            return true;
        }
        if (this.angleanim.val != 0)
        {
            return true;
        }
        if (this.alphaanim.val != 0)
        {
            return true;
        }
        if (this.brightanim.val != 0)
        {
            return true;
        }
        if (this.contrastanim.val != 0)
        {
            return true;
        }
        if (this.saturationanim.val != 0)
        {
            return true;
        }
        if (this.hueanim.val != 0)
        {
            return true;
        }
        if (this.mask1startanim.val != 0)
        {
            return true;
        }
        if (this.mask1stopanim.val != 0)
        {
            return true;
        }
        if (this.mask2startanim.val != 0)
        {
            return true;
        }
        if (this.mask2stopanim.val != 0)
        {
            return true;
        }
        if (this.fmaskena.val != 0)
        {
            return true;
        }
        if (this.slightdiranim.val != 0)
        {
            return true;
        }
        if (this.sdensityanim.val != 0)
        {
            return true;
        }
        if (this.dlightdiranim.val != 0)
        {
            return true;
        }
        if (this.doffsetanim.val != 0)
        {
            return true;
        }
        if (this.ddensityanim.val != 0)
        {
            return true;
        }
        if (this.ddiffuseanim.val != 0)
        {
            return true;
        }
        if (this.ilightdiranim.val != 0)
        {
            return true;
        }
        if (this.ioffsetanim.val != 0)
        {
            return true;
        }
        if (this.idensityanim.val != 0)
        {
            return true;
        }
        if (this.idiffuseanim.val != 0)
        {
            return true;
        }
        if (this.elightdiranim.val != 0)
        {
            return true;
        }
        if (this.eoffsetanim.val != 0)
        {
            return true;
        }
        return this.edensityanim.val != 0;
    }

    public void Hilight(Bitmap img, double sdir, double sden, double edir, double offset, double eden)
    {
        int i;
        int y;
        int x;
        double rLX = -Math.sin(sdir * Math.PI / 180.0);
        double rLY = Math.cos(sdir * Math.PI / 180.0);
        double edirx = -Math.sin(edir * Math.PI / 180.0);
        double ediry = Math.cos(edir * Math.PI / 180.0);
        int[][] iTemp = new int[img.height][img.width];
        int[][] iTemp2 = new int[img.height][img.width];
        double[] table = new double[64];
        for (int y2 = 0; y2 < img.height; ++y2)
        {
            Arrays.fill(iTemp[y2], 0);
            Arrays.fill(iTemp2[y2], 0);
        }
        Arrays.fill(table, 0.0);
        double sigma = offset;
        int xMax = (int)Math.min(32.0, sigma * 3.0 + 1.0);
        double fsum = 0.0;
        for (x = 0; x < xMax; ++x)
        {
            double f;
            table[x] = f = Math.exp((double)(-x * x) / (2.0 * sigma * sigma));
            if (x == 0)
            {
                fsum += f;
                continue;
            }
            fsum += f * 2.0;
        }
        for (y = 0; y < img.height; ++y)
        {
            for (int x2 = 0; x2 < img.width; ++x2)
            {
                int ay1 = 0;
                int ay0 = 0;
                int ax1 = 0;
                int ax0 = 0;
                int ax = img.GetAlpha(x2, y);
                if (x2 >= 1)
                {
                    ax0 = img.GetAlpha(x2 - 1, y);
                }
                if (x2 < img.width - 1)
                {
                    ax1 = img.GetAlpha(x2 + 1, y);
                }
                if (y >= 1)
                {
                    ay0 = img.GetAlpha(x2, y - 1);
                }
                if (y < img.height - 1)
                {
                    ay1 = img.GetAlpha(x2, y + 1);
                }
                iTemp[y][x2] = (int)(((double)(ay1 - ay0) * ediry + (double)(ax1 - ax0) * edirx) * (double)ax);
            }
        }
        for (y = 0; y < img.height; ++y)
        {
            for (int x3 = 0; x3 < img.width; ++x3)
            {
                for (i = -xMax; i <= xMax; ++i)
                {
                    int xx = x3 + i;
                    if (xx < 0 || xx >= img.width)
                        continue;
                    int[] arrn = iTemp2[y];
                    int n = x3;
                    arrn[n] = (int)((double)arrn[n] + table[Math.abs(i)] * (double)iTemp[y][xx]);
                }
            }
        }
        for (y = 0; y < img.height; ++y)
        {
            Arrays.fill(iTemp[y], 0);
        }
        for (x = 0; x < img.width; ++x)
        {
            for (int y3 = 0; y3 < img.height; ++y3)
            {
                for (i = -xMax; i <= xMax; ++i)
                {
                    int yy = y3 + i;
                    if (yy < 0 || yy >= img.height)
                        continue;
                    int[] arrn = iTemp[y3];
                    int n = x;
                    arrn[n] = (int)((double)arrn[n] + table[Math.abs(i)] * (double)iTemp2[yy][x]);
                }
            }
        }
        Col col00 = new Col(255, 0, 0);
        for (int y4 = 0; y4 < img.height; ++y4)
        {
            double rY = ((double)y4 + 0.5) * 2.0 / (double)img.height - 1.0;
            for (int x4 = 0; x4 < img.width; ++x4)
            {
                double rX = ((double)x4 + 0.5) * 2.0 / (double)img.width - 1.0;
                double br = -(rLX * rX + rLY * rY) * sden * 2.55;
                col00.GetPix(img, x4, y4);
                col00.ChangeBrightness(br + (double)iTemp[y4][x4] * eden);
                img.SetPix(x4, y4, col00);
            }
        }
    }

    public void Apply(Bitmap imgSrc, Bitmap imgDest, boolean add, Col coldef, int dx, int dy, int dw, int dh, double r)
    {
        int iSD;
        double rLYD;
        double rLYE;
        double rLXE;
        double zoomx = this.Anim(this.zoomxf.val, this.zoomxt.val, r, this.zoomxanim.val);
        double zoomy = this.Anim(this.zoomyf.val, this.zoomyt.val, r, this.zoomyanim.val);
        if (this.zoomxysepa.val == 0)
        {
            zoomy = zoomx;
        }
        double offx = this.Anim(this.offxf.val, this.offxt.val, r, this.offxanim.val);
        double offy = this.Anim(this.offyf.val, this.offyt.val, r, this.offyanim.val);
        double angle = this.Anim(this.anglef.val, this.anglet.val, r, this.angleanim.val);
        double alpha = this.Anim(this.alphaf.val, this.alphat.val, r, this.alphaanim.val);
        double bright = this.Anim(this.brightf.val, this.brightt.val, r, this.brightanim.val);
        double cont = this.Anim(this.contrastf.val, this.contrastt.val, r, this.contrastanim.val);
        double sat = this.Anim(this.saturationf.val, this.saturationt.val, r, this.saturationanim.val);
        double hue = this.Anim(this.huef.val, this.huet.val, r, this.hueanim.val);
        double mask1start = this.Anim(this.mask1startf.val, this.mask1startt.val, r, this.mask1startanim.val);
        double mask1stop = this.Anim(this.mask1stopf.val, this.mask1stopt.val, r, this.mask1stopanim.val);
        double mask2start = this.Anim(this.mask2startf.val, this.mask2startt.val, r, this.mask2startanim.val);
        double mask2stop = this.Anim(this.mask2stopf.val, this.mask2stopt.val, r, this.mask2stopanim.val);
        double slightdir = this.Anim(this.slightdirf.val, this.slightdirt.val, r, this.slightdiranim.val);
        double sden = this.Anim(this.sdensityf.val, this.sdensityt.val, r, this.sdensityanim.val);
        double dlightdir = this.Anim(this.dlightdirf.val, this.dlightdirt.val, r, this.dlightdiranim.val);
        double doff = this.Anim(this.doffsetf.val, this.doffsett.val, r, this.doffsetanim.val);
        double dden = this.Anim(this.ddensityf.val, this.ddensityt.val, r, this.ddensityanim.val);
        double ddiff = this.Anim(this.ddiffusef.val, this.ddiffuset.val, r, this.ddiffuseanim.val);
        double ilightdir = this.Anim(this.ilightdirf.val, this.ilightdirt.val, r, this.ilightdiranim.val);
        double ioff = this.Anim(this.ioffsetf.val, this.ioffsett.val, r, this.ioffsetanim.val);
        double iden = this.Anim(this.idensityf.val, this.idensityt.val, r, this.idensityanim.val);
        double idiff = this.Anim(this.idiffusef.val, this.idiffuset.val, r, this.idiffuseanim.val);
        double elightdir = this.Anim(this.elightdirf.val, this.elightdirt.val, r, this.elightdiranim.val);
        double eoff = this.Anim(this.eoffsetf.val, this.eoffsett.val, r, this.eoffsetanim.val);
        double eden = this.Anim(this.edensityf.val, this.edensityt.val, r, this.edensityanim.val);
        if (zoomx == 0.0 || zoomy == 0.0)
        {
            return;
        }
        double dZoomX = 1.0 / zoomx;
        double dZoomY = 1.0 / zoomy;
        Bitmap imgTemp = new Bitmap(dw, dh);
        Bitmap imgShadow = new Bitmap(dw, dh);
        Bitmap imgShadow2 = new Bitmap(dw, dh);
        XYMatrix mx = new XYMatrix();
        mx.Translate((imgSrc.width - dw) / 2, (imgSrc.height - dh) / 2);
        mx.RotateAt(angle, (this.centerx.val + 50.0) * 0.01 * (double)imgSrc.width,
                    (50.0 - this.centery.val) * 0.01 * (double)imgSrc.height);
        mx.Translate(-offx * 0.01 * (double)imgSrc.width, offy * 0.01 * (double)imgSrc.height);
        if (this.keepdir.val != 0)
        {
            mx.RotateAt(-angle, (double)imgSrc.width * 0.5, (double)imgSrc.height * 0.5);
        }
        mx.ScaleAt(dZoomX * 100.0, dZoomY * 100.0, (double)imgSrc.width * 0.5, (double)imgSrc.height * 0.5);
        double[] pos = new double[2];
        coldef.a = 0;
        Col col00 = new Col(0, 0, 0);
        Col col01 = new Col(0, 0, 0);
        Col col10 = new Col(0, 0, 0);
        Col col11 = new Col(0, 0, 0);
        int px = 0;
        int py = 0;
        int y = 0;
        int x = 0;
        int w = (int)Math.min((double)(imgSrc.width * 100 / dw) * dZoomX / 2.0,
                              (double)(imgSrc.height * 100 / dh) * dZoomX / 2.0);
        for (y = 0; y < dh; ++y)
        {
            for (x = 0; x < dw; ++x)
            {
                pos[0] = (double)x + 0.5;
                pos[1] = (double)y + 0.5;
                mx.Transform(pos);
                px = (int)Math.floor(pos[0]);
                py = (int)Math.floor(pos[1]);
                if (px < -1 || px > imgSrc.width || py < -1 || py > imgSrc.height)
                {
                    col00.Clear();
                    col00.Conv(bright, cont, sat, hue);
                    imgTemp.SetPix(x, y, col00);
                    continue;
                }
                if (w >= 2)
                {
                    int xStep;
                    int yStep;
                    if (w > 10)
                    {
                        xStep = yStep = (int)Math.sqrt(w);
                    }
                    else
                    {
                        yStep = 1;
                        xStep = 1;
                    }
                    col00.Clear();
                    for (int yy = -w; yy <= w; yy += yStep)
                    {
                        int yyy = py + yy;
                        if (yyy < 0 || yyy >= imgSrc.height)
                            continue;
                        for (int xx = -w; xx <= w; xx += xStep)
                        {
                            int xxx = px + xx;
                            if (xxx < 0 || xxx >= imgSrc.width)
                                continue;
                            col00.AddVal(imgSrc, xxx, yyy);
                        }
                    }
                    col00.SetAve();
                }
                else
                {
                    pos[0] = pos[0] - 0.5;
                    pos[1] = pos[1] - 0.5;
                    px = (int)Math.floor(pos[0]);
                    py = (int)Math.floor(pos[1]);
                    if (px < 0 || py < 0 || px >= imgSrc.width || py >= imgSrc.height)
                    {
                        col00.Copy(coldef);
                    }
                    else
                    {
                        col00.GetPix(imgSrc, px, py);
                    }
                    if (++px < 0 || py < 0 || px >= imgSrc.width || py >= imgSrc.height)
                    {
                        col01.Copy(coldef);
                    }
                    else
                    {
                        col01.GetPix(imgSrc, px, py);
                    }
                    if (--px < 0 || ++py < 0 || px >= imgSrc.width || py >= imgSrc.height)
                    {
                        col10.Copy(coldef);
                    }
                    else
                    {
                        col10.GetPix(imgSrc, px, py);
                    }
                    if (++px < 0 || py < 0 || px >= imgSrc.width || py >= imgSrc.height)
                    {
                        col11.Copy(coldef);
                    }
                    else
                    {
                        col11.GetPix(imgSrc, px, py);
                    }
                    col00.Blend4(col01, col10, col11, pos[0] - (double)(--px), pos[1] - (double)(--py));
                }
                col00.Conv(bright, cont, sat, hue);
                double dX = (double)x + 0.5 - this.centerx.val * (double)dw / 100.0;
                double dY = (double)y + 0.5 + this.centery.val * (double)dh / 100.0;
                if (this.antialias.val == 0)
                {
                    col00.a = col00.a >= 128 ? 255 : 0;
                }
                double rMask1 = 255.0;
                if (this.mask1ena.val != 0)
                {
                    rMask1 = this.MaskWipe(this.mask1type.val, dX, dY, dw, dh, mask1start, mask1stop,
                                           this.mask1grad.val, this.mask1graddir.val);
                }
                if (this.mask2ena.val != 0)
                {
                    double rMask2 = this.MaskWipe(this.mask2type.val, dX, dY, dw, dh, mask2start, mask2stop,
                                                  this.mask2grad.val, this.mask2graddir.val);
                    rMask1 = this.mask2op.val == 0 ? rMask1 * rMask2 / 255.0 : Math.max(rMask1, rMask2);
                }
                col00.a = (int)((double)col00.a * rMask1 / 255.0 * (alpha * 0.01));
                imgTemp.SetPix(x, y, col00);
            }
        }
        int ew = (int)Math.floor((double)Math.min(dw, dh) * (eoff + 1.0) / 200.0);
        double few = (double)Math.min(dw, dh) * (eoff + 1.0) / 400.0;
        double rLX = rLXE = -Math.sin(slightdir * Math.PI / 180.0);
        double rLY = rLYE = Math.cos(slightdir * Math.PI / 180.0);
        if (this.elightdirena.val != 0)
        {
            rLXE = -Math.sin(elightdir * Math.PI / 180.0);
            rLYE = Math.cos(elightdir * Math.PI / 180.0);
        }
        if (few != 0.0)
        {
            this.Hilight(imgTemp, slightdir, sden, elightdir, few, eden / (40000.0 * few));
        }
        if (dden != 0.0)
        {
            Col colDef = new Col(0, 0, 0);
            if (dden < 0.0)
            {
                colDef.b = 255;
                colDef.g = 255;
                colDef.r = 255;
            }
            double rLXD = rLX;
            rLYD = rLY;
            if (this.dlightdirena.val != 0)
            {
                rLXD = -Math.sin(dlightdir * Math.PI / 180.0);
                rLYD = Math.cos(dlightdir * Math.PI / 180.0);
            }
            iSD = (int)Math.abs(dden);
            this.MakeShadow(imgShadow, imgTemp, 0, this.dstype.val, this.dsgrad.val,
                            doff *= (double)Math.min(dw, dh) / 100.0, rLXD, rLYD, iSD, colDef);
            this.Diffuse(imgShadow, ddiff);
        }
        if (iden != 0.0)
        {
            Col colDef = new Col(0, 0, 0);
            if (iden < 0.0)
            {
                colDef.b = 255;
                colDef.g = 255;
                colDef.r = 255;
            }
            double rLXD = rLX;
            rLYD = rLY;
            if (this.ilightdirena.val != 0)
            {
                rLXD = -Math.sin(ilightdir * Math.PI / 180.0);
                rLYD = Math.cos(ilightdir * Math.PI / 180.0);
            }
            iSD = (int)Math.abs(iden);
            this.MakeShadow(imgShadow2, imgTemp, 1, 0, 0.0, ioff *= (double)Math.min(dw, dh) / 100.0, rLXD, rLYD, iSD,
                            colDef);
            this.Diffuse(imgShadow2, idiff);
        }
        if (add)
        {
            for (y = 0; y < dh; ++y)
            {
                if (dden != 0.0)
                {
                    for (x = 0; x < dw; ++x)
                    {
                        col00.GetPix(imgShadow, x, y);
                        col00.AddPix(imgDest, dx + x, dy + y);
                    }
                }
                for (x = 0; x < dw; ++x)
                {
                    col00.GetPix(imgTemp, x, y);
                    col00.AddPix(imgDest, dx + x, dy + y);
                }
                if (iden == 0.0)
                    continue;
                for (x = 0; x < dw; ++x)
                {
                    col00.GetPix(imgShadow2, x, y);
                    col00.MulAddPix(imgDest, imgTemp, dx + x, dy + y, x, y);
                }
            }
        }
        else
        {
            for (y = 0; y < dh; ++y)
            {
                if (dden != 0.0)
                {
                    for (x = 0; x < dw; ++x)
                    {
                        col00.GetPix(imgShadow, x, y);
                        col00.SetPix(imgDest, dx + x, dy + y);
                    }
                    for (x = 0; x < dw; ++x)
                    {
                        col00.GetPix(imgTemp, x, y);
                        col00.AddPix(imgDest, dx + x, dy + y);
                    }
                }
                else
                {
                    for (x = 0; x < dw; ++x)
                    {
                        col00.GetPix(imgTemp, x, y);
                        col00.SetPix(imgDest, dx + x, dy + y);
                    }
                }
                if (iden == 0.0)
                    continue;
                for (x = 0; x < dw; ++x)
                {
                    col00.GetPix(imgShadow2, x, y);
                    col00.MulAddPix(imgDest, imgTemp, dx + x, dy + y, x, y);
                }
            }
        }
    }

    public double Anim(double from, double to, double ratio, int curve)
    {
        switch (curve)
        {
        case 0: {
            return from;
        }
        case 1: {
            return from + (to - from) * ratio;
        }
        }
        double r = from + (to - from) * this.ctl.animcurve[curve - 2].GetVal(ratio);
        return r;
    }
}

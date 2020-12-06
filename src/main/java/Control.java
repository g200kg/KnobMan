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
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;

public class Control
{
    public final String strVer = new String("JKnobMan 1.3.3");
    public String strJVer = "Java Version:" + System.getProperty("java.version");
    static final int iMaxCurve = 8;
    public Prefs prefs;
    public ArrayList<Layer> layers;
    public AnimCurve[] animcurve;
    public Render render;
    public int iMaxLayer;
    public int iCurrentLayer;
    public Journal journal;
    public Bitmap imgRender;
    public File[] fileTextures;
    public String[] strTextures;
    public ImageIcon[] iconTextures;
    public String strCurrentFile;
    public String strOutputFile;
    public String strExportType;
    public String strKnobDir;
    public String strImgDir;
    public TexLoader txloader;
    public int[] pal;
    public int iExporting;
    public int iEdit;
    public int iRenderModeOrg;
    boolean bHasAlpha;
    public ProgressDialog progress;
    public RenderReq renderreq;
    private static Control inst;
    static int Up_Render;
    static int Up_LayerPrev;
    static int Up_Eff;
    static int Up_Prim;
    static int Up_Prefs;
    static int Up_LyPanel;
    static int Up_AllLayer;
    static int Up_PrimType;
    static int UpPrimParam;
    static int UpPrimParamType;
    static int UpEffParam;
    static int UpPrefParam;
    static int UpAll;

    static
    {
        Up_Render = 1;
        Up_LayerPrev = 2;
        Up_Eff = 4;
        Up_Prim = 8;
        Up_Prefs = 16;
        Up_LyPanel = 32;
        Up_AllLayer = 64;
        Up_PrimType = 128;
        UpPrimParam = 15;
        UpPrimParamType = 47;
        UpEffParam = 7;
        UpPrefParam = 63;
        UpAll = 255;
    }

    public Control()
    {
        int i;
        int[] arrn = new int[24];
        arrn[1] = 128;
        arrn[2] = 32768;
        arrn[3] = 32896;
        arrn[4] = 0x800000;
        arrn[5] = 0x800080;
        arrn[6] = 0x808000;
        arrn[7] = 0x808080;
        arrn[8] = 0xC0C0C0;
        arrn[9] = 255;
        arrn[10] = 65280;
        arrn[11] = 65535;
        arrn[12] = 0xFF0000;
        arrn[13] = 0xFF00FF;
        arrn[14] = 0xFFFF00;
        arrn[15] = 0xFFFFFF;
        this.pal = arrn;
        inst = this;
        this.renderreq = new RenderReq();
        this.prefs = new Prefs();
        this.layers = new ArrayList();
        for (i = 0; i < 3; ++i)
        {
            this.layers.add(new Layer(this, true));
        }
        this.iMaxLayer = 3;
        this.iCurrentLayer = 1;
        this.animcurve = new AnimCurve[8];
        for (i = 0; i < 8; ++i)
        {
            this.animcurve[i] = new AnimCurve();
        }
        this.journal = new Journal();
        File texdir = new File(new ResFilename("Texture").GetString());
        this.fileTextures = texdir.listFiles();
        if (this.fileTextures == null)
        {
            this.strTextures = new String[1];
            this.iconTextures = new ImageIcon[1];
        }
        else
        {
            this.strTextures = new String[this.fileTextures.length + 1];
            this.iconTextures = new ImageIcon[this.fileTextures.length + 1];
            for (int i2 = 0; i2 < this.fileTextures.length; ++i2)
            {
                this.strTextures[i2 + 1] = this.fileTextures[i2].getName();
            }
        }
        this.strTextures[0] = "";
        this.imgRender = new Bitmap(this.prefs.width, this.prefs.height * this.prefs.frames);
        this.render = new Render(this);
        this.render.start();
        this.strCurrentFile = "";
        this.iEdit = 0;
        this.iExporting = 0;
        this.progress = null;
        this.txloader = new TexLoader();
    }

    public Layer GetCurrentLayer()
    {
        if (this.iCurrentLayer <= 0 || this.iCurrentLayer - 1 >= this.iMaxLayer)
        {
            return null;
        }
        return this.layers.get(this.iCurrentLayer - 1);
    }

    public void SelLayer(int n)
    {
        if (n > this.iMaxLayer)
        {
            n = this.iMaxLayer;
        }
        this.iCurrentLayer = n;
        if (GUIEditor.getInstance() == null)
        {
            return;
        }
        if (n == 0)
        {
            GUIEditor.getInstance().SetupLayer(null);
        }
        else
        {
            GUIEditor.getInstance().SetupLayer(this.layers.get(this.iCurrentLayer - 1));
        }
        this.Update(Up_LyPanel);
    }

    public void SetSize()
    {
        int striph;
        int stripw;
        this.prefs.width = this.prefs.pwidth.val * (1 << this.prefs.oversampling.val);
        this.prefs.height = this.prefs.pheight.val * (1 << this.prefs.oversampling.val);
        this.prefs.frames = this.prefs.rendermode == 0 ? this.prefs.priFramesPrev.val : this.prefs.priFramesRender.val;
        if (this.prefs.testindex >= this.prefs.priFramesRender.val)
        {
            this.prefs.testindex = this.prefs.priFramesRender.val - 1;
        }
        for (int i = 0; i < this.iMaxLayer; ++i)
        {
            this.layers.get(i).SetSize();
        }
        if (this.prefs.alignhorz.val == 0)
        {
            stripw = this.prefs.width;
            striph = this.prefs.height * this.prefs.frames;
        }
        else
        {
            stripw = this.prefs.width * this.prefs.frames;
            striph = this.prefs.height;
        }
        this.imgRender = new Bitmap(stripw, striph);
        if (this.prefs.rendermode == 2)
        {
            GUIEditor.getInstance().SetSize(this.prefs.width, this.prefs.height);
        }
        else
        {
            GUIEditor.getInstance().SetSize(stripw, striph);
        }
    }

    int ReadAnim(ProfileReader ppr, String anim, String curve)
    {
        if (ppr.ReadInt(anim, 0) == 0)
        {
            return 0;
        }
        return ppr.ReadInt(curve, 1) + 1;
    }

    public void LoadLayer(ProfileReader ppr, Layer ly, int i)
    {
        byte[] tx;
        Layer ly2;
        String sec = "Layer" + (i + 1);
        ppr.SetSection(sec);
        if (ly.pcVisible.val < 0)
        {
            ly.pcVisible.val = ppr.ReadInt("Visible", 1);
        }
        ly.pcSolo.val = ppr.ReadInt("VisibleSolo", 0);
        ly.name = ppr.ReadString("Name", "");
        String s = ppr.ReadString("Primitive", "None");
        if (s.equals("None"))
        {
            ly.prim.type.val = 0;
        }
        else if (s.equals("Image"))
        {
            ly.prim.type.val = 1;
        }
        else if (s.equals("Circle"))
        {
            ly.prim.type.val = 2;
        }
        else if (s.equals("CircleFill"))
        {
            ly.prim.type.val = 3;
        }
        else if (s.equals("MetalCircle"))
        {
            ly.prim.type.val = 4;
        }
        else if (s.equals("WaveCircle"))
        {
            ly.prim.type.val = 5;
        }
        else if (s.equals("Sphere"))
        {
            ly.prim.type.val = 6;
        }
        else if (s.equals("Rect"))
        {
            ly.prim.type.val = 7;
        }
        else if (s.equals("RectFill"))
        {
            ly.prim.type.val = 8;
        }
        else if (s.equals("Triangle"))
        {
            ly.prim.type.val = 9;
        }
        else if (s.equals("Line"))
        {
            ly.prim.type.val = 10;
        }
        else if (s.equals("RadiateLine"))
        {
            ly.prim.type.val = 11;
        }
        else if (s.equals("H-Lines"))
        {
            ly.prim.type.val = 12;
        }
        else if (s.equals("V-Lines"))
        {
            ly.prim.type.val = 13;
        }
        else if (s.equals("Text"))
        {
            ly.prim.type.val = 14;
        }
        else if (s.equals("Shape"))
        {
            ly.prim.type.val = 15;
        }
        ly.prim.color.col.r = ppr.ReadInt("ColR", 255);
        ly.prim.color.col.g = ppr.ReadInt("ColG", 0);
        ly.prim.color.col.b = ppr.ReadInt("ColB", 0);
        ly.prim.color.col.rgbtohls();
        ly.prim.aspect.val = ppr.ReadFloat("PrimAspect", 0.0f);
        ly.prim.round.val = ppr.ReadFloat("PrimRound", 0.0f);
        ly.prim.width.val = ppr.ReadFloat("PrimWidth", 10.0f);
        ly.prim.length.val = ppr.ReadFloat("PrimLength", 50.0f);
        ly.prim.step.val = ppr.ReadFloat("PrimStep", 20.0f);
        ly.prim.anglestep.val = ppr.ReadFloat("PrimAngleStep", 45.0f);
        ly.prim.emboss.val = ppr.ReadFloat("PrimEmboss", 0.0f);
        ly.prim.embossdiffuse.val = ppr.ReadFloat("PrimEmbossDiffuse", 0.0f);
        ly.prim.ambient.val = ppr.ReadFloat("PrimAmbient", 50.0f);
        ly.prim.lightdir.val = ppr.ReadFloat("PrimLightDir", -50.0f);
        ly.prim.specular.val = ppr.ReadFloat("PrimSpecular", 0.0f);
        ly.prim.specularwidth.val = ppr.ReadFloat("PrimSpecWidth", 50.0f);
        ly.prim.texturefile.val = 0;
        ly.prim.texturename = ppr.ReadString("PrimTextureFile", "");
        ly.prim.texturedepth.val = ppr.ReadFloat("PrimTexture", 0.0f);
        ly.prim.texturezoom.val = ppr.ReadFloat("PrimTexZoom", 100.0f);
        ly.prim.diffuse.val = ppr.ReadFloat("PrimDiffuse", 0.0f);
        ly.prim.text.val = ppr.ReadString("PrimText", "");
        String sf = ppr.ReadString("PrimFont", "SansSerif");
        String[] f = GUIEditor.getInstance().fonts;
        for (int fn = 0; fn < f.length; ++fn)
        {
            if (!f[fn].equals(sf))
                continue;
            ly.prim.font.val = fn;
        }
        ly.prim.fontsize.val = ppr.ReadFloat("PrimSize", 50.0f);
        ly.prim.bold.val = ppr.ReadInt("PrimBold", 0);
        ly.prim.italic.val = ppr.ReadInt("PrimItalic", 0);
        ly.prim.textalign.val = ppr.ReadInt("PrimTextAlign", 0);
        ly.prim.transparent.val = ppr.ReadInt("Transparent", 0);
        ly.prim.autofit.val = ppr.ReadInt("AutoFit", 1);
        ly.prim.intellialpha.val = ppr.ReadInt("IntelliAlpha", 0);
        ly.prim.numframe.val = ppr.ReadInt("PrimFrames", 1);
        ly.prim.framealign.val = ppr.ReadInt("PrimAlign", 0);
        ly.prim.file.val = ppr.ReadString("PrimFile", "");
        ly.prim.shape.val = ppr.ReadString("PrimShape", "");
        ly.prim.fill.val = ppr.ReadInt("PrimFill", 1);
        ly.eff.antialias.val = ppr.ReadInt("Antialias", 1);
        ly.eff.unfold.val = ppr.ReadInt("Unfold", 0);
        ly.eff.animstep.val = ppr.ReadInt("AnimStep", 0);
        ly.eff.zoomxysepa.val = ppr.ReadInt("ZoomXYSepa", 0);
        ly.eff.zoomxf.val = ppr.ReadFloat("Zoom1", 100.0f);
        ly.eff.zoomxt.val = ppr.ReadFloat("Zoom2", 100.0f);
        ly.eff.zoomxanim.val = this.ReadAnim(ppr, "AnimateZoom", "ZoomCurve");
        ly.eff.zoomyf.val = ppr.ReadFloat("ZoomY1", 100.0f);
        ly.eff.zoomyt.val = ppr.ReadFloat("ZoomY2", 100.0f);
        ly.eff.zoomyanim.val = this.ReadAnim(ppr, "AnimateZoomY", "ZoomYCurve");
        ly.eff.offxf.val = ppr.ReadFloat("LayerOffsetX1", 0.0f);
        ly.eff.offxt.val = ppr.ReadFloat("LayerOffsetX2", 0.0f);
        ly.eff.offxanim.val = this.ReadAnim(ppr, "AnimateLayerOffsetX", "LayerOffsetXCurve");
        ly.eff.offyf.val = ppr.ReadFloat("LayerOffsetY1", 0.0f);
        ly.eff.offyt.val = ppr.ReadFloat("LayerOffsetY2", 0.0f);
        ly.eff.offyanim.val = this.ReadAnim(ppr, "AnimateLayerOffsetY", "LayerOffsetYCurve");
        ly.eff.keepdir.val = ppr.ReadInt("KeepDir", 0);
        ly.eff.centerx.val = ppr.ReadFloat("RotCenterX1", 0.0f);
        ly.eff.centery.val = ppr.ReadFloat("RotCenterY1", 0.0f);
        ly.eff.anglef.val = ppr.ReadFloat("Angle1", 0.0f);
        ly.eff.anglet.val = ppr.ReadFloat("Angle2", 0.0f);
        ly.eff.angleanim.val = this.ReadAnim(ppr, "AnimateAngle", "AngleCurve");
        ly.eff.alphaf.val = ppr.ReadFloat("Alpha1", 100.0f);
        ly.eff.alphat.val = ppr.ReadFloat("Alpha2", 100.0f);
        ly.eff.alphaanim.val = this.ReadAnim(ppr, "AnimateAlpha", "AlphaCurve");
        ly.eff.brightf.val = ppr.ReadFloat("Brightness1", 0.0f);
        ly.eff.brightt.val = ppr.ReadFloat("Brightness2", 0.0f);
        ly.eff.brightanim.val = this.ReadAnim(ppr, "AnimateBrightness", "BrightnessCurve");
        ly.eff.contrastf.val = ppr.ReadFloat("Contrast1", 0.0f);
        ly.eff.contrastt.val = ppr.ReadFloat("Contrast2", 0.0f);
        ly.eff.contrastanim.val = this.ReadAnim(ppr, "AnimateContrast", "ContrastCurve");
        ly.eff.saturationf.val = ppr.ReadFloat("Saturation1", 0.0f);
        ly.eff.saturationt.val = ppr.ReadFloat("Saturation2", 0.0f);
        ly.eff.saturationanim.val = this.ReadAnim(ppr, "AnimateSaturation", "SaturationCurve");
        ly.eff.huef.val = ppr.ReadFloat("Hue1", 0.0f);
        ly.eff.huet.val = ppr.ReadFloat("Hue2", 0.0f);
        ly.eff.hueanim.val = this.ReadAnim(ppr, "AnimateHue", "HueCurve");
        ly.eff.mask1ena.val = ppr.ReadInt("UseMask", 0);
        ly.eff.mask1type.val = ppr.ReadInt("MaskType", 0);
        ly.eff.mask1grad.val = ppr.ReadFloat("MaskGradation", 0.0f);
        ly.eff.mask1graddir.val = ppr.ReadInt("MaskGradDir", 0);
        ly.eff.mask1startf.val = ppr.ReadFloat("MaskStart1", -140.0f);
        ly.eff.mask1startt.val = ppr.ReadFloat("MaskStart2", -140.0f);
        ly.eff.mask1startanim.val = this.ReadAnim(ppr, "AnimateMaskStart", "MaskStartCurve");
        ly.eff.mask1stopf.val = ppr.ReadFloat("MaskStop1", 140.0f);
        ly.eff.mask1stopt.val = ppr.ReadFloat("MaskStop2", 140.0f);
        ly.eff.mask1stopanim.val = this.ReadAnim(ppr, "AnimateMaskStop", "MaskStopCurve");
        ly.eff.mask2ena.val = ppr.ReadInt("UseMask2", 0);
        ly.eff.mask2op.val = ppr.ReadInt("Mask2Operation", 0);
        ly.eff.mask2type.val = ppr.ReadInt("Mask2Type", 0);
        ly.eff.mask2grad.val = ppr.ReadFloat("Mask2Gradation", 0.0f);
        ly.eff.mask2graddir.val = ppr.ReadInt("Mask2GradDir", 0);
        ly.eff.mask2startf.val = ppr.ReadFloat("Mask2Start1", -140.0f);
        ly.eff.mask2startt.val = ppr.ReadFloat("Mask2Start2", -140.0f);
        ly.eff.mask2startanim.val = this.ReadAnim(ppr, "AnimateMask2Start", "Mask2StartCurve");
        ly.eff.mask2stopf.val = ppr.ReadFloat("Mask2Stop1", 140.0f);
        ly.eff.mask2stopt.val = ppr.ReadFloat("Mask2Stop2", 140.0f);
        ly.eff.mask2stopanim.val = this.ReadAnim(ppr, "AnimateMask2Stop", "Mask2StopCurve");
        ly.eff.fmaskena.val = ppr.ReadInt("UseFMask", 0);
        ly.eff.fmaskstart.val = ppr.ReadFloat("FMaskStart", 0.0f);
        ly.eff.fmaskstop.val = ppr.ReadFloat("FMaskStop", 0.0f);
        ly.eff.fmaskbits.val = ppr.ReadString("FMaskBits", "");
        ly.eff.slightdirf.val = ppr.ReadFloat("LightDir", -45.0f);
        ly.eff.slightdirt.val = ppr.ReadFloat("LightDir2", -45.0f);
        ly.eff.slightdiranim.val = this.ReadAnim(ppr, "AnimateLightDir", "LightDirCurve");
        ly.eff.sdensityf.val = ppr.ReadFloat("Lighting", 0.0f);
        ly.eff.sdensityt.val = ppr.ReadFloat("Lighting2", 0.0f);
        ly.eff.sdensityanim.val = this.ReadAnim(ppr, "AnimateLighting", "LightingCurve");
        ly.eff.dlightdirena.val = ppr.ReadInt("LightDirDEn", 0);
        ly.eff.dlightdirf.val = ppr.ReadFloat("LightDirD", -45.0f);
        ly.eff.dlightdirt.val = ppr.ReadFloat("LightDirD2", -45.0f);
        ly.eff.dlightdiranim.val = this.ReadAnim(ppr, "AnimateLightDirD", "LightDirDCurve");
        ly.eff.doffsetf.val = ppr.ReadFloat("ShadowOffset1", 5.0f);
        ly.eff.doffsett.val = ppr.ReadFloat("ShadowOffset2", 5.0f);
        ly.eff.doffsetanim.val = this.ReadAnim(ppr, "AnimateShadowOffset", "ShadowOffsetCurve");
        ly.eff.ddensityf.val = ppr.ReadFloat("ShadowDensity1", 0.0f);
        ly.eff.ddensityt.val = ppr.ReadFloat("ShadowDensity2", 0.0f);
        ly.eff.ddensityanim.val = this.ReadAnim(ppr, "AnimateShadowDensity", "ShadowDensityCurve");
        ly.eff.ddiffusef.val = ppr.ReadFloat("ShadowDiffuse1", 0.0f);
        ly.eff.ddiffuset.val = ppr.ReadFloat("ShadowDiffuse2", 0.0f);
        ly.eff.ddiffuseanim.val = this.ReadAnim(ppr, "AnimateShadowDiffuse", "ShadowDiffuseCurve");
        ly.eff.dstype.val = ppr.ReadInt("ShadowType", 0);
        ly.eff.dsgrad.val = ppr.ReadFloat("ShadowGradate", 100.0f);
        ly.eff.ilightdirena.val = ppr.ReadInt("LightDirIEn", 0);
        ly.eff.ilightdirf.val = ppr.ReadFloat("LightDirI", -45.0f);
        ly.eff.ilightdirt.val = ppr.ReadFloat("LightDirI2", -45.0f);
        ly.eff.ilightdiranim.val = this.ReadAnim(ppr, "AnimateLightDirI", "LightDirICurve");
        ly.eff.ioffsetf.val = ppr.ReadFloat("IShadowOffset1", 5.0f);
        ly.eff.ioffsett.val = ppr.ReadFloat("IShadowOffset2", 5.0f);
        ly.eff.ioffsetanim.val = this.ReadAnim(ppr, "AnimateIShadowOffset", "IShadowOffsetCurve");
        ly.eff.idensityf.val = ppr.ReadFloat("IShadowDensity1", 0.0f);
        ly.eff.idensityt.val = ppr.ReadFloat("IShadowDensity2", 0.0f);
        ly.eff.idensityanim.val = this.ReadAnim(ppr, "AnimateIShadowDensity", "IShadowDensityCurve");
        ly.eff.idiffusef.val = ppr.ReadFloat("IShadowDiffuse1", 20.0f);
        ly.eff.idiffuset.val = ppr.ReadFloat("IShadowDiffuse2", 20.0f);
        ly.eff.idiffuseanim.val = this.ReadAnim(ppr, "AnimateIShadowDiffuse", "IShadowDiffuseCurve");
        ly.eff.elightdirena.val = ppr.ReadInt("LightDirEEn", 0);
        ly.eff.elightdirf.val = ppr.ReadFloat("LightDirE", -45.0f);
        ly.eff.elightdirt.val = ppr.ReadFloat("LightDirE2", -45.0f);
        ly.eff.elightdiranim.val = this.ReadAnim(ppr, "AnimateLightDirE", "LightDirECurve");
        ly.eff.eoffsetf.val = ppr.ReadFloat("HilightOffset1", 0.0f);
        ly.eff.eoffsett.val = ppr.ReadFloat("HilightOffset2", 0.0f);
        ly.eff.eoffsetanim.val = this.ReadAnim(ppr, "AnimateHilightOffset", "HilightOffsetCurve");
        ly.eff.edensityf.val = ppr.ReadFloat("HilightDensity1", 0.0f);
        ly.eff.edensityt.val = ppr.ReadFloat("HilightDensity2", 0.0f);
        ly.eff.edensityanim.val = this.ReadAnim(ppr, "AnimateHilightDensity", "HilightDensityCurve");
        ly.prim.tex0 = null;
        ly.prim.tex = null;
        for (int ii = 0; ii < i; ++ii)
        {
            ly2 = this.layers.get(ii);
            if (ly.prim.texturename.length() <= 0 || !ly2.prim.texturename.equals(ly.prim.texturename))
                continue;
            ly.prim.tex = ly.prim.tex0 = ly2.prim.tex;
            if (ly.prim.tex0 == null)
                break;
            this.iconTextures[0] = ly.prim.tex0.GetImageIcon();
            break;
        }
        if (ly.prim.tex == null && (tx = ppr.ExtractFile("TexBmp")).length > 0)
        {
            ly.prim.tex = ly.prim.tex0 = new Tex(new Bitmap(tx), ly.prim.texturename);
            this.iconTextures[0] = ly.prim.tex0.GetImageIcon();
        }
        for (int ii = 0; ii < i; ++ii)
        {
            ly2 = this.layers.get(ii);
            if (ly.prim.file.val.length() <= 0 || !ly2.prim.file.val.equals(ly.prim.file.val))
                continue;
            ly.prim.bmpImage = ly2.prim.bmpImage;
            break;
        }
        byte[] im = ppr.ExtractFile("ImgBmp");
        if (im.length > 0)
        {
            ly.prim.bmpImage = new Bitmap(im);
        }
        ly.prim.Update();
    }

    public void LoadExec(String str)
    {
        int i;
        GUIEditor.getInstance().SetStatus("Loading...");
        GUIEditor.getInstance().getRootPane().setCursor(new Cursor(3));
        this.render.Stop();
        this.iEdit = 0;
        this.strCurrentFile = str;
        this.strKnobDir = Pathname.GetDir(this.strCurrentFile);
        ProfileReader ppr = new ProfileReader(str);
        ppr.SetSection("Prefs");
        this.prefs.rendermode = 0;
        this.prefs.pwidth.Update(ppr.ReadInt("OutputSizeX", 32));
        this.prefs.pheight.Update(ppr.ReadInt("OutputSizeY", 32));
        this.prefs.xylinkset();
        this.prefs.oversampling.Update(ppr.ReadInt("OverSampling", 0));
        this.prefs.alignhorz.Update(ppr.ReadInt("AlignHorizontal", 0));
        this.prefs.priFramesPrev.val = this.prefs.priFramesRender.val = ppr.ReadInt("NumOfImage", 0);
        if (this.prefs.priFramesPrev.val <= 0)
        {
            this.prefs.priFramesRender.val = ppr.ReadInt("RenderFrames", 31);
            this.prefs.priFramesPrev.val = ppr.ReadInt("PreviewFrames", 5);
        }
        this.layers.clear();
        this.iMaxLayer = 0;
        this.iMaxLayer = ppr.ReadInt("Layers", 1);
        for (i = 0; i < this.iMaxLayer; ++i)
        {
            this.layers.add(new Layer(this, true));
        }
        this.SetSize();
        this.prefs.bkcolor.col.SetRgb(ppr.ReadInt("BkColorR", 255), ppr.ReadInt("BkColorG", 255),
                                      ppr.ReadInt("BkColorB", 255));
        for (i = 0; i < 8; ++i)
        {
            int ii = i + 1;
            this.animcurve[i].lv[0] = ppr.ReadInt("Curve" + ii + "L0", 0);
            for (int j = 1; j < 11; ++j)
            {
                String n = (new String[] {"0", "1", "2", "3", "4", "a", "b", "c", "d", "e", "f", "5"})[j];
                this.animcurve[i].tm[j] = ppr.ReadInt("Curve" + ii + "T" + n, -1);
                this.animcurve[i].lv[j] = ppr.ReadInt("Curve" + ii + "L" + n, -1);
            }
            this.animcurve[i].lv[11] = ppr.ReadInt("Curve" + ii + "L5", 100);
            this.animcurve[i].tm[0] = 0;
            this.animcurve[i].tm[11] = 100;
            this.animcurve[i].stepreso.val = ppr.ReadInt("Curve" + ii + "StepReso", 0);
        }
        for (i = 0; i < this.layers.size(); ++i)
        {
            this.layers.get((int)i).pcVisible.val = ppr.ReadInt("Visible1_" + i, -1);
        }
        ppr.SetSection("Pal");
        for (i = 0; i < 24; ++i)
        {
            int c = ppr.ReadInt("Pal" + i, -1);
            if (c < 0)
                continue;
            this.pal[i] = c;
        }
        for (i = 0; i < this.layers.size(); ++i)
        {
            GUIEditor.getInstance().SetStatus("Loading...(" + i + "/" + this.layers.size() + ")");
            this.LoadLayer(ppr, this.layers.get(i), i);
        }
        this.SelLayer(0);
        GUIEditor.getInstance().SetStatus("Loading...(Update Prefs)");
        this.Update(Up_Prefs);
        GUIEditor.getInstance().SetStatus("Loading...(Update Others)");
        this.Update(~Up_Prefs);
        this.SelLayer(1);
        GUIEditor.getInstance().Reset();
        GUIEditor.getInstance().getRootPane().setCursor(new Cursor(0));
        GUIEditor.getInstance().SetStatus("Load Done [" + this.strCurrentFile + "]");
    }

    public byte[] Thumbnail()
    {
        int w = this.prefs.width >> this.prefs.oversampling.val;
        int h = this.prefs.height >> this.prefs.oversampling.val;
        Bitmap bmp = new Bitmap(w, h);
        this.imgRender.DecimationTo(bmp, 0, 0, w, h, 0, 0, this.prefs.width, this.prefs.height);
        return bmp.GetBytes("PNG");
    }

    public void WriteAnimParam(ProfileWriter ppw, String s1, String s2, String s3, String s4, double paramFrom,
                               double paramTo, int paramAnim)
    {
        ppw.WriteFloat(s1, paramFrom);
        ppw.WriteFloat(s2, paramTo);
        ppw.WriteInt(s3, paramAnim != 0 ? 1 : 0);
        ppw.WriteInt(s4, Math.max(0, paramAnim - 1));
    }

    private void SaveLayer(ProfileWriter ppw, Layer ly, int i)
    {
        Layer ly2;
        ppw.Section("Layer" + (i + 1));
        ppw.WriteStr("Name", ly.name);
        ppw.WriteInt("ColR", ly.prim.color.col.r);
        ppw.WriteInt("ColG", ly.prim.color.col.g);
        ppw.WriteInt("ColB", ly.prim.color.col.b);
        ppw.WriteStr("Primitive", (new String[] {"None", "Image", "Circle", "CircleFill", "MetalCircle", "WaveCircle",
                                                 "Sphere", "Rect", "RectFill", "Triangle", "Line", "RadiateLine",
                                                 "H-Lines", "V-Lines", "Text", "Shape"})[ly.prim.type.val]);
        ppw.WriteInt("PrimFill", ly.prim.fill.val);
        ppw.WriteFloat("PrimAspect", ly.prim.aspect.val);
        ppw.WriteFloat("PrimWidth", ly.prim.width.val);
        ppw.WriteFloat("PrimRound", ly.prim.round.val);
        ppw.WriteFloat("PrimLength", ly.prim.length.val);
        ppw.WriteFloat("PrimStep", ly.prim.step.val);
        ppw.WriteFloat("PrimAngleStep", ly.prim.anglestep.val);
        ppw.WriteFloat("PrimEmboss", ly.prim.emboss.val);
        ppw.WriteFloat("PrimEmbossDiffuse", ly.prim.embossdiffuse.val);
        if (ly.prim.tex != null)
        {
            File f = new File(ly.prim.tex.name);
            String nam = f.getName();
            int e = nam.lastIndexOf(46);
            if (e >= 0)
            {
                nam = nam.substring(0, e);
            }
            ppw.WriteStr("PrimTextureFile", nam);
        }
        ppw.WriteFloat("PrimTexture", ly.prim.texturedepth.val);
        ppw.WriteFloat("PrimTexZoom", ly.prim.texturezoom.val);
        ppw.WriteFloat("PrimDiffuse", ly.prim.diffuse.val);
        ppw.WriteFloat("PrimAmbient", ly.prim.ambient.val);
        ppw.WriteFloat("PrimSpecWidth", ly.prim.specularwidth.val);
        ppw.WriteFloat("PrimSpecular", ly.prim.specular.val);
        ppw.WriteFloat("PrimLightDir", ly.prim.lightdir.val);
        ppw.WriteFloat("PrimSize", ly.prim.fontsize.val);
        ppw.WriteInt("PrimBold", ly.prim.bold.val);
        ppw.WriteInt("PrimItalic", ly.prim.italic.val);
        ppw.WriteInt("PrimTextAlign", ly.prim.textalign.val);
        ppw.WriteStr("PrimText", ly.prim.text.val);
        ppw.WriteStr("PrimFont", GUIEditor.getInstance().fonts[ly.prim.font.val]);
        ppw.WriteStr("PrimFile", ly.prim.file.val);
        ppw.WriteStr("PrimShape", ly.prim.shape.val);
        ppw.WriteInt("Transparent", ly.prim.transparent.val);
        ppw.WriteInt("AutoFit", ly.prim.autofit.val);
        ppw.WriteInt("IntelliAlpha", ly.prim.intellialpha.val);
        ppw.WriteInt("PrimFrames", ly.prim.numframe.val);
        ppw.WriteInt("PrimAlign", ly.prim.framealign.val);
        ppw.WriteInt("Antialias", ly.eff.antialias.val);
        ppw.WriteInt("Unfold", ly.eff.unfold.val);
        ppw.WriteInt("KeepDir", ly.eff.keepdir.val);
        ppw.WriteInt("AnimStep", ly.eff.animstep.val);
        this.WriteAnimParam(ppw, "Angle1", "Angle2", "AnimateAngle", "AngleCurve", ly.eff.anglef.val, ly.eff.anglet.val,
                            ly.eff.angleanim.val);
        ppw.WriteInt("ZoomXYSepa", ly.eff.zoomxysepa.val);
        this.WriteAnimParam(ppw, "Zoom1", "Zoom2", "AnimateZoom", "ZoomCurve", ly.eff.zoomxf.val, ly.eff.zoomxt.val,
                            ly.eff.zoomxanim.val);
        this.WriteAnimParam(ppw, "ZoomY1", "ZoomY2", "AnimateZoomY", "ZoomYCurve", ly.eff.zoomyf.val, ly.eff.zoomyt.val,
                            ly.eff.zoomyanim.val);
        ppw.WriteFloat("RotCenterX1", ly.eff.centerx.val);
        ppw.WriteFloat("RotCenterY1", ly.eff.centery.val);
        this.WriteAnimParam(ppw, "LayerOffsetX1", "LayerOffsetX2", "AnimateLayerOffsetX", "LayerOffsetXCurve",
                            ly.eff.offxf.val, ly.eff.offxt.val, ly.eff.offxanim.val);
        this.WriteAnimParam(ppw, "LayerOffsetY1", "LayerOffsetY2", "AnimateLayerOffsetY", "LayerOffsetYCurve",
                            ly.eff.offyf.val, ly.eff.offyt.val, ly.eff.offyanim.val);
        this.WriteAnimParam(ppw, "Alpha1", "Alpha2", "AnimateAlpha", "AlphaCurve", ly.eff.alphaf.val, ly.eff.alphat.val,
                            ly.eff.alphaanim.val);
        this.WriteAnimParam(ppw, "Brightness1", "Brightness2", "AnimateBrightness", "BrightnessCurve",
                            ly.eff.brightf.val, ly.eff.brightt.val, ly.eff.brightanim.val);
        this.WriteAnimParam(ppw, "Contrast1", "Contrast2", "AnimateContrast", "ContrastCurve", ly.eff.contrastf.val,
                            ly.eff.contrastt.val, ly.eff.contrastanim.val);
        this.WriteAnimParam(ppw, "Saturation1", "Saturation2", "AnimateSaturation", "SaturationCurve",
                            ly.eff.saturationf.val, ly.eff.saturationt.val, ly.eff.saturationanim.val);
        this.WriteAnimParam(ppw, "Hue1", "Hue2", "AnimateHue", "HueCurve", ly.eff.huef.val, ly.eff.huet.val,
                            ly.eff.hueanim.val);
        ppw.WriteInt("UseMask", ly.eff.mask1ena.val);
        ppw.WriteInt("MaskType", ly.eff.mask1type.val);
        ppw.WriteFloat("MaskGradation", ly.eff.mask1grad.val);
        ppw.WriteInt("MaskGradDir", ly.eff.mask1graddir.val);
        this.WriteAnimParam(ppw, "MaskStart1", "MaskStart2", "AnimateMaskStart", "MaskStartCurve",
                            ly.eff.mask1startf.val, ly.eff.mask1startt.val, ly.eff.mask1startanim.val);
        this.WriteAnimParam(ppw, "MaskStop1", "MaskStop2", "AnimateMaskStop", "MaskStopCurve", ly.eff.mask1stopf.val,
                            ly.eff.mask1stopt.val, ly.eff.mask1stopanim.val);
        ppw.WriteInt("UseMask2", ly.eff.mask2ena.val);
        ppw.WriteInt("Mask2Operation", ly.eff.mask2op.val);
        ppw.WriteInt("Mask2Type", ly.eff.mask2type.val);
        ppw.WriteFloat("Mask2Gradation", ly.eff.mask2grad.val);
        ppw.WriteInt("Mask2GradDir", ly.eff.mask2graddir.val);
        this.WriteAnimParam(ppw, "Mask2Start1", "Mask2Start2", "AnimateMask2Start", "Mask2StartCurve",
                            ly.eff.mask2startf.val, ly.eff.mask2startt.val, ly.eff.mask2startanim.val);
        this.WriteAnimParam(ppw, "Mask2Stop1", "Mask2Stop2", "AnimateMask2Stop", "Mask2StopCurve",
                            ly.eff.mask2stopf.val, ly.eff.mask2stopt.val, ly.eff.mask2stopanim.val);
        ppw.WriteInt("UseFMask", ly.eff.fmaskena.val);
        ppw.WriteFloat("FMaskStart", ly.eff.fmaskstart.val);
        ppw.WriteFloat("FMaskStop", ly.eff.fmaskstop.val);
        ppw.WriteStr("FMaskBits", ly.eff.fmaskbits.val);
        this.WriteAnimParam(ppw, "LightDir", "LightDir2", "AnimateLightDir", "LightDirCurve", ly.eff.slightdirf.val,
                            ly.eff.slightdirt.val, ly.eff.slightdiranim.val);
        this.WriteAnimParam(ppw, "Lighting", "Lighting2", "AnimateLighting", "LightingCurve", ly.eff.sdensityf.val,
                            ly.eff.sdensityt.val, ly.eff.sdensityanim.val);
        ppw.WriteInt("LightDirDEn", ly.eff.dlightdirena.val);
        this.WriteAnimParam(ppw, "LightDirD", "LightDirD2", "AnimateLightDirD", "LightDirDCurve", ly.eff.dlightdirf.val,
                            ly.eff.dlightdirt.val, ly.eff.dlightdiranim.val);
        this.WriteAnimParam(ppw, "ShadowOffset1", "ShadowOffset2", "AnimateShadowOffset", "ShadowOffsetCurve",
                            ly.eff.doffsetf.val, ly.eff.doffsett.val, ly.eff.doffsetanim.val);
        this.WriteAnimParam(ppw, "ShadowDensity1", "ShadowDensity2", "AnimateShadowDensity", "ShadowDensityCurve",
                            ly.eff.ddensityf.val, ly.eff.ddensityt.val, ly.eff.ddensityanim.val);
        this.WriteAnimParam(ppw, "ShadowDiffuse1", "ShadowDiffuse2", "AnimateShadowDiffuse", "ShadowDiffuseCurve",
                            ly.eff.ddiffusef.val, ly.eff.ddiffuset.val, ly.eff.ddiffuseanim.val);
        ppw.WriteInt("ShadowType", ly.eff.dstype.val);
        ppw.WriteFloat("ShadowGradate", ly.eff.dsgrad.val);
        ppw.WriteInt("LightDirIEn", ly.eff.ilightdirena.val);
        this.WriteAnimParam(ppw, "LightDirI", "LightDirI2", "AnimateLightDirI", "LightDirICurve", ly.eff.ilightdirf.val,
                            ly.eff.ilightdirt.val, ly.eff.ilightdiranim.val);
        this.WriteAnimParam(ppw, "IShadowOffset1", "IShadowOffset2", "AnimateIShadowOffset", "IShadowOffsetCurve",
                            ly.eff.ioffsetf.val, ly.eff.ioffsett.val, ly.eff.ioffsetanim.val);
        this.WriteAnimParam(ppw, "IShadowDensity1", "IShadowDensity2", "AnimateIShadowDensity", "IShadowDensityCurve",
                            ly.eff.idensityf.val, ly.eff.idensityt.val, ly.eff.idensityanim.val);
        this.WriteAnimParam(ppw, "IShadowDiffuse1", "IShadowDiffuse2", "AnimateIShadowDiffuse", "IShadowDiffuseCurve",
                            ly.eff.idiffusef.val, ly.eff.idiffuset.val, ly.eff.idiffuseanim.val);
        ppw.WriteInt("LightDirEEn", ly.eff.elightdirena.val);
        this.WriteAnimParam(ppw, "LightDirE", "LightDirE2", "AnimateLightDirE", "LightDirECurve", ly.eff.elightdirf.val,
                            ly.eff.elightdirt.val, ly.eff.elightdiranim.val);
        this.WriteAnimParam(ppw, "HilightOffset1", "HilightOffset2", "AnimateHilightOffset", "HilightOffsetCurve",
                            ly.eff.eoffsetf.val, ly.eff.eoffsett.val, ly.eff.eoffsetanim.val);
        this.WriteAnimParam(ppw, "HilightDensity1", "HilightDensity2", "AnimateHilightDensity", "HilightDensityCurve",
                            ly.eff.edensityf.val, ly.eff.edensityt.val, ly.eff.edensityanim.val);
        if (ly.prim.texturedepth.val != 0.0)
        {
            int ii;
            for (ii = 0; ii < i; ++ii)
            {
                ly2 = this.layers.get(ii);
                if (ly2.prim.texturedepth.val != 0.0 && ly2.prim.texturename.equals(ly.prim.texturename))
                    break;
            }
            if (ii == i && ly.prim.tex != null && ly.prim.tex.bmp != null)
            {
                ppw.EmbedImage("TexBmp", ly.prim.tex.bmp);
            }
        }
        if (ly.prim.type.val == 1)
        {
            int ii;
            for (ii = 0; ii < i; ++ii)
            {
                ly2 = this.layers.get(ii);
                if (ly2.prim.type.val == 1 && ly2.prim.file.val.equals(ly.prim.file.val))
                    break;
            }
            if (i == ii && ly.prim.bmpImage != null)
            {
                ppw.EmbedImage("ImgBmp", ly.prim.bmpImage);
            }
        }
    }

    public void SaveExec(String name)
    {
        int i;
        GUIEditor.getInstance().SetStatus("Saving...");
        byte[] thumb = this.Thumbnail();
        ProfileWriter ppw = new ProfileWriter(name, "utf-8");
        ppw.WriteHeader(thumb);
        ppw.Section("Prefs");
        ppw.WriteStr("Version", "1490");
        ppw.WriteInt("Layers", this.layers.size());
        ppw.WriteInt("CurrentLayer", 1);
        ppw.WriteInt("Styles", 1);
        ppw.WriteInt("CurrentStyle", 0);
        ppw.WriteInt("OutputSizeX", this.prefs.pwidth.val);
        ppw.WriteInt("OutputSizeX1", this.prefs.pwidth.val);
        ppw.WriteInt("OutputSizeY", this.prefs.pheight.val);
        ppw.WriteInt("OutputSizeY1", this.prefs.pheight.val);
        ppw.WriteInt("OverSampling", this.prefs.oversampling.val);
        ppw.WriteInt("RenderFrames", this.prefs.priFramesRender.val);
        ppw.WriteInt("PreviewFrames", this.prefs.priFramesPrev.val);
        ppw.WriteInt("AlignHorizontal", this.prefs.alignhorz.val);
        ppw.WriteInt("BkColorR", this.prefs.bkcolor.col.r);
        ppw.WriteInt("BkColorG", this.prefs.bkcolor.col.g);
        ppw.WriteInt("BkColorB", this.prefs.bkcolor.col.b);
        for (i = 0; i < this.layers.size(); ++i)
        {
            ppw.WriteInt("Visible1_" + i, this.layers.get((int)i).pcVisible.val);
        }
        for (i = 1; i <= 8; ++i)
        {
            ppw.WriteInt("Curve" + i + "L0", this.animcurve[i - 1].lv[0]);
            for (int j = 1; j < 11; ++j)
            {
                String n = (new String[] {"0", "1", "2", "3", "4", "a", "b", "c", "d", "e", "f", "5"})[j];
                ppw.WriteInt("Curve" + i + "T" + n, this.animcurve[i - 1].tm[j]);
                ppw.WriteInt("Curve" + i + "L" + n, this.animcurve[i - 1].lv[j]);
            }
            ppw.WriteInt("Curve" + i + "L5", this.animcurve[i - 1].lv[11]);
            ppw.WriteInt("Curve" + i + "StepReso", this.animcurve[i - 1].stepreso.val);
        }
        for (i = 0; i < this.layers.size(); ++i)
        {
            GUIEditor.getInstance().SetStatus("Saving...(" + i + "/" + this.layers.size() + ")");
            this.SaveLayer(ppw, this.layers.get(i), i);
        }
        ppw.Close();
        GUIEditor.getInstance().SetStatus("Save Done [" + this.strCurrentFile + "]");
    }

    public void Edit()
    {
        GUIEditor.getInstance().SetStatus(" ");
        if (this.iEdit == 0)
        {
            this.iEdit = 1;
            GUIEditor.getInstance().SetTitle();
        }
    }

    public void New()
    {
        int i;
        if (this.iEdit != 0 &&
            Dlg.Confirm("[" + this.strCurrentFile + "]\n" + GUIEditor.getInstance().GetMsgStr("notsaved")) == 2)
        {
            return;
        }
        this.journal.Write();
        this.prefs.Init();
        this.iMaxLayer = 3;
        this.iCurrentLayer = 1;
        this.layers.clear();
        for (i = 0; i < 3; ++i)
        {
            this.layers.add(new Layer(this, true));
        }
        for (i = 0; i < 8; ++i)
        {
            this.animcurve[i].Reset();
        }
        this.strCurrentFile = "";
        this.SelLayer(1);
        this.Update(UpAll);
        this.iEdit = 0;
        GUIEditor.getInstance().SetTitle();
        GUIEditor.getInstance().SetStatus(" ");
    }

    public boolean accept(File dir, String name)
    {
        File file = new File(name);
        return !file.isDirectory();
    }

    public void Open()
    {
        File file;
        if (this.iEdit != 0 &&
            Dlg.Confirm("[" + this.strCurrentFile + "]\n" + GUIEditor.getInstance().GetMsgStr("notsaved")) == 2)
        {
            return;
        }
        FileDialog f = new FileDialog((Frame)GUIEditor.getInstance(), "Load Knob File", 0);
        f.setDirectory(this.strKnobDir);
        f.setFile("*.knob\u0000*.*\u0000");
        f.setVisible(true);
        if (f.getFile() == null)
        {
            return;
        }
        String path = String.valueOf(f.getDirectory()) + f.getFile();
        if (!Pathname.GetExt(path).equals("knob"))
        {
            path = Pathname.AddDefExt(path, "knob");
        }
        if (!(file = new File(path)).exists())
        {
            Dlg.Error(GUIEditor.getInstance().GetMsgStr("notfound"));
            return;
        }
        this.journal.Write();
        this.LoadExec(path);
        GUIEditor.getInstance().recent.Add(path);
    }

    public void SaveAs()
    {
        String name;
        FileDialog f;
        String path;
        if (this.strCurrentFile.equals(""))
        {
            this.strCurrentFile = "untitled.knob";
        }
        do
        {
            f = new FileDialog((Frame)GUIEditor.getInstance(), "Save Knob File", 1);
            f.setDirectory(this.strKnobDir);
            f.setFile(Pathname.GetFilename(this.strCurrentFile));
            f.setVisible(true);
            if (f.getFile() == null)
            {
                return;
            }
            name = f.getFile();
            if (Pathname.GetExt(name).equals("knob"))
            {
                path = String.valueOf(f.getDirectory()) + name;
                break;
            }
            name = Pathname.AddDefExt(name, "knob");
        } while (new File(path = String.valueOf(f.getDirectory()) + name).exists() &&
                 Dlg.Confirm(String.valueOf(GUIEditor.getInstance().GetMsgStr("overwrite")) + "\n[" + path + "]") != 0);
        GUIEditor.getInstance().recent.Add(path);
        this.strCurrentFile = path;
        this.strKnobDir = Pathname.GetDir(path);
        this.SaveExec(path);
        this.iEdit = 0;
        GUIEditor.getInstance().SetTitle();
    }

    public void Browse(String loc)
    {
        Desktop desktop = Desktop.getDesktop();
        try
        {
            desktop.browse(new URI(loc));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    public void Save()
    {
        if (this.strCurrentFile != "")
        {
            this.SaveExec(this.strCurrentFile);
            this.iEdit = 0;
            GUIEditor.getInstance().SetTitle();
        }
        else
        {
            this.SaveAs();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void Export()
    {
        String name;
        FileDialog f;
        String path;
        this.bHasAlpha = false;
        this.iRenderModeOrg = this.prefs.rendermode;
        this.strOutputFile = this.strCurrentFile.equals("")
                                 ? "untitled." + this.strExportType
                                 : Pathname.ReplaceExt(this.strCurrentFile, this.strExportType);
        do
        {
            f = new FileDialog((Frame)GUIEditor.getInstance(), "Export Image", 1);
            f.setDirectory(this.strImgDir);
            f.setFile(Pathname.GetFilename(this.strOutputFile));
            f.setVisible(true);
            if (f.getFile() == null)
            {
                return;
            }
            name = f.getFile();
            if (!Pathname.GetExt(name).equals(""))
            {
                path = String.valueOf(f.getDirectory()) + name;
                break;
            }
            name = Pathname.AddDefExt(name, this.strExportType);
        } while (new File(path = String.valueOf(f.getDirectory()) + name).exists() &&
                 Dlg.Confirm(String.valueOf(GUIEditor.getInstance().GetMsgStr("overwrite")) + "\n[" + path + "]") != 0);
        this.strOutputFile = path;
        this.strImgDir = Pathname.GetDir(path);
        String ext = Pathname.GetExt(this.strOutputFile);
        if (this.prefs.exportoption.val == 2)
        {
            if (ext.equals("png"))
            {
                this.bHasAlpha = true;
                this.strExportType = ext;
            }
            else
            {
                if (!ext.equals("gif"))
                {
                    Dlg.Message(GUIEditor.getInstance().GetMsgStr("animformaterr"));
                    return;
                }
                this.strExportType = ext;
            }
        }
        else if (ext.equals("png"))
        {
            this.bHasAlpha = true;
            this.strExportType = ext;
        }
        else if (ext.equals("jpg"))
        {
            this.strExportType = ext;
        }
        else if (ext.equals("bmp"))
        {
            this.strExportType = ext;
        }
        else if (ext.equals("gif"))
        {
            this.strExportType = ext;
        }
        else
        {
            if (!ext.equals("tga"))
            {
                Dlg.Message("Unknown Image Type");
                return;
            }
            this.bHasAlpha = true;
            this.strExportType = ext;
        }
        this.renderreq.WaitBreak();
        this.prefs.rendermode = 1;
        this.SetSize();
        this.iExporting = 1;
        if (!this.renderreq.IsRun())
        {
            this.renderreq.RunReq();
        }
        this.progress = new ProgressDialog("Now Rendering...");
        this.progress.Start();
        while (this.iExporting != 0)
        {
            try
            {
                Thread.sleep(100L);
            }
            catch (InterruptedException interruptedException)
            {
                // empty catch block
            }
        }
        if (this.iRenderModeOrg != this.prefs.rendermode)
        {
            this.prefs.rendermode = this.iRenderModeOrg;
            this.Update(UpPrefParam | Up_LayerPrev);
            this.SelLayer(this.iCurrentLayer);
        }
        if (this.progress != null && this.progress.isVisible())
        {
            this.progress.setVisible(false);
            this.progress = null;
        }
        GUIEditor.getInstance().SetStatus("Export Done [" + this.strOutputFile + "]");
    }

    public void RenderingDone(int iExport)
    {
        if (iExport != 0)
        {
            Bitmap bmp;
            if (this.prefs.oversampling.val != 0)
            {
                int w = this.imgRender.width >> this.prefs.oversampling.val;
                int h = this.imgRender.height >> this.prefs.oversampling.val;
                bmp = new Bitmap(w, h);
                this.imgRender.DecimationTo(bmp, 0, 0, w, h, 0, 0, this.imgRender.width, this.imgRender.height);
            }
            else
            {
                bmp = this.imgRender;
            }
            if (!this.bHasAlpha)
            {
                Graphics2D g2 = (Graphics2D)bmp.img.getGraphics();
                g2.setComposite(AlphaComposite.DstOver);
                g2.setColor(new Color(this.prefs.bkcolor.col.r, this.prefs.bkcolor.col.g, this.prefs.bkcolor.col.b));
                g2.fillRect(0, 0, bmp.width, bmp.height);
            }
            switch (this.prefs.exportoption.val)
            {
            case 0: {
                bmp.Write(this.strOutputFile, this.strExportType);
                break;
            }
            case 1: {
                Bitmap bmp1 = new Bitmap(this.prefs.pwidth.val, this.prefs.pheight.val);
                Graphics2D g2 = (Graphics2D)bmp1.img.getGraphics();
                g2.setComposite(AlphaComposite.Src);
                TextCounter tc = new TextCounter(this.strOutputFile);
                for (int i = 0; i < this.prefs.frames; ++i)
                {
                    int py;
                    int px;
                    String str = tc.GetCurrent();
                    tc.GetNext();
                    if (this.prefs.alignhorz.val != 0)
                    {
                        px = i * this.prefs.width;
                        py = 0;
                    }
                    else
                    {
                        px = 0;
                        py = i * this.prefs.height;
                    }
                    g2.drawImage(bmp.img, 0, 0, this.prefs.width, this.prefs.height, px, py, px + this.prefs.width,
                                 py + this.prefs.height, null);
                    bmp1.Write(str, this.strExportType);
                }
                break;
            }
            case 2: {
                if (this.strExportType.equals("gif"))
                {
                    AnimGif agif = new AnimGif(bmp, this.prefs.frames, this.prefs.alignhorz.val);
                    agif.Write(this.strOutputFile, this.prefs.loop.val, this.prefs.duration.val, this.prefs.bidir.val);
                    break;
                }
                APng apng = new APng(bmp, this.prefs.frames, this.prefs.alignhorz.val);
                apng.Write(this.strOutputFile, this.prefs.loop.val, this.prefs.duration.val, this.prefs.bidir.val);
            }
            }
            if (this.progress != null)
            {
                this.progress.setVisible(false);
                this.progress = null;
            }
        }
    }

    public void Undo()
    {
        this.journal.Undo();
        GUIEditor.getInstance().SetStatus(" ");
    }

    public void Redo()
    {
        this.journal.Redo();
        GUIEditor.getInstance().SetStatus(" ");
    }

    public void MoveLayer(int from, int to)
    {
        Layer l = this.layers.get(from);
        if (from > to)
        {
            this.layers.remove(from);
            this.layers.add(to, l);
            this.Update(Up_LyPanel | Up_Render);
        }
        if (from < to)
        {
            this.layers.remove(from);
            this.layers.add(to, l);
            this.Update(Up_LyPanel | Up_Render);
        }
    }

    public void BackLayer()
    {
        if (this.iCurrentLayer > 1)
        {
            this.journal.Write();
            this.MoveLayer(this.iCurrentLayer - 1, this.iCurrentLayer - 2);
            this.SelLayer(this.iCurrentLayer - 1);
            this.Edit();
        }
    }

    public void ForeLayer()
    {
        if (this.iCurrentLayer > 0 && this.iCurrentLayer < this.iMaxLayer)
        {
            this.journal.Write();
            this.MoveLayer(this.iCurrentLayer - 1, this.iCurrentLayer);
            this.SelLayer(this.iCurrentLayer + 1);
            this.Edit();
        }
    }

    public void RenameLayer()
    {
        if (this.iCurrentLayer > 0)
        {
            this.journal.Write();
            String name = Dlg.Input("JKnobMan", GUIEditor.getInstance().GetMsgStr("renamelayer"),
                                    this.layers.get((int)(this.iCurrentLayer - 1)).name);
            if (name != null)
            {
                this.layers.get((int)(this.iCurrentLayer - 1)).name = name.equals("") ? null : name;
                GUIEditor.getInstance().lypanel.Set();
            }
            this.Edit();
        }
    }

    public void AddLayer()
    {
        this.journal.Write();
        this.layers.add(this.iCurrentLayer, new Layer(this, true));
        ++this.iMaxLayer;
        ++this.iCurrentLayer;
        this.SelLayer(this.iCurrentLayer);
        this.Update(Up_LyPanel);
        this.Edit();
        GUIEditor.getInstance().SetStatus("AddLayer:" + this.iCurrentLayer);
    }

    public void DupLayer()
    {
        if (this.iCurrentLayer > 0)
        {
            this.journal.Write();
            this.layers.add(this.iCurrentLayer, this.layers.get(this.iCurrentLayer - 1).Clone(true));
            ++this.iMaxLayer;
            ++this.iCurrentLayer;
            this.SelLayer(this.iCurrentLayer);
            GUIEditor.getInstance().SetupLayer(this.GetCurrentLayer());
            this.Update(Up_LyPanel | Up_Prim | Up_LayerPrev | Up_Render);
            this.Edit();
            GUIEditor.getInstance().SetStatus("DupLayer:" + (this.iCurrentLayer - 1) + "=>" + this.iCurrentLayer);
        }
    }

    public void DelLayer()
    {
        if (this.iCurrentLayer > 0)
        {
            this.journal.Write();
            if (Dlg.Confirm(GUIEditor.getInstance().GetMsgStr("dellayer")) == 0)
            {
                this.Edit();
                GUIEditor.getInstance().SetStatus("DelLayer:" + this.iCurrentLayer);
                this.layers.remove(this.iCurrentLayer - 1);
                --this.iMaxLayer;
                if (this.iCurrentLayer > this.iMaxLayer)
                {
                    this.iCurrentLayer = this.iMaxLayer;
                }
                this.SelLayer(this.iCurrentLayer);
                this.Update(Up_LyPanel | Up_LayerPrev | Up_Render);
            }
        }
    }

    public void CopyLayer()
    {
        if (this.iCurrentLayer > 0)
        {
            ProfileWriter ppw = new ProfileWriter(GUIEditor.getInstance().strClip, "utf-16le");
            ppw.WriteBOM();
            this.SaveLayer(ppw, this.layers.get(this.iCurrentLayer - 1), 0);
            ppw.Close();
            GUIEditor.getInstance().SetStatus("CopyLayer:" + this.iCurrentLayer);
        }
    }

    public void PasteLayer()
    {
        this.journal.Write();
        this.layers.add(this.iCurrentLayer, new Layer(this, true));
        ++this.iMaxLayer;
        ++this.iCurrentLayer;
        ProfileReader ppr = new ProfileReader(GUIEditor.getInstance().strClip);
        this.LoadLayer(ppr, this.layers.get(this.iCurrentLayer - 1), 0);
        ppr.Close();
        GUIEditor.getInstance().SetupLayer(this.GetCurrentLayer());
        this.Update(Up_LyPanel | Up_Prim | Up_LayerPrev | Up_Render);
        this.Edit();
        GUIEditor.getInstance().SetStatus("PasteLayer:" + this.iCurrentLayer);
    }

    public void CanvasSize()
    {
        DialogCanvasSize dlg = new DialogCanvasSize(GUIEditor.getInstance());
    }

    public void Config()
    {
        DialogConfig dlg = new DialogConfig(GUIEditor.getInstance());
    }

    public void Exit()
    {
        GUIEditor.getInstance().getToolkit().getSystemEventQueue().postEvent(
            new WindowEvent(GUIEditor.getInstance(), 201));
    }

    public static Control getInstance()
    {
        return inst;
    }

    public void Update(int m)
    {
        if ((m & Up_Prefs) != 0)
        {
            this.renderreq.WaitBreak();
            this.SetSize();
            GUIEditor.getInstance().prefspanel.Setup();
            GUIEditor.getInstance().prevpanel.scr.doLayout();
        }
        if ((m & Up_LyPanel) != 0)
        {
            GUIEditor.getInstance().lypanel.Set();
        }
        if ((m & Up_PrimType) != 0)
        {
            this.SetSize();
            GUIEditor.getInstance().ppanel.DispPrimParams(this.iCurrentLayer);
        }
        if ((m & Up_Prim) != 0 && this.iCurrentLayer > 0)
        {
            this.layers.get((int)(this.iCurrentLayer - 1)).prim.Update();
        }
        if ((m & Up_LayerPrev) != 0)
        {
            GUIEditor.getInstance().UpdateLayerPreview();
        }
        if ((m & Up_Render) != 0)
        {
            this.render.Stop();
            this.render.Redraw();
        }
    }

    class TexLoader extends SwingWorker<Object, Object>
    {
        @Override public Object doInBackground()
        {
            Control ctl = Control.getInstance();
            for (int i = 0; i < ctl.fileTextures.length; ++i)
            {
                Tex tx = new Tex(ctl.fileTextures[i].getPath());
                Control.this.iconTextures[i + 1] = tx.GetImageIcon();
            }
            return null;
        }

        @Override public void done()
        {
            for (int i = 1; i < Control.getInstance().strTextures.length; ++i)
            {
                GUIEditor.getInstance().ppanel.texturefile.SetItem(i, Control.this.iconTextures[i],
                                                                   Control.this.strTextures[i]);
            }
        }
    }
}

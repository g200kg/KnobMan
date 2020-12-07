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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class GUIEditor
    extends JFrame implements UpdateReq, ComponentListener, WindowListener, ActionListener, DropTargetListener
{
    Rectangle rcWin = new Rectangle();
    JSplitPane mainsplit;
    JSplitPane lysplit;
    LayerPanel lypanel;
    JPanel prpanel;
    PrimPanel ppanel;
    JScrollPane primscr;
    EffPanel effpanel;
    PreviewPanel prevpanel;
    JPanel editpanel;
    PrefsPanel prefspanel;
    CurveEditor curveeditor;
    ShapeEditor shapeeditor;
    BitmapView bv1;
    BitmapView bv2;
    Control ctl;
    JMenuBar menu;
    JMenu menuRecent;
    String strIni = new ResFilename("JKnobMan.ini").GetString();
    String strIcon;
    String strNone;
    String strLangIni;
    String strShortcutsIni;
    String strKnobBrowser;
    String strClip = new ResFilename("$Clip.dat").GetString();
    String strLastExportType;
    Bitmap bmpNone;
    Image imgAppIcon = new ResFilename("Resource/Images/JKnobMan.png").GetImage();
    ImageIcon[] imgIcon;
    ImageIcon iconPicker;
    ImageIcon iconPickerCursor;
    ImageIcon iconPickerCursorNull;
    ImageIcon iconSpin;
    ImageIcon iconVisible;
    ImageIcon iconSolo;
    ImageIcon iconAddLayer;
    ImageIcon iconDupLayer;
    ImageIcon iconDelLayer;
    ImageIcon iconUpLayer;
    ImageIcon iconDownLayer;
    ImageIcon iconZoomin;
    ImageIcon iconZoom1;
    ImageIcon iconZoomout;
    ImageIcon iconPreview;
    ImageIcon iconRender;
    ImageIcon iconTest;
    ImageIcon iconConfig;
    ImageIcon iconTreeOpen;
    ImageIcon iconTreeClose;
    ImageIcon[] iconMask;
    ImageIcon[] iconAnim;
    ImageIcon[] iconOp;
    ImageIcon[] iconGrad;
    String[] fonts;
    ImageIcon[] fontIcons;
    Font fontUI;
    Font fontUILarge;
    Font fontUISmall;
    JPanel panelCurrent;
    String strLaf;
    UIManager.LookAndFeelInfo[] lafs;
    TransparentIcon ticon;
    JCheckBoxMenuItem cbmTest;
    ProfileReader pprLang;
    ProfileReader pprShortcuts;
    RecentFiles recent;
    Locale locale;
    HashMap<String, String> hmMessage;
    String strLocale = null;
    DropTarget dtTexture;
    DropTarget dtImage;
    int iOs;
    int iWheelDir;
    int iUseCapture;
    int iDisableMnemonic;
    private static GUIEditor inst;
    Status status;

    public GUIEditor(Control ctl)
    {
        int j;
        String s;
        this.bmpNone = new Bitmap(16, 16);
        this.setIconImage(this.imgAppIcon);
        this.ctl = ctl;
        inst = this;
        String os = System.getProperty("os.name");
        String osver = System.getProperty("os.version");
        this.iOs = 2;
        if (os.indexOf("Windows") >= 0)
        {
            this.iOs = 0;
        }
        if (os.indexOf("Mac") >= 0)
        {
            this.iOs = 1;
        }
        if (this.iOs == 0)
        {
            this.strKnobBrowser = Double.parseDouble(osver) >= 6.0
                                      ? new ResFilename("Resource/Support/KnobBrowserVista.dll").GetString()
                                      : new ResFilename("Resource/Support/KnobBrowserXP.dll").GetString();
        }
        this.fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        this.fontIcons = new ImageIcon[this.fonts.length];
        for (int i = 0; i < this.fonts.length; ++i)
        {
            Bitmap bmp = new Bitmap(24, 20);
            Graphics2D g2 = (Graphics2D)bmp.img.getGraphics();
            Font f = new Font(this.fonts[i], 0, 20);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setFont(f);
            g2.setColor(Color.red);
            g2.drawString("Abc", 0, 18);
            this.fontIcons[i] = new ImageIcon(bmp.img);
        }
        this.rcWin = new Rectangle();
        ProfileReader ppr = new ProfileReader(this.strIni);
        ppr.SetSection("Window");
        int iMaxi = ppr.ReadInt("maximize", 0);
        this.rcWin.x = ppr.ReadInt("x", 100);
        this.rcWin.y = ppr.ReadInt("y", 100);
        this.rcWin.width = ppr.ReadInt("w", 1200);
        this.rcWin.height = ppr.ReadInt("h", 600);
        int iDiv1 = ppr.ReadInt("div1", 160);
        int iDiv2 = ppr.ReadInt("div2", 870);
        ppr.SetSection("UI");
        String l = Locale.getDefault().getLanguage();
        switch (this.iOs)
        {
        case 0: {
            this.strLaf = ppr.ReadString("laf", "Windows");
            this.strShortcutsIni = ppr.ReadString("shortcuts", "default.ini");
            if (l.equals("ja"))
            {
                this.strLangIni = ppr.ReadString("lang", "ja.ini");
                break;
            }
            this.strLangIni = ppr.ReadString("lang", "en.ini");
            break;
        }
        case 1: {
            this.strLaf = ppr.ReadString("laf", "Mac OS X");
            this.strShortcutsIni = ppr.ReadString("shortcuts", "mackey.ini");
            if (l.equals("ja"))
            {
                this.strLangIni = ppr.ReadString("lang", "ja-mac.ini");
                break;
            }
            this.strLangIni = ppr.ReadString("lang", "en.ini");
            break;
        }
        default: {
            this.strLaf = ppr.ReadString("laf", "");
            this.strShortcutsIni = ppr.ReadString("shortcuts", "default.ini");
        }
        }
        this.iWheelDir = ppr.ReadInt("wheeldir", 0);
        this.iUseCapture = ppr.ReadInt("usecapture", 0);
        this.iDisableMnemonic = ppr.ReadInt("disablemnemonic", 0);
        WinPos.Limit(this.rcWin);
        this.setBounds(this.rcWin.x, this.rcWin.y, this.rcWin.width, this.rcWin.height);
        try
        {
            this.lafs = UIManager.getInstalledLookAndFeels();
            for (int i = 0; i < this.lafs.length; ++i)
            {
                if (!this.lafs[i].getName().equals(this.strLaf))
                    continue;
                UIManager.setLookAndFeel(this.lafs[i].getClassName());
            }
        }
        catch (Exception i)
        {
            // empty catch block
        }
        this.menuRecent = new JMenu();
        ppr.SetSection("Files");
        this.recent = new RecentFiles(this.menuRecent, 0);
        this.recent.LoadFromIni(ppr);
        this.recent.Refresh();
        ctl.strExportType = ppr.ReadString("lastexport", "png");
        if (ctl.strExportType.length() == 0)
        {
            ctl.strExportType = "png";
        }
        ctl.strKnobDir = ppr.ReadString("knobdir", "");
        ctl.strImgDir = ppr.ReadString("imgdir", "");
        ppr.Close();
        this.imgIcon = new ImageIcon[16];
        for (int i = 0; i < 16; ++i)
        {
            String s2 =
                "Resource/Images/" +
                (new String[] {"none.png", "image.png", "circle.png", "circlefill.png", "metalcircle.png",
                               "wavecircle.png", "sphere.png", "rect.png", "rectfill.png", "triangle.png", "line.png",
                               "radiateline.png", "hlines.png", "vlines.png", "text.png", "shape.png"})[i];
            this.imgIcon[i] = new ResFilename(s2).GetImageIcon();
        }
        this.iconAnim = new ImageIcon[10];
        this.iconAnim[0] = new ResFilename("Resource/Images/animn.png").GetImageIcon();
        this.iconAnim[1] = new ResFilename("Resource/Images/anim0.png").GetImageIcon();
        this.iconAnim[2] = new ResFilename("Resource/Images/anim1.png").GetImageIcon();
        this.iconAnim[3] = new ResFilename("Resource/Images/anim2.png").GetImageIcon();
        this.iconAnim[4] = new ResFilename("Resource/Images/anim3.png").GetImageIcon();
        this.iconAnim[5] = new ResFilename("Resource/Images/anim4.png").GetImageIcon();
        this.iconAnim[6] = new ResFilename("Resource/Images/anim5.png").GetImageIcon();
        this.iconAnim[7] = new ResFilename("Resource/Images/anim6.png").GetImageIcon();
        this.iconAnim[8] = new ResFilename("Resource/Images/anim7.png").GetImageIcon();
        this.iconAnim[9] = new ResFilename("Resource/Images/anim8.png").GetImageIcon();
        this.iconMask = new ImageIcon[4];
        this.iconMask[0] = new ResFilename("Resource/Images/maskrot.png").GetImageIcon();
        this.iconMask[1] = new ResFilename("Resource/Images/maskrad.png").GetImageIcon();
        this.iconMask[2] = new ResFilename("Resource/Images/maskh.png").GetImageIcon();
        this.iconMask[3] = new ResFilename("Resource/Images/maskv.png").GetImageIcon();
        this.iconOp = new ImageIcon[2];
        this.iconOp[0] = new ResFilename("Resource/Images/opand.png").GetImageIcon();
        this.iconOp[1] = new ResFilename("Resource/Images/opor.png").GetImageIcon();
        this.iconGrad = new ImageIcon[2];
        this.iconGrad[0] = new ResFilename("Resource/Images/grads.png").GetImageIcon();
        this.iconGrad[1] = new ResFilename("Resource/Images/gradb.png").GetImageIcon();
        this.iconSpin = new ResFilename("Resource/Images/spin.png").GetImageIcon();
        this.iconVisible = new ResFilename("Resource/Images/visible.png").GetImageIcon();
        this.iconSolo = new ResFilename("Resource/Images/solo.png").GetImageIcon();
        this.iconPicker = new ResFilename("Resource/Images/picker.png").GetImageIcon();
        this.iconPickerCursor = new ResFilename("Resource/Images/pickercursor.png").GetImageIcon();
        this.iconPickerCursorNull = new ResFilename("Resource/Images/null.png").GetImageIcon();
        this.iconAddLayer = new ResFilename("Resource/Images/addlayer.png").GetImageIcon();
        this.iconDupLayer = new ResFilename("Resource/Images/duplayer.png").GetImageIcon();
        this.iconDelLayer = new ResFilename("Resource/Images/dellayer.png").GetImageIcon();
        this.iconUpLayer = new ResFilename("Resource/Images/uplayer.png").GetImageIcon();
        this.iconDownLayer = new ResFilename("Resource/Images/downlayer.png").GetImageIcon();
        this.iconPreview = new ResFilename("Resource/Images/preview.png").GetImageIcon();
        this.iconRender = new ResFilename("Resource/Images/render.png").GetImageIcon();
        this.iconTest = new ResFilename("Resource/Images/test.png").GetImageIcon();
        this.iconZoomout = new ResFilename("Resource/Images/zoomout.png").GetImageIcon();
        this.iconZoom1 = new ResFilename("Resource/Images/zoom1.png").GetImageIcon();
        this.iconZoomin = new ResFilename("Resource/Images/zoomin.png").GetImageIcon();
        this.iconConfig = new ResFilename("Resource/Images/config.png").GetImageIcon();
        this.iconTreeOpen = new ResFilename("Resource/Images/treeopen.png").GetImageIcon();
        this.iconTreeClose = new ResFilename("Resource/Images/treeclose.png").GetImageIcon();
        CellItem[][] arrarrcellItem = new CellItem[6][];
        CellItem[] arrcellItem = new CellItem[13];
        arrcellItem[0] = new CellItem("file", null);
        arrcellItem[1] = new CellItem("new", null);
        arrcellItem[3] = new CellItem("export", null);
        arrcellItem[5] = new CellItem("load", null);
        arrcellItem[6] = new CellItem("recent", null);
        arrcellItem[7] = new CellItem("save", null);
        arrcellItem[8] = new CellItem("saveas", null);
        arrcellItem[10] = new CellItem("config", this.iconConfig);
        arrcellItem[12] = new CellItem("exit", null);
        arrarrcellItem[0] = arrcellItem;
        CellItem[] arrcellItem2 = new CellItem[5];
        arrcellItem2[0] = new CellItem("edit", null);
        arrcellItem2[1] = new CellItem("undo", null);
        arrcellItem2[2] = new CellItem("redo", null);
        arrcellItem2[4] = new CellItem("setlayout", null);
        arrarrcellItem[1] = arrcellItem2;
        CellItem[] arrcellItem3 = new CellItem[11];
        arrcellItem3[0] = new CellItem("layer", null);
        arrcellItem3[1] = new CellItem("renamelayer", null);
        arrcellItem3[2] = new CellItem("addlayer", null);
        arrcellItem3[3] = new CellItem("duplayer", null);
        arrcellItem3[4] = new CellItem("dellayer", null);
        arrcellItem3[6] = new CellItem("backlayer", null);
        arrcellItem3[7] = new CellItem("forelayer", null);
        arrcellItem3[9] = new CellItem("copylayer", null);
        arrcellItem3[10] = new CellItem("pastelayer", null);
        arrarrcellItem[2] = arrcellItem3;
        arrarrcellItem[3] =
            new CellItem[] {new CellItem("animcurve", null),          new CellItem("curve1", this.iconAnim[2]),
                            new CellItem("curve2", this.iconAnim[3]), new CellItem("curve3", this.iconAnim[4]),
                            new CellItem("curve4", this.iconAnim[5]), new CellItem("curve5", this.iconAnim[6]),
                            new CellItem("curve6", this.iconAnim[7]), new CellItem("curve7", this.iconAnim[8]),
                            new CellItem("curve8", this.iconAnim[9])};
        arrarrcellItem[4] = new CellItem[] {new CellItem("test", null), new CellItem("testknob", null)};
        arrarrcellItem[5] = new CellItem[] {new CellItem("help", null), new CellItem("helpindex", null),
                                            new CellItem("knobgallery", null)};
        CellItem[][] strMenu = arrarrcellItem;
        this.menu = new JMenuBar();
        this.pprLang = new ProfileReader(new ResFilename("Resource/Lang/" + this.strLangIni).GetString());
        this.pprShortcuts =
            new ProfileReader(new ResFilename("Resource/Shortcuts/" + this.strShortcutsIni).GetString());
        this.pprLang.SetSection("UIFont");
        String sFont = this.pprLang.ReadString("font", "");
        String uifont = null;
        int i = 0;
        while ((s = PartStr.Get(sFont, i)) != null && s.length() > 0)
        {
            for (j = 0; j < this.fonts.length; ++j)
            {
                if (!this.fonts[j].equals(s))
                    continue;
                uifont = s;
                break;
            }
            if (uifont != null)
                break;
            ++i;
        }
        this.fontUI = new Font(uifont, 0, this.pprLang.ReadInt("sizenormal", 12));
        this.fontUILarge = new Font(uifont, 1, this.pprLang.ReadInt("sizelarge", 12));
        this.fontUISmall = new Font(null, 0, this.pprLang.ReadInt("sizesmall", 10));
        this.pprLang.SetSection("Locale");
        this.strLocale = this.pprLang.ReadString("locale", null);
        this.locale = this.strLocale != null ? new Locale(this.strLocale) : null;
        this.hmMessage = new HashMap();
        this.pprLang.SetSection("Config");
        this.hmMessage.put("lookandfeel", this.pprLang.ReadString("lookandfeel", "Look and Feel:"));
        this.hmMessage.put("language", this.pprLang.ReadString("language", "Language:"));
        this.hmMessage.put("shortcuts", this.pprLang.ReadString("shortcuts", "Shortcuts"));
        this.hmMessage.put("wheeldir", this.pprLang.ReadString("wheeldir", "Invert Wheel Direnction"));
        this.hmMessage.put("usecapture", this.pprLang.ReadString("usecapture", "Use Capture in 'Test' mode"));
        this.hmMessage.put("disablemnemo", this.pprLang.ReadString("disablemnemo", "Disable Menu Mnemonic"));
        this.hmMessage.put("regbrowser", this.pprLang.ReadString("regbrowser", "Register KnobBrowser"));
        this.hmMessage.put("unregbrowser", this.pprLang.ReadString("unregbrowser", "Unregister KnobBrowser"));
        this.hmMessage.put("ok", this.pprLang.ReadString("ok", "Ok"));
        this.hmMessage.put("restart", this.pprLang.ReadString("restart", "Restart"));
        this.pprLang.SetSection("CanvasSize");
        this.hmMessage.put("size", this.pprLang.ReadString("size", "Canvas Size:"));
        this.hmMessage.put("offset", this.pprLang.ReadString("offset", "Offset:"));
        this.hmMessage.put("ok", this.pprLang.ReadString("ok", "Ok"));
        this.hmMessage.put("cancel", this.pprLang.ReadString("cancel", "Cancel"));
        this.pprLang.SetSection("Message");
        this.hmMessage.put("notfound", this.pprLang.ReadString("notfound", "File Not Found."));
        this.hmMessage.put("notsaved", this.pprLang.ReadString("notsaved", "File Not Saved. OK?"));
        this.hmMessage.put("formaterr", this.pprLang.ReadString("formaterr", "File format error"));
        this.hmMessage.put("renamelayer", this.pprLang.ReadString("renamelayer", "Rename Layer"));
        this.hmMessage.put("dellayer", this.pprLang.ReadString("dellayer", "Delete Layer. OK?"));
        this.hmMessage.put("imgformaterr", this.pprLang.ReadString("imgformaterr", "File Format Error"));
        this.hmMessage.put("animformaterr", this.pprLang.ReadString("animformaterr", "Anim File Format Error"));
        this.hmMessage.put("overwrite", this.pprLang.ReadString("overwrite", "File already exist. Overwrite?"));
        this.pprLang.SetSection("Layers");
        this.hmMessage.put("preference", this.pprLang.ReadString("preference", "Preference"));
        this.hmMessage.put("layer", this.pprLang.ReadString("layer", "Layer%d"));
        this.pprLang.SetSection("Menu");
        this.pprShortcuts.SetSection("Shortcuts");
        for (i = 0; i < strMenu.length; ++i)
        {
            String strItem = this.pprLang.ReadString(strMenu[i][0].text, strMenu[i][0].text);
            JMenu submenu = new JMenu(PartStr.Get(strItem, 0));
            String strMnemo = PartStr.Get(strItem, 1);
            if (this.iDisableMnemonic == 0 && strMnemo != null && strMnemo.length() > 0)
            {
                submenu.setMnemonic(strMnemo.charAt(0));
            }
            this.menu.add(submenu);
            for (j = 1; j < strMenu[i].length; ++j)
            {
                String strTooltip;
                if (strMenu[i][j] == null)
                {
                    submenu.addSeparator();
                    continue;
                }
                String strShort = this.pprShortcuts.ReadString(strMenu[i][j].text, null);
                strItem = this.pprLang.ReadString(strMenu[i][j].text, strMenu[i][j].text);
                ImageIcon icon = strMenu[i][j].icon;
                if (strMenu[i][j].text.equals("recent"))
                {
                    this.menuRecent.setText(PartStr.Get(strItem, 0));
                    strMnemo = PartStr.Get(strItem, 1);
                    if (this.iDisableMnemonic == 0 && strMnemo != null && strMnemo.length() > 0)
                    {
                        this.menuRecent.setMnemonic(strMnemo.charAt(0));
                    }
                    if (strShort != null && strShort.length() > 0)
                    {
                        this.menuRecent.setAccelerator(KeyStroke.getKeyStroke(strShort));
                    }
                    submenu.add(this.menuRecent);
                    continue;
                }
                if (strMenu[i][j].text.equals("testknob"))
                {
                    this.cbmTest = new JCheckBoxMenuItem(PartStr.Get(strItem, 0));
                    strMnemo = PartStr.Get(strItem, 1);
                    if (this.iDisableMnemonic == 0 && strMnemo != null && strMnemo.length() > 0)
                    {
                        this.cbmTest.setMnemonic(strMnemo.charAt(0));
                    }
                    if (strShort != null && strShort.length() > 0)
                    {
                        this.cbmTest.setAccelerator(KeyStroke.getKeyStroke(strShort));
                    }
                    if ((strTooltip = PartStr.Get(strItem, 2)) != null && strTooltip.length() > 0)
                    {
                        this.cbmTest.setToolTipText(strTooltip);
                    }
                    submenu.add(this.cbmTest);
                    this.cbmTest.setActionCommand(strMenu[i][j].text);
                    this.cbmTest.addActionListener(this);
                    continue;
                }
                JMenuItem item = new JMenuItem(PartStr.Get(strItem, 0));
                strMnemo = PartStr.Get(strItem, 1);
                if (this.iDisableMnemonic == 0 && strMnemo != null && strMnemo.length() > 0)
                {
                    item.setMnemonic(strMnemo.charAt(0));
                }
                if (strShort != null && strShort.length() > 0)
                {
                    item.setAccelerator(KeyStroke.getKeyStroke(strShort));
                }
                if ((strTooltip = PartStr.Get(strItem, 2)) != null && strTooltip.length() > 0)
                {
                    item.setToolTipText(strTooltip);
                }
                if (icon != null)
                {
                    item.setIcon(icon);
                }
                submenu.add(item);
                item.setActionCommand(strMenu[i][j].text);
                item.addActionListener(this);
            }
        }
        this.setJMenuBar(this.menu);
        this.pprLang.SetSection("Layers");
        this.prevpanel = null;
        this.lypanel = new LayerPanel(this);
        JScrollPane lyscr = new JScrollPane(this.lypanel, 22, 32);
        lyscr.getVerticalScrollBar().setUnitIncrement(20);
        this.ppanel = new PrimPanel(this);
        this.effpanel = new EffPanel(this);
        this.prefspanel = new PrefsPanel(this.pprLang);
        this.curveeditor = new CurveEditor(this.pprLang);
        this.shapeeditor = new ShapeEditor(this.pprLang);
        this.pprLang.SetSection("Buttons");
        this.prevpanel = new PreviewPanel(this, this.pprLang);
        JPanel basepanel = new JPanel(new BorderLayout());
        this.status = new Status(ctl.strVer);
        basepanel.add((Component)this.status, "South");
        Color colbg = basepanel.getBackground();
        JPanel toolpanel = new JPanel(new FlowLayout(0));
        String sAdd = this.pprLang.ReadString("addlayer", "Add");
        JButton btnAddLayer = new JButton(PartStr.Get(sAdd, 0));
        btnAddLayer.setIcon(this.iconAddLayer);
        btnAddLayer.setFont(this.fontUISmall);
        btnAddLayer.setToolTipText(PartStr.Get(sAdd, 1));
        btnAddLayer.setActionCommand("addlayer");
        String sDup = this.pprLang.ReadString("duplayer", "Dup");
        JButton btnDupLayer = new JButton(PartStr.Get(sDup, 0));
        btnDupLayer.setIcon(this.iconDupLayer);
        btnDupLayer.setFont(this.fontUISmall);
        btnDupLayer.setToolTipText(PartStr.Get(sDup, 1));
        btnDupLayer.setActionCommand("duplayer");
        String sDel = this.pprLang.ReadString("dellayer", "Del");
        JButton btnDelLayer = new JButton(PartStr.Get(sDel, 0));
        btnDelLayer.setIcon(this.iconDelLayer);
        btnDelLayer.setFont(this.fontUISmall);
        btnDelLayer.setToolTipText(PartStr.Get(sDel, 1));
        btnDelLayer.setActionCommand("dellayer");
        String sBack = this.pprLang.ReadString("backlayer", "Back");
        JButton btnBackLayer = new JButton(PartStr.Get(sBack, 0));
        btnBackLayer.setIcon(this.iconUpLayer);
        btnBackLayer.setFont(this.fontUISmall);
        btnBackLayer.setToolTipText(PartStr.Get(sBack, 1));
        btnBackLayer.setActionCommand("backlayer");
        String sFore = this.pprLang.ReadString("forelayer", "Fore");
        JButton btnForeLayer = new JButton(PartStr.Get(sFore, 0));
        btnForeLayer.setIcon(this.iconDownLayer);
        btnForeLayer.setFont(this.fontUISmall);
        btnForeLayer.setToolTipText(PartStr.Get(sFore, 1));
        btnForeLayer.setActionCommand("forelayer");
        btnAddLayer.addActionListener(this);
        btnDupLayer.addActionListener(this);
        btnDelLayer.addActionListener(this);
        btnBackLayer.addActionListener(this);
        btnForeLayer.addActionListener(this);
        toolpanel.add(btnAddLayer);
        toolpanel.add(btnDupLayer);
        toolpanel.add(btnDelLayer);
        toolpanel.add(btnBackLayer);
        toolpanel.add(btnForeLayer);
        basepanel.add((Component)toolpanel, "North");
        this.ppanel.setSize(520, 100);
        this.editpanel = new JPanel(new BorderLayout());
        this.ppanel.setBounds(0, 0, 20, 600);
        this.ppanel.setPreferredSize(new Dimension(20, 500));
        this.ppanel.setBackground(colbg);
        this.pprLang.SetSection("Pane");
        this.primscr = new JScrollPane(this.ppanel, 22, 31);
        this.primscr.getVerticalScrollBar().setUnitIncrement(20);
        TitledBorder tb =
            new TitledBorder(new LineBorder(Color.gray), this.pprLang.ReadString("primitive", "Primitive"), 1, 2);
        tb.setTitleFont(GUIEditor.getInstance().fontUI);
        this.primscr.setBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), tb));
        this.prpanel = new JPanel(new BorderLayout());
        this.prpanel.setPreferredSize(new Dimension(320, 600));
        this.bv1 = new BitmapView(0, 450, 128, 128, null, new Col(128, 128, 128));
        this.bv1.keepaspect = true;
        this.bv2 = new BitmapView(130, 450, 128, 128, null, new Col(128, 128, 128));
        this.bv2.keepaspect = true;
        JPanel pvpanel = new JPanel(new FlowLayout());
        tb = new TitledBorder(new LineBorder(Color.gray), this.pprLang.ReadString("layerpreview", "Layer Preview"), 1,
                              2);
        tb.setTitleFont(GUIEditor.getInstance().fontUI);
        pvpanel.setBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), tb));
        pvpanel.add(this.bv1);
        pvpanel.add(this.bv2);
        pvpanel.setSize(300, 190);
        pvpanel.setPreferredSize(new Dimension(300, 190));
        this.prpanel.add((Component)pvpanel, "South");
        this.prpanel.add((Component)this.primscr, "Center");
        this.editpanel.add((Component)this.prpanel, "West");
        JScrollPane effscr = new JScrollPane(this.effpanel);
        effscr.getVerticalScrollBar().setUnitIncrement(20);
        tb = new TitledBorder(new LineBorder(Color.gray), this.pprLang.ReadString("effects", "Effects"), 1, 2);
        tb.setTitleFont(GUIEditor.getInstance().fontUI);
        effscr.setBorder(new CompoundBorder(new EmptyBorder(8, 8, 8, 8), tb));
        this.editpanel.add((Component)effscr, "Center");
        this.lysplit = new JSplitPane(1, lyscr, this.editpanel);
        this.lysplit.setDividerSize(6);
        this.lysplit.setDividerLocation(iDiv1);
        this.lysplit.setBackground(colbg);
        this.editpanel.setBackground(colbg);
        this.primscr.setBackground(colbg);
        effscr.setBackground(colbg);
        basepanel.add((Component)this.lysplit, "Center");
        this.mainsplit = new JSplitPane(1, basepanel, this.prevpanel);
        this.mainsplit.setDividerSize(6);
        this.mainsplit.setDividerLocation(iDiv2);
        this.getContentPane().add(this.mainsplit);
        this.setDefaultCloseOperation(0);
        this.addWindowListener(this);
        this.addComponentListener(this);
        this.lypanel.Set();
        this.SetTitle();
        if (iMaxi != 0)
        {
            this.setExtendedState(6);
        }
        this.setVisible(true);
        new DropTarget(this, this);
        this.panelCurrent = this.editpanel;
        this.ticon = null;
        this.pprShortcuts.Close();
        this.pprLang.Close();
        ctl.txloader.execute();
    }

    @Override public void windowActivated(WindowEvent e)
    {
    }

    @Override public void windowDeactivated(WindowEvent e)
    {
    }

    public void windowIconized(WindowEvent e)
    {
    }

    @Override public void windowOpened(WindowEvent e)
    {
        this.ppanel.type.ch.requestFocusInWindow();
    }

    @Override public void windowClosed(WindowEvent e)
    {
    }

    @Override public void windowIconified(WindowEvent e)
    {
    }

    @Override public void windowDeiconified(WindowEvent e)
    {
    }

    @Override public void windowClosing(WindowEvent e)
    {
        if (Control.getInstance().iEdit != 0 &&
            Dlg.Confirm("[" + this.ctl.strCurrentFile + "]\n" + this.GetMsgStr("notsaved")) == 2)
        {
            return;
        }
        this.ctl.render.Exit();
        this.SaveConfig();
        System.exit(0);
    }

    public void SaveConfig()
    {
        ProfileWriter ppw = new ProfileWriter(this.strIni, "utf-8");
        ppw.Section("Window");
        if ((GUIEditor.getInstance().getExtendedState() & 6) == 6)
        {
            ppw.WriteInt("maximize", 1);
        }
        else
        {
            ppw.WriteInt("maximize", 0);
        }
        ppw.WriteInt("x", GUIEditor.getInstance().rcWin.x);
        ppw.WriteInt("y", GUIEditor.getInstance().rcWin.y);
        ppw.WriteInt("w", GUIEditor.getInstance().rcWin.width);
        ppw.WriteInt("h", GUIEditor.getInstance().rcWin.height);
        ppw.WriteInt("div1", this.lysplit.getDividerLocation());
        ppw.WriteInt("div2", this.mainsplit.getDividerLocation());
        ppw.Section("UI");
        ppw.WriteStr("laf", this.strLaf);
        ppw.WriteStr("lang", this.strLangIni);
        ppw.WriteStr("shortcuts", this.strShortcutsIni);
        ppw.WriteInt("wheeldir", this.iWheelDir);
        ppw.WriteInt("usecapture", this.iUseCapture);
        ppw.WriteInt("disablemnemonic", this.iDisableMnemonic);
        ppw.Section("Files");
        ppw.WriteStr("lastexport", Control.getInstance().strExportType);
        ppw.WriteStr("knobdir", Control.getInstance().strKnobDir);
        ppw.WriteStr("imgdir", Control.getInstance().strImgDir);
        this.recent.SaveToIni(ppw);
        ppw.Close();
    }

    public void Reset()
    {
        this.lypanel.Set();
        this.SetTitle();
        if (Control.getInstance().iMaxLayer >= 1)
        {
            this.SetupLayer(Control.getInstance().layers.get(0));
        }
        else
        {
            this.SetupLayer(null);
        }
        this.prevpanel.btnPrev.setSelected(true);
        this.prevpanel.btnRender.setSelected(false);
        this.prevpanel.btnTest.setSelected(false);
        this.ppanel.DispPrimParams(Control.getInstance().iCurrentLayer);
        Control.getInstance().prefs.rendermode = 0;
        this.prevpanel.lbFrame.setVisible(false);
        this.requestFocus();
    }

    String GetMsgStr(String key)
    {
        return GUIEditor.getInstance().hmMessage.get(key);
    }

    void SetStatus(String str)
    {
        this.status.SetText(str);
    }

    void SetTitle()
    {
        Control ctl = Control.getInstance();
        String s = ctl.strCurrentFile;
        File f = new File(s);
        if (ctl.iEdit != 0)
        {
            this.setTitle("JKnobMan - * [" + f.getName() + "]");
        }
        else
        {
            this.setTitle("JKnobMan - [" + f.getName() + "]");
        }
    }

    public void SetupLayer(Layer layer)
    {
        if (layer == null)
        {
            if (this.lysplit.getRightComponent() != this.prefspanel)
            {
                int l = this.lysplit.getDividerLocation();
                this.lysplit.setRightComponent(this.prefspanel);
                this.lysplit.setDividerLocation(l);
            }
            this.prefspanel.Setup();
        }
        else
        {
            if (this.lysplit.getRightComponent() != this.editpanel)
            {
                int l = this.lysplit.getDividerLocation();
                this.lysplit.setRightComponent(this.editpanel);
                this.lysplit.setDividerLocation(l);
            }
            this.ppanel.Setup(layer);
            this.effpanel.Setup(layer);
            if (layer.eff.centerx.val != 0.0 || layer.eff.centery.val != 0.0)
            {
                int px = (int)(layer.eff.centerx.val * 1.28 + 64.0);
                int py = (int)(-layer.eff.centery.val * 1.28 + 64.0);
                this.bv1.Setup(layer.imgprevf, px, py);
                this.bv2.Setup(layer.imgprevt, px, py);
            }
            else
            {
                this.bv1.Setup(layer.imgprevf, -1, -1);
                this.bv2.Setup(layer.imgprevt, -1, -1);
            }
        }
    }

    public void SetupAnimCurve(AnimCurve ac, int n)
    {
        if (this.lysplit.getRightComponent() != this.curveeditor)
        {
            int l = this.lysplit.getDividerLocation();
            this.lysplit.setRightComponent(this.curveeditor);
            this.lysplit.setDividerLocation(l);
        }
        this.curveeditor.Setup(ac, n);
    }

    public void SetupShapeEditor(Layer ly)
    {
        if (this.lysplit.getRightComponent() != this.shapeeditor)
        {
            int l = this.lysplit.getDividerLocation();
            this.lysplit.setRightComponent(this.shapeeditor);
            this.lysplit.setDividerLocation(l);
        }
        this.shapeeditor.Setup(ly);
    }

    public void SetSize(int w, int h)
    {
        this.prevpanel.SetSize(w, h);
    }

    public void UpdatePreview(Bitmap bmp)
    {
        this.prevpanel.Draw(bmp);
    }

    @Override public void Update(int m)
    {
        if (m > 0)
        {
            this.lypanel.Set();
        }
        if (m > 0)
        {
            this.prevpanel.Update();
        }
        this.ppanel.DispPrimParams(Control.getInstance().iCurrentLayer);
    }

    public void UpdateLayerPreview()
    {
        Layer ly = this.ctl.GetCurrentLayer();
        if (ly != null)
        {
            ly.UpdateLayerPreview();
        }
    }

    public static GUIEditor getInstance()
    {
        return inst;
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        String act = e.getActionCommand();
        if (act.startsWith("recent"))
        {
            boolean yes = true;
            if (this.ctl.iEdit == 0 ||
                Dlg.Confirm("[" + this.ctl.strCurrentFile + "]\n" + this.GetMsgStr("notsaved")) != 2)
            {
                int i = Integer.parseInt(act.substring(6));
                this.ctl.LoadExec(this.recent.Get(i));
            }
        }
        if (act.equals("new"))
        {
            this.ctl.New();
        }
        else if (act.equals("export"))
        {
            this.ctl.Export();
        }
        else if (act.equals("load"))
        {
            this.ctl.Open();
        }
        else if (act.equals("save"))
        {
            this.ctl.Save();
        }
        else if (act.equals("saveas"))
        {
            this.ctl.SaveAs();
        }
        else if (act.equals("config"))
        {
            this.ctl.Config();
        }
        else if (act.equals("exit"))
        {
            this.ctl.Exit();
        }
        else if (act.equals("undo"))
        {
            this.ctl.Undo();
        }
        else if (act.equals("redo"))
        {
            this.ctl.Redo();
        }
        else if (act.equals("setlayout"))
        {
            this.ctl.CanvasSize();
        }
        else if (act.equals("renamelayer"))
        {
            this.ctl.RenameLayer();
        }
        else if (act.equals("addlayer"))
        {
            this.ctl.AddLayer();
        }
        else if (act.equals("duplayer"))
        {
            this.ctl.DupLayer();
        }
        else if (act.equals("dellayer"))
        {
            this.ctl.DelLayer();
        }
        else if (act.equals("backlayer"))
        {
            this.ctl.BackLayer();
        }
        else if (act.equals("forelayer"))
        {
            this.ctl.ForeLayer();
        }
        else if (act.equals("copylayer"))
        {
            this.ctl.CopyLayer();
        }
        else if (act.equals("pastelayer"))
        {
            this.ctl.PasteLayer();
        }
        else if (act.equals("testknob"))
        {
            if (this.ticon == null)
            {
                this.ticon = new TransparentIcon();
                this.ticon.Update(this.ctl.imgRender, this.ctl.prefs.width, this.ctl.prefs.height,
                                  this.ctl.prefs.oversampling.val, this.ctl.prefs.frames, this.ctl.prefs.alignhorz.val);
                ((JCheckBoxMenuItem)e.getSource()).setSelected(true);
            }
            else
            {
                ((JCheckBoxMenuItem)e.getSource()).setSelected(false);
                this.ticon.dispose();
                this.ticon = null;
            }
        }
        else if (act.equals("curve1"))
        {
            this.SetupAnimCurve(this.ctl.animcurve[0], 1);
        }
        else if (act.equals("curve2"))
        {
            this.SetupAnimCurve(this.ctl.animcurve[1], 2);
        }
        else if (act.equals("curve3"))
        {
            this.SetupAnimCurve(this.ctl.animcurve[2], 3);
        }
        else if (act.equals("curve4"))
        {
            this.SetupAnimCurve(this.ctl.animcurve[3], 4);
        }
        else if (act.equals("curve5"))
        {
            this.SetupAnimCurve(this.ctl.animcurve[4], 5);
        }
        else if (act.equals("curve6"))
        {
            this.SetupAnimCurve(this.ctl.animcurve[5], 6);
        }
        else if (act.equals("curve7"))
        {
            this.SetupAnimCurve(this.ctl.animcurve[6], 7);
        }
        else if (act.equals("curve8"))
        {
            this.SetupAnimCurve(this.ctl.animcurve[7], 8);
        }
        else if (act.equals("helpindex"))
        {
            if (this.strLocale.equals("ja"))
            {
                this.ctl.Browse("http://www.g200kg.com/jp/docs/jknobman/index.html");
            }
            else
            {
                this.ctl.Browse("http://www.g200kg.com/en/docs/jknobman/index.html");
            }
        }
        else if (act.equals("knobgallery"))
        {
            this.ctl.Browse("http://www.g200kg.com/en/webknobman/gallery.php");
        }
    }

    @Override public void dragEnter(DropTargetDragEvent e)
    {
        if (e.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
        {
            e.acceptDrag(1);
        }
    }

    @Override public void dragExit(DropTargetEvent e)
    {
    }

    @Override public void dragOver(DropTargetDragEvent e)
    {
    }

    @Override public void dropActionChanged(DropTargetDragEvent e)
    {
    }

    @Override public void drop(DropTargetDropEvent dtde)
    {
        try
        {
            Transferable tr = dtde.getTransferable();
            if (tr.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
            {
                dtde.acceptDrop(3);
                List myList = (List)tr.getTransferData(DataFlavor.javaFileListFlavor);
                File file = (File)myList.get(0);
                String name = file.getAbsolutePath();
                String ext = Pathname.GetExt(name);
                if (dtde.getSource() == this.dtImage)
                {
                    if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("jpg") ||
                        ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("gif"))
                    {
                        this.ctl.journal.Write();
                        Layer ly = Control.getInstance().GetCurrentLayer();
                        File f = new File(name);
                        this.ppanel.file.SetText(f.getPath());
                        this.ctl.GetCurrentLayer().LoadImage(f.getPath());
                        this.ctl.Update(Control.UpPrimParam);
                        this.ctl.Edit();
                    }
                    else
                    {
                        Dlg.Error(this.GetMsgStr("imgformaterr"));
                    }
                }
                else if (dtde.getSource() == this.dtTexture)
                {
                    if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("jpg") ||
                        ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("gif"))
                    {
                        this.ctl.journal.Write();
                        Layer ly = Control.getInstance().GetCurrentLayer();
                        ly.prim.tex = ly.prim.tex0 = new Tex(new Bitmap(name), name);
                        File f = new File(name);
                        ly.prim.texturename = f.getName();
                        this.ppanel.texturefile.SetItem(0, ly.prim.tex.GetImageIcon(), f.getName());
                        this.ctl.Update(Control.UpPrimParam);
                        this.ctl.Edit();
                    }
                    else
                    {
                        Dlg.Error("Should be .png/.bmp/.jpg/.jpeg/.gif");
                    }
                }
                else if (ext.equalsIgnoreCase("knob"))
                {
                    int n = this.ctl.iEdit;
                    if (this.ctl.iEdit != 0 &&
                        Dlg.Confirm("[" + this.ctl.strCurrentFile + "]\n" + this.GetMsgStr("notsaved")) != 2)
                    {
                        n = 0;
                    }
                    if (n == 0)
                    {
                        this.ctl.journal.Write();
                        this.recent.Add(file.getAbsolutePath());
                        this.ctl.LoadExec(file.getAbsolutePath());
                    }
                }
                dtde.getDropTargetContext().dropComplete(true);
            }
            else
            {
                dtde.rejectDrop();
            }
        }
        catch (IOException ioe)
        {
            dtde.rejectDrop();
        }
        catch (UnsupportedFlavorException ufe)
        {
            dtde.rejectDrop();
        }
    }

    @Override public void componentHidden(ComponentEvent e)
    {
    }

    @Override public void componentMoved(ComponentEvent e)
    {
        if (this.getExtendedState() == 0)
        {
            GUIEditor.getInstance().rcWin = this.getBounds();
        }
    }

    @Override public void componentResized(ComponentEvent e)
    {
        GUIEditor.getInstance().prevpanel.ReLayout();
        if (this.getExtendedState() == 0)
        {
            GUIEditor.getInstance().rcWin = this.getBounds();
        }
    }

    @Override public void componentShown(ComponentEvent e)
    {
    }

    class Status extends JLabel
    {
        public Status(String s)
        {
            this.setText(s);
        }

        public void SetText(String s)
        {
            this.setText(s);
            Rectangle r = this.getBounds();
            this.paintImmediately(0, 0, r.width, r.height);
        }
    }
}

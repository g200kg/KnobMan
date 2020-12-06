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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

class DialogConfig extends JDialog implements ActionListener
{
    JComboBox comLaf;
    JComboBox comLang;
    JComboBox comShortcuts;
    JCheckBox cbWheelDir;
    JCheckBox cbUseCapture;
    JCheckBox cbDisableMnemonic;
    JButton btnOk;
    JButton btnRestart;
    JButton btnRegKB;
    JButton btnUnregKB;
    public String ret = null;
    File[] fileLang;
    File[] fileShortcuts;
    GUIEditor editor = GUIEditor.getInstance();

    public DialogConfig(Frame frame)
    {
        super(frame, "JKnobMan", true);
        this.setSize(400, 410);
        this.setResizable(false);
        this.setLayout(null);
        Container c = this.getContentPane();
        JLabel lbVer = new JLabel(Control.getInstance().strVer);
        lbVer.setBounds(20, 10, 220, 27);
        c.add(lbVer);
        JLabel lbJVer = new JLabel(Control.getInstance().strJVer);
        lbJVer.setBounds(20, 27, 220, 27);
        c.add(lbJVer);
        JLabel lbLaf = new JLabel(this.editor.GetMsgStr("lookandfeel"));
        lbLaf.setBounds(20, 60, 140, 27);
        c.add(lbLaf);
        String[] laf = new String[GUIEditor.getInstance().lafs.length];
        for (int i = 0; i < GUIEditor.getInstance().lafs.length; ++i)
        {
            laf[i] = GUIEditor.getInstance().lafs[i].getName();
        }
        this.comLaf = new JComboBox<String>(laf);
        this.comLaf.setBounds(160, 60, 200, 27);
        this.comLaf.setSelectedItem(GUIEditor.getInstance().strLaf);
        this.comLaf.addActionListener(this);
        c.add(this.comLaf);
        JLabel lbLang = new JLabel(this.editor.GetMsgStr("language"));
        lbLang.setBounds(20, 90, 140, 27);
        c.add(lbLang);
        File langdir = new File(new ResFilename("Resource/Lang").GetString());
        this.fileLang = langdir.listFiles();
        String[] lang = null;
        if (this.fileLang != null)
        {
            lang = new String[this.fileLang.length];
            for (int i = 0; i < this.fileLang.length; ++i)
            {
                lang[i] = this.fileLang[i].getName();
            }
        }
        else
        {
            lang = new String[] {""};
        }
        this.comLang = new JComboBox<String>(lang);
        this.comLang.setBounds(160, 90, 200, 27);
        this.comLang.setSelectedItem(GUIEditor.getInstance().strLangIni);
        this.comLang.addActionListener(this);
        c.add(this.comLang);
        JLabel lbShortcuts = new JLabel(this.editor.GetMsgStr("shortcuts"));
        lbShortcuts.setBounds(20, 120, 140, 27);
        c.add(lbShortcuts);
        File shortcutsdir = new File(new ResFilename("Resource/Shortcuts").GetString());
        this.fileShortcuts = shortcutsdir.listFiles();
        String[] shortcuts = null;
        if (this.fileShortcuts != null)
        {
            shortcuts = new String[this.fileShortcuts.length];
            for (int i = 0; i < this.fileShortcuts.length; ++i)
            {
                shortcuts[i] = this.fileShortcuts[i].getName();
            }
        }
        else
        {
            shortcuts = new String[] {""};
        }
        this.comShortcuts = new JComboBox<String>(shortcuts);
        this.comShortcuts.setBounds(160, 120, 200, 27);
        this.comShortcuts.setSelectedItem(GUIEditor.getInstance().strShortcutsIni);
        this.comShortcuts.addActionListener(this);
        c.add(this.comShortcuts);
        this.cbWheelDir = new JCheckBox(this.editor.GetMsgStr("wheeldir"));
        this.cbWheelDir.setBounds(30, 150, 330, 27);
        if (GUIEditor.getInstance().iWheelDir != 0)
        {
            this.cbWheelDir.setSelected(true);
        }
        this.cbWheelDir.addActionListener(this);
        c.add(this.cbWheelDir);
        this.cbUseCapture = new JCheckBox(this.editor.GetMsgStr("usecapture"));
        this.cbUseCapture.setBounds(30, 180, 330, 27);
        if (GUIEditor.getInstance().iUseCapture != 0)
        {
            this.cbUseCapture.setSelected(true);
        }
        this.cbUseCapture.addActionListener(this);
        c.add(this.cbUseCapture);
        this.cbDisableMnemonic = new JCheckBox(this.editor.GetMsgStr("disablemnemo"));
        this.cbDisableMnemonic.setBounds(30, 210, 330, 27);
        if (GUIEditor.getInstance().iDisableMnemonic != 0)
        {
            this.cbDisableMnemonic.setSelected(true);
        }
        this.cbDisableMnemonic.addActionListener(this);
        c.add(this.cbDisableMnemonic);
        this.btnRegKB = new JButton(this.editor.GetMsgStr("regbrowser"));
        this.btnUnregKB = new JButton(this.editor.GetMsgStr("unregbrowser"));
        this.btnRegKB.setBounds(20, 240, 270, 27);
        this.btnUnregKB.setBounds(20, 270, 270, 27);
        this.btnRegKB.addActionListener(this);
        this.btnUnregKB.addActionListener(this);
        if (GUIEditor.getInstance().iOs != 0)
        {
            this.btnRegKB.setEnabled(false);
            this.btnUnregKB.setEnabled(false);
        }
        c.add(this.btnRegKB);
        c.add(this.btnUnregKB);
        this.btnOk = new JButton(this.editor.GetMsgStr("ok"));
        this.btnOk.setBounds(30, 340, 120, 30);
        c.add(this.btnOk);
        this.btnRestart = new JButton(this.editor.GetMsgStr("restart"));
        this.btnRestart.setBounds(160, 340, 120, 30);
        c.add(this.btnRestart);
        this.btnOk.addActionListener(this);
        this.btnRestart.addActionListener(this);
        Rectangle rc = frame.getBounds();
        this.setLocation(rc.x + rc.width / 2 - 150, rc.y + rc.height / 2 - 55);
        this.setVisible(true);
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        String kb = GUIEditor.getInstance().strKnobBrowser;
        if (e.getSource() == this.btnRegKB)
        {
            try
            {
                Runtime.getRuntime().exec("regsvr32.exe \"" + kb + "\"");
            }
            catch (Exception exception)
            {
                // empty catch block
            }
        }
        if (e.getSource() == this.btnUnregKB)
        {
            try
            {
                Runtime.getRuntime().exec("regsvr32.exe /u \"" + kb + "\"");
            }
            catch (Exception exception)
            {
                // empty catch block
            }
        }
        if (e.getSource() == this.comLang)
        {
            GUIEditor.getInstance().strLangIni = this.fileLang[this.comLang.getSelectedIndex()].getName();
        }
        if (e.getSource() == this.comShortcuts)
        {
            GUIEditor.getInstance().strShortcutsIni =
                this.fileShortcuts[this.comShortcuts.getSelectedIndex()].getName();
        }
        if (e.getSource() == this.comLaf)
        {
            GUIEditor.getInstance().strLaf = GUIEditor.getInstance().lafs[this.comLaf.getSelectedIndex()].getName();
        }
        if (e.getSource() == this.cbWheelDir)
        {
            int n = GUIEditor.getInstance().iWheelDir = this.cbWheelDir.isSelected() ? 1 : 0;
        }
        if (e.getSource() == this.cbUseCapture)
        {
            int n = GUIEditor.getInstance().iUseCapture = this.cbUseCapture.isSelected() ? 1 : 0;
        }
        if (e.getSource() == this.cbDisableMnemonic)
        {
            int n = GUIEditor.getInstance().iDisableMnemonic = this.cbDisableMnemonic.isSelected() ? 1 : 0;
        }
        if (e.getSource() == this.btnOk)
        {
            this.dispose();
        }
        if (e.getSource() == this.btnRestart)
        {
            if (Dlg.Confirm(GUIEditor.getInstance().GetMsgStr("notsaved")) == 2)
            {
                return;
            }
            this.dispose();
            GUIEditor.getInstance().SaveConfig();
            GUIEditor.getInstance().dispose();
            System.gc();
            new JKnobMan(null);
        }
    }

    class WindowListener extends WindowAdapter
    {
        WindowListener()
        {
        }

        @Override public void windowClosing(WindowEvent e)
        {
            DialogConfig.this.dispose();
        }
    }
}

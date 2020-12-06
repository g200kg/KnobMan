/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class RecentFiles
{
    String[] strFileName = new String[10];
    JMenu menu;
    int iIDStart;

    public RecentFiles(JMenu menuInit, int iIDStartInit)
    {
        for (int i = 0; i < 10; ++i)
        {
            this.strFileName[i] = "";
        }
        this.menu = menuInit;
        this.iIDStart = iIDStartInit;
    }

    String Get(int i)
    {
        String s = this.strFileName[i];
        for (int j = i; j > 0; --j)
        {
            this.strFileName[j] = this.strFileName[j - 1];
        }
        this.strFileName[0] = s;
        this.Refresh();
        return s;
    }

    void Add(String str)
    {
        if (str != null)
        {
            int i;
            for (i = 0; i < 10; ++i)
            {
                if (!this.strFileName[i].equals(str))
                    continue;
                for (int j = i; j < 9; ++j)
                {
                    this.strFileName[j] = this.strFileName[j + 1];
                }
                this.strFileName[9] = "";
                break;
            }
            for (i = 9; i >= 1; --i)
            {
                this.strFileName[i] = this.strFileName[i - 1];
            }
            this.strFileName[0] = str;
            this.Refresh();
        }
    }

    void Refresh()
    {
        this.menu.removeAll();
        for (int i = 0; i < 8; ++i)
        {
            JMenuItem item = new JMenuItem(String.valueOf(i + 1) + ": " + this.strFileName[i]);
            if (GUIEditor.getInstance().iDisableMnemonic == 0)
            {
                item.setMnemonic(49 + i);
            }
            item.setActionCommand("recent" + i);
            item.addActionListener(GUIEditor.getInstance());
            this.menu.add(item);
        }
    }

    void LoadFromIni(ProfileReader ppr)
    {
        for (int i = 0; i < 8; ++i)
        {
            String s = ppr.ReadString(String.format("%d", i + 1), "");
            this.strFileName[i] = s != null ? s : "";
        }
        this.Refresh();
    }

    void SaveToIni(ProfileWriter ppw)
    {
        for (int i = 0; i < 8; ++i)
        {
            ppw.WriteStr(String.format("%d", i + 1), this.strFileName[i]);
        }
    }
}

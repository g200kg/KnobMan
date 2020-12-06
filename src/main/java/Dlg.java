/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import javax.swing.JOptionPane;

public class Dlg
{
    static final int OK = 0;
    static final int CANCEL = 2;

    static void Message(String msg)
    {
        GUIEditor editor = GUIEditor.getInstance();
        if (editor != null && editor.locale != null)
        {
            JOptionPane.setDefaultLocale(editor.locale);
        }
        JOptionPane.showMessageDialog(editor, msg, "JKnobMan", 1);
    }

    static void Error(String msg)
    {
        GUIEditor editor = GUIEditor.getInstance();
        if (editor.locale != null)
        {
            JOptionPane.setDefaultLocale(editor.locale);
        }
        JOptionPane.showMessageDialog(editor, msg, "JKnobMan", 0);
    }

    static int Confirm(String msg)
    {
        int r;
        GUIEditor editor = GUIEditor.getInstance();
        if (editor.locale != null)
        {
            JOptionPane.setDefaultLocale(editor.locale);
        }
        if ((r = JOptionPane.showConfirmDialog(editor, msg, "JKnobMan", 2, 1)) < 0)
        {
            return 2;
        }
        return r;
    }

    static String Input(String title, String msg, String inival)
    {
        GUIEditor editor = GUIEditor.getInstance();
        if (editor.locale != null)
        {
            JOptionPane.setDefaultLocale(editor.locale);
        }
        return JOptionPane.showInputDialog(editor, msg, inival);
    }
}

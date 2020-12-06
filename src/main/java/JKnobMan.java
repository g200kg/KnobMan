/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class JKnobMan
{
    private static JKnobMan jknobman;
    GUIEditor editor;
    Control control;

    public static void main(String[] args)
    {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        if (args.length > 0)
        {
            new JKnobMan(args[0]);
        }
        else
        {
            new JKnobMan(null);
        }
    }

    public JKnobMan(String arg)
    {
        jknobman = this;
        this.control = new Control();
        this.editor = new GUIEditor(this.control);
        this.control.SelLayer(1);
        if (arg != null)
        {
            this.control.LoadExec(arg);
            GUIEditor.getInstance().recent.Add(arg);
        }
    }

    public static JKnobMan getInstance()
    {
        return jknobman;
    }
}

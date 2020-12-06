/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class Pathname
{
    public static String GetExt(String s)
    {
        int n = s.lastIndexOf(46);
        if (n < 0)
        {
            return "";
        }
        return s.substring(n + 1);
    }

    public static String RemoveExt(String s)
    {
        int n = s.lastIndexOf(46);
        if (n < 0)
        {
            return s;
        }
        return s.substring(0, n);
    }

    public static String AddDefExt(String s, String ext)
    {
        if (Pathname.GetExt(s).equalsIgnoreCase(ext))
        {
            return s;
        }
        return String.valueOf(s) + "." + ext;
    }

    public static String ReplaceExt(String s, String ext)
    {
        return String.valueOf(Pathname.RemoveExt(s)) + "." + ext;
    }

    public static String GetDir(String s)
    {
        int n = s.lastIndexOf(92);
        if (n < 0)
        {
            n = s.lastIndexOf(47);
        }
        if (n < 0)
        {
            return "";
        }
        return s.substring(0, n);
    }

    public static String GetFilename(String s)
    {
        int n = s.lastIndexOf(92);
        if (n < 0)
        {
            n = s.lastIndexOf(47);
        }
        if (n < 0)
        {
            return s;
        }
        if (s.length() > n + 1)
        {
            return s.substring(n + 1);
        }
        return "";
    }
}

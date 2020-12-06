/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class Parse
{
    private static int Skip(String s, int l, int p)
    {
        while (p < l)
        {
            if (s.charAt(p) > ' ')
                break;
            ++p;
        }
        return p;
    }

    public static double Double(String s)
    {
        int l = s.length();
        int p = 0;
        double v = 0.0;
        double frac = 0.0;
        boolean sign = false;
        if ((p = Parse.Skip(s, l, p)) >= l)
        {
            return 0.0;
        }
        if (s.charAt(p) == '-')
        {
            sign = true;
            ++p;
        }
        for (p = Parse.Skip(s, l, p); p < l; ++p)
        {
            char c = s.charAt(p);
            if (c == '.')
            {
                frac = 1.0;
                continue;
            }
            if (!Character.isDigit((int)c))
                break;
            if (frac == 0.0)
            {
                v = v * 10.0 + (double)Character.digit((int)c, 10);
                continue;
            }
            v += (double)Character.digit((int)c, 10) * (frac *= 0.1);
        }
        if (sign)
        {
            return -v;
        }
        return v;
    }

    public static int Int(String s)
    {
        int l = s.length();
        int p = 0;
        int v = 0;
        boolean sign = false;
        if ((p = Parse.Skip(s, l, p)) >= l)
        {
            return 0;
        }
        if (s.charAt(p) == '-')
        {
            sign = true;
            ++p;
        }
        for (p = Parse.Skip(s, l, p); p < l; ++p)
        {
            char c = s.charAt(p);
            if (!Character.isDigit((int)c))
                break;
            v = v * 10 + Character.digit((int)c, 10);
        }
        if (sign)
        {
            return -v;
        }
        return v;
    }
}

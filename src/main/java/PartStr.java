/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class PartStr
{
    public static String Get(String s, int n)
    {
        if (s == null)
        {
            return null;
        }
        int p1 = 0;
        int p2 = s.indexOf(44);
        while (--n >= 0)
        {
            if (p2 < 0)
            {
                return null;
            }
            p1 = p2 + 1;
            p2 = s.indexOf(44, p1);
        }
        if (p2 < 0)
        {
            return s.substring(p1);
        }
        return s.substring(p1, p2);
    }
}

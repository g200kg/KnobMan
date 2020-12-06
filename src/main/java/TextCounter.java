/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class TextCounter
{
    String str;

    public TextCounter(String strInit)
    {
        this.str = strInit;
    }

    public String GetCurrent()
    {
        return this.str;
    }

    public String GetNext()
    {
        String strExt;
        int p = this.str.lastIndexOf(46);
        if (p < 0)
        {
            strExt = "";
            p = this.str.length();
        }
        else
        {
            strExt = this.str.substring(p);
        }
        int n = 0;
        while (p > 0)
        {
            if (this.str.charAt(--p) >= '0' && this.str.charAt(p) <= '9')
            {
                ++n;
                continue;
            }
            ++p;
            break;
        }
        int num = 0;
        if (n > 0)
        {
            num = Integer.parseInt(this.str.substring(p, p + n));
        }
        String strForm = this.str.charAt(p) == '0' ? "%0" + n + "d" : "%d";
        this.str = String.valueOf(this.str.substring(0, p)) + String.format(strForm, num + 1) + strExt;
        return this.str;
    }
}

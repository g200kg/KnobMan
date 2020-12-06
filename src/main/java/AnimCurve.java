/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class AnimCurve
{
    int[] tm = new int[12];
    int[] lv = new int[12];
    ParamI stepreso = new ParamI(0);
    static final int iMaxPoints = 12;

    public AnimCurve()
    {
        this.Reset();
    }

    public void Reset()
    {
        for (int i = 0; i < 12; ++i)
        {
            this.lv[i] = -1;
            this.tm[i] = -1;
        }
        this.lv[0] = 0;
        this.tm[0] = 0;
        this.lv[11] = 100;
        this.tm[11] = 100;
        this.stepreso.val = 0;
    }

    public double GetVal(double ratio)
    {
        int i;
        int iT0 = 0;
        int iL0 = this.lv[0];
        for (i = 1; i < 12; ++i)
        {
            if (this.tm[i] < 0)
                continue;
            int iT1 = this.tm[i];
            int iL1 = this.lv[i];
            if (ratio * 100.0 <= (double)this.tm[i])
            {
                ratio =
                    ((double)iL0 + (double)(iL1 - iL0) * (ratio * 100.0 - (double)iT0) / (double)(iT1 - iT0)) / 100.0;
                break;
            }
            iT0 = iT1;
            iL0 = iL1;
        }
        if (this.stepreso.val == 1)
        {
            ratio = 0.0;
        }
        else if (this.stepreso.val >= 2)
        {
            i = (int)Math.floor(ratio * (double)this.stepreso.val);
            if (i >= this.stepreso.val)
            {
                --i;
            }
            ratio = (double)i / (double)(this.stepreso.val - 1);
        }
        return ratio;
    }
}

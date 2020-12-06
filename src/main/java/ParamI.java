/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class ParamI
{
    int val;
    int min;
    int max;
    int m;

    public ParamI(int defval)
    {
        this.val = defval;
    }

    public void Update(int v)
    {
        this.val = v;
    }
}

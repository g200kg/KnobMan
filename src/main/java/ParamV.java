/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class ParamV
{
    double val;

    public ParamV(double defval)
    {
        this.val = defval;
    }

    public void Update(double v)
    {
        this.val = v;
    }
}

/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class ParamCol
{
    Col col = new Col(0, 0, 0);

    public ParamCol(Col defcol)
    {
        this.col.Copy(defcol);
    }

    public void Update(Col col)
    {
        this.col.Copy(col);
    }
}

/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class IntelliAlpha
{
    static int IntelliAlphaPix(int dwDefault, int dwTarget, int iAlphaStep)
    {
        int b;
        int g;
        int r;
        double a;
        if ((dwDefault &= 0xFFFFFF) == (dwTarget &= 0xFFFFFF))
        {
            return dwDefault;
        }
        int rDefault = dwDefault >> 16 & 0xFF;
        int gDefault = dwDefault >> 8 & 0xFF;
        int bDefault = dwDefault & 0xFF;
        int rTarget = dwTarget >> 16 & 0xFF;
        int gTarget = dwTarget >> 8 & 0xFF;
        int bTarget = dwTarget & 0xFF;
        int piAlpha = 0;
        do
        {
            if ((piAlpha += iAlphaStep) >= 255)
            {
                piAlpha = 255;
                a = 1.0;
            }
            else
            {
                a = (double)piAlpha / 255.0;
            }
            r = (int)(((double)rTarget - (double)rDefault * (1.0 - a)) / a);
            g = (int)(((double)gTarget - (double)gDefault * (1.0 - a)) / a);
            b = (int)(((double)bTarget - (double)bDefault * (1.0 - a)) / a);
        } while (!(a >= 1.0) && (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255));
        return piAlpha << 24 | r << 16 | g << 8 | b;
    }

    static void Process(Bitmap bmpLayer, int dwDefaultColor, int iIntelliAlpha, int iTransparent)
    {
        int iAlphaStep = 0;
        if (bmpLayer == null || bmpLayer.width == 0 || bmpLayer.height == 0)
        {
            return;
        }
        if (iTransparent == 0)
        {
            return;
        }
        if (iTransparent == 1)
        {
            for (int y = 0; y < bmpLayer.height; ++y)
            {
                for (int x = 0; x < bmpLayer.width; ++x)
                {
                    bmpLayer.SetPix(x, y, bmpLayer.GetPix(x, y) | 0xFF000000);
                }
            }
            return;
        }
        if (iIntelliAlpha != 0 && (iAlphaStep = 255 - iIntelliAlpha * 254 / 100) < 16)
        {
            iAlphaStep = 16;
        }
        if (iAlphaStep != 0)
        {
            int x;
            int y;
            for (y = 0; y < bmpLayer.height; ++y)
            {
                for (x = 0; x < bmpLayer.width / 2; ++x)
                {
                    bmpLayer.SetPix(x, y,
                                    IntelliAlpha.IntelliAlphaPix(dwDefaultColor, bmpLayer.GetPix(x, y), iAlphaStep));
                }
            }
            for (y = 0; y < bmpLayer.height; ++y)
            {
                for (x = bmpLayer.width - 1; x >= bmpLayer.width / 2; --x)
                {
                    bmpLayer.SetPix(x, y,
                                    IntelliAlpha.IntelliAlphaPix(dwDefaultColor, bmpLayer.GetPix(x, y), iAlphaStep));
                }
            }
        }
        else
        {
            for (int y = 0; y < bmpLayer.height; ++y)
            {
                for (int x = 0; x < bmpLayer.width; ++x)
                {
                    if (bmpLayer.GetPix(x, y) == dwDefaultColor)
                    {
                        bmpLayer.SetPix(x, y, 0);
                        continue;
                    }
                    bmpLayer.SetPix(x, y, bmpLayer.GetPix(x, y) | 0xFF000000);
                }
            }
        }
    }
}

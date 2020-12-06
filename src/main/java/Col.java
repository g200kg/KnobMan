/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class Col
{
    int a;
    int r;
    int g;
    int b;
    int h;
    int l;
    int s;
    int cnt;

    Col(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
        this.s = 0;
        this.l = 0;
        this.h = 0;
        this.cnt = 0;
    }

    Col(Col c)
    {
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
        this.a = c.a;
        this.s = 0;
        this.l = 0;
        this.h = 0;
        this.cnt = 0;
    }

    Col(int r, int g, int b, int a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.s = 0;
        this.l = 0;
        this.h = 0;
        this.cnt = 0;
    }

    Col(int v)
    {
        this.a = v >> 24 & 0xFF;
        this.r = v >> 16 & 0xFF;
        this.g = v >> 8 & 0xFF;
        this.b = v & 0xFF;
        this.s = 0;
        this.l = 0;
        this.h = 0;
        this.cnt = 0;
    }

    Col(ParamCol c)
    {
        this.r = c.col.r;
        this.g = c.col.g;
        this.b = c.col.b;
        this.a = c.col.a;
        this.s = 0;
        this.l = 0;
        this.h = 0;
        this.cnt = 0;
    }

    public String GetColStr()
    {
        return "#" + String.format("%02x", this.r) + String.format("%02x", this.g) + String.format("%02x", this.b);
    }

    public int htorgb(int n1, int n2, int h)
    {
        while (h < 0)
        {
            h += 240;
        }
        while (h > 240)
        {
            h -= 240;
        }
        if (h < 40)
        {
            return n1 + ((n2 - n1) * h + 20) / 40;
        }
        if (h < 120)
        {
            return n2;
        }
        if (h < 160)
        {
            return n1 + ((n2 - n1) * (160 - h) + 20) / 40;
        }
        return n1;
    }

    public void SetHls(int h, int l, int s)
    {
        this.h = h;
        this.l = l;
        this.s = s;
        if (l > 240)
        {
            l = 240;
        }
        if (l < 0)
        {
            l = 0;
        }
        if (s > 240)
        {
            s = 240;
        }
        if (s <= 0)
        {
            this.g = this.b = l * 255 / 240;
            this.r = this.b;
        }
        else
        {
            int tmp2 = l <= 120 ? (l * (240 + s) + 120) / 240 : l + s - (l * s + 120) / 240;
            int tmp1 = 2 * l - tmp2;
            this.r = (this.htorgb(tmp1, tmp2, h + 80) * 255 + 120) / 240;
            this.g = (this.htorgb(tmp1, tmp2, h) * 255 + 120) / 240;
            this.b = (this.htorgb(tmp1, tmp2, h - 80) * 255 + 120) / 240;
        }
    }

    public void SetRgb(int r, int g, int b)
    {
        int cmax = Math.max(Math.max(r, g), b);
        int cmin = Math.min(Math.min(r, g), b);
        this.r = r;
        this.g = g;
        this.b = b;
        this.l = ((cmax + cmin) * 240 + 255) / 510;
        if (cmax == cmin)
        {
            this.s = 0;
            this.h = 0;
        }
        else
        {
            this.s = this.l < 120 ? ((cmax - cmin) * 240 + (cmax + cmin) / 2) / (cmax + cmin)
                                  : ((cmax - cmin) * 240 + (510 - cmax - cmin) / 2) / (510 - cmax - cmin);
            int rr = ((cmax - this.r) * 40 + (cmax - cmin) / 2) / (cmax - cmin);
            int gg = ((cmax - this.g) * 40 + (cmax - cmin) / 2) / (cmax - cmin);
            int bb = ((cmax - this.b) * 40 + (cmax - cmin) / 2) / (cmax - cmin);
            this.h = this.r == cmax ? bb - gg : (this.g == cmax ? 80 + rr - bb : 160 + gg - rr);
            if (this.h < 0)
            {
                this.h += 240;
            }
            if (this.h > 240)
            {
                this.h -= 240;
            }
        }
    }

    public void Clear()
    {
        this.cnt = 0;
        this.a = 0;
        this.b = 0;
        this.g = 0;
        this.r = 0;
    }

    public void SetCol(Col c)
    {
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
        this.a = c.a;
    }

    public void SetInt(int v)
    {
        this.a = v >> 24 & 0xFF;
        this.r = v >> 16 & 0xFF;
        this.g = v >> 8 & 0xFF;
        this.b = v & 0xFF;
    }

    public void hlstorgb()
    {
        this.SetHls(this.h, this.l, this.s);
    }

    public void rgbtohls()
    {
        this.SetRgb(this.r, this.g, this.b);
    }

    public void ChangeBrightness(int n)
    {
        this.r += n;
        this.r = Math.max(0, Math.min(255, this.r));
        this.g += n;
        this.g = Math.max(0, Math.min(255, this.g));
        this.b += n;
        this.b = Math.max(0, Math.min(255, this.b));
    }

    public void ChangeBrightness(double n)
    {
        this.ChangeBrightness((int)n);
    }

    public void BlendTo(int r, int g, int b, int a)
    {
        this.r = (this.r * (256 - a) + r * a) / 256;
        this.g = (this.g * (256 - a) + g * a) / 256;
        this.b = (this.b * (256 - a) + b * a) / 256;
    }

    public void Bright(int a)
    {
        this.r = this.r * a / 255;
        this.g = this.g * a / 255;
        this.b = this.b * a / 255;
    }

    public void Copy(Col c)
    {
        this.r = c.r;
        this.g = c.g;
        this.b = c.b;
        this.a = c.a;
    }

    public void Blend4(Col c01, Col c10, Col c11, double x, double y)
    {
        double x0 = 1.0 - x;
        double y0 = 1.0 - y;
        double xy00 = x0 * y0 * (double)this.a;
        double xy01 = x * y0 * (double)c01.a;
        double xy10 = x0 * y * (double)c10.a;
        double xy11 = x * y * (double)c11.a;
        double at = xy00 + xy01 + xy10 + xy11;
        if (at != 0.0)
        {
            xy00 /= at;
            xy01 /= at;
            xy10 /= at;
            xy11 /= at;
        }
        else
        {
            xy11 = 0.0;
            xy10 = 0.0;
            xy01 = 0.0;
            xy00 = 0.0;
        }
        double rr = (double)this.r * xy00 + (double)c01.r * xy01 + (double)c10.r * xy10 + (double)c11.r * xy11;
        double gg = (double)this.g * xy00 + (double)c01.g * xy01 + (double)c10.g * xy10 + (double)c11.g * xy11;
        double bb = (double)this.b * xy00 + (double)c01.b * xy01 + (double)c10.b * xy10 + (double)c11.b * xy11;
        double aa = (double)this.a * x0 * y0 + (double)c01.a * x * y0 + (double)c10.a * x0 * y + (double)c11.a * x * y;
        this.a = (int)aa;
        this.r = (int)rr;
        this.g = (int)gg;
        this.b = (int)bb;
    }

    public void Conv(double b, double c, double s, double h)
    {
        h *= 0.6666666666666666;
        this.rgbtohls();
        this.l = (int)((double)(this.l - 120) * (c + 100.0) / 100.0 + 120.0);
        this.l = (int)((double)this.l + b * 240.0 / 100.0);
        if (this.l >= 240)
        {
            this.l = 239;
        }
        if (this.l < 0)
        {
            this.l = 0;
        }
        this.s = (int)((double)this.s * (s + 100.0) / 100.0);
        this.h = (int)((double)this.h + h);
        this.SetHls(this.h, this.l, this.s);
    }

    public void GetPix(Bitmap img, int x, int y)
    {
        int val = img.GetPix(x, y);
        this.a = val >> 24 & 0xFF;
        this.r = val >> 16 & 0xFF;
        this.g = val >> 8 & 0xFF;
        this.b = val & 0xFF;
    }

    public void SetPix(Bitmap img, int x, int y)
    {
        img.SetPix(x, y, this);
    }

    public void AddPix(Bitmap img, int x, int y)
    {
        int vald = img.GetPix(x, y);
        int a0 = vald >> 24 & 0xFF;
        if (this.a >= 255 || a0 == 0)
        {
            img.SetPix(x, y, this);
        }
        else
        {
            int r0 = vald >> 16 & 0xFF;
            int g0 = vald >> 8 & 0xFF;
            int b0 = vald & 0xFF;
            int a = 255 - (255 - a0) * (255 - this.a) / 255;
            int aa = a0 * (255 - this.a) + this.a * 255;
            int r = (r0 * a0 * (255 - this.a) + this.r * this.a * 255) / aa;
            int g = (g0 * a0 * (255 - this.a) + this.g * this.a * 255) / aa;
            int b = (b0 * a0 * (255 - this.a) + this.b * this.a * 255) / aa;
            img.SetPix(x, y, a, r, g, b);
        }
    }

    public void MulAddPix(Bitmap img, Bitmap imgMask, int dx, int dy, int mx, int my)
    {
        int valm = imgMask.GetPix(mx, my);
        int vald = img.GetPix(dx, dy);
        this.a = this.a * (valm >> 24 & 0xFF) / 255;
        int a0 = vald >> 24 & 0xFF;
        if (this.a >= 255 || a0 == 0)
        {
            img.SetPix(dx, dy, this);
        }
        else
        {
            int r0 = vald >> 16 & 0xFF;
            int g0 = vald >> 8 & 0xFF;
            int b0 = vald & 0xFF;
            int a = 255 - (255 - a0) * (255 - this.a) / 255;
            int aa = a0 * (255 - this.a) + this.a * 255;
            int r = (r0 * a0 * (255 - this.a) + this.r * this.a * 255) / aa;
            int g = (g0 * a0 * (255 - this.a) + this.g * this.a * 255) / aa;
            int b = (b0 * a0 * (255 - this.a) + this.b * this.a * 255) / aa;
            img.SetPix(dx, dy, a, r, g, b);
        }
    }

    public void Reset()
    {
        this.cnt = 0;
        this.a = 0;
        this.b = 0;
        this.g = 0;
        this.r = 0;
    }

    public void AddVal(Bitmap img, int x, int y)
    {
        int val = img.GetPix(x, y);
        this.a += val >> 24 & 0xFF;
        this.r += val >> 16 & 0xFF;
        this.g += val >> 8 & 0xFF;
        this.b += val & 0xFF;
        ++this.cnt;
    }

    public void SetAve(Bitmap img, int x, int y)
    {
        img.SetPix(x, y, this.a / this.cnt, this.r / this.cnt, this.g / this.cnt, this.b / this.cnt);
    }

    public void SetAve()
    {
        if (this.cnt != 0)
        {
            this.a /= this.cnt;
            this.r /= this.cnt;
            this.g /= this.cnt;
            this.b /= this.cnt;
        }
    }
}

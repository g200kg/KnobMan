/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

public class Tex
{
    String name;
    int width;
    int height;
    int zwidth;
    int zheight;
    int init = 0;
    Bitmap bmp;
    Bitmap zbmp;
    int[] pdata = null;
    int[] adata;
    int[] zpdata;
    int[] zadata;
    double zoom = -1.0;

    public Tex(String name)
    {
        this.name = name;
        this.zwidth = 0;
        this.width = 0;
        this.zheight = 0;
        this.height = 0;
        this.bmp = null;
    }

    public Tex(Bitmap bmp, String name)
    {
        this.name = name;
        this.width = this.zwidth = bmp.width;
        this.height = this.zheight = bmp.height;
        this.bmp = bmp;
    }

    public Tex Clone()
    {
        return new Tex(this.bmp, this.name);
    }

    public int Get(double x, double y, double z)
    {
        int ind;
        int xx;
        int yy;
        if (this.init == 0)
        {
            this.init = 1;
            if (this.bmp == null)
            {
                this.zbmp = this.bmp = new Bitmap(this.name);
            }
            this.width = this.zwidth = this.bmp.width;
            this.height = this.zheight = this.bmp.height;
            this.zoom = -1.0;
            this.pdata = new int[(this.width + 2) * (this.height + 2)];
            this.adata = new int[(this.width + 2) * (this.height + 2)];
            this.zpdata = new int[(this.zwidth + 1) * (this.zheight + 1)];
            this.zadata = new int[(this.zwidth + 1) * (this.zheight + 1)];
            int p = 0;
            for (yy = 0; yy < this.height + 2; ++yy)
            {
                for (xx = 0; xx < this.width + 2; ++xx)
                {
                    ind = yy * (this.width + 2) + xx;
                    int c = this.bmp.GetPix(xx % this.width, yy % this.height);
                    int l = ((c >> 16 & 0xFF) * 3 + (c >> 8 & 0xFF) * 6 + (c & 0xFF)) / 10;
                    this.pdata[ind] = Math.min(255, l);
                    this.adata[ind] = c >> 24 & 0xFF;
                    p += 4;
                }
            }
        }
        int r = 1;
        if (z <= 50.0)
        {
            r = 2;
        }
        if ((double)r != this.zoom)
        {
            this.zwidth = this.width / r;
            this.zheight = this.height / r;
            for (yy = 0; yy < this.zheight + 1; ++yy)
            {
            block7:
                for (xx = 0; xx < this.zwidth + 1; ++xx)
                {
                    ind = yy * (this.zwidth + 1) + xx;
                    int ind2 = yy * r * (this.width + 2) + xx * r;
                    switch (r)
                    {
                    case 1: {
                        this.zpdata[ind] = this.pdata[ind2];
                        this.zadata[ind] = this.adata[ind2];
                        continue block7;
                    }
                    case 2: {
                        this.zpdata[ind] = (this.pdata[ind2] + this.pdata[ind2 + 1] +
                                            this.pdata[ind2 + this.width + 2] + this.pdata[ind2 + this.width + 3]) /
                                           4;
                        this.zadata[ind] = (this.adata[ind2] + this.adata[ind2 + 1] +
                                            this.adata[ind2 + this.width + 2] + this.adata[ind2 + this.width + 3]) /
                                           4;
                    }
                    }
                }
            }
            this.zoom = r;
        }
        if (z == 0.0 || this.width == 0)
        {
            return -16776961;
        }
        x = (x + 0.5) * 100.0 / (z *= (double)r) + (double)this.zwidth * 0.5;
        y = (y + 0.5) * 100.0 / z + (double)this.zheight * 0.5;
        x -= Math.floor(x / (double)this.zwidth) * (double)this.zwidth;
        y -= Math.floor(y / (double)this.zheight) * (double)this.zheight;
        int ix = (int)x;
        int iy = (int)y;
        double fx = x - (double)ix;
        double fy = y - (double)iy;
        double p0 = (double)this.zpdata[iy * (this.zwidth + 1) + ix] * (1.0 - fx) +
                    (double)this.zpdata[iy * (this.zwidth + 1) + ix + 1] * fx;
        double p1 = (double)this.zpdata[(iy + 1) * (this.zwidth + 1) + ix] * (1.0 - fx) +
                    (double)this.zpdata[(iy + 1) * (this.zwidth + 1) + ix + 1] * fx;
        double a0 = (double)this.zadata[iy * (this.zwidth + 1) + ix] * (1.0 - fx) +
                    (double)this.zadata[iy * (this.zwidth + 1) + ix + 1] * fx;
        double a1 = (double)this.zadata[(iy + 1) * (this.zwidth + 1) + ix] * (1.0 - fx) +
                    (double)this.zadata[(iy + 1) * (this.zwidth + 1) + ix + 1] * fx;
        int pp = (int)(p0 * (1.0 - fy) + p1 * fy);
        int aa = (int)(a0 * (1.0 - fy) + a1 * fy);
        return (aa << 24) + pp;
    }

    public ImageIcon GetImageIcon()
    {
        double z = 100.0;
        this.Get(0.0, 0.0, 100.0);
        if (this.width >= 32 && this.height >= 32)
        {
            z = 50.0;
        }
        BufferedImage bimg = new BufferedImage(32, 20, 6);
        for (int y = 0; y < 20; ++y)
        {
            for (int x = 0; x < 32; ++x)
            {
                int c = this.Get((double)x - 15.5, (double)y - 9.5, z);
                bimg.setRGB(x, y, (c & 0xFF000000) + ((c & 0xFF) << 16));
            }
        }
        return new ImageIcon(bimg);
    }
}

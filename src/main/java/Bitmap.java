/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class Bitmap
{
    public int width;
    public int height;
    BufferedImage img;
    byte[] binOrg = null;

    Bitmap(int wini, int hini)
    {
        this.width = wini;
        this.height = hini;
        this.img = new BufferedImage(this.width, this.height, 6);
    }

    Bitmap(String name)
    {
        File file = new File(name);
        this.binOrg = new byte[(int)file.length()];
        try
        {
            InputStream is = new FileInputStream(file);
            is.read(this.binOrg);
            is.close();
            is = new ByteArrayInputStream(this.binOrg);
            this.img = ImageIO.read(is);
            is.close();
            if (this.img == null)
            {
                this.height = 0;
                this.width = 0;
                return;
            }
            this.width = this.img.getWidth();
            this.height = this.img.getHeight();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.height = 0;
            this.width = 0;
            this.img = null;
        }
    }

    Bitmap(byte[] b)
    {
        this.binOrg = b;
        ByteArrayInputStream in = new ByteArrayInputStream(b);
        try
        {
            this.img = ImageIO.read(in);
            this.width = this.img.getWidth();
            this.height = this.img.getHeight();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            this.img = null;
        }
    }

    Bitmap(Bitmap b)
    {
        this.width = b.width;
        this.height = b.height;
        if (b.width * b.height > 0)
        {
            this.img = new BufferedImage(this.width, this.height, 6);
            this.CopyFrom(b, this.width, this.height);
        }
    }

    Bitmap(Image im)
    {
        if (im != null)
        {
            if (im.getWidth(null) <= 0)
            {
                this.height = 0;
                this.width = 0;
                this.img = null;
                return;
            }
            this.width = im.getWidth(null);
            this.height = im.getWidth(null);
            this.img = new BufferedImage(this.width, this.height, 6);
            Graphics2D g2 = (Graphics2D)this.img.getGraphics();
            g2.drawImage(im, 0, 0, null);
        }
    }

    private IndexColorModel createIndexColorModel()
    {
        int[] cmap = new int[256];
        int i = 0;
        for (int r = 0; r < 256; r += 51)
        {
            for (int g = 0; g < 256; g += 51)
            {
                for (int b = 0; b < 256; b += 51)
                {
                    cmap[i++] = r << 16 | g << 8 | b;
                }
            }
        }
        int grayIncr = 256 / (256 - i);
        int gray = grayIncr * 3;
        while (i < 256)
        {
            cmap[i] = gray << 16 | gray << 8 | gray;
            gray += grayIncr;
            ++i;
        }
        return new IndexColorModel(8, 256, cmap, 0, true, 215, 0);
    }

    public void Write(String fname, String format, int x, int y, int w, int h)
    {
        try
        {
            if (format.equals("png"))
            {
                ImageIO.write((RenderedImage)this.img, "png", new File(fname));
            }
            else if (format.equals("gif"))
            {
                BufferedImage img = new BufferedImage(this.width, this.height, 13, this.createIndexColorModel());
                Graphics2D g2 = (Graphics2D)img.getGraphics();
                g2.drawImage((Image)this.img, 0, 0, null);
                ImageIO.write((RenderedImage)img, format, new File(fname));
            }
            else if (format.equals("tga"))
            {
                byte[] header = new byte[18];
                FileOutputStream fo = new FileOutputStream(fname);
                for (int i = 0; i < header.length; ++i)
                {
                    header[i] = 0;
                }
                header[2] = 2;
                header[12] = (byte)(w & 0xFF);
                header[13] = (byte)(w >> 8 & 0xFF);
                header[14] = (byte)(h & 0xFF);
                header[15] = (byte)(h >> 8 & 0xFF);
                header[16] = 32;
                header[17] = 40;
                fo.write(header);
                for (int yy = 0; yy < h; ++yy)
                {
                    for (int xx = 0; xx < w; ++xx)
                    {
                        int c = this.GetPix(x + xx, y + yy);
                        fo.write(c & 0xFF);
                        fo.write(c >> 8 & 0xFF);
                        fo.write(c >> 16 & 0xFF);
                        fo.write(c >> 24 & 0xFF);
                    }
                }
                fo.close();
            }
            else
            {
                BufferedImage img = new BufferedImage(this.width, this.height, 5);
                Graphics2D g2 = (Graphics2D)img.getGraphics();
                g2.drawImage((Image)this.img, 0, 0, null);
                ImageIO.write((RenderedImage)img, format, new File(fname));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Write(String file, String format)
    {
        this.Write(file, format, 0, 0, this.width, this.height);
    }

    public byte[] GetBytes(String format)
    {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try
        {
            ImageIO.write((RenderedImage)this.img, format, b);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return b.toByteArray();
    }

    public void ClearRect(int x, int y, int w, int h, Color c)
    {
        Graphics2D g2 = (Graphics2D)this.img.getGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setColor(c);
        g2.fillRect(x, y, w, h);
    }

    public void Clear(Color c)
    {
        this.ClearRect(0, 0, this.width, this.height, c);
    }

    public void Clear()
    {
        this.ClearRect(0, 0, this.width, this.height, new Color(0, 0, 0, 0));
    }

    public void CopyFrom(Bitmap bmp, int w, int h)
    {
        Graphics2D g2 = (Graphics2D)this.img.getGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.drawImage(bmp.img, 0, 0, w, h, null);
    }

    public void Draw(Bitmap bmp, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2)
    {
        Graphics g = this.img.getGraphics();
        g.drawImage(bmp.img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
    }

    public void DecimationFrom(BufferedImage img, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh)
    {
        Graphics2D g2 = (Graphics2D)this.img.getGraphics();
        BufferedImage bimg = img.getSubimage(sx, sy, sw, sh);
        Image bimg2 = bimg.getScaledInstance(dw, dh, 4);
        g2.setBackground(new Color(0, 0, 0, 0));
        g2.clearRect(dx, dy, dw, dh);
        g2.drawImage(bimg2, dx, dy, null);
    }

    public void DecimationTo(Bitmap bmpDest, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh)
    {
        Graphics2D g2 = (Graphics2D)bmpDest.img.getGraphics();
        BufferedImage bimg = this.img.getSubimage(sx, sy, sw, sh);
        Image bimg2 = bimg.getScaledInstance(dw, dh, 4);
        g2.setBackground(new Color(0, 0, 0, 0));
        g2.clearRect(dx, dy, dw, dh);
        g2.drawImage(bimg2, dx, dy, null);
    }

    public void Decimation2(Bitmap bmpDest, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh)
    {
        int lssy = sy;
        for (int y = 0; y < dh; ++y)
        {
            int ssy = sy + sh * (y + 1) / dh;
            int lssx = sx;
            for (int x = 0; x < dw; ++x)
            {
                int ssx = sx + sw * (x + 1) / dw;
                int bb = 0;
                int gg = 0;
                int rr = 0;
                int aa = 0;
                int n2 = 0;
                int yy = lssy;
                do
                {
                    int xx = lssx;
                    do
                    {
                        int c = this.GetPix(xx, yy);
                        int a = c >> 24 & 0xFF;
                        aa += a;
                        rr += (c >> 16 & 0xFF) * a;
                        gg += (c >> 8 & 0xFF) * a;
                        bb += (c & 0xFF) * a;
                        ++n2;
                    } while (++xx < ssx);
                } while (++yy < ssy);
                if (n2 != 0 && aa != 0)
                {
                    bmpDest.SetPix(dx + x, dy + y, aa / n2, rr / aa, gg / aa, bb / aa);
                }
                else
                {
                    bmpDest.SetPix(dx + x, dy + y, 0);
                }
                lssx = ssx;
            }
            lssy = ssy;
        }
    }

    public void Decimation(Bitmap bmpDest, int dx, int dy, int dw, int dh, int sx, int sy, int sw, int sh, int colorkey)
    {
        int lssy = sy;
        for (int y = 0; y < dh; ++y)
        {
            int ssy = sy + sh * (y + 1) / dh;
            int lssx = sx;
            for (int x = 0; x < dw; ++x)
            {
                int ssx = sx + sw * (x + 1) / dw;
                int bb = 0;
                int gg = 0;
                int rr = 0;
                int aa = 0;
                int n2 = 0;
                int yy = lssy;
                do
                {
                    int xx = lssx;
                    do
                    {
                        int c;
                        if ((c = this.GetPix(xx, yy)) == colorkey)
                            continue;
                        aa += 255;
                        rr += (c >> 16 & 0xFF) * 255;
                        gg += (c >> 8 & 0xFF) * 255;
                        bb += (c & 0xFF) * 255;
                        ++n2;
                    } while (++xx < ssx);
                } while (++yy < ssy);
                if (n2 != 0 && aa != 0)
                {
                    bmpDest.SetPix(dx + x, dy + y, aa / n2, rr / aa, gg / aa, bb / aa);
                }
                else
                {
                    bmpDest.SetPix(dx + x, dy + y, 0);
                }
                lssx = ssx;
            }
            lssy = ssy;
        }
    }

    public int GetPix(int x, int y)
    {
        return this.img.getRGB(x, y);
    }

    public void SetPix(int x, int y, int a, int r, int g, int b)
    {
        int val = (a << 24) + (r << 16) + (g << 8) + b;
        this.img.setRGB(x, y, val);
    }

    public void SetPix(int x, int y, int v)
    {
        this.img.setRGB(x, y, v);
    }

    public void SetPix(int x, int y, Col c)
    {
        this.img.setRGB(x, y, (c.a << 24) + (c.r << 16) + (c.g << 8) + c.b);
    }

    public void SetAlpha(int x, int y, int a)
    {
        int val = this.img.getRGB(x, y);
        this.img.setRGB(x, y, (val & 0xFFFFFF) + (a << 24));
    }

    public int GetAlpha(int x, int y)
    {
        int val = this.img.getRGB(x, y);
        return val >> 24 & 0xFF;
    }
}

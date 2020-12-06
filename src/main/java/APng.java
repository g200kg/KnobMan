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
import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;

public class APng
{
    Bitmap bmp;
    Bitmap bmp1;
    Crc crc;
    int frames;
    int horz;
    int width1;
    int height1;

    static void SetDword(byte[] b, int ipos, int d)
    {
        b[ipos] = (byte)(d >> 24 & 0xFF);
        b[ipos + 1] = (byte)(d >> 16 & 0xFF);
        b[ipos + 2] = (byte)(d >> 8 & 0xFF);
        b[ipos + 3] = (byte)(d & 0xFF);
    }

    static void SetWord(byte[] b, int ipos, int d)
    {
        b[ipos] = (byte)(d >> 8 & 0xFF);
        b[ipos + 1] = (byte)(d & 0xFF);
    }

    static int GetDword(byte[] b, int p)
    {
        return b[p] << 24 & 0xFF000000 | b[p + 1] << 16 & 0xFF0000 | b[p + 2] << 8 & 0xFF00 | b[p + 3] & 0xFF;
    }

    static void WriteDword(FileOutputStream fo, int dw)
    {
        try
        {
            fo.write(dw >> 24 & 0xFF);
            fo.write(dw >> 16 & 0xFF);
            fo.write(dw >> 8 & 0xFF);
            fo.write(dw & 0xFF);
        }
        catch (IOException iOException)
        {
            // empty catch block
        }
    }

    static int FindChunk(byte[] b, int dwName)
    {
        int p = 0;
        p += 8;
        while (true)
        {
            int dwLen = APng.GetDword(b, p);
            int dw = APng.GetDword(b, p + 4);
            if (dw == dwName)
            {
                return p;
            }
            if (dw == 1229278788)
            {
                return 0;
            }
            p += dwLen + 12;
        }
    }

    APng(Bitmap bmp, int frames, int horz)
    {
        this.bmp = bmp;
        this.frames = frames;
        this.horz = horz;
        this.crc = new Crc();
        if (horz != 0)
        {
            this.width1 = bmp.width / frames;
            this.height1 = bmp.height;
        }
        else
        {
            this.width1 = bmp.width;
            this.height1 = bmp.height / frames;
        }
        this.bmp1 = new Bitmap(this.width1, this.height1);
    }

    public void Write(String name, int loop, int duration, int bidir)
    {
        byte[] sig = new byte[] {-119, 80, 78, 71, 13, 10, 26, 10};
        byte[] arrby = new byte[25];
        arrby[3] = 13;
        arrby[4] = 73;
        arrby[5] = 72;
        arrby[6] = 68;
        arrby[7] = 82;
        arrby[16] = 8;
        arrby[17] = 6;
        byte[] ihdr = arrby;
        byte[] arrby2 = new byte[20];
        arrby2[3] = 8;
        arrby2[4] = 97;
        arrby2[5] = 99;
        arrby2[6] = 84;
        arrby2[7] = 76;
        byte[] actl = arrby2;
        byte[] arrby3 = new byte[38];
        arrby3[3] = 26;
        arrby3[4] = 102;
        arrby3[5] = 99;
        arrby3[6] = 84;
        arrby3[7] = 76;
        arrby3[32] = 1;
        byte[] fctl = arrby3;
        byte[] arrby4 = new byte[12];
        arrby4[4] = 102;
        arrby4[5] = 100;
        arrby4[6] = 65;
        arrby4[7] = 84;
        byte[] fdat = arrby4;
        byte[] arrby5 = new byte[12];
        arrby5[4] = 73;
        arrby5[5] = 69;
        arrby5[6] = 78;
        arrby5[7] = 68;
        arrby5[8] = -82;
        arrby5[9] = 66;
        arrby5[10] = 96;
        arrby5[11] = -126;
        byte[] iend = arrby5;
        Graphics2D g2 = (Graphics2D)this.bmp1.img.getGraphics();
        g2.setComposite(AlphaComposite.Src);
        try
        {
            FileOutputStream fp = new FileOutputStream(name);
            int iNumImage = this.frames;
            if (bidir != 0 && iNumImage > 2)
            {
                iNumImage = iNumImage * 2 - 2;
            }
            fp.write(sig);
            APng.SetDword(ihdr, 8, this.width1);
            APng.SetDword(ihdr, 12, this.height1);
            APng.SetDword(ihdr, 21, this.crc.Calc(ihdr, 4, 17));
            fp.write(ihdr);
            APng.SetDword(actl, 8, iNumImage);
            APng.SetDword(actl, 12, loop);
            APng.SetDword(actl, 16, this.crc.Calc(actl, 4, 12));
            fp.write(actl);
            int iSeq = 0;
            for (int i = 0; i < iNumImage; ++i)
            {
                int j = i;
                if (i > this.frames)
                {
                    j = this.frames * 2 - i - 2;
                }
                APng.SetDword(fctl, 8, iSeq++);
                APng.SetDword(fctl, 12, this.width1);
                APng.SetDword(fctl, 16, this.height1);
                APng.SetWord(fctl, 28, duration);
                APng.SetWord(fctl, 30, 1000);
                APng.SetDword(fctl, 34, this.crc.Calc(fctl, 4, 30));
                fp.write(fctl);
                if (this.horz != 0)
                {
                    g2.drawImage(this.bmp.img, 0, 0, this.width1, this.height1, this.width1 * j, 0,
                                 this.width1 * (j + 1), this.height1, null);
                }
                else
                {
                    g2.drawImage(this.bmp.img, 0, 0, this.width1, this.height1, 0, this.height1 * j, this.width1,
                                 this.height1 * (j + 1), null);
                }
                byte[] b = this.bmp1.GetBytes("PNG");
                int pChunk = APng.FindChunk(b, 1229209940);
                if (i == 0)
                {
                    fp.write(b, pChunk, APng.GetDword(b, pChunk) + 12);
                    continue;
                }
                int dwLen = APng.GetDword(b, pChunk);
                APng.SetDword(fdat, 0, dwLen + 4);
                APng.SetDword(fdat, 8, iSeq++);
                fp.write(fdat);
                int dwCRC = this.crc.Calc(fdat, 4, 8);
                fp.write(b, pChunk + 8, dwLen);
                dwCRC = this.crc.Update(dwCRC, b, pChunk + 8, dwLen);
                APng.WriteDword(fp, dwCRC);
            }
            APng.SetDword(iend, 8, this.crc.Calc(iend, 4, 4));
            fp.write(iend);
            fp.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    class Crc
    {
        int[] dwTab = new int[256];

        Crc()
        {
            for (int i = 0; i < 256; ++i)
            {
                int c = i;
                for (int k = 0; k < 8; ++k)
                {
                    c = (c & 1) != 0 ? 0xEDB88320 ^ c >> 1 & Integer.MAX_VALUE : c >> 1 & Integer.MAX_VALUE;
                }
                this.dwTab[i] = c;
            }
        }

        int Update(int crc, byte[] buf, int start, int len)
        {
            int c = ~crc;
            for (int i = start; i < start + len; ++i)
            {
                c = this.dwTab[(c ^ buf[i]) & 0xFF] ^ c >> 8 & 0xFFFFFF;
            }
            return ~c;
        }

        int Calc(byte[] buf, int start, int len)
        {
            return this.Update(0, buf, start, len);
        }
    }
}

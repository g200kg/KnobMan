/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Graphics2D;
import java.io.FileOutputStream;
import java.io.IOException;

public class AnimGif
{
    Bitmap bmp;
    Bitmap bmp1;
    int frames;
    int horz;
    int width1;
    int height1;

    public AnimGif(Bitmap bmp, int frames, int horz)
    {
        this.bmp = bmp;
        this.frames = frames;
        this.horz = horz;
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

    static void SetWordLE(byte[] b, int ipos, int d)
    {
        b[ipos] = (byte)(d & 0xFF);
        b[ipos + 1] = (byte)(d >> 8 & 0xFF);
    }

    public void Write(String name, int loop, int duration, int bidir)
    {
        byte[] arrby = new byte[13];
        arrby[0] = 71;
        arrby[1] = 73;
        arrby[2] = 70;
        arrby[3] = 56;
        arrby[4] = 57;
        arrby[5] = 97;
        arrby[10] = 112;
        byte[] header = arrby;
        byte[] arrby2 = new byte[19];
        arrby2[0] = 33;
        arrby2[1] = -1;
        arrby2[2] = 11;
        arrby2[3] = 78;
        arrby2[4] = 69;
        arrby2[5] = 84;
        arrby2[6] = 83;
        arrby2[7] = 67;
        arrby2[8] = 65;
        arrby2[9] = 80;
        arrby2[10] = 69;
        arrby2[11] = 50;
        arrby2[12] = 46;
        arrby2[13] = 48;
        arrby2[14] = 3;
        arrby2[15] = 1;
        arrby2[16] = -1;
        arrby2[17] = -1;
        byte[] appex = arrby2;
        byte[] arrby3 = new byte[8];
        arrby3[0] = 33;
        arrby3[1] = -7;
        arrby3[2] = 4;
        byte[] grex = arrby3;
        byte[] arrby4 = new byte[10];
        arrby4[0] = 44;
        arrby4[9] = -121;
        byte[] imb = arrby4;
        byte[] imbEnd = new byte[2];
        byte[] trailer = new byte[] {59};
        Graphics2D g2 = (Graphics2D)this.bmp1.img.getGraphics();
        try
        {
            FileOutputStream fp = new FileOutputStream(name);
            AnimGif.SetWordLE(header, 6, this.width1);
            AnimGif.SetWordLE(header, 8, this.height1);
            fp.write(header);
            AnimGif.SetWordLE(appex, 16, loop);
            fp.write(appex);
            int iNumImage = this.frames;
            if (bidir != 0 && iNumImage > 2)
            {
                iNumImage = iNumImage * 2 - 2;
            }
        block2:
            for (int i = 0; i < iNumImage; ++i)
            {
                int j = i;
                if (i >= this.frames)
                {
                    j = this.frames * 2 - i - 2;
                }
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
                byte[] b = this.bmp1.GetBytes("GIF");
                AnimGif.SetWordLE(grex, 4, duration / 10);
                fp.write(grex);
                AnimGif.SetWordLE(imb, 5, this.width1);
                AnimGif.SetWordLE(imb, 7, this.height1);
                int sgct = (1 << (b[10] & 7) + 1) * 3;
                int iOff = 13 + sgct;
                while (b[iOff] == 33)
                {
                    iOff += (b[iOff + 2] & 0xFF) + 4;
                }
                imb[9] = (byte)(0x80 | b[iOff + 9] & 0x47);
                fp.write(imb);
                fp.write(b, 13, sgct);
                fp.write(b, iOff += 10, 1);
                ++iOff;
                while (true)
                {
                    int iLen = b[iOff] & 0xFF;
                    fp.write(b, iOff, iLen + 1);
                    if (iLen == 0)
                        continue block2;
                    iOff += iLen + 1;
                }
            }
            fp.write(trailer);
            fp.close();
        }
        catch (IOException iOException)
        {
            // empty catch block
        }
    }
}

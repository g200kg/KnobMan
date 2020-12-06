/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class ProfileWriter
{
    OutputStream os;
    Writer fw;

    public ProfileWriter(String strFile, String enc)
    {
        try
        {
            this.os = new FileOutputStream(strFile);
            this.fw = new OutputStreamWriter(this.os, enc);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Close()
    {
        try
        {
            this.fw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void WriteHeader(byte[] b)
    {
        try
        {
            if (b == null)
            {
                byte[] arrby = new byte[6];
                arrby[0] = 75;
                arrby[1] = 77;
                arrby[2] = 6;
                this.os.write(arrby);
            }
            else
            {
                int l = b.length;
                b[0] = 75;
                b[1] = 77;
                b[2] = (byte)(l & 0xFF);
                b[3] = (byte)(l >> 8 & 0xFF);
                b[4] = (byte)(l >> 16 & 0xFF);
                b[5] = (byte)(l >> 24 & 0xFF);
                this.os.write(b);
            }
            this.os.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void WriteBOM()
    {
        try
        {
            this.fw.write(65279);
            this.fw.write(13);
            this.fw.write(10);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void Section(String strSection)
    {
        try
        {
            this.fw.write("[" + strSection + "]\r\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void WriteStr(String strKey, String strValue)
    {
        try
        {
            if (strValue == null)
            {
                this.fw.write(String.valueOf(strKey) + "=\r\n");
            }
            else
            {
                this.fw.write(String.valueOf(strKey) + "=" + strValue + "\r\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void WriteInt(String strKey, int iValue)
    {
        try
        {
            this.fw.write(String.valueOf(strKey) + "=" + Integer.toString(iValue) + "\r\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void WriteFloat(String strKey, double fValue)
    {
        try
        {
            this.fw.write(String.valueOf(strKey) + "=" + Double.toString(fValue) + "\r\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void EmbedImage(String strKeyBase, Bitmap bmp)
    {
        byte[] by = bmp.binOrg != null ? bmp.binOrg : bmp.GetBytes("PNG");
        int iLCount = 0;
        int iBCount = 0;
        while (true)
        {
            String strLine = String.valueOf(strKeyBase) + Integer.toString(iLCount) + "=";
            for (int i = 0; i < 256; ++i)
            {
                strLine = String.valueOf(strLine) + String.format("%02x", by[iBCount++]);
                if (iBCount >= by.length)
                    break;
            }
            strLine = String.valueOf(strLine) + "\r\n";
            try
            {
                this.fw.write(strLine);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (iBCount >= by.length)
                break;
            ++iLCount;
        }
    }
}

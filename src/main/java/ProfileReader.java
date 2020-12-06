/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class ProfileReader
{
    int wID;
    int dwLen;
    int dwSection;
    boolean bUnicode;
    public File file;
    public RandomAccessFile fp;
    ArrayList<String> strBody;
    String strLine;
    int index;

    private void SeekTo(int n)
    {
        this.index = n;
    }

    private int GetPos()
    {
        return this.index;
    }

    private void SeekHead()
    {
        this.index = 0;
    }

    private int GetLine()
    {
        if (this.fp == null)
        {
            return -1;
        }
        if (this.index >= this.strBody.size())
        {
            return -1;
        }
        this.strLine = this.strBody.get(this.index++);
        if (this.strLine == null)
        {
            return -1;
        }
        return 1;
    }

    public void SetSection(String strSection)
    {
        if (this.fp == null)
        {
            return;
        }
        String strSec = "[" + strSection + "]";
        this.SeekHead();
        while (this.GetLine() > 0)
        {
            if (this.strLine.indexOf(strSec) != 0)
                continue;
            this.dwSection = this.GetPos();
            return;
        }
    }

    public ProfileReader(String strFile)
    {
        this.file = new File(strFile);
        this.fp = null;
        try
        {
            String s;
            BufferedReader br;
            InputStreamReader isr;
        block17 : {
            this.fp = new RandomAccessFile(this.file, "r");
            if (this.fp == null)
            {
                return;
            }
            this.bUnicode = false;
            this.wID = this.fp.readUnsignedShort();
            if (this.wID == 65534)
            {
                this.bUnicode = true;
                this.dwLen = 2;
            }
            else if (this.wID == 16973)
            {
                this.dwLen = this.fp.readInt();
            }
            else
            {
                if (this.wID == 35152)
                {
                    this.dwLen = 8;
                    while (true)
                    {
                        this.fp.seek(this.dwLen);
                        int dwChunkLen = this.fp.readInt();
                        int dwChunkID = this.fp.readInt();
                        if (dwChunkID == 1950701684)
                        {
                            this.dwLen += 16;
                            break block17;
                        }
                        this.dwLen += dwChunkLen + 12;
                    }
                }
                if (this.wID == 19277)
                {
                    this.dwLen = this.fp.readInt();
                    this.dwLen = this.dwLen << 24 & 0xFF000000 | this.dwLen << 8 & 0xFF0000 | this.dwLen >> 8 & 0xFF00 |
                                 this.dwLen >> 24 & 0xFF;
                    this.bUnicode = true;
                }
                else
                {
                    this.dwLen = 0;
                }
            }
        }
            this.fp.close();
            FileInputStream in = new FileInputStream(this.file);
            if (this.dwLen > 0)
            {
                in.skip(this.dwLen);
            }
            this.strBody = new ArrayList();
            in.read();
            int ch = in.read();
            in.close();
            in = new FileInputStream(this.file);
            if (this.dwLen > 0)
            {
                in.skip(this.dwLen);
            }
            if (this.bUnicode)
            {
                isr = ch != 0 ? new InputStreamReader((InputStream)in, "UTF-8")
                              : new InputStreamReader((InputStream)in, "UTF-16LE");
                br = new BufferedReader(isr);
            }
            else
            {
                isr = new InputStreamReader((InputStream)in, "UTF-8");
                br = new BufferedReader(isr);
            }
            while ((s = br.readLine()) != null)
            {
                this.strBody.add(s);
                if (!s.equals("[End]"))
                    continue;
            }
            isr.close();
            this.index = 0;
        }
        catch (IOException e)
        {
            this.fp = null;
        }
    }

    public void Close()
    {
    }

    public int Error()
    {
        if (this.fp == null)
        {
            return 1;
        }
        return 0;
    }

    public int ReadInt(String strKey, int iDefault)
    {
        String str = String.valueOf(strKey) + "=";
        if (this.fp == null)
        {
            return iDefault;
        }
        this.SeekTo(this.dwSection);
        while (this.GetLine() > 0)
        {
            if (this.strLine.indexOf(str) == 0)
            {
                try
                {
                    return Integer.parseInt(this.strLine.substring(str.length()));
                }
                catch (NumberFormatException e)
                {
                    return 1;
                }
            }
            if (!this.strLine.startsWith("["))
                continue;
        }
        return iDefault;
    }

    public double ReadFloat(String strKey, float fDefault)
    {
        String str = String.valueOf(strKey) + "=";
        if (this.fp == null)
        {
            return fDefault;
        }
        this.SeekTo(this.dwSection);
        while (this.GetLine() > 0)
        {
            if (this.strLine.indexOf(str) == 0)
            {
                String s = this.strLine.replaceAll(",", ".");
                return Double.parseDouble(s.substring(str.length()));
            }
            if (!this.strLine.startsWith("["))
                continue;
        }
        return fDefault;
    }

    public String ReadString(String strKey, String strDefault)
    {
        String str = String.valueOf(strKey) + "=";
        if (this.fp == null)
        {
            return strDefault;
        }
        this.SeekTo(this.dwSection);
        while (this.GetLine() > 0)
        {
            if (this.strLine.indexOf(str) == 0)
            {
                return this.strLine.substring(str.length());
            }
            if (!this.strLine.startsWith("["))
                continue;
        }
        return strDefault;
    }

    public String ReadNext(String strDefault)
    {
        if (this.fp == null)
        {
            return strDefault;
        }
        if (this.GetLine() <= 0)
        {
            return strDefault;
        }
        return this.strLine.substring(this.strLine.indexOf("=") + 1);
    }

    public byte[] ExtractFile(String strKeyBase)
    {
        int ibufsize = 4096;
        int idatsize = 0;
        byte[] datFile = new byte[ibufsize];
        String strKey = String.valueOf(strKeyBase) + "0";
        String strFileBuff = this.ReadString(strKey, "");
        if (strFileBuff.length() > 0)
        {
            int k;
            byte[] datFileNew;
            int i = 0;
            while (true)
            {
                strKey = String.valueOf(strKeyBase) + i;
                strFileBuff = i == 0 ? this.ReadString(strKey, "") : this.ReadNext("[");
                int jMax = strFileBuff.length();
                if (strFileBuff.charAt(0) == '[')
                    break;
                for (int j = 0; j < jMax - 1; j += 2)
                {
                    int c;
                    try
                    {
                        c = Integer.parseInt(strFileBuff.substring(j, j + 2), 16);
                    }
                    catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                        c = 0;
                    }
                    datFile[idatsize++] = (byte)c;
                    if (idatsize < ibufsize)
                        continue;
                    datFileNew = new byte[ibufsize += 4096];
                    for (k = 0; k < idatsize; ++k)
                    {
                        datFileNew[k] = datFile[k];
                    }
                    datFile = datFileNew;
                }
                if (jMax < 512)
                    break;
                ++i;
            }
            datFileNew = new byte[idatsize];
            for (k = 0; k < idatsize; ++k)
            {
                datFileNew[k] = datFile[k];
            }
            return datFileNew;
        }
        return new byte[0];
    }
}

/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class DynamicText
{
    String str;
    int p;
    int pi;
    int total;
    String fmt;
    int fx;
    double pn1;
    double pn2;
    double pn3;
    double dLog10e = 0.4342944819032518;

    public DynamicText(String s)
    {
        this.str = s;
        this.p = 0;
        this.pi = 0;
        this.fx = 0;
        this.fmt = "%d";
        this.total = this.Count();
    }

    private char GetChar(int p)
    {
        if (p >= this.str.length())
        {
            return '\u0000';
        }
        return this.str.charAt(p);
    }

    private int Skip(int p)
    {
        while (this.GetChar(p) == ' ')
        {
            ++p;
        }
        return p;
    }

    private int Digit(char x)
    {
        if (x >= '0' && x <= '9')
        {
            return 1;
        }
        return 0;
    }

    private double Eval0(int p, double rVal)
    {
        switch (this.GetChar(p))
        {
        case 'x': {
            this.p = p + 1;
            return rVal;
        }
        case '(': {
            double d = this.Eval(p + 1, rVal);
            if (this.GetChar(this.p) == ')')
            {
                ++this.p;
            }
            return d;
        }
        case '-': {
            return -this.Eval0(p + 1, rVal);
        }
        case '+': {
            return this.Eval0(p + 1, rVal);
        }
        }
        return this.GetANum(p);
    }

    private double Eval1(int p, double rVal)
    {
        if (this.str.indexOf("pow(", p) == p)
        {
            double d = this.Eval(p + 4, rVal);
            if (this.GetChar(this.p) == ',')
            {
                ++this.p;
            }
            d = Math.pow(d, this.Eval(this.p, rVal));
            if (this.GetChar(this.p) == ')')
            {
                ++this.p;
            }
            return d;
        }
        if (this.str.indexOf("exp(", p) == p)
        {
            double d = this.Eval(p + 4, rVal);
            if (this.GetChar(this.p) == ')')
            {
                ++this.p;
            }
            return Math.exp(d);
        }
        if (this.str.indexOf("log(", p) == p)
        {
            double d = this.Eval(p + 4, rVal);
            if (this.GetChar(this.p) == ')')
            {
                ++this.p;
            }
            return Math.log(d);
        }
        if (this.str.indexOf("log10(", p) == p)
        {
            double d = this.Eval(p + 6, rVal);
            if (this.GetChar(this.p) == ')')
            {
                ++this.p;
            }
            return this.dLog10e * Math.log(d);
        }
        if (this.str.indexOf("sqrt(", p) == p)
        {
            double d = this.Eval(p + 5, rVal);
            if (this.GetChar(this.p) == ')')
            {
                ++this.p;
            }
            return Math.sqrt(d);
        }
        return this.Eval0(p, rVal);
    }

    private double Eval2(int p, double rVal)
    {
        double d = this.Eval1(p, rVal);
    block4:
        while (true)
        {
            switch (this.GetChar(this.p))
            {
            case '*': {
                d *= this.Eval1(this.p + 1, rVal);
                continue block4;
            }
            case '/': {
                d /= this.Eval1(this.p + 1, rVal);
                continue block4;
            }
            }
            break;
        }
        return d;
    }

    private double Eval3(int p, double rVal)
    {
        double d = this.Eval2(p, rVal);
    block4:
        while (true)
        {
            switch (this.GetChar(this.p))
            {
            case '+': {
                d += this.Eval2(this.p + 1, rVal);
                continue block4;
            }
            case '-': {
                d -= this.Eval2(this.p + 1, rVal);
                continue block4;
            }
            }
            break;
        }
        return d;
    }

    private double Eval(int p, double rVal)
    {
        return this.Eval3(p, rVal);
    }

    private String WSprintf(double rVal)
    {
        if (this.fx != 0)
        {
            rVal = this.Eval(this.fx, rVal);
        }
        String str = "";
        int pcs = 4;
        int w = 0;
        boolean z = false;
        boolean p = false;
        for (int i = 0; i < this.fmt.length(); ++i)
        {
            if (this.fmt.charAt(i) == '%')
            {
                String strv = "";
                if (++i >= this.fmt.length())
                    break;
                if (this.fmt.charAt(i) == '+')
                {
                    if (++i >= this.fmt.length())
                        break;
                    if (rVal >= 0.0)
                    {
                        p = true;
                    }
                }
                if (this.fmt.charAt(i) == '0')
                {
                    z = true;
                    if (++i >= this.fmt.length())
                        break;
                }
                if (this.Digit(this.fmt.charAt(i)) != 0)
                {
                    w = this.fmt.charAt(i) - 48;
                    if (++i >= this.fmt.length())
                        break;
                }
                if (this.fmt.charAt(i) == '.')
                {
                    if (++i >= this.fmt.length())
                        break;
                    pcs = 0;
                    if (this.Digit(this.fmt.charAt(i)) != 0)
                    {
                        pcs = this.fmt.charAt(i) - 48;
                        ++i;
                    }
                }
                switch (this.fmt.charAt(i))
                {
                case 'd': {
                    strv = Integer.toString((int)rVal);
                    break;
                }
                case 'f': {
                    String strf = this.fmt;
                    try
                    {
                        strv = String.format(strf, rVal);
                    }
                    catch (Exception e)
                    {
                        strv = "";
                    }
                    break;
                }
                case 'x': {
                    strv = String.format("%x", (int)rVal);
                    break;
                }
                case 'X': {
                    strv = String.format("%X", (int)rVal);
                    break;
                }
                }
                if (w != 0)
                {
                    String s = String.valueOf(z ? "00000000" : "        ") + strv;
                    strv = s.substring(s.length() - w);
                }
                if (p)
                {
                    strv = "+" + strv;
                }
                str = String.valueOf(str) + strv;
                continue;
            }
            str = String.valueOf(str) + this.fmt.charAt(i);
        }
        return str;
    }

    private double GetANum(int p)
    {
        double sign = 1.0;
        double frac = 0.0;
        int n1 = 0;
        if (this.GetChar(p = this.Skip(p)) == '-')
        {
            sign = -1.0;
            ++p;
        }
        p = this.Skip(p);
        while (this.Digit(this.GetChar(p)) != 0)
        {
            n1 = n1 * 10 + this.GetChar(p) - 48;
            ++p;
        }
        if (this.GetChar(p) == '.')
        {
            double dp = 0.1;
            while (this.Digit(this.GetChar(++p)) != 0)
            {
                frac += dp * (double)(this.GetChar(p) - 48);
                dp *= 0.1;
                ++p;
            }
        }
        this.p = this.Skip(p);
        return sign * ((double)n1 + frac);
    }

    private int CheckNum(int p)
    {
        int iParen = 0;
        this.fx = 0;
        this.fmt = "%d";
        double r1 = this.GetANum(++p);
        if (this.GetChar(this.p) != ':')
        {
            return -1;
        }
        double r2 = this.GetANum(this.p + 1);
        double r3 = 1.0;
        if (this.GetChar(this.p) == ':')
        {
            if ((this.Digit(this.GetChar(this.p + 1)) != 0 || this.GetChar(this.p + 1) == '.') &&
                (r3 = this.GetANum(this.p + 1)) == 0.0)
            {
                r3 = 1.0;
            }
            if (this.GetChar(this.p) == ':')
            {
                ++this.p;
                this.fmt = "";
                while (this.GetChar(this.p) != ')' && this.GetChar(this.p) != ':' && this.p < this.str.length())
                {
                    this.fmt = String.valueOf(this.fmt) + this.GetChar(this.p++);
                }
            }
            if (this.GetChar(this.p) == ':')
            {
                ++this.p;
                this.fx = this.p;
                iParen = 0;
                while (this.p < this.str.length() && this.GetChar(this.p) != ':')
                {
                    if (this.GetChar(this.p) == '(')
                    {
                        ++iParen;
                    }
                    if (this.GetChar(this.p) == ')' && --iParen < 0)
                        break;
                    ++this.p;
                }
            }
        }
        if (this.GetChar(this.p) != ')')
        {
            return -1;
        }
        this.pn1 = r1;
        this.pn2 = r2;
        this.pn3 = r3;
        return (int)(Math.abs(r1 - r2) / r3);
    }

    private int Count()
    {
        int i = 1;
        for (int p = 0; p < this.str.length(); ++p)
        {
            int j;
            if (this.GetChar(p) == ',')
            {
                ++i;
                continue;
            }
            if (this.GetChar(p) != '(' || (j = this.CheckNum(p)) <= 0)
                continue;
            p = this.p;
            i += j;
        }
        return i;
    }

    private String GetNext(int p)
    {
        String strTemp = "";
        int pStart = p;
        while (true)
        {
            char c;
            int i;
            if (this.GetChar(p) == '(' && (i = this.CheckNum(p)) >= 0)
            {
                strTemp = this.pn2 >= this.pn1
                              ? String.valueOf(strTemp) + this.WSprintf(this.pn1 + (double)this.pi * this.pn3)
                              : String.valueOf(strTemp) + this.WSprintf(this.pn1 - (double)this.pi * this.pn3);
                ++this.pi;
                ++this.p;
                while (this.p < this.str.length() && this.str.charAt(this.p) != ',')
                {
                    strTemp = String.valueOf(strTemp) + this.str.charAt(this.p++);
                }
                if (this.pi > i)
                {
                    this.pi = 0;
                    this.p = this.Skip(this.p);
                    if (this.p < this.str.length() && this.str.charAt(this.p) == ',')
                    {
                        ++this.p;
                    }
                }
                else
                {
                    this.p = pStart;
                }
                return strTemp;
            }
            if (p >= this.str.length() || (c = this.str.charAt(p++)) == ',')
                break;
            strTemp = String.valueOf(strTemp) + c;
        }
        this.p = p;
        return strTemp;
    }

    private String GetItem(int n)
    {
        int p = 0;
        this.pi = 0;
        String pItem = "";
        if (n >= this.total)
        {
            n = this.total - 1;
        }
        while (n >= 0)
        {
            pItem = this.GetNext(p);
            p = this.p;
            --n;
        }
        return pItem;
    }

    public String Get(int frame, int maxframe)
    {
        String s = maxframe <= 1 ? this.GetItem(0) : this.GetItem(this.total * frame / (maxframe - 1));
        return s;
    }
}

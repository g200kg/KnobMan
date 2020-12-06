/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class XYMatrix
{
    double[] a = new double[6];

    public XYMatrix()
    {
        for (int i = 0; i < 6; ++i)
        {
            this.a[i] = 0.0;
        }
        this.a[3] = 1.0;
        this.a[0] = 1.0;
    }

    public void Set(int i, double v)
    {
        this.a[i] = v;
    }

    public void Reset()
    {
        this.a[3] = 1.0;
        this.a[0] = 1.0;
        this.a[5] = 0.0;
        this.a[4] = 0.0;
        this.a[2] = 0.0;
        this.a[1] = 0.0;
    }

    public void Copy(XYMatrix b)
    {
        for (int i = 0; i < 6; ++i)
        {
            this.a[i] = b.a[i];
        }
    }

    public void Mul(XYMatrix b)
    {
        XYMatrix t = new XYMatrix();
        t.a[0] = this.a[0] * b.a[0] + this.a[2] * b.a[1];
        t.a[1] = this.a[1] * b.a[0] + this.a[3] * b.a[1];
        t.a[2] = this.a[0] * b.a[2] + this.a[2] * b.a[3];
        t.a[3] = this.a[1] * b.a[2] + this.a[3] * b.a[3];
        t.a[4] = this.a[0] * b.a[4] + this.a[2] * b.a[5] + this.a[4];
        t.a[5] = this.a[1] * b.a[4] + this.a[3] * b.a[5] + this.a[5];
        this.Copy(t);
    }

    public void Translate(double x, double y)
    {
        XYMatrix t = new XYMatrix();
        t.a[4] = x;
        t.a[5] = y;
        t.Mul(this);
        this.Copy(t);
    }

    public void Rotate(double r)
    {
        r = r * Math.PI / 180.0;
        XYMatrix t = new XYMatrix();
        t.a[0] = t.a[3] = Math.cos(r);
        t.a[2] = Math.sin(r);
        t.a[1] = -t.a[2];
        t.Mul(this);
        this.Copy(t);
    }

    public void Scale(double x, double y)
    {
        XYMatrix t = new XYMatrix();
        t.a[0] = x;
        t.a[3] = y;
        t.Mul(this);
        this.Copy(t);
    }

    public void RotateAt(double r, double x, double y)
    {
        XYMatrix t = new XYMatrix();
        t.Translate(-x, -y);
        t.Rotate(r);
        t.Translate(x, y);
        t.Mul(this);
        this.Copy(t);
    }

    public void ScaleAt(double sx, double sy, double x, double y)
    {
        XYMatrix t = new XYMatrix();
        t.Translate(-x, -y);
        t.Scale(sx, sy);
        t.Translate(x, y);
        t.Mul(this);
        this.Copy(t);
    }

    public void SkewX(double r)
    {
        r = r * Math.PI / 180.0;
        XYMatrix t = new XYMatrix();
        t.a[2] = Math.tan(r);
        t.Mul(this);
        this.Copy(t);
    }

    public void SkewY(double r)
    {
        r = r * Math.PI / 180.0;
        XYMatrix t = new XYMatrix();
        t.a[1] = Math.tan(r);
        t.Mul(this);
        this.Copy(t);
    }

    public void Transform(double[] p)
    {
        double x = p[0] * this.a[0] + p[1] * this.a[2] + this.a[4];
        double y = p[0] * this.a[1] + p[1] * this.a[3] + this.a[5];
        p[0] = x;
        p[1] = y;
    }
}

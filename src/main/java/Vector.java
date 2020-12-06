/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class Vector
{
    double x;
    double y;

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public void Set(Vector v)
    {
        this.x = v.x;
        this.y = v.y;
    }

    public void Add(Vector v)
    {
        this.x += v.x;
        this.y += v.y;
    }

    public void Sub(Vector v)
    {
        this.x -= v.x;
        this.y -= v.y;
    }

    public void Mul(Vector v)
    {
        this.x *= v.x;
        this.y *= v.y;
    }

    public void Scale(double t)
    {
        this.x *= t;
        this.y *= t;
    }

    public void Div(Vector v)
    {
        this.x /= v.x;
        this.y /= v.y;
    }

    public void Rotate(double t)
    {
        double rSin = Math.sin(t * Math.PI / 180.0);
        double rCos = Math.cos(t * Math.PI / 180.0);
        double xx = rCos * this.x + rSin * this.y;
        this.y = rCos * this.y - rSin * this.x;
        this.x = xx;
    }

    public void RotateAt(double t, Vector vOrig, Vector vAsp)
    {
        this.Sub(vOrig);
        this.Rotate(t);
        this.Add(vOrig);
    }

    public double Product(Vector v)
    {
        return this.x * v.x + this.y * v.y;
    }

    public double Length2()
    {
        return this.x * this.x + this.y * this.y;
    }

    public double Abs()
    {
        return Math.sqrt(this.Length2());
    }

    public void Normalize()
    {
        double t = this.Abs();
        this.x /= t;
        this.y /= t;
    }
}

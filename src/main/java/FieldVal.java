/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.awt.Color;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FieldVal extends JPanel
    implements FocusListener, ActionListener, MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
    JLabel lb;
    JTextField tf;
    JButton bt;
    int y0;
    double val0;
    ParamV pv;
    ParamI pi;
    double mn;
    double mx;
    Color colDef;
    boolean bEditMode;
    UpdateReq ur;
    boolean bModified;
    int m;

    public FieldVal(Container frame, int x, int y, String name, int w1, int w2, boolean d, double mn, double mx,
                    UpdateReq ur, int m)
    {
        this.setLayout(null);
        this.mn = mn;
        this.mx = mx;
        this.ur = ur;
        this.m = m;
        this.bEditMode = false;
        this.setBounds(x, y, w1 + w2, 22);
        this.lb = new JLabel(name);
        this.lb.setFont(GUIEditor.getInstance().fontUI);
        this.lb.setHorizontalAlignment(4);
        this.tf = new JTextField("0.00", 8);
        this.tf.setFont(GUIEditor.getInstance().fontUI);
        this.tf.setHorizontalAlignment(0);
        this.bt = new JButton(GUIEditor.getInstance().iconSpin);
        this.bt.addActionListener(this);
        this.bt.addMouseListener(this);
        this.bt.addMouseMotionListener(this);
        this.add(this.lb);
        this.add(this.tf);
        this.add(this.bt);
        this.lb.setBounds(0, 0, w1 - 2, 22);
        this.tf.setBounds(w1, 0, w2 - 27, 22);
        this.bt.setBounds(w1 + w2 - 25, 0, 25, 22);
        this.tf.addActionListener(this);
        this.tf.addKeyListener(this);
        this.bt.addKeyListener(this);
        this.tf.addMouseWheelListener(this);
        this.bt.addMouseWheelListener(this);
        this.tf.addFocusListener(this);
        frame.add(this);
        this.bt.setFocusable(false);
        this.pv = null;
        this.pi = null;
        this.colDef = this.tf.getBackground();
        this.bModified = false;
    }

    public void ReLayout(int x, int y)
    {
        Rectangle r = this.getBounds();
        this.setBounds(x, y, r.width, r.height);
    }

    public void SetEditMode(boolean b)
    {
        this.bEditMode = b;
    }

    public void SetColor(boolean c)
    {
        if (c)
        {
            this.tf.setBackground(Color.yellow);
        }
        else
        {
            this.tf.setBackground(this.colDef);
        }
    }

    public void SetModified(boolean f)
    {
        this.tf.setBackground(Color.yellow);
        this.bModified = f;
    }

    double LimitVal(double v)
    {
        if (v < this.mn)
        {
            v = this.mn;
        }
        if (v > this.mx)
        {
            v = this.mx;
        }
        v = (double)((int)(v * 100.0)) / 100.0;
        return v;
    }

    public void Setup(ParamV pv)
    {
        this.pi = null;
        this.pv = pv;
        if (this.pv != null)
        {
            this.tf.setText(String.format("%.2f", pv.val));
        }
    }

    public void Setup(ParamI pi)
    {
        this.pv = null;
        this.pi = pi;
        if (this.pi != null)
        {
            this.tf.setText(String.format("%d", pi.val));
        }
    }

    public void Setup(double v)
    {
        if (this.pv != null)
        {
            if (this.pv.val != v)
            {
                Control.getInstance().journal.WriteOnce(this);
            }
            this.pv.val = v;
            this.Setup(this.pv);
        }
        else if (this.pi != null)
        {
            int i = (int)v;
            if (this.pi.val != i)
            {
                Control.getInstance().journal.WriteOnce(this);
            }
            this.pi.val = i;
            this.Setup(this.pi);
        }
    }

    public void Setup(int i)
    {
        this.Setup((double)i);
    }

    public void Setup()
    {
        if (this.pi != null)
        {
            this.Setup(this.pi);
        }
        else if (this.pv != null)
        {
            this.Setup(this.pv);
        }
    }

    public void Reform()
    {
        this.tf.setBackground(this.colDef);
        String s = this.tf.getText();
        double v = 0.0;
        v = s.startsWith("#")
                ? (double)Integer.parseInt(s.substring(1), 16)
                : (s.startsWith("0x") ? (double)Integer.parseInt(s.substring(2), 16) : Parse.Double(this.tf.getText()));
        v = this.LimitVal(v);
        if (this.pv != null)
        {
            this.pv.val = v;
            this.tf.setText(String.format("%.2f", v));
        }
        else if (this.pi != null)
        {
            this.pi.val = (int)v;
            this.tf.setText(String.format("%d", this.pi.val));
        }
    }

    @Override public void actionPerformed(ActionEvent e)
    {
        Control.getInstance().journal.ResetOnce();
        this.Reform();
        if (this.ur != null)
        {
            this.ur.Update(32768 + this.m);
        }
    }

    @Override public void mousePressed(MouseEvent e)
    {
        if (this.bt.isEnabled())
        {
            this.SetModified(false);
            this.y0 = e.getY();
            if (this.pv != null)
            {
                this.val0 = this.pv.val;
            }
            else if (this.pi != null)
            {
                this.val0 = this.pi.val;
            }
        }
    }

    @Override public void mouseClicked(MouseEvent e)
    {
    }

    @Override public void mouseReleased(MouseEvent e)
    {
        if (this.bt.isEnabled())
        {
            Control.getInstance().journal.ResetOnce();
            this.tf.setBackground(this.colDef);
            this.tf.requestFocus();
            if (this.ur != null)
            {
                this.ur.Update(32768 + this.m);
            }
        }
    }

    @Override public void mouseEntered(MouseEvent e)
    {
    }

    @Override public void mouseExited(MouseEvent e)
    {
    }

    @Override public void mouseMoved(MouseEvent e)
    {
    }

    @Override public void mouseDragged(MouseEvent e)
    {
        if (this.bt.isEnabled())
        {
            int y = e.getY();
            int mod = e.getModifiersEx();
            if (this.pv != null || this.pi != null)
            {
                double val = (mod & 0x40) != 0 ? this.LimitVal(this.val0 - (double)(y - this.y0) * 0.01)
                                               : this.LimitVal(this.val0 - (double)(y - this.y0));
                this.Setup(val);
                if (this.ur != null)
                {
                    this.ur.Update(this.m);
                }
            }
        }
    }

    @Override public void keyPressed(KeyEvent e)
    {
        int mod = e.getModifiersEx();
        int code = e.getKeyCode();
        switch (code)
        {
        case 38: {
            if (this.bEditMode)
            {
                this.SetModified(true);
            }
            else
            {
                this.tf.setBackground(this.colDef);
            }
            if (this.pv != null)
            {
                if ((mod & 0x40) != 0)
                {
                    this.Setup(this.LimitVal(this.pv.val + 0.01));
                }
                else if ((mod & 0x80) != 0)
                {
                    this.Setup(this.LimitVal(this.pv.val + 10.0));
                }
                else
                {
                    this.Setup(this.LimitVal(this.pv.val + 1.0));
                }
            }
            if (this.pi != null)
            {
                if ((mod & 0x80) != 0)
                {
                    this.Setup(this.LimitVal(this.pi.val + 10));
                }
                else
                {
                    this.Setup(this.LimitVal(this.pi.val + 1));
                }
            }
            if (this.ur != null)
            {
                this.ur.Update(this.m);
            }
            e.consume();
            break;
        }
        case 40: {
            if (this.bEditMode)
            {
                this.SetModified(true);
            }
            else
            {
                this.tf.setBackground(this.colDef);
            }
            if (this.pv != null)
            {
                if ((mod & 0x40) != 0)
                {
                    this.Setup(this.LimitVal(this.pv.val - 0.01));
                }
                else if ((mod & 0x80) != 0)
                {
                    this.Setup(this.LimitVal(this.pv.val - 10.0));
                }
                else
                {
                    this.Setup(this.LimitVal(this.pv.val - 1.0));
                }
            }
            if (this.pi != null)
            {
                if ((mod & 0x80) != 0)
                {
                    this.Setup(this.LimitVal(this.pi.val - 10));
                }
                else
                {
                    this.Setup(this.LimitVal(this.pi.val - 1));
                }
            }
            if (this.ur != null)
            {
                this.ur.Update(this.m);
            }
            e.consume();
            break;
        }
        case 10: {
            Control.getInstance().journal.ResetOnce();
            this.tf.setBackground(this.colDef);
            break;
        }
        case 8:
        case 32:
        case 127: {
            this.SetModified(true);
            break;
        }
        default: {
            if ((mod & 0x380) != 0 || (code < 48 || code > 57) && (code < 65 || code > 90))
                break;
            this.SetModified(true);
        }
        }
    }

    @Override public void keyReleased(KeyEvent e)
    {
    }

    @Override public void keyTyped(KeyEvent e)
    {
    }

    @Override public void mouseWheelMoved(MouseWheelEvent e)
    {
        if (e.getSource() == GUIEditor.getInstance().getFocusOwner())
        {
            int iInv = GUIEditor.getInstance().iWheelDir != 0 ? -1 : 1;
            int shift = e.getModifiersEx() & 0x40;
            int ctrl = e.getModifiersEx() & 0x80;
            if (this.bEditMode)
            {
                this.SetModified(true);
            }
            if (this.pv != null)
            {
                if (shift != 0)
                {
                    this.Setup(this.LimitVal(this.pv.val - (double)(iInv * e.getWheelRotation()) * 0.01));
                }
                else if (ctrl != 0)
                {
                    this.Setup(this.LimitVal(this.pv.val - (double)(iInv * e.getWheelRotation() * 10)));
                }
                else
                {
                    this.Setup(this.LimitVal(this.pv.val - (double)(iInv * e.getWheelRotation())));
                }
            }
            else if (this.pi != null)
            {
                if (ctrl != 0)
                {
                    this.Setup(this.LimitVal(this.pi.val - iInv * e.getWheelRotation()));
                }
                else
                {
                    this.Setup(this.LimitVal(this.pi.val - iInv * e.getWheelRotation()));
                }
            }
            if (this.ur != null)
            {
                this.ur.Update(this.m);
            }
        }
        else
        {
            this.getParent().dispatchEvent(e);
        }
    }

    @Override public void focusGained(FocusEvent e)
    {
    }

    @Override public void focusLost(FocusEvent e)
    {
        this.Reform();
        if (this.ur != null && this.bModified)
        {
            this.ur.Update(32768 + this.m);
        }
    }
}

/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

public class RenderReq
{
    boolean runreq = false;
    boolean breakreq = false;
    boolean isrun = false;

    RenderReq()
    {
    }

    boolean IsRun()
    {
        return this.isrun;
    }

    boolean BreakCheck()
    {
        return this.breakreq;
    }

    synchronized void RunReq()
    {
        this.runreq = true;
        this.notifyAll();
    }

    synchronized void BreakReq()
    {
        this.runreq = false;
        if (!this.isrun)
        {
            return;
        }
        this.breakreq = true;
    }

    void WaitBreak()
    {
        this.BreakReq();
        while (this.IsRun())
        {
            try
            {
                Thread.sleep(20L);
            }
            catch (InterruptedException interruptedException)
            {
                // empty catch block
            }
        }
    }

    synchronized void Wait()
    {
        do
        {
            try
            {
                this.isrun = false;
                this.breakreq = false;
                this.wait(20L);
            }
            catch (InterruptedException interruptedException)
            {
                // empty catch block
            }
        } while (!this.runreq);
        this.isrun = true;
        this.runreq = false;
    }
}

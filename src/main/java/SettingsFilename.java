/*
 * This file is part of JKnobMan.
 * Copyright (c) 2012-2020 g200kg
 *
 * Use of this source code is governed by an MIT-style license that can be
 * found in the LICENSE file or at https://opensource.org/licenses/MIT.
 *
 * SPDX-License-Identifier: MIT
 */

import java.io.File;
import org.apache.commons.exec.OS;

public class SettingsFilename extends ResFilename
{
    public SettingsFilename(String strName)
    {
        super(strName);
    }

    /**
     * Returns the full path to the settings file.
     */
    public String GetString()
    {
        if (usesXdgPaths)
        {
            File configDir = getXdgKnobmanConfigDir();
            File configFile = new File(configDir, this.str);
            return configFile.toString();
        }
        return super.GetString();
    }

    /** Whether the configuration uses the Freedesktop path specification */
    static boolean usesXdgPaths = !OS.isFamilyWindows() && !OS.isFamilyMac();

    /**
     * Returns the JKnobMan configuration directory for Freedesktop platforms.
     * It is usually ~/.config/JKnobMan/.
     */
    private synchronized static File getXdgKnobmanConfigDir()
    {
        if (cacheKnobmanXdgConfigDir != null)
        {
            return cacheKnobmanXdgConfigDir;
        }
        File dir = new File(getXdgConfigDir(), "JKnobMan");
        dir.mkdirs();
        cacheKnobmanXdgConfigDir = dir;
        return dir;
    }

    private static File cacheKnobmanXdgConfigDir = null;

    /**
     * Returns the configuration home directory for Freedesktop platforms.
     * It is usually ~/.config/.
     */
    private static File getXdgConfigDir()
    {
        File dir;
        String path = System.getenv("XDG_CONFIG_HOME");
        if (path != null)
        {
            dir = new File(path);
        }
        else
        {
            path = System.getProperty("user.home");
            dir = new File(path, ".config");
        }
        return dir;
    }
}

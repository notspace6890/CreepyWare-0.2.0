// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.manager;

import java.util.ArrayList;
import dev.fxcte.creepyware.util.NoStackTraceThrowable;
import dev.fxcte.creepyware.util.DisplayUtil;
import dev.fxcte.creepyware.util.SystemUtil;
import dev.fxcte.creepyware.util.URLReader;
import java.util.List;

public class HWIDManager
{
    public static final String pastebinURL = "https://pastebin.com/raw/JVRvfij3";
    public static List<String> hwids;
    
    public static void hwidCheck() {
        HWIDManager.hwids = URLReader.readURL();
        final boolean isHwidPresent = HWIDManager.hwids.contains(SystemUtil.getSystemInfo());
        if (!isHwidPresent) {
            DisplayUtil.Display();
            throw new NoStackTraceThrowable("");
        }
    }
    
    static {
        HWIDManager.hwids = new ArrayList<String>();
    }
}

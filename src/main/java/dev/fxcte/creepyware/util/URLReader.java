// 
// Decompiled by Procyon v0.5.36
// 

package dev.fxcte.creepyware.util;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class URLReader
{
    public static List<String> readURL() {
        final List<String> s = new ArrayList<String>();
        try {
            final URL url = new URL("https://pastebin.com/raw/JVRvfij3");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            String hwid;
            while ((hwid = bufferedReader.readLine()) != null) {
                s.add(hwid);
            }
        }
        catch (Exception ex) {}
        return s;
    }
}

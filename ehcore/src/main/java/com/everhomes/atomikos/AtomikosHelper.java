// @formatter:off
package com.everhomes.atomikos;

import java.io.File;

public class AtomikosHelper {
    public static void fixup() {
        String workingDir = System.getProperty("user.dir");
        File file = new File(workingDir + File.separator + "atomikos" + File.separator + "tmlog.lck");
        file.delete();
        
        file = new File(workingDir + File.separator + "atomikos" + File.separator + "tm.out.lck");
        file.delete();
    }
}

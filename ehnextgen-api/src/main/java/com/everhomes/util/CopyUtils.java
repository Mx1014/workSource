package com.everhomes.util;

import java.io.*;
import java.util.List;

/**
 * Created by sfyan on 2016/11/4.
 */
public class CopyUtils {


    @SuppressWarnings("unchecked")
    public static <T> List<T> deepCopyList(List<T> src)
    {
        List<T> dest = null;
        try
        {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
        }
        catch (IOException e)
        {
            throw new RuntimeException("copy list error", e);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException("copy list error", e);
        }
        return dest;
    }
}

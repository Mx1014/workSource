package com.everhomes.util;

import com.sun.corba.se.impl.ior.ObjectAdapterIdNumber;

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

    /**
     * 增加一个对象的深拷贝，by wentian @ 2018.5.9
     *
     */
    public static <T extends Serializable> T deepCopy(T obj) throws Exception{
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
        objOut.writeObject(obj);
        ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(byteOut.toByteArray()));
        return (T)objIn.readObject();
    }
}

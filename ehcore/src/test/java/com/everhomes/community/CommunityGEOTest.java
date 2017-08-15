package com.everhomes.community;


import com.everhomes.junit.CoreServerTestCase;
import org.apache.lucene.spatial.geohash.GeoHashUtils;
import org.junit.Test;

public class CommunityGEOTest extends CoreServerTestCase {


    @Test
    public void testBasics() {
        //
        String result = GeoHashUtils.encode(22.68, 114.33);
        System.out.print(result);
    }
}

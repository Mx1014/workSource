package com.everhomes.contentserver;

import com.everhomes.junit.CoreServerTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xq.tian on 2017/6/21.
 */
public class ContentServerProviderImplTest extends CoreServerTestCase {

    @Autowired
    private ContentServerProvider contentServerProvider;

    @Test
    public void findByResourceId() throws Exception {
        // String oldResId = "image/MTpmZWE1OGIzMjZmMzQyMzE2M2M5OTVkMTZhNDE5YjA4ZQ";
        String oldResId = "audio/MjptNGE6Yjc1OTFjOTRmYTgwY2M4ZjhiMTkzYTc3MmNlMWNjODQ";
        ContentServerResource res = null;
        for (int i = 0; i < 10; i++) {
            res = contentServerProvider.findByResourceId(oldResId);
            assertEquals("res id should be equal", oldResId, res.getResourceId());
        }

        String newResId = "file/MTpmZWE1OGIzMjZmMzQyMzE2M2M5OTVkMTZhNDE5YjA4ZQ";
        res.setResourceId(newResId);
        contentServerProvider.updateResource(res);// Didn't evict cache

        for (int i = 0; i < 10; i++) {
            res = contentServerProvider.findByResourceId(oldResId);
            assertEquals("Didn't evict cache, so new res id also old", oldResId, res.getResourceId());
        }
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.dataaccess.conf;

import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.dataaccess.models.conf.ApiKeys;
import java.io.File;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author puma
 */
public class ApiKeyProviderTest {

    public ApiKeyProviderTest() {
    }

    @Test
    public void TestFile() throws Exception {
        String apiKeysFilePath = "../../tests/api/test_api.key";

        File f = new File(apiKeysFilePath).getAbsoluteFile();
        ApiKeys api = XmlFileAccess.fromFile(ApiKeys.class, f);

        String apiKey = api.getData("test");
        assertEquals("ABCDE", apiKey);
    }
}

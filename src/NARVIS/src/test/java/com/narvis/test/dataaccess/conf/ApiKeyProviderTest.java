/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.test.dataaccess.conf;

import com.narvis.common.functions.serialization.XmlSerializer;
import com.narvis.dataaccess.DataAccessFactory;
import com.narvis.dataaccess.models.conf.ApiKeys;
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
    public void TestFile() throws Exception
    {
        String apiKeysFilePath = "path_to_file";
        ApiKeys api = XmlSerializer.fromFile(ApiKeys.class, apiKeysFilePath);
        
        String apiKey = api.getData("OpenWeatherMap");
        assertEquals("01b5f54b9605d5bbae6cf9f831560fb5", apiKey);
    }
}

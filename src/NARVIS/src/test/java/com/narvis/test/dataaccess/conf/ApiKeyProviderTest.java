/*
 * The MIT License
 *
 * Copyright 2015 uwy.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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

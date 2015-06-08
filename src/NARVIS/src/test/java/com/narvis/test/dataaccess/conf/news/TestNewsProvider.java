/*
 * The MIT License
 *
 * Copyright 2015 puma.
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
package com.narvis.test.dataaccess.conf.news;

import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.impl.ModuleConfigurationDataProvider;
import com.narvis.dataaccess.news.NewsProvider;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.print.attribute.HashAttributeSet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author puma
 */
public class TestNewsProvider {

    public TestNewsProvider() {
    }

    @Test
    public void testNewsForCity() throws ProviderException {

        ModuleConfigurationDataProvider confProvider = new ModuleConfigurationDataProvider(new File("../../release/modules/News"));
        NewsProvider provider = new NewsProvider(confProvider);

        Map<String, String> details = new HashMap<>();

        details.put("mexico", "location");
        String response = provider.getDataDetails(details, "");

        System.out.println(response);

    }
}

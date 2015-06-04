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
package com.narvis.dataaccess.models.lang.word;

import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name="Word")
public class Word {
    @Attribute(name="IsIgnored", required = false, empty = "false")
    boolean isIgnored;    
    
    @Element(name="Value", type = String.class)
    String value;
    
    @ElementList (name="InformationTypes", type = String.class, required = false)
    List<String> informationTypes;
    
    @ElementList (name="Hint", type = String.class, required = false)
    List<String> hints;
    
    public Word()
    {
        informationTypes = new LinkedList<>();
    }
    
    public boolean isIgnored()
    {
        return this.isIgnored;
    }
    
    public String getValue()
    {
        return this.value;
    }
    
    public List<String> getInformationTypes()
    {
        return this.informationTypes;
    }
    
    public List<String> getHints()
    {
        return this.hints;
    }
    
    public boolean containInformationType(String informationType)
    {
        informationType.toLowerCase();
        return informationTypes.contains(informationType);
    }
}

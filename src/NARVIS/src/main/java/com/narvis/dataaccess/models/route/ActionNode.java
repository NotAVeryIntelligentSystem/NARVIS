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
package com.narvis.dataaccess.models.route;

import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
@Root(name = "Action")
public class ActionNode {

    @Element(name = "Provider", type = String.class)
    private String providerName;
    @ElementList(name = "AskFor", required = false, type = String.class)
    public List<String> askFor;

    public ActionNode() {
        providerName = "";
        askFor = new LinkedList<>();
    }

    public ActionNode(@Element(name = "Provider") String providerName, @ElementList(name = "AskFor") List<String> askFor) {
        this.providerName = providerName;
        this.askFor = askFor;
    }

    public ActionNode(String providerName) {
        this.providerName = providerName;
        askFor = new LinkedList<>();
    }

    public String getProviderName() {
        return providerName;
    }

    public List<String> getAskFor() {
        return askFor;
    }

    public void setProdiverName(String providerName) {
        this.providerName = providerName;
    }

    public void setAskFor(List<String> askFor) {
        this.askFor = askFor;
    }
}

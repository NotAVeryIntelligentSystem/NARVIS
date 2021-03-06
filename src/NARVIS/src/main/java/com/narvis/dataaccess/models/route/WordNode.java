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
@Root(name = "Word")
public class WordNode {

    @Element(name = "Value", type = String.class, required = false)
    private String value;
    @ElementList(name = "Words", type = WordNode.class, required = false)
    @SuppressWarnings("FieldMayBeFinal")
    private List<WordNode> words;
    @ElementList(name = "Actions", type = ActionNode.class, required = false)
    @SuppressWarnings("FieldMayBeFinal")
    private List<ActionNode> actions;

    public WordNode() {
        words = new LinkedList<>();
        actions = new LinkedList<>();
    }

    /**
     * Constructeur
     *
     * @param value : Valeur du mot
     */
    public WordNode(String value) {
        words = new LinkedList<>();
        actions = new LinkedList<>();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public List<ActionNode> getActions() {
        return actions;
    }

    public List<WordNode> getWords() {
        return words;
    }

    public void addWord(WordNode newWord) {
        words.add(newWord);
    }

    public void addAction(ActionNode newAction) {
        actions.add(newAction);
    }

    public void setValue(String value) {
        this.value = value;
    }
}

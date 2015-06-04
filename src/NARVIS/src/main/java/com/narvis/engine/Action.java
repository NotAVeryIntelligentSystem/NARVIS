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
package com.narvis.engine;

import java.util.LinkedList;
import java.util.List;

/**
 * Représente une action devant être réalisée.
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class Action {
    private String providerName; // Le nom du provider correspondant à l'action
    private List<String> precisions; // Les éventuels précisions sur les types de données attendus
    private List<String> details; // La liste des mots de détail de la phrase
    
    public Action(){
        providerName = "";
        precisions = new LinkedList<>();
        details = new LinkedList<>();
    }
    
    public Action(String pProviderName, List<String> pPrecisions, List<String> pDetails){
        providerName = pProviderName;
        precisions = pPrecisions;
        details = pDetails;
    }
    
    public String getProviderName() {
        return providerName;
    }

    public List<String> getPrecisions() {
        return precisions;
    }

    public List<String> getDetails() {
        return details;
    }
}

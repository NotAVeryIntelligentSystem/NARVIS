/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

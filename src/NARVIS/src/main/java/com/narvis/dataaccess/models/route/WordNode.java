/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.route;

import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name = "Word")
public class WordNode {
    @Element(name="Value", type = String.class, required = false)
    private String value;
    @ElementList(name="Words", type = WordNode.class, required = false)
    @SuppressWarnings("FieldMayBeFinal")
    private List<WordNode> words;
    @ElementList(name="Actions", type = ActionNode.class, required = false)
    @SuppressWarnings("FieldMayBeFinal")
    private List<ActionNode> actions;
    
    public WordNode()
    {
        words = new LinkedList<>();
        actions = new LinkedList<>();
    }
    
    /**
     * Constructeur
     * @param value : Valeur du mot
     */
    public WordNode(String value)
    {
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
    
    public void addWord(WordNode newWord)
    {
        words.add(newWord);
    }
    
    public void addAction(ActionNode newAction)
    {
        actions.add(newAction);
    }
    
    public void setValue(String value){
        this.value = value;
    }
}

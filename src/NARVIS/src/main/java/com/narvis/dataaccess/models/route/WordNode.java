/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.route;

import java.util.LinkedList;
import java.util.List;
import com.narvis.dataaccess.interfaces.models.route.IActionNode;
import com.narvis.dataaccess.interfaces.models.route.IWordNode;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name = "Word")
public class WordNode implements IWordNode{
    @Element(name="Value", type = String.class, required = false)
    private String value;
    @ElementList(name="Words", type = WordNode.class)
    private final List<IWordNode> words;
    @ElementList(name="Actions", type = ActionNode.class)
    private final List<IActionNode> actions;
    
    public WordNode()
    {
        value = "";
        words = new LinkedList<>();
        actions = new LinkedList<>();
    }
    
    public WordNode(@Element(name="Value") String value, @ElementList(name="Words") List<IWordNode> words, @ElementList(name="Actions") List<IActionNode> actions)
    {
        this.value = value;
        this.words = words;
        this.actions = actions;
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
    
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public List<IActionNode> getActions() {
        return actions;
    }

    @Override
    public List<IWordNode> getWords() {
        return words;
    }
    
    @Override
    public void addWord(IWordNode newWord)
    {
        words.add(newWord);
    }
    
    @Override
    public void addAction(IActionNode newAction)
    {
        actions.add(newAction);
    }
    
    @Override
    public void setValue(String value){
        this.value = value;
    }
}

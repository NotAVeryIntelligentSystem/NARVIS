/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.engine.fondamentalAnalyser;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Zack
 */
public class WordNode implements IWordNode{
    private String value;
    private List<IWordNode> words;
    private List<IActionNode> actions;
    
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

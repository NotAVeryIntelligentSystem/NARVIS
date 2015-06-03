/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.route;

import com.narvis.dataaccess.interfaces.models.route.IRouteNode;
import com.narvis.dataaccess.interfaces.models.route.IWordNode;
import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name = "Route")
public class RouteNode implements IRouteNode{
    @ElementList(name="Words", type = WordNode.class)
    private final List<IWordNode> words;
    
    public RouteNode()
    {
        words = new LinkedList<>();
    }
    
    public RouteNode(@ElementList(name="Words") List<IWordNode> words)
    {
        this.words = words;
    }
    
    @Override
    public List<IWordNode> getWords() 
    {
        return words;
    }

    @Override
    public void addWord(IWordNode newWord) 
    {
        words.add(newWord);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.route;

import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name = "Route")
public class RouteNode {

    @ElementList(name = "Words", type = WordNode.class, required = false)
    private List<WordNode> words;

    public RouteNode() {
        words = new LinkedList<>();
    }

    public RouteNode(@ElementList(name = "Words") List<WordNode> words) {
        this.words = words;
    }

    public List<WordNode> getWords() {
        return words;
    }

    public void setWords(List<WordNode> words) {
        this.words = words;
    }

    public void addWord(WordNode newWord) {
        words.add(newWord);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.interfaces.models.route;

import java.util.List;

/**
 *
 * @author Zack
 */
public interface IRouteNode extends INode{
    List<IWordNode> getWords();
    void addWord(IWordNode newWord);
}

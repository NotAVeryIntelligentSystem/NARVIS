/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.interfaces.models.lang;

import com.narvis.dataaccess.interfaces.IDataProvider;
import com.narvis.dataaccess.models.lang.IgnoredWord;
import java.util.List;

/**
 *
 * @author Zack
 */
public interface IIgnoredWordsProvider extends IDataProvider {
    List<IIgnoredWord> getIgnoredWords();
}

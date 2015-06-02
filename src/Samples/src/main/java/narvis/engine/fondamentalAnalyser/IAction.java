/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.engine.fondamentalAnalyser;

import java.util.List;

/**
 *
 * @author Zack
 */
public interface IAction extends INode {
    String getProviderName();
    List<String> getAskFor();
}

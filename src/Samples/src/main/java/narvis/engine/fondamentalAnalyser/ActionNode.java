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
public class ActionNode implements IActionNode{
    private String providerName;
    public List<String> askFor;

    public ActionNode(String providerName)
    {
        this.providerName = providerName;
        askFor = new LinkedList<>();
    }
    
    @Override
    public String getProviderName() {
        return providerName;
    }

    @Override
    public List<String> getAskFor() {
        return askFor;
    }
    
    @Override
    public void setProdiverName(String providerName)
    {
        this.providerName = providerName;
    }
    
    @Override
    public void setAskFor(List<String> askFor)
    {
        this.askFor = askFor;
    }
}

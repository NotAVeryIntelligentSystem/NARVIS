/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.dataaccess.models.route;

import java.util.LinkedList;
import java.util.List;
import com.narvis.dataaccess.interfaces.models.route.IActionNode;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author Zack
 */
@Root(name = "Action")
public class ActionNode implements IActionNode{
    @Element(name = "Provider", type = String.class)
    private String providerName;
    @ElementList(name = "AskFor", required=false, type = String.class)
    public List<String> askFor;

    public ActionNode()
    {
        providerName = "";
        askFor = new LinkedList<>();
    }
    
    public ActionNode(@Element(name="Provider") String providerName, @ElementList(name="AskFor") List<String> askFor) {
        this.providerName = providerName;
        this.askFor = askFor;
    }
    
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

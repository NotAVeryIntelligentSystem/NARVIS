/*
 * The MIT License
 *
 * Copyright 2015 Zack.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.narvis.scripts;

import com.narvis.dataaccess.impl.RoutesProvider;
import com.narvis.dataaccess.models.conf.ApiKeys;
import com.narvis.dataaccess.models.conf.ModuleConf;
import com.narvis.dataaccess.models.layouts.ModuleErrors;
import com.narvis.dataaccess.models.route.ActionNode;
import com.narvis.dataaccess.models.route.RouteNode;
import com.narvis.dataaccess.models.route.WordNode;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Zack
 */
public class RoutesConf {

    public final static String MODULE_NAME = "Routes";
    public final static String MODULE_CLASS_PATH = RoutesProvider.class.getCanonicalName();

    public final static String ROUTES_DATA_PATH = "routes.xml";

    public RouteNode createRouteNode() {
        RouteNode retVal = new RouteNode();
        ActionNode currentAction;
        List<String> askFor = new LinkedList<>();

        retVal.addWord(
                createWordNode("give",
                        createWordNode(null,
                                createWordNode("weather", new ActionNode("OpenWeatherMap")))));
        retVal.addWord(
                createWordNode("bring",
                        createWordNode(null,
                                createWordNode("weather", new ActionNode("OpenWeatherMap")))));
        
        currentAction = new ActionNode("OpenWeatherMap");
        askFor = new LinkedList<>();
        askFor.add("temperature");
        currentAction.setAskFor(askFor);

        retVal.addWord(
                createWordNode("what",
                        createWordNode("is",
                                createWordNode("temperature", currentAction))));
        
        currentAction = new ActionNode("OpenWeatherMap");
        askFor = new LinkedList<>();
        askFor.add("cloud");
        currentAction.setAskFor(askFor);

        retVal.addWord(
                createWordNode("is",
                        createWordNode("it",
                                createWordNode("cloudy", currentAction))));

        retVal.addWord(
                createWordNode("how",
                        createWordNode("are",
                                createWordNode("you", new ActionNode("HardwareStatus")))));
        
        retVal.addWord(
                createWordNode("what",
                        createWordNode("is",
                                createWordNode("new", new ActionNode("News")))));        
        
        currentAction = new ActionNode("narvis");
        askFor = new LinkedList<>();
        askFor.add("learnsimilaritybetweenroutes");
        currentAction.setAskFor(askFor);

        retVal.addWord(
                createWordNode(null,
                        createWordNode("mean",
                                createWordNode(null, currentAction))));

        currentAction = new ActionNode("narvis");
        askFor = new LinkedList<>();
        askFor.add("learnuserlocation");
        currentAction.setAskFor(askFor);

        retVal.addWord(
                createWordNode("i",
                        createWordNode("live", currentAction)));

        currentAction = new ActionNode("narvis");
        askFor = new LinkedList<>();
        askFor.add("forgetuserdata");
        currentAction.setAskFor(askFor);

        retVal.addWord(
                createWordNode("forget",
                        createWordNode("me", currentAction)));

        return retVal;
    }

    private WordNode createWordNode(String name, ActionNode... actions) {
        WordNode retVal = new WordNode(name);
        for (ActionNode action : actions) {
            retVal.addAction(action);
        }
        return retVal;
    }

    private WordNode createWordNode(String name, WordNode... words) {
        WordNode retVal = new WordNode(name);
        for (WordNode word : words) {
            retVal.addWord(word);
        }
        return retVal;
    }

    public ModuleErrors createErrorsLayout() {

        ModuleErrors retVal = new ModuleErrors();

        retVal.getMap().put("general", "Hum... I'm sure you don't really need to know that");
        retVal.getMap().put("engine", "");
        retVal.getMap().put("data", "");
        retVal.getMap().put("persist", "I understand, but can't remember... it's probably due to alcohol");
        retVal.getMap().put("noanswers", "I don't know what you're talking about...");

        return retVal;
    }

    public ApiKeys createApiKeys() {
        ApiKeys retVal = new ApiKeys();

        retVal.setName(MODULE_NAME);

        return retVal;
    }

    public ModuleConf createModuleConf() {
        ModuleConf retVal = new ModuleConf();
        retVal.setModuleClassPath(MODULE_CLASS_PATH);

        retVal.getEntries().put("RoutesDataPath", ROUTES_DATA_PATH);

        return retVal;
    }
}

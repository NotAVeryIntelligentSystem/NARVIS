/*
 * The MIT License
 *
 * Copyright 2015 uwy.
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
package com.narvis.dataaccess.impl;

import com.narvis.common.tools.serialization.XmlFileAccess;
import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import com.narvis.dataaccess.interfaces.IDataProviderDetails;
import com.narvis.dataaccess.models.route.ActionNode;
import com.narvis.dataaccess.models.route.RouteNode;
import com.narvis.dataaccess.models.route.WordNode;
import com.narvis.engine.Action;
import com.narvis.engine.FondamentalAnalyser;
import com.narvis.engine.Parser;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Zack
 */
public class RoutesProvider implements IDataProviderDetails, IDataModelProvider<RouteNode> {
    //private final static String ROUTESPATH = "src\\test\\java\\com\\narvis\\test\\dataaccess\\models\\route\\routes.xml"; // Le chemin d'accès au fichier XML contenant les routes
    private final RouteNode routes;
    private final ModuleConfigurationDataProvider conf;
    
    private final FondamentalAnalyser fondamentalAnalyser;
    private final Parser parser;
    
    public RoutesProvider(ModuleConfigurationDataProvider conf) throws ParserConfigurationException, SAXException, IOException, Exception{
        this.conf = conf;
        this.routes = XmlFileAccess.fromFile(RouteNode.class, new File(this.conf.getDataFolder(), this.getRoutesDataPath()));
        
        this.fondamentalAnalyser = new FondamentalAnalyser();
        this.parser = new Parser();
    }
    
    private String getRoutesDataPath() {
        return this.conf.getData("Conf", "RoutesDataPath");
    }

    @Override
    public void persist() {
        try {
            XmlFileAccess.toFile(this.routes, new File(this.conf.getDataFolder(), this.getRoutesDataPath()));
        } catch (Exception ex) {
            NarvisLogger.getInstance().log(Level.SEVERE, ex.toString());
        }
    }

    @Override
    public String getData(String... keywords) {
        return this.routes.toString();
    }

    @Override
    public RouteNode getModel(String... keywords) {
        return this.routes;
    }

    
    @Override
    public String getDataDetails(Map<String, String> detailsToValue, String... keywords) {

        if(keywords.length == 0)
        {
            return "Fuck fuck fuck...";
        }
        
        String command = keywords[0];
        
        switch(command)
        {
            case "add":
                break;
            case "similarity":
                break;
            default:
                return "Fuck fuck fuck...";
        }
        
        return "";
    }

    /**
     * Créé des relations de similitude entre plusieurs phrases. Pour cela, il faut qu'au moins une des phrase soit déjà connue.
     * @param pParsedSentences : Liste de phrases préalablement parsées
     */
    public void createSimilarityBetween(List<List<String>> pParsedSentences){
        Action findedAction = null,     // Première action trouvée
               currentAction = null;    // Action correspondant à la phrase courrante
        int iSentence = 0,              // Indice de la phrase courrante
            iFindedSentence = -1;       // Indice de la phrase correspondant à l'action trouvée
        
        /* Pour chaque phrase, on en recherche une correspondant à une action */
        for(List<String> parsedSentence : pParsedSentences)
        {
            currentAction = fondamentalAnalyser.findAction(parsedSentence);
            
            /* Si une action est trouvée pour la première fois */
            if(findedAction == null && currentAction != null){
                /* La première action trouvée devient l'action courrante */
                findedAction = currentAction;
                /* L'indice de la phrase correspondant à l'action trouvée devient l'indice de la phrase courrante */
                iFindedSentence = iSentence;
            
            /* Si une action a déjà été trouvée ET qu'on trouve une nouvelle, il y a ERREUR */
            }else if(findedAction != null && currentAction != null){
                NarvisLogger.getInstance().warning("Plusieurs phrases correspondent déjà à une action...");
                return;
            }
            iSentence++;
        }
        
        /* Si aucune action n'est trouvée, il y a ERREUR */
        if(findedAction == null)
        {
            NarvisLogger.getInstance().warning("Aucune phrase n'est déjà connue...");
            return;
        }
        
        /* On retire de la liste la phrase déjà connnue */
        pParsedSentences.remove(iFindedSentence);

        /* On récupère l'arbre des routes */
        RouteNode route = this.getModel();
        List<WordNode> words = route.getWords();
        
        /*
        On parcour les noeuds du premier niveau de l'arbre avant de faire appel à la fonction récurcive (searchPath()).
        On doit faire ça parceque la racine de l'arbre (RouteNode) n'est pas du même type que le paramètre attendu par
        la fonction récurcive, qui est un mot (WordNode).
        */
        
        /* Pour chaque phrase, on créé une route avec comme finalitée l'action connue */
        for(List<String> parsedSentence : pParsedSentences){
            
            boolean isFound = false;
        
            if(parsedSentence.size() > 0){
                final String currentSentenceWord = parsedSentence.get(0);
                parsedSentence.remove(0);
                
                for (WordNode routesWord : words) {

                    if(routesWord.getValue() == null || routesWord.getValue().isEmpty() || routesWord.getValue().equals(currentSentenceWord)){
                        createPath(routesWord, parsedSentence, findedAction);
                        isFound = true;
                        break;
                    }
                }

                /* Si aucun noeud enfant ne correspond au mot, on créé un nouveau noeud */
                if(!isFound){
                    WordNode newWordNode;

                    if(!currentSentenceWord.equals("something") && !currentSentenceWord.equals("someone")){
                        newWordNode = new WordNode(currentSentenceWord);
                    }else{
                        newWordNode = new WordNode(null);
                    }

                    route.addWord(newWordNode);

                    createPath(newWordNode, parsedSentence, findedAction);
                }
            }
        }
        
        /* On remplace avec le nouvel arbre des routes */
        this.persist();
    }
    
    
    /**
     * Ajoute un nouveau chemin aux routes.
     * @param wordNode : Le noeud de l'arbre sur lequel ajouter la route
     * @param parsedSentence : La phrase préalablement parsée depuis laquelle doit être créée la route
     * @param action : L'action à ajouter à la fin de la nouvelle route
     */
    private void createPath(WordNode wordNode, List<String> parsedSentence, Action action)
    {
        final List<WordNode> routesWords = wordNode.getWords();
        boolean isFound = false;
        
        if(parsedSentence.size() > 0){
            final String currentSentenceWord = parsedSentence.get(0);
            parsedSentence.remove(0);
            
            for (WordNode routesWord : routesWords) {
                
                if(routesWord.getValue() == null || routesWord.getValue().isEmpty() || routesWord.getValue().equals(currentSentenceWord)){
                    createPath(routesWord, parsedSentence, action);
                    isFound = true;
                    break;
                }
            }
            
            /* Si aucun noeud enfant ne correspond au mot, on créé un nouveau noeud */
            if(!isFound){
                WordNode newWordNode;
             
                if(!currentSentenceWord.equals("something") && !currentSentenceWord.equals("someone")){
                    newWordNode = new WordNode(currentSentenceWord);
                }else{
                    newWordNode = new WordNode(null);
                }

                wordNode.addWord(newWordNode);
                
                createPath(newWordNode, parsedSentence, action);
            }
        }else{
        
            /* On fois qu'on a finit de générer le chemin, on ajoute l'action à la fin */          
            ActionNode newActionNode = new ActionNode(action.getProviderName());
            newActionNode.setAskFor(action.getPrecisions());

            wordNode.addAction(newActionNode);
        }
    }
    
}

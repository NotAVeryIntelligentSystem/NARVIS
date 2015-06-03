/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.engine;

import com.narvis.dataaccess.models.route.ActionNode;
import com.narvis.dataaccess.models.route.WordNode;
import com.narvis.dataaccess.models.route.RoutesProvider;
import java.io.*;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import com.narvis.common.generics.NarvisLogger;
import com.narvis.dataaccess.interfaces.models.route.IActionNode;
import com.narvis.dataaccess.interfaces.models.route.IRouteNode;
import com.narvis.dataaccess.interfaces.models.route.IRoutesProvider;
import com.narvis.dataaccess.interfaces.models.route.IWordNode;
import org.xml.sax.SAXException;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class FondamentalAnalyser {
    private final IRoutesProvider routesProvider;
    
    private String providerName = "";
    private List<String> askFor = null;
    private List<String> details = null; // La phrase parsée à partir de laquelle l'arbre est parcouru. Elle devient par la suite la liste des détails de la phrase.
    
    public FondamentalAnalyser() throws ParserConfigurationException, SAXException, IOException{        
        routesProvider = new RoutesProvider();
    }
    
    /**
     * Recherche le provider et les informations attendues en fonction de la phrase en entrée.
     * @param pParsedSentence : La phrase ayant préalablement été parsée
     * @return Retourne l'action correspondant à la phrase, ou NULL si aucune n'est trouvée.
     */
    public Action findAction(List<String> pParsedSentence){
        //Action action = null;
        details = pParsedSentence;
        List<IWordNode> rootWords = routesProvider.getRouteNode().getWords();
        IActionNode action = null;
        Action implAction = null;
        
        for(IWordNode word : rootWords)
        {
            final String currentSentenceWord = details.get(0);
            if(word.getValue() == null || word.getValue().isEmpty() || word.getValue().equals(currentSentenceWord))
            {
                action = searchPath(word, 1);

                if(action != null)
                {
                    if(word.getValue() != null || !word.getValue().isEmpty())
                    {
                        details.remove(0);
                    }
                    break;
                }
            }
        }

        if(action != null){
            providerName = action.getProviderName();
            
            askFor = action.getAskFor();
            
            implAction = new Action(providerName, askFor, details);
        }else{
            providerName = "";
            askFor = null;
            details = null;
        }
        
        return implAction;
    }
    
     /**
     * Créé des relations de similitude entre plusieurs phrases. Pour cela, il faut qu'au moins une des phrase soit déjà connue.
     * @param pParsedSentences : Liste de phrases préalablement parsées
     */
    public void createSimilarityBetween(List<List<String>> pParsedSentences){
        Action findedAction = null, currentAction = null;
        int i = 0, indexOfKnownSentence = -1;
        
        /* Pour chaque phrase, on en recherche une correspondant à une action */
        for(List<String> parsedSentence : pParsedSentences){
            currentAction = findAction(parsedSentence);
            if(findedAction == null && currentAction != null){
                findedAction = currentAction;
                indexOfKnownSentence = i;
            }else if(findedAction != null && currentAction != null){
                NarvisLogger.getInstance().warning("Plusieurs phrases correspondent déjà à une action...");
                return;
            }
            i++;
        }
        
        if(findedAction == null){
            NarvisLogger.getInstance().warning("Aucune phrase n'est déjà connue...");
            return;
        }
        
        /* On retire de la liste la phrase déjà connnue */
        pParsedSentences.remove(indexOfKnownSentence);

        /* On récupère l'arbre des routes */
        IRouteNode route = routesProvider.getRouteNode();
        List<IWordNode> words = route.getWords();
        
        /* Pour chaque phrase, on créé une route avec comme finalitée l'action connue */
        for(List<String> parsedSentence : pParsedSentences){
            
            boolean isFound = false;
        
            if(parsedSentence.size() > 0){
                final String currentSentenceWord = parsedSentence.get(0);
                parsedSentence.remove(0);
                
                for (IWordNode routesWord : words) {

                    if(routesWord.getValue() == null || routesWord.getValue().isEmpty() || routesWord.getValue().equals(currentSentenceWord)){
                        createPath(routesWord, parsedSentence, findedAction);
                        isFound = true;
                        break;
                    }
                }

                /* Si aucun noeud enfant ne correspond au mot, on créé un nouveau noeud */
                if(!isFound){
                    IWordNode newWordNode;

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
        routesProvider.setRouteNode(route);
    }
    
     /**
     * Enregistre l'état des routes dans le fichier XML
     */
    public void saveRoutes(){
        routesProvider.persist();
    }
    
    /**
     * Parcour l'arbre des routes en fonction des mots de la phrase.
     * Lorsqu'il n'y a plus de branche à parcourir, on remonte l'arbre jusqu'à tomber sur une action.
     * @param pBranche : La branche de l'arbre à parcourir
     * @param pParsedSentence : La phrase ayant préalablement été parsée
     * @param iWord : L'indice du mot courrant
     * @return Un noeud de l'arbre correspondant à une action, ou NULL si aucune action n'est trouvée.
     */
    private IActionNode searchPath(IWordNode pWord, int iWord){
        
        final List<IWordNode> words = pWord.getWords();

        IActionNode action = null;

        if(iWord < details.size()){
            final String currentSentenceWord = details.get(iWord);
            for (IWordNode currentWordNode : words) {
                if(currentWordNode.getValue() == null || currentWordNode.getValue().isEmpty()){
                    action = searchPath(currentWordNode, iWord+1);
                    break;
                    
                }else if(currentWordNode.getValue().equals(currentSentenceWord)){
                    details.remove(iWord);
                    action = searchPath(currentWordNode, iWord);
                    break;
                    
                }
            }
        }
        
        if(action == null){
            final List<IActionNode> actions = pWord.getActions();
            
            if(actions.size() > 0)
            {
                action = actions.get(0);
            }
        }
        
        return action;
    }
    
    /**
     * Ajoute un nouveau chemin.
     * @param pWordNode : Le noeud de l'arbre à parcourir
     * @param pParsedSentence : La phrase préalablement parsée
     * @param pAction : L'action correspondante
     */
    private void createPath(IWordNode pWordNode, List<String> pParsedSentence, Action pAction)
    {
        final List<IWordNode> routesWords = pWordNode.getWords();
        boolean isFound = false;
        
        if(pParsedSentence.size() > 0){
            final String currentSentenceWord = pParsedSentence.get(0);
            pParsedSentence.remove(0);
            
            for (IWordNode routesWord : routesWords) {
                
                if(routesWord.getValue() == null || routesWord.getValue().isEmpty() || routesWord.getValue().equals(currentSentenceWord)){
                    createPath(routesWord, pParsedSentence, pAction);
                    isFound = true;
                    break;
                }
            }
            
            /* Si aucun noeud enfant ne correspond au mot, on créé un nouveau noeud */
            if(!isFound){
                IWordNode newWordNode;
             
                if(!currentSentenceWord.equals("something") && !currentSentenceWord.equals("someone")){
                    newWordNode = new WordNode(currentSentenceWord);
                }else{
                    newWordNode = new WordNode(null);
                }

                pWordNode.addWord(newWordNode);
                
                createPath(newWordNode, pParsedSentence, pAction);
            }
        }else{
        
            /* On fois qu'on a finit de générer le chemin, on ajoute l'action à la fin */          
            ActionNode newActionNode = new ActionNode(pAction.getProviderName());
            newActionNode.setAskFor(pAction.getPrecisions());

            pWordNode.addAction(newActionNode);
        }
    }
    
}

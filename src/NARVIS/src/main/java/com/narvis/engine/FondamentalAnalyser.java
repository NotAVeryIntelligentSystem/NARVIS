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
package com.narvis.engine;

import com.narvis.common.debug.NarvisLogger;
import com.narvis.dataaccess.DataAccessFactory;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.PersistException;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import com.narvis.dataaccess.interfaces.IMetaDataProvider;
import com.narvis.dataaccess.models.route.ActionNode;
import com.narvis.dataaccess.models.route.RouteNode;
import com.narvis.dataaccess.models.route.WordNode;
import com.narvis.engine.exception.AmbigousException;
import com.narvis.engine.exception.NoActionException;
import com.narvis.engine.exception.NoSentenceException;
import java.io.*;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 * Module permettant d'analyser des phrases parsées au préalable afin de
 * sélectionner le module qui permettra de répondre à la demande
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class FondamentalAnalyser {

    private final IDataModelProvider<RouteNode> routesProvider;

    private String providerName = "";
    private List<String> askFor = null;
    private List<String> details = null; // La phrase parsée à partir de laquelle l'arbre est parcouru. Elle devient par la suite la liste des détails de la phrase.

    /**
     * Default constructor
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws Exception
     */
    public FondamentalAnalyser() throws ParserConfigurationException, SAXException, IOException, Exception {
        // Récupération du RoutesProvider
        IMetaDataProvider metaDataProvider = DataAccessFactory.getMetaDataProvider();
        this.routesProvider = (IDataModelProvider<RouteNode>) metaDataProvider.getDataProvider("Routes");
    }

    /**
     * Recherche le provider et les informations attendues en fonction de la
     * phrase en entrée.
     *
     * @param pParsedSentence : La phrase ayant préalablement été parsée
     * @return Retourne l'action correspondant à la phrase, ou NULL si aucune
     * n'est trouvée.
     * @throws com.narvis.dataaccess.exception.NoDataException
     * @throws com.narvis.engine.exception.NoActionException
     * @throws com.narvis.engine.exception.NoSentenceException
     */
    public Action findAction(List<String> pParsedSentence) throws NoDataException, NoActionException, NoSentenceException {

        RouteNode route;            // Root of the route model
        ActionNode action = null;   // Action node that correspond to the sentence
        Action implAction = null;   // Returned action that correspond to the sentence

        /*
         If the sentence in params is NULL or empty, return NULL.
         Because no route could ever match an empty sentence.      
         */
        if (pParsedSentence == null || pParsedSentence.isEmpty()) {
            throw new NoSentenceException("Empty sentence", "Gonna speak to me or not ?");
        }

        /*
         Initialize the details with the parsed sentence.
         When an action is finded in a route, all the words that were used in
         the route are removed from details.
         So, when an action is finded there are in details only the words that
         correspond to the real details.
         */
        details = pParsedSentence;

        /* Get route model */
        route = routesProvider.getModel();

        /* Get WordNodes children of the root */
        List<WordNode> rootWords = route.getWords();

        /*
        Brows nodes of the first level of the tree before calling the recurcive
        function (searchPath() ).
        We have to do this because the root (RouteNode) hasn't the same type
        as the param expected by the recurcive function, that is a WordNode.
        */
        WordNode jockerWordNode = null;
        
        for(WordNode word : rootWords)
        {
            /* Get the first word of the sentence */
            final String currentSentenceWord = details.get(0);
            
            /* If the word is empty, it's a "joker" we gonna use at the end */
            if(word.getValue() == null || word.getValue().isEmpty())
            {
                jockerWordNode = word;
            }
            
            /* If the word is equals the current word */
            if(word.getValue() != null && word.getValue().equals(currentSentenceWord))
            {
                /* Search an action that match the sentence */
                action = searchPath(word, 1);

                /* If an action is finded */
                if (action != null) {
                    /* If the node isn't empty */
                    if (word.getValue() != null || !word.getValue().isEmpty()) {
                        /* Remove the current word in the details because he was used by the fondamental analyser */
                        details.remove(0);
                    }
                    break;
                }
            }
        }
        
        /* If we didn't find any action and we have a joker, we try to find an action with it */
        if(jockerWordNode != null && action == null)
        {
            action = searchPath(jockerWordNode, 1);
        }

        /* If no action is found */
        if (action != null) {
            /* Récupère le nom de provider correspondant au noeud action */
            providerName = action.getProviderName();

            /* Récupère les précisions correspondants au noeud action */
            askFor = action.getAskFor();

            /* Créé une action avec le contenu du noeud action trouvé */
            implAction = new Action(providerName, askFor, details);

            /* Sinon, Reset de toutes les valeurs */
        } else {
            providerName = "";
            askFor = null;
            details = null;
            throw new NoActionException("No action correspondance", "I don't understand what you're asking for...");
        }

        return implAction;
    }
    
    /**
     * Enregistre l'état des routes dans le fichier XML
     * @throws com.narvis.dataaccess.exception.PersistException
     */
    public void saveRoutes() throws PersistException {
        routesProvider.persist();
    }

    /**
     * Créé des relations de similitude entre plusieurs phrases. Pour cela, il
     * faut qu'au moins une des phrase soit déjà connue.
     *
     * @param pParsedSentences : Liste de phrases préalablement parsées
     * @throws com.narvis.dataaccess.exception.NoDataException
     * @throws com.narvis.dataaccess.exception.PersistException
     * @throws com.narvis.engine.exception.NoActionException
     * @throws com.narvis.engine.exception.NoSentenceException
     */
    public void createSimilarityBetween(List<List<String>> pParsedSentences) throws NoDataException, PersistException, NoActionException, NoSentenceException, AmbigousException {
        Action findedAction = null,     // Première action trouvée
               currentAction = null;    // Action correspondant à la phrase courrante
        int iSentence = 0,              // Indice de la phrase courrante
            iFindedSentence = -1;       // Indice de la phrase correspondant à l'action trouvée
        
        /* Pour chaque phrase, on en recherche une correspondant à une action */
        for(List<String> parsedSentence : pParsedSentences)
        {
            try{
                currentAction = this.findAction(parsedSentence);
                
                /* Si une action est trouvée pour la première fois */
                if(findedAction == null && currentAction != null){
                    /* La première action trouvée devient l'action courrante */
                    findedAction = currentAction;
                    /* L'indice de la phrase correspondant à l'action trouvée devient l'indice de la phrase courrante */
                    iFindedSentence = iSentence;

                /* Si une action a déjà été trouvée ET qu'on trouve une nouvelle, il y a ERREUR */
                }else if(findedAction != null && currentAction != null){
                    NarvisLogger.getInstance().getLogger().warning("Ambigous sentences, there is more than one sentence that correspond to an action");
                    throw new AmbigousException("There is more than one sentence that correspond to an action", "I already know that...");
                }
            
            }catch(NoActionException ex){
                
            }
            
            iSentence++;
        }
        
        /* Si aucune action n'est trouvée, il y a ERREUR */
        if(findedAction == null)
        {
            NarvisLogger.getInstance().getLogger().warning("Aucune phrase n'est déjà connue...");
            throw new NoActionException("No action finded", "I don't know any of your sentences...");
        }
        
        /* On retire de la liste la phrase déjà connnue */
        pParsedSentences.remove(iFindedSentence);

        /* On récupère l'arbre des routes */
        RouteNode route = routesProvider.getModel();
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

                WordNode jokerWordNode = null;
                for (WordNode routesWord : words) {
                    /* If the word is empty, it's a "joker" we gonna use at the end */
                    if(routesWord.getValue() == null || routesWord.getValue().isEmpty())
                    {
                        jokerWordNode = routesWord;
                    }
                
                    if(routesWord.getValue() != null && routesWord.getValue().equals(currentSentenceWord)){
                        createPath(routesWord, parsedSentence, findedAction);
                        isFound = true;
                        break;
                    }
                }
                
                if(!isFound && jokerWordNode != null && isJokerWord(currentSentenceWord)){
                    createPath(jokerWordNode, parsedSentence, findedAction);
                }                
                /* Si aucun noeud enfant ne correspond au mot, on créé un nouveau noeud */
                else if(!isFound){
                    WordNode newWordNode;

                    if(!isJokerWord(currentSentenceWord)){
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
        routesProvider.persist();
    }

    /**
     * Parcour l'arbre des routes en fonction des mots de la phrase. Si une
     * action est trouvée lors du parcour des routes, elle est retournée.
     * Lorsqu'il n'y a plus de branche à parcourir, on remonte l'arbre jusqu'à
     * tomber sur une action.
     *
     * @param wordNode : La noeud de l'arbre à parcourir
     * @param pParsedSentence : La phrase ayant préalablement été parsée
     * @param iWord : L'indice du mot courrant de la phrase
     * @return Un noeud de l'arbre correspondant à une action, ou NULL si aucune
     * action n'est trouvée.
     */
    private ActionNode searchPath(WordNode wordNode, int iWord) {

        List<WordNode> wordNodeChildren;    // Les noeud mots enfant du noeud mot courrant
        ActionNode action = null;           // Noeud action correspondant à la phrase

        wordNodeChildren = wordNode.getWords();

        /* Si il reste des mots à analyser dans la phrase */
        if (iWord < details.size()) {
            final String currentSentenceWord = details.get(iWord);
            
            WordNode jockerWordNode = null;
            for (WordNode currentWordNode : wordNodeChildren) {
                /* If the word is empty, it's a "joker" we gonna use at the end */
                if(currentWordNode.getValue() == null || currentWordNode.getValue().isEmpty())
                {
                    jockerWordNode = currentWordNode;
                }
            
                if(currentWordNode.getValue() != null && currentWordNode.getValue().equals(currentSentenceWord)){
                    details.remove(iWord);
                    action = searchPath(currentWordNode, iWord);
                    break;

                }
            }
            
            /* If we didn't find any action and we have a joker, we try to find an action with it */
            if(jockerWordNode != null && action == null)
            {
                action = searchPath(jockerWordNode, iWord+1);
            }
        }

        /* Si aucune action n'a été trouvée, on recherche une action sur le noeud courrant */
        if (action == null) {
            final List<ActionNode> actions = wordNode.getActions();

            if (actions.size() > 0) {
                action = actions.get(0);
            }
        }

        return action;
    }

    /**
     * Ajoute un nouveau chemin aux routes.
     *
     * @param wordNode : Le noeud de l'arbre sur lequel ajouter la route
     * @param parsedSentence : La phrase préalablement parsée depuis laquelle
     * doit être créée la route
     * @param action : L'action à ajouter à la fin de la nouvelle route
     */
    private void createPath(WordNode wordNode, List<String> parsedSentence, Action action) {
        final List<WordNode> routesWords = wordNode.getWords();
        boolean isFound = false;
        
        if(parsedSentence.size() > 0){
            final String currentSentenceWord = parsedSentence.get(0);
            parsedSentence.remove(0);
            
            WordNode jokerWordNode = null;
            for (WordNode routesWord : routesWords) {
                /* If the word is empty, it's a "joker" we gonna use at the end */
                if(routesWord.getValue() == null || routesWord.getValue().isEmpty())
                {
                    jokerWordNode = routesWord;
                }
                    
                if(routesWord.getValue() != null && routesWord.getValue().equals(currentSentenceWord)){
                    createPath(routesWord, parsedSentence, action);
                    isFound = true;
                    break;
                }
            }
            
            if(!isFound && jokerWordNode != null && isJokerWord(currentSentenceWord)){
                createPath(jokerWordNode, parsedSentence, action);
            }
            
            /* Si aucun noeud enfant ne correspond au mot, on créé un nouveau noeud */
            else if(!isFound){
                WordNode newWordNode;
             
                if(!isJokerWord(currentSentenceWord)){
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

    
    private boolean isJokerWord(String word)
    {
        return word.equals("something") || word.equals("someone");
    }
}

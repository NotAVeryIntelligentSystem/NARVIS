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

import com.narvis.dataaccess.DataAccessFactory;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.PersistException;
import com.narvis.dataaccess.interfaces.dataproviders.IDataModelProvider;
import com.narvis.dataaccess.interfaces.IMetaDataProvider;
import com.narvis.dataaccess.models.route.ActionNode;
import com.narvis.dataaccess.models.route.RouteNode;
import com.narvis.dataaccess.models.route.WordNode;
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
     * Search a pattern in the routes that match the sentence
     * @param pParsedSentence
     * @return The action corresponding to the pattern
     * @throws NoDataException
     * @throws NoActionException
     * @throws NoSentenceException 
     */
    public Action findAction(List<String> pParsedSentence) throws NoDataException, NoActionException, NoSentenceException {
        ActionNode action = null;   // Action node that correspond to the sentence
        Action implAction = null;   // Returned action that correspond to the sentence
        int offsetSentence = 0;
        
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
        
        while(offsetSentence < pParsedSentence.size() && action==null ){
            action = this.findAction(offsetSentence);
            offsetSentence++;
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
    private ActionNode findAction(int sentenceOffset) throws NoDataException, NoActionException, NoSentenceException {

        RouteNode route;            // Root of the route model
        ActionNode action = null;   // Action node that correspond to the sentence

        /* Get route model */
        route = routesProvider.getModel();

        /* Get WordNodes children of the root */
        List<WordNode> rootWords = route.getWords();
        
        /* If we still have a word to compare in the sentence */
        if (sentenceOffset < details.size()) {

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
                final String currentSentenceWord = details.get(sentenceOffset);

                /* If the word is empty, it's a "joker" we gonna use at the end */
                if(word.getValue() == null || word.getValue().isEmpty())
                {
                    jockerWordNode = word;
                }

                /* If the word is equals the current word */
                if(word.getValue() != null && word.getValue().equals(currentSentenceWord))
                {
                    /* Search an action that match the sentence */
                    action = searchPath(word, sentenceOffset+1);

                    /* If an action is finded */
                    if (action != null) {
                        /* If the node isn't empty */
                        if (word.getValue() != null || !word.getValue().isEmpty()) {
                            /* Remove the current word in the details because he was used by the fondamental analyser */
                            details.remove(sentenceOffset);
                        }
                        break;
                    }
                }
            }

            /* If we didn't find any action and we have a joker, we try to find an action with it */
            if(jockerWordNode != null && action == null)
            {
                action = searchPath(jockerWordNode, sentenceOffset+1);
            }
        }

        return action;
    }
    
    /**
     * Brows the routes tree to match the words of the sentence with an action.
     * When there is no more node that match in the tree, we get back to the last action we see.
     *
     * @param wordNode : The node to brows
     * @param pParsedSentence : The sentence to match
     * @param sentenceOffset : The index of the word we're looking for in the sentence
     * @return An action that match the sentence, or null if none is find
     */
    private ActionNode searchPath(WordNode wordNode, int sentenceOffset) {

        List<WordNode> wordNodeChildren;    // Les noeud mots enfant du noeud mot courrant
        ActionNode action = null;           // Noeud action correspondant à la phrase

        wordNodeChildren = wordNode.getWords();

        /* Si il reste des mots à analyser dans la phrase */
        if (sentenceOffset < details.size()) {
            final String currentSentenceWord = details.get(sentenceOffset);
            
            WordNode jockerWordNode = null;
            for (WordNode currentWordNode : wordNodeChildren) {
                /* If the word is empty, it's a "joker" we gonna use at the end */
                if(currentWordNode.getValue() == null || currentWordNode.getValue().isEmpty())
                {
                    jockerWordNode = currentWordNode;
                }
            
                if(currentWordNode.getValue() != null && currentWordNode.getValue().equals(currentSentenceWord)){
                    details.remove(sentenceOffset);
                    action = searchPath(currentWordNode, sentenceOffset);
                    break;

                }
            }
            
            /* If we didn't find any action and we have a joker, we try to find an action with it */
            if(jockerWordNode != null && action == null)
            {
                action = searchPath(jockerWordNode, sentenceOffset+1);
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
     * Enregistre l'état des routes dans le fichier XML
     * @throws com.narvis.dataaccess.exception.PersistException
     */
    public void saveRoutes() throws PersistException {
        routesProvider.persist();
    }
    
    public IDataModelProvider<RouteNode> getRoutesProvider()
    {
        return this.routesProvider;
    }
}

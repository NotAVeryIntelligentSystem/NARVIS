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
import com.narvis.dataaccess.exception.*;
import com.narvis.dataaccess.models.route.*;;
import com.narvis.dataaccess.models.user.*;
import com.narvis.dataaccess.models.user.UsersData;
import com.narvis.engine.exception.AmbigousException;
import com.narvis.engine.exception.EngineException;
import com.narvis.engine.exception.NoActionException;
import com.narvis.engine.exception.NoDetailsException;
import com.narvis.engine.exception.NoSentenceException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Zack
 */
public class InternalActionsExecuter {
    NarvisEngine narvisEngine;
    private final String LOCATION_STRING = "location";
    private final String USERNAME_STRING = "username";
    
    public InternalActionsExecuter(NarvisEngine narvisEngine){
        this.narvisEngine = narvisEngine;
    }
    
    /**
     * Guide NARVIS to choose the right internal action.
     * @param action : The action to execute
     * @param detailsTypes
     * @return The answer
     * @throws com.narvis.engine.exception.EngineException
     * @throws com.narvis.dataaccess.exception.ProviderException
     */ 
    public String executeInternalAction(Action action, Map<String, String> detailsTypes) throws EngineException, ProviderException
    {
        String answer = "";
        if(action.getPrecisions().isEmpty())
            throw new NoActionException("No internal action found", "I don't understand what you're asking for");
        
        switch(action.getPrecisions().get(0))
        {
            case "learnsimilaritybetweenroutes":
                answer = learnSimilarityBetweenRoutes(detailsTypes);
                break;
            case "learnuserlocation":
                answer = learnUserLocation(detailsTypes);
                break;
            case "forgetuserdata":
                answer = forgetUser(detailsTypes);
                break;
            default:
                throw new NoActionException("No internal action found", "I don't understand what you're asking for"); 
        }
        
        return answer;
    }
    
    
    public String forgetUser(Map<String, String> detailsTypes) throws NoDataException, PersistException
    {
        String successAnswer = "Okay, good by";
        
        String username = "";
        if((username = lookForUSerName( detailsTypes )) == null)
            throw new NoDataException("No username found", "I'm sorry but, who are you ?!");
        
        UsersData usersData = narvisEngine.getDetailsAnalyser().getUserDataProvider().getModel();
        
        usersData.removeUser(username);
        
        /* Remember to save the modification */
        narvisEngine.getDetailsAnalyser().getUserDataProvider().persist();
        
        return successAnswer;
    }
    
    /**
     * Learn the location of a user
     * @param detailsTypes
     * @return The success answer if the action has succesfuly
     * @throws com.narvis.dataaccess.exception.NoDataException
     * @throws com.narvis.engine.exception.EngineException
     * @throws com.narvis.dataaccess.exception.PersistException
     */
    public String learnUserLocation(Map<String, String> detailsTypes) throws EngineException, PersistException, NoDataException
    {
        String successAnswer = "I'll remember that";
        
        String location = lookForValueLocation( detailsTypes );
        String username = lookForUSerName( detailsTypes );
        
        if(location == null)
            throw new NoDetailsException("No location found", "I don't understand, please precise your location");
        
        if(username == null)
            throw new NoDetailsException("No username found", "I'm sorry but, who are you ?!");
        
        UsersData usersData = narvisEngine.getDetailsAnalyser().getUserDataProvider().getModel();
        UserData userData = usersData.getUser(username);
        
        /* If we can't find a user, we create it */
        if(userData == null)
        {
            usersData.addUser(username);
            
            /* If even after the creation there is no user correspondance, there is a problem... */
            if((userData = usersData.getUser(username)) == null)
            {
                throw new EngineException("Add user impossible", "Sorry I can't remember your personnal informations...");
            }
        }
        
        /* We add (or update) the location for the user */
        userData.addData(LOCATION_STRING, location);
        
        /* Remember to save the modification */
        narvisEngine.getDetailsAnalyser().getUserDataProvider().persist();
        
        return successAnswer;
    }
    
    /**
     * Search in the map if there is a key that has for value "location"
     * @param details
     * @return The key that correspond, or null if there is none
     */
    private String lookForValueLocation( Map<String,String> details ) {
        
        for( Map.Entry<String, String> entry : details.entrySet() ) {

            if( entry.getValue().equals(LOCATION_STRING) ) {
                return entry.getKey();
            }
        }
        
        return null;
        
        
    }
    
        /**
     * Search in the map if there is a key that has for value "username"
     * @param details
     * @return The key that correspond, or null if there is none
     */
    private String lookForUSerName( Map<String,String> details ) {
        
        for( Map.Entry<String, String> entry : details.entrySet() ) {

            if( entry.getValue().equals(USERNAME_STRING) ) {
                return entry.getKey();
            }
        }
        
        return null;
        
        
    }
    
    /**
     * Learn the similarity between sentences that are passed in details.
     * @param details
     * @return The success answer if the action has succesfuly
     * @throws EngineException
     * @throws NoDataException
     * @throws PersistException 
     */
    public String learnSimilarityBetweenRoutes(Map<String, String> details) throws EngineException, ProviderException
    {
        String successAnswer = "I've learned this similarity";
        
        this.createSimilarityBetweenRoutes(narvisEngine.getParser().getParsedSentencesFromDetails(details));
        
        return successAnswer;
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
    private void createSimilarityBetweenRoutes(List<List<String>> pParsedSentences) throws NoDataException, PersistException, NoActionException, NoSentenceException, AmbigousException {
        Action findedAction = null;     // Première action trouvée
        int iSentence = 0,              // Indice de la phrase courrante
            iFindedSentence = -1;       // Indice de la phrase correspondant à l'action trouvée
        
        /* Pour chaque phrase, on en recherche une correspondant à une action */
        for(List<String> parsedSentence : pParsedSentences)
        {
            try{
                Action currentAction = narvisEngine.getFondamentalAnalyser().findAction(parsedSentence);   // Action correspondant à la phrase courrante
                
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
        RouteNode route = narvisEngine.getFondamentalAnalyser().getRoutesProvider().getModel();
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
        narvisEngine.getFondamentalAnalyser().saveRoutes();
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

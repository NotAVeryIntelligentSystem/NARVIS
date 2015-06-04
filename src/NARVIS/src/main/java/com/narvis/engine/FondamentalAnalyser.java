/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.engine;

import com.narvis.common.generics.NarvisLogger;
import com.narvis.dataaccess.DataAccessFactory;
import com.narvis.dataaccess.interfaces.IDataModelProvider;
import com.narvis.dataaccess.models.route.ActionNode;
import com.narvis.dataaccess.models.route.RouteNode;
import com.narvis.dataaccess.models.route.WordNode;
import java.io.*;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;


import org.xml.sax.SAXException;

/**
 * Module permettant d'analyser des phrases parsées au préalable afin de sélectionner le module qui permettra de répondre à la demande
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class FondamentalAnalyser {
    private final IDataModelProvider<RouteNode> routesProvider;
    
    private String providerName = "";
    private List<String> askFor = null;
    private List<String> details = null; // La phrase parsée à partir de laquelle l'arbre est parcouru. Elle devient par la suite la liste des détails de la phrase.
    
    /**
     * Constructeur par défaut
     * @throws ParserConfigurationException : Le provider de configuration du module
     * @throws SAXException
     * @throws IOException
     * @throws Exception 
     */
    public FondamentalAnalyser() throws ParserConfigurationException, SAXException, IOException, Exception
    {
        // Récupération du RoutesProvider
        this.routesProvider = (IDataModelProvider<RouteNode>) DataAccessFactory.getMetaDataProvider().getDataProvider("Routes");
    }
    
    /**
     * Recherche le provider et les informations attendues en fonction de la phrase en entrée.
     * @param pParsedSentence : La phrase ayant préalablement été parsée
     * @return Retourne l'action correspondant à la phrase, ou NULL si aucune n'est trouvée.
     */
    public Action findAction(List<String> pParsedSentence){
        
        RouteNode route;            // Racine du modèle des routes
        ActionNode action = null;   // Noeud action correspondant à la phrase
        Action implAction = null;   // Action à retourner correspondante à la phrase 
        
        /* 
        Initialise la liste des détails avec la phrase parsée.
        Lorsqu'une action est trouvée sur un chemin, tous les mots de la phrase
        ayant permit d'accéder à l'action sont supprimés.
        Ainsi, une fois l'action trouvée il ne reste dans les détails plus que
        les mots correspondant réélement aux détails de la phrase.
        */
        details = pParsedSentence;
        
        /* Récupère le modèle des routes */
        route = routesProvider.getModel();
        
        /* Récupère les mots à la racine */
        List<WordNode> rootWords = route.getWords();
        
        /*
        On parcour les noeuds du premier niveau de l'arbre avant de faire appel 
        à la fonction récurcive (searchPath()).
        On doit faire ça parceque la racine de l'arbre (RouteNode) n'est pas 
        du même type que le paramètre attendu par
        la fonction récurcive, qui est un mot (WordNode).
        */
        
        /* Pour chaque mots */
        for(WordNode word : rootWords)
        {
            /* Récupère le premier mot de la phrase */
            final String currentSentenceWord = details.get(0);
            
            /* Si le noeud n'est pas vide OU qu'il correspond au mot courrant */
            if(word.getValue() == null || word.getValue().isEmpty() || word.getValue().equals(currentSentenceWord))
            {
                /* Recherche une action correspondant à la suite de la phrase */
                action = searchPath(word, 1);

                /* Si une action a été trouvée */
                if(action != null)
                {
                    /* Si le noeud n'est pas vide */
                    if(word.getValue() != null || !word.getValue().isEmpty())
                    {
                        /* On supprime le mot des détails puisqu'il a été "consommé" par le fondamental analyser */
                        details.remove(0);
                    }
                    break;
                }
            }
        }

        /* Si une action a été trouvée */
        if(action != null){
            /* Récupère le nom de provider correspondant au noeud action */
            providerName = action.getProviderName();
            
            /* Récupère les précisions correspondants au noeud action */
            askFor = action.getAskFor();
            
            /* Créé une action avec le contenu du noeud action trouvé */
            implAction = new Action(providerName, askFor, details);
        
        /* Sinon, Reset de toutes les valeurs */
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
        Action findedAction = null,     // Première action trouvée
               currentAction = null;    // Action correspondant à la phrase courrante
        int iSentence = 0,              // Indice de la phrase courrante
            iFindedSentence = -1;       // Indice de la phrase correspondant à l'action trouvée
        
        /* Pour chaque phrase, on en recherche une correspondant à une action */
        for(List<String> parsedSentence : pParsedSentences)
        {
            currentAction = findAction(parsedSentence);
            
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
        routesProvider.persist();
    }
    
     /**
     * Enregistre l'état des routes dans le fichier XML
     */
    public void saveRoutes(){
        routesProvider.persist();
    }
    
    /**
     * Parcour l'arbre des routes en fonction des mots de la phrase.
     * Si une action est trouvée lors du parcour des routes, elle est retournée.
     * Lorsqu'il n'y a plus de branche à parcourir, on remonte l'arbre jusqu'à tomber sur une action.
     * @param wordNode : La noeud de l'arbre à parcourir
     * @param pParsedSentence : La phrase ayant préalablement été parsée
     * @param iWord : L'indice du mot courrant de la phrase
     * @return Un noeud de l'arbre correspondant à une action, ou NULL si aucune action n'est trouvée.
     */
    private ActionNode searchPath(WordNode wordNode, int iWord){
        
        List<WordNode> wordNodeChildren;    // Les noeud mots enfant du noeud mot courrant
        ActionNode action = null;           // Noeud action correspondant à la phrase

        wordNodeChildren = wordNode.getWords();
        
        /* Si il reste des mots à analyser dans la phrase */
        if(iWord < details.size()){
            final String currentSentenceWord = details.get(iWord);
            for (WordNode currentWordNode : wordNodeChildren) {
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
        
        /* Si aucune action n'a été trouvée, on recherche une action sur le noeud courrant */
        if(action == null){
            final List<ActionNode> actions = wordNode.getActions();
            
            if(actions.size() > 0)
            {
                action = actions.get(0);
            }
        }
        
        return action;
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

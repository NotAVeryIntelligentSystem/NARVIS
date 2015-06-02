/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.engine.fondamentalAnalyser;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import narvis.engine.parser.Parser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Yoann LE MOUËL & Alban BONNET & Charles COQUE & Raphaël BLIN
 */
public class FondamentalAnalyser {
    private final static Logger LOGGER = Logger.getLogger(Parser.class.getName());
    
    private final static String ROUTESPATH = "src\\main\\java\\narvis\\engine\\fondamentalAnalyser\\routes.xml"; // Le chemin d'accès au fichier XML contenant les routes
    private Document document = null; // Le document XML contenant les routes
    
    private String providerName = "";
    private List<String> askFor = null;
    private List<String> details = null; // La phrase parsée à partir de laquelle l'arbre est parcouru. Elle devient par la suite la liste des détails de la phrase.
    
    public FondamentalAnalyser() throws ParserConfigurationException, SAXException, IOException{
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        final DocumentBuilder builder = factory.newDocumentBuilder();
        document= builder.parse(new File(ROUTESPATH));
    }
    
    /**
     * Recherche le provider et les informations attendues en fonction de la phrase en entrée.
     * @param pParsedSentence : La phrase ayant préalablement été parsée
     * @return Retourne l'action correspondant à la phrase, ou NULL si aucune n'est trouvée.
     */
    public Action findAction(List<String> pParsedSentence){
        Action action = null;
        details = pParsedSentence;
        Element actionElement = searchPath(document.getDocumentElement(), 0);
        
        if(actionElement != null && actionElement.getNodeName().equals("action")){
            providerName = actionElement.getAttribute("provider");
            
            askFor=new LinkedList();
            askFor.addAll(Arrays.asList(actionElement.getAttribute("askfor").split("[+]")));
            
            action = new Action(providerName, askFor, details);
        }else{
            providerName = "";
            askFor = null;
            details = null;
        }
        
        return action;
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
                LOGGER.warning("Plusieurs phrases correspondent déjà à une action...");
                return;
            }
            i++;
        }
        
        if(findedAction == null){
            LOGGER.warning("Aucune phrase n'est déjà connue...");
            return;
        }
        
        /* On retire de la liste la phrase déjà connnue */
        pParsedSentences.remove(indexOfKnownSentence);

        /* Pour chaque phrase, on créé une route avec comme finalitée l'action connue */
        for(List<String> parsedSentence : pParsedSentences){
            createPath(document.getDocumentElement(), parsedSentence, findedAction);
        }
    }
    
        /**
     * Enregistre l'état des routes dans le fichier XML
     * @throws java.io.FileNotFoundException
     * @throws javax.xml.transform.TransformerConfigurationException
     */
    public void saveRoutes() throws FileNotFoundException, TransformerConfigurationException, TransformerException{
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
	final Transformer transformer = transformerFactory.newTransformer();
	final DOMSource source = new DOMSource(document);
	final StreamResult sortie = new StreamResult(new File(ROUTESPATH));
			
	//prologue
	transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");			
	    		
	//formatage
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			
	//sortie
	transformer.transform(source, sortie);	
    }
    
    /**
     * Parcour l'arbre des routes en fonction des mots de la phrase.
     * Lorsqu'il n'y a plus de branche à parcourir, on remonte l'arbre jusqu'à tomber sur une action.
     * @param pBranche : La branche de l'arbre à parcourir
     * @param pParsedSentence : La phrase ayant préalablement été parsée
     * @param iWord : L'indice du mot courrant
     * @return Un noeud de l'arbre correspondant à une action, ou NULL si aucune action n'est trouvée.
     */
    private Element searchPath(Element pBranche, int iWord){
        final NodeList routesWords = pBranche.getChildNodes();
        
        Element action = null;
        
        if(iWord < details.size()){
            final String currentSentenceWord = details.get(iWord);
            for(int i=0; i<routesWords.getLength(); i++){
                if(routesWords.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    final Element currentRouteBranche = (Element) routesWords.item(i);
                    
                    if(currentRouteBranche.getNodeName().equals("word")){
                        if(currentRouteBranche.getAttribute("value").isEmpty()){
                            action = searchPath(currentRouteBranche, iWord+1);
                            break;
                        }else if(currentRouteBranche.getAttribute("value").equals(currentSentenceWord)){
                            details.remove(iWord);
                            action = searchPath(currentRouteBranche, iWord);
                            break;
                        }
                    }
                }
            }
        }
        
        if(action == null){
            for(int i=0; i<routesWords.getLength(); i++){
                if(routesWords.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    final Element currentRouteBranche = (Element) routesWords.item(i);
                    
                    if(currentRouteBranche.getNodeName().equals("action")){
                        action = currentRouteBranche;
                        break;
                    }
                }
            }
        }
        
        return action;
    }
    
    /**
     * Ajoute un nouveau chemin.
     * @param pBranche : La branche de l'arbre à parcourir
     * @param pParsedSentence : La phrase préalablement parsée
     * @param pAction : L'action correspondante
     */
    private void createPath(Element pBranche, List<String> pParsedSentence, Action pAction){
        final NodeList routesWords = pBranche.getChildNodes();
        boolean isFound = false;
        
        if(pParsedSentence.size() > 0){
            final String currentSentenceWord = pParsedSentence.get(0);
            pParsedSentence.remove(0);
            
            for(int i=0; i<routesWords.getLength(); i++){
                if(routesWords.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    final Element currentRouteBranche = (Element) routesWords.item(i);
                    
                    if(currentRouteBranche.getNodeName().equals("word")){
                        if(currentRouteBranche.getAttribute("value").isEmpty() || currentRouteBranche.getAttribute("value").equals(currentSentenceWord)){
                            createPath(currentRouteBranche, pParsedSentence, pAction);
                            isFound = true;
                            break;
                        }
                    }
                }
            }
            
            /* Si aucun noeud enfant ne correspond au mot, on créé un nouveau noeud */
            if(!isFound){
                Element newElement = (Element) document.createElement("word");
             
                if(!currentSentenceWord.equals("something") && !currentSentenceWord.equals("someone")){
                    newElement.setAttribute("value", currentSentenceWord);
                }

                pBranche.appendChild(newElement);
                
                createPath(newElement, pParsedSentence, pAction);
            }
        }else{
        
            /* On fois qu'on a finit de générer le chemin, on ajoute l'action à la fin */
            Element newAction = (Element) document.createElement("action");

            newAction.setAttribute("provider", pAction.getProviderName());
            
            String askFor = "";
            for(String precision : pAction.getPrecisions()){
                if(!askFor.isEmpty()){
                    askFor += "+";
                }
                askFor += precision;
            }
            if(!askFor.isEmpty()){
                newAction.setAttribute("askfor", askFor);
            }

            pBranche.appendChild(newAction);
        }
    }
    
}

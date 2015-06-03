/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.engine.fondamentalAnalyser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Zack
 */
public class RoutesProvider implements IRoutesProvider {
    private final static String ROUTESPATH = "src\\main\\java\\narvis\\engine\\fondamentalAnalyser\\routes.xml"; // Le chemin d'accès au fichier XML contenant les routes
    private Document document = null; // Le document XML contenant les routes
    
    private List<IWordNode> words;
    
    public RoutesProvider() throws ParserConfigurationException, SAXException, IOException{
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        final DocumentBuilder builder = factory.newDocumentBuilder();
        document= builder.parse(new File(ROUTESPATH));
        
        words = new LinkedList<>();
    }
    
    @Override
    public List<IWordNode> getWords() {
        Element root = document.getDocumentElement();
        
        final NodeList routesWords = root.getChildNodes();
        
        words.clear();
        
        for(int i=0; i<routesWords.getLength(); i++){
            if(routesWords.item(i).getNodeType() == Node.ELEMENT_NODE) {
                final Element currentRouteBranche = (Element) routesWords.item(i);
                
                switch (currentRouteBranche.getNodeName()) {
                    case "word":
                        IWordNode newWordNode = new WordNode(currentRouteBranche.getAttribute("value"));
                        words.add(newWordNode);
                        fillTree(currentRouteBranche, newWordNode);
                        break;
                    case "action":
                        ActionNode newActionNode = new ActionNode(currentRouteBranche.getAttribute("provider"));
                        newActionNode.setAskFor(Arrays.asList(currentRouteBranche.getAttribute("askfor").split("[+]")));
                        break;
                }
            }
        }
        
        return words;
    }
    
    @Override
    public void setWords(List<IWordNode> words)
    {
        this.words = words;
    }

    @Override
    public Object getModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist() {
        try {
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

        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(RoutesProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(RoutesProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillTree(Element currentNode, IWordNode currentWordNode)
    {
        final NodeList routesWords = currentNode.getChildNodes();
        
        for(int i=0; i<routesWords.getLength(); i++){
            if(routesWords.item(i).getNodeType() == Node.ELEMENT_NODE) {
                final Element currentRouteBranche = (Element) routesWords.item(i);
                
                switch (currentRouteBranche.getNodeName()) {
                    case "word":
                        IWordNode newWordNode = new WordNode(currentRouteBranche.getAttribute("value"));
                        currentWordNode.addWord(newWordNode);
                        fillTree(currentRouteBranche, newWordNode);
                        break;
                    case "action":
                        ActionNode newActionNode = new ActionNode(currentRouteBranche.getAttribute("provider"));
                        newActionNode.setAskFor(Arrays.asList(currentRouteBranche.getAttribute("askfor").split("[+]")));
                        currentWordNode.addAction(newActionNode);
                        break;
                }
            }
        }
    }

}

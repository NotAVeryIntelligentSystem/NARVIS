/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.engine.fondamentalAnalyser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Yoann
 */
public class NARVIS_router_test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        List<List<String>> allSentences = new LinkedList();
        
        List<String> parsedSentence = new LinkedList();
        parsedSentence.add("give");
        parsedSentence.add("me");
        parsedSentence.add("weather");
        parsedSentence.add("London");
        
        List<String> parsedSentence2 = new LinkedList();
        parsedSentence2.add("bring");
        parsedSentence2.add("someone");
        parsedSentence2.add("weather");
        allSentences.add(parsedSentence2);
        
        List<String> parsedSentence3 = new LinkedList();
        parsedSentence3.add("give");
        parsedSentence3.add("me");
        parsedSentence3.add("weather");
        allSentences.add(parsedSentence3);
        
        try {
            final FondamentalAnalyser fondamentalAnalyser = new FondamentalAnalyser();
            Action action = fondamentalAnalyser.findAction(parsedSentence);
            if(action != null){
                String providerName = action.getProviderName();
                System.out.println("Provider name : "+providerName);
                List<String> askFor = action.getPrecisions();
                if(askFor != null){
                    System.out.println("Ask for : ");
                    for(String askFor1 : askFor){
                        System.out.println(askFor1);
                    }
                }
                List<String> details = action.getDetails();
                if(details != null){
                    System.out.println("Details : ");

                    for (String detail : details) {
                        System.out.println(detail);
                    }
                }
                
            }else{
                System.out.println("Aucune action n'a été trouvée qui puisse répondre à la demande...");
            }
            
            fondamentalAnalyser.createSimilarityBetween(allSentences);
            fondamentalAnalyser.saveRoutes();
        } catch (final ParserConfigurationException e) {
        } catch (SAXException ex) {
            Logger.getLogger(NARVIS_router_test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NARVIS_router_test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

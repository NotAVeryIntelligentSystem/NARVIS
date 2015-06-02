/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.engine.parser;

import narvis.engine.parser.Parser;
import java.util.List;

/**
 *
 * @author Zack
 */
public class TestParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String myMessage = "What is the weather in London ?";
        
        List<String> myParsedMessage = Parser.Parse(myMessage);
        
        return;
    }
    
}

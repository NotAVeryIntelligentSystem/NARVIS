package narvis.engine.parser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
/**
 * Singleton permettant de parser une chaine de caractère en une liste de mots.
 * @author Zack
 */
public class Parser {
    private final static Logger LOGGER = Logger.getLogger(Parser.class.getName());
    
    private final static DataLangProvider langProvider = new DataLangProvider();
    
    /**
     * Constructeur par défaut du Parser
     */
    public Parser()
    {
    }
    
    /**
     * Transforme une chaine de caractères en une liste de mots en retirant
     * les mots inutiles
     * @param message : Chaine de caractères contenant le message à parser
     * @return Le message parsé en une liste de mots
     */
    public static List<String> Parse(String message)
    {
        ArrayList<String> parsedMessage = new ArrayList<>();
        parsedMessage.addAll(Arrays.asList(message.split(" ")));

        ArrayList<String> ignoredWords = langProvider.getIgnoredWords();
                
        parsedMessage.removeAll(ignoredWords);
        
        return parsedMessage;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.modules.lexicalAnalyser;

import java.util.Iterator;
import standup.lexicon.CustomLexicon;
import standup.lexicon.Dictionary;
import standup.lexicon.Lexeme;
import standup.lexicon.LexemeSet;
import standup.lexicon.LexicalComponents;
import standup.lexicon.LexiconException;
import standup.lexicon.WordSequence;
import standup.profiling.ProfileException;
import standup.profiling.ProfileManager;

/**
 *
 * @author Puma
 */
public class LexicalAnalyserPortal implements ILexicalAnalyser{

    
    public LexicalAnalyserPortal() 
    {

    }
    
    @Override
    public String getWordDefinition(String word){
        
        // Get all senses of the word 'bank'
        LexemeSet bankLexemes = Dictionary.getSpelledLexemes(new WordSequence(word));

        // Print the different meanings of the various senses
        for (Iterator<Lexeme> iter = bankLexemes.getLexemesIterator(); iter.hasNext();)
        {
          Lexeme lexeme = iter.next();
          
          System.out.println("The meaning of "+lexeme+" is "+lexeme.getConcept().getBriefGloss());
        }
        
        return "";
    }
    
    @Override
    public void parseSentence(String sentence) {
   
        String flushedString = flushSentence(sentence.toLowerCase());
        
        String[] words = flushedString.split(" ");
        for( String word : words )
        {
            getWordDefinition(word);
        }
    }
    
    /***
     * Flush out the sentence of all : "The"  
     * @param sentences
     * @return 
     */
    private String flushSentence(String sentences)
    {
        String flushOutThe = sentences.replace("the", " ");
        
        return flushOutThe.replace("  ", "");
        
    }
    
    
    public static void main(String[] args) 
    {
        LexicalAnalyserPortal lp = new LexicalAnalyserPortal();
        
        lp.parseSentence("I love the zoo");
        
    }
    
}

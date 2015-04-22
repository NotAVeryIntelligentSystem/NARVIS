/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.modules.lexicalAnalyser;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.VerbSynset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 *
 * @author Puma
 */
public class WordNetLexicalAnalyser implements ILexicalAnalyser {

        
    
    @Override
    public String getWordDefinition(String word) {
        
        VerbSynset verbSynset;
        VerbSynset[] hyponyms;

        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(word, SynsetType.VERB);
        for (int i = 0; i < synsets.length; i++) {
            verbSynset = (VerbSynset)(synsets[i]);
            hyponyms = verbSynset.getHypernyms();
            System.out.println(verbSynset.getWordForms()[0] +
                    ": " + verbSynset.getDefinition() + ") has " + hyponyms.length + " hyponyms");
        } 
        
        return "";
    }
    
    
    /**
     * Return true if the word can be a noon
     * @param word
     * @return 
     */
    public boolean isNoun(String word)
    {
        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(word, SynsetType.NOUN);
        
        return synsets.length > 0;
    }
    
    public boolean isVerb(String word)
    {
        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(word, SynsetType.VERB);
        
        return synsets.length > 0;
    }
    
    public static void main(String[] args)
    {
        System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");
        
        WordNetLexicalAnalyser analyser = new WordNetLexicalAnalyser();
        analyser.parseSentence("tell me the weather in nimes");
    }
    
    

    @Override
    public void parseSentence(String sentence) {
   
        String flushedString = flushSentence(sentence.toLowerCase());
        
        String[] words = flushedString.split(" ");
        if( isNoun(words[0]) && isVerb(words[1] ) )
        {
            System.out.println("noun : " + words[0] + " verbs " + words[1]);
        }
        else if( isNoun(words[1]) && isVerb(words[0] ) )
        {
            System.out.println("noun : " + words[1] + " verbs : " + words[0]);
        }
            
            
    }
    
    /***
     * Flush out the sentence of all : "The"  
     * @param sentences
     * @return 
     */
    private String flushSentence(String sentences)
    {
        String flushOutThe = sentences.replace(" the ", " ");
        
        String[] subjects = {"i ","you ","we ","they ","he ","she ", " me "," your "," yours ", " in "};
        
        
        for( String subject : subjects)
        {
            flushOutThe = flushOutThe.replace(subject, " ");
        }
        
        return flushOutThe.replace("  ", "");
        
    }
}

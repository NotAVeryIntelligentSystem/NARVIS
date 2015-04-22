/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package narvis.modules.lexicalAnalyser;

/**
 *
 * @author Puma
 */
public interface ILexicalAnalyser {
 
    String getWordDefinition(String word);
    void parseSentence(String sentence);
    
}

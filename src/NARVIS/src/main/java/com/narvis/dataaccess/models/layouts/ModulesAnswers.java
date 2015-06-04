/*
 * The MIT License
 *
 * Copyright 2015 puma.
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
package com.narvis.dataaccess.models.layouts;

import com.narvis.dataaccess.interfaces.IDataProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;

/**
 * Represent the XML file wich contains the answers for each command
 * @author puma
 */
@Root(name = "Answers")
public class ModulesAnswers implements IDataProvider{
    
     
     private Map<String, String> CommandToAnswer = new HashMap<>();
    
    @ElementList
    private List<AnswserSentence> Answer;

    public Map<String, String> getMap() {
        return CommandToAnswer;
    }

    public void setMap(Map<String, String> map) {
        this.CommandToAnswer = map;
    }
    
    public List<AnswserSentence> getSentences() {
        return Answer;
    }

    public void setSentences(List<AnswserSentence> Sentences) {
        this.Answer = Sentences;
    }
 
    @Commit
    public void build()
    {
        for( AnswserSentence sentence : Answer )
        {
            if( !CommandToAnswer.containsKey(sentence.getCommand()) )
                CommandToAnswer.put(sentence.getCommand(), sentence.getSentence());
        }
    }
    
    
    public String getSentence(String command) {
        
        return CommandToAnswer.get(command);
        
    }

    
    /**
     * Use it to get the answer attached to the command, Only one command at a time is supported
     * @param keywords : This command represent the command Attribute in the XML file
     * @return The answer or null if the given command has no attached answer
     */
    @Override
    public String getData(String... keywords) {
        
        if( this.CommandToAnswer.containsKey(keywords[0]) )
        {
            return this.CommandToAnswer.get(keywords[0]);
        }
        
        return null;
        
    }

}

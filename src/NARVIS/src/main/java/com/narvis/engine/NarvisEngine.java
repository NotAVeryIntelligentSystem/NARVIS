/*
 * The MIT License
 *
 * Copyright 2015 Nakou.
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
package com.narvis.engine;

import com.narvis.dataaccess.interfaces.dataproviders.IDataProviderDetails;
import com.narvis.dataaccess.interfaces.dataproviders.IDataProvider;
import com.narvis.common.debug.NarvisLogger;
import com.narvis.common.tools.executer.Executer;
import com.narvis.common.tools.executer.ExecuterException;
import com.narvis.dataaccess.*;
import com.narvis.dataaccess.exception.IllegalKeywordException;
import com.narvis.dataaccess.exception.NoDataException;
import com.narvis.dataaccess.exception.PersistException;
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.interfaces.*;
import com.narvis.engine.exception.EngineException;
import com.narvis.engine.exception.NoActionException;
import com.narvis.engine.exception.NoSentenceException;
import com.narvis.frontend.MessageInOut;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Nakou
 */
public class NarvisEngine {
    private static NarvisEngine narvis;

    private final Parser parser;
    private final FondamentalAnalyser fondamentalAnalyser;
    private final DetailsAnalyser detailAnalyser;
    private final IMetaDataProvider metaDataProvider;
    private final Executer executer;

    private NarvisEngine() throws Exception {
        this.executer = new Executer("Narvis engine");
        parser = new Parser();
        fondamentalAnalyser = new FondamentalAnalyser();
        detailAnalyser = new DetailsAnalyser();
        metaDataProvider = DataAccessFactory.getMetaDataProvider();
    }

    public static NarvisEngine getInstance() throws Exception {
        if (narvis != null) {
            return narvis;
        } else {
            narvis = new NarvisEngine();
            return narvis;
        }
    }

    public void start() {
        this.executer.start();
        // We starts all front ends
        for(String frontEnd : this.metaDataProvider.getAvailableFrontEnds()) {
            this.metaDataProvider.getFrontEnd(frontEnd).start();
        }
    }

    public void close() throws Exception {
        // We stop all fronts ends
        for(String frontEnd : this.metaDataProvider.getAvailableFrontEnds()) {
            this.metaDataProvider.getFrontEnd(frontEnd).close();
        }
        this.executer.close();
    }

    public void getMessage(final MessageInOut lastMessage) throws ExecuterException {
        this.executer.addToExecute(new Runnable() {

            @Override
            public void run() {
                try {
                    brainProcess(lastMessage);
                } catch (IllegalKeywordException ex) {
                    NarvisLogger.logException(ex);
                    onError(lastMessage, ex.getNarvisErrorMessage());
                } catch (NoDataException ex) {
                    NarvisLogger.logException(ex);
                    onError(lastMessage, ex.getNarvisErrorMessage());
                } catch (ProviderException ex) {
                    NarvisLogger.logException(ex);
                    onError(lastMessage, ex.getNarvisErrorMessage());
                } catch (NoActionException | NoSentenceException ex) {
                    NarvisLogger.logException(ex);
                    onError(lastMessage, ex.getNarvisErrorMessage());
                } catch (EngineException ex) {
                    NarvisLogger.logException(ex);
                    onError(lastMessage, ex.getNarvisErrorMessage());
                } 
            }
        });
    }
    
    private void onError(MessageInOut originalMessage, String message) {
        this.metaDataProvider.getFrontEnd(originalMessage.getInputAPI()).getOutput().setOuput(new MessageInOut(originalMessage.getInputAPI(), message, originalMessage.getAnswerTo()));
    }

    private void brainProcess(MessageInOut message) throws ProviderException, EngineException{
        List<String> parsedSentence;
        Map<String, String> detailsTypes;
        
        parsedSentence = parser.parse(message.getContent());
        Action action = fondamentalAnalyser.findAction(parsedSentence);
        
        detailsTypes = detailAnalyser.getDetailsTypes(action.getDetails());
        IDataProvider provider = this.metaDataProvider.getDataProvider(action.getProviderName());
        String protoAnswer = "";
        
        /* If the provider is NARVIS, we execute an internal action */
        if(action.getProviderName().equals("narvis")){
            protoAnswer = doInternalAction(action, detailsTypes);
            
        /* else, we did the classic way and get the provider that correspond to the action */
        }else{
            String[] askForArray = (String[]) action.getPrecisions().toArray(new String[action.getPrecisions().size()]);
            if (provider instanceof IDataProviderDetails) {
                protoAnswer = ((IDataProviderDetails) provider).getDataDetails(detailsTypes, askForArray);
            } else {
                protoAnswer = ((IDataProvider) provider).getData(askForArray);
            }
        }

        /* We finaly put the proto answer returne by the provider at the end of the details map */
        detailsTypes.put("sentence", protoAnswer);
        
        IDataProviderDetails answerBuilder = (IDataProviderDetails) this.metaDataProvider.getDataProvider("Answers");
        String finalAnswer = answerBuilder.getDataDetails(detailsTypes);
        this.metaDataProvider.getFrontEnd(message.getInputAPI()).getOutput().setOuput(new MessageInOut(message.getInputAPI(),finalAnswer,message.getAnswerTo()));
    }

    
    /**
     * Guide NARVIS to choose the right internal action.
     * @param action : The action to execute
     * @return The answer
     */ 
    private String doInternalAction(Action action, Map<String, String> detailsTypes) throws EngineException, ProviderException
    {
        String answer = "";
        if(action.getPrecisions().isEmpty())
            throw new NoActionException("No internal action found", "I don't understand what you're asking for");
        
        switch(action.getPrecisions().get(0))
        {
            case "learnsimilaritybetweenroutes":
                answer = learnSimilarityBetweenRoutes(detailsTypes);
                break;
            default:
                throw new NoActionException("No internal action found", "I don't understand what you're asking for"); 
        }
        
        return answer;
    }
    
    /**
     * Learn the similarity between sentences that are passed in details.
     * @param details
     * @return The success answer if the action has succesfuly
     * @throws ParseException
     * @throws EngineException
     * @throws NoDataException
     * @throws PersistException 
     */
    private String learnSimilarityBetweenRoutes(Map<String, String> details) throws EngineException, ProviderException
    {
        String successAnswer = "I've learned this similarity.";
        
        fondamentalAnalyser.createSimilarityBetween(parser.getParsedSentencesFromDetails(details));
        
        return successAnswer;
    }
}

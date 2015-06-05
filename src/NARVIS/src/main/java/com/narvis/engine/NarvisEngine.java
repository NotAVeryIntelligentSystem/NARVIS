/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.narvis.engine;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nakou
 */
public class NarvisEngine {

    private static String IOname = "Twitter"; // TODO : Change by load from conf files
    private static NarvisEngine narvis;

    private Parser parser;
    private FondamentalAnalyser fondamentalAnalyser;
    private DetailsAnalyser detailAnalyser;
    private IMetaDataProvider metaDataProviderAction;
    private IMetaDataProvider metaDataProviderAnswer;
    private final Executer executer;
    private MessageInOut lastMessage;

    private NarvisEngine() throws Exception {
        this.executer = new Executer("Narvis engine");
        parser = new Parser();
        fondamentalAnalyser = new FondamentalAnalyser();
        detailAnalyser = new DetailsAnalyser();
        metaDataProviderAction = DataAccessFactory.getMetaDataProvider();
        metaDataProviderAnswer = DataAccessFactory.getMetaDataProvider();
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
        this.metaDataProviderAction.getFrontEnd(IOname).start();
    }

    public void close() throws Exception {
        this.metaDataProviderAction.getFrontEnd(IOname).close();
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
                    onError(ex.getNarvisErrorMessage());
                } catch (NoDataException ex) {
                    NarvisLogger.logException(ex);
                    onError(ex.getNarvisErrorMessage());
                } catch (ProviderException ex) {
                    NarvisLogger.logException(ex);
                    onError(ex.getNarvisErrorMessage());
                } catch (NoActionException | NoSentenceException ex) {
                    NarvisLogger.logException(ex);
                    onError(ex.getNarvisErrorMessage());
                } 
            }
        });
    }
    
    private void onError(String message) {
        // todo
        this.metaDataProviderAction.getFrontEnd(IOname).getOutput().setOuput(new MessageInOut(this.lastMessage.getInputAPI(),message,this.lastMessage.getAnswerTo()));
    }

    private void brainProcess(MessageInOut message) throws NoDataException, ProviderException, NoActionException, NoSentenceException{
        this.lastMessage = message;
        List<String> parsedSentence = parser.parse(message.getContent());
        Action action = fondamentalAnalyser.findAction(parsedSentence);
        
        Map<String, String> detailsTypes = detailAnalyser.getDetailsTypes(action.getDetails());
        IDataProvider provider = this.metaDataProviderAction.getDataProvider(action.getProviderName());
        String protoAnswer = "";
        
        String[] askForArray = (String[]) action.getPrecisions().toArray(new String[action.getPrecisions().size()]);
        if (provider instanceof IDataProviderDetails) {
            protoAnswer = ((IDataProviderDetails) provider).getDataDetails(detailsTypes, askForArray);
        } else {
            protoAnswer = ((IDataProvider) provider).getData(askForArray);
        }
        Map<String,String> answerParams = new HashMap<>();
        answerParams.put("sentence", protoAnswer);
        IDataProvider answerBuilder = this.metaDataProviderAnswer.getDataProvider("Answers");
        String[] bullshit = new String[1];
        bullshit[0] = "polite3";
        String finalAnswer = ((IDataProviderDetails) answerBuilder).getDataDetails(answerParams, bullshit);
        this.metaDataProviderAction.getFrontEnd(IOname).getOutput().setOuput(new MessageInOut(message.getInputAPI(),finalAnswer,message.getAnswerTo()));
    }

    
    /**
     * Guide NARVIS to choose the right internal action.
     * @param action : The action to execute
     * @return The answer
     */ 
    private String doInternalAction(Action action, Map<String, String> detailsTypes) throws ParseException, EngineException, NoDataException, PersistException
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
    private String learnSimilarityBetweenRoutes(Map<String, String> details) throws ParseException, EngineException, NoDataException, PersistException
    {
        String successAnswer = "I've learned this similarity.";
        
        fondamentalAnalyser.createSimilarityBetween(parser.getParsedSentencesFromDetails(details));
        
        return successAnswer;
    }
}

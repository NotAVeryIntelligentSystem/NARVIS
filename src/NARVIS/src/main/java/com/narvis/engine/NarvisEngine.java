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
import com.narvis.dataaccess.exception.ProviderException;
import com.narvis.dataaccess.interfaces.*;
import com.narvis.dataaccess.models.answers.AnswersProvider;
import com.narvis.frontend.MessageInOut;
import com.narvis.frontend.interfaces.IOutput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nakou
 */
public class NarvisEngine {

    private static String IOname = "Console"; // TODO : Change by load from conf files
    private static NarvisEngine narvis;

    private Parser parser;
    private FondamentalAnalyser fondamental;
    private DetailsAnalyser detailAnalyser;
    private IMetaDataProvider metaDataProviderAction;
    private IMetaDataProvider metaDataProviderAnswer;
    private final Executer executer;

    private NarvisEngine() throws Exception {
        this.executer = new Executer("Narvis engine");
        parser = new Parser();
        fondamental = new FondamentalAnalyser();
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
                    onError();
                } catch (NoDataException ex) {
                    NarvisLogger.logException(ex);
                    onError();
                } catch (ProviderException ex) {
                    NarvisLogger.logException(ex);
                    onError();
                } 
            }
        });
    }
    
    private void onError() {
        // todo
    }

    private void brainProcess(MessageInOut message) throws NoDataException, ProviderException{
        List<String> parsedSentence = parser.parse(message.getContent());
        Action action = fondamental.findAction(parsedSentence);
        
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

}

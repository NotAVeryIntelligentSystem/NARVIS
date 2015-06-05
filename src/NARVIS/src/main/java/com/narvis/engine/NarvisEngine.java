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
import com.narvis.frontend.MessageInOut;
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

    private static String IOname = "console"; // TODO : Change by load from conf files
    private static NarvisEngine narvis;

    private Parser parser;
    private FondamentalAnalyser fondamental;
    private DetailsAnalyser detailAnalyser;
    private IMetaDataProvider metaDataProvider;
    private final Executer executer;

    private NarvisEngine() throws Exception {
        this.executer = new Executer("Narvis engine");
        parser = new Parser();
        fondamental = new FondamentalAnalyser();
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
        this.metaDataProvider.getFrontEnd("Console").start();
    }

    public void close() throws Exception {
        this.metaDataProvider.getFrontEnd("Console").close();
        this.executer.close();
    }

    public void getMessage(final MessageInOut lastMessage) throws ExecuterException {
        this.executer.addToExecute(new Runnable() {

            @Override
            public void run() {
                try {
                    brainProcess(lastMessage.getContent());
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

    private void brainProcess(String message) throws NoDataException, ProviderException{
        List<String> parsedSentence = parser.parse(message);
        Action action = fondamental.findAction(parsedSentence);
        Map<String, String> detailsTypes = detailAnalyser.getDetailsTypes(action.getDetails());
        IDataProvider provider = this.metaDataProvider.getDataProvider(action.getProviderName());
        if (provider instanceof IDataProviderDetails) {
            String protoAnswer = ((IDataProviderDetails) provider).getDataDetails(detailsTypes, (String[]) action.getPrecisions().toArray());
        } else {
            String protoAnswer = ((IDataProvider) provider).getData((String[]) action.getPrecisions().toArray());
        }
        // AnswerBuilder;

    }

}

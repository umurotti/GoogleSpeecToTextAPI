/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.googleapi;

//import com.google.cloud.speech.v1.RecognitionAudio;
//import com.google.cloud.speech.v1.RecognitionConfig;
//import com.google.cloud.speech.v1.RecognizeResponse;
//import com.google.cloud.speech.v1.SpeechClient;
//import com.google.cloud.speech.v1.SpeechContext;
//import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
//import com.google.cloud.speech.v1.SpeechRecognitionResult;
//import com.google.cloud.speech.v1.WordInfo;
//import com.google.cloud.speech.v1.RecognitionAudio;
//import com.google.cloud.speech.v1.RecognitionConfig;
//import com.google.cloud.speech.v1.RecognizeResponse;
//import com.google.cloud.speech.v1.SpeechClient;
//import com.google.cloud.speech.v1.SpeechContext;
//import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
//import com.google.cloud.speech.v1.SpeechRecognitionResult;
//import com.google.cloud.speech.v1.WordInfo;
import com.google.cloud.speech.v1p1beta1.RecognitionAudio;
import com.google.cloud.speech.v1p1beta1.RecognitionConfig;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.SpeechClient;
import com.google.cloud.speech.v1p1beta1.SpeechContext;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.WordInfo;
import com.google.protobuf.ByteString;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author umur
 */
public class SpeechConverter {

    private SpeechClient speechClient;
    private String inputBase64;
    private List<String> phrases;
    private RecognizeResponse response;

    public SpeechConverter(String inputBase64) throws IOException {
        speechClient = SpeechClient.create();
        phrases = new ArrayList<>();
        this.inputBase64 = inputBase64;
        response = null;
    }

    public SpeechConverter() throws IOException {
        speechClient = SpeechClient.create();

        phrases = new ArrayList<>();
        this.inputBase64 = null;
        response = null;
    }

    public void addPhrase(String phrase) {
        phrases.add(phrase);
    }

    public void build(String model) {
        SpeechContext speechCont = SpeechContext.newBuilder().addAllPhrases(phrases).build();
        RecognitionConfig config = RecognitionConfig.newBuilder()
                //.setEncoding(RecognitionConfig.AudioEncoding.ENCODING_UNSPECIFIED)
                //.setSampleRateHertz(44100)
                .setLanguageCode("en-US")
                .setModel(model)
                .addSpeechContexts(speechCont)
                .setEnableWordTimeOffsets(true)
                .setUseEnhanced(true)
                .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(Base64.decodeBase64(inputBase64)))
                .build();
        response = speechClient.recognize(config, audio);
    }

    public void build(String model, String inputBase64) {
        SpeechContext speechCont = SpeechContext.newBuilder().addAllPhrases(phrases).build();
        RecognitionConfig config = RecognitionConfig.newBuilder()
                //.setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                //.setSampleRateHertz(44100)
                .setLanguageCode("en-US")
                .setModel(model)
                .addSpeechContexts(speechCont)
                //.setEnableWordConfidence(true)
                .setEnableWordTimeOffsets(true)
                //.setEnableSpeakerDiarization(true)
                .setUseEnhanced(true)
                .setMaxAlternatives(5)
                .build();
        RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(Base64.decodeBase64(inputBase64)))
                .build();
        response = speechClient.recognize(config, audio);
    }

    public RecognizeResponse getResponse() {
        return response;
    }

    public List<SpeechRecognitionResult> getResults() {
        return response.getResultsList();
    }

    public void print() {
//        for (SpeechRecognitionResult result : this.getResults()) {
////             There can be several alternative transcripts for a given chunk of speech. Just use the
////             first (most likely) one here.
//            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//            System.out.printf("Transcription: %s%n", alternative.getTranscript());
//            for (WordInfo wordInfo : alternative.getWordsList()) {
//                System.out.println(wordInfo.getWord());
//                System.out.printf(
//                        "\t%s.%s sec - %s.%s sec\n",
//                        wordInfo.getStartTime().getSeconds(),
//                        wordInfo.getStartTime().getNanos() / 100000000,
//                        wordInfo.getEndTime().getSeconds(),
//                        wordInfo.getEndTime().getNanos() / 100000000);
//            }
////            System.out.println(result.getAlternativesL);
//        }
        for (SpeechRecognitionResult result : this.getResults()) {
            // There can be several alternative transcripts for a given chunk of speech. Just use the
            // first (most likely) one here.
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            System.out.printf("Transcription: %s\n", alternative.getTranscript());
            for (WordInfo wordInfo : alternative.getWordsList()) {
                System.out.println(wordInfo.getWord());
                System.out.printf(
                        "\t%s.%s sec - %s.%s sec\n",
                        wordInfo.getStartTime().getSeconds(),
                        wordInfo.getStartTime().getNanos() / 100000000,
                        wordInfo.getEndTime().getSeconds(),
                        wordInfo.getEndTime().getNanos() / 100000000);
            }
        }

    }

    public Map<String, String> parseResult(Map<String, String> commands, Map<String, String> keyTypes) {
        Map<String, String> out = new HashMap<>();
        ArrayList<ArrayList<String>> pool = new ArrayList<ArrayList<String>>();
        
        for (int j = 0; j < 10; j++) {
            pool.add(new ArrayList<>());
        }
        int iteration = 0;
        int size = 0;
        try {

//        System.out.println(this.getResults().get(1).getAlternatives(0).getTranscript());
//        System.out.println(this.getResults().get(1).getAlternatives(1).getTranscript());
//        System.out.println(this.getResults().get(1).getAlternatives(2).getTranscript());
            String item = "";
            boolean flag = false;
//            for (int i = 0; i < this.getResults().size(); i++) {
//                SpeechRecognitionResult result = this.getResults().get(i);
//                // There can be several alternative transcripts for a given chunk of speech. Just use the
//                // first (most likely) one here.
//                List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
//                ListIterator<SpeechRecognitionAlternative> it = alternatives.listIterator();

                ///***///
                
                
                
                
//                while (it.hasNext()) {
//                    String[] splitArr = it.next().getTranscript().trim().split(" ");
//                    if(splitArr.length == 1) {
//                        
//                    }
//                }
//                if (!flag) {
//                    while (it.hasNext()) {
//                        String[] splitArr = it.next().getTranscript().trim().split(" ");
//                        for (int j = 0; j < splitArr.length; j++) {
//                            while (true) {
//                                try {
//                                    pool.get(j + index).add(splitArr[j].trim().toLowerCase());
//                                    break;
//                                } catch (IndexOutOfBoundsException e) {
//                                    pool.add(new ArrayList<>());
//                                }
//                            }
//                        }
//                    }
//                    index = pool.size();
//                    if (index >= 2) {
//                        flag = true;
//                    }
//                } else {
//                    String[] splitArr = it.next().getTranscript().trim().split(" ");
//                    for (int j = 0; j < splitArr.length; j++) {
//                        pool.get(j + index).add(splitArr[j]);
//                    }
//                }
                ///***///

//                while (it.hasNext() && !flag && (i < 2)) {
//                    String tmp = it.next().getTranscript();
//                    if (i == 0) {
//                        if (commands.containsKey(tmp)) {
//                            out.put("command", tmp);
//                            flag = true;
//                        }
//                    } else {
//                        String check = tmp.replaceAll("\\s+", "").toLowerCase();
//                        if (keyTypes.containsKey(check)) {
//                            out.put("keyType", check);
//                            flag = true;
//                        }
//                    }
//                }
//                if (i >= 2) {
//                    item += result.getAlternatives(0).getTranscript();
//                }
//            }
            
            if(this.getResults().size() == 1) {
                SpeechRecognitionResult result = this.getResults().get(0);
                List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
                ListIterator<SpeechRecognitionAlternative> it = alternatives.listIterator();
                while (it.hasNext()) {
                    String[] splitArr = it.next().getTranscript().trim().split(" ");
                    for (int i = 0; i < splitArr.length; i++) {
                        pool.get(i).add(splitArr[i]);
                        if(iteration == 0)
                            size++;
                    }
                    iteration++;
                }
            } else if (this.getResults().size() == 2) {
                SpeechRecognitionResult result = this.getResults().get(0);
                List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
                ListIterator<SpeechRecognitionAlternative> it = alternatives.listIterator();
                while (it.hasNext()) {
                    String[] splitArr = it.next().getTranscript().trim().split(" ");
                    for (int i = 0; i < splitArr.length && i < 2; i++) {
                        pool.get(i).add(splitArr[i]);
                        
                    }
                }
                result = this.getResults().get(1);
                alternatives = result.getAlternativesList();
                it = alternatives.listIterator();
                
                    String[] splitArr = it.next().getTranscript().trim().split(" ");
                    for (int i = 0; i < splitArr.length; i++) {
                        pool.get(i+2).add(splitArr[i]);
                        size++;
                    }
                size = size + 2;
            } else if (this.getResults().size() == 3) {
                SpeechRecognitionResult result = this.getResults().get(0);
                List<SpeechRecognitionAlternative> alternatives = result.getAlternativesList();
                ListIterator<SpeechRecognitionAlternative> it = alternatives.listIterator();
                while (it.hasNext()) {
                    String[] splitArr = it.next().getTranscript().trim().split(" ");
                    for (int i = 0; i < splitArr.length; i++) {
                        pool.get(0).add(splitArr[i]);
                        
                    }
                }
                result = this.getResults().get(1);
                alternatives = result.getAlternativesList();
                it = alternatives.listIterator();
                while (it.hasNext()) {
                    String[] splitArr = it.next().getTranscript().trim().split(" ");
                    for (int i = 0; i < splitArr.length; i++) {
                        pool.get(1).add(splitArr[i]);
                        
                    }
                }
                
                result = this.getResults().get(2);
                alternatives = result.getAlternativesList();
                it = alternatives.listIterator();
                
                    String[] splitArr = it.next().getTranscript().trim().split(" ");
                    for (int i = 0; i < splitArr.length; i++) {
                        pool.get(i+2).add(splitArr[i]);
                        size++;
                    }
                size = size + 2;
            }
            
            boolean found = false;
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    ArrayList<String> tmp = pool.get(i);
                    for (int j = 0; (j < tmp.size()) && !found; j++) {
                        if (commands.containsKey(tmp.get(j))) {
                            out.put("command", tmp.get(j));
                            found = true;
                        }
                    }
                } else if (i == 1) {
                    ArrayList<String> tmp = pool.get(i);
                    for (int j = 0; (j < tmp.size()) && !found; j++) {
                        if (keyTypes.containsKey(tmp.get(j))) {
                            out.put("keyType", tmp.get(j));
                            found = true;
                        }
                    }
                } else {
                    item += pool.get(i).get(0) + " ";
                }

                found = false;
            }
            out.put("item", item);
        } catch (Throwable t) {
            Logger.getLogger(SpeechConverter.class.getName()).log(Level.SEVERE, "ERROR", t);
        }
        speechClient.shutdownNow();
        
        System.out.println("cmd=" + out.get("command"));
        System.out.println("keyType=" + out.get("keyType"));
        System.out.println("item=" + out.get("item"));
        
        return out;
    }
}

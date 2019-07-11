/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.googleapi;

//import com.google.api.gax.core.FixedCredentialsProvider;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.speech.v1.SpeechClient;
//import com.google.cloud.speech.v1.SpeechSettings;
//import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
//import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1p1beta1.RecognizeResponse;
import java.io.FileInputStream;
import java.io.IOException;
// Imports the Google Cloud client library
//import com.google.cloud.speech.v1.RecognitionAudio;
//import com.google.cloud.speech.v1.RecognitionConfig;
//import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
//import com.google.cloud.speech.v1.RecognizeResponse;
//import com.google.cloud.speech.v1.SpeechClient;
//import com.google.cloud.speech.v1.SpeechContext;
//import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
//import com.google.cloud.speech.v1.SpeechRecognitionResult;
//import com.google.cloud.speech.v1.WordInfo;
import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author umur
 */
public class QuickStartSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
//        String fileName = "/home/umur/Downloads/My First Project-badb88d7e4b2.json";
//        FileInputStream credentialsStream = new FileInputStream(fileName);
//        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);
//        FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(credentials);
//
//        SpeechSettings speechSettings
//                = SpeechSettings.newBuilder()
//                        .setCredentialsProvider(credentialsProvider)
//                        .build();

//
//        SpeechClient speechClient = SpeechClient.create();
//        ///////
//        // Instantiates a client
//
//        // The path to the audio file to transcribe
        String fileName2 = "/home/umur/Desktop/S2T_Samples/flac/1.flac";
//
//        // Reads the audio file into memory
        Path path = Paths.get(fileName2);
        byte[] data = Files.readAllBytes(path);
        byte[] encodedAudio = Base64.encodeBase64(data);
//        System.out.println(new String(encodedAudio));
////        byte[] encodedAudio = Base64.encodeBase64(data.);
////        ByteString audioBytes = ByteString.copyFrom(data);
//        ByteString audioBytes = ByteString.copyFrom(data);
//        
//        List<String> phrases = new ArrayList();
//        phrases.add("play");
//        phrases.add("track");
//        phrases.add("final");
//        phrases.add("artist");
//        phrases.add("playlist");
//        // Builds the sync recognize request
//        SpeechContext speechCont = SpeechContext.newBuilder().addAllPhrases(phrases).build();
//        RecognitionConfig config = RecognitionConfig.newBuilder()
//                .setEncoding(AudioEncoding.FLAC)
//                .setSampleRateHertz(44100)
//                .setLanguageCode("en-US")
//                .setModel("command_and_search")
//                .addSpeechContexts(speechCont)
//                .setEnableWordTimeOffsets(true)
//                .setUseEnhanced(true)
//                .build();
//        RecognitionAudio audio = RecognitionAudio.newBuilder()
//                .setContent(audioBytes)
//                .build();
        SpeechConverter converter = new SpeechConverter();

        // Performs speech recognition on the audio file
//        RecognizeResponse response = speechClient.recognize(config, audio);
        converter.addPhrase("play");
        converter.build("default", new String(encodedAudio));
        RecognizeResponse response = converter.getResponse();
        converter.print();
        
        Map<String, String> cmds = new HashMap<>();
        Map<String, String> keytyps = new HashMap<>();
        cmds.put("play", "play");
        keytyps.put("track", "track");
        keytyps.put("artist", "artist");

        keytyps.put("playlist", "playlist");
        
        Map<String, String> alo = converter.parseResult(cmds, keytyps);
//        System.out.println("cmd=" + alo.get("command"));
//        System.out.println("keyType=" + alo.get("keyType"));
//        System.out.println("item=" + alo.get("item"));

//        List<SpeechRecognitionResult> results = response.getResultsList();
//
//        for (SpeechRecognitionResult result : results) {
//            // There can be several alternative transcripts for a given chunk of speech. Just use the
//            // first (most likely) one here.
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
//            System.out.println(result);
//        }
    }
}

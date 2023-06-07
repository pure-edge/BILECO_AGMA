/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package bileco_agma.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
 
/**
 * This is an example program that demonstrates how to play back an audio file
 * using the Clip in Java Sound API.
 * @author www.codejava.net
 *
 */
public class AudioPlayer implements LineListener {
    public static int LOOP_CONTINUOUSLY = Clip.LOOP_CONTINUOUSLY;
    private Clip audioClip;
    private int loopCount;

    public AudioPlayer(String audioFilePath) {
        InputStream resourceAsStream = getClass().getResourceAsStream(audioFilePath);
        
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(resourceAsStream);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
 
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }
    
    public void play(int loopCount) {
        this.loopCount = loopCount;
        audioClip.loop(loopCount);
        audioClip.start();        
    }
     
    public void stop() {
        audioClip.close();
    }
    
    /**
     * Listens to the START and STOP events of the audio line.
    */
    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
         
        if (type == LineEvent.Type.STOP && loopCount == 0) {
            audioClip.close();
        } 
    }

    /*@Override
    public void update(LineEvent event) {
        //
    }*/
}

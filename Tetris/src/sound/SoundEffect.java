/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sound;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;


/**
 * This enum encapsulates all the sound effects of a game, so as to separate the sound playing
 * codes from the game codes.
 * 1. Define all your sound effect names and the associated wave file.
 * 2. To play a specific sound, simply invoke SoundEffect.SOUND_NAME.play().
 * 3. You might optionally invoke the static method SoundEffect.init() to pre-load all the
 *    sound files, so that the play is not paused while loading the file for the first time.
 * 4. You can use the static variable SoundEffect.volume to mute the sound.
 */
public enum SoundEffect {

    CERASE("tracks/ClassicErase.wav"),      
    CFALL("tracks/ClassicFall.wav"),   
    MERASE("tracks/MarioErase.wav"),      
    MFALL("tracks/MarioFall.wav"),  
    MGAMEOVER5("tracks/MarioGameover.wav"),
    MPAUSE("tracks/MarioPause.wav"),      
    PERASE("tracks/PacmanErase.wav"),      
    PFALL("tracks/PacmanFallt.wav"),  
    PGAMEOVER5("tracks/Pacmangameover.wav"),
    PPAUSE("tracks/PacmanPause.wav"),     
    SERASE("tracks/StarwarsErase.wav"),      
    SFALL("tracks/StarwarsFall.wav");
    
    
    private Clip clip;
    private FloatControl volumeControl;
    static int globalvolume;
    /**
     * Creates a new SoundEffect with each element of the enum with its own sound file.
     */
    SoundEffect(String soundFileName) {
        try {
            // Use URL (instead of File) to read from disk and JAR.
            URL url = this.getClass().getResource(soundFileName);
            // Set up an audio input stream piped from the sound file.
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            // Get a clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioInputStream);
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.err.println("It was not possible to start the sound");
        //    e.printStackTrace();
        } catch (Exception e) {
            System.err.println("It was not possible to start the sound");
        //    e.printStackTrace();
        }
    }

    /**
     * Adjuts the level of volume of the game.
     * @param newVolume defines the updated volume.
     */
    public void setVolume(int newVolume) {
        if(volumeControl == null)
            return;
        float volume;
        volume = (float) (volumeControl.getMaximum() - volumeControl.getMinimum());
        volume *= (float) Math.log(2*newVolume) /Math.log(2*100);
        volumeControl.setValue(volume+volumeControl.getMinimum());
    }
    /**
     * Adjuts the level of volume of the effets of game.
     * @param newVolume defines the updated volume.
     */    
    static public void setGlobalVolume(int newVolume) {
        for (SoundEffect se : SoundEffect.values()) {
            se.setVolume(newVolume);
        }
    }
    /*
     * Plays in a continous way a clip by rewinding a theme.
     */
    public void play() {
        if(volumeControl == null)
            return ;
        if (volumeControl.getValue() != volumeControl.getMinimum()) {
            if (clip.isRunning()) {
                clip.stop();   // Stop the player if it is still running
            }
            clip.setFramePosition(0); // rewind to the beginning
            clip.start();// Start playing
        }
    }
    /**
     * Maintains the execution of a clip.
     */
    public void setLoop() {
        if(clip == null)
            return;
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }
    /**
     * Determines the end in a execution of a clip.
     */
    public void setStop(){
        clip.stop();
    }

    /**
     * Optional static method to pre-load all the sound files.
     */ 
    public static void init() {
        values(); // calls the constructor for all the elements
    }
}

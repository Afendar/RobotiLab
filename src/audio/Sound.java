package audio;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

/**
 * Sound class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class Sound {
    
    public static Sound explosion = new Sound("/sfx/explosion.wav");
    public static Sound[] hit = {new Sound("/sfx/hit.wav"), new Sound("/sfx/hit.wav")};
    public static Sound[] jump = {new Sound("/sfx/jump.wav"), new Sound("/sfx/jump2.wav")};
    public static Sound[] lazer = {new Sound("/sfx/laser.wav"), new Sound("/sfx/laser2.wav")};
    public static Sound pickup = new Sound("/sfx/pickup.wav");
    public static Sound select = new Sound("/sfx/select.wav");
    public String path;
    public int volume;

    /**
     * 
     * @param path 
     */
    private Sound(String path){
        this.path = path;
        this.volume = 80;
    }

    /**
     * 
     * @param in
     * @param line 
     */
    private void stream(AudioInputStream in, SourceDataLine line){
        try{
            final byte[] buffer = new byte[65536];
            for(int n = 0;n != -1; n = in.read(buffer, 0, buffer.length)){
                line.write(buffer, 0, n);
            }
        }
        catch(IOException e){
            e.getMessage();
        }
    }
    
    /**
     * 
     */
    public void play(){
        this.volume = 80;
        try{
            URL url = this.getClass().getResource(this.path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            
            AudioFormat audioFormat = audioInputStream.getFormat();
            
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
            
            if(sourceLine.isControlSupported(FloatControl.Type.MASTER_GAIN)){
                FloatControl gainControl = (FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN);
                float attenuation = -80 + (80 * this.volume / 100);
                gainControl.setValue(attenuation);
            }
                
            sourceLine.start();
            
            int nBytesRead = 0;
            byte[] abData = new byte[128000];
            while (nBytesRead != -1) {
                    nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    @SuppressWarnings("unused")
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }
            sourceLine.drain();
            sourceLine.close();
        }catch(IOException|UnsupportedAudioFileException|LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }
}

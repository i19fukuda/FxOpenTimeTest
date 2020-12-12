package midi;

import java.util.ArrayList;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MidiConductor {

    private Sequence sequence;
    private Sequencer sequencer;
    private ArrayList<Track> tracks;
    private int tempo;

    public MidiConductor(int tempo){
        this.tempo = tempo;
        try {
            this.sequence = new Sequence(Sequence.PPQ,24);
            MetaMessage mmes = new MetaMessage();
            int l = 60*1000000/this.tempo;
            mmes.setMessage(0x51,new byte[]{(byte)(l/65536), (byte)(l%65536/256), (byte)(l%256)},3);

            this.tracks = new ArrayList<>();
            this.tracks.add(this.sequence.createTrack());
            this.tracks.get(0).add(new MidiEvent(mmes, 0));

            // 曲のデータが入るトラック
            this.tracks.add(this.sequence.createTrack());
            this.tracks.get(1).add(new MidiEvent(mmes, 0));

            this.sequencer = MidiSystem.getSequencer(false);
            Receiver receiver = MidiSystem.getReceiver();
            this.sequencer.getTransmitter().setReceiver(receiver);
            //this.sequencer.open();
            //this.sequencer.setSequence(this.sequence);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void play(long startTick){
        MyMidiPlayer player = new MyMidiPlayer(this.sequencer, this.sequence);
        player.start();
    }

    public void setNote(int note, int volume,long tick, long length){
        if(note > 127){
            System.out.println("out of range:" + note + "  set 255");
            note = 127;
        }
        if(volume > 127){
            System.out.println("out of range:" + volume + "set 255");
            volume = 127;
        }
        try{
            ShortMessage messageOn = new ShortMessage();
            messageOn.setMessage(ShortMessage.NOTE_ON, note, volume);

            ShortMessage messageOff = new ShortMessage();
            messageOff.setMessage(ShortMessage.NOTE_OFF, note, 0);

            MidiEvent eventOn = new MidiEvent(messageOn,tick);
            MidiEvent eventOff = new MidiEvent(messageOff, tick + length);

            this.sequence.getTracks()[1].add(eventOn);
            this.sequence.getTracks()[1].add(eventOff);

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void removeAllNoteFromeTrack(){
        Track track = sequence.getTracks()[1];
        while(track.size() > 0){
            track.remove(track.get(0));
        }
    }

    public Sequence getSequence() {
        return this.sequence;
    }
    public Sequencer getSequencer() {
        return this.sequencer;
    }

}

class MyMidiPlayer extends Thread{
    Sequencer sequencer;
    Sequence sequence;
    public MyMidiPlayer(Sequencer sequencer, Sequence sequence){
        this.sequencer = sequencer;
        this.sequence = sequence;
    }
    public void run(){
        try{
            this.sequencer.open();
            System.out.println("Sequencer open");
            this.sequencer.setSequence(sequence);
            System.out.println("sequencer start");
            this.sequencer.start();
            while(this.sequencer.isRunning()){
                Thread.sleep(100);
            }
            this.sequencer.stop();
            while(this.sequencer.isRunning()){
                Thread.sleep(100);
            }
            this.sequencer.close();
            //this.sequencer.setTickPosition(0L);
            System.out.println("sequencer stoped,closed succece");
        } catch (Exception e){
            try{
                this.sequencer.stop();
                this.sequencer.close();
                System.out.println("sequencer stoped,closed with error");

            } catch (Exception ee){}
            System.out.println(e.getMessage());
        }
    }
}
package views;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class NoteRect {
    //final private int RECT_SIZE = 48;
    private Rectangle noteRect;
    private long noteId;
    private long noteLength;
    private int notePich;
    private long noteStartTick;

    private boolean isControlDown = false;

    public NoteRect(int notePich, long noteLength, long noteStartTick){
        System.out.println("noteRect made");
        System.out.println(notePich + " " + noteLength + " " + noteStartTick);
        this.notePich =      notePich;
        this.noteLength =    noteLength;
        this.noteStartTick = noteStartTick;
        this.noteId = System.currentTimeMillis();

        this.noteRect = new Rectangle();
        this.noteRect.setHeight(20);
        this.noteRect.setWidth(noteLength);
        this.noteRect.setFill(Color.BLACK);

        this.noteRect.setOnMouseClicked(event -> clicEventHandler(event));
        //this.noteRect.setOnMouseDragged(event -> dragEventHandler(event));
        this.noteRect.setOnKeyPressed(event -> keyEventHandler(event));
    }

    private void clicEventHandler(MouseEvent event){
        if(event.getClickCount() == 1){
            this.noteRect.setFill(Color.RED);
            
            if(this.isControlDown){
                System.out.println("ctrl");
                this.noteRect.setFill(Color.BLUE);
                this.noteLength +=50;
                this.noteRect.setWidth(this.noteLength);
            }
        }
    }
    private void keyEventHandler(KeyEvent event){
        if(event.isControlDown()){
            this.isControlDown = true;
        } else this.isControlDown = false;
    }

    public long getNoteId(){
        return this.noteId;
    }
    public void setNoteId(long id){
        this.noteId =id;
    }

    public long getnoteLength(){
        return this.noteLength;
    }
    public void setnoteLength(long length){
        this.noteLength = length;
    }

    public int getNotePich(){
        return this.notePich;
    }
    public void setNotePich(int pich){
        this.notePich = pich;
    }

    public Rectangle getRect(){
        return this.noteRect;
    }

    public long getnoteStartTick(){
        return this.noteStartTick;
    }

}

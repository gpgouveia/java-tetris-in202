 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import tetris.Screen.OutOfScreenBoundsException;

/**
 *
 * @author felipeteles
 */
class Position {
    private short x;
    private short y;
    static Screen.BorderRetriever borderRetriever;

    public Position(Position old) throws OutOfScreenBoundsException{
        setX(old.getX());
        setY(old.getY());
        
    }
    public Position(int newX,int newY) throws OutOfScreenBoundsException{
        setX((short)newX);
        setX((short)newX);        
    }
    public Position(short newX, short newY) throws OutOfScreenBoundsException{
        setX(newX);
        setX(newX);
    }
    public void setX(short newX)throws OutOfScreenBoundsException{
        if(newX > borderRetriever.getMaxX() || newX < 0)
            throw new OutOfScreenBoundsException();
        x = newX;
    }
    public void setY(short newY)throws OutOfScreenBoundsException{
        if(newY > borderRetriever.getMaxY() || newY < 0)
            throw new OutOfScreenBoundsException();
        y = newY;
    }
    public void setPosition(short newX,short newY){
       try{
            setX(newX);
            setX(newX);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public short getX(){
        return x;//mudar
    }
    public short getY(){
        return y;//mudar
    }
    static public void setBorderRetriever(Screen.BorderRetriever borderRetriever){
        Position.borderRetriever = borderRetriever;
    }


}
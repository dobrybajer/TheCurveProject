package com.student.thecurvegame;

import java.io.Serializable;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Łukasz on 26.01.14.
 */
public class Message implements  Parcelable
{
    private ArrayList<String> Names;
    private ArrayList<Integer> Wsp;
    //Chord mChord;

    public Message(ArrayList<String> _Names,ArrayList<Integer> _Wsp )
    {
        Names=_Names;
        Wsp=_Wsp;
        //mChord=_mChord;

    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        // TODO Auto-generated method stub
        out.writeSerializable(Names);
        out.writeSerializable(Wsp);
       // out.writeValue(mChord);

    }
    public Message(Parcel in)
    {
        Names=(ArrayList<String>)in.readSerializable();
        Wsp = (ArrayList<Integer>) in.readSerializable();
       // mChord=(Chord) in.readValue(Chord.class.getClassLoader());
    }

    public static final Parcelable.Creator<Message> CREATOR= new Parcelable.Creator<Message>(){
        public Message createFromParcel(Parcel in){
            return new Message(in);
        }
        public Message[] newArray(int size)
        {
            throw new UnsupportedOperationException();
        }
    };

    public ArrayList<String> getNames()
    {
        return Names;
    }

    public ArrayList<Integer> getWsp()
    {
        return Wsp;
    }

    /*public Chord getmChord()
    {
        //return mChord;
    }*/
}



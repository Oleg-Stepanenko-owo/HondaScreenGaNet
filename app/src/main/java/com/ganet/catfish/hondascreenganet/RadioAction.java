package com.ganet.catfish.hondascreenganet;

import android.provider.MediaStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oleg on 29.07.2016.
 */
public class RadioAction {

    private Map<String, eRadioCommand> mCommand;

    static public enum eRadioType {
        eFM1,
        eFM2,
        eAM
    }

    static public enum eRadioCommand {
        ePlay,  //0100
        eSeekR, //0400
        eSeekL, //0500
        eChange,//0110
        eNone
    }

    RadioAction() {
        mCurrRAction = eRadioCommand.eNone;
        mRQuality = 0;
        mCommand = new HashMap<String, eRadioCommand>();
        mCommand.put("0100", eRadioCommand.ePlay);
        mCommand.put("0400", eRadioCommand.eSeekR);
        mCommand.put("0500", eRadioCommand.eSeekL);
        mCommand.put("0110", eRadioCommand.eChange);
    }

    /**
     *
     * @param comm
     * @return
     */
    public eRadioCommand getCommand( String comm ) {
        if( mCommand.containsKey(comm) )
            return mCommand.get(comm);
        return eRadioCommand.eNone;
    }

    /**
     * 
     * @param radioType
     */
    public void setRadioType( String radioType ) {
        if(radioType.equals("01"))mRadioType = eRadioType.eFM1;
        else if(radioType.equals("02"))mRadioType = eRadioType.eFM2;
        else if(radioType.equals("11"))mRadioType = eRadioType.eAM;
    }

    public void setFrequency( String data ) {
        String tmpFrq = data.replace("F","");
        if( mRadioType == eRadioType.eAM ) mFrequency = tmpFrq;
        else {
            mFrequency = tmpFrq.substring( 0, tmpFrq.length() -1 );
            mFrequency += ("." + tmpFrq.substring( tmpFrq.length() -1, tmpFrq.length() ));
        }
    }

    public eRadioCommand mCurrRAction;
    public eRadioType mRadioType;
    public String mFrequency;
    public int mStoreID;
    public int mRQuality;
}

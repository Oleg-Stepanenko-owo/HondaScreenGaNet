package com.ganet.catfish.hondascreenganet;

import com.ganet.catfish.hondascreenganet.Data.ActiveTrack;
import com.ganet.catfish.hondascreenganet.Data.DevTime;
import com.ganet.catfish.hondascreenganet.Data.Folder;
import com.ganet.catfish.hondascreenganet.Data.RadioAction;
import com.ganet.catfish.hondascreenganet.Data.Track;
import com.ganet.catfish.hondascreenganet.Data.Volume;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by oleg on 25.07.2016.
 */
public class GaNetManager {
    private MainActivity mainActivity;
    public int currentDiskId;
    public int currentLevel;

    public Folder mFolder;
    public Map<Integer, Track> mTrack;
    public DevTime mDevTime;
    public ActiveTrack mActiveTrack;
    public Volume mVol;
    public RadioAction mRadio;

    public ParserGANET mParser;

    GaNetManager( MainActivity mainA ) {
        mainActivity = mainA;
        mActiveTrack = new ActiveTrack();
        mTrack = new HashMap<Integer, Track>();
        mDevTime = new DevTime();
        mFolder = new Folder();
        mVol = new Volume();
        mRadio = new RadioAction();
        mParser = new ParserGANET( this );
    }

    public ParserGANET getParser() {
        return mParser;
    }

    public void invalidate( ParserGANET.eParse activeParseID ) { mainActivity.invalidate( activeParseID ); }

    public String getTrackById( int trackID ){
        String returnVal = "";
        if( mTrack.containsKey(Integer.valueOf(trackID)) ){
            returnVal = mTrack.get(Integer.valueOf(trackID)).getName();
        }
        return returnVal;
    }

}

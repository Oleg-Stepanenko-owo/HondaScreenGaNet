package com.ganet.catfish.hondascreenganet;

import java.util.Vector;

/**
 * Created by oleg on 25.07.2016.
 */
public class GaNetManager {
    private MainActivity mainActivity;

    public ActiveTrack mActiveTrack;
    public Vector<Folder> vFolder;
    public Vector<Track> vTrack;
    public DevTime devTime;
    public ParserGANET mParser;

    GaNetManager( MainActivity mainA ) {
        mainActivity = mainA;
        mParser = new ParserGANET();
        mActiveTrack = new ActiveTrack();
    }

    public ParserGANET getParser() {
        return mParser;
    }

    public void invalidate() {
        switch ( mParser.getActiveParseID() ){
            case eTr:
                mainActivity.invalidate( ParserGANET.eParse.eTr );
                break;
            case eFolder:
                break;
            case eTime:
                mainActivity.invalidate( ParserGANET.eParse.eTime );
                break;
            case eNone: ;
                break;
            case eActiveTr:
                mainActivity.invalidate( ParserGANET.eParse.eActiveTr );
                break;
        }
    }
}

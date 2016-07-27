package com.ganet.catfish.hondascreenganet;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by oleg on 25.07.2016.
 */
public class ParserGANET {

    static public enum eParse{
        eActiveTr,
        eTr,
        eFolder,
        eTime,
        eNone
    }

    private String receivedLine;
    private ActiveTrack mActiveTrack;
    private Folder mFolder;
    private Map<Integer, Track> mTrack;
    private DevTime mDevTime;
    // private Track tempParseTrack;


    private eParse activeParseID;

    private String srcDev, dstDev;
    private String commandDev;
    private String exCommand;
    private String dataDev;

    ParserGANET() {
        mTrack = new HashMap<Integer, Track>();
        mTrack.clear();
        mActiveTrack = new ActiveTrack();
    }

    public eParse parseLine( String line ){
        activeParseID = eParse.eNone;
        // <GA:183 100 600D01000140 1517 0000CD> - TIME
        if( line.indexOf("<GA:") != -1 ){
            int chPos = 4;
            srcDev = line.substring( chPos, chPos += 3 );
            dstDev = line.substring( chPos, chPos += 3 );

            // TIME
            if( line.indexOf("600D01000140", chPos) != -1 ){
                commandDev = line.substring( chPos, chPos += 12 );
                dataDev = line.substring( chPos, chPos += 4 );
                mDevTime = new DevTime( dataDev );
                activeParseID = eParse.eTime;
            } else if( (line.indexOf("684B3102") != -1) && dstDev.equals("131") ) {
                commandDev = line.substring( chPos, chPos += 8 );
                exCommand = line.substring( chPos, chPos += 4 );
                int endPos = (line.length() - 3); //without 2last symbols (CRS)
                dataDev = line.substring( chPos, endPos );
                if( getExCommand(exCommand) == MainGanetPKG.eExCommand.eINFO ) {
                    Track tempParseTrack = new Track();
                    tempParseTrack.updateActiveTrackInfo(dataDev);

                    if( mTrack.containsKey(tempParseTrack.getTrackId()) ) {
                        Track margeParseTrack = mTrack.get(tempParseTrack.getTrackId());
                        margeParseTrack.trackMarge(tempParseTrack);
                        mTrack.put(tempParseTrack.getTrackId(), margeParseTrack);
                    } else mTrack.put(tempParseTrack.getTrackId(), tempParseTrack );

//                    mActiveTrack.updateActiveTrackInfo(dataDev);
                    activeParseID = eParse.eTr;
                }
            }
        }
        return activeParseID;
    }

    private MainGanetPKG.eExCommand getExCommand(String exCommand) {
        switch( exCommand ) {
            case "0377":
                return MainGanetPKG.eExCommand.eINFO;
            case "0300":
                return MainGanetPKG.eExCommand.ePLAY;
        }
        return MainGanetPKG.eExCommand.eNONE;
    }

    public ActiveTrack getActiveTrack(){
        return mActiveTrack;
    }

    public Folder getFolder(){
        return mFolder;
    }

    public Track getTrackByID( int id ){
        return mTrack.get( Integer.valueOf(id) );
    }
    public  Map<Integer, Track> getTracksList() { return mTrack; }

    public DevTime getDevTime() { return mDevTime; }

    public eParse getActiveParseID(){
        return activeParseID;
    }

}

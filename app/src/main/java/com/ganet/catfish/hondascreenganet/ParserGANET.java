package com.ganet.catfish.hondascreenganet;

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
    private Track mTrack;
    private DevTime mDevTime;


    private eParse activeParseID;

    private String srcDev, dstDev;
    private String commandDev;
    private String dataDev;

    ParserGANET() {

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
            }
        }
        return activeParseID;
    }

    public ActiveTrack getActiveTrack(){
        return mActiveTrack;
    }

    public Folder getFolder(){
        return mFolder;
    }

    public Track getTrack(){
        return mTrack;
    }

    public DevTime getDevTime() { return mDevTime; }

    public eParse getActiveParseID(){
        return activeParseID;
    }

}

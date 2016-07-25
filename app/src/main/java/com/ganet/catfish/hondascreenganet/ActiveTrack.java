package com.ganet.catfish.hondascreenganet;

/**
 * Created by oleg on 25.07.2016.
 */
public class ActiveTrack {

    int diskID;
    int playMin;
    int playSec;
    int folderId;
    int trackId;

    ActiveTrack(){
        diskID = -1;
        playMin = 0;
        playSec = 0;
        folderId = -1;
        trackId = -1;
    }

    /*
    need to parse active track info.
     */
    public void updateInfo( String infoStr ) {

    }
}

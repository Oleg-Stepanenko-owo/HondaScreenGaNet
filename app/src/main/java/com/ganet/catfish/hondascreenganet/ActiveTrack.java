package com.ganet.catfish.hondascreenganet;

/**
 * Created by oleg on 25.07.2016.
 * current play active track
 */
public class ActiveTrack {

    public int diskID;
    public int playMin;
    public int playSec;
    public int folderId;
    public int subFolderId;
    public int trackId;
    public boolean selectedTrack;

    private int currPack, allPack;
    private boolean p0, p1, p2, p3;
    private String pS0, pS1, pS2, pS3;
    private boolean readyShow;

    private boolean isReadyToShow() {
        return readyShow;
    };

    ActiveTrack() {
        diskID = -1;
        playMin = 0;
        playSec = 0;
        folderId = -1;
        trackId = -1;
        resetTrackName();
    }


    private void resetTrackName() {
        p0 = false;
        p1 = p0;
        p2 = p0;
        p2 = p0;
        pS0 = "";
        pS1 = "";
        pS2 = "";
        pS3 = "";
        readyShow = false;
    }


    private void updateNameTrack( String text ) {
        String trackTextTmp = "";
        for( int a = 2; a < text.length(); a +=2 ) {
            String strTmp = text.substring( a, a+=2 );
            if( !strTmp.equals("FF") ) {
                int tmpInt = Integer.parseInt( strTmp, 16 );
                trackTextTmp += Character.toString((char)tmpInt);
            }
        }

        if( currPack == 0 ) { pS0 = trackTextTmp; p0 = true; }
        else if( currPack == 1 ) { pS1 = trackTextTmp; p1 = true; }
        else if( currPack == 2 ) { pS2 = trackTextTmp; p2 = true; }
        else if( currPack == 3 ) { pS3 = trackTextTmp; p3 = true; }
    }
}

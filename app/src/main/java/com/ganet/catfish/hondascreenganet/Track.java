package com.ganet.catfish.hondascreenganet;

/**
 * Created by oleg on 25.07.2016.
 */
public class Track {
//---------------------------------------------------
    private int folderId;
    private int subFolderId;
    private int trackId;
    public boolean selectedTrack;

    private int currPack, allPack;
    public boolean p0, p1, p2, p3;
    public String pS0, pS1, pS2, pS3;
//-----------------------------------------------------

    Track() {
        p0 = false;
        p1 = p0;
        p2 = p0;
        p3 = p0;
        pS0 = "";
        pS1 = "";
        pS2 = "";
        pS3 = "";
    }

    public boolean isReadyToShow() {
        return (p0 && p1 && p2 && p3);
    };

//    public void updateTrackInfo( String data ) {
//
//    }
//
//    private void getExCommand() {
//
//    }

    /*
   need to parse active track info.
   Track NAME --------------------------------------------------------------------------------------------
< d 183 131
1E   684B3102	0377    0F 02	01 0F      30         02     		03       00300036005F004F0061007300690073   92
1E   684B3102	0377    0F 02	01 0F      30         02     		13       005F004C00690076006500200046006F   A2
1E   684B3102	0377    0F 03	01 0F      45         02     		33       00540068006500200057006F0072006C   F9
   Track pack | info |	folder|subfolder|track number|02-not select|pack/ALL|         Track name             |
                                                     12-select
    */

    /**
     * updateActiveTrackInfo
     * @param data
     */
    public void updateActiveTrackInfo( String data ) {
        int textPos = 2;
        String valueCom;

        valueCom = data.substring( textPos, (textPos +=2) );
        folderId = Integer.valueOf(valueCom);

        valueCom = data.substring( textPos, (textPos +=2) );
        subFolderId = Integer.valueOf(valueCom);

        valueCom = data.substring( (textPos+=2), (textPos +=2) );
        trackId = Integer.valueOf(valueCom);

        valueCom = data.substring( textPos, (textPos +=2) );
        if( valueCom.getBytes()[0] == '0' ) selectedTrack = false;
        else selectedTrack = true;

        valueCom = data.substring( textPos, (textPos +=1) );
        currPack = Integer.valueOf(valueCom);

        valueCom = data.substring( textPos, (textPos +=1) );
        allPack = Integer.valueOf(valueCom);
        if( allPack == 2)       {p3 = true;}
        else if( allPack == 1 ) { p3 = true; p2 = true; }
        else if( allPack == 0 ) { p3 = true; p2 = true; p1 = true; }

        valueCom = data.substring( textPos, data.length() );
        updateNameTrack(valueCom);
    }

    /**
     * updateNameTrack
     * @param text
     */
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

    /**
     * resetTrackName
     */
    private void resetTrackName() {
        p0 = false;
        p1 = p0;
        p2 = p0;
        p2 = p0;
        pS0 = "";
        pS1 = "";
        pS2 = "";
        pS3 = "";
    }

    /**
     * getTrackId
     * @return
     */
    public Integer getTrackId(){
        return Integer.valueOf( trackId );
    }

    /**
     * trackMarge
     * @param tempParseTrack
     */
    public void trackMarge(Track tempParseTrack) {
        if( trackId == tempParseTrack.getTrackId().intValue() )
        {
            if( tempParseTrack.p0 && !p0 ) {
                p0 = true;
                pS0 = tempParseTrack.pS0;
            }
            if( tempParseTrack.p1 && !p1 ) {
                p1 = true;
                pS1 = tempParseTrack.pS1;
            }
            if( tempParseTrack.p2 && !p2 ) {
                p2 = true;
                pS2 = tempParseTrack.pS2;
            }
            if( tempParseTrack.p3 && !p3 ) {
                p3 = true;
                pS3 = tempParseTrack.pS3;
            }
        }
    }

    /**
     * getName
     * @return
     */
    public String getName() {
        String returnVal = "<.......>";

        if( isReadyToShow() )
            returnVal = pS0 + pS1 + pS2 + pS3;
        return returnVal;
    }
}

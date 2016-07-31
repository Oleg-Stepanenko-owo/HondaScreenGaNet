package com.ganet.catfish.hondascreenganet.Data;

/**
 * Created by oleg on 25.07.2016.
 * current play active track
 */
public class ActiveTrack {

    public int diskID;
    public int playMin;
    public int playSec;
    public int folderId;
    public int trackId;

    public ActiveTrack() {
        diskID = -1;
        playMin = 0;
        playSec = 0;
        folderId = -1;
        trackId = -1;

    }

    /**
     20 684B310203   	00     	F3    	60   	FF  F020    0F 03 0F    46    FFFFFFFF  2130 0F 03 0F 19002030200101  C5
     20 684B310203   	00     	F3    	60   	FF  F003    0F 01 0F    10    FFFFFFFF  2130 0F 03 0F 18002030200101  6F
     20	684B310203		00		F5		60		FF	F235	0F 00 0F	02	  FFFFFFFF	2130 0F 00 0F 03002030200000  7C
     20	684B310203		00		F5		60		FF	F236	0F 00 0F	02	  FFFFFFFF	2130 0F 00 0F 03002030200000  7D
     20	684B310203		00		F3		60		FF	F000	0F 02 0F    43	  FFFFFFFF	2130 0F	03 0F 19002030200101  A1
        Track pack |     play | DISK |        	  | TIME  |  folder | Track|
                                                    FFFF - isert
                                                    F000 - start play
     <GA:183131684B31020300 F360FFF0000F010F01FFFFFFFF21300F130F03002030200104  5B>
     <GA:183131684B31020300 F360FFF0090F010F01FFFFFFFF21300F130F03002030200104  64>
     <GA:183131684B31020300 F360FFF1150F010F01FFFFFFFF21300F130F03002030200104  71>
     *
     * @param data
     */
    public void updateActiveTrackInfo( String data ) {
        int textPos = 1;
        String valueCom;
        //DISK INFO --------------------
        valueCom = data.substring( textPos, (textPos +=1) );
        diskID = Integer.valueOf(valueCom);

        valueCom = data.substring( textPos+=4, textPos +=2 );
        valueCom = valueCom.replace( "F", "" );
        playMin = Integer.valueOf(valueCom);

        valueCom = data.substring( textPos, textPos +=2 );
        valueCom = valueCom.replace( "F", "" );
        playSec = Integer.valueOf(valueCom);

        valueCom = data.substring( textPos +=2, textPos +=2 );
        valueCom = valueCom.replace( "F", "" );
        folderId = Integer.valueOf(valueCom);

        valueCom = data.substring( textPos +=2, textPos +=2 );
        valueCom = valueCom.replace( "F", "" );
        trackId = Integer.valueOf(valueCom);
    }
}

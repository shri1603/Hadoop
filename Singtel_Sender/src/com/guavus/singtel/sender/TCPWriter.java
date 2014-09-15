
package com.guavus.singtel.sender;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import java.util.Properties;
import java.nio.ByteBuffer;

class TCPWriter
{

    public static Properties propertyFile = new Properties();
    public static String GNGiDataSourceFolderPath;
    public static String GNGiDataOutputFolderPath;
    public static String Port;
    public static String IP;
    public static ByteBuffer inbytes;

    public static void main(String argv[]) throws Exception
    {
        Socket clientSocket = new Socket("192.168.1.214",1024);
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
       // long lnCurrentTime = 0l;
     // System.out.println(PropUtil.getPropertyValue(PropUtil.GnGi_IP)+Integer.valueOf(PropUtil.getPropertyValue(PropUtil.GnGi_Port));
        BufferedOutputStream outToServer = new BufferedOutputStream(clientSocket.getOutputStream());
        
        //long starttime = System.currentTimeMillis();
        //int i=1;
        byte[] data = new byte[8192];
        //byte[] currDate = new byte[8];
        //int[] lineFeed = new int[20];
        //long iSentBytes = 0;
        //long iMBDivisor = (1024 * 1024);
         //sdf.setTimeZone(TimeZone.getTimeZone("UTC"));  
        while(true)
        {
                //FileInputStream fin = new FileInputStream(PropUtil.getPropertyValue(PropUtil.GnGi_INPUT_FILE_PATH));
               // FileInputStream fin = new FileInputStream("/Users/shridhar/Desktop/gng5K.txt");///Volumes/D/Singtel/GnGi Sample 12 Jul 2013
        	//FileInputStream fin = new FileInputStream("/Users/ajit/Documents/singtel/GnGi Archive/gng5K.txt");
              FileInputStream fin = new FileInputStream("/data/Singtel/gng5K.txt");
                int read = 0;
                int ln=0;
               // long cmp = 0l;
               
                while ( (read = fin.read(data)) != -1 )
                {
                   /* lnCurrentTime = System.currentTimeMillis();       
                    currDate = sdf.format(new Date(lnCurrentTime)).getBytes();
                    
                    int iIndex = 0, iDP = 0;
                    for(int a=0; a<read; a++){
                        if(data[a] == (byte)((char)10)){
                            lineFeed[iIndex] = a;
                            iIndex++;
                        }
                    }                
                    
                    //Replace PdpActivation Time
                    iIndex = 0;
                    if(lineFeed[iIndex] < 483){
                        iDP = lineFeed[iIndex] + 91 + 1;
                        iIndex++;
                    }else{
                        iDP = 91;
                    }
                    while((iDP + 8) < read){
                        System.arraycopy(currDate, 0, data, iDP, 8);
                        if(lineFeed[iIndex] == 0)
                            break;
                        iDP = lineFeed[iIndex] + 91 + 1;
                        iIndex++;
                    }
                    
                    //Replace Uplink FlowStart Time
                    iIndex = 0;
                    if(lineFeed[iIndex] < 483){
                        iDP = lineFeed[iIndex] + 131 + 1;
                        iIndex++;
                    }else{
                        iDP = 131;
                    }
                    while((iDP + 8) < read){
                      
                        System.arraycopy(currDate, 0, data, iDP, 8);
                        if(lineFeed[iIndex] == 0)
                            break;
                        iDP = lineFeed[iIndex] + 131 + 1;
                        iIndex++;
                    }
                    
                    //Replace Uplink FlowEnd Time
                    iIndex = 0;
                    if(lineFeed[iIndex] < 483){
                        iDP = lineFeed[iIndex] + 145 + 1;
                        iIndex++;
                    }else{
                        iDP = 145;
                    }
                    while((iDP + 8) < read){
                       
                        System.arraycopy(currDate, 0, data, iDP, 8);
                        if(lineFeed[iIndex] == 0)
                            break;
                        iDP = lineFeed[iIndex] + 145 + 1;
                        iIndex++;
                    }
                    
                    //Replace Downlink FlowStart Time
                    iIndex = 0;
                    if(lineFeed[iIndex] < 483){
                        iDP = lineFeed[iIndex] + 186 + 1;
                        iIndex++;
                    }else{
                        iDP = 186;
                    }
                    while((iDP + 8) < read){
                       
                        System.arraycopy(currDate, 0, data, iDP, 8);
                        if(lineFeed[iIndex] == 0)
                            break;
                        iDP = lineFeed[iIndex] + 186 + 1;
                        iIndex++;
                    }
                    
                    //Replace Downlink FlowEnd Time
                    iIndex = 0;
                    if(lineFeed[iIndex] < 483){
                        iDP = lineFeed[iIndex] + 200 + 1;
                        iIndex++;
                    }else{
                        iDP = 200;
                    }
                    while((iDP + 8) < read){
                        
                        System.arraycopy(currDate, 0, data, iDP, 8);
                        if(lineFeed[iIndex] == 0)
                            break;
                        iDP = lineFeed[iIndex] + 200 + 1;
                        iIndex++;
                    } */

                    outToServer.write(data,0,read); 
                    
                   /* iSentBytes += read;
                    ln++;
                    cmp = starttime + (60000*i);
                    if(cmp <= System.currentTimeMillis())
                    {      	
                        System.out.println("Data Send"+((ln*8192)/(60*1024*1024)));
                        ln=0;
                        i++;
                    }*/
                    
                }
                fin.close(); 
        }
}
}

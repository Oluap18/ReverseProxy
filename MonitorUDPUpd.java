import java.io.*;
import java.net.*;
import java.util.*;

public class MonitorUDPUpd extends Thread{

   private DatagramSocket clientSocket;

   public MonitorUDPUpd(DatagramSocket c){
      this.clientSocket = c;
   }

   public void run(){
      try{
         byte[] receiveData = new byte[1024];
         byte[] sendData = new byte[1024];
         DatagramPacket sendPacket;
         DatagramPacket receivePacket;
         while(true){
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            InetAddress ipAddress = receivePacket.getAddress();
            switch(receiveData[0]){
               case 1:
                  sendData = new byte[receivePacket.getLength()];
                  System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), sendData, 0, receivePacket.getLength());
                  sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 5555);
                  clientSocket.send(sendPacket);
                  break;
            }
         }
    	}
      catch(Exception e){}
   }
}
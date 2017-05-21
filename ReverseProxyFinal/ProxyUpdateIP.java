import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.Timestamp;

public class ProxyUpdateIP extends Thread{

	private Map<InetAddress,TabelaMonitor> tabela;
	private InetAddress ipAddress;
	private DatagramSocket serverSocket;
	private byte[] packet;
	private static Object obj = new Object();
	private PacketLossTime task;
	private Timer timer;
	private boolean cond[] = new boolean[1];
	private byte nextPacket;
	private int duplicados;

	public ProxyUpdateIP(Map<InetAddress, TabelaMonitor> t, InetAddress i, DatagramSocket s){
      	this.tabela = t;
        this.ipAddress = i;
        this.serverSocket = s;
        packet = new byte[1024];
        cond[0] = true;
   	}

   	public void run(){
      	try{
      		TabelaMonitor t;
      		synchronized(tabela){ t = tabela.get(ipAddress); }
      		while( tabela.containsKey(ipAddress) && checkStatus(t)){
      			pooling(t);
      			Thread.sleep(5000);
      		}
      	}
      	catch(Exception e){
      		e.printStackTrace();
      	}
      	tabela.remove(ipAddress);
 	}

 	public boolean checkStatus(TabelaMonitor t){
 		Timestamp now = new Timestamp(System.currentTimeMillis());
 		synchronized(t){
 			if(now.getTime()-t.getTime().getTime()>20000)
 				return false;
 		}
      	return true;
 	}

 	public synchronized void pooling(TabelaMonitor t){
 		byte[] sendData = new byte[2];
 		Timestamp init, after;
 		int packetCount=0, rtt=0, packetLoss=0;
 		int tentativas = 0;
 		byte[] timestamp = new byte[100];
 		byte[] auxaux = new byte[100];
 		duplicados=0;
 		packet[1] =2;
 		try{
 			for(nextPacket=0;nextPacket<2;){
 				cond[0] = true;
 				init = new Timestamp(System.currentTimeMillis());
 				timestamp = init.toString().getBytes();
		      	sendData = new byte[timestamp.length+2];
	      		sendData[0] = 1;
	      		sendData[1] = nextPacket;
	      		System.arraycopy(timestamp, 0, sendData, 2, timestamp.length);
	      		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 5555);
	      		packetCount++;
		      	synchronized(obj){
		      		task = new PacketLossTime(obj, cond);
		      		timer = new Timer();
		      		serverSocket.send(sendPacket);
		      		if(t.getPacketCount()==0){
		      			timer.schedule(task, 100);
		      		}
		      		else{
		      			timer.schedule(task, (long) t.getRtt()*2);
		      		}
		      		while(cond[0])	
		      			obj.wait();
		      		if(packet[1]==nextPacket){
		      			after = new Timestamp(System.currentTimeMillis());
		      			byte[] time = new byte[packet.length-2];
		      			System.arraycopy(packet, 2, time, 0, packet.length-2);
		      			String now = new String(time);
		      			rtt += after.getTime() - Timestamp.valueOf(now).getTime();
		      			tentativas=0;
		      			nextPacket++;
		      		}
		      		else{
		      			packetLoss++;
		      			tentativas++;
		      		}
		      	}
		      	if(tentativas==4)
		      			break;
		    }
		    synchronized(t){
		    	t.addPacketCount(packetCount);
		    	t.addRtt(rtt);
		    	t.addPacketLoss(packetLoss);
		    	t.addDuplicados(duplicados);
			}
	  	}
      	catch(Exception e){
      		e.printStackTrace();
      	}
 	}

 	public void setPacket(byte[] d){
 		synchronized(obj){
 			if(nextPacket == d[1]){
 				this.packet = d;
 				timer.cancel();
 				timer.purge();
 				cond[0] = false;
 				obj.notify();
 			}
 			else{
 				duplicados++;
 			}
 		}
 	}

}
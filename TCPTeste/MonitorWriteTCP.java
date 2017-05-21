import java.io.*;
import java.net.*;
import java.util.*;

public class MonitorWriteTCP extends Thread{

	private Socket socket;
	private String str;
	private static Object obj = new Object();
    private boolean cond;
	
	public MonitorWriteTCP(Socket monitor){
		this.socket=monitor;
		cond = true;
	}

	public void setMensagem(String st){
        str = st;
        cond=false;
        //System.out.println("SetMensagem write tcp monitor " + cond +  socket.getInetAddress());
        synchronized(obj){
            obj.notifyAll();
        }
    }

	public void run(){
		try{
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			while(true){
				//System.out.println("Comecei o write monitor");
				cond=true;
                synchronized(obj){
                    while(cond){
                    	//System.out.println("Wait write tcp monitor " + socket.getInetAddress());
                        obj.wait();
                    }
                    //System.out.println("Acordei MonitorWrite " + socket.getInetAddress());
                }
                //System.out.println("Recebi a mensagem do monitor " + socket.getInetAddress());
				if(str==null)
					break;
				out.println(str);
				out.flush();
				System.out.println("Escrevi-a MonitorWrite.");
			}
		}
		catch(Exception e){}
		System.out.println("Sai do write.");
		try{
			socket.shutdownOutput();
			socket.close();
		}
		catch(Exception e){}
	}
}	

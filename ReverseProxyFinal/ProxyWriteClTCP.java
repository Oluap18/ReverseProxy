import java.io.*;
import java.net.*;
import java.util.*;

public class ProxyWriteClTCP extends Thread{

    private Socket socket;
    private String str;
    private static Object obj = new Object();
    private boolean cond;

    public ProxyWriteClTCP(Socket s){
        this.socket = s;
        cond = true;
    }

    public void setMensagem(String st){
        str = st;
        //System.out.println("Set Mensagem write tcp cliente " + cond + socket.getInetAddress());
        cond=false;
        synchronized(obj){
            obj.notifyAll();
        }
    }

    public void run(){
        try{
            boolean wh = true;
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            while(wh){
                cond=true;
                synchronized(obj){
                    while(cond){
                        //System.out.println("Wait write cliente " + cond + socket.getInetAddress() + str);
                        obj.wait();
                    }
                    //System.out.println("Acordei WriteCL " + socket.getInetAddress());
                }
                if(str==null)
                    break;
                out.println(str);
                out.flush();
                //System.out.println("Escrevi WriteCL " + socket.getInetAddress());
            }
    	}
        catch(Exception e){}
        System.out.println("Cliente Fechou.");
        try{
            socket.shutdownOutput();
            socket.close();
        }
        catch(Exception e){}
    }
}
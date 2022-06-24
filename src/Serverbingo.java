import java.net.*;
import java.io.*;
import java.util.*;

public class Serverbingo{
	HashMap clients;
	boolean pro=true;
	public Serverbingo() {
		clients=new HashMap();
		Collections.synchronizedMap(clients);
	}
	public synchronized void bool(int i) {
		if(i==1) {
			pro=false;
		}else {
			pro=true;
		}
	}
	public void start() {
		ServerSocket serverSocket=null;
		Socket socket=null;
		try {
			String colorname = null;
			serverSocket=new ServerSocket(7777);
			System.out.println("서버가 준비되었습니다.");
			while(true) {
				socket=serverSocket.accept();
				System.out.println("["+socket.getInetAddress()+":"+socket.getPort()+"로부터 연결요청이 들어왔습니다.");
				if(clients.size()<2) {
					if(clients.size()==0) {
						colorname="red";
					}else {
						colorname="green";
					}
					ServerReceiver thread=new ServerReceiver(socket,colorname);
					thread.start();
				}else {
					System.out.println("연결요청 거부됨");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	void nameToAll() {
		Iterator it=clients.keySet().iterator();
		String name="",na="name";
		while(it.hasNext()) {
			name+=(String)it.next()+"\n";
		}
		sendToAll(name,na);
	}
	void sendToAll(String msg,String colorname) {
		Iterator it=clients.keySet().iterator();
		while(it.hasNext()) {
			try {
				DataOutputStream out=(DataOutputStream)clients.get(it.next());
				out.writeUTF(colorname+" "+msg);
			}catch(IOException e) {}
		}
	}
	void sendToAll(String msg) {
		Iterator it=clients.keySet().iterator();
		while(it.hasNext()) {
			try {
				DataOutputStream out=(DataOutputStream)clients.get(it.next());
				out.writeUTF(msg);
			}catch(IOException e) {}
		}
	}
	void sendToOne(String msg) {
		int i=0;
		Iterator it=clients.keySet().iterator();
		while(it.hasNext()) {
			DataOutputStream out=(DataOutputStream)clients.get(it.next());
			try {
				if(pro==true) {
					out.writeUTF(msg);
					bool(1);
					break;
				}else {
					if(i==1) {
						out.writeUTF(msg);
						bool(2);
						break;
					}
				}
				i++;
			}catch(IOException e) {}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Serverbingo().start();
	}
	class ServerReceiver extends Thread{
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		String name="",color;
		String[] n;
		boolean bingo=false;
		ServerReceiver(Socket socket,String colorname){
			this.socket=socket;
			color=colorname;
			try {
				in=new DataInputStream(socket.getInputStream());
				out=new DataOutputStream(socket.getOutputStream());
			}catch(IOException e) {}
		}
		public void run() {
			try {
				name=in.readUTF();
				clients.put(name, out);
//				sendToAll(name+"님이 들어오셨습니다.");
				System.out.println("1현재 접속자 수 : "+clients.size());
				while(true) {
					if(clients.size()==2) {
						sendToOne("next 0");
						nameToAll();
					}
					while(in!=null) {
						String s=in.readUTF();
						n=s.split(" ");
						
						if(n[0].equals("next")) {
							sendToOne(n[0]+" "+n[1]);
						}else if(n[0].equals("빙고")) {
							bingo=true;
							break;
						}else if(n[0].equals("restart")) {
							break;
						}else if(s.charAt(0)=='[') {
							sendToAll(s,color);
						}
					}
					break;
				}

			}catch(IOException e) {
				
			}finally {
				if(bingo==true) {
					sendToAll("e "+name+" 님이 승리하셨습니다. "+n[1]);
				}else {
					sendToAll("re "+name+" 님이 기권하셨습니다.");
				}
				clients.remove(name);
				pro=true;
				System.out.println("현재 접속자 수 : "+clients.size());
			}
		}
	}

}
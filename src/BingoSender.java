import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BingoSender {
	Socket socket;
	Bingo bingo;
	DataOutputStream out;
	BingoSender(Bingo bingo){
		this.bingo=bingo;
		this.socket=bingo.socket;
		
		try {
			out=new DataOutputStream(socket.getOutputStream());
		}catch(Exception e) {}
	}
	public void namesend() {
		try {
			out.writeUTF(bingo.name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void send() {
		try {
			out.writeUTF(bingo.sendstring);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

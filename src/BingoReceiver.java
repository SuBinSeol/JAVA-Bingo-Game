import java.awt.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import javax.swing.text.*;
import java.time.*;
import javax.swing.*;


public class BingoReceiver extends Thread{
	Socket socket;
	Bingo bingo;
	DataInputStream in;
	Color c;
	String name="";
	BingoReceiver(Socket socket,Bingo bingo){
		this.socket=socket;
		this.bingo=bingo;
		try {
			in=new DataInputStream(socket.getInputStream());
		}catch(Exception e) {}
	}
	public void changeColor(String color) {
        if(color.equals("red")) {
        	c=Color.red;
        }else if(color.equals("orange")) {
        	c=Color.orange;
        }else if(color.equals("green")) {
        	c=Color.green;
        }else if(color.equals("blue")) {
        	c=Color.blue;
        }else if(color.equals("magenta")) {
        	c=Color.magenta;
        }
	}
	public void run() {
        StyledDocument doc = bingo.textPane.getStyledDocument();
        SimpleAttributeSet styleSet = new SimpleAttributeSet();
		while(true) {
			try {
				String[] r=in.readUTF().split(" ");
				if(r[0].equals("next")) {
					if(Integer.parseInt(r[1])>0) {
						for(int a=0;a<bingo.size;a++) {
							for(int b=0;b<bingo.size;b++) {
								if(bingo.numButtons[a][b].getText().equals(r[1])) {
									bingo.numButtons[a][b].setContentAreaFilled(true);
									bingo.numButtons[a][b].setBackground(Color.lightGray);
									bingo.Mode2();
									break;
								}
							}
						}
					}else {
						bingo.setstarttime();
					}
					bingo.time=true;
					bingo.Time();
				}else if(r[0].equals("name")){
					name=" 접속한 사람 \n";
					for(int j=1;j<r.length;j++) {
						name+=r[j]+"\n";
					}
					bingo.n_ta.setText("\n"+name);
				}else if(r[0].equals("red")||r[0].equals("green")){
					changeColor(r[0]);
		            StyleConstants.setForeground(styleSet, c);  // 전경색 지정
		            try {
		                doc.insertString(doc.getLength(), "\n"+r[1], styleSet);
		                bingo.textPane.setCaretPosition(bingo.textPane.getDocument().getLength());
		            } catch (BadLocationException e1) {
		                e1.printStackTrace();
		            }
				}else {
					 JOptionPane.showMessageDialog(null, r[1]+r[2]+r[3]+"\n"+r[4]);
					 Connection conn=null;
					 MariaDB mdb=new MariaDB();
					 conn=mdb.connectDB();

					 PreparedStatement pstmt=null;
					 try {
					     DateFormat df = new SimpleDateFormat("HH:mm:ss");
					     Date date = null;
					     date = df.parse(r[4]);
					     long time = date.getTime();
					     Timestamp ts = new Timestamp(time);
					     
						 pstmt = conn.prepareStatement("UPDATE Ranking SET win = win+1,times=? WHERE id=?");
						 pstmt.setTimestamp(1, ts);
						 pstmt.setString(2, r[1]);
						 pstmt.executeUpdate();
						 System.out.println("데이터삽입성공");
						 pstmt = conn.prepareStatement("UPDATE Ranking SET lose = lose+1,times=? WHERE id=?");
						 String n[]=name.split("\n");
						 for(int i=1;i<n.length;i++) {
							 if(!n[i].equals(r[1])) {
								 pstmt.setTimestamp(1, ts);
								 pstmt.setString(2, n[i]);
							 }
						 }
						 pstmt.executeUpdate();
						 System.out.println("데이터삽입성공");
						 pstmt.close();
						 conn.close();
					 }catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					 }catch(SQLException e1) {
						 System.out.println("데이터삽입오류 : "+e1.getMessage());
					 }
					//System.out.println("!!1:"+r[0]+" 2:"+r[1]+" 3: "+r[2]+"4: "+r[3]+"5:"+r[4]);
					break;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}

	}
}

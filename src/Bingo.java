import java.net.*;
import java.sql.*;
import java.text.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.util.*;
import java.util.Date;
import javax.swing.Timer;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.text.*;
public class Bingo extends JFrame implements ActionListener{
	Socket socket;
	DataOutputStream out;
	DataInputStream in;
	Set set=new LinkedHashSet();//사용자
	Set set2=new LinkedHashSet();//컴퓨터
	Calendar StartTime;
	int combingo[][];
	JPanel numPanel;
	JPanel Panel1,Panel2,Panel3;
	JButton[][] numButtons;
	JLabel label;
	JLabel timelabel;
	JMenuBar mb=new JMenuBar();
	JMenu mFile=new JMenu("File");
	JMenu amount=new JMenu("amount");
	JMenuItem Restart=new JMenuItem("Restart");
	JMenu Size=new JMenu("Size");
	JMenuItem Exit=new JMenuItem("Exit");
	JMenuItem Ranking=new JMenuItem("Ranking");
	JMenuItem S_three=new JMenuItem("3x3");
	JMenuItem S_five=new JMenuItem("5x5");
	JMenuItem S_seven=new JMenuItem("7x7");
	JMenuItem Single=new JMenuItem("Single");
	JMenuItem Double=new JMenuItem("Double");
	JTextPane textPane=new JTextPane();
	JTextArea n_ta=new JTextArea("   접속한 사람");
	JTextField tf=new JTextField();
	Button go_bt=new Button("전송");
	JScrollPane scroll=new JScrollPane(textPane);
	JScrollPane n_scroll=new JScrollPane(n_ta);
    GridBagLayout grid = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
	String name="",sendstring="";
	boolean time=false,state=false;
	int Mode=0,size=0;
	Timer timer;
	BingoSender sender;
	BingoReceiver receiver;
	static int count = 0;
	Bingo(String name){
		super("Bingo");
		this.socket=socket;
		this.name=name;
		numPanel=new JPanel();
	    Panel1=new JPanel();
	    Panel1.setLayout(new BorderLayout());
	    Panel2=new JPanel();
	    Panel2.setLayout(new BorderLayout());
	    Panel3=new JPanel();
	    Panel3.setLayout(grid);

		label=new JLabel(" 사용자 "+name);
		timelabel=new JLabel("Timer :   0");
		timelabel.setFont(timelabel.getFont().deriveFont(30.0f));
		label.setFont(label.getFont().deriveFont(18.0f));
		
		mFile.add(Restart);
		mFile.addSeparator();
		mFile.add(Size);
		mFile.addSeparator();
		mFile.add(Exit);
		mFile.addSeparator();
		mFile.add(Ranking);
		Size.add(S_three);
		Size.add(S_five);
		Size.add(S_seven);
		amount.add(Single);
		amount.addSeparator();
		amount.add(Double);
		mb.add(mFile);
		mb.add(amount);
		
		Panel1.add("Center",tf);
		Panel1.add("East",go_bt);
		Panel2.add("Center",scroll);
		Panel2.add("South",Panel1);//채팅창
		
		gbc.fill = GridBagConstraints.BOTH;
		//여기도오론올오롱로오로올오로오로
		Panel3=Sidefunction2();

		add("North",mb);
		add("Center",Panel3);
		Restart.addActionListener(this);
		Exit.addActionListener(this);
		Ranking.addActionListener(this);
		S_three.addActionListener(this);
		S_five.addActionListener(this);
		S_seven.addActionListener(this);
		Single.addActionListener(this);
		Double.addActionListener(this);
		go_bt.addActionListener(this);
		textPane.setEditable(false);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setBounds(300,200,600,400);
		setVisible(true);
	}
    public void make(JComponent c, int x, int y, int w, int h) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        grid.setConstraints(c, gbc);
        // GridBagLayout의 GridBagConstraints의 set하는 방법
    }
    public JPanel Sidefunction() {
    	JPanel p=new JPanel();
    	p.setLayout(grid);
        gbc.weightx = 7.0;
        gbc.weighty = 1.0;
        make(numPanel, 0, 0, 2, 3);
        gbc.weightx = 1.0;
        gbc.weighty = 2.0;
        make(timelabel,2,0,0,1);
        gbc.weightx = 2.0;
        gbc.weighty = 1.0;
        make(n_scroll,2,1,1,1);
        make(Panel2,2,2,1,1);
        p.add(numPanel);
        p.add(timelabel);
        p.add(n_scroll);
        p.add(Panel2);
        return p;
    }
    public JPanel SidePanel() {
    	JPanel p=new JPanel();
    	p.setLayout(grid);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        make(timelabel,0,0,1,1);
        make(n_scroll,0,1,1,1);
        gbc.weightx = 1.0;
        gbc.weighty = 2.0;
        make(Panel2,0,2,1,2);
        p.add(timelabel);
        p.add(n_scroll);
        p.add(Panel2);
        return p;
    }
    public JPanel Sidefunction2() {
    	JPanel p=new JPanel();
    	p.setLayout(new BorderLayout());
//    	gbc.fill = GridBagConstraints.BOTH;
//    	p.setLayout(grid);
    	JPanel p1=new JPanel();
    	p1.setLayout(new GridLayout(3,0));
    	p1.setPreferredSize(new Dimension(150,400));
    	p1.add(timelabel);
    	p1.add(n_scroll);
    	p1.add(Panel2);
        p.add(numPanel,"Center");
        p.add(SidePanel(),"East");
        return p;
    }
	public void startsocket() {
		String serverIp = "127.0.0.1";
		try {
			socket=new Socket(serverIp,7777);
			JOptionPane.showMessageDialog(null, "서버에 연결되었습니다.","서버연결",JOptionPane.PLAIN_MESSAGE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sender=new BingoSender(this);
        receiver=new BingoReceiver(socket, this);
        receiver.start();
        sender.namesend();
	}
	public JPanel Pan(int num) {//판넬 위치 조정
		JPanel j=new JPanel();
	    JPanel jp=new JPanel();
	    j.setLayout(new BorderLayout());
	    jp.setLayout(grid);
	    numPanel=arrnum(num);
	    gbc.fill = GridBagConstraints.BOTH;
	    
	    //여ㅓ어어기ㅣㄱ이ㅣ잉!!!!!!!!!!!!~!!!!!!!
	    jp=Sidefunction2();
		j.add("North",mb);
		j.add("Center",jp);
	    return j;
	}
	public JPanel menu() {
		JPanel m=new JPanel();
		JPanel mp=new JPanel();
	    m.setLayout(new BorderLayout());
	    mp.setLayout(new BorderLayout());
	    timelabel.setText("Timer :   "+Integer.toString(0));
	    m.add("North",mb);
	    m.add("West",label);
	    m.add("East",timelabel);
	    mp.add("North",m);
	    return mp;
	}
	public JPanel arrnum(int num) {//선택한 사이즈만큼 버튼 만들어 판넬에 붙임
		size=num;
		JPanel numPanel=new JPanel();
		numPanel.setLayout(new GridLayout(num,num));
		numButtons=new JButton[num][num];
		set.clear();
		while(set.size()<(numButtons.length*numButtons.length)) {//랜덤으로 넣음
			set.add((int)(Math.random()*(num*num))+1+"");
		}
		Iterator it=set.iterator();
		int i=0;
		while(i<numButtons.length) {
			int j=0;
		    while(j<numButtons[i].length) {
		    	numButtons[i][j]=new JButton((String)it.next());
		        numButtons[i][j].addActionListener(this);
//		        numButtons[i][j].setBorderPainted(false);
		        numButtons[i][j].setContentAreaFilled(false);
//		        numButtons[i][j].setFocusPainted(false);
//		        numButtons[i][j].setOpaque(false);
		        numPanel.add(numButtons[i][j]);
		        j++;
		    }
		    i++;
		}
		return numPanel;
	}
	public void Combingo() {//컴퓨터빙고 생성
		combingo=new int[size][size];
		int i=0,j=0;
		while(set2.size()<(size*size)) {//랜덤으로 넣음
			set2.add((int)(Math.random()*(size*size))+1+"");
		}
		Iterator it=set2.iterator();
		i=0;
		while(i<combingo.length) {
			j=0;
			while(j<combingo[i].length) {
				combingo[i][j]=Integer.parseInt((String)it.next());
				System.out.print((combingo[i][j]<10?"  ":" ")+combingo[i][j]);
				j++;
			}
			System.out.println();
			i++;
		}
	}
	public int comnum(int c) {//컴퓨터 숫자 뽑기
		int i=0,j=0,num=0;
		 if(c==0) {
			 while(true) {
				 i=(int)(Math.random()*size);
				 j=(int)(Math.random()*size);
				 if(combingo[i][j]!=0) {
					 num=combingo[i][j];
					 break;
				 }
			 }
		 }else {
			 num=c;
		 }
		 return num;
	}
	public void CheckSingle() {
		 int i=0,j=0,a=0,b=0,com=0,per=0;
		 for(a=0;a<size;a++){
			 i=0;
			 j=0;
			 for(b=0;b<size;b++){
				 if(combingo[a][b]==0) {
					 i++;
				 }
				 if(numButtons[a][b].getBackground().getRed()==192){
					 j++;
				 }
			 }
			 if(i==size){
				 com++;
			 }
			 if(j==size){
				 per++;
			 }
			 i=0;
			 j=0;
			 for(b=0;b<size;b++){
				 if(combingo[b][a]==0) {
					 i++;
				 }
				 if(numButtons[b][a].getBackground().getRed()==192){
					 j++;
				 }
			 }
			 if(i==size){
				 com++;
			 }
			 if(j==size){
				 per++;
			 }
		 }
		 i=0;
		 j=0;
		 for(b=0;b<size;b++){
			 if(combingo[b][b]==0){
				 i++;
		     }
		     if(numButtons[b][b].getBackground().getRed()==192){
		    	 j++;
		     }
		}
	    if(i==size){
	    	com++;
	    }
	    if(j==size){
	    	per++;
	    }
	    i=0;
	    j=0;
	    a=size-1;
	    for(b=0;b<size;b++){
		     if(combingo[b][a]==0){
		    	 i++;
		     }
		     if(numButtons[b][a].getBackground().getRed()==192){
		    	 j++;
		     }
		     a--;
	    }
	    if(i==size){
	    	com++;
	    }
	    if(j==size){
	    	per++;
	    }
		if(com>=size-2) {
			 String ti=playTime();
			 Set_score("UPDATE Ranking SET lose = lose+1,times=? WHERE id=?",ti);
			JOptionPane.showMessageDialog(null, "컴퓨터 빙고!\n"+ti);
			timelabel.setText("Timer :   "+Integer.toString(0));
			state=true;
		}else if(per>=size-2) {
			 String ti=playTime();
			 Set_score("UPDATE Ranking SET win = win+1,times=? WHERE id=?",ti);
			JOptionPane.showMessageDialog(null, "사용자 빙고!\n"+ti);
			timelabel.setText("Timer :   "+Integer.toString(0));
			state=true;
		}
		if(state==false) {
			time=true;
			Time();
		}
	}
	public int smartcomnum() {
		 int i=0,j=0,number=1,com=0,c=0,per=0,r=0,r2=0;
		 char ch='■';
		 int a=0,b=0,num=0;
		 for(a=0;a<size;a++) {
			 i=0;j=0;
			 for(b=0;b<size;b++) {
				 if(combingo[a][b]==0) {
					 i++;
				 }
				 if(numButtons[a][b].getBackground().getRed()==192) {
					 j++;
				 }
			 }
			 if(i==size-1) {
				 r=1;
				 for(b=0;b<size;b++) {
					 if(combingo[a][b]!=0) {
						 c=combingo[a][b];
					 }
				 }
			 }
			 if(i==size-2&&r==0) {
				 r2=1;
				 for(b=0;b<size;b++) {
					 if(combingo[a][b]!=0) {
						 c=combingo[a][b];
					 }
				 }
			 }
			 if(i==size-3&&r2==0&&r==0) {
				 for(b=0;b<size;b++) {
					 if(combingo[a][b]!=0) {
						 c=combingo[a][b];
					 }
				 }
			 }
			 if(i==size) {
				 com++;
			 }
			 if(j==size) {
				 per++;
			 }
			 i=0;j=0;
			 for(b=0;b<size;b++) {
				 if(combingo[b][a]==0) {
					 i++;
				 }
				 if(numButtons[b][a].getBackground().getRed()==192) {
					 j++;
				 }
			 }
			 if(i==size-1) {
				 r=1;
				 for(b=0;b<size;b++) {
					 if(combingo[b][a]!=0) {
						 c=combingo[b][a];
					 }
				 }
			 }
			 if(i==size-2&&r==0) {
				 r2=1;
				 for(b=0;b<size;b++) {
					 if(combingo[b][a]!=0) {
						 c=combingo[b][a];
					 }
				 }
			 }
			 if(i==size-3&&r2==0&&r==0) {
				 for(b=0;b<size;b++) {
					 if(combingo[b][a]!=0) {
						 c=combingo[b][a];
					 }
				 }
			 }
			 if(i==size-4&&c==0) {
				 for(b=0;b<size;b++) {
					 if(combingo[b][a]!=0) {
						 c=combingo[b][a];
					 }
				 }
			 }
			 if(i==size) {
				 com++;
			 }
			 if(j==size) {
				 per++;
			 }
		 }
		 i=0;j=0;
		 for(b=0;b<size;b++) {
			 if(combingo[b][b]==0) {
				 i++;
			 }
			 if(numButtons[b][b].getBackground().getRed()==192) {
				 j++;
			 }
		 }
		 if(i==size-1) {
			 r=1;
			 for(b=0;b<size;b++) {
				 if(combingo[b][b]!=0) {
					 c=combingo[b][b];
				 }
			 }
		 }
		 if(i==size-2&&r==0) {
			 r2=1;
			 for(b=0;b<size;b++) {
				 if(combingo[b][b]!=0) {
					 c=combingo[b][b];
				 }
			 }
		 }
		 if(i==size-3&&r2==0&&r==0) {
			 for(b=0;b<size;b++) {
				 if(combingo[b][b]!=0) {
					 c=combingo[b][b];
				 }
			 }
		 }
		 if(i==size) {
			 com++;
		 }
		 if(j==size) {
			 per++;
		 }
		 i=0;j=0;a=size-1;
		 for(b=0;b<size;b++) {
			 if(combingo[b][a]==0) {
				 i++;
			 }
			 if(numButtons[b][a].getBackground().getRed()==192) {
				 j++;
			 }
			 a--;
		 }
		 if(i==size-1) {
			 r=1;
			 a=size-1;
			 for(b=0;b<size;b++) {
				 if(combingo[b][a]!=0) {
					 c=combingo[b][a];
				 }
				 a--;
			 }
		 }
		 if(i==size-2&&r==0) {
			 r2=1;
			 a=size-1;
			 for(b=0;b<size;b++) {
				 if(combingo[b][a]!=0) {
					 c=combingo[b][a];
				 }
				 a--;
			 }
		 }
		 if(i==size-3&&r2==0&&r==0) {
			 a=size-1;
			 for(b=0;b<size;b++) {
				 if(combingo[b][a]!=0) {
					 c=combingo[b][a];
				 }
				 a--;
			 }
		 }
		 if(i==size) {
			 com++;
		 }
		 if(j==size) {
			 per++;
		 }
		 if(per>=size-2) {
			 String ti=playTime();
			 Set_score("UPDATE Ranking SET win = win+1,times=? WHERE id=?",ti);
			 JOptionPane.showMessageDialog(null, "사용자 빙고!\n"+ti);
			 timelabel.setText("Timer :   "+Integer.toString(0));
			 state=true;
		 }else if(com>=size-2) {
			 String ti=playTime();
			 Set_score("UPDATE Ranking SET lose = lose+1,times=? WHERE id=?",ti);
			 JOptionPane.showMessageDialog(null, "컴퓨터 빙고!\n"+ti);
			 timelabel.setText("Timer :   "+Integer.toString(0));
			 state=true;
		 }
		 return c;
	}
	public void Set_score(String s,String ti) {
		 Connection conn=null;
		 MariaDB mdb=new MariaDB();
		 conn=mdb.connectDB();
		 PreparedStatement pstmt=null;
		 try {
			 DateFormat df = new SimpleDateFormat("HH:mm:ss");
			 Date date = null;
			 date = df.parse(ti);
			 long time = date.getTime();
			 Timestamp ts = new Timestamp(time);
			 pstmt = conn.prepareStatement(s);
			 pstmt.setTimestamp(1, ts);
			 pstmt.setString(2, name);
			 pstmt.executeUpdate();
			 pstmt.close();
			 conn.close();
		 }catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		 }catch(SQLException e1) {
			 System.out.println("데이터삽입오류 : "+e1.getMessage());
		 }
	}
	public void ModeSingle() {//1인용 컴터랑 대전
		 int i=0,j=0,c=0,a=0,b=0,num=0;
		 char ch='■';
		 for(int q=0;q<size;q++){//컴터 빙고판 출력
			 for(int p=0;p<size;p++){
		          if(combingo[q][p]==0){
		        	  System.out.printf("%-3c",ch);
		          }
		          else{
		              System.out.printf("%-3d",combingo[q][p]);
		          }
			 }
			 System.out.println();
	     }
		 System.out.println();
		 c=smartcomnum();//컴터 숫자 정함과 동시에 빙고인지 체크
		 if(state==false) {
			 System.out.print("컴퓨터 숫자 : ");
			 num=comnum(c);//컴터 숫자 정하고
			 System.out.println(num);
			 for(a=0;a<size;a++){//컴터 숫자인거 0이랑 그레이로 바꾸고
				 for(b=0;b<size;b++){
					 if(combingo[a][b]==num){
						 combingo[a][b]=0;
					 }
					 if(numButtons[a][b].getText().equals(Integer.toString(num))) {
						 numButtons[a][b].setContentAreaFilled(true);
						 numButtons[a][b].setBackground(Color.lightGray);
					 }
				 }
			 }
			 for(int q=0;q<size;q++){//컴터 빙고판 출력
				 for(int p=0;p<size;p++){
			          if(combingo[q][p]==0){
			        	  System.out.printf("%-3c",ch);
			          }
			          else{
			              System.out.printf("%-3d",combingo[q][p]);
			          }
				 }
				 System.out.println();
		     }
			 System.out.println();
			 CheckSingle();//컴터 빙고판 빙고인지 체크
		 }

	}
	
	public int ModeDouble() {//2인용일 때 빙고 체크
	    int a=0,b=0,num=0,i=0,j=0,per=0;
	    for(a=0;a<size;a++) {
	    	i=0;
	        for(b=0;b<size;b++) {
	        	if(numButtons[a][b].getBackground().getRed()==192) {
	        		i++;
	            }
	        }
	        if(i==size) {
	        	per++;
	        }
	        i=0;
	        for(b=0;b<size;b++) {
	        	if(numButtons[b][a].getBackground().getRed()==192) {
	        		i++;
	            }
	        }
	        if(i==size) {
	        	per++;
	        }
	     }
	     i=0;
	     for(b=0;b<size;b++) {
	        if(numButtons[b][b].getBackground().getRed()==192) {
	           i++;
	        }
	     }
	     if(i==size) {
	        per++;
	     }
	     i=0;
	     a=size-1;
	     for(b=0;b<size;b++) {
	        if(numButtons[b][a].getBackground().getRed()==192) {
	           i++;
	        }
	        a--;
	     }
	     if(i==size) {
	        per++;
	     }
	     return per;
	}
	public String playTime() {//play 시간 측정
		String t="";
		Calendar endTime=Calendar.getInstance();
		long difference=(endTime.getTimeInMillis()-StartTime.getTimeInMillis())/1000;
		t=difference/3600+":";
		difference%=3600;
		t+=difference/60+":";
		difference%=60;
		t+=difference;
		return t;
//		timelabel.setText("play time - "+t);
	}
	public void setstarttime() {//대전할 때 시작 시간 입력
		StartTime=Calendar.getInstance();
	}
	public void Time() {//타이머
		count=10;
		timer=new Timer(1000,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(count==10) {
					timelabel.setText("Timer : "+Integer.toString(count));
				}else {
					timelabel.setText("Timer :   "+Integer.toString(count));
				}
				count--;
				if(count<0) {
					timer.stop();
					timelabel.setText("Timer :   "+Integer.toString(0));
					 while(true) {
						 int a=(int)(Math.random()*size);
						 int b=(int)(Math.random()*size);
						 if(numButtons[a][b].getBackground().getRed()==238) {
							 numButtons[a][b].doClick();
							 break;
						 }
					}
				}
			}
		});
		timer.start();		
	}
	public void Mode2() {
		 if(ModeDouble()>=size-2){
			 sendstring="빙고 "+playTime();
			 sender.send();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String s= e.getActionCommand();
		if(s.equals("3x3")){
            getContentPane().removeAll();
            getContentPane().add(Pan(3));
            revalidate();
            repaint();
            S_three.setBackground(Color.lightGray);
            S_five.setBackground(new Color(238,238,238));
            S_seven.setBackground(new Color(238,238,238));
		}else if(s.equals("5x5")){
            getContentPane().removeAll();
            getContentPane().add(Pan(5));
            revalidate();
            repaint();
            S_three.setBackground(new Color(238,238,238));
            S_five.setBackground(Color.lightGray);
            S_seven.setBackground(new Color(238,238,238));
		}else if(s.equals("7x7")){
            getContentPane().removeAll();
            getContentPane().add(Pan(7));
            revalidate();
            repaint();
            S_three.setBackground(new Color(238,238,238));
            S_five.setBackground(new Color(238,238,238));
            S_seven.setBackground(Color.lightGray);
		}else if(s.equals("Restart")){
			JOptionPane.showMessageDialog(null, "게임을 다시 시작합니다.","초기화",JOptionPane.PLAIN_MESSAGE);
			if(Mode==2) {
				sendstring="restart";
				sender.send();
				sender=null;
				receiver=null;
				try {
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else if(Mode==1) {
				timer.stop();
			}
			n_ta.setText("   접속한 사람");
			tf.setText("");
			textPane.setText("");
        	time=false;
        	state=false;
        	Mode=0;
        	size=0;
        	StartTime=null;
            amount.setEnabled(true);
            Size.setEnabled(true);
            S_three.setBackground(new Color(238,238,238));
            S_five.setBackground(new Color(238,238,238));
            S_seven.setBackground(new Color(238,238,238));
            JPanel p=new JPanel();
            p.setLayout(new BorderLayout());
    		p.add("North",mb);
    		//Panel3=Sidefunction2();
    		//p.add("Center",Sidefunction2());
            getContentPane().removeAll();
            getContentPane().add(p);
            revalidate();
            repaint();
        }else if(s.equals("Single")){
        	if(size>0) {
                Mode=1;
                amount.setEnabled(false);
                Size.setEnabled(false);
            	Combingo();
            	time=true;
        		StartTime=Calendar.getInstance();
        		Time();
        	}else {
        		JOptionPane.showMessageDialog(null, "size부터 선택해주세요","접근거부",JOptionPane.PLAIN_MESSAGE);
        	}
        }else if(s.equals("Double")){
        	if(size>0) {
        		startsocket();
            	Mode=2;
                amount.setEnabled(false);
                Size.setEnabled(false);
        	}else {
        		JOptionPane.showMessageDialog(null, "size부터 선택해주세요","접근거부",JOptionPane.PLAIN_MESSAGE);
        	}
        }else if(s.equals("Exit")){
        	JOptionPane.showMessageDialog(null, "게임을 종료합니다.","종료",JOptionPane.PLAIN_MESSAGE);
        	dispose();
        }else if(s.equals("전송")) {
        	if(!tf.getText().equals("")) {
        		sendstring="["+name+"]"+tf.getText();
        		sender.send();
        	}
        }else if(s.equals("Ranking")) {
        	new Ranking();
        }else {
        	 if(size>0&&Mode>0) {
	        	 if(time==true) {
	        		 JButton button=(JButton)e.getSource();
			         if(button.getBackground().getRed()==238) {
			        	 button.setContentAreaFilled(true);
			        	 button.setBackground(Color.lightGray);
			        	 time=false;
			        	 timelabel.setText("Timer :   0");
			        	 timer.stop();
			        	 if(Mode==1) { 
			        		 int num=Integer.parseInt(button.getText());
			        		 for(int i=0;i<size;i++) {
			        			 for(int j=0;j<size;j++) {
			        				 if(combingo[i][j]==num) {
			        					 combingo[i][j]=0;
			        				 }
			        			 }
			        		 }
			        		 if(state==false) {
			        			 ModeSingle();
			        		 }
			        	 }else {//Mode가 2라면
			        		 if(ModeDouble()>=size-2){
			        			 sendstring="빙고 "+playTime();
			        		}else {
			        			sendstring="next "+button.getText();
			        		}
			        		sender.send();
			            }
			        }else {
			        	JOptionPane.showMessageDialog(null, "이미 눌렀던 버튼입니다.");
			        }
			    }else {
			    	JOptionPane.showMessageDialog(null, "상대방이 버튼을 누르지 않았습니다.");
		        }
        	 }else {
        		 JOptionPane.showMessageDialog(null, "size 또는 amount를 선택해주세요.");
        	 }
         }
	}
}

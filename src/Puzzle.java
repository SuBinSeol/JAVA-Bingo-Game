import java.net.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class Puzzle extends JFrame implements ActionListener{
	Set set=new LinkedHashSet();//사용자
	Calendar StartTime;
	JPanel numPanel;
	JPanel Panel1;
	JButton[][] numButtons;
	JLabel label;
	JLabel timelabel;
	JMenuBar mb=new JMenuBar();
	JMenu mFile=new JMenu("File");
	JMenuItem Restart=new JMenuItem("Restart");
	JMenu Size=new JMenu("Size");
	JMenuItem Exit=new JMenuItem("Exit");
	JMenuItem S_three=new JMenuItem("3x3");
	JMenuItem S_five=new JMenuItem("5x5");
	JMenuItem S_seven=new JMenuItem("7x7");
	String name="",send="";
	boolean time=false;
	int size=0;
	Puzzle(String name) {
		super("Puzzle");
		this.name=name;
	    Panel1=new JPanel();
	    Panel1.setLayout(new BorderLayout());
		label=new JLabel(" 사용자 "+name);
		timelabel=new JLabel();
		timelabel.setFont(timelabel.getFont().deriveFont(15.0f));
		label.setFont(label.getFont().deriveFont(15.0f));
		mFile.add(Restart);
		mFile.addSeparator();
		mFile.add(Size);
		mFile.addSeparator();
		mFile.add(Exit);
		Size.add(S_three);
		Size.add(S_five);
		Size.add(S_seven);
		mb.add(mFile);
		Panel1.add("North",mb);
		Panel1.add("West",label);
		Panel1.add("East",timelabel);
		add("North",Panel1);
		
		Restart.addActionListener(this);
		Exit.addActionListener(this);
		S_three.addActionListener(this);
		S_five.addActionListener(this);
		S_seven.addActionListener(this);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		setBounds(300,200,600,400);
		setVisible(true);
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
            Size.setEnabled(false);
            StartTime=Calendar.getInstance();
		}else if(s.equals("5x5")){
            getContentPane().removeAll();
            getContentPane().add(Pan(5));
            revalidate();
            repaint();
            S_five.setBackground(Color.lightGray);
            Size.setEnabled(false);
            StartTime=Calendar.getInstance();
		}else if(s.equals("7x7")){
            getContentPane().removeAll();
            getContentPane().add(Pan(7));
            revalidate();
            repaint();
            S_seven.setBackground(Color.lightGray);
            Size.setEnabled(false);
            StartTime=Calendar.getInstance();
		}else if(s.equals("Restart")){
			JOptionPane.showMessageDialog(null, "게임을 다시 시작합니다.","초기화",JOptionPane.PLAIN_MESSAGE);
        	size=0;
        	StartTime=null;
            Size.setEnabled(true);
            S_three.setBackground(new Color(238,238,238));
            S_five.setBackground(new Color(238,238,238));
            S_seven.setBackground(new Color(238,238,238));
            getContentPane().removeAll();
            getContentPane().add(menu());
            revalidate();
            repaint();
        }else if(s.equals("Exit")){
        	JOptionPane.showMessageDialog(null, "게임을 종료합니다.","종료",JOptionPane.PLAIN_MESSAGE);
        	dispose();
        }else {
        	 if(size>0) {
        		 JButton button=(JButton)e.getSource();
        		 int n=PuzzleGame(button.getText());
        	 }else {
        		 JOptionPane.showMessageDialog(null, "size를 선택해주세요.");
        	 }
         }
	}
	public JPanel Pan(int num) {//판넬 위치 조정
		JPanel j=new JPanel();
	    JPanel jp=new JPanel();
	    j.setLayout(new BorderLayout());
	    jp.setLayout(new BorderLayout());
	    j.add("North",mb);
	    j.add("West",label);
	    j.add("East",timelabel);
	    jp.add("North",j);
	    jp.add("Center",arrnum(num));
	    return jp;
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
		    	String n=(String)it.next();
		    	if(n.equals(Integer.toString(size*size))) {
		    		numButtons[i][j]=new JButton("0");
		    		numButtons[i][j].setContentAreaFilled(true);
		    		numButtons[i][j].setBackground(Color.lightGray);
		    	}else {
		    		numButtons[i][j]=new JButton(n);
//			        numButtons[i][j].setBorderPainted(false);
			        numButtons[i][j].setContentAreaFilled(false);
//			        numButtons[i][j].setFocusPainted(false);
//			        numButtons[i][j].setOpaque(false);
		    	}
//		    	System.out.println("s="+numButtons[i][j].getBackground());
		        numButtons[i][j].addActionListener(this);
		        numPanel.add(numButtons[i][j]);
		        j++;
		    }
		    i++;
		}
		return numPanel;
	}
	public JPanel menu() {
		JPanel m=new JPanel();
		JPanel mp=new JPanel();
	    m.setLayout(new BorderLayout());
	    mp.setLayout(new BorderLayout());
	    timelabel.setText(Integer.toString(0));
	    m.add("North",mb);
	    m.add("West",label);
	    m.add("East",timelabel);
	    mp.add("North",m);
	    return mp;
	}
	public String Startnum() {
		String n="";
		for(int i=0;i<size;i++) {
			for(int j=0;j<size;j++) {
				if(numButtons[i][j].getText().equals("0")) {
					numButtons[i][j].setContentAreaFilled(true);
					numButtons[i][j].setBackground(Color.lightGray);
					n=i+" "+j;
					break;
				}
			}
		}
		return n;
	}
	public int PuzzleGame(String num) {
	    int a=0,b=0,c=0,i=0,j=0,number=0;
	    String n[]=Startnum().split(" ");
	    i=Integer.parseInt(n[0]);
	    j=Integer.parseInt(n[1]);
	   	Loop:for(a=0;a<size;a++) {
	    	for(b=0;b<size;b++) {
		    	if(numButtons[a][b].getText().equals(num)) {
		    		break Loop;
		    	}
	    	}
		}
		if(i==a||j==b) {
	    	if(i+1==a||i-1==a||j+1==b||j-1==b) {
	    		numButtons[a][b].setText(numButtons[i][j].getText());
	    		numButtons[a][b].setContentAreaFilled(true);
	    		numButtons[a][b].setBackground(Color.lightGray);
	    		numButtons[i][j].setBackground(new Color(238,238,238));
	    		numButtons[i][j].setContentAreaFilled(false);
	    		numButtons[i][j].setText(num);
		    	i=a;
		    	j=b;
		   }
		}else {
			JOptionPane.showMessageDialog(null, "상하좌우 버튼만 누를 수 있습니다.");
		}
		number=1;
    	c=0;
		for(int q=0;q<size;q++){
	    	for(int p=0;p<size;p++){
		    	if(numButtons[q][p].getText().equals(Integer.toString(number))) {
			    	c++;
			    	if(c==((size*size)-1)) {
			    		JOptionPane.showMessageDialog(null, "게임종료\n"+playTime());
				    	return 1;
			    	}
			   }
			   number++;
	    	}
		}
		return 0;
	}

	public String playTime() {
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
}

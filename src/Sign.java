import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.*;

public class Sign extends JFrame implements ActionListener{
	JPanel Panel=new JPanel();
	JPanel Panel1=new JPanel();
	JLabel l=new JLabel("Sign up",JLabel.CENTER);
	JLabel l_id=new JLabel("id");
	JLabel l_pw=new JLabel("pw");
	JLabel l_name=new JLabel("name");
	JTextField id=new JTextField();
	JPasswordField pw = new JPasswordField();
	JTextField name=new JTextField();
	JButton id_check=new JButton("중복 검사");
	JButton sign=new JButton("Sign Up");
	Connection conn=null;
	GridBagLayout grid = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
	int check=0;
	public Sign(){
		super("Sign up");
		pw.setEchoChar('*');
		Panel.setLayout(grid);
		Panel.setBorder(new TitledBorder(new LineBorder(Color.black,1)));
		l.setFont(new Font("Serif",Font.BOLD,20));
		l.setFont(l.getFont().deriveFont(30.0f));
		l_id.setFont(new Font("Serif",Font.BOLD,15));
		l_id.setFont(l_id.getFont().deriveFont(20.0f));
		l_pw.setFont(new Font("Serif",Font.BOLD,15));
		l_pw.setFont(l_pw.getFont().deriveFont(20.0f));
		l_name.setFont(new Font("Serif",Font.BOLD,15));
		l_name.setFont(l_name.getFont().deriveFont(20.0f));
		
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        make(l_id, 0, 0, 1, 1); // 아래의 make함수를 지정
        make(l_pw, 0, 1, 1, 1); 
        make(l_name, 0, 2, 1, 1); 
        make(id_check, 3, 0, 1, 1);
        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        make(id, 1, 0, 2, 1);
        make(pw, 1, 1, 3, 1);
        make(name, 1, 2, 3, 1);
        Panel.add(l_id);
        Panel.add(l_pw);
        Panel.add(l_name);
        Panel.add(id_check);
        Panel.add(id);
        Panel.add(pw);
        Panel.add(name);
        add(l,"North");
        add(Panel,"Center");
        add(sign,"South");
        
		//디자인 1
//		Panel1.setLayout(new BorderLayout());
//		Panel1.add(id,"Center");
//		Panel1.add(id_check,"East");
//		l.setFont(new Font("Serif",Font.BOLD,20));
//		l.setFont(l.getFont().deriveFont(30.0f));
//		l_id.setFont(new Font("Serif",Font.BOLD,15));
//		l_id.setFont(l_id.getFont().deriveFont(20.0f));
//		l_pw.setFont(new Font("Serif",Font.BOLD,15));
//		l_pw.setFont(l_pw.getFont().deriveFont(20.0f));
//		l_name.setFont(new Font("Serif",Font.BOLD,15));
//		l_name.setFont(l_name.getFont().deriveFont(20.0f));
//		Panel.setLayout(new GridBagLayout());
//		Panel.setBorder(new TitledBorder(new LineBorder(Color.black,1)));
//		GridBagConstraints gbc=new GridBagConstraints();
//		gbc.fill=GridBagConstraints.BOTH;
//        gbc.weightx=0.1;
//        gbc.gridx=0;  
//        gbc.gridy=0;
//        Panel.add(l_id,gbc);
//        gbc.gridx=0;  
//        gbc.gridy=1;
//        Panel.add(l_pw,gbc);
//        gbc.gridx=0;  
//        gbc.gridy=2;
//        Panel.add(l_name,gbc);
//        gbc.weightx=0.2;
//        gbc.gridx=1;  
//        gbc.gridy=0;
//        Panel.add(Panel1,gbc);
//        gbc.gridx=1;  
//        gbc.gridy=1;
//        Panel.add(pw,gbc);
//        gbc.gridx=1;  
//        gbc.gridy=2;
//        Panel.add(name,gbc);
//
//        add(l,"North");
//		add(Panel,"Center");
//		add(sign,"South");

		id_check.addActionListener(this);
		sign.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setBounds(350,150,400,200);
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
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String s= e.getActionCommand();
		MariaDB mdb=new MariaDB();
		int count=0,i=0;
		if(s.equals("중복 검사")) {
			String sign_id=id.getText();
			conn=mdb.connectDB();
			String count_sql = "SELECT COUNT(*) FROM information";
			String sql = "SELECT id FROM information";
			Statement st = null;
			ResultSet rs = null;
			try {
				st = conn.createStatement();
				rs = st.executeQuery(count_sql);
				while(rs.next()) {
					count=rs.getInt(1);
				}
				rs = st.executeQuery(sql);
				while(rs.next()) {
					String userId  = rs.getString(1);
					if(sign_id.equals(userId)||sign_id.equals("")) {
						JOptionPane.showMessageDialog(null, "이미 존재하는 아이디입니다.","아이디 중복",JOptionPane.PLAIN_MESSAGE);
						break;
					}
					i++;
				}
				if(count==i) {
					JOptionPane.showMessageDialog(null, "사용가능한 아이디입니다.","확인 완료",JOptionPane.PLAIN_MESSAGE);
					check=1;
				}
			}catch(SQLException e1) {
				System.out.println("데이터삽입오류 : "+e1.getMessage());
			}
		}else {
			conn=mdb.connectDB();
			String pws="";
			char[] secret_pw = pw.getPassword();
			for(char cha : secret_pw){ 
				Character.toString(cha);
				pws += (pws.equals("")) ? ""+cha+"" : ""+cha+"";
			}
			if(id.getText().equals("")||pws.equals("")||name.getText().equals("")||check==0) {
				JOptionPane.showMessageDialog(null, "빈칸을 채워주세요.","error",JOptionPane.PLAIN_MESSAGE);
			}else {
				PreparedStatement pstmt=null;
				try {
					pstmt = conn.prepareStatement("insert into information values(?,?,?)");
					pstmt.setString(1, id.getText());
					pstmt.setString(2, pws);
					pstmt.setString(3, name.getText());
					pstmt.executeUpdate();

				    DateFormat df = new SimpleDateFormat("HH:mm:ss");
				    Date date = null;
				    date = df.parse("00:00:00");
				    long time = date.getTime();

				    Timestamp ts = new Timestamp(time);
				    
					pstmt = conn.prepareStatement("insert into Ranking (id,times) values(?,?)");
					pstmt.setString(1, id.getText());
					pstmt.setTimestamp(2, ts);
					pstmt.executeUpdate();
					pstmt.close();
					conn.close();
					JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.","회원가입",JOptionPane.PLAIN_MESSAGE);
					check=0;
					dispose();
				}catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}catch(SQLException e1) {
					System.out.println("데이터삽입오류 : "+e1.getMessage());
				}
			}

		}
	}
}

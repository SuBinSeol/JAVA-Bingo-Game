import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;

public class Login extends JFrame implements ActionListener{
	JLabel l=new JLabel("Login",JLabel.CENTER);
	JLabel l_id=new JLabel("id");
	JLabel l_pw=new JLabel("pw");
	JTextField id=new JTextField();
	JPasswordField pw=new JPasswordField();
	JButton login=new JButton("Login");
	JButton sign=new JButton("Sign Up");
	JPanel Panel;
	Connection conn=null;
	GridBagLayout grid = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
	public Login(){
		super("Login");
		setLayout(new BorderLayout());
		Panel=new JPanel();		
		Panel.setLayout(grid);
		
		pw.setEchoChar('*');
		
		l.setFont(new Font("Serif",Font.BOLD,20));
		l.setFont(l.getFont().deriveFont(30.0f));
		l_id.setFont(new Font("Serif",Font.BOLD,10));
		l_id.setFont(l_id.getFont().deriveFont(20.0f));
		l_pw.setFont(new Font("Serif",Font.BOLD,10));
		l_pw.setFont(l_pw.getFont().deriveFont(20.0f));

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        make(l_id, 0, 0, 1, 1); // 아래의 make함수를 지정
        make(l_pw, 0, 1, 1, 1); 
        make(login, 3, 0, 1, 2);
        make(sign, 3, 2, 1, 1);

        gbc.weightx = 9.0;
        gbc.weighty = 1.0;
        make(id, 1, 0, 2, 1);
        make(pw, 1, 1, 2, 1);
        
        Panel.add(l_id);
        Panel.add(l_pw);
        Panel.add(id);
        Panel.add(pw);
        Panel.add(login);
        Panel.add(sign);
        add(l,"North");
        add(Panel,"Center");
        
		login.addActionListener(this);
		sign.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setBounds(300,200,450,200);
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
	public void actionPerformed(ActionEvent e) {
		String s= e.getActionCommand();
		MariaDB mdb=new MariaDB();
		String pws="";
		char[] secret_pw = pw.getPassword();
		for(char cha : secret_pw){ 
			Character.toString(cha);
			pws += (pws.equals("")) ? ""+cha+"" : ""+cha+"";
		}
		if(s.equals("Login")) {
			if(id.getText().equals("")||pws.equals("")) {
				JOptionPane.showMessageDialog(null, "빈칸을 채워주세요.","빈칸 오류",JOptionPane.PLAIN_MESSAGE);
			}else {
				int count=0,i=0;;
				conn=mdb.connectDB();
				String sql = "SELECT * FROM information";
				Statement st = null;
				ResultSet rs = null;
				try {
					st = conn.createStatement();
					rs = st.executeQuery("SELECT COUNT(*) FROM information");
					while(rs.next()) {
						count=rs.getInt(1);
					}
					rs = st.executeQuery(sql);
					while(rs.next()) {
						String userId=rs.getString(1);
						String userpw=rs.getString(2);
						if(id.getText().equals(userId)&&pws.equals(userpw)) {
							JOptionPane.showMessageDialog(null, "환영합니다. "+userId+"님!","로그인",JOptionPane.PLAIN_MESSAGE);
							new Bingo(userId);
							dispose();
							break;
						}
						i++;
						if(count==i) {
							JOptionPane.showMessageDialog(null, "아이디와 비밀번호가 일치하지 않습니다.","error",JOptionPane.PLAIN_MESSAGE);
							id.setText("");
							pw.setText("");
						}
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}else {
			new Sign();
			dispose();
		}
	}
}

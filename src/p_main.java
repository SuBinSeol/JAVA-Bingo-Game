import javax.swing.JOptionPane;

public class p_main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String name = JOptionPane.showInputDialog("닉네임을 입력하세요");
		new Puzzle(name);
	}

}

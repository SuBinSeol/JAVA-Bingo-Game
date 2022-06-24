import java.sql.*;

public class DBtest {
	private static final String DB_DRIVER_CLASS="org.mariadb.jdbc.Driver";
	private static final String DB_URL="jdbc:mariadb://127.0.0.1:3306/bingo";
	private static final String DB_USERNAME="root";
	private static final String DB_PASSWORD="1234";
	private static Connection conn;
	private static void connectDB() {
		try {
			Class.forName(DB_DRIVER_CLASS);
			conn=DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			System.out.println("연결 성공");
			String sql = "SELECT * FROM information";
			Statement st = null;
			ResultSet rs = null;
			st = conn.createStatement();
			rs = st.executeQuery(sql);
            while(rs.next()) {
                
                // 레코드의 컬럼은 0이 아닌 1부터 시작.
                String userId  = rs.getString(1);
                String userPw  = rs.getString(2);
                
                System.out.println(userId + " " + userPw);
                
                // 레코드의 컬럼명으로도 가져올 수 있습니다.
//                String userId2  = rs.getString("USER_ID");
//                String userPw2  = rs.getString("USER_PW");
//                String userTel2 = rs.getString("USER_TEL");
                
//               System.out.println(userId2 + " " + userPw2 + " " + userTel2);
            }
		}catch(ClassNotFoundException e) {
			System.out.println("드라이브 로딩 실패");
		}catch(SQLException e) {
			System.out.println("DB 연결 실패");
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DBtest test=new DBtest();
		test.connectDB();
	}

}

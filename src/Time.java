import java.util.Timer;
import java.util.TimerTask;
public class Time {
	static int count = 0;
	public Time() {
		Timer timer=new Timer();
		TimerTask task=new TimerTask(){
		    @Override
		    public void run() {
		    //TODO Auto-generated method stub
				if(count < 10){ //count���� 5���� �۰ų� ���������� ����
					System.out.println("[ī��Ʈ�ٿ� : "+count+"]");
					count++; //����Ƚ�� ���� 
				}
				else{
					timer.cancel(); //Ÿ�̸� ����
					System.out.println("[ī��Ʈ�ٿ� : ����]");
				}
		    }	
		};
		timer.schedule(task, 1000, 1000); //���� Task, 1�ʵ� ����, 1�ʸ��� �ݺ�	
	}
}

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
				if(count < 10){ //count값이 5보다 작거나 같을때까지 수행
					System.out.println("[카운트다운 : "+count+"]");
					count++; //실행횟수 증가 
				}
				else{
					timer.cancel(); //타이머 종료
					System.out.println("[카운트다운 : 종료]");
				}
		    }	
		};
		timer.schedule(task, 1000, 1000); //실행 Task, 1초뒤 실행, 1초마다 반복	
	}
}

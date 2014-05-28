package game;

import java.util.*;
import java.io.*;
import chat.Server;

public class Game {
	HashMap<String, DataOutputStream> clients;
	HashMap<String, DataInputStream> clients_in;
	HashMap<String, String> clients_job;
	Iterator<String> it;
	int num, maf, pol, doc;	// 총, 마피아, 경찰, 의사
	public int Num;	
	boolean day = true; // 낮 밤을 결정하는 상수
	boolean chat = true; // 클라이언트 채팅 가능 여부
	public Main main; // Game클래스의 내부클래스 Main에 접근.
	public Server server; // Server클래스에 접근

	public Game() {
		System.out.println("Game 생성자");
	}

	public void Set(Server server) {
		try {
			// 서버의 정보를 받아온다.
			this.server = server;
			this.clients = server.clients;
			this.clients_in = server.clients_in;	// 필요성 검증 해야함.
			this.clients_job = server.clients_job;
			
			num = clients.size();
			if (clients.isEmpty()) {
				System.out.println("클라이언트가 비어있습니다.");
			} else {
				maf = num / 3;
				// it = clients.keySet().iterator();
				Random random = new Random();
				int val;
				// while (it.hasNext()) {
				// clients_job.put(it.next(), "Citizen");
				// System.out.println("모두 시민으로"); // 테스트 출력
				// }

				System.out.println("---------마피아 설정---------");
				if (num > 5) {
					while (maf > 1) {
						System.out.println("첫번째 while 진입"); // 테스트 출력
						it = clients.keySet().iterator();
						while (it.hasNext()) {
							System.out.println("두번째 while 진입"); // 테스트 출력
							val = random.nextInt(num);
							System.out.println(val); // 테스트 출력
							if (val == 1) {
								if (maf < 1) {
									System.out.println("마피아 꽉참"); // 테스트 출력
									break;
								}
								maf--;
								clients_job.put(it.next(), "Mafia");
								System.out.println("마피아 한명 뽑힘"); // 테스트 출력
							} else {
								clients_job.put(it.next(), "Citizen");
								System.out.println("그냥 시민으로"); // 테스트 출력
							}
						}
					}
					System.out.println("총인원: " + num + "명");
					System.out.println("마피아 수: " + num + "/3=" + maf);

				} else {
					System.out.println("인원이 너무 적습니다.(6명 이상)");
				}
				System.out.println("----------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Status() {
		it = clients_job.keySet().iterator();
		System.out.println("스테이터스시작" + clients_job.keySet());
		while (it.hasNext()) {
			String temp = it.next();
			System.out.println("스테이터스반복문진입");
			System.out.println("이름: " + temp);
			System.out.println("직업: " + clients_job.get(temp));
		}
	}

	public void Start() {
		server.gameon = true;
		System.out.println("마피아를 시작합니다!");
		main = new Main();
		main.start();
	}
	
	public void End() {
		server.gameon = false;
		System.out.println("게임이 끝났습니다.");
	}
	
	public class Main extends Thread {
		boolean Win(){
			boolean win=true;	// true:시민이김, false:마피아이김
			int maf_num=0, citi_num=0;
			Iterator<String> it = clients_job.keySet().iterator();
			while(it.hasNext()){
				String tmpid = it.next();
				switch(tmpid){
				case "Mafia":
					maf_num++;
					break;
				case "Citizen":
					citi_num++;
					break;
				}
				
			}
			if(maf_num>=citi_num){
				System.out.println("마피아가 이겼습니다.");
				win = false;
			}
			else if(maf_num==0){
				System.out.println("시민이 이겼습니다.");
				win = true;
			}
			return win;
		}
		
		public void TimeOut(int sec) {	// 시간제한을 준다. 시간이 지나면 대화 막음.
			System.out.println(sec+"초 후 지목을 시작합니다.");
			server.sender.chat = false;	// ServerSender클래스의 chat을 false로. sendToAll함수 작동막음.
			
		}
		
		public void PointOut() {	// 지목 
			System.out.println("지목해주세요.");
		}
		
		public void LiveOrDead(String id) {	// 투표
			System.out.println("투표해주세요.");
		}
		
		public void run() {
			while (day) {	// 낮
				if(Win()) break;	// 승부가 났는지 검사
				System.out.println("낮이 되었습니다.");
				TimeOut(180);

				day = false;
				if (!day) {	// 밤
					System.out.println("밤이 되었습니다.");
					
					day = true;
				} // 밤
			} // 낮 while
			End();
		} // run
	} // Main
} // Game

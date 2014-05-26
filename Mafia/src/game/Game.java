package game;

import java.util.*;
import java.io.*;

public class Game {
	HashMap<String, String> clients_job;
	Iterator<String> it;
	int num, maf, pol, doc;
	HashMap<String, DataOutputStream> clients;
	public int Num;
	boolean day = true; // 낮 밤을 결정하는 상수

	public Game() {
		System.out.println("Game 생성자");
	}

	public void Set(HashMap<String, DataOutputStream> clients, HashMap<String, String> clients_job) {
		try {
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
		System.out.println("마피아를 시작합니다!");
		new Main().start();
	}
	
	public void End() {
		System.out.println("게임이 끝났습니다.");
	}
	
	public class Main extends Thread {
		void Win(){
			int maf_num=0, citi_num=0;
			Iterator<String> it = clients_job.keySet().iterator();
			while(it.hasNext()){
				String tmpid = it.next();
				switch(tmpid){
				case "Mafia":
					maf_num++;
					break;
				case "Citizen":
					citi_num=0;
					break;
				}
			}
			if(maf_num>=citi_num)
				System.out.println("마피아가 이겼습니다.");
			else if(maf_num==0)
				System.out.println("시민이 이겼습니다.");
		}
		
		public void run() {
			while (day) {	// 낮
				if (!day) {	// 밤
					System.out.println("밤이 되었습니다.");
					day = true;
				} // 밤
				
				System.out.println("낮이 되었습니다.");
				
				
				
			} // 낮
		} // run
	} // Main
} // Game

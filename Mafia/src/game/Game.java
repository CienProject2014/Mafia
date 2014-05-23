package game;

import java.util.*;

public class Game {
	HashMap clients;
	public int num;

	public Game() {
		System.out.println("Game 생성자");
	}

	public void Set(HashMap clients) {
		this.clients = clients;
		System.out.println("---------마피아 설정---------");
		num = clients.size();
		if (num > 6) {
			System.out.println("총인원: " + num + "명");
			System.out.println("마피아 수: " + num + "/3=" + num / 3);
		} else {
			System.out.println("인원이 너무 적습니다.(6명 이상)");
		}
	}

	public void Start() {
		new Main().Start();

	}

	public class Main extends Thread {
		void Start() {
			System.out.println("마피아를 시작합니다!");
		}

		public void run() {

		}
	}
}

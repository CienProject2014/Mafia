package game;

import java.util.*;

public class Game {
	HashMap clients;
	public int num;

	public Game() {
		System.out.println("Game ������");
	}

	public void Set(HashMap clients) {
		this.clients = clients;
		System.out.println("---------���Ǿ� ����---------");
		num = clients.size();
		if (num > 6) {
			System.out.println("���ο�: " + num + "��");
			System.out.println("���Ǿ� ��: " + num + "/3=" + num / 3);
		} else {
			System.out.println("�ο��� �ʹ� �����ϴ�.(6�� �̻�)");
		}
	}

	public void Start() {
		new Main().Start();

	}

	public class Main extends Thread {
		void Start() {
			System.out.println("���ǾƸ� �����մϴ�!");
		}

		public void run() {

		}
	}
}

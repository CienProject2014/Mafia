package game;

import java.net.*;
import java.util.*;
import game.Unit.*;
import chat.Server;

public class Game {
	HashMap clients;
	public int Num;

	public Game() {
		System.out.println("Game ������");
	}

	public void Set(HashMap clients) {
		this.clients = clients;
		System.out.println("---------���Ǿ� ����---------");
		Num = clients.size();
		if (Num > 6) {
			System.out.println("���ο�: " + Num + "��");
			System.out.println("���Ǿ� ��: " + Num + "/3=" + Num / 3);
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

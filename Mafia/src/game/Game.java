package game;

import java.util.*;
import java.io.*;

public class Game {
	HashMap<String, String> clients_job;
	Iterator<String> it;
	int num, maf, pol, doc;
	HashMap<String, DataOutputStream> clients;
	public int Num;
	boolean day = true; // �� ���� �����ϴ� ���

	public Game() {
		System.out.println("Game ������");
	}

	public void Set(HashMap<String, DataOutputStream> clients, HashMap<String, String> clients_job) {
		try {
			num = clients.size();
			if (clients.isEmpty()) {
				System.out.println("Ŭ���̾�Ʈ�� ����ֽ��ϴ�.");
			} else {
				maf = num / 3;
				// it = clients.keySet().iterator();
				Random random = new Random();
				int val;
				// while (it.hasNext()) {
				// clients_job.put(it.next(), "Citizen");
				// System.out.println("��� �ù�����"); // �׽�Ʈ ���
				// }

				System.out.println("---------���Ǿ� ����---------");
				if (num > 5) {
					while (maf > 1) {
						System.out.println("ù��° while ����"); // �׽�Ʈ ���
						it = clients.keySet().iterator();
						while (it.hasNext()) {
							System.out.println("�ι�° while ����"); // �׽�Ʈ ���
							val = random.nextInt(num);
							System.out.println(val); // �׽�Ʈ ���
							if (val == 1) {
								if (maf < 1) {
									System.out.println("���Ǿ� ����"); // �׽�Ʈ ���
									break;
								}
								maf--;
								clients_job.put(it.next(), "Mafia");
								System.out.println("���Ǿ� �Ѹ� ����"); // �׽�Ʈ ���
							} else {
								clients_job.put(it.next(), "Citizen");
								System.out.println("�׳� �ù�����"); // �׽�Ʈ ���
							}
						}
					}
					System.out.println("���ο�: " + num + "��");
					System.out.println("���Ǿ� ��: " + num + "/3=" + maf);

				} else {
					System.out.println("�ο��� �ʹ� �����ϴ�.(6�� �̻�)");
				}
				System.out.println("----------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void Status() {
		it = clients_job.keySet().iterator();
		System.out.println("�������ͽ�����" + clients_job.keySet());
		while (it.hasNext()) {
			String temp = it.next();
			System.out.println("�������ͽ��ݺ�������");
			System.out.println("�̸�: " + temp);
			System.out.println("����: " + clients_job.get(temp));
		}
	}

	public void Start() {
		System.out.println("���ǾƸ� �����մϴ�!");
		new Main().start();
	}
	
	public void End() {
		System.out.println("������ �������ϴ�.");
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
				System.out.println("���Ǿư� �̰���ϴ�.");
			else if(maf_num==0)
				System.out.println("�ù��� �̰���ϴ�.");
		}
		
		public void run() {
			while (day) {	// ��
				if (!day) {	// ��
					System.out.println("���� �Ǿ����ϴ�.");
					day = true;
				} // ��
				
				System.out.println("���� �Ǿ����ϴ�.");
				
				
				
			} // ��
		} // run
	} // Main
} // Game

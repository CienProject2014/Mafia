package game;

import java.util.*;
import java.io.*;


public class Game {
	HashMap<String, String> clients_job = new HashMap<String, String>();
	Iterator<String> it;
	int num, maf, pol, doc;
	HashMap<String, DataOutputStream> clients;
	public int Num;

	public Game() {
		System.out.println("Game ������");
	}

	public HashMap<String, String> Set(HashMap<String, DataOutputStream> clients) {
		try {
			num = clients.size();
			if (clients.isEmpty()) {
				System.out.println("Ŭ���̾�Ʈ�� ����ֽ��ϴ�.");
				return null;
			} else {
				maf = num / 3;
				//it = clients.keySet().iterator();
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
					System.out.println("���Ǿ� ��: " + num + "/3=" + num / 3);

				} else {
					System.out.println("�ο��� �ʹ� �����ϴ�.(6�� �̻�)");
				}
				System.out.println("----------------------------");
				return clients_job;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
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
		new Main().Start();
	}

	public class Main extends Thread {
		void Start() {
			System.out.println("���ǾƸ� �����մϴ�!");
			start();
		}

		public void run() {

		}
	}
}

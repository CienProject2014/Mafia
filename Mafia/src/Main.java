import java.util.Scanner;

import chat.Client;
import chat.Server;

public class Main {

	public static void main(String[] args) {
		try {
			System.out.println("-----�޴�----");
			System.out.println("1. ��������");
			System.out.println("2. ��������");
			Scanner sc = new Scanner(System.in);

			Server server = new Server();
			// case 2���� Ŭ���̾�Ʈ���� ������ ������ �ٷ��� �̷��� ����
			
			byte sel;
			sel = sc.nextByte();
			switch (sel) {
			case 1:
				server.Start();
				
				break;
			case 2:
				String serverIp = "127.0.0.1";
				System.out.println("�г����� �Է��ϼ���.");
				String id = sc.next();

				Client client = new Client();
				client.Start(id, serverIp);
				
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

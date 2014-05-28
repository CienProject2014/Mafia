import java.util.Scanner;

import chat.Client;
import chat.Server;

public class Main {

	public static void main(String[] args) {
		try {
			System.out.println("-----메뉴----");
			System.out.println("1. 서버생성");
			System.out.println("2. 서버참가");
			Scanner sc = new Scanner(System.in);

			
			// case 2에서 클라이언트에게 서버의 정보를 줄려고 이렇게 했음
			// 줄 필요가 없어서 다시 수정.
			
			byte sel;
			sel = sc.nextByte();
			switch (sel) {
			case 1:
				Server server = new Server();
				server.Start();
				
				break;
			case 2:
				String serverIp = "127.0.0.1";
				System.out.println("닉네임을 입력하세요.");
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

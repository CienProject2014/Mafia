package chat;

import java.net.*;
import java.io.*;
import java.util.*;
import chat.Client;
import game.Game;

public class Server {
	HashMap clients;
	Game game;

	public Server() {
		// 비었음
	}

	public void Start() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
		game = new Game();

		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("서버가 시작되었습니다.");

			ServerSender sender = new ServerSender(); // 서버 sender 쓰레드 생성
			sender.start();
			// 여기다 따로 한 이유는 딱 하나만 생성하기 위함. (receiver는 클라이언트 갯수만큼 생성)

			while (true) {
				socket = serverSocket.accept(); // 클라이언트 연결요청 기다림

				ServerReceiver receiver = new ServerReceiver(socket);

				receiver.start();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // start()

	void sendToAll(String msg, String id) {
		Iterator it = clients.keySet().iterator();

		while (it.hasNext()) {
			try { // 서버에서 클라이언트로 출력하는 부분!
				DataOutputStream out = (DataOutputStream) clients.get(it.next());
				if (out.equals(clients.get(id)))
					continue; // 메세지 보낸 본인은 출력 안하게 continue
				out.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // while
	} // sendToAll

	class ServerSender extends Thread {
		boolean chat = true;

		public void run() {
			Scanner scanner = new Scanner(System.in);
			String message = "";
			try {
				while (true) {
					message = scanner.nextLine();
					if (message.isEmpty()) { // 아무것도 입력안하고 엔터시 continue
						continue;
					}
					if (message.charAt(0) == '@') { // @로 시작하는 명령어 입력시
						if (message.equals("@set")) // set->세팅
							game.Set(clients);
						else if (message.equals("@start")) // start->시작
							System.out.println("");// game.Startt;
						else
							System.out.println("명령어를 제대로 입력해주세요.");
					}
					sendToAll("[*Server*]" + message, "Server");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // run() }
	}

	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;

		Client client; // 클라이언트 객체 받음
		String id; // Clients의 key값

		ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void setClient(Client client) {
			this.client = client;
		}

		public void run() {
			try {
				Init();

				// -------- 쓰레드가 반복 --------//
				while (in != null) {
					String message = in.readUTF(); // 클라이언트가 보낸 메세지
					sendToAll(message, id); // 받은 메세지를 클라이언트 모두에게 뿌림
					System.out.println(message); // 서버에서 메세지 확인
				}
				// ------------- 끝 -------------//
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				Out();
			} // try
		} // run

		void Init() {
			try {
				id = in.readUTF();
				if (clients.containsKey(id)) {
					System.out.println("동일한 닉네임이 존재합니다.");
					// 이벤트 아직 안만듬
				} else if (clients.containsKey("Server")) {
					System.out.println("그 아이디는 만들 수 없습니다.");
					// 이벤트 아직 안만듬
				}

				clients.put(id, out);

				sendToAll("#" + id + "님이 들어오셨습니다.", "Server");
				System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");

				while (in != null) {
					String message = in.readUTF(); // 클라이언트가 보낸 메세지
					sendToAll(message, id); // 받은 메세지를 클라이언트 모두에게 뿌림
					System.out.println(message); // 서버에서 메세지 확인
				}
			} catch (IOException e) {
				e.printStackTrace();
			} // try
		}

		void Out() {
			sendToAll("#" + id + "님이 나가셨습니다.", "Server");
			clients.remove(id);
			System.out.println("[" + socket.hashCode() + "]"
					+ "에서 접속을 종료하였습니다.");
			System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
		}
	} // ReceiverThread
} // class
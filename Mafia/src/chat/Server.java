package chat;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
	HashMap clients;

	public Server() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
	}

	public void Start() {
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
				
				// 서버의 쓰레드 시작, 클라이언트마다 쓰레드 하나 만듬.
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
					if (message.equals("shutup")) {
						sendToAll("Everybody Shutup!", "Server");
						continue;
					}
					sendToAll(message, "Server");
				}
			} catch (Exception e) {
			}
		} // run() }
	}

	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream in;
		DataOutputStream out;
		// 클라이언트 정보를 저장하는 변수들; 쓰레드마다 따로 저장되겠지?
		String id; // Clients의 key값

		ServerReceiver(Socket socket) {
			this.socket = socket;
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
			}
		}

		public void run() {
			try {
				id = in.readUTF();
				if (clients.containsKey(id)) {
					System.out.println("동일한 닉네임이 존재합니다.");
					// 이벤트 아직 안만듬
				} else if (clients.containsKey("Server")) {
					System.out.println("그 아이디는 만들 수 없습니다.");
					// 이벤트 아직 안만듬
				}

				sendToAll("#" + id + "님이 들어오셨습니다.", "Server");

				clients.put(id, out);
				System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
				while (in != null) {
					sendToAll(in.readUTF(), id); // 클라이언트id에게 받은 메세지를 뿌림
				}
			} catch (IOException e) {
				// ignore
			} finally {
				sendToAll("#" + id + "님이 나가셨습니다.", "Server");
				clients.remove(id);
				System.out.println("[" + socket.hashCode() + "]"
						+ "에서 접속을 종료하였습니다.");
				System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
			} // try
		} // run
	} // ReceiverThread
} // class
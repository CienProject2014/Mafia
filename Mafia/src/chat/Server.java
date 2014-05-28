package chat;

import java.net.*;
import java.io.*;
import java.util.*;

import chat.Client;
import game.Game;

public class Server {
	public HashMap<String, DataOutputStream> clients;
	public HashMap<String, DataInputStream> clients_in;
	public HashMap<String, String> clients_job;
	public Game game;
	public Server me = this;
	public ServerSender sender;
	public boolean gameon = false; // 게임이 시작되었는지
	
	enum Condition{
		SERVER, ALL, MAFIA, POLICE, DOCTOR, NO
	}
	Condition sel = Condition.ALL;

	public Server() {
		// 비었음
	}

	public void Start() {
		clients = new HashMap<String, DataOutputStream>();
		// clients_in = new HashMap<String, DataInputStream>();
		clients_job = new HashMap<String, String>();
//		Collections.synchronizedMap(clients);
		
		game = new Game();

		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("서버가 시작되었습니다.");

			sender = new ServerSender(); // 서버 sender 쓰레드 생성
			sender.start();
			// 여기다 따로 한 이유는 딱 하나만 생성하기 위함. (receiver는 클라이언트 갯수만큼 생성)
			// sender안의 함수, 변수를 사용하기 위해 객체를 저장함.

			while (true) {
				socket = serverSocket.accept(); // 클라이언트 연결요청 기다림

				ServerReceiver receiver = new ServerReceiver(socket);

				receiver.start();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // start()
	
	// 감지에 걸리면 대화 못들음.
	boolean condition(String job, Condition sel){
		boolean result = false;
		
		switch(sel){
		case SERVER:
			result = true;
			break;
		case ALL:	// ALL: 모두 대화가능
			if(job.equals("Citizen")||job.equals("Mafia")||job.equals("Police")||job.equals("Doctor"))
				result = true;
			break;
		case MAFIA:	// MAFIA: 마피아만 대화
			if(job.equals("Mafia"))
				result = true;
			break;
		case POLICE:	// POLICE: 경찰만 대화
			if(job.equals("Police"))
				result = true;
			break;
		case DOCTOR:	// DOCTOR: 의사만 대화
			if(job.equals("DOCTOR"))
				result = true;
			break;
		case NO:	// NO: 아무도 대화 못함.
			result = false;
		}
		
		
		return result;
	}

	// 시스템 메세지
	void sendTo(String msg, String id) {
		Iterator<String> it = clients.keySet().iterator();

		while (it.hasNext()) {
			String tmpid = it.next();
			try { // 서버에서 클라이언트로 출력하는 부분!
				DataOutputStream out = (DataOutputStream) clients.get(tmpid);
				if (out.equals(clients.get(id)))
					continue; // 메세지 보낸 본인은 출력 안하게 continue
				out.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // while
	} // sendToAll

	// msg를 id가 job을 가진 사람에게
	void sendToGame(String msg, String id, Condition sel){
		Iterator<String> it = clients.keySet().iterator();

		while (it.hasNext()) {
			String tmpid = it.next();
			if(true){	// job에게만
				try { // 서버에서 클라이언트로 출력하는 부분!
					DataOutputStream out = (DataOutputStream) clients.get(tmpid);
					if (out.equals(clients.get(id)))
						continue; // 메세지 보낸 본인은 출력 안하게 continue
					out.writeUTF(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // if
			else{
				System.out.println("지금은 말할수 없습니다.");
			}
		} // while
	}
	
	public class ServerSender extends Thread {
		public boolean chat = true;

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
						if (message.equals("@set")){ // set->세팅
							game.Set(me);
						}
						else if (message.equals("@start")) { // start->시작
							game.Start();
						}
						else if (message.equals("@status")){ // status->직업표시
							System.out.println("사용자 직업 상태:");
							game.Status();
						}
						else if (message.equals("@time")) {
							System.out.print("주어질 시간을 입력하세요: ");
							int sec = scanner.nextInt();
							chat = false;
						}
						else
							System.out.println("명령어를 제대로 입력해주세요.");
					}
					sendTo("[*Server*]" + message, "Server");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} // run() }
	}

	public class ServerReceiver extends Thread {
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
					// 받은 메세지를 클라이언트 모두에게 뿌림
					if(gameon) sendToGame(message, id, Condition.MAFIA); // 게임이 시작되었으면
					else sendTo(message, id);  // 게임이 시작되지 않았으면
					
					System.out.println(message); // 서버에서 메세지 확인
				}
				// ------------- 끝 -------------//
			} catch (IOException e) {
				
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
				// clients_in.put(d. in);
				clients_job.put(id, "Citizen");

				sendTo("#" + id + "님이 들어오셨습니다.", "Server");
				System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");

				while (in != null) {
					String message = in.readUTF(); // 클라이언트가 보낸 메세지
					sendTo(message, id); // 받은 메세지를 클라이언트 모두에게 뿌림
					System.out.println(message); // 서버에서 메세지 확인
				}
			} catch (IOException e) {
				e.printStackTrace();
			} // try
		}

		void Out() {
			sendTo("#" + id + "님이 나가셨습니다.", "Server");
			clients.remove(id);
			System.out.println("[" + socket.hashCode() + "]"
					+ "에서 접속을 종료하였습니다.");
			System.out.println("현재 서버접속자 수는 " + clients.size() + "입니다.");
		}
	} // ReceiverThread
} // class

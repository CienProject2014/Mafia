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
	public boolean gameon = false; // ������ ���۵Ǿ�����
	
	enum Condition{
		SERVER, ALL, MAFIA, POLICE, DOCTOR, NO
	}
	Condition sel = Condition.ALL;

	public Server() {
		// �����
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
			System.out.println("������ ���۵Ǿ����ϴ�.");

			sender = new ServerSender(); // ���� sender ������ ����
			sender.start();
			// ����� ���� �� ������ �� �ϳ��� �����ϱ� ����. (receiver�� Ŭ���̾�Ʈ ������ŭ ����)
			// sender���� �Լ�, ������ ����ϱ� ���� ��ü�� ������.

			while (true) {
				socket = serverSocket.accept(); // Ŭ���̾�Ʈ �����û ��ٸ�

				ServerReceiver receiver = new ServerReceiver(socket);

				receiver.start();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // start()
	
	// ������ �ɸ��� ��ȭ ������.
	boolean condition(String job, Condition sel){
		boolean result = false;
		
		switch(sel){
		case SERVER:
			result = true;
			break;
		case ALL:	// ALL: ��� ��ȭ����
			if(job.equals("Citizen")||job.equals("Mafia")||job.equals("Police")||job.equals("Doctor"))
				result = true;
			break;
		case MAFIA:	// MAFIA: ���ǾƸ� ��ȭ
			if(job.equals("Mafia"))
				result = true;
			break;
		case POLICE:	// POLICE: ������ ��ȭ
			if(job.equals("Police"))
				result = true;
			break;
		case DOCTOR:	// DOCTOR: �ǻ縸 ��ȭ
			if(job.equals("DOCTOR"))
				result = true;
			break;
		case NO:	// NO: �ƹ��� ��ȭ ����.
			result = false;
		}
		
		
		return result;
	}

	// �ý��� �޼���
	void sendTo(String msg, String id) {
		Iterator<String> it = clients.keySet().iterator();

		while (it.hasNext()) {
			String tmpid = it.next();
			try { // �������� Ŭ���̾�Ʈ�� ����ϴ� �κ�!
				DataOutputStream out = (DataOutputStream) clients.get(tmpid);
				if (out.equals(clients.get(id)))
					continue; // �޼��� ���� ������ ��� ���ϰ� continue
				out.writeUTF(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} // while
	} // sendToAll

	// msg�� id�� job�� ���� �������
	void sendToGame(String msg, String id, Condition sel){
		Iterator<String> it = clients.keySet().iterator();

		while (it.hasNext()) {
			String tmpid = it.next();
			if(true){	// job���Ը�
				try { // �������� Ŭ���̾�Ʈ�� ����ϴ� �κ�!
					DataOutputStream out = (DataOutputStream) clients.get(tmpid);
					if (out.equals(clients.get(id)))
						continue; // �޼��� ���� ������ ��� ���ϰ� continue
					out.writeUTF(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} // if
			else{
				System.out.println("������ ���Ҽ� �����ϴ�.");
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
					if (message.isEmpty()) { // �ƹ��͵� �Է¾��ϰ� ���ͽ� continue
						continue;
					}
					if (message.charAt(0) == '@') { // @�� �����ϴ� ��ɾ� �Է½�
						if (message.equals("@set")){ // set->����
							game.Set(me);
						}
						else if (message.equals("@start")) { // start->����
							game.Start();
						}
						else if (message.equals("@status")){ // status->����ǥ��
							System.out.println("����� ���� ����:");
							game.Status();
						}
						else if (message.equals("@time")) {
							System.out.print("�־��� �ð��� �Է��ϼ���: ");
							int sec = scanner.nextInt();
							chat = false;
						}
						else
							System.out.println("��ɾ ����� �Է����ּ���.");
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

		Client client; // Ŭ���̾�Ʈ ��ü ����
		String id; // Clients�� key��

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

				// -------- �����尡 �ݺ� --------//
				while (in != null) {
					String message = in.readUTF(); // Ŭ���̾�Ʈ�� ���� �޼���
					// ���� �޼����� Ŭ���̾�Ʈ ��ο��� �Ѹ�
					if(gameon) sendToGame(message, id, Condition.MAFIA); // ������ ���۵Ǿ�����
					else sendTo(message, id);  // ������ ���۵��� �ʾ�����
					
					System.out.println(message); // �������� �޼��� Ȯ��
				}
				// ------------- �� -------------//
			} catch (IOException e) {
				
			} finally {
				Out();
			} // try
		} // run

		void Init() {
			try {
				id = in.readUTF();
				if (clients.containsKey(id)) {
					System.out.println("������ �г����� �����մϴ�.");
					// �̺�Ʈ ���� �ȸ���
				} else if (clients.containsKey("Server")) {
					System.out.println("�� ���̵�� ���� �� �����ϴ�.");
					// �̺�Ʈ ���� �ȸ���
				}

				clients.put(id, out);
				// clients_in.put(d. in);
				clients_job.put(id, "Citizen");

				sendTo("#" + id + "���� �����̽��ϴ�.", "Server");
				System.out.println("���� ���������� ���� " + clients.size() + "�Դϴ�.");

				while (in != null) {
					String message = in.readUTF(); // Ŭ���̾�Ʈ�� ���� �޼���
					sendTo(message, id); // ���� �޼����� Ŭ���̾�Ʈ ��ο��� �Ѹ�
					System.out.println(message); // �������� �޼��� Ȯ��
				}
			} catch (IOException e) {
				e.printStackTrace();
			} // try
		}

		void Out() {
			sendTo("#" + id + "���� �����̽��ϴ�.", "Server");
			clients.remove(id);
			System.out.println("[" + socket.hashCode() + "]"
					+ "���� ������ �����Ͽ����ϴ�.");
			System.out.println("���� ���������� ���� " + clients.size() + "�Դϴ�.");
		}
	} // ReceiverThread
} // class

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
		// �����
	}

	public void Start() {
		clients = new HashMap();
		Collections.synchronizedMap(clients);
		game = new Game();

		ServerSocket serverSocket = null;
		Socket socket = null;

		try {
			serverSocket = new ServerSocket(7777);
			System.out.println("������ ���۵Ǿ����ϴ�.");

			ServerSender sender = new ServerSender(); // ���� sender ������ ����
			sender.start();
			// ����� ���� �� ������ �� �ϳ��� �����ϱ� ����. (receiver�� Ŭ���̾�Ʈ ������ŭ ����)

			while (true) {
				socket = serverSocket.accept(); // Ŭ���̾�Ʈ �����û ��ٸ�

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
			try { // �������� Ŭ���̾�Ʈ�� ����ϴ� �κ�!
				DataOutputStream out = (DataOutputStream) clients.get(it.next());
				if (out.equals(clients.get(id)))
					continue; // �޼��� ���� ������ ��� ���ϰ� continue
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
					if (message.isEmpty()) { // �ƹ��͵� �Է¾��ϰ� ���ͽ� continue
						continue;
					}
					if (message.charAt(0) == '@') { // @�� �����ϴ� ��ɾ� �Է½�
						if (message.equals("@set")) // set->����
							game.Set(clients);
						else if (message.equals("@start")) // start->����
							System.out.println("");// game.Startt;
						else
							System.out.println("��ɾ ����� �Է����ּ���.");
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
					sendToAll(message, id); // ���� �޼����� Ŭ���̾�Ʈ ��ο��� �Ѹ�
					System.out.println(message); // �������� �޼��� Ȯ��
				}
				// ------------- �� -------------//
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
					System.out.println("������ �г����� �����մϴ�.");
					// �̺�Ʈ ���� �ȸ���
				} else if (clients.containsKey("Server")) {
					System.out.println("�� ���̵�� ���� �� �����ϴ�.");
					// �̺�Ʈ ���� �ȸ���
				}

				clients.put(id, out);

				sendToAll("#" + id + "���� �����̽��ϴ�.", "Server");
				System.out.println("���� ���������� ���� " + clients.size() + "�Դϴ�.");

				while (in != null) {
					String message = in.readUTF(); // Ŭ���̾�Ʈ�� ���� �޼���
					sendToAll(message, id); // ���� �޼����� Ŭ���̾�Ʈ ��ο��� �Ѹ�
					System.out.println(message); // �������� �޼��� Ȯ��
				}
			} catch (IOException e) {
				e.printStackTrace();
			} // try
		}

		void Out() {
			sendToAll("#" + id + "���� �����̽��ϴ�.", "Server");
			clients.remove(id);
			System.out.println("[" + socket.hashCode() + "]"
					+ "���� ������ �����Ͽ����ϴ�.");
			System.out.println("���� ���������� ���� " + clients.size() + "�Դϴ�.");
		}
	} // ReceiverThread
} // class
/*
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class test {
	ServerSocket sers = null; // 서버 소켓
	Socket s = null; // 클라이언트 소켓
	List cl_list = Collections.synchronizedList(new ArrayList());
	Vector list = new Vector(); // 이름을 저장하기 위핸 벡터

	public static void main(String[] args) {
		new test(args);
	}

	// 생성자
	test(String[] args) {
		try {
			sers = new ServerSocket(Integer.parseInt("3000"));
		} catch (Exception e) {
			System.out.println("Can't open ServerSocket");
		}
		System.out
				.println("Java Chat Server started. Accepting the client ...");

		try {
			while (true) {
				s = sers.accept();
				// 통신을 담당할 스레드 생성
				ManageClient mc = new ManageClient(s);
				cl_list.add(mc);
				System.out.println("참가자가 추가되었습니다. 총 참가자는 =" + (cl_list.size())
						+ "명입니다.");

				// 스레드 동작 시작
				mc.start();
			}
		} catch (Exception e) {
		}
	}

	// 통신을 담당할 스레드
	class ManageClient extends Thread {
		Socket s;
		BufferedReader br;
		PrintWriter pw;

		// 생성자
		public ManageClient(Socket s) {
			this.s = s;
			try {
				br = new BufferedReader(new InputStreamReader(
						s.getInputStream()));
				pw = new PrintWriter(
						new OutputStreamWriter(s.getOutputStream()));
			} catch (Exception e) {
				System.out.println("Can't make stream");
			}
		}

		// run() 메소드
		public void run() {
			String msg;
			String name;
			int cnt = 0;

			while (true) {
				try {
					msg = br.readLine();
				} catch (Exception e) {
					System.out
							.println("Reading error for a client. (Could be logout)");
					exitClient(this);
					return;
				}
				int index = msg.indexOf("BYE");
				int si = msg.indexOf(":");

				if (msg.indexOf("[connect]") >= 0) { // 처음 사용자 접속시 이름 벡터에 추가
					list.addElement(msg.substring(0, si));
					broadcast(msg);
				} else if (index >= 0) {
					broadcast(msg.substring(0, index) + "님이 채팅을 종료합니다.");
					exitClient(this); // 채팅 탈퇴 처리
					return; // run() 메소드 종료
				} else if (msg.indexOf("[HIDE]") >= 0) { // 귓속말 요청시 해당 클라이언트에게만
															// 메세지를 보낸다.
					name = msg.substring(msg.lastIndexOf("<") + 1,
							msg.lastIndexOf(">"));
					for (int i = 0; System.out.println("member : "
							+ list.elementAt(i)
							+ " // "
							+ msg.substring(msg.indexOf("<") + 1,
									msg.indexOf(">")) + "->" + name);) {
						if (name.equals((String) list.elementAt(i)) == true) {
							cnt = i;
						}
					}
					System.out.println("cnt = " + cnt);
					unicast(msg, cnt);
				} else {
					System.out.println(msg);
					broadcast(msg); // 모든 클라이언트에게 메시지를 방송
				}
				yield(); // 다른 스레드에게 잠시 양보
			}
		}
	}

	// 특정 클라이언트에게 메세지를 보내는 메소드 (귓속말)
	public void unicast(String msg, int i) {

		try {
			ManageClient c = (ManageClient) cl_list.get(i);
			c.pw.println(msg);
			c.pw.flush();
		} catch (Exception e) {
			System.out.println("귓속말 에러");
		}
	}

	// 모든 클라이언트에게 메시지를 방송하는 메소드
	public void broadcast(String msg) {
		int numOfConnect = cl_list.size();
		for (int i = 0; i < numOfConnect; i++) {
			try {
				ManageClient mc = (ManageClient) cl_list.get(i);
				mc.pw.println(msg);
				mc.pw.flush();
			} catch (Exception e) {
				System.out.println("broadcast() error.");
			}
		}
	}

	// 클라이언트가 탈퇴할 때 호출되는 메소드
	public void exitClient(ManageClient c) {
		int i = cl_list.indexOf(c);
		if (i >= 0) {
			cl_list.remove(i);
			list.removeElementAt(i);
			try {
				if (c.br != null)
					c.br.close();
				if (c.pw != null)
					c.pw.close();
				if (c.s != null)
					c.s.close();
			} catch (Exception e) {
				System.out.println("Exception error");
			}
			System.out.println("A Client exited! Number of client= "
					+ cl_list.size());
		} else {
			System.out.println("No such a client in Server! ");
		}
	}
}
*/

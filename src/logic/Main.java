package logic;

public class Main {

	public static void main(String[] args) {
		Router r = new Router("192.168.10.1");
		Updater u = new Updater(r);
		Thread t = new Thread(u);
		t.setDaemon(false);
		t.start();
	}

}

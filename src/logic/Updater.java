package logic;

/*
 * sluzi samo za testiranje
 * */
public class Updater implements Runnable {

	private int interval = 5 * 1000;
	private Router r;

	public Updater(Router ro) {
		r = ro;
	}

	@Override
	public void run() {
		try {
			while (true) {
				r.initUpdate();
				System.out.println(r.toString());
				Thread.sleep(interval);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		r.closeSession();
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}

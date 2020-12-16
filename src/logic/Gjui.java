package logic;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import com.ireasoning.protocol.snmp.SnmpConst;
import com.ireasoning.protocol.snmp.SnmpCounter32;
import com.ireasoning.protocol.snmp.SnmpDataType;
import com.ireasoning.protocol.snmp.SnmpInt;
import com.ireasoning.protocol.snmp.SnmpSession;
import com.ireasoning.protocol.snmp.SnmpTableModel;

public class Gjui {
	// KRK KAD SI GLUP RADI OVAKO PATTERN ZVANI MONGOL
	private Instant start1, start2;
	private JFrame frmVarijanta;
	private JPanel ruter1, ruter2;
	private Router r;
	private JComboBox<String> comboBox;
	private JLabel lblRouterIp;
	// PRVI RUTER
	private JLabel lblIdSuseda1;
	private JLabel lblStanjeSesije1;
	private JLabel lblBgpVerz1;
	private JLabel lblIPsuseda1;
	private JLabel lblASsuseda1;
	private JLabel lblInUpdates1;
	private JLabel lblOutUpdates1;
	private JLabel lblKeepalive1;
	private JLabel lblElapsed12;
	// DRUGI RUTER
	private JLabel lblIdSuseda2;
	private JLabel lblStanjeSesije2;
	private JLabel lblBgpVerzija2;
	private JLabel lblIPsuseda2;
	private JLabel lblASsuseda2;
	private JLabel lblInUpdates2;
	private JLabel lblOutUpdates2;
	private JLabel lblKeepalive2;
	private JLabel lblElapsed21;
	private JLabel lblImeSuseda;
	private JLabel lblImeSuseda2;
	// ostalo
	private Timer timerUpdateTime;
	private Timer timerUpdateAll;
	private static Gjui window;
	private long u1prev = 0, u2prev = 0;
	private SnmpSession sesijaP = null;

	private BgpPeer bgp1 = null;
	private BgpPeer bgp2 = null;
	private SnmpTableModel tabla = null;

	private static final int delayZ = 10000;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// if(EventQueue.isDispatchThread())
				// System.out.println("DISP THR");
				window = new Gjui();
				window.r = new Router("192.168.10.1"); // inicijalno selektovano djubre
				window.addTimers();
				window.start1 = Instant.now();
				window.start2 = Instant.now();
				window.frmVarijanta.setVisible(true);
			}
		});

	}

	/**
	 * Create the application.
	 */
	public Gjui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVarijanta = new JFrame();
		frmVarijanta.setTitle("Varijanta 4");
		frmVarijanta.getContentPane().setBackground(new Color(204, 204, 255));
		frmVarijanta.getContentPane().setBounds(new Rectangle(100, 100, 100, 100));
		frmVarijanta.setBounds(100, 100, 520, 354);
		frmVarijanta.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// pogasi sesije
		frmVarijanta.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				sesijaP.close();
				r.closeSession();
			}
		});
		frmVarijanta.getContentPane().setLayout(null);

		ruter1 = new JPanel();
		ruter1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ruter1.setBackground(new Color(204, 153, 255));
		ruter1.setBounds(new Rectangle(30, 80, 218, 156));
		frmVarijanta.getContentPane().add(ruter1);
		ruter1.setLayout(new BoxLayout(ruter1, BoxLayout.Y_AXIS));

		lblImeSuseda = new JLabel("ROUTER ");
		lblImeSuseda.setBackground(Color.LIGHT_GRAY);
		lblImeSuseda.setVerticalAlignment(SwingConstants.BOTTOM);
		lblImeSuseda.setHorizontalAlignment(SwingConstants.CENTER);
		ruter1.add(lblImeSuseda);

		lblIdSuseda1 = new JLabel("ID suseda:");
		lblIdSuseda1.setVerticalAlignment(SwingConstants.TOP);
		ruter1.add(lblIdSuseda1);

		lblStanjeSesije1 = new JLabel("Stanje sesije:");
		ruter1.add(lblStanjeSesije1);

		lblBgpVerz1 = new JLabel("BGP verzija:");
		ruter1.add(lblBgpVerz1);

		lblIPsuseda1 = new JLabel("IP suseda:");
		ruter1.add(lblIPsuseda1);

		lblASsuseda1 = new JLabel("AS suseda:");
		ruter1.add(lblASsuseda1);

		lblKeepalive1 = new JLabel("Keepalive:");
		ruter1.add(lblKeepalive1);

		lblOutUpdates1 = new JLabel("Updates OUT:");
		ruter1.add(lblOutUpdates1);

		lblInUpdates1 = new JLabel("Updates IN:");
		ruter1.add(lblInUpdates1);

		lblElapsed12 = new JLabel("Elapsed R2:");
		ruter1.add(lblElapsed12);

		// DRUGI RUTER

		ruter2 = new JPanel();
		ruter2.setBounds(272, 80, 218, 156);
		ruter2.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ruter2.setBackground(new Color(204, 153, 255));
		frmVarijanta.getContentPane().add(ruter2);
		ruter2.setLayout(new BoxLayout(ruter2, BoxLayout.Y_AXIS));

		lblImeSuseda2 = new JLabel("ROUTER ");
		lblImeSuseda2.setVerticalAlignment(SwingConstants.BOTTOM);
		ruter2.add(lblImeSuseda2);

		lblIdSuseda2 = new JLabel("ID suseda:");
		ruter2.add(lblIdSuseda2);

		lblStanjeSesije2 = new JLabel("Stanje sesije:");
		ruter2.add(lblStanjeSesije2);

		lblBgpVerzija2 = new JLabel("BGP verzija:");
		ruter2.add(lblBgpVerzija2);

		lblIPsuseda2 = new JLabel("IP suseda:");
		ruter2.add(lblIPsuseda2);

		lblASsuseda2 = new JLabel("AS suseda:");
		ruter2.add(lblASsuseda2);

		lblKeepalive2 = new JLabel("Keepalive:");
		ruter2.add(lblKeepalive2);

		lblOutUpdates2 = new JLabel("Updates OUT:");
		ruter2.add(lblOutUpdates2);

		lblInUpdates2 = new JLabel("Updates IN:");
		ruter2.add(lblInUpdates2);

		lblElapsed21 = new JLabel("Elapsed R1:");
		ruter2.add(lblElapsed21);

		lblRouterIp = new JLabel("Izaberite ruter:");
		lblRouterIp.setBounds(141, 30, 131, 15);
		frmVarijanta.getContentPane().add(lblRouterIp);

		comboBox = new JComboBox<String>();
		comboBox.setMaximumRowCount(3);
		comboBox.setBounds(272, 25, 123, 24);
		comboBox.addItem("192.168.10.1");
		comboBox.addItem("192.168.20.1");
		comboBox.addItem("192.168.30.1");
		frmVarijanta.getContentPane().add(comboBox);
		// the ActionListener.actionPerformed method on a Swing object is already
		// invoked from the EDT.
		// isto i za ovo sa itemima itd, moze odavde da se update gui
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					System.out.println("SELEKTOVO SI " + (String) e.getItem());
					window.r = new Router((String) e.getItem());
					window.start1 = Instant.now();
					window.start2 = Instant.now();
					window.r.initUpdate();
					window.updateRouters();
					// timerUpdateAll.restart();
					// timerUpdateTime.restart();
					timerUpdateAll.stop();
					timerUpdateTime.stop();
					window.addTimers();
				}
			}
		});

	}

	public void addTimers() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ActionListener al = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						System.out.println("Proslo 5sec");
						r.initUpdate();
						window.updateRouters();
					}
				};
				// for initial update
				r.initUpdate();
				window.updateRouters();

				// delayZ = 10000
				timerUpdateAll = new Timer(5000, al);
				timerUpdateAll.start();

				/*
				 * Although all Timers perform their waiting using a single, shared thread
				 * (created by the first Timer object that executes), the action event handlers
				 * for Timers execute on another thread -- the event-dispatching thread. This
				 * means that the action handlers for Timers can safely perform operations on
				 * Swing components. However, it also means that the handlers must execute
				 * quickly to keep the GUI responsive.
				 */

				ActionListener al2 = new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						window.updateRouters();
						window.updateTime();
					}
				};
				// initial update time
				window.updateTime();

				timerUpdateTime = new Timer(500, al2);
				timerUpdateTime.start();
			}
		});

	}

	public void updateRouters() {
		bgp1 = r.getBgpPeers().get(0);
		bgp2 = r.getBgpPeers().get(1);

		try {
			sesijaP = new SnmpSession(r.getIpAddress(), 161, "si2019", "si2019", SnmpConst.SNMPV2);

		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Iz updateRouters() sesija se nije inicijalizovala");
		}

		// ruter1

		lblImeSuseda.setText("ROUTER " + bgp1.getBgpPeerRemoteAs().toString());
		lblIdSuseda1.setText("ID suseda: " + bgp1.getBgpPeerIdentifier().toString());
		lblStanjeSesije1.setText("Stanje sesije: " + BgpPeer.getState((SnmpInt) bgp1.getBgpPeerState()));
		lblBgpVerz1.setText("BGP verzija:" + bgp1.getBgpPeerNegotiatedVersion().toString());
		lblIPsuseda1.setText("IP suseda:" + bgp1.getBgpPeerRemoteAddr().toString());
		lblASsuseda1.setText("AS suseda: " + bgp1.getBgpPeerRemoteAs().toString());
		lblKeepalive1.setText("Keepalive: " + bgp1.getBgpPeerKeepAlive().toString());
		lblInUpdates1.setText("Updates IN: " + bgp1.getBgpPeerInUpdates().toString());
		lblOutUpdates1.setText("Updates OUT: " + bgp1.getBgpPeerOutUpdates().toString());

		// ruter2
		lblImeSuseda2.setText("ROUTER " + bgp2.getBgpPeerRemoteAs().toString());
		lblIdSuseda2.setText("ID suseda: " + bgp2.getBgpPeerIdentifier().toString());
		lblStanjeSesije2.setText("Stanje sesije: " + BgpPeer.getState((SnmpInt) bgp2.getBgpPeerState()));
		lblBgpVerzija2.setText("BGP verzija:" + bgp2.getBgpPeerNegotiatedVersion().toString());
		lblIPsuseda2.setText("IP suseda:" + bgp2.getBgpPeerRemoteAddr().toString());
		lblASsuseda2.setText("AS suseda: " + bgp2.getBgpPeerRemoteAs().toString());
		lblKeepalive2.setText("Keepalive: " + bgp2.getBgpPeerKeepAlive().toString());
		lblInUpdates2.setText("Updates IN: " + bgp2.getBgpPeerInUpdates().toString());
		lblOutUpdates2.setText("Updates OUT: " + bgp2.getBgpPeerOutUpdates().toString());
		lblElapsed21.setForeground(new Color(51, 51, 51));
		lblElapsed12.setForeground(new Color(51, 51, 51));
		lblInUpdates1.setForeground(new Color(51, 51, 51));
		lblInUpdates2.setForeground(new Color(51, 51, 51));

	}

	public void updateTime() {
		long u1 = 0, u2 = 0;
		if (bgp1 != null && bgp2 != null && sesijaP != null) {
			try {
				tabla = sesijaP.snmpGetTable("bgpPeerTable");
				SnmpDataType peerInUpdates1 = tabla.get(0, 9).getValue();
				SnmpDataType peerInUpdates2 = tabla.get(1, 9).getValue();
				// dohvati iz sesije broj update poruka koje su pristigle i vidi sa prethodnim
				// dal se razlikuje
				if (u1prev == 0 && u2prev == 0) {
					u1prev = ((SnmpCounter32) peerInUpdates1).getValue();
					u2prev = ((SnmpCounter32) peerInUpdates2).getValue();
					u1 = ((SnmpCounter32) peerInUpdates1).getValue();
					u2 = ((SnmpCounter32) peerInUpdates2).getValue();
				} else {
					u1 = ((SnmpCounter32) peerInUpdates1).getValue();
					u2 = ((SnmpCounter32) peerInUpdates2).getValue();
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Iz updateTime() ne radi nesto, vrv tabelu nije napravilo dobro");
			}
			if (u1prev == u1) {
				lblElapsed12.setText("Last update from R" + bgp1.getBgpPeerRemoteAs().toString() + ": "
						+ Duration.between(start1, Instant.now()).toSeconds() + "s ago");
			} else {
				lblElapsed12.setText("Last update from R" + bgp1.getBgpPeerRemoteAs().toString() + ": " + 0 + "s ago");
				lblElapsed12.setForeground(new Color(255, 0, 234));
				lblInUpdates1.setForeground(new Color(255, 0, 234));
				start1 = Instant.now();
			}
			if (u2prev == u2)
				lblElapsed21.setText("Last update from R" + bgp2.getBgpPeerRemoteAs().toString() + ": "
						+ Duration.between(start2, Instant.now()).toSeconds() + "s ago");
			else {
				lblElapsed21.setText("Last update from R" + bgp2.getBgpPeerRemoteAs().toString() + ": " + 0 + "s ago");
				lblElapsed21.setForeground(new Color(255, 0, 234));
				lblInUpdates2.setForeground(new Color(255, 0, 234));
				start2 = Instant.now();
			}

			u1prev = u1;
			u2prev = u2;
		}
		if (bgp1 == null || bgp2 == null || sesijaP == null)
			System.out.println("E TU SI SE SJEBO");

	}
}

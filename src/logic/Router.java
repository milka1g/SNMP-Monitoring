package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ireasoning.protocol.snmp.MibUtil;
import com.ireasoning.protocol.snmp.SnmpConst;
import com.ireasoning.protocol.snmp.SnmpCounter32;
import com.ireasoning.protocol.snmp.SnmpDataType;
import com.ireasoning.protocol.snmp.SnmpInt;
import com.ireasoning.protocol.snmp.SnmpSession;
import com.ireasoning.protocol.snmp.SnmpTableModel;
import com.ireasoning.protocol.snmp.SnmpTarget;
import com.ireasoning.util.MibParseException;

/*
 * klasa koja se instancira nakon sto korisnik unese ip adresu tog rutera
 * */
public class Router {

	private String ipAddress;
	private static final String community = "si2019";

	private SnmpTarget targetRouter;
	private SnmpSession session;
	private SnmpTableModel bgpPeerTable;
	private int bgpPeerInUpdates;
	private List<BgpPeer> bgpPeers = new ArrayList<>();

	public Router(String ip) {
		this.ipAddress = ip;
		targetRouter = new SnmpTarget(ipAddress, 161, community, community, SnmpConst.SNMPV2);
		bgpPeerInUpdates = 0;
		System.out.println("Kreiran je ruter " + ip);
		initUpdate();
	}

	public void initUpdate() {
		bgpPeers.clear();
		bgpPeerInUpdates = 0;
		try {
			session = new SnmpSession(targetRouter);
			session.setTimeout(5000);
			session.setRetries(0);
			MibUtil.loadMib("mibs/BGP4-MIB");
			bgpPeerTable = session.snmpGetTable("bgpPeerTable");

		} catch (IOException e) {
			e.printStackTrace();
		} catch (MibParseException e) {
			e.printStackTrace();
		}
		if (bgpPeerTable == null) {
			System.err.println("Table not found in loaded MIBs");
			return;
		}
		int cnt = 0;
		for (int i = 0; i < bgpPeerTable.getRowCount(); i++) {

			SnmpDataType peerId = bgpPeerTable.get(cnt, 0).getValue();
			SnmpDataType peerState = bgpPeerTable.get(cnt, 1).getValue();
			SnmpDataType peerBGPv = bgpPeerTable.get(cnt, 3).getValue();
			SnmpDataType peerRemoteAddr = bgpPeerTable.get(cnt, 6).getValue();
			SnmpDataType peerRemoteAS = bgpPeerTable.get(cnt, 8).getValue();
			SnmpDataType peerInUpdates = bgpPeerTable.get(cnt, 9).getValue();
			SnmpDataType peerOutUpdates = bgpPeerTable.get(cnt, 10).getValue();
			SnmpDataType peerKeepAlive = bgpPeerTable.get(cnt, 18).getValue();
			bgpPeerInUpdates += ((SnmpCounter32) peerInUpdates).getValue();
			BgpPeer peer = new BgpPeer(peerId, peerState, peerBGPv, peerRemoteAddr, peerRemoteAS, peerInUpdates,
					peerOutUpdates, peerKeepAlive);
			bgpPeers.add(peer);
			cnt++;
		}

	}

	public SnmpTarget getTargetRouter() {
		return targetRouter;
	}

	public void setTargetRouter(SnmpTarget targetRouter) {
		this.targetRouter = targetRouter;
	}

	public List<BgpPeer> getBgpPeers() {
		return bgpPeers;
	}

	public void setBgpPeers(List<BgpPeer> bgpPeers) {
		this.bgpPeers = bgpPeers;
	}

	@Override
	public String toString() {
		String ret = "";
		for (BgpPeer p : bgpPeers) {
			ret += "ID: " + p.getBgpPeerIdentifier() + " " + "ST: " + BgpPeer.getState((SnmpInt) p.getBgpPeerState())
					+ " " + "V: " + p.getBgpPeerNegotiatedVersion() + " " + "IP: " + p.getBgpPeerRemoteAddr() + " "
					+ "UI: " + p.getBgpPeerInUpdates() + " " + "UO: " + p.getBgpPeerOutUpdates() + " " + "KP: "
					+ p.getBgpPeerKeepAlive() + " \n";
		}
		return ret;
	}

	public void closeSession() {
		session.close();
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}

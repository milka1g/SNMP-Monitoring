package logic;

import java.util.HashMap;
import java.util.Map;

import com.ireasoning.protocol.snmp.SnmpDataType;
import com.ireasoning.protocol.snmp.SnmpInt;

/*
 * klasa predstavlja bgp suseda posmatranog rutera
 * */
public class BgpPeer {
	public enum State {
		Idle, Connect, Active, OpenSent, OpenConfirm, Established;
	}
	
	private SnmpDataType bgpPeerIdentifier; //0;IpAddress -> OctetString size(4)
	private SnmpDataType bgpPeerState; //1;integer
	private SnmpDataType bgpPeerNegotiatedVersion; //3;integer
	private SnmpDataType bgpPeerRemoteAddr; //6;IpAddress -> OctetString size(4)
	private SnmpDataType bgpPeerRemoteAs; //8;integer
	private SnmpDataType bgpPeerInUpdates; //9;Counter32 -> int
	private SnmpDataType bgpPeerOutUpdates; //10;Counter32 -> int
	private SnmpDataType bgpPeerKeepAlive; //18;integer
	public static Map<State, String> indexToState = new HashMap<>();
	
	static {
		indexToState.put(State.Idle,"Idle");
		indexToState.put(State.Connect,"Connect");
		indexToState.put(State.Active,"Active");
		indexToState.put(State.OpenSent,"OpenSent");
		indexToState.put(State.OpenConfirm,"OpenConfirm");
		indexToState.put(State.Established,"Established");
	}
	
	
	
	public BgpPeer(SnmpDataType bgpPeerIdentifier, SnmpDataType bgpPeerState, SnmpDataType bgpPeerNegotiatedVersion,
			SnmpDataType bgpPeerRemoteAddr, SnmpDataType bgpPeerRemoteAs, SnmpDataType bgpPeerInUpdates,
			SnmpDataType bgpPeerOutUpdates, SnmpDataType bgpPeerKeepAlive) {
		super();
		this.bgpPeerIdentifier = bgpPeerIdentifier;
		this.bgpPeerState = bgpPeerState;
		this.bgpPeerNegotiatedVersion = bgpPeerNegotiatedVersion;
		this.bgpPeerRemoteAddr = bgpPeerRemoteAddr;
		this.bgpPeerRemoteAs = bgpPeerRemoteAs;
		this.bgpPeerInUpdates = bgpPeerInUpdates;
		this.bgpPeerOutUpdates = bgpPeerOutUpdates;
		this.bgpPeerKeepAlive = bgpPeerKeepAlive;
	}
	public SnmpDataType getBgpPeerIdentifier() {
		return bgpPeerIdentifier;
	}
	public void setBgpPeerIdentifier(SnmpDataType bgpPeerIdentifier) {
		this.bgpPeerIdentifier = bgpPeerIdentifier;
	}
	public SnmpDataType getBgpPeerState() {
		return bgpPeerState;
	}
	public void setBgpPeerState(SnmpDataType bgpPeerState) {
		this.bgpPeerState = bgpPeerState;
	}
	public SnmpDataType getBgpPeerNegotiatedVersion() {
		return bgpPeerNegotiatedVersion;
	}
	public void setBgpPeerNegotiatedVersion(SnmpDataType bgpPeerNegotiatedVersion) {
		this.bgpPeerNegotiatedVersion = bgpPeerNegotiatedVersion;
	}
	public SnmpDataType getBgpPeerRemoteAddr() {
		return bgpPeerRemoteAddr;
	}
	public void setBgpPeerRemoteAddr(SnmpDataType bgpPeerRemoteAddr) {
		this.bgpPeerRemoteAddr = bgpPeerRemoteAddr;
	}
	public SnmpDataType getBgpPeerRemoteAs() {
		return bgpPeerRemoteAs;
	}
	public void setBgpPeerRemoteAs(SnmpDataType bgpPeerRemoteAs) {
		this.bgpPeerRemoteAs = bgpPeerRemoteAs;
	}
	public SnmpDataType getBgpPeerInUpdates() {
		return bgpPeerInUpdates;
	}
	public void setBgpPeerInUpdates(SnmpDataType bgpPeerInUpdates) {
		this.bgpPeerInUpdates = bgpPeerInUpdates;
	}
	public SnmpDataType getBgpPeerOutUpdates() {
		return bgpPeerOutUpdates;
	}
	public void setBgpPeerOutUpdates(SnmpDataType bgpPeerOutUpdates) {
		this.bgpPeerOutUpdates = bgpPeerOutUpdates;
	}
	public SnmpDataType getBgpPeerKeepAlive() {
		return bgpPeerKeepAlive;
	}
	public void setBgpPeerKeepAlive(SnmpDataType bgpPeerKeepAlive) {
		this.bgpPeerKeepAlive = bgpPeerKeepAlive;
	}
	
	public static String getState(SnmpInt val) {
		String ret = null;
		switch(val.getValue()){
			case 1:
				ret ="Idle";
				break;
			case 2: 
				ret = "Connect";
				break;
			case 3: 
				ret = "Active";
				break;
			case 4: 
				ret = "OpenSent";
				break;
			case 5: 
				ret = "OpenConfirm";
				break;
			case 6: 
				ret = "Established";
				break;
		}
		return ret;
			
	}
	


}

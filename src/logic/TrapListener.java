package logic;

import java.io.IOException;

import com.ireasoning.protocol.Listener;
import com.ireasoning.protocol.Msg;
import com.ireasoning.protocol.snmp.SnmpTrapdSession;
import com.ireasoning.protocol.snmp.SnmpOID;
import com.ireasoning.protocol.snmp.SnmpTrap;;

public class TrapListener implements Listener{

	public static void main(String[] args) {
		
	
		TrapListener t = new TrapListener();
		t.trapd();

	}

	@Override
	public void handleMsg(Object arg0, Msg arg1) {
		int niz[] = {1,3,6,1,2,1,15,3,1,10}; //len 10
		int niz2[] = {1,3,6,1,2,1,15,3,1,14};
		int tes[] = {1,3,6,1,6,3,1,1,4,1,0}; //ll
		int test[] = {1,3,6,1,2,1,15,7,2};
		SnmpOID jel = new SnmpOID(niz);
		SnmpOID jel2 = new SnmpOID(niz2);
		SnmpOID jel3 = new SnmpOID(test);
		if((((SnmpTrap)arg1).getSnmpTrapOID()).compareTo(jel3, 9)==0) {
			System.out.println(arg1.toString() + "ESAM REKO)))))))))))))))))))))))))))))))))))))");
			System.out.println((((SnmpTrap)arg1).getIpAddress()) + "ETO TIIIIIIIIIIIIIIIIIII");
			}
		//compareTo(jel,10)==0
		System.out.println(arg1.toString());
		System.out.println("++++++++++++++++++++++++++++");
	}
	
	
	public void trapd() {
		try
        {
            System.out.println( "Trap receiver listening on port: " + 1031);
            SnmpTrapdSession session = new SnmpTrapdSession(1031);
            
            
            //register for trap event, so handleMsg will be called when trap comes
            session.addListener(this);
            
            //blocks and wait for traps
            session.waitForTrap();
        }
        catch(IOException e)
        {
            System.out.println(e);
            e.printStackTrace();
        }
	}
	
	

}

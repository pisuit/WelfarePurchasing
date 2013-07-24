
package welfare.security.ldap;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPMessage;
import com.novell.ldap.LDAPSearchQueue;
import com.novell.ldap.LDAPSearchResult;
import com.novell.ldap.LDAPSearchResults;

/*
 * Created on 10 มิ.ย. 2550
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author manop
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class LDAPConnect {

	//private String ldapHost = "mkfs01.aerothai.co.th";
	private String ldapHost = "172.16.24.13";
	private int ldapPort = LDAPConnection.DEFAULT_PORT;
	private int ldapVersion = LDAPConnection.LDAP_V3;
	private LDAPConnection connection;
	
	public LDAPConnect() {
		connection = new LDAPConnection();
	}
		
	public void disconnect() {
		try {
			connection.disconnect();
		} catch (LDAPException e) {
			e.printStackTrace();
		}
	}

	private Vector getAttribute(String entryDN,String attr){
		Vector v=new Vector();
		LDAPSearchResults searchResults;		
		LDAPEntry entry = null;
		try    {
			searchResults =
				connection.search(  entryDN,	LDAPConnection.SCOPE_BASE, "",null, false);
				entry = searchResults.next();
		}catch(LDAPException e) {
				//e.printStackTrace();
			return v;
		}

		//System.out.println(entry);
		LDAPAttributeSet attributeSet = entry.getAttributeSet();
		//System.out.println(attributeSet);
		Iterator allAttributes = attributeSet.iterator();

		while(allAttributes.hasNext()) {
				LDAPAttribute attribute =
							(LDAPAttribute)allAttributes.next();
			 String attributeName = attribute.getName();
			 if (attributeName.equalsIgnoreCase(attr)) {
				Enumeration en = attribute.getStringValues();
				for (;en.hasMoreElements();){
					v.add(en.nextElement());
				}
			 }
		}
		return v;
	}

	public LDAPUser login(String aUserName, String aPassword) throws InvalidLoginException {
		LDAPUser onlineUser = null;
		try {
			connection.connect(ldapHost, ldapPort);
			System.out.println("Connect Successfull");
			System.out.println(aUserName);
			LDAPSearchQueue searchResults = connection.search("o=Aerothai",LDAPConnection.SCOPE_SUB,"cn="+aUserName,new String[]{ LDAPConnection.NO_ATTRS},true, (LDAPSearchQueue)null);
			LDAPMessage message ;
			message = searchResults.getResponse();
			if ( message instanceof LDAPSearchResult  ) {
				LDAPEntry entry =  ((LDAPSearchResult)message).getEntry();
				String dn = entry.getDN();
				String[] userDn = dn.split(",");
				String fullDn = userDn[0] + "," + userDn[1] + "," + userDn[2] + ",o=Aerothai";
				connection.bind(ldapVersion, fullDn, aPassword.getBytes("UTF8"));
				System.out.println("Bind Successfull");
				onlineUser = new LDAPUser();
				try {
					onlineUser.setFirstName((String)getAttribute(fullDn,"givenName").elementAt(0));
					onlineUser.setLastName((String)getAttribute(fullDn,"sn").elementAt(0));
					try {
						Integer.parseInt((String)getAttribute(fullDn,"cn").elementAt(1));
						onlineUser.setEmployeeCode((String)getAttribute(fullDn,"cn").elementAt(1));
					} catch (NumberFormatException e) {
						onlineUser.setEmployeeCode((String)getAttribute(fullDn,"cn").elementAt(0));
					}
					onlineUser.setDepartment( (String) getAttribute(fullDn,"ou").elementAt(0));
					onlineUser.setLocation( (userDn[2].split("="))[1] );
				}	catch (ArrayIndexOutOfBoundsException e1) {
						e1.printStackTrace();
						onlineUser = null;
						throw new InvalidLoginException("เกิดความผิดพลาดระหว่างการดึงข้อมูล กรุณา Login ใหม่อีกครั้ง");			
				}
			} else {
				disconnect();
				throw new InvalidLoginException("ไม่พบผู้ใช้งานชื่อ "+aUserName+" กรุณาลองอีกครั้ง หรือ ติดต่อเจ้าหน้าที่กองวค.พว. เพื่อตรวจสอบข้อมูลของท่าน");
			}
			disconnect();
		} catch (LDAPException e) {
			e.printStackTrace();
			throw new InvalidLoginException("รหัสผ่านไม่ถูกต้อง กรุณาลองอีกครั้ง หรือ ติดต่อเจ้าหน้าที่กองวค.พว. เพื่อตรวจสอบข้อมูลของท่าน");			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new InvalidLoginException(e);			
		} finally {
			disconnect();
		}
		return onlineUser;
	}

	public static void main(String[] args) {
	}

}

package com.jtang.syn.netty;

import java.util.Vector;

import com.jtang.common.table.SourceInfo;
import com.jtang.common.table.TableInfo;
import com.jtang.common.table.TableItem;

public class ClientManager {

	private Vector<ClientDescription> clients = new Vector<ClientDescription>();
	private int size = 0;

	//初始化，将本地节点加入到节点中
	public ClientManager(int port){
		ClientDescription cds= new ClientDescription();
		cds.setWatchIp("127.0.0.1");
		cds.setWatchPort(port);
		this.addClient(cds);
	}
	public Vector<ClientDescription> getClients() {
		return clients;
	}

	public void setClients(Vector<ClientDescription> clients) {
		this.clients = clients;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void addClient(ClientDescription client) {

		if (this.isExistClient(client))
			return;
		this.clients.add(client);
		this.size++;
	}

	public boolean isExistClient(ClientDescription client) {

		for (int i = 0; i < size; i++) {
			if (this.clients.get(i).equals(client)) {
				return true;
			}

		}
		return false;
	}

	public ClientDescription getClientByTableItem(TableItem tableItem) {

		SourceInfo sourceInfo = tableItem.getSourceInfo();
		int size = this.clients.size();
		for (int i = 0; i < size; i++) {

			ClientDescription client = this.clients.get(i);
			int j = client.getDBs().size();
			for (int n = 0; n < j; n++) {
				
				DBDescription db = client.getDBs().get(n);
				if (db.getIp().equals(sourceInfo.getIp())
						&& db.getPort() == sourceInfo.getPort()
						&& db.getDBName().equalsIgnoreCase(
								sourceInfo.getDBName())) {
					return client;
				}
				
			}

		}
		return null;
	}

	public ClientDescription addClientByTableItem(TableItem tableItem){
		
		if(tableItem==null){
			return null;
		}
		SourceInfo sourceInfo = tableItem.getSourceInfo();
		TableInfo tableInfo = tableItem.getTableInfo();
		//得到clientDescription
		ClientDescription myClient = null;
		int cSize = this.clients.size();
		for(int i=0;i<cSize;i++){
			ClientDescription client = this.clients.get(i);
			if(client.getWatchIp().equalsIgnoreCase(sourceInfo.getIp())){
				myClient=client;
				break;
			}
		}
		if(myClient==null){
			myClient = new ClientDescription();
			myClient.setWatchIp(sourceInfo.getIp());
			myClient.setWatchPort(8880);
			this.clients.add(myClient);
		}
		//得到DBdescription 
		int dSize = myClient.getDBs().size();
		DBDescription myDb = null;
		for(int j=0;j<dSize;j++){
			DBDescription dbd = myClient.getDBs().get(j);
			if(dbd.getIp().equalsIgnoreCase(sourceInfo.getIp())&&
					dbd.getPort()==sourceInfo.getPort()&&
					dbd.getDBName().equalsIgnoreCase(sourceInfo.getDBName())&&
					dbd.getUserName().equals(sourceInfo.getUserName())){
				 myDb = dbd;
			}
		}
		if(myDb==null){
			myDb = new DBDescription();
			myDb.setIp(sourceInfo.getIp());
			myDb.setPort(sourceInfo.getPort());
			myDb.setDBName(sourceInfo.getDBName());
			myDb.setUserName(sourceInfo.getUserName());
			myClient.getDBs().add(myDb);
		}
		//增加tableInfo 
		myDb.addTable(tableInfo);
		return myClient;
	}
	
	public ClientDescription getClientByIp(String ip){
		
		int size = this.clients.size();
		for (int i = 0; i < size; i++) {

			ClientDescription client = this.clients.get(i);
			if(client.getWatchIp().equalsIgnoreCase(ip)){
				return client;
			}
		}
		return null;
		
	}
}

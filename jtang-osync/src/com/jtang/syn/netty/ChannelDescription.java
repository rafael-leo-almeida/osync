package com.jtang.syn.netty;

import org.jboss.netty.channel.Channel;
public class ChannelDescription {

	private Channel channel;
	private String ip;
	private int port;
	public Channel getChannel() {
		return channel;
	}
	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
       if(!(obj instanceof ChannelDescription))
	      return false;
       ChannelDescription channel = (ChannelDescription)obj;
       if(channel.getIp().equals(this.getIp())&&channel.getPort()==this.getPort())
    	   return true;
       else
    	   return false;
	}
	
	
	
	
}

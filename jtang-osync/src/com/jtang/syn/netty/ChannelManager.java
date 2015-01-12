package com.jtang.syn.netty;

import java.net.InetSocketAddress;
import java.util.Vector;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class ChannelManager {

	private Vector<ChannelDescription> channels = new Vector<ChannelDescription>();

	public Vector<ChannelDescription> getChannels() {
		return channels;
	}

	public void setChannels(Vector<ChannelDescription> channels) {
		this.channels = channels;
	}
	
    public boolean isExistChannel(ChannelDescription channel){
    	
    	int size = this.channels.size();
    	for(int i=0;i<size;i++){
    		
    		if(this.getChannels().equals(channel)){
    			return true;
    		}
    	}
    		
    	return false;
    }
	
    public void addChannel(ChannelDescription channel){
    	if(this.isExistChannel(channel))
    		return;
    	this.channels.add(channel);
    }
    
    public Channel getChannelByIpAndPort(String ip,int port){
    	
    	int size = this.channels.size();
    	ChannelDescription cd = null;
    	int item = -1;
    	for(int i=0;i<size;i++){
    		if(this.channels.get(i).getIp().equals(ip)&&this.channels.get(i).getPort()==port){
    			cd = this.channels.get(i);
    			item = i;
    			break;
    		}
    	}	
    	if(cd!=null&&cd.getChannel().isConnected()){
    		return cd.getChannel();
    	}
    	ClientBootstrap bootstrap = new ClientBootstrap(
				new NioClientSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));
		// Configure the pipeline factory.
		bootstrap.setPipelineFactory(new SynClientPipelineFactory());
		// Start the connection attempt.
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip,
				port));
		// Wait until the connection attempt succeeds or fails.
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return null;
		}
		ChannelDescription c = new ChannelDescription();
		c.setChannel(channel);c.setIp(ip);c.setPort(port);
		if(cd!=null){
			this.channels.get(item).setChannel(channel);
		}else{
			this.addChannel(c);
		}
    	return channel;
    	
    }
    
	
}

/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.jtang.syn.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.TenantManager;

/**
 * Simplistic telnet server.
 */
public class SynServer extends Thread{

    private final int port;
    private ConcurrentLinkedQueue<String> jsonStrings;
    private TableMapManager tableMaps;
    private String url;
    private String userName;
    private String password;
    private TenantManager tanentPool;
    public SynServer(int port,ConcurrentLinkedQueue<String> jsonStrings) {
        this.port = port;
        this.jsonStrings = jsonStrings;
        if(this.jsonStrings==null)
        	this.jsonStrings = new ConcurrentLinkedQueue<String>();
    }
    public SynServer(int port,ConcurrentLinkedQueue<String> jsonStrings,TableMapManager tableMaps,String url,
    		String userName,String password){
    	
    	  this.port = port;
          this.jsonStrings = jsonStrings;
          if(this.jsonStrings==null)
          	this.jsonStrings = new ConcurrentLinkedQueue<String>();
          this.tableMaps=tableMaps;
          this.url=url;
          this.userName=userName;
          this.password=password;
    }
    
    public SynServer(int port,ConcurrentLinkedQueue<String> jsonStrings,TableMapManager tableMaps,
    		TenantManager tanentPool){
    	
    	  this.port = port;
          this.jsonStrings = jsonStrings;
          if(this.jsonStrings==null)
          	this.jsonStrings = new ConcurrentLinkedQueue<String>();
          this.tableMaps=tableMaps;
          this.tanentPool=tanentPool;
    }
    
    public void run() {
        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Configure the pipeline factory.
        bootstrap.setPipelineFactory(new SynServerPipelineFactory(this.jsonStrings,this.tableMaps,this.tanentPool));

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(port));
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8888;
        }
        new SynServer(port,null).run();
    }
}

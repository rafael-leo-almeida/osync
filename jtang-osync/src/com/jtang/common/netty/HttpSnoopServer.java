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
package com.jtang.common.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.jtang.common.tenant.TenantManager;
import com.jtang.monitor.manager.Command;

/**
 * An HTTP server that sends back the content of the received HTTP request
 * in a pretty plaintext form.
 */
public class HttpSnoopServer extends Thread{

    private final int port;
    private TenantManager tanentPool;
    private LinkedBlockingQueue<Command> commandQueue;
    
    public HttpSnoopServer(int port) {
        this.port = port;
    }

    public HttpSnoopServer(int port,TenantManager tanentPool,LinkedBlockingQueue<Command> commandQueue){
    	this.port=port;
    	this.tanentPool = tanentPool;
    	this.commandQueue=commandQueue;
    }
    public void run() {
        // Configure the server.
    	System.out.println("netty interface starting");
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the event pipeline factory.
        if(this.tanentPool==null)
           bootstrap.setPipelineFactory(new HttpSnoopServerPipelineFactory());
        else
        	bootstrap.setPipelineFactory(new HttpSnoopServerPipelineFactory(tanentPool,this.commandQueue));
        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(port));
    }

    public static void main(String[] args) {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8081;
        }
        new HttpSnoopServer(port).run();
    }

}

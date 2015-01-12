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

import static org.jboss.netty.channel.Channels.pipeline;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import com.jtang.common.table.TableMapManager;
import com.jtang.common.tenant.TenantManager;

/**
 * Creates a newly configured {@link ChannelPipeline} for a new channel.
 */
public class SynServerPipelineFactory implements
        ChannelPipelineFactory {
    ConcurrentLinkedQueue<String> jsonStrings;
    TableMapManager tableMaps;
    private String url;
    private String userName;
    private String password;
    private TenantManager tanentPool;
    public SynServerPipelineFactory(ConcurrentLinkedQueue<String> jsonStrings){
    	this.jsonStrings = jsonStrings;
    }
    public SynServerPipelineFactory(ConcurrentLinkedQueue<String> jsonStrings,TableMapManager tableMaps ,String url,String userName,String password){
    	
    	this.jsonStrings=jsonStrings;
    	this.tableMaps=tableMaps;
    	this.url=url;
    	this.userName=userName;
    	this.password=password;
    	
    }
    
  public SynServerPipelineFactory(ConcurrentLinkedQueue<String> jsonStrings,TableMapManager tableMaps ,
		  TenantManager tanentPool){
    	
    	this.jsonStrings=jsonStrings;
    	this.tableMaps=tableMaps;
    	this.tanentPool = tanentPool;
    	
    }
    
    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();

        // Add the text line codec combination first,
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(
                8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        // and then business logic.
        pipeline.addLast("handler", new SynServerHandler(this.jsonStrings,this.tableMaps,this.tanentPool));

        return pipeline;
    }
}

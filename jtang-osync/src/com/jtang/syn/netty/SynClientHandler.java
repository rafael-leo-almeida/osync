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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.jtang.common.record.RecordDaoImpl;
import com.jtang.syn.comm.ReturnMsg;

/**
 * Handles a client-side channel.
 */
public class SynClientHandler extends SimpleChannelUpstreamHandler {

    private static final Logger logger = Logger.getLogger(
            SynClientHandler.class.getName());

    private RecordDaoImpl recordDao = new RecordDaoImpl();
    @Override
    public void handleUpstream(
            ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
        if (e instanceof ChannelStateEvent) {
            logger.info(e.toString());
        }
        super.handleUpstream(ctx, e);
    }

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        // Print out the line received from the server.
        String returnMsgJson = e.getMessage().toString();
        System.out.println("receiveReturnMsg:"+returnMsgJson);
        if(StringUtils.isBlank(returnMsgJson)){
        	return;
        }
        if(!returnMsgJson.startsWith("{")){
        	return;
        }
        ReturnMsg rm = new ReturnMsg(returnMsgJson);
        if(rm!=null&&rm.getRecordId()!=-1){
        	if(rm.getStatus()==0){
        		this.recordDao.updateRTStatusById(rm.getRecordId(), new Date(rm.getReceiveTime()),
        				0);
        	}else if(rm.getStatus()==1){
            	this.recordDao.updateRTStatusTypeById(rm.getRecordId(), new Date(rm.getReceiveTime()),
            			1, 0);
        	}else if(rm.getStatus()==2){
        		this.recordDao.updateRTStatusTypeById(rm.getRecordId(), new Date(rm.getReceiveTime()),
            			1, 1);
        	}else{
        		this.recordDao.updateRTStatusTypeById(rm.getRecordId(), new Date(rm.getReceiveTime()),
            			1, 2);
        	}
        }
        
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }
}

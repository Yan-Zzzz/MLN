/**
  * Created by MomoLuaNative.
  * Copyright (c) 2019, Momo Group. All rights reserved.
  *
  * This source code is licensed under the MIT.
  * For the full copyright and license information,please view the LICENSE file in the root directory of this source tree.
  */
package com.immomo.luanative.hotreload.io.impl;

import com.immomo.luanative.hotreload.io.iWriter;
import com.immomo.luanative.codec.encode.EncoderFactory;
import com.immomo.luanative.codec.encode.iEncoder;
import com.immomo.luanative.codec.PBCommandFactory;
import com.immomo.luanative.codec.protobuf.PBDeviceCommand;
import com.immomo.luanative.codec.protobuf.PBErrorCommand;
import com.immomo.luanative.codec.protobuf.PBLogCommand;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class WriterImpl implements iWriter {

    private iEncoder encoder = EncoderFactory.getInstance();
    private BlockingQueue<Object> msgQueue = new LinkedBlockingDeque<>();

    private byte[] popMsg() {
        try {
            return (byte[])msgQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void pushMsg(byte[] msg) {
        try {
            msgQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(byte[] data) {

    }

    @Override
    public void writeLog(String log, String entryFilePath) {
        PBLogCommand.pblogcommand cmd = (PBLogCommand.pblogcommand) PBCommandFactory.getLogCommand(log, entryFilePath);
        pushMsg(encoder.encode(cmd));
    }

    @Override
    public void writeError(String error, String entryFilePath) {
        PBErrorCommand.pberrorcommand cmd = (PBErrorCommand.pberrorcommand) PBCommandFactory.getErrorCommand(error, entryFilePath);
        pushMsg(encoder.encode(cmd));
    }

    @Override
    public void writeDevice() {
        PBDeviceCommand.pbdevicecommand cmd = (PBDeviceCommand.pbdevicecommand) PBCommandFactory.getDeviceCommand();
        pushMsg(encoder.encode(cmd));
    }

    @Override
    public byte[] popData() {
        return popMsg();
    }
}
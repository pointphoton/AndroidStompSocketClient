package com.example.socket.pathmatcher;

import com.example.dlog.DLog;
import com.example.socket.dto.StompHeader;
import com.example.socket.dto.StompMessage;

public class SimplePathMatcher implements PathMatcher {

    @Override
    public boolean matches(String path, StompMessage msg) {
        DLog.write("path= " + path + " msg= " + msg);
        String dest = msg.findHeader(StompHeader.DESTINATION);
        if (dest == null) return false;
        else return path.equals(dest);
    }
}
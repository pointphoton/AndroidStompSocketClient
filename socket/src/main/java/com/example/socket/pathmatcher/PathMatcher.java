package com.example.socket.pathmatcher;

import com.example.socket.dto.StompMessage;

public interface PathMatcher {

    boolean matches(String path, StompMessage msg);
}
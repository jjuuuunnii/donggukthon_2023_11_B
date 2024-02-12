package com.cafevery.security.info.response;


import com.cafevery.dto.type.ESocialType;

public interface UserResponse {

    ESocialType supportServer();

    String getId();

    String getName();

    Class<? extends UserResponse> getImplementationClass();
}

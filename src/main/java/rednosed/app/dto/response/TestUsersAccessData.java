package rednosed.app.dto.response;

import lombok.Builder;

@Builder
public record TestUsersAccessData(
        String test1AccessToken,
        String test2AccessToken
) {
}

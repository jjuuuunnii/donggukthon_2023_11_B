package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import javax.annotation.Nullable;
import java.util.List;

@Builder
public record StampSingleInfoDto(

        @NotNull(message = "우표의 닉네임은 null이 될 수 없습니다.")
        String nickname,

        @NotNull(message = "우표의 좋아요 수는 null이 될 수 없습니다.")
        int likeCnt,

        @NotNull(message = "우표의 좋아요는 null이 될 수 없습니다.")
        boolean like,

        @NotNull(message = "우표의 닉네임은 null이 될 수 없습니다.")
        String date,

        @Nullable
        List<String> friendList,

        @NotNull(message = "이미지는 null이 될 수 없습니다.")
        String ImgUrl
) {
}

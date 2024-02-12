package rednosed.app.dto.response;

import lombok.Builder;

@Builder
public record SealLikeDataTmpDto(
        String sealId,
        String sealImgUrl,
        String sealName,
        long likeCount
) {
}

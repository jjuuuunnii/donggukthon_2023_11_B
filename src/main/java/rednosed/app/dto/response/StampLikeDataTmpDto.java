package rednosed.app.dto.response;

import lombok.Builder;

@Builder
public record StampLikeDataTmpDto(
        String stampId,
        String stampImgUrl,
        String stampName,
        long likeCount
) {}

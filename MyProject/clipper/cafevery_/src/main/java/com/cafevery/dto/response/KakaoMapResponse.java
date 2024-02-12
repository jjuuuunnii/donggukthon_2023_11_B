package com.cafevery.dto.response;

import java.util.List;

public record KakaoMapResponse(Meta meta, List<Document> documents) {

    public record Meta(Integer total_count, Integer pageable_count, Boolean is_end, SameName same_name) {
    }

    public record SameName(List<String> region, String keyword, String selected_region) {
    }

    public record Document(
            String id,
            String place_name,
            String category_name,
            String category_group_code,
            String category_group_name,
            String phone,
            String address_name,
            String road_address_name,
            String x,
            String y,
            String place_url,
            String distance
    ) {
    }
}
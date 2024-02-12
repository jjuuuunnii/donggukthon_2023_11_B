package com.cafevery.utility;

import com.cafevery.dto.type.EDay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScheduleParser {

    private static final Pattern PATTERN = Pattern.compile(
            "([월화수목금토일])~?([월화수목금토일])?\\s(\\d{1,2}:\\d{2})\\s~\\s(\\d{1,2}:\\d{2})",
            Pattern.UNICODE_CHARACTER_CLASS
    );

    public Map<EDay, String> parseSchedule(String scheduleStr) {
        Map<EDay, String> scheduleMap = new EnumMap<>(EDay.class);

        Matcher matcher = PATTERN.matcher(scheduleStr);
        while (matcher.find()) {
            EDay startDay = EDay.fromString(matcher.group(1));
            EDay endDay = matcher.group(2) == null ? startDay : EDay.fromString(matcher.group(2));
            String startTime = matcher.group(3);
            String endTime = matcher.group(4);

            if (startDay != null) {
                if (startDay == endDay) {
                    scheduleMap.put(startDay, startTime + " ~ " + endTime);
                } else {
                    EDay[] days = EDay.values();
                    for (EDay day : days) {
                        if (day.getNumber() >= startDay.getNumber() && day.getNumber() <= endDay.getNumber()) {
                            scheduleMap.put(day, startTime + " ~ " + endTime);
                        }
                    }
                }
            }
        }

        return scheduleMap;
    }
}

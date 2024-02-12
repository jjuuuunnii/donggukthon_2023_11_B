package com.cafevery.service;

import com.cafevery.dto.response.ApiResponseDto;
import com.cafevery.dto.response.KakaoMapResponse;
import com.cafevery.dto.type.EDay;
import com.cafevery.repository.CafeRepository;
import com.cafevery.utility.WebClientUtil;
import com.cafevery.utility.WebCrawlingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CafeService {

    private final CafeRepository cafeRepository;
    private final WebClientUtil webClientUtil;
    private final WebCrawlingUtil webCrawlingUtil;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoApiKey;

    @Value("${kakao.api.address-url}")
    private String geocodingApiUrl;

    @Value("${kakao.api.keyword-url}")
    private String keywordSearchApiUrl;


    public void makeLocationOfCafeSync() {
        try {
            ClassPathResource resource = new ClassPathResource("cafe_data_org.csv");
            try (
                    Reader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("cafe_data_org.csv").getInputStream()));
                    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("src/main/resources/cafe_data.csv")));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Name", "Address", "PhoneNumber", "Latitude", "Longitude"))
            ) {
                String requestApiKey = "KakaoAK " + kakaoApiKey;
                Map<String, String> headers = Map.of("Authorization", requestApiKey);

                for (CSVRecord csvRecord : csvParser) {
                    String name = csvRecord.get("name");
                    String address = csvRecord.get("address");
                    String phoneNumber = csvRecord.get("phoneNumber");

                    int commaIndex = address.indexOf(",");
                    if (commaIndex != -1) {
                        address = address.substring(0, commaIndex);
                    }

                    String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);

                    ApiResponseDto apiResponseDto = webClientUtil.get(geocodingApiUrl, ApiResponseDto.class, headers, Map.of("address", encodedAddress));
                    if (apiResponseDto.documents().isEmpty()) {
                        log.error("name: {}, address: {}, phoneNumber: {}", name, address, phoneNumber);
                        continue;
                    }
                    double longitude = Double.parseDouble(apiResponseDto.documents().get(0).x());
                    double latitude = Double.parseDouble(apiResponseDto.documents().get(0).y());
                    log.info("name: {}, address: {}, phoneNumber: {}, latitude: {}, longitude: {}", name, address, phoneNumber, latitude, longitude);

                    csvPrinter.printRecord(name, address, phoneNumber, latitude, longitude);
                }

                csvPrinter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getCafeBusinessHour() {

        try (
                Reader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("cafe_data.csv").getInputStream()));
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/kangseung1110/cafe_data_with_business_hour.csv")));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Name", "Address", "PhoneNumber", "Latitude", "Longitude", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"))
        ) {
//            String requestApiKey = "KakaoAK " + kakaoApiKey;
//            Map<String, String> headers = Map.of("Authorization", requestApiKey);
//            String test = URLEncoder.encode("이디야커피", StandardCharsets.UTF_8);
//            KakaoMapResponse kakaoMapResponse = webClientUtil.get(keywordSearchApiUrl, KakaoMapResponse.class, headers, Map.of("query", test, "x", "128.961082612203", "y", "35.1037845190945"));
//            log.info("requestUrl: {}", kakaoMapResponse.documents().get(0).place_url());
//            Map<EDay, String> timeData = webCrawlingUtil.getTimeData(kakaoMapResponse.documents().get(0).place_url());
            for (CSVRecord csvRecord : csvParser) {
                String name = csvRecord.get("Name");
                String address = csvRecord.get("Address");
                String phoneNumber = csvRecord.get("PhoneNumber");
                String latitude = csvRecord.get("Latitude");
                String longitude = csvRecord.get("Longitude");

                String requestApiKey = "KakaoAK " + kakaoApiKey;
                Map<String, String> headers = Map.of("Authorization", requestApiKey);
                String cafeName = URLEncoder.encode(name, StandardCharsets.UTF_8);
                KakaoMapResponse kakaoMapResponse = webClientUtil.get(
                        keywordSearchApiUrl, KakaoMapResponse.class, headers, Map.of("query", cafeName, "x", longitude, "y", latitude));
                if(kakaoMapResponse.documents().isEmpty()) {
                    continue;
                }
                log.info("requestUrl: {}", kakaoMapResponse.documents().get(0).place_url());
                Map<EDay, String> timeData = webCrawlingUtil.getTimeData(kakaoMapResponse.documents().get(0).place_url());
                if(timeData == null) {
                    continue;
                }

                csvPrinter.printRecord(name, address, phoneNumber, latitude, longitude,
                        timeData.get(EDay.MONDAY),
                        timeData.get(EDay.TUESDAY),
                        timeData.get(EDay.WEDNESDAY),
                        timeData.get(EDay.THURSDAY),
                        timeData.get(EDay.FRIDAY),
                        timeData.get(EDay.SATURDAY),
                        timeData.get(EDay.SUNDAY));
            }
            csvPrinter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


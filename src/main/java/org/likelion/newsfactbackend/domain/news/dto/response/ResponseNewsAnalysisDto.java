package org.likelion.newsfactbackend.domain.news.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseNewsAnalysisDto {
    private List<Map<String, Object>> analysisResults;
    private Map<String, Double> wordCloudResults;
}

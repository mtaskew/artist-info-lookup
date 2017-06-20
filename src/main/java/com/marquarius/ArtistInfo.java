package com.marquarius;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.marquarius.request.Request;
import com.marquarius.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


/**
 * Created by marquariusaskew on 6/14/17.
 */
public class ArtistInfo implements RequestHandler<Request, Response> {
    private RestTemplate restTemplate = new RestTemplate();
    private Logger logger = LoggerFactory.getLogger(ArtistInfo.class);

    public Response handleRequest(Request request, Context context) {
        String artistName = request.getCurrentIntent().getSlots().getArtistName();
        logger.info("Finding the info for [" + artistName + "].");
        String url = buildUrl(artistName);
        String message;

        try {
            ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
            String type = request.getCurrentIntent().getSlots().getType();
            Map<String, Object> bandsInTownMap = responseEntity.getBody();

            if("Business".equalsIgnoreCase(type)) {
                String responseUrl = (String) bandsInTownMap.get("url");
                message = "Bands in town is usually a good place to find business info. Check it out " + responseUrl;

            } else {
                String responseUrl = (String) bandsInTownMap.get("facebook_page_url");
                if("Personal".equalsIgnoreCase(type)) {
                    message = "Facebook is the spot for personal info these days. You should start there " + responseUrl;

                } else {
                    message = "Umm... I really don't know what you're looking for here. Why don't you try Facebook? Everything is on there " +
                            "these days :). " + responseUrl;
                }
            }

        } catch(Exception e) {
            logger.error("Unable to handle this request. ", e);
            message = "Come on son, my contacts don't deal with local artists :)";
        }

        return Response.generateInfoUrlResponse(message);
    }

    private String buildUrl(String artistName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("https://rest.bandsintown.com/artists/")
                .append(artistName)
                .append("?app_id=artistInfo");

        return stringBuilder.toString();
    }
}

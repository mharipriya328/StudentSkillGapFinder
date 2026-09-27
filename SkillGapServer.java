import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SkillGapServer {

    static Map<String, List<String>> careerSkills = new HashMap<>();

    public static void main(String[] args) throws Exception {

        // Required skills for each career

        careerSkills.put("data_analyst",
                Arrays.asList(
                        "python",
                        "sql",
                        "excel",
                        "powerbi",
                        "statistics"
                ));

        careerSkills.put("data_scientist",
                Arrays.asList(
                        "python",
                        "sql",
                        "statistics"
                ));

        careerSkills.put("web_developer",
                Arrays.asList(
                        "javascript",
                        "html",
                        "css"
                ));

        careerSkills.put("cloud_engineer",
                Arrays.asList(
                        "python",
                        "java"
                ));


        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        server.createContext("/skillgap", SkillGapServer::handleSkillGap);

        server.createContext("/health", SkillGapServer::handleHealth);

        server.setExecutor(null);

        System.out.println("=================================");
        System.out.println(" Student Skill Gap Finder");
        System.out.println(" Java Backend Started");
        System.out.println(" Server: http://localhost:8080");
        System.out.println("=================================");

        server.start();
    }


    static void handleHealth(HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        String response = "{\"status\":\"Java backend is running\"}";

        sendResponse(exchange, response);
    }


    static void handleSkillGap(HttpExchange exchange) throws IOException {

        addCorsHeaders(exchange);

        String query = exchange.getRequestURI().getQuery();

        Map<String, String> parameters = parseQuery(query);

        String career = parameters.get("career");

        String skillsText = parameters.get("skills");

        if (career == null) {
            sendResponse(
                    exchange,
                    "{\"error\":\"Career is required\"}"
            );
            return;
        }


        List<String> userSkills = new ArrayList<>();

        if (skillsText != null && !skillsText.isEmpty()) {

            String[] skills = skillsText.split(",");

            for (String skill : skills) {
                userSkills.add(skill.toLowerCase());
            }
        }


        List<String> requiredSkills =
                careerSkills.get(career);


        if (requiredSkills == null) {

            sendResponse(
                    exchange,
                    "{\"error\":\"Unknown career\"}"
            );

            return;
        }


        List<String> missingSkills = new ArrayList<>();

        for (String skill : requiredSkills) {

            if (!userSkills.contains(skill)) {

                missingSkills.add(skill);
            }
        }


        StringBuilder json = new StringBuilder();

        json.append("{");

        json.append("\"career\":\"")
                .append(career)
                .append("\",");

        json.append("\"missingSkills\":[");


        for (int i = 0; i < missingSkills.size(); i++) {

            json.append("\"")
                    .append(missingSkills.get(i))
                    .append("\"");

            if (i < missingSkills.size() - 1) {
                json.append(",");
            }
        }


        json.append("]");

        json.append("}");


        sendResponse(exchange, json.toString());
    }


    static Map<String, String> parseQuery(String query) {

        Map<String, String> result = new HashMap<>();

        if (query == null) {
            return result;
        }


        String[] pairs = query.split("&");


        for (String pair : pairs) {

            String[] parts = pair.split("=", 2);

            if (parts.length == 2) {

                String key =
                        URLDecoder.decode(
                                parts[0],
                                StandardCharsets.UTF_8
                        );

                String value =
                        URLDecoder.decode(
                                parts[1],
                                StandardCharsets.UTF_8
                        );

                result.put(key, value);
            }
        }


        return result;
    }


    static void addCorsHeaders(HttpExchange exchange) {

        exchange.getResponseHeaders()
                .add("Access-Control-Allow-Origin", "*");

        exchange.getResponseHeaders()
                .add("Access-Control-Allow-Methods", "GET");

        exchange.getResponseHeaders()
                .add("Access-Control-Allow-Headers", "*");

        exchange.getResponseHeaders()
                .add("Content-Type", "application/json");
    }


    static void sendResponse(
            HttpExchange exchange,
            String response
    ) throws IOException {

        byte[] bytes =
                response.getBytes(StandardCharsets.UTF_8);


        exchange.sendResponseHeaders(
                200,
                bytes.length
        );


        OutputStream output =
                exchange.getResponseBody();

        output.write(bytes);

        output.close();
    }
}
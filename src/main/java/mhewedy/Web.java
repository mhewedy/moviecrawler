package mhewedy;

import static spark.Spark.*;
import static spark.SparkBase.*;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;

/**
 * Created by mhewedy on 6/1/14.
 */
public class Web {

    public static void start() {

        String portEnv = Optional.ofNullable(System.getenv("PORT")).orElse("");
        if (portEnv.matches("\\d+")){
            setPort(Integer.parseInt(portEnv));
        }

        staticFileLocation("/public");

        get("/", (request, response) -> {
            response.redirect("/html/index.html");
            return "";
        });


        get("/process", (request, response) -> {
            response.header("content-type", "text/html; charset=utf-8");

            Optional<String> url = Optional.ofNullable(request.queryParams("url"));
            Optional<String> limit = Optional.ofNullable(request.queryParams("limit"));
            int iLimit = 40;

            if (limit.orElse("").matches("\\d+")) {
                iLimit = Integer.parseInt(limit.orElse(""));
            }

            if (url.isPresent()){
                try {
                    App.processCrawler(url.get(), iLimit, new PrintStream(response.raw().getOutputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                response.status(400);
            }
            return "";
        });

    }

	public static void main(String[] args) {
		Web.start();
	}
}

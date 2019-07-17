package com.oscar.controller.filter;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CorsFilter implements WebFilter {

    private static final String OPTION_METHOD = "OPTIONS";
    private static final String HEADER_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private static final String HEADER_ALLOW_CREDENTIALS_V = "true";
    private static final String HEADER_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    private static final String HEADER_ALLOW_HEADERS_V = "Content-Type";
    private static final String HEADER_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private static final String HEADER_ALLOW_METHODS_V = "OPTIONS,POST,GET,PUT,DELETE";
    private static final String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private static final String HEADER_ALLOW_ALL_V = "*";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_CACHE_CONTROL_V = "no-cache, no-store, must-revalidate";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_PRAGMA_V = "no-cache";
    private static final String HEADER_EXPIRES = "Expires";
    private static final String HEADER_EXPIRES_V = "0";

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange,
                             WebFilterChain webFilterChain) {
        HttpHeaders headers = serverWebExchange.getResponse().getHeaders();
        headers.add(HEADER_ALLOW_CREDENTIALS, HEADER_ALLOW_CREDENTIALS_V);
        headers.add(HEADER_ALLOW_HEADERS, HEADER_ALLOW_HEADERS_V);
        headers.add(HEADER_ALLOW_METHODS, HEADER_ALLOW_METHODS_V);
        headers.add(HEADER_ALLOW_ORIGIN, HEADER_ALLOW_ALL_V);
        headers.add(HEADER_CACHE_CONTROL, HEADER_CACHE_CONTROL_V);
        headers.add(HEADER_PRAGMA, HEADER_PRAGMA_V);
        headers.add(HEADER_EXPIRES, HEADER_EXPIRES_V);

        if (serverWebExchange.getRequest().getMethod().equals(OPTION_METHOD)) {
            serverWebExchange.getResponse().setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }
        
        return webFilterChain.filter(serverWebExchange);
    }
}

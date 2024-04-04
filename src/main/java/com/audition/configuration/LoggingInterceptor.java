package com.audition.configuration;

import com.audition.common.logging.AuditionLogger;
import java.io.IOException;
import java.nio.charset.Charset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    private transient AuditionLogger logger;
    private static final Logger LOG = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution)
        throws IOException {
        logRequest(request, body);
        final ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(final HttpRequest request, final byte[] body) throws IOException {
        logger.info(LOG, "===log request start===");
        logger.info(LOG, "URI: {}", request.getURI());
        logger.info(LOG, "Method: {}", request.getMethod());
        logger.info(LOG, "Headers: {}", request.getHeaders());
        logger.info(LOG, "Request body: {}", new String(body, "UTF-8"));
        logger.info(LOG, "===log request end===");
    }
    private void logResponse(final ClientHttpResponse response) throws IOException {
        logger.info(LOG, "===log response start===");
        logger.info(LOG, "Status code: {}", response.getStatusCode());
        logger.info(LOG, "Status text: {}", response.getStatusText());
        logger.info(LOG, "Headers: {}", response.getHeaders());
        logger.info(LOG, "Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
        logger.info(LOG, "===log response end===");
    }
}

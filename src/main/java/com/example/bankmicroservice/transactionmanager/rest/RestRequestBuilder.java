package com.example.bankmicroservice.transactionmanager.rest;

import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.Map;

@Getter
public class RestRequestBuilder<I, R> {

    private String endpointName;
    private I request;
    private final Class<R> responseType;
    private Map<String, String> headers;
    private Map<String, String> params;
    private final MediaType mediaType;

    private int timeout;

    public RestRequestBuilder(Class<R> responseType){
        this.responseType = responseType;
        this.mediaType = MediaType.APPLICATION_JSON;
    }

    public RestRequestBuilder<I,R> endpointName(String endpointName){
        this.endpointName = endpointName;
        return this;
    }

    public RestRequestBuilder<I,R> request(I request){
        this.request = request;
        return this;
    }

    public RestRequestBuilder<I,R> params(Map<String, String> params){
        this.params = params;
        return this;
    }

    public RestRequestBuilder<I,R> headers(Map<String, String> headers){
        this.headers = headers;
        return this;
    }

    public RestRequestBuilder<I,R> build(){
        return this;
    }

}

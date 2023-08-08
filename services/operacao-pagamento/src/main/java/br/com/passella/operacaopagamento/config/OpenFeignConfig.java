package br.com.passella.operacaopagamento.config;

import feign.okhttp.OkHttpClient;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;

import java.util.ArrayList;
import java.util.List;


public class OpenFeignConfig {
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }

    @Bean
    public HttpMessageConverters httpMessageConverters(){
        final List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());
        converters.add(new AllEncompassingFormHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        return new HttpMessageConverters(converters);

    }
}

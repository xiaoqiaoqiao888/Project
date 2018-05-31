package com.camelot.pmt.config;

import com.camelot.pmt.shiro.CurrentUserMethodArgumentResolver;
import com.camelot.pmt.shiro.interceptor.RequestInterceptor;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

/**
 * Created by daiyang on 2018/5/8.
 */
@EnableWebMvc
@Configuration
@EnableSwagger2
public class MvcConfig extends WebMvcConfigurerAdapter {

    @Value("${swagger.host}")
    private String swaggerHost;

    // TODO 需要把 内容放到.yml文件
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("PMT RESTful APIS").description("PMT API 文档").contact(new Contact("RD",
                        "http://172.21.101.111:3000/daiyang/project-contribute.git", "daiyang@camelotchina.com"))
                .version("1.0.0").build();
    }

    @Bean
    public Docket createRestApi() {
        ParameterBuilder builder = new ParameterBuilder();
        Parameter parameter = builder
                // 从cookie中获取token
                .parameterType("cookie") // 参数类型支持header, cookie, body, query etc
                .name("Authorization") // 参数名
                .defaultValue("") // 默认值
                .description("请输入token").modelRef(new ModelRef("string")) // 指定参数值的类型
                .required(false).build(); // 非必需，这里是全局配置，然而在登陆的时候是不用验证的
        List<Parameter> parameters = Lists.newArrayList(parameter);
        return new Docket(DocumentationType.SWAGGER_2).host(this.swaggerHost).select()
                .apis(RequestHandlerSelectors.basePackage("com.camelot.pmt.controller")).paths(PathSelectors.any())
                .build().apiInfo(this.apiInfo()).globalOperationParameters(parameters);
    }

    @Bean
    public PageHelper pageHelper() {
        Properties properties = new Properties();
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");

        PageHelper pageHelper = new PageHelper();
        pageHelper.setProperties(properties);
        return pageHelper;
    }

    /**
     * 配置跨域资源访问
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * swagger-ui.html路径映射，浏览器中使用/api-docs访问
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/api-docs", "/swagger-ui.html");
    }
    /**
     * 创建API文档
     *
     * @return
     */
    // @Bean
    // public Docket createRestApi() {
    // return new Docket(DocumentationType.SWAGGER_2)
    // .apiInfo(apiInfo())
    // .select()
    // .apis(RequestHandlerSelectors.basePackage("com.camelot.pmt"))
    // .paths(PathSelectors.any())
    // .build();
    // }

    // /**
    // * 序列换成json时,将所有的long变成string
    // * 因为js中得数字类型不能包含所有的java long值
    // *
    // * @param converters
    // */
    // @Override
    // public void configureMessageConverters(List<HttpMessageConverter<?>>
    // converters) {
    // ObjectMapper objectMapper = new ObjectMapper();
    // SimpleModule simpleModule = new SimpleModule();
    // simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    // simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
    // MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new
    // MappingJackson2HttpMessageConverter();
    // objectMapper.registerModule(simpleModule);
    // jackson2HttpMessageConverter.setObjectMapper(objectMapper);
    // converters.add(jackson2HttpMessageConverter);
    // }

    /**
     * EnableWebMvc 开启默认拦截 导致 swagger2 页面出现 404
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }
}

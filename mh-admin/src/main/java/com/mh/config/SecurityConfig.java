package com.mh.config;

import com.mh.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author dmh
 * @Date 2023/7/18 9:21
 * @Version 1.0
 * @introduce  SpringSecurity登录配置
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //密码编码
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    //请求授权的规则~
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf功能:跨站请求伪造,默认只能通过post方式提交logout请求
                .csrf().disable()
                //不通过session获取SecurityContext
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //对于登陆接口，允许匿名访问
                .antMatchers("/user/login").anonymous()
//                //注销接口需要认证才能访问(这里一定要加，因为要携带token)
//                .antMatchers("/logout").authenticated()
//                //jwt过滤器测试用，如果测试没有问题吧这里删除了
////                .antMatchers("/link/getAllLink").authenticated()  //认证之后才能访问
//                //个人信息接口必须登录后才能访问
//                .antMatchers("/user/userInfo").authenticated()
                //上传文件接口必须登录才能访问（前端联调的时候，前端页面没有携带token）
//                .antMatchers("/upload").authenticated()
                //上传文件接口必须登录才能访问（前端联调的时候，前端页面没有携带token）
//                .antMatchers("/content/tag").authenticated()
                //除去上面的所有请求全部不需要认证即可访问
//                .anyRequest().permitAll();
                .anyRequest().authenticated();

        //配置异常处理器
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        //关闭默认的注销功能(默认的也是“/logout”)
        http.logout().disable();
        //这里要加一个登录验证的过滤器：JwtAuthenticationTokenFilter
        //把jwtAuthenticationTokenFilter添加到SpringSecurity的过滤器链中
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //允许跨域请求
        http.cors();
    }
    //认证
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

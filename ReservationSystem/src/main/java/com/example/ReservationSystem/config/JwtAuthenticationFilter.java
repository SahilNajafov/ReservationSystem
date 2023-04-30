package com.example.ReservationSystem.config;


import com.example.ReservationSystem.exceptions.TokenIsExpiredException;
import com.example.ReservationSystem.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;


@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            var obj = request.getSession().getAttribute("Authorization");
            if(obj==null){
                filterChain.doFilter(request,response);
                return;
            }
            else{
                logger.error("*********token girdi "+obj);
            }
            final String authHeader = obj.toString();
//                    = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;
            if (authHeader == null || Strings.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request,response);
                return;
            }

            jwt=authHeader.substring(7);

            userEmail=jwtService.extractUsername(jwt);

            // it means user is not connected
            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if(jwtService.isTokenValid(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
            filterChain.doFilter(request,response);
        }
        catch (RuntimeException e){
            e.printStackTrace();
            throw new TokenIsExpiredException();  // 401 cixmalidi ama 403 cixir????????????????????????????
        }
        catch (Exception e){
            logger.error("Cannot set user authentication: {e}", e);
        }

    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return Stream.of("/signup","/login").anyMatch(p -> p.equals(request.getServletPath()));
////        ,"/login/**","/signup/**"
//    }
}

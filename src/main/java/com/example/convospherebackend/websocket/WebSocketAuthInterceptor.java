package com.example.convospherebackend.websocket;


import com.example.convospherebackend.entities.User;
import com.example.convospherebackend.exception.InvalidAuthenticationPrincipalException;
import com.example.convospherebackend.services.JwtService;
import com.example.convospherebackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel){
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing Authorization header");
            }
            String token = authHeader.substring(7);
            String id = jwtService.getUserIdFromToken(token);
            User user = userService.getUserById(id);

            if(user == null) {
                throw new InvalidAuthenticationPrincipalException("Invalid Jwt Token");
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
            accessor.setUser(authentication);
            accessor.getSessionAttributes().put("userId", user.getId());
        }
        return message;
    }

}

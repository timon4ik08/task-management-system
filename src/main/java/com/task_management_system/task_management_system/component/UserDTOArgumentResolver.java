package com.task_management_system.task_management_system.component;

import com.task_management_system.task_management_system.annotation.CurrentUser;
import com.task_management_system.task_management_system.model.dto.UserDTO;
import com.task_management_system.task_management_system.security.service.UserDetailsImpl;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.stream.Collectors;

public class UserDTOArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(CurrentUser.class) != null
                && parameter.getParameterType().equals(UserDTO.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        // Получаем текущего аутентифицированного пользователя
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        // Преобразуем UserDetails в UserDTO
        UserDTO userDTO = new UserDTO();
        userDTO.setId(((UserDetailsImpl) userDetails).getId());
        userDTO.setEmail(userDetails.getUsername());
        userDTO.setRoles(userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet()));

        return userDTO;
    }
}
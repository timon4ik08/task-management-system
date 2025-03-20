package com.task_management_system.task_management_system.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // Аннотация может применяться к методам
@Retention(RetentionPolicy.RUNTIME) // Аннотация доступна в runtime
@PreAuthorize("hasRole('ADMIN')") // Заменяет @PreAuthorize("hasRole('ADMIN')")
public @interface AdminOnly {
}

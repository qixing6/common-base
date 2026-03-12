package com.example.commonbase.exception;

import com.example.commonbase.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常 → code:{}，message:{}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * Spring Validation 参数校验失败（@Valid 失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");
        log.warn("参数校验异常 → {}", message);
        return Result.fail(400, message);
    }

    /**
     * 请求体格式错误（比如 JSON 格式错、类型错）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("请求参数格式错误", e);
        return Result.fail(400, "请求参数格式不正确");
    }

    /**
     * 空指针异常（单独抓，方便定位）
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Void> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.fail(500, "服务器内部空指针异常");
    }

    /**
     * 路径不存在
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.warn("访问路径不存在: {}", e.getRequestURL());
        return Result.fail(404, "请求的接口不存在");
    }

    /**
     * 请求方式错误（GET/POST 不匹配）
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方式不支持: {}", e.getMethod());
        return Result.fail(405, "不支持" + e.getMethod() + "请求方式");
    }

    /**
     * 参数类型不匹配（如 String 转 int 失败）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("参数类型不匹配: {}", e.getName());
        return Result.fail(400, "参数 " + e.getName() + " 类型不匹配");
    }

    /**
     * 权限不足
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足");
        return Result.fail(403, "权限不足，无法访问");
    }

    /**
     * 未登录 / Token 异常
     */
    @ExceptionHandler(SecurityException.class)
    public Result<Void> handleSecurityException(SecurityException e) {
        log.warn("认证异常: {}", e.getMessage());
        return Result.fail(401, e.getMessage());
    }

    /**
     * 兜底：所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统未知异常", e);
        return Result.fail(500, "服务器繁忙，请稍后再试");
    }
}
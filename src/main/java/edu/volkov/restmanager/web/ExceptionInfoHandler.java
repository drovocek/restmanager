package edu.volkov.restmanager.web;

import edu.volkov.restmanager.util.ValidationUtil;
import edu.volkov.restmanager.util.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static edu.volkov.restmanager.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {

    public static final String EXCEPTION_DUPLICATE_EMAIL = "exception.user.duplicateEmail";

    private static final Map<String, String> CONSTRAINS_I18N_MAP = new HashMap<>();
    private static final Map<String, String> EXCEPTION_MESS_MAP = new HashMap<>();

    static {
        CONSTRAINS_I18N_MAP.put("users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL);
        EXCEPTION_MESS_MAP.put("exception.user.duplicateEmail", "User with this email already exists");
        EXCEPTION_MESS_MAP.put("exception.user.updateRestriction", "Admin/User update is forbidden");
        EXCEPTION_MESS_MAP.put("exception.user.duplicateName", "Restaurant with this name already exists");
        EXCEPTION_MESS_MAP.put("exception.user.duplicateMenuInOneDate", "Menu for this day does exist");
        EXCEPTION_MESS_MAP.put("error.appError", "Application error");
        EXCEPTION_MESS_MAP.put("error.dataNotFound", "Data not found");
        EXCEPTION_MESS_MAP.put("error.dataError", "Data error");
        EXCEPTION_MESS_MAP.put("error.validationError", "Validation error");
        EXCEPTION_MESS_MAP.put("error.wrongRequest", "Wrong request");
        EXCEPTION_MESS_MAP.put("error.timeLimit", "Voice cannot be changed after 11:00");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorInfo> updateRestrictionError(HttpServletRequest req, ApplicationException appEx) {
        return logAndGetErrorInfo(req, appEx, false, appEx.getType(), EXCEPTION_MESS_MAP.get(appEx.getMsgCode()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINS_I18N_MAP.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, EXCEPTION_MESS_MAP.get(entry.getValue()));
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(er -> EXCEPTION_MESS_MAP.get(er.getCode()))
                .toArray(String[]::new);

        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, details);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private ResponseEntity<ErrorInfo> logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logStackTrace, ErrorType errorType, String... details) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logStackTrace, errorType);

        return ResponseEntity.status(errorType.getStatus())
                .body(new ErrorInfo(req.getRequestURL(), errorType,
                        EXCEPTION_MESS_MAP.get(errorType.getErrorCode()),
                        details.length != 0 ? details : new String[]{ValidationUtil.getMessage(rootCause)})
                );
    }
}
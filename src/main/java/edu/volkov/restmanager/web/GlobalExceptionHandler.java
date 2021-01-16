package edu.volkov.restmanager.web;

import edu.volkov.restmanager.util.ValidationUtil;
import edu.volkov.restmanager.util.exception.ApplicationException;
import edu.volkov.restmanager.util.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetExceptionView(req, e, false, ErrorType.WRONG_REQUEST, null);
    }

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView updateRestrictionException(HttpServletRequest req, ApplicationException appEx) {
        return logAndGetExceptionView(req, appEx, false, appEx.getType(), appEx.getMsgCode());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        return logAndGetExceptionView(req, e, true, ErrorType.APP_ERROR, null);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String code) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(log, req, e, logException, errorType);

        Map<String, Object> errResp = new HashMap();
        errResp.put("exception", rootCause);
        errResp.put("message", code != null ? messageSourceAccessor.getMessage(code) : ValidationUtil.getMessage(rootCause));
        errResp.put("typeMessage", messageSourceAccessor.getMessage(errorType.getErrorCode()));
        errResp.put("status", errorType.getStatus());

        ModelAndView mav = new ModelAndView("exception", errResp);

        mav.setStatus(errorType.getStatus());
        return mav;
    }
}

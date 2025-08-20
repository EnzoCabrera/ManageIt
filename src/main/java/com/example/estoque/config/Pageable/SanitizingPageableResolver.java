package com.example.estoque.config.Pageable;

import com.example.estoque.exceptions.AppException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.*;

public class SanitizingPageableResolver extends PageableHandlerMethodArgumentResolver {

    @Override
    public Pageable resolveArgument(MethodParameter methodParameter,
                                    ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest,
                                    WebDataBinderFactory binderFactory) {

        Pageable pageable = super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);

        AllowedSort cfg = methodParameter.getParameterAnnotation(AllowedSort.class);
        if (cfg == null) {
            return pageable;
        }

        Set<String> whitelist = new HashSet<>(Arrays.asList(cfg.value()));
        String  defaultProp = cfg.defaultProp();
        Sort.Direction defaultDir = Sort.Direction.fromString(cfg.defaultDir());

        if (pageable == null || pageable.getSort().isUnsorted()) {
            return PageRequest.of(
                    pageable == null ? 0 : pageable.getPageNumber(),
                    pageable == null ? 20 : pageable.getPageSize(),
                    Sort.by(defaultDir, defaultProp)
            );
        }

        List<Sort.Order> safe = new ArrayList<>();
        for (Sort.Order o : pageable.getSort()) {
            String prop = o.getProperty();
            if (!whitelist.contains(prop)) {
                throw new AppException("Sorting by '" + prop + "' is not allowed.", HttpStatus.BAD_REQUEST);
            }
            safe.add(new Sort.Order(o.getDirection(), prop));
        }

        Sort sort = safe.isEmpty()
                ? Sort.by(defaultDir, defaultProp)
                : Sort.by(safe);

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}

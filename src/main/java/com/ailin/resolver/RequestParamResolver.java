package com.ailin.resolver;

import com.ailin.MyException.AllParamException;
import com.ailin.anotation.RequestParam;
import com.ailin.dto.getUserDto;
import com.ailin.mode.ResultMode;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class RequestParamResolver implements HandlerMethodArgumentResolver {

    /**
     * 用于判定是否需要处理该参数分解，返回true为需要，并会去调用下面的方法resolveArgument
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParameterType();
        RequestParam annotation = methodParameter.getParameterAnnotation(RequestParam.class);
        if("java.lang.String".equals(parameterType.getName())){
            return annotation != null && annotation.required();
        }else if("int".equals(parameterType.getName())){
            return annotation != null && annotation.required();
        }
        Field[] fields = parameterType.getFields();
        return methodParameter.getMethod().getParameterTypes().length == 1 && parameterType.getClassLoader() != null &&
                fields.length > 0 && fields[0].getAnnotation(ApiModelProperty.class) != null;
    }

    /**
     * 真正用于处理参数分解的方法，返回的Object就是controller方法上的形参对象
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        Class<?> parameterType = methodParameter.getParameterType();
        Map<String, String[]> parameterMap = nativeWebRequest.getParameterMap();

        //处理String及int
        if("java.lang.String".equals(parameterType.getName())){
            return verify(methodParameter, parameterMap);
        }else if("int".equals(parameterType.getName())){
            return Integer.parseInt(verify(methodParameter, parameterMap));
        }
        Object o = parameterType.newInstance();
        Field[] fields = parameterType.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            ApiModelProperty apiModelProperty = field.getAnnotation(ApiModelProperty.class);
            if(apiModelProperty != null && apiModelProperty.required()){
                String[] s = parameterMap.get(name);
                if(s == null || (s.length > 0 && StringUtils.isEmpty(s[0].trim()))){
                    throw  new AllParamException("90", name + "必填");
                }
                BeanUtils.setProperty(o,name,s[0].trim());
            }
        }
        return o;
    }

    private String verify(MethodParameter methodParameter, Map<String, String[]> parameterMap) {
        String name = methodParameter.getParameterName();
        String[] s = parameterMap.get(name);
        if(s == null || (s.length > 0 && StringUtils.isEmpty(s[0].trim()))){
            throw  new AllParamException("90", name + "必填");
        }
        return s[0];
    }
}

package com.example.model;

import com.example.model.common.context.UserContext;
import com.example.model.common.context.UserContextInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Slf4j
//@SpringBootTest
class ModelApplicationTests {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final UserContextInfo contextInfo = new UserContextInfo(456789L, "zrq", 18, 1);
    @Value("#{ @systemProperties['user.region'] }")
    private String locale;
    @Value("#{'hello! '+@person.name+' your age is '+@person.age}")
    private String info;

    public static void main1(String[] args) {
        Expression expression = parser.parseExpression("'hello'#{#start}", ParserContext.TEMPLATE_EXPRESSION);
        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();
        context.setVariable("start", "zrq");
//        context.setVariable("end", "ok!");
        String value = expression.getValue(context, String.class);
        log.info("{}", value);
    }

    @Test
    public void test1() {
        Expression expression = parser.parseExpression("T(java.lang.Math).random() * 100");
        Double value = expression.getValue(Double.class);
        System.out.println("value = " + value);
    }

    @Test
    public void test2() {
        UserContextInfo contextInfo = new UserContextInfo(456789L, "zrq", 18, 1);
        UserContext.setUserInfo(contextInfo);
        Expression expression = parser.parseExpression("T(com.example.model.common.context.UserContext)");

        Class value = expression.getValue(Class.class);

        System.out.println("value = " + value);
    }

    @Test
    public void test3() {
        UserContextInfo contextInfo = new UserContextInfo(456789L, "zrq", 18, 1);
        UserContext.setUserInfo(contextInfo);

        Expression expression = parser.parseExpression("T(com.example.model.common.context.UserContext).getUserName() ?: 'jyy'");

        Object value = expression.getValue();

        System.out.println("value = " + value);
    }

    @Test
    public void test4() {
        Expression expression = parser.parseExpression("hello");

        SimpleEvaluationContext context = SimpleEvaluationContext.forReadOnlyDataBinding().build();

        String value = expression.getValue(context, String.class);

        System.out.println("value = " + value);
    }

    @Test
    public void test5() {
        Expression expression = parser.parseExpression("id");
        Long value = expression.getValue(contextInfo, Long.class);
        System.out.println("value = " + value);
    }

    @Test
    public void test6() {
        StandardEvaluationContext context = new StandardEvaluationContext(contextInfo);
        context.setVariable("hobby", "play games!");
        context.setVariable("contextInfo", contextInfo);
//        Expression expression = parser.parseExpression("username+ #hobby");
        Expression expression = parser.parseExpression("#contextInfo.username+ #hobby");
        String value = expression.getValue(context, String.class);
        System.out.println("value = " + value);
    }

    @Test
    public void test7() {
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression("'hello world!'.concat('---')");
        String value = expression.getValue(String.class);
        System.out.println("value = " + value);
    }

    @Test
    public void test8() {
        Expression expression = parser.parseExpression("'hello--'.concat(#contextInfo.username)");
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("contextInfo", contextInfo);
        String value = expression.getValue(context, String.class);
        System.out.println("value = " + value);
    }

    @Test
    public void test9(){
        System.out.println("info = " + info);
    }
}

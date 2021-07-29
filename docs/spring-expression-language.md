![](https://img.shields.io/badge/spring--boot-2.5.3-red) ![](https://img.shields.io/badge/gradle-7.1.1-brightgreen) ![](https://img.shields.io/badge/java-11-blue)

### SpEL (Spring Expression Langauge) 이란?

스프링 표현식은 `SpEL`로 표기하고 객체 그래프를 조회하고 조작하는 기능을 제공합니다.

[Unified EL](https://docs.oracle.com/javaee/5/tutorial/doc/bnahq.html과 비슷하지만 메소드 호출과 문자열 템플릿 기능도 제공합니다.

자바에서 사용할 수 있는 표현식은 여러 가지(OGNL: Object Graph Navigation Language, MVEL: MVFlEX Expression Language, JBOss EL)가 있지만 `SpEL`은
스프링 프로젝트 내에서 사용할 용도로 만든 표현식입니다.

스프링 3 버전부터 지원하고 있습니다.

### SpEL을 사용하는 스프링 프로젝트

`SpEL`은 스프링 프로젝트 전반적으로 모두 사용되지만 가장 비중있게 사용하는 부분은 아래와 같습니다.

* @Value
* @ComditionalOnExpression
* spring-security
    * @PreAuthorize
    * @PostAuthorize
    * @PreFilter
    * @PostFilter
* spring-data
    * @Query
* Thymeleaf

주로 애너테이션의 속성으로 활용됩니다.

### SpEL 구성

```java
package io.lcalmsky.spel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@SpringBootApplication
public class SpelApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpelApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        ExpressionParser expressionParser = new SpelExpressionParser();
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        Expression expression = expressionParser.parseExpression("");
        Object value = expression.getValue();
    }
}

```

### SpEL 사용

표현식을 사용하기 위해서는 `#{}`를, `properties`를 참조할 때는 `${}`를 사용합니다.

콜론(`:`)을 이용해 기본 값을 명시할 수 있고 표현식과 참조를 동시에 사용할 수 있습니다.

간단히 소스 코드로 작성해보겠습니다.

먼저 `application.properties`(또는 `application.yml`)를 작성합니다.

스프링 웹 기능을 사용하지 않을 것이므로 관련 설정을 off 하였습니다.

#### application.properties

```properties
spring.main.web-application-type=none
foo.bar=100
foo.baz=string
foo.qux=true
```

다음으로 표현식 사용을 위해 테스트 클래스 작성을 따로 하지 않고 기본 클래스에 내용을 추가하였습니다.

#### SpelApplication.java

```java
package io.lcalmsky.spel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpelApplication implements CommandLineRunner { // (1)

    public static void main(String[] args) {
        SpringApplication.run(SpelApplication.class, args);
    }

    @Value("#{1 + 1}") // (2)
    private int value;
    @Value("#{'heungmin' + ' ' + 'Son'}") // (3)
    private String name;
    @Value("#{1 + 1 eq 2 }") // (4)
    private boolean equals;

    @Value("${foo.bar}") // (5)
    private int bar;
    @Value("${foo.baz}") // (6)
    private String baz;
    @Value("${foo.qux}") // (7)
    private boolean qux;
    @Value("${foo.quux:default value}") // (8)
    private String quux;

    @Override
    public void run(String... args) throws Exception {
        // (9)
        System.out.println("expressions");
        System.out.println("value = " + value);
        System.out.println("name = " + name);
        System.out.println("equals = " + equals);

        System.out.println("references");
        System.out.println("bar = " + bar);
        System.out.println("baz = " + baz);
        System.out.println("qux = " + qux);
        System.out.println("quux = " + quux);
    }
}

```

> (1) `CommandLineRunner`를 구현해 앱 실행 시 `run` 메서드가 호출되게 하였습니다.  
> (2) `1 + 1`과 같은 숫자를 이용한 표현이 가능합니다.  
> (3) 문자열을 이용한 표현이 가능합니다.  
> (4) `boolean` 표현이 가능합니다.  
> (5)(6)(7) `properties`의 속성을 읽어와 주입하는데 타입을 자동으로 매칭시켜줍니다.  
> (8) `properties`에 해당 값이 없으면 기본 값을 이용합니다.  
> (9) 제대로 연산이 수행되고 참조가 되는지 확인하기 위해 값을 출력합니다.

앱을 실행시켜보면 정상적으로 연산이 수행되고 값을 읽어왔음을 확인할 수 있습니다.

```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.5.3)

2021-07-29 14:15:22.784  INFO 33062 --- [           main] io.lcalmsky.spel.SpelApplication         : Starting SpelApplication using Java 11.0.11 on Jungminui-iMac.local with PID 33062 (/Users/jaime/git-repo/jaime-notes/spel/build/classes/java/main started by jaime in /Users/jaime/git-repo/jaime-notes)
2021-07-29 14:15:22.787  INFO 33062 --- [           main] io.lcalmsky.spel.SpelApplication         : No active profile set, falling back to default profiles: default
2021-07-29 14:15:23.437  INFO 33062 --- [           main] io.lcalmsky.spel.SpelApplication         : Started SpelApplication in 1.227 seconds (JVM running for 1.911)
expressions
value = 2
name = heungmin Son
equals = true
references
bar = 100
baz = string
qux = true
quux = 'default value'
```

조금 더 응용해 보겠습니다.

```java
@Value("#{${foo.quux:'default value'}.replace(' ', '')}") // (1)
private String quuxWithoutSpace;

@Override
public void run(String... args) throws Exception {
      // 생략
      System.out.println("quuxWithoutSpace = " + quuxWithoutSpace);
    }
```

> (1) `foo.quux`를 `properties`에서 읽어오고 그 값이 없으면 기본 값 `default value`로 대체한 뒤 표현식을 이용해 공백을 없애줍니다.

실행한 결과는 아래와 같습니다.

```text
quuxWithoutSpace = defaultvalue
```

### SpEL 동작 방식

`SpEL Parser`를 이용해 표현식을 파싱한 뒤 표현식에서 값을 추출합니다.

```java
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
// 생략
{
  ExpressionParser expressionParser = new SpelExpressionParser();
  Expression expression = expressionParser.parseExpression("");
  Object value = expression.getValue();
}
```

`getValue()`는 타입을 지정하지 않은 경우 `Object`로, 타입을 지정할 경우 해당 타입으로 반환하게 됩니다.

타입이 맞지 않을 경우 `TypeMismatchException`이 발생합니다.

`List`, `Map` 등의 타입으로도 매핑할 수 있고 메서드나 연산자 등을 사용할 수 있습니다.

자세한 내용은 [공식 문서](https://docs.spring.io/spring-framework/docs/5.2.9.RELEASE/spring-framework-reference/core.html#expressions)에 나와있으니 참고하시면 될 거 같습니다.

### Bean 참조

표현식을 이용해 `Bean`을 바로 참조할 수 있습니다.

#### Properties.java
```java
package io.lcalmsky.spel;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Properties {
    private int number = 1;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
```

`@Configuration`, `@Component` 등 빈 등록을 위한 애너테이션을 추가한 뒤 필드에 대한 `getter/setter`를 추가해주면,

```java
// 생략
@Value("#{properties.number}")
private int number;
// 생략
{
    System.out.println("number from bean = " + number);
}
```

다른 클래스에서 표현식을 이용해 해당 값을 주입할 수 있습니다.

이 부분을 따로 다룬 이유는 바로 프레임워크나 라이브러리에서 많이 쓰이는 방식이기 때문입니다.

`@Enable`로 시작하는 애너테이션을 많이 보셨을텐데 이러한 애너테이션을 추가하게되면 `@Import` 나 `@ComponentScan`을 이용해 클래스나 패키지에 있는 `Bean`들을 모두 등록하게되고 이 때 자바 클래스 설정을 사용하는 경우 기본 값이 제공되기 때문에 별도의 설정 없이도 동작하게 됩니다.

그리고 설정을 변경해야 하는 경우 `properties` 에서 `bean`의 이름과 속성을 탐색해서 수정해 줄 수 있는데 내부적으로 `setter`가 정의되어있기 때문입니다.

맨 위에 객체 그래프를 조회하고 조작한다고 설명했는데 이런식의 `bean`을 통한 설정을 잘 제공한다면 사용하는 개발자에게 엄청난 편의성을 제공할 수 있습니다.

물론 라이브러리를 사용하듯이 다루려면 위 내용 이외에 다른 기능들도 같이 사용해야 합니다만 기본 원리에 대해 설명하고자 추가로 설명하였습니다.


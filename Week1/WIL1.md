WIL1

★ SpringBoot -> SB로 작성하겠다. 

- 이런 의존관계에 관련된 라이브러리와 프레임워크들을 Gradle이 전부 땡겨온다.
- 고대의 프로그래머들은 웹서버를 직접 서버에 설치해 놓고 거기에 자바코드를 밀어넣는 식으로 웹서버와 개발 라이브러리가 분리되어 있었지만, 
    요즘에는 소스 라이브러리가 웹서버를 들고 있다(ex)Tomcat))

SpringBootLibrary 
    s-b-s-w에는 하위 라이브러리들이 포함되어 있고, 각 라이브러리들을 또 필요한 라이브러리들을 가져와 사용한다. (이는 extensions에서 확인할 수 있다.)
        - tomcat : 톰캣(웹서버) : java기반의 서블릿 컨테이너이자 웹서버로 Spring으로 웹사이트 구축 시 필수 요소.
                                Tomcat에는 오로지 서블릿/JSP 및 HTTP 처리 엔진만이 들어있다.
        - webmvc : 스프링 웹 MVC 
        - thymeleaf 타임리프 템플릿 엔진(View) : 뷰 템플릿으로 컨트롤러가 전달하는 데이터를 이용하여 동적으로 화면을 구성.
                        html태그 기반으로 "th:" 속성을 이용하여 동적인 View를 제공. "th:"태그가 붙어있다면 서버 사이드에서 렌더링하여 기존 것을 대체하고 없으면 그대로 사용. 

                        여기서 템플릿 엔진이란 : 서버에서 DB 또는 API 등을 통해 가져온 데이터를 미지 정의해놓은 템플릿에 넣어
                        html을 그려내 클라이언트에게 전달. 
        - spring-boot-stater : 스프링 부트 + 코어 + 로깅 
                -> spring-core : Spring의 근간 / IoC/DI 기능 지원. / container가 Bean(객체) 생성, 관리,제거

                -> spring-logging(logback, slf4j : ) : 시스템의 상태 및 동작 정보를 시간경과에 따라기록 / 둘은 가장
                                    대표적인 logging Framework / 현업에서는 system.out.println이 아니라 로깅을 사용. log로 남겨야 심각한 에러를 관찰하고, 로그만 따로 모을 수 있다.
                Etc...

- SB에서는 Welcome Page기능을 제공한다. resources/static/index.html 

- thymeleaf 템플릿엔젠 동작 확인 
    웹 브라우저에서 파일을 요청하면, 내장 톰캣 서버에서 먼저 컨트롤러를 확인한 뒤 컨트롤러가 존재했을 때, 리턴값으로 문자를 반환하면
    viewResolver가 화면을 찾아서 처리. --> SB template 엔진의 default는 viewName 매핑


- intelliJ에서 실행이 아닌 빌드하여 실행하기
    1. ./gradlew build
    2. cd build/libs
    3. java -jar hello-spring-0.0.1-SNAPSHOT.jar

- 웹 개발의 세가지 방법 
    정적 컨텐츠 / MVC와 템플릿 엔진 / API

    1. 정적 컨텐츠 : 그냥 파일을 서버에서 있는 그대로 보내는 것 
        요청이 들어오면 Ruquest된 파일의 관련 컨트롤러가 있는지 확인하고 없다면 서버에서 그대로 보내준다.

    2. MVC(Model View Controller) : 여기서 View는 화면을 그
        리는데 모든 역량을 집중하고, Model과 Controller는 비즈니스 로직 관련된 것과 내부적 처리에 집중한다. 
         진행 순서로는 서버에 요청이 들어오면 서버는 관련 컨트롤러를 찾아 템플릿엔진을 통해 변환한 뒤 웹 브라우저에 넘겨준다.

    3. API(Application Programming Interface) : 요즘은 Json 데이터 구조 포맷으로 클라이언트에게 전달한다.
         @ResponseBody와 사용했을때 객체가 없다면, http body 부에 문자 내용을 직접 반환한다(view Resolver를 사용하지 않음) 
        객체를 반환하면 객체가 key, value로 이루어진 객체로 변환된다. 
        문자일때는 HttpMessageConverter중에서 StringConverter가, 객체일때는 JsonConverter가 호출된다. 

        - 클라이언트의 HTTP Accept헤더와 서버의 Controller return type 정보, 이 둘을 조합하여 
          HttpMessageConverter가 선택된다. 


★★★★★ 추가로 찾아본 것 ★★★★★
- JSP : Java Server Pages / 서버 사이드 템플릿 엔진 / HTML 코드에 JAVA 코드를 넣어 동적웹페이지를 생성하는
        웹 어플리케이션 도구. JSP가 실행되면 자바 Servlet으로 변환되며, 웹 어플리케이션 서버에서 동작하며, 필요한 기능
        수행하고 생성된 데이터를 클라이언트에게 응답함. 여기서 Servlet은 웹페이지를 동적으로 생성하기 위한 서버 측 프로그램.

- DI(Dependency Injection) : 의존 관계 주입 기능 / 객체를 직접 생성하는 것이 아닌 외부에서 생성한 후 주입 시켜주는 것
                            이를 통해 module 간의 결합도가 낮아지고 유연성이 높아진다. 
                             외부 IoC컨테이너가 객체를 주입하는 형태로 이 기능을 제공.

- IoC(Inversion of Control) : 제어의 역전 / 메소드나 객체의 호출작업을 개발자가 결정하는 것이 아닌 외부에서 결정하는 것
                            객체의 의존성을 역전시켜 객체 간의 결합도를 줄이고 유연한 코드를 작성할 수 있게 하여 가독성 및
                            코드 중복, 유지 보수를 쉽게 해준다. 

                            이에 스프링에서는 다음 순서로 객체가 만들어지고 실행됨
                            1. 객체 생성 
                            2. 제어권을 가진 Spring이 만들어 놓은 객체를 주입
                            3. 의존성 객체 메소드 호출(ex> Setter())
                            그 결과, 객체(Bean)들은 싱글턴 패턴을 가진다.
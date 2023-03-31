- 비즈니스 요구사항 정리
아직 DB가 정해지지 않은 상황이라 가정하고 최대한 간단하게 제작
Data : ID(데이터 구분 위해 시스템이 정하는 값), name
기능 :  회원가입, 등록, 조회

- 의존관계 
Contoller : 웹 mvc컨트롤러 역할. 
Service : 핵심 비즈니스 로직 구현.
Repository(Interface) : DB에 접근, domain 객체를 DB에 저장하고 관리 
        여기에서는 일단 회원객체 저장하는 저장소 
        (Save, findById, findByName, findAll) 기능

- Map<Long, Member> store :: Hashmap으로 저장 
    실무에서는 동시성 문제가 있어 ConcurrentHashMap, Long은 
    AtomicLong을 사용해야 한다. 

- save() : sequence값 올려주며 Id 설정과 name 저장
- findById() 에서 Optinal.ofNullable(store.get(id))와 같이 
    Optional로 감싸서 반환하면, 클라이언트가 여러 Optional 기능 사용
    가능. Optional로 감싸면 Optional 안에 객체가 존재. 
    요즈음에는 값에 null이 있을 가능성이 있다면 Optional로 감싼다.
- findByName() : 람다 사용 / parameter로 넘어온 name이 같은지 
    확인하고, 같은 경우에만 필터링, 찾으면 결과 반환.
''' 
store.values().stream() 
					.filter(member -> member.getName().equals(name))
					.findAny();
'''

> Test case 작성
개발한
JUnit 프레임워크를 사용하여, 쉽게 테스트 진행 가능
- @Test -> org.junit.jupiter.api.Test에서 import하여 사용. 
- Optional에서 값을 꺼낼 때는 get()과 같은 방법을 사용했으나, 별로 
    좋은 방법이 아니다. 

- 두 값 같은지 확인
''' 
assertions(org.assertj).assertThat(member).isEqualTo(result);
'''

- Test 케이스는 class레벨에서 전체 테스트 가능. 그러나 테스트 순서는 보장 되지 않기 때문에 순서 의존적이지 않게 method별로 따로 동작하게 설계해야한다. -> 이전 객체가 저장되어 있으면 에러가 발생하기 때문에 테스트가 하나 끝나면 데이터를 아래와 같은 방법으로 clear해주어야 한다.
- 빌드 할 때 testcode는 포함되지 않는다. 

'''
@AfterEach // : 메서드 끝날때마다 실행하는 것 
public void afterEach() {repository.clearStore();}

public void clearStore() {
    store.clear(); // 저장 객체 clear
}
'''

- TDD(Test Driven Development)
미리 검증할 수 있는 테스트 케이스부터 만든 후에 개발을 진행하는 것. 
매우 짧은 개발 사이클을 가지며, 테스트 코드를 작성하며 결과를 예상해볼 수 있기 때문에 설계의 문제로 인한 오류 개선 속도가 빨라 질 수 있다.
우리는 반대로 구현클래스부터 개발한 케이스이다. 
이제는 testcode 없이 개발을 진행하는 것은 매우 위험하다.

- given // when // then 문법
뭔가 주어졌는데(given), 이걸 실행했을 때(when), 이게 나와야 돼(then)같이 주석으로 코드 구획을 나누어 놓으면 가독성이 좋다. 나중에 추가적으로 변형해서 사용하는 것 추천.

- DI 도입
지금까지 작성한 code에서 new MemoryMemberRepository();를 두번 실행하였다. 이는 test와 member과 static이 아니라면 문제가 생길 수 있다. 이에 같은 객체로 test를 진행하여야 하는데 이를 위해 객체를 내가 직접 생성하는 것이 아니라 외부에서 넣어 주도록 한다.
@BeforeEach : 동작하기 전에 각각 실행하게 함.
public void beforeEach() {} -> 같은 MeomoryMemberRepository를 사용하게 하기 위해 -> 외부에서 이를 넣어준다. 이게 바로 Defencdency Injection


> 스프링 빈과 의존관계
회원가입한 후 결과를 html로 뿌려줘야 한다. -> 
이를 위해 Member Controller가 필요 -> 
MemberServie 통해 회원가입, 데이터 조회 ->
이를 의존관계 있다고 표현

- 우리가 만드는 객체는 하나만 생성하여 공용함 -> Spring Container에 등록하고 사용한다.

- @Autowired 
위와 같은 Annotation이 있으면 멤버서비스는 스프링 컨테이너에 있는 멤버서비스를 가져와 자동으로 연결하여 준다. 그리고
구현체에 @Repository와 같은 Annotation이 있다면, 이 또한 컨테이너에 등록.
이렇게 MemberControll가 생성될 때, spring bean에 등록되어 있는 MemberService 객체를 자여와 연결시켜준다 -> DI

이와 같이 Controller가 외부 요청을 받고,
Service에서 비즈니스 로직을 만들고, 
Repository에 데이터를 저장하는 것이 개발의 정형된 패턴이다.

>스프링 빈 등록 방법 
1. 컴포넌트 스캔 
- Repository, controller, Service 안에는 @Component가 등록되어 있다. 
- spring은 component 관련 Annotation이 있다면 이를 다 생성한 뒤 이 객체들을 스프링 컨테이너에 등록.
- @Autowiredsms 의존관계를 연결한다. 
- 이게 바로 컴포넌트 설정과 자동 의존관계 설정. 
- 파일 패키지와 동일하거나 파일 패키지 아닌 객체는 스프링 빈으로 등록하지 않는다.
- 스프링 빈 등록 시에는 기본으로 싱글톤 등록(하나만) 그 후 공유.

2. 자바 코드로 직접 스프링 빈 등록 
- 직접 설정 파일에 등록하는 것으로 @Configuration, @Bean 사용.
- 스프링이 @Configuration을 읽으면 어! 이거는 스프링 빈에 등록하라는 뜻이네 하면서 스프링 빈에 등록

>DI의 3가지 방법
1. 필드 주입
2. 생성자 주입
3. Setter 주입 

- 필드주입은 중간에 변경을 할 수 없고, Setter주입은 public으로 접근 제한이 열려있기 때문에 아무 개발자나 객체를 변경할 수 있으므로 사용 X
- 등록된 객체는 한 번 세팅이후에는 바꿀 일이 없기에 생성자 주입 사용!!




---
>단축키
opt + enter : 필요한 라이브러리 import
cmd + opt + v : Optional 자동으로 가져옴.
cmd + shft + t : 자동으로 테스트케이스 작성.
ctrl + t : refactoring 관련 메서드(메서드 추출)
cmd + p : 메서드와 파라미터 확인.
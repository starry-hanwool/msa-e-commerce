# 이한울
# 마이크로서비스로 이커머스 구축하기

# 환경

---

- JAVA 12
- Spring Boot 2.3.7
- JPA
- Webflux
- JUnit5
- Mysql8
- Kafka
- Kubernetes (minikube)

# 설계

---

해당 프로젝트는 **마이크로 서비스** 학습을 목표로 하여, 비지니스 로직 부분은 생략되어 있다.

도메인 모델은 단순하게 주문과 상품으로만 구성하였다.

상품(Product)은 아이템(Item)들로 구성되어 있고, 구매시 아이템을 원자단위로 취급하도록 하였다.

![image](https://user-images.githubusercontent.com/73062660/106392788-d76a1f80-6436-11eb-9900-b240b05a810a.png)
<img src="https://user-images.githubusercontent.com/73062660/106392882-3b8ce380-6437-11eb-8af9-e4cf0ce6e074.png" width="150">

모든 서비스는 쿠버네티스 위에 올려 볼 것이다.

이 프로젝트는 **주문 서비스(Order Service)**와 
 주문 서비스와 같은 마이크로 서비스들의 API를 묶어 하나의 API로 제공해주는 **컴포지트 서비스(Mall Composite),**
 이 사이에서 메시지를 전달해주는 **Kafka**로 구성되어 있다.

1. 주문 서비스
    - 주문처리의 경우 정합성이 중요하기 때문에 기존의 Web MVC + jdbc 환경으로 구축한다.
2. 컴포지트 서비스
    - **오케스트레이션** 스타일로 중앙에서 마이크로 서비스들의 순서를 관리함으로써 마이크로 서비스들이 서로를 몰라도 된다는 장점이 있다. (이 프로젝트는 주문 서비스 하나 밖에 없지만, 서비스가 많아질 경우 서비스끼리 직접 통신 한다면 의존도가 높아져 복잡해진다.)
    - 컴포지트 서비스는 동시에 각 마이크로 서비스로 요청을 하는 경우가 자주 있을 수 있기 때문에, Spring 5에서 지원하는 WebFlux를 사용한다.
    WebFlux는 ***논블로킹*** 방식(작업을 요청한 후 기다리지 않고 원래 작업을 계속하다가 callback으로 결과를 받아서 작업을 이어간다.)으로 스레드를 운영하여 이렇게 *동시성이 높은* 서비스의 경우 적은 자원으로 가용성을 높여준다.
        - ex) 마이페이지 API가 있다고 하면, 회원 서비스에서 회원 정보를 가져오는 일(1초)과
         주문 서비스에서 최근 주문내역을 가져오는일(2초)을 동시에 처리하여 총 latency(2초)를 줄여준다.
        - 참고 : [https://alwayspr.tistory.com/44](https://alwayspr.tistory.com/44)
3. Kafka
    - 마이크로 서비스는 서비스 간 통신의 유실 및 실패라는 이슈가 생기게 되는데, 이럴경우 기존 모놀리식 구조의 서비스와 다르게 트랜잭션 처리가 불가능하다.
    - 이 때 Kafka와 같은 MQ 서비스를 사이에 두고 메시지를 발행/구독 하는 형태로 통신함으로써, 장애 서비스의 복구시 해당 이벤트를 다시 수신할 수 있게되어 이벤트 유실의 가능성을 막을 수 있게 된다. 특히 Kafka의 경우 메시지를 디스크에 저장하기 때문에 장애 복구 능력이 뛰어나다.
    - 이 프로젝트에서는 orders와 composite라는 ***Topic***으로 이벤트를 주고 받는다.

![image](https://user-images.githubusercontent.com/73062660/106393176-02557300-6439-11eb-8136-7c535e02839a.png)

구매하기 요청시 가주문을 먼저 생성한 후 결제 금액 확인 등의 검증 작업을 한 후 상태를 전이한다.

주문이 거부 되었을 경우는 차감된 재고를 복구하는 등의 ***보상 트랜잭션*** 처리를 해야한다.

![image](https://user-images.githubusercontent.com/73062660/106393192-1f8a4180-6439-11eb-82b2-c49c486f7c97.png)

## 패키지 구조

---

- 멀티 모듈 프로젝트
    - 멀티 모듈 형태로 프로젝트를 구성하여 DTO, http exception (RestContoller 단에서 발생한 Exception을 `@RestControllerAdvice` 로 전역 처리) 등 비지니스 로직이 의존하지 않는 애플리케이션 계층 도메인을 공통으로 사용할 수 있게 하였다.
    - common
        - 이 프로젝트에서는 각 마이크로 서비스들의 API를 컴포지트 서비스를 통해서 제공하기 때문에, port 역할을 하는 service 인터페이스들을 공통 모듈로 빼내서 일관성 있게 개발 할 수 있도록 하였다.
        - 비지니스 로직의 entity를 직접 공유하지 않고, DTO를 공통 모듈에 정의하여 사용한다.
        entity를 누구나 접근할 수 있게 하면, 애플리케이션 계층의 수정사항이 핵심 비지니스까지 영향을 미치게 되어 위험하기 때문에 캡슐화로 보호되어야 한다. (Setter 사용을 지양)
    - 각 모듈별 패키지 구조 : 육각형 아키텍처 기반
        - 어댑터 : 비지니스 로직에 의존
        - 포트 : 비즈니스 로직이 외부와 상호 작용하는 방법이 정의된 작업(operation). ex) 인터페이스
        - 인바운드 어댑터, 포트 : 외부 요청에 의해 비즈니스 로직 수행
        - 아웃바운드 어댑터, 포트 : 비지니스 로직이 외부 시스템 호출

<img src="https://user-images.githubusercontent.com/73062660/106393217-3d57a680-6439-11eb-8734-5716e9e8f827.png" width="220"> <img src="https://user-images.githubusercontent.com/73062660/106393228-49436880-6439-11eb-9a33-397f33f3353b.png" width="220"> <img src="https://user-images.githubusercontent.com/73062660/106393238-53fdfd80-6439-11eb-9245-307d64f7b836.png" width="240">


## 핵심 문제 해결

---

- 주문번호

    트래픽이 많은 경우 1초에도 여러건의 주문이 발생할 수 있으므로 현재 일시 뒤에 임의의 숫자를 붙여주는데,
    흔히 사용하는 기본 Random 함수는 현재 시간을 Seed로 사용하기 때문에 동시에 주문한 경우 같은 값이 나올 수 있다.

    그러므로 안전한 Seed를 기본으로 제공하는 SecureRandom 함수를 사용해 임의의 숫자를 생성해 주문번호를 만든다.

    ```java
    	String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = String.valueOf(1000 + new SecureRandom().nextInt(1000000000));
        String orderNum = now + randomStr;
    ```

    고유한 값인 주문번호는 결제 연동시 PG사에도 제공해야하기 때문에 스펙을 맞추는 것이 좋다.

    이니시스 기준으로 40을 넘지않도록 맞춰줬다.

    ```java
    @Column(nullable = false, unique = true, length = 40)
        private String orderNum;
    ```

- 구매하기 기능은 화면에 주문 결과를 뿌려줘야 하기 때문에 카프카 통신을 동기식으로 할 필요가 있다.

    우선 `ReplyingKafkaTemplate` 빈을 메시지 형태에 맞게 재정의 한다. 각 메시지가 순차적으로 처리되야 하기 때문에 key 값을 줘서 동일 파티션에 배정되도록 하고, GroupId를 지정하여 파티션에 배정된 메시지를 하나의 consumer가 독점적으로 처리하도록 한다.

    ```java
    	@Bean
        public ReplyingKafkaTemplate<String, Event, Event> replyingKafkaTemplate(ProducerFactory<String, Event> pf,
                                                                                   ConcurrentKafkaListenerContainerFactory<Long, Event> factory) {
            ConcurrentMessageListenerContainer<String, Event> replyContainer = factory.createContainer(INPUT_COMPOSITE);
            replyContainer.getContainerProperties().setMissingTopicsFatal(false);
            replyContainer.getContainerProperties().setGroupId(GROUP_ID);
            return new ReplyingKafkaTemplate<>(pf, replyContainer);
        }
    ```

```java
	// 주문 요청
        Event event = new Event(Event.Type.ORDER_CREATE, key, orderDTO);
        ProducerRecord<String, Event> record = new ProducerRecord<String, Event>(OUTPUT_ORDERS, key, event);
        RequestReplyFuture<String, Event, Event> future = replyingKafkaTemplate.sendAndReceive(record);

        // 가주문 정보 응답
        ConsumerRecord<Long, Event> response = future.get();
        Event receivedEvent = response.value();
        OrderDTO result = mapper.convertValue(receivedEvent.getData(), OrderDTO.class);
```

# Test

---

JUnit을 이용한 테스트 코드 작성으로 TDD 환경 구축

- 유닛 테스트
    - BDDMokito와 `@Nested` 를 이용해 given, when, then 구조로 비지니스 로직의 행위 모방하여 테스트를 진행한다.

        유닛 테스트는 실제 bean들이 아닌 가짜 `MockBean` 을 주입하여 가벼우면서 각 bean들에 의존적이지 않은 테스트를 할 수 있다.

        예) 실제 repository를 사용하면 API 테스트시 실제 DB 값에 따라 성공 실패 여부가 달라져버린다.

    ```java
    @WebFluxTest(OderCompositeAPI.class)
    @ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
    @DirtiesContext
    @ActiveProfiles("logging-test")
    class OderCompositeAPITest {

        private static final String OPEN_API_ORDER_URI = "/open-api/order/";

        @Autowired
        private ApplicationContext context;

        private WebTestClient client;

        @MockBean
        private WebClient.Builder builder;

        @MockBean
        private OrderCompositeIntegration integration;

        @MockBean
        private OrderCompositeServiceImpl service;

        @BeforeEach
        void setup(RestDocumentationContextProvider restDocumentation) {
            this.client = WebTestClient.bindToApplicationContext(this.context)
                    .configureClient()
                    .filter(documentationConfiguration(restDocumentation))
                    .build();
        };

    		@Nested
        @DisplayName("주문 정보 확인")
        class getOrderDetailsById {

            @Test
            @DisplayName("성공")
            void getOrderDetailsByIdSuccess() throws Exception {
                // given
                final String requestOrderNum = "20210131221624721523117";
                OrderDTO returnOrder = OrderDTO.builder()
                        .orderNum(requestOrderNum)
                        .build();
                given(service.getOrderDetails(requestOrderNum)).willReturn(returnOrder);

                // when, then
                client.get()
                        .uri(OPEN_API_ORDER_URI + requestOrderNum)
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.orderNum").isEqualTo(requestOrderNum)
                        .consumeWith(document("get-order"));
            }

            @Test
            @DisplayName("실패 - 정보 없음")
            void getOrderDetailsByIdNotFound() throws Exception {
                // given
                final String requestOrderNum = "20210131221624721523117";

                given(service.getOrderDetails(requestOrderNum)).willReturn(null);

                // when, then
                client.get()
                        .uri(OPEN_API_ORDER_URI + requestOrderNum)
                        .accept(APPLICATION_JSON)
                        .exchange()
                        .expectStatus().is4xxClientError()
                        .expectBody()
                        .jsonPath("$.code").isEqualTo(ResponseCode.NOT_FOUND.code);
            }
        }

    ...
    ```

- 통합 테스트
    - **API를 구현한 Controller**는 `@SpringBootTest` 어노테이션을 지정하여 통합 테스트를 진행한다.
    해당 어노테이션은 실제 앱을 구동하는 것과 같이 ApplicationContext을 생성하여, Mock이 아닌 동작 가능한 형태로 Bean들을 로드하기 때문에 유닛 테스트에 비해 속도가 매우 느리다.
    - 통합 테스트는 외부 서비스와 통신하는 부분이 포함되어 있기 때문에, 외부 서비스의 상태에 따라 테스트가 실패 할 수도 있다.
    이런 의존성 및 속도(서비스 규모가 커질수록 테스트에 드는 시간이 너무 많이 소모되어 TDD의 효과를 반감시킴) 등의 문제로 build 시 해당 테스트는 제외하도록 build.gradle에 설정한다.

        ```groovy
        build {
        	...
        	test.exclude("me/hanwool/mallcomposite/integration/**")
        }
        ```

- Profiles별 테스트 환경
    - (default) : local
    - dev :  docker-compose
    - stage : kubernetes

# 산출물

---

- API 문서

    실제 서비스 코드에 영향을 주지 않고, 문서화에 좀 더 집중되는 것이 좋을 것 같아, 요즘 많이 사용하는 Swagger 보다는 Spring Rest Docs를 선택하였다.

    Test에 성공하면 Test code를 기반으로 문서가 자동으로 생성된다.

    ![image](https://user-images.githubusercontent.com/73062660/106393269-80197e80-6439-11eb-8c7e-1d24933b0af4.png)


# 쿠버네티스 환경 구축

### 설치 및 설정

1. Hyper-V 설정
    - 윈도우 10 Home 버전은 직접 설치해줘야한다.
    - [https://forbes.tistory.com/542](https://forbes.tistory.com/542)
2. Docker Desktop 설치
3. Minikube : 로컬 환경에서 쿠버네티스 테스트 환경 제공
    - 윈도우 환경은 Chocolatey 등으로 설치
    `choco install minikube`
    - Docker가 실행된 상태에서 minikube를 시작하고, dashboard를 실행하면 자동으로 브라우저에 아래와 같은 화면이 뜬다.
    `minikube start`
    `minikube dashboard`

    ![image](https://user-images.githubusercontent.com/73062660/106393284-96bfd580-6439-11eb-836f-afb43f393758.png)

    - 참고
        - [v1-18.docs.kubernetes.io/ko/docs/tasks/tools/install-minikube/](https://v1-18.docs.kubernetes.io/ko/docs/tasks/tools/install-minikube/)
        - [kubernetes.io/ko/docs/tutorials/hello-minikube](https://kubernetes.io/ko/docs/tutorials/hello-minikube/)

### Helm

미리 설정된 서비스들(kafka, mysql 등 )을 다운받아 쉽게 쿠버네티스에 설치 할 수 있게 도와준다. 

custom char를 만들어 관리하면 서비스들의 환경 설정을 하나의 패키지 단위로 관리(릴리스 히스토리 확인, 롤백 가능 등) 할 수 있게 되어 쿠버네티스 운영에 유용하다.

- [Helm 설치](https://helm.sh/ko/docs/intro/quickstart/)
    - (윈도우 기준) 관리자 권한으로 명령 프롬프트 실행

        `choco install kubernetes-helm`

- 인텔리j : Kubernetes 플러그인 설치 (helm 템플릿 지원)
- 현재 프로젝트용 chart를 생성한다. (name은 소문자와 '.', '-' 로만 이뤄져야함.)

    `helm create hw-chart`

- 레포지토리 추가

    `helm repo add bitnami [https://charts.bitnami.com/bitnami](https://charts.bitnami.com/bitnami)`

- ***Chart.yaml***에 의존성을 정의해준다. (helm 3 부터는 requirements.yaml가 Chart.yml로 통합되었다.)

    레포지토리에서 다음 명령어로 name과 version 정보 찾아서 dependencies에 추가.

    `helm search repo mysql`

    ```yaml
    dependencies:
      - name: mysql
        version: 8.2.8
        repository: https://charts.bitnami.com/bitnami
        condition: mysql.enabled
        alias: mysql

      - name: kafka
        version: 12.7.1
        repository: https://charts.bitnami.com/bitnami
        condition: kafka.enabled
        alias: kafka
    ```

- 생성한 chart 경로로 이동해 다음 명령어를 실행하면 의존성의 내용들이 자동으로 chart 폴더에 설치된다.

    `cd hw-chart`

    `helm dependency update`

- 생성한 chart 폴더의 최상위 values.yaml (parent)에 설치한 파일(child: mysql, kafka 등)의 values.yaml에서 수정할 값을 추가하고 빌드하면, 해당 값으로 override 된다.
mysql chart의 경우 보통 values에 정의된 비밀번호는 secret에(base64 인코딩 되어 저장)에 저장되도록 되어있다.
(secret과 configMap 파일은 보안상 같은 namespace 에서만 공유 가능하다. —namespace 옵션을 따로 주지 않았다면 default namespace로 배정된다.)

    ```yaml
    # mysql
    mysql:
      auth:
        rootPassword: hanwool
        database: orders
        username: order_admin
        password: hanwool
    ```

- 해당 chart를 설치하면 dashboard에서 구동된 모습을 볼 수 있다. (dashboard 상단에 namespace selector가 있으니 default로 설치하지 않았다면 선택해줘야 볼 수 있다.)

    `helm install hw-chart .`
    `helm list`

- 참고
    - [https://www.presslabs.com/docs/mysql-operator/integrate-operator/](https://www.presslabs.com/docs/mysql-operator/integrate-operator/)

### 실행

- Develop onKubernetes 실행

### 주의

프로젝트 gradle build 를 잊지말자.

### kafka 설정

- 카프카 로그 확인 (intelliJ > Kubernetes Explorer > 해당 Pods > 우클릭 > stream Logs)

    ```bash
    INFO Registered broker 0 at path /brokers/ids/0 with addresses: INTERNAL://hw-chart-kafka-0.hw-chart-kafka-headless.default.svc.cluster.local:9093,CLIENT://hw-chart-kafka-0.hw-chart-kafka-headless.default.svc.cluster.local:9092, czxid (broker epoch): 24 (kafka.zk.KafkaZkClient)
    ```

- 로그에 있는 *CLIENT*의 주소를 application.yml 에 설정

    ```yaml
    spring:
    profiles: stage
    kafka:
      bootstrap-servers: hw-chart-kafka-0.hw-chart-kafka-headless.default.svc.cluster.local:9092
    ```

### mysql 설정

- mysql 터미널에서 DB 및 사용자 생성 등 작업을 미리 해둔다.

    `mysql -uroot -p;`

    `create database orders;`

    `CREATE USER 'order_admin'@'%' IDENTIFIED BY 'hanwool';` // 앞에서 values에 입력한 사용자는 생략

    `GRANT ALL PRIVILEGES ON orders.* TO 'order_admin'@'%';`

- 로컬 port를 pod port로 포워딩하기
    - `kubectl port-forward service/hw-chart-mysql :3306`

        ```yaml
        Forwarding from 127.0.0.1:62461 -> 3306
        Forwarding from [::1]:62461 -> 3306
        ```

    - 3306:3306으로 직접 port를 지정해줘도 되지만, 로컬에 설치된 mysql의 port와 충돌을 방지하려면 위처럼 kubectl이 직접 남은 port를 지정해준 것을 쓰는 것이 좋다.
    이제 로컬에서 127.0.0.1:62461 로 접속이 가능하다.
    - 참고
        - [https://kubernetes.io/ko/docs/tasks/access-application-cluster/port-forward-access-application-cluster/](https://kubernetes.io/ko/docs/tasks/access-application-cluster/port-forward-access-application-cluster/)
        - kubectl port-forward 는 프롬프트를 리턴하지 않으므로, 이 연습을 계속하려면 다른 터미널을 열어야 한다.

### skaffold

상용서비스가 아닌 프로젝트들은 앞서 설치한 cloud code를 통해 제공하는 skaffold를 이용하는 것이 개발시 편한 것 같다.
helm으로 상용서비스들은 띄워놓고, 인텔리j에서는 개발 프로젝트만 따로 skaffold.yaml 으로 관리하면 쉽게 따로 배포/중지가 가능하다.

- Intellij : File > Setting > Plugins > Cloud Code 플러그인 설치
- skaffold 설정
    - Tools > Cloud Code > Kubernetes > Add Kubernetes Support
    - kubernetes-manifests 폴더를 생성하고 프로젝트별 yaml 파일 생성.
    - skaffold.yaml에 프로젝트별 yaml 파일 추가 (secret과 configmap 파일도 추가 가능하다.)

        ```yaml
        apiVersion: skaffold/v2beta10
        kind: Config
        metadata:
          name: msa-e-commerce
        build:
          artifacts:
          - image: mall-composite
            context: services\mall-composite
            docker: {}
          - image: order
            context: services\order-service
            docker: {}
        deploy:
          kubectl:
            manifests:
            - kubernetes-manifests/mall-composite.yaml
            - kubernetes-manifests/order-service.yaml
            - kubernetes-manifests/order-secret.yaml
            - kubernetes-manifests/order-configmap.yaml
            - kubernetes-manifests/composite-ingress.yaml
        ```

- mysql secret의 password등의 정보를 env로 application.yaml에 전달하여 설정한다.

    ```yaml
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: order-deployment
      labels:
        app: order
    spec:
      replicas: 1
      selector:
        matchLabels:
          app: order
      template:
        metadata:
          labels:
            app: order
        spec:
          containers:
            - name: order
              image: order
              ports:
                - containerPort: 8077
              resources:
                limits:
                  memory: 350Mi
              env:
                - name: SPRING_PROFILES_ACTIVE
                  value: stage
                - name: TZ
                  value: Asia/Seoul
                - name: DB_HOST
                  valueFrom:
                    configMapKeyRef:
                      name: order-configmap
                      key: db_host
                - name: DB_NAME
                  valueFrom:
                    configMapKeyRef:
                      name: order-configmap
                      key: db_name
                - name: DB_USERNAME
                  valueFrom:
                    secretKeyRef:
                      name: order-secret
                      key: username
                - name: DB_PASSWORD
                  valueFrom:
                    secretKeyRef:
                      name: hw-chart-mysql
                      key: mysql-password
    ```

    ```yaml
    spring:
      profiles: stage
      datasource:
        url: jdbc:mysql://${DB_HOST}/${DB_NAME}
        username: ${DB_USERNAME}
        password: ${DB_PASSWORD}
    ```

### nginx ingress를 통해 외부 트래픽 받기

---

쿠버네티스에서는 ingress controller를 게이트웨이로 사용하여 내부 Pod에 접근할 수 있다.

1. nginx ingress 설치 (controller)

    ingress로 설치하여 외부 접근이 가능하게 노출한다.

    `helm repo add ingress-nginx [https://kubernetes.github.io/ingress-nginx](https://kubernetes.github.io/ingress-nginx)`

    `helm repo update`

    `helm install ingress-nginx ingress-nginx/ingress-nginx --namespace order-space`

    설정 확인

    EXTERNAL-IP가 <Pending> 상태이면, IP를 사용하기 위해서는 조금 기다려야 하는데, 

    `kubectl --namespace default get services -o wide -w ingress-nginx-controller`

    설치시 서비스 타입이 LoadBalancer로 설정되어있는데, 로컬 테스트에서는 GCP나 AWS 등의 클라우드에서 로드발란서 ip를 제공 받는 것이 아니기 때문에  type을 NodePort로 바꾸어 외부 트래픽을 받을 수 있도록 한다.

    ```java
    controller:
      service:
        type: NodePort
    ```

    `helm upgrade -f G:\1_Study\1mine\msa-e-commerce\kubernetes-manifests\custom-nginx.yaml ingress-nginx ingress-nginx/ingress-nginx`

    minikube에서는 아래 명령어로 ip를 바인딩 해주면 브라우저가 자동으로 띄워진다. 

    `minikube service ingress-nginx-controller -n order-space`

2. 또는 ingress 애드온을 활성화 한후 tunnel 명령어로 external IP를 노출시켜준다.

`minikube addons enable ingress`

> After the addon is enabled, please run "minikube tunnel" and your ingress resources would be available at "127.0.0.1"

`minikube tunnel`

- 참고 : [https://minikube.sigs.k8s.io/docs/handbook/accessing/](https://minikube.sigs.k8s.io/docs/handbook/accessing/)

- 서비스 ingress 설정

    ```java
    apiVersion: networking.k8s.io/v1
    kind: Ingress
    metadata:
      name: composite-ingress
    	namespace: order-space
      annotations:
        nginx.ingress.kubernetes.io/rewrite-target: /
    spec:
      rules:
        - http:
            paths:
              - path: /composite
                pathType: Prefix
                backend:
                  service:
                    name: mall-composite
                    port:
                      number: 80
    ```

# 추가 고려 사항

---

- Schema Registry
- Mapstruct
- Circuit Breaker
- jwt, oauth2.0
- Envers
- kafka manager

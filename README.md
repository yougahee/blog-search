# 🔎 BLOG Search API SERVER
- 작성자 : gahyu 
- 작성일자 : 23/03/21

<br>

## 📌 사용 프레임워크 및 DB
- SpringBoot
- h2 DB

<br>

## 📌 사용 라이브러리
#### 1) feign client 
   - kakao, naver 오픈 소스 API 연동

#### 2) lombok
   - @getter, @setter, @NoArgsConstructor 사용을 위함.

#### 3) JPA
   - 사용 목적 :  DB 컨트롤 

#### 4) common-lang3
   - 사용 목적 : StringUtils 사용

#### 5) spring-boot-starter-test
- 테스트 코드

<br> 

## 📌 API 명세서
- 🔗 [명세서 바로가기](https://github.com/yougahee/blog-search/wiki/%EB%B8%94%EB%A1%9C%EA%B7%B8-%EA%B2%80%EC%83%89-API)
- 🔗 [블로그 검색 API](https://github.com/yougahee/blog-search/wiki/%EB%B8%94%EB%A1%9C%EA%B7%B8-%EA%B2%80%EC%83%89-API)
- 🔗 [인기 검색어 API](https://github.com/yougahee/blog-search/wiki/%EC%9D%B8%EA%B8%B0-%EA%B2%80%EC%83%89%EC%96%B4-%EC%A1%B0%ED%9A%8C-API)

<br>

## 📌 기능 구현
### 1. 블로그 검색 API
#### 기본 컨셉 : 카카오 블로그 검색 API 연동
- Fallback 
  - 만약, 카카오 블로그 검색 API 오류 발생 시, 네이버 블로그 검색 API 연동
- 인기 검색어 수집을 위한 비동기 처리
  - 해당 API로 들어온 검색어는 비동기 처리를 통해 저장

### 2. 인기 검색어 API
위 블로그 검색을 통해 들어온 검색어의 검색횟수를 바탕으로 인기 검색어 상위 10개 리스트 제공
- 메인 > In memory DB : h2 Database 사용
  - ex) RDB
- 서브 > 로컬 메모리 사용
  - ex) 외부 DB를 사용한다면 redis 사용

### * 주요 고민 포인트
1. 인기 검색어 API 요청이 들어올 때마다 H2 DB > 쿼리 조회 > 대용량 API 호출 시 DB부하
   - 생각1) 인기 검색어 API - @Cachable / 검색 API - @CacheEvict
     - 대용량 서비스 > 검색이 정말 많은 경우라면, 크게 개선되지 않음.

2. 검색어 수집 기준
   - 검색 API controller 단의 커스텀 애노테이션을 생성하여 AOP로 개발 고민  
     - Bad Request(Invalid Parameter 등) 의 경우는 검색어 수집 기준에서 제외
     - Kakao API 오류 또는 검색어 결과가 없는 경우는 검색어 수집
   - 즉, 서비스 단에 들어오면 검색어 수집하는 것이 좋다는 판단으로 인해 서비스 단의 비동기 작업으로 추가

<br>

## 📌 dependencies
```
	implementation 'org.springframework.boot:spring-boot-starter-web'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'

	// lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'

	// lombok 테스트 의존성 추가
	testCompileOnly 'org.projectlombok:lombok:1.18.12'
	testAnnotationProcessor 'org.projectlombok:lombok:1.18.12'

	// feign
	implementation 'org.springframework.cloud:spring-cloud-starter-openfeign'
    implementation 'org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j'

	// jpa
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

	// db
	runtimeOnly 'com.h2database:h2'

	// utils
	implementation group: 'org.apache.commons', name: 'commons-lang3', version: '3.0'
	implementation group: 'org.hibernate', name: 'hibernate-validator', version: '7.0.5.Final'

	// test
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

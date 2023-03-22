# 🔎 BLOG Search API SERVER
- 작성자 : gahyu 
- 작성일자 : 23/03/21

<br>

## 📌 Project Intoduction
- 블로그 검색 및 인기 검색어 제공 서버

- ### Executable JAR file 
	- 🔗 [바로가기](https://github.com/yougahee/blog-search/tree/master/excutable_jar)
	- 실행 명령어
		- ```
			java -jar blog-search-gahyu.jar
	  		```
- ### API 명세서
	- 🔗 [명세서 바로가기](https://github.com/yougahee/blog-search/wiki)
	- 🔗 [블로그 검색 API](https://github.com/yougahee/blog-search/wiki/%EB%B8%94%EB%A1%9C%EA%B7%B8-%EA%B2%80%EC%83%89-API)
	- 🔗 [인기 검색어 API](https://github.com/yougahee/blog-search/wiki/%EC%9D%B8%EA%B8%B0-%EA%B2%80%EC%83%89%EC%96%B4-%EC%A1%B0%ED%9A%8C-API)
<br>


## 📌 Framework & DB
- SpringBoot 2.6.6 
- h2 DB

<br>

## 📌 Library
order | Library       |  사용 목적
--- | ------------- | ------------------------------
1 | Feign | kakao, naver 오픈 소스 API 연동을 위한 HTTP Client
2 | lombok | @getter, @setter, @NoArgsConstructor 사용 등으로 코드 간략화
3 | JPA | DB 컨트롤
4 | common-lang3 | StringUtils.isNull / notNull 등 문자열 관련 utils 함수 사용
5 | spring-boot-starter-test | 테스트 코드 작성


<br>

## 📌 Project Details
### 1. 멀티 모듈 구성 및 모듈간 의존성 제약
- 프로젝트의 확장성을 고려하여 함께 사용할 수 있는 DTO domain 은 search-core 에서 관리하도록 구성

### 2. 블로그 검색 API
> 기본 컨셉 : 카카오 블로그 검색 오픈 API 를 통한 검색 리스트 제공
- Fallback 
  - 카카오 블로그 검색 API 오류 발생 시, 네이버 블로그 검색 API 연동하도록 Fallback 구현
  - 네이버 블로그 검색 API 오류 시, 검색 API 응답은 에러를 던지지 않고, 빈 리스트 return
- 인기 검색어 수집을 위한 비동기 처리
  - 해당 API로 들어온 검색어는 비동기 처리를 통해 저장

### 3. 인기 검색어 API
> 위 블로그 검색을 통해 들어온 검색어의 검색횟수를 바탕으로 인기 검색어 상위 10개 리스트 제공
- 메인DB(h2)와 서브DB(로컬 메모리)를 사용하여 인기 검색어 상위 10개 리스트 제공
- 검색어 저장
	- 블로그 검색 API 를 통해 들어온 검색어를 h2 와 로컬 메모리에 저장한다.
	- 해당 작업은 비동기 작업으로 블로그 검색 API 응답 시간에 영향을 주지 않도록 개발
	- 메인 DB 정보를 먼저 저장 완료 한 후 로컬 메모리에 저장하여 데이터 동기화
- 스케줄링
	- 검색 요청이 많아, 데이터가 많아지면 로컬 메모리의 저장 공간이 부족을 고려하여 5분마다 메인 DB에서 조회하여 refresh
		- 메인 DB에서 상위 1000개의 검색어와 검색횟수를 조회해와서 저장
	- 스케줄링이 되는 동안 로컬과 메인 DB 간의 동기화 꺠질 수 있는 점을 고려해 메인 DB 정보를 먼저 저장 후 로컬 메모리에 저장하여 데이터 동기화
	- 스케줄링이 진행되는 동안, 인기 검색어의 로컬 데이터가 변경될 수 있는 점을 고려하여 커스텀 애노테이션 추가
		- lock 을 걸어 스케줄링이 진행되는 동안 인기 검색어 조회에 lock
- 인기 검색어 조회
	- 커스텀 애노테이션 > lock 을 걸어 인기 검색어 동기화 처리

```
- 메인 > In memory DB : h2 Database 사용
  - ex) RDB
- 서브 > 로컬 메모리 사용
  - ex) 외부 DB를 사용한다면 redis 사용
```

<br> 

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

## 📌 Appendix
### dependencies
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

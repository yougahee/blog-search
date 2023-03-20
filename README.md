# BLOG Search API SERVER
### 작성자 : gahyu

## 📌 사용 프레임워크 및 DB
- SpringBoot
- h2


## 📌 사용 라이브러리
1) feign client 
   - 사용 목적 : kakao, naver 오픈 소스 API 연동
<br> </br>

2) lombok
   - 사용 목적 : @getter, @setter, @NoArgsConstructor 사용을 위함.
   <br> </br>

3) JPA
   - 사용 목적 :  DB 컨트롤 
   <br> </br>

4) common-lang3
   - 사용 목적 : StringUtils 사용
<br> </br>

     

## 📌 API 목록
- [명세서 바로가기](https://github.com/yougahee/blog-search/wiki/%EB%B8%94%EB%A1%9C%EA%B7%B8-%EA%B2%80%EC%83%89-API)
1) 블로그 검색 API
2) 인기 검색어 API


## 📌 dependencies
```
	implementation 'org.springframework.boot:spring-boot-starter-web'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'

	// lombok
	compileOnly 'org.projectlombok:lombok'
	annotationProcessor 'org.projectlombok:lombok'

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
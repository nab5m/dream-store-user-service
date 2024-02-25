# 준영GOAT 꿈백화점 UserService

## ACTIVE_PROFILE 설정
- SPRING_PROFILES_ACTIVE 환경 변수로 설정 가능
- develop-h2 (기본) : h2 데이터베이스 사용, 로컬 개발 환경이나 테스트 실행 시 사용 가능
- develop-mysql : MySQL 데이터베이스 사용, 로컬 개발 환경에서 반영구적으로 테스트 데이터를 저장하기 위해 사용
- production: 배포 서버에서 사용되는 프로필

## 환경 변수 설정
1. redis 설정
   - spring-redis-host=<로컬/테스트 레디스 주소>
   - spring-redis-port=<로컬/테스트 레디스 포트>
2. spring-cloud AWS 키 설정
   - AWS_REGION=<AWS 리전>
   - spring-cloud-aws-credentials-access-key=<AWS 엑세스 키>
   - spring-cloud-aws-credentials-secret-key=<AWS 시크릿 키>
   - spring-cloud-aws-sqs-region=<AWS SQS 리전>
2. (선택) mysql 사용 시
   - SPRING_PROFILES_ACTIVE=develop-mysql
   - spring-datasource-url=<로컬/테스트 데이터베이스 주소>
     - 예시 : jdbc:mysql://127.0.0.1:3306/junyounggoat?serverTimezone=UTC&characterEncoding=UTF-8
   - spring-datasource-username=<데이터베이스 사용자 이름>
   - spring-datasource-password=<데이터베이스 비밀번호>
3. (선택) 카카오 로그인 설정
   - kakao-login-redirect-uri=<카카오 로그인 시 사용할 redirect-uri>
   - kakao-rest-api-key=<카카오 rest api 키>
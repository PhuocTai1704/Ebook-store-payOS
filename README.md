# Ebook-store-payOS

Dự án gồm **frontend** và **backend**.

## Yêu cầu

* Node.js 22+ (frontend)
* Java 21 (backend)
* Maven
* MySQL

## Cấu trúc thư mục

```
Ebook-store-payOS/
├─ Ebook-FE/   # React + Vite
├─ Ebook-BE/   # Spring Boot
└─ README.md
```

## Cấu hình biến môi trường (Backend)

Chỉ chỉnh các giá trị cần thay đổi, các giá trị khác giữ mặc định trong `application.properties`:

### Database

```
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ebook
DB_USERNAME=root
DB_PASSWORD=...
```

### PayOS

```
PAYOS_CLIENT_ID=...
PAYOS_API_KEY=...
PAYOS_CHECKSUM_KEY=...
```

### Email

```
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=...
```

## Chạy dự án

### Frontend

```
cd Ebook-FE
npm install
npm run dev
```

### Backend

```
cd Ebook-BE
mvn clean spring-boot:run
```

## Build

### Frontend

```
cd Ebook-FE
npm run build
```

### Backend

```
cd Ebook-BE
mvn clean package
java -jar target/backend.jar
```

## Git commit mẫu

```
fix: fix upload image and add email feature
```

## License

MIT / GPL

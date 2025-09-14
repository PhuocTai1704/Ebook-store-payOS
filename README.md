# Ebook-store-payOS

Dự án gồm frontend và backend.

## Yêu cầu
- Node.js 22+ (frontend)
- Java 21 (backend)
- Maven
- MySQL 

## Cấu trúc thư mục
Ebook-store-payOS/
├─ Ebook-FE/ # React + Vite
├─ Ebook-BE/ # Spring Boot
└─ README.md

### Cấu hình biến môi trường
## Cấu hình Backend
# Database (thay đổi cho phù hợp môi trường của bạn)
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ebook
DB_USERNAME=root
DB_PASSWORD=...

# PayOS (điền giá trị thật của bạn)
PAYOS_CLIENT_ID=...
PAYOS_API_KEY=...
PAYOS_CHECKSUM_KEY=...

# Email (thay username/password thật của bạn)
EMAIL_USERNAME=your_email@gmail.com
EMAIL_PASSWORD=...

## Chạy dự án

### Frontend
cd Ebook-FE
npm install
npm run dev
### Backend
cd Ebook-BE
mvn clean spring-boot:run

## Build

### Frontend
cd frontend
npm run build
### Backend
cd backend
mvn clean package
java -jar target/backend.jar

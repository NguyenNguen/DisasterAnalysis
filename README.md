# Disaster Analysis

![Java](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=java)
![JavaFX](https://img.shields.io/badge/JavaFX-Desktop-blue.svg?style=flat-square)
![SQLServer](https://img.shields.io/badge/Database-SQL_Server-red.svg?style=flat-square&logo=microsoftsqlserver)
![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-brightgreen.svg?style=flat-square)

> Ứng dụng dữ liệu mạng xã hội để nâng cao hiệu quả trong logistics nhân đạo.

---

## Giới thiệu

Trong bối cảnh thảm họa thiên nhiên ngày càng gia tăng, việc nắm bắt tình hình thực địa và tâm lý người dân là vô cùng cấp bách. Dự án **Disaster Analysis** được xây dựng nhằm tự động hóa luồng dữ liệu (Data Pipeline) từ khâu thu thập trên mạng xã hội, tiền xử lý, cho đến phân tích ngôn ngữ tự nhiên (NLP) và xuất báo cáo. 

Hệ thống giúp các tổ chức cứu trợ theo dõi sự thay đổi tâm lý công chúng và thống kê mức độ thiệt hại theo thời gian thực, từ đó tối ưu hóa việc phân bổ nguồn lực và hỗ trợ ra quyết định trong logistics nhân đạo.

## Tính năng nổi bật

* Thu thập & Tiền xử lý dữ liệu: Tự động cào dữ liệu (bài đăng, bình luận) từ YouTube và các trang báo điện tử (VnExpress, Dantri, v.v.) dựa trên từ khóa tùy chỉnh.
* Phân tích cảm xúc (Sentiment Analysis): Theo dõi sự biến động tâm lý công chúng (Tích cực, Tiêu cực, Trung lập) theo các mốc thời gian (Giờ, Ngày, Tuần, Tháng).
* Phân loại thiệt hại (Damage Classification): Tự động phân nhóm văn bản thành các hạng mục thiệt hại: Cơ sở hạ tầng, Nhà cửa, Tài sản cá nhân, Người bị ảnh hưởng, v.v.
* Tích hợp AI (AI Insights): Kết nối với LLMs (Google Gemini, OpenAI) để tự động phân tích chuyên sâu, tạo báo cáo tổng hợp và đề xuất giải pháp cứu trợ.
* Trực quan hóa & Xuất báo cáo: Giao diện JavaFX hiện đại với các biểu đồ động (Bar chart, Line chart) và hỗ trợ xuất toàn bộ dữ liệu ra tệp Excel (`.xlsx`).

## Kiến trúc Hệ thống

Dự án chú trọng vào thiết kế phần mềm sạch, áp dụng triệt để các nguyên lý OOP và Design Patterns mà không phụ thuộc vào các framework lớn (như Spring Boot):

* **Kiến trúc cốt lõi:** **Hexagonal Architecture (Ports and Adapters)** kết hợp **Layered Architecture**. Tầng nghiệp vụ (Domain) được cô lập hoàn toàn khỏi các chi tiết hạ tầng (Database, UI, External APIs).
* **Design Patterns nổi bật:** *Creational:* Singleton, Factory Method.
  * *Structural:* Adapter, DTO (Data Transfer Object).
  * *Behavioral:* Strategy, Repository.
  * *Other:* Custom Dependency Injection (thông qua class `ApplicationContext`).
* **Xử lý đồng thời (Concurrency):** Sử dụng `JavaFX Task` và Thread Pools để xử lý bất đồng bộ các tác vụ nặng (cào API, truy vấn DB, gọi AI), đảm bảo UI luôn mượt mà.

## Công nghệ sử dụng (Tech Stack)

* **Ngôn ngữ:** Java 21
* **Giao diện:** JavaFX, FXML
* **Cơ sở dữ liệu:** Microsoft SQL Server, HikariCP (Connection Pool)
* **Thư viện & API:** Google YouTube Data API v3, Jsoup (Web Scraping)
  * Apache POI (Xuất file Excel)
  * Stanford CoreNLP (Tiền xử lý ngôn ngữ tự nhiên)
  * Jackson (Xử lý JSON), Dotenv-java (Quản lý biến môi trường)

## Hướng dẫn cài đặt (Getting Started)

### Yêu cầu hệ thống (Prerequisites)
* Java JDK 21 trở lên.
* Microsoft SQL Server đang chạy (Local hoặc Remote).
* API Keys hợp lệ (YouTube Data API, Google Gemini API / OpenAI API).

### Các bước cài đặt (Installation)

1. **Clone repository:**
   ```bash
   git clone [https://github.com/yourusername/disaster-social-analysis.git](https://github.com/yourusername/disaster-social-analysis.git)
   cd disaster-social-analysis

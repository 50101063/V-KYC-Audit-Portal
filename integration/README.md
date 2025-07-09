# V-KYC Audit Portal - Integration Documentation

This document outlines the integration strategy, configuration details, and key interaction points between the frontend, backend, and database components of the V-KYC Audit Portal. It serves as a guide for ensuring seamless communication and data flow across the system.

## 1. Overview of Integration Points

The V-KYC Audit Portal's architecture involves three main components:

*   **Frontend (React Application):** The user interface for interacting with the system.
*   **Backend (Spring Boot API):** The core application logic, exposing RESTful APIs.
*   **Database (PostgreSQL):** Stores V-KYC recording metadata.
*   **External Systems:** LTF NFS Server (for video files) and AWS S3 (for bulk downloads).

The integration ensures that:
*   Frontend can securely call Backend APIs.
*   Backend can connect to and interact with the PostgreSQL database.
*   Backend can securely access video files from the NFS server.
*   Backend can manage temporary bulk download files in AWS S3.

## 2. API Endpoints and Data Contracts

The Backend API exposes the following endpoints (as detailed in `backend/README.md`):

*   **`GET /api/v1/records`**: Search and retrieve V-KYC recording metadata.
    *   **Parameters:** `lanId`, `date`, `month`, `page`, `size`.
    *   **Response:** Paginated list of `RecordMetadata` objects.
*   **`GET /api/v1/records/{lanId}/download`**: Download a single V-KYC video recording.
    *   **Parameters:** `lanId` (path variable).
    *   **Response:** Streams the video file.
*   **`POST /api/v1/bulk-download/upload`**: Upload a CSV/TXT file containing LAN IDs for bulk download.
    *   **Request Body:** Multipart file containing LAN IDs.
    *   **Response:** Initial processing status and metadata for requested LAN IDs.
*   **`GET /api/v1/bulk-download/download/{filename}`**: Download the zipped bulk video file from S3.
    *   **Parameters:** `filename` (path variable, typically a temporary S3 key).
    *   **Response:** Streams the zipped file.

**Data Contract (`RecordMetadata` / `vkyc_records` table):**

The `RecordMetadata` entity in the backend (Java) must precisely map to the `vkyc_records` table in PostgreSQL. Key fields include:

| Backend Field (Java)      | Database Column (PostgreSQL) | Type (Java) | Type (SQL)      | Description                                     |
| :------------------------ | :--------------------------- | :---------- | :-------------- | :---------------------------------------------- |
| `lanId`                   | `lan_id`                     | `String`    | `VARCHAR(255)`  | Unique LAN ID (Primary Key)                     |
| `userId`                  | `user_id`                    | `String`    | `VARCHAR(255)`  | User ID associated with the recording           |
| `callDurationMinutes`     | `call_duration_minutes`      | `Integer`   | `INTEGER`       | Duration of the call in minutes                 |
| `status`                  | `status`                     | `String`    | `VARCHAR(50)`   | Recording status (e.g., APPROVED)               |
| `callTime`                | `call_time`                  | `LocalTime` | `TIME`          | Time of the call                                |
| `callDate`                | `call_date`                  | `LocalDate` | `DATE`          | Date of the call                                |
| `nfsVkycUploadTime`       | `nfs_vkyc_upload_time`       | `LocalDateTime` | `TIMESTAMP`     | Timestamp of upload to NFS                      |
| `nfsFilePath`             | `nfs_file_path`              | `String`    | `VARCHAR(1024)` | Full path to the video file on the NFS server   |

**Note for Backend Developer:** Ensure your `RecordMetadata.java` entity uses `@Column(name = "column_name")` annotations where necessary to correctly map camelCase Java fields to snake_case database columns (e.g., `nfsVkycUploadTime` to `nfs_vkyc_upload_time`).

## 3. Configuration for Integration

### 3.1 Backend Configuration (`backend/src/main/resources/application.properties`)

The backend Spring Boot application requires specific configurations to connect to the database and interact with AWS S3. These properties should ideally be managed via environment variables in production.

A template file `integration/backend_config_template.properties` is provided. You will need to copy its contents to `backend/src/main/resources/application.properties` and fill in the actual values.

```properties
# Database Configuration (PostgreSQL)
spring.datasource.url=jdbc:postgresql://<your-db-host>:5432/<your-db-name>
spring.datasource.username=<your-db-username>
spring.datasource.password=<your-db-password>
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
# IMPORTANT: Set ddl-auto to 'none' as database schema is managed by 'database/schema.sql'
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# AWS S3 Configuration for Bulk Downloads
aws.s3.bucket-name=<your-s3-bucket-name>
aws.region=<your-aws-region> # e.g., us-east-1

# NFS Configuration (Conceptual - actual path will be in DB)
# If a base NFS mount point is needed for the application to access the NFS server, define it here.
# Otherwise, the nfs_file_path from the database will be directly used.
# nfs.base.path=/mnt/nfs/vkyc-recordings

# Server Port
server.port=8080
```

### 3.2 Frontend Configuration (Environment Variables)

The frontend React application needs to know the base URL of the backend API. This should be configured via environment variables during the build process.

A template file `integration/frontend_env_template.js` is provided. You can adapt this for your frontend build system (e.g., `.env` file for Create React App/Vite, or a configuration object).

**Example (`.env` file for React projects):**

```dotenv
# Frontend Environment Variables
REACT_APP_BACKEND_API_BASE_URL=http://localhost:8080/api/v1
# In production, this would be your deployed backend API Gateway/Load Balancer URL
# REACT_APP_BACKEND_API_BASE_URL=https://api.yourdomain.com/api/v1
```

**Note for Frontend Developer:** Access this variable in your React application (e.g., `process.env.REACT_APP_BACKEND_API_BASE_URL`).

## 4. Sync Points and Data Flow

### 4.1 Frontend to Backend Communication

*   All user actions (search, upload, download click) on the frontend trigger HTTP requests to the backend API.
*   Frontend should handle loading states, success messages, and error responses from the backend gracefully.
*   Authentication tokens (e.g., JWTs) obtained from the Identity Provider should be included in the `Authorization` header of all backend API requests.

### 4.2 Backend to Database Interaction

*   The `RecordRepository` in the backend uses JPA to interact with the `vkyc_records` table.
*   Ensure the `RecordMetadata` entity accurately reflects the database schema, including `@Column` annotations for snake_case mapping.
*   Database connection details are configured in `application.properties`.

### 4.3 Backend to NFS Server Interaction

*   The `RecordService` will contain logic to read video files from the `nfs_file_path` stored in the database.
*   This interaction assumes a secure network connection (VPN/Direct Connect) is established between the backend's hosting environment (e.g., AWS ECS) and the on-premises LTF NFS server.
*   The backend will stream these files directly to the frontend for individual downloads.

### 4.4 Backend to AWS S3 Interaction

*   For bulk downloads, the backend's `RecordService` will:
    1.  Fetch multiple video files from NFS.
    2.  Zip them into a single archive.
    3.  Upload the zipped file to the configured AWS S3 bucket.
    4.  Generate a time-limited pre-signed URL for the S3 object.
*   The frontend will receive this pre-signed URL and initiate a direct download from S3, offloading the backend.

## 5. Testing Integration

To ensure successful integration, the following tests should be performed:

*   **End-to-End API Tests:**
    *   Verify that the frontend can successfully call all backend API endpoints (`/records`, `/download`, `/upload`, `/bulk-download`).
    *   Check correct request/response formats and status codes.
*   **Database Connectivity Tests:**
    *   Ensure the backend can connect to the PostgreSQL database.
    *   Verify that data can be read from and written to the `vkyc_records` table correctly.
*   **NFS File Access Tests:**
    *   Confirm the backend can read video files from the configured NFS paths.
    *   Test individual video download functionality end-to-end.
*   **S3 Bulk Download Tests:**
    *   Test the bulk LAN ID upload process, ensuring the backend zips files, uploads to S3, and generates a valid pre-signed URL.
    *   Verify that the frontend can use the pre-signed URL to download the zipped file directly from S3.
*   **Authentication & Authorization Tests:**
    *   Ensure only authorized users can access the system and perform specific actions.
    *   Test invalid credentials and unauthorized access attempts.

## 6. Future Enhancements

*   **Centralized Configuration Management:** For production, consider using AWS Systems Manager Parameter Store or AWS Secrets Manager for managing sensitive configurations (database credentials, API keys) securely.
*   **Message Queues:** For more robust asynchronous bulk processing, consider integrating a message queue (e.g., AWS SQS) between the API endpoint and the actual file processing logic.
*   **Monitoring & Alerting:** Implement comprehensive monitoring of API performance, database health, and file access patterns using CloudWatch and related AWS services.
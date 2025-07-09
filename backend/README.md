# V-KYC Audit Portal - Backend Service

This directory contains the backend service for the V-KYC Audit Portal, implemented using Spring Boot (Java).

## Technologies Used

*   **Language:** Java 17+
*   **Framework:** Spring Boot 3.x
*   **Build Tool:** Maven
*   **API Style:** RESTful APIs
*   **Database Interaction:** Spring Data JPA (placeholder, actual integration with PostgreSQL will be handled by Database Developer)
*   **File Handling:** Java NIO / Apache Commons IO (for interacting with NFS and creating zips)

## Setup and Execution Instructions

### Prerequisites

*   Java Development Kit (JDK) 17 or higher
*   Maven 3.6.x or higher
*   (Optional) An IDE like IntelliJ IDEA or Eclipse with Spring Boot support

### 1. Clone the Repository

If you haven't already, clone the main project repository:

```bash
git clone https://github.com/50101063/V-KYC-Audit-Portal.git
cd V-KYC-Audit-Portal
```

### 2. Navigate to the Backend Directory

```bash
cd backend
```

### 3. Build the Application

Build the Spring Boot application using Maven:

```bash
mvn clean install
```

### 4. Run the Application

You can run the application using the Spring Boot Maven plugin:

```bash
mvn spring-boot:run
```

Alternatively, you can run the generated JAR file:

```bash
java -jar target/vkyc-audit-portal-0.0.1-SNAPSHOT.jar
```

The application will start on port `8080` by default (unless configured otherwise in `application.properties`).

## API Endpoints

The backend exposes RESTful APIs for V-KYC recording search and download.

### 1. Search and Display V-KYC Records

*   **Endpoint:** `GET /api/v1/records`
*   **Description:** Retrieves a paginated list of V-KYC recording metadata based on search criteria.
*   **Query Parameters:**
    *   `lanId` (Optional): Filter by LAN ID.
    *   `date` (Optional): Filter by date (e.g., `YYYY-MM-DD`).
    *   `month` (Optional): Filter by month (e.g., `MM`).
    *   `page` (Optional): Page number (default: 0).
    *   `size` (Optional): Number of records per page (default: 10).
*   **Example Request:**
    ```
    GET http://localhost:8080/api/v1/records?lanId=C08383828282882&page=0&size=10
    ```
*   **Example Response (simulated):**
    ```json
    {
      "content": [
        {
          "userId": "C08383828282882",
          "callDuration": "0:05:54",
          "status": "APPROVED",
          "time": "9:54:45 PM",
          "date": "23-02-2025",
          "nfsVkycUploadTime": "2025-02-24",
          "nfsPath": "/simulated_nfs/C08383828282882.mp4"
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false,
        "offset": 0,
        "sort": {
          "empty": true,
          "sorted": false,
          "unsorted": true
        }
      },
      "last": true,
      "totalElements": 1,
      "totalPages": 1,
      "size": 10,
      "number": 0,
      "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
      },
      "first": true,
      "numberOfElements": 1,
      "empty": false
    }
    ```

### 2. Individual Video Download

*   **Endpoint:** `GET /api/v1/records/{lanId}/download`
*   **Description:** Downloads a single V-KYC recording file based on its LAN ID.
*   **Path Variable:**
    *   `lanId` (Required): The LAN ID of the recording to download.
*   **Example Request:**
    ```
    GET http://localhost:8080/api/v1/records/C08383828282882/download
    ```
*   **Response:** Binary stream of the video file.

### 3. Bulk LAN ID Upload & Bulk Download

*   **Endpoint:** `POST /api/v1/bulk-download/upload`
*   **Description:** Uploads a CSV/TXT file containing a list of LAN IDs (min 2, max 50). The backend processes these, zips the corresponding video files, and provides a download link (simulated for now, would be a pre-signed S3 URL in production).
*   **Request Body:** `multipart/form-data` with a file part named `file`.
*   **Example Request (using curl):**
    ```bash
    curl -X POST -F "file=@/path/to/your/lan_ids.csv" http://localhost:8080/api/v1/bulk-download/upload
    ```
    *   `lan_ids.csv` content example:
        ```
        C08383828282882
        C02503254240258405
        ```
*   **Example Response (simulated - would include a download URL for a real implementation):**
    ```json
    {
      "message": "Bulk download processing initiated for 2 LAN IDs. A download link will be provided shortly.",
      "requestedLanIds": [
        "C08383828282882",
        "C02503254240258405"
      ],
      "simulatedDownloadLink": "http://localhost:8080/api/v1/bulk-download/download/simulated_bulk_20250327.zip"
    }
    ```
*   **Simulated Bulk Download Endpoint:** `GET /api/v1/bulk-download/download/{filename}`
    *   **Description:** Placeholder for downloading the simulated zipped bulk file. In a real scenario, this would be a pre-signed S3 URL.
    *   **Example:**
        ```
        GET http://localhost:8080/api/v1/bulk-download/download/simulated_bulk_20250327.zip
        ```

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── vkyc/
│   │   │           └── auditportal/
│   │   │               ├── VkycAuditPortalApplication.java (Main Spring Boot application)
│   │   │               ├── controller/
│   │   │               │   └── RecordController.java (REST API endpoints)
│   │   │               ├── service/
│   │   │               │   └── RecordService.java (Business logic)
│   │   │               └── repository/
│   │   │                   └── RecordRepository.java (Data access interface)
│   │   └── resources/
│   │       └── application.properties (Application configuration)
│   └── test/
│       └── ... (Unit and integration tests)
├── pom.xml (Maven project configuration)
└── README.md (This file)
```

## Collaboration Notes

*   **Frontend Developer:** Please refer to the API Endpoints section for integration details. Coordinate on data formats and error handling.
*   **Database Developer:** The `RecordRepository` is a placeholder. Actual JPA entities and database schema will be defined and managed by you. The `RecordService` will interact with the database via this repository.
*   **NFS Integration:** The `RecordService` and `RecordController` contain simulated logic for NFS interaction and file zipping. The actual implementation will require secure connectivity to the LTF NFS server as per the Solution Architect's design.

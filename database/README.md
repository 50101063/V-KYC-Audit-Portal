# Database Solution - V-KYC Audit Portal

This folder contains all database-related code and scripts for the V-KYC Audit Portal project. The database is designed to store metadata for V-KYC recordings, enabling efficient search, filtering, and retrieval by the backend services.

## 1. Technologies Used

*   **Database System:** PostgreSQL 15.x
*   **Schema Definition:** SQL scripts

## 2. Database Schema

The core database schema is defined in `schema.sql`. It includes the `vkyc_records` table, which stores essential metadata for each V-KYC recording.

### `vkyc_records` Table Schema:

| Column Name           | Data Type      | Constraints           | Description                                    |
| :-------------------- | :------------- | :-------------------- | :--------------------------------------------- |
| `id`                  | `SERIAL`       | `PRIMARY KEY`         | Unique identifier for each record.             |
| `lan_id`              | `VARCHAR(255)` | `UNIQUE`, `NOT NULL`  | Loan Account Number, unique identifier for a V-KYC case. |
| `call_duration_minutes` | `INTEGER`      |                       | Duration of the V-KYC call in minutes.         |
| `status`              | `VARCHAR(50)`  | `NOT NULL`            | Status of the V-KYC call (e.g., 'APPROVED').   |
| `call_time`           | `TIME`         |                       | Time of the V-KYC call.                        |
| `call_date`           | `DATE`         | `NOT NULL`            | Date of the V-KYC call.                        |
| `nfs_upload_time`     | `TIMESTAMP`    |                       | Timestamp when the recording was uploaded to NFS. |
| `nfs_file_path`       | `TEXT`         | `NOT NULL`            | Full path to the recording file on the LTF NFS server. |

### Indexes:

Indexes have been created on frequently queried columns to optimize search performance:

*   `idx_vkyc_records_lan_id` on `lan_id`
*   `idx_vkyc_records_call_date` on `call_date`

## 3. Database Setup and Execution

Follow these steps to set up and prepare the PostgreSQL database for the V-KYC Audit Portal:

### 3.1. Install PostgreSQL

If you don't have PostgreSQL installed, download and install it from the official website:
[https://www.postgresql.org/download/](https://www.postgresql.org/download/)

### 3.2. Create a Database and User

It is recommended to create a dedicated database and user for the application with appropriate permissions. You can do this using `psql` (PostgreSQL interactive terminal):

```sql
-- Connect as a superuser (e.g., postgres user)
psql -U postgres

-- Create a new database
CREATE DATABASE vkyc_audit_db;

-- Create a new user for the application (replace 'your_username' and 'your_password')
CREATE USER your_username WITH PASSWORD 'your_password';

-- Grant all privileges on the database to the new user
GRANT ALL PRIVILEGES ON DATABASE vkyc_audit_db TO your_username;

-- Connect to the new database and grant privileges on schema (important for future tables)
\c vkyc_audit_db
GRANT ALL ON SCHEMA public TO your_username;

-- Exit psql
\q
```

### 3.3. Execute Schema Script

Navigate to this `database/` directory in your terminal and execute the `schema.sql` script to create the `vkyc_records` table and indexes:

```bash
psql -U your_username -d vkyc_audit_db -h localhost -f schema.sql
```

Replace `your_username` and `vkyc_audit_db` with your created user and database names, respectively. If your PostgreSQL is running on a different host or port, adjust `-h` and add `-p` accordingly.

### 3.4. Database Connection Details (for Backend Developer)

The Backend Developer will need the following connection details to configure the Spring Boot application (e.g., in `application.properties` or `application.yml`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/vkyc_audit_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=none # Important: Prevent Hibernate from managing schema, use our scripts
spring.jpa.show-sql=true # For debugging, can be false in production
```

**Note for Backend Developer:** Ensure that the Spring Boot application uses `spring.jpa.hibernate.ddl-auto=none` to prevent Hibernate from attempting to create or update the schema automatically. The schema will be managed solely through these SQL scripts.

## 4. Collaboration with Backend Developer

*   **Data Access Patterns:** The `vkyc_records` table is designed to support the search, filter, and individual download functionalities. The `nfs_file_path` column is crucial for the backend to locate and stream video files from the LTF NFS server.
*   **Transaction Requirements:** Ensure that database operations (e.g., inserting new records, if that functionality were to be added) are handled within proper transactions to maintain data integrity.
*   **Data Integrity Constraints:** The `UNIQUE` constraint on `lan_id` ensures no duplicate entries for the same V-KYC case. `NOT NULL` constraints ensure essential data points are always present.
*   **Future Enhancements:** If new data requirements arise (e.g., storing information about bulk download requests, audit logs), this schema can be extended or new tables can be added. Please communicate any such needs.

This database setup provides the foundation for the V-KYC Audit Portal's data storage. Ensure it is correctly configured and accessible by the backend services.
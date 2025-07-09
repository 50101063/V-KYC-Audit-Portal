-- Schema for V-KYC Audit Portal Database
-- Database: PostgreSQL 15.x

-- Table: vkyc_records
-- Stores metadata for V-KYC recordings, enabling efficient search and retrieval.

CREATE TABLE IF NOT EXISTS vkyc_records (
    id SERIAL PRIMARY KEY,
    lan_id VARCHAR(255) UNIQUE NOT NULL,
    call_duration_minutes INTEGER,
    status VARCHAR(50) NOT NULL,
    call_time TIME,
    call_date DATE NOT NULL,
    nfs_upload_time TIMESTAMP,
    nfs_file_path TEXT NOT NULL
);

-- Indexes for performance optimization
CREATE INDEX IF NOT EXISTS idx_vkyc_records_lan_id ON vkyc_records (lan_id);
CREATE INDEX IF NOT EXISTS idx_vkyc_records_call_date ON vkyc_records (call_date);

-- Optional: Seed data for testing purposes (can be removed in production)
-- INSERT INTO vkyc_records (lan_id, call_duration_minutes, status, call_time, call_date, nfs_upload_time, nfs_file_path) VALUES
-- ('C08383828282882', 5, 'APPROVED', '09:54:45 PM', '2025-02-23', '2025-02-24 00:00:00', '/nfs/vkyc/2025/02/C08383828282882.mp4'),
-- ('C02503254240258405', 3, 'APPROVED', '09:52:23 PM', '2025-03-25', '2025-03-26 00:00:00', '/nfs/vkyc/2025/03/C02503254240258405.mp4'),
-- ('C024032025114314', 2, 'APPROVED', '08:52:01 PM', '2025-03-25', '2025-03-26 00:00:00', '/nfs/vkyc/2025/03/C024032025114314.mp4'),
-- ('C02503264936883686', 2, 'APPROVED', '09:47:04 PM', '2025-03-27', '2025-03-28 00:00:00', '/nfs/vkyc/2025/03/C02503264936883686.mp4');
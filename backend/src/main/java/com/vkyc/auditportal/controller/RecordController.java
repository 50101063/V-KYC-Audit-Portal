package com.vkyc.auditportal.controller;

import com.vkyc.auditportal.model.RecordMetadata;
import com.vkyc.auditportal.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/records")
public class RecordController {

    @Autowired
    private RecordService recordService;

    // Simulated data for demonstration
    private final List<RecordMetadata> simulatedRecords = Arrays.asList(
        new RecordMetadata("C08383828282882", "0:05:54", "APPROVED", "9:54:45 PM", "23-02-2025", "2025-02-24", "/simulated_nfs/C08383828282882.mp4"),
        new RecordMetadata("C02503254240258405", "0:03:01", "APPROVED", "9:52:23 PM", "25-03-2025", "2025-03-26", "/simulated_nfs/C02503254240258405.mp4"),
        new RecordMetadata("C02502242042069761", "0:05:41", "APPROVED", "8:56:52 PM", "25-03-2025", "2025-03-26", "/simulated_nfs/C02502242042069761.mp4"),
        new RecordMetadata("C024032025114314", "0:02:20", "APPROVED", "8:52:01 PM", "25-03-2025", "2025-03-26", "/simulated_nfs/C024032025114314.mp4"),
        new RecordMetadata("C02503252120609515", "0:05:20", "APPROVED", "8:44:44 PM", "25-03-2025", "2025-03-26", "/simulated_nfs/C02503252120609515.mp4"),
        new RecordMetadata("C02503253141799390", "0:08:00", "APPROVED", "8:38:22 PM", "25-03-2025", "2025-03-26", "/simulated_nfs/C02503253141799390.mp4"),
        new RecordMetadata("C02503200607877884", "0:06:51", "APPROVED", "8:34:51 PM", "25-03-2025", "2025-03-26", "/simulated_nfs/C02503200607877884.mp4"),
        new RecordMetadata("C02503245213389653", "0:03:03", "APPROVED", "8:34:28 PM", "25-03-2025", "2025-03-26", "/simulated_nfs/C02503245213389653.mp4"),
        new RecordMetadata("C02503232234479326", "0:04:43", "APPROVED", "8:30:15 PM", "25-03-2025", "2025-03-26", "/simulated_nfs/C02503232234479326.mp4"),
        new RecordMetadata("C024032025113615", "0:05:07", "APPROVED", "9:00:15 PM", "26-03-2025", "2025-03-27", "/simulated_nfs/C024032025113615.mp4"),
        new RecordMetadata("C020032025130759", "0:05:34", "APPROVED", "8:57:53 PM", "26-03-2025", "2025-03-27", "/simulated_nfs/C020032025130759.mp4"),
        new RecordMetadata("C020032025162126", "0:03:57", "APPROVED", "8:56:33 PM", "26-03-2025", "2025-03-27", "/simulated_nfs/C020032025162126.mp4"),
        new RecordMetadata("C02503250173681850", "0:04:10", "APPROVED", "8:53:13 PM", "26-03-2025", "2025-03-27", "/simulated_nfs/C02503250173681850.mp4")
    );


    @GetMapping
    public ResponseEntity<Map<String, Object>> searchRecords(
            @RequestParam(required = false) String lanId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String month,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Simulate filtering based on parameters
        List<RecordMetadata> filteredRecords = simulatedRecords.stream()
            .filter(record -> lanId == null || record.getUserId().equalsIgnoreCase(lanId))
            .filter(record -> date == null || record.getDate().equalsIgnoreCase(date))
            .filter(record -> month == null || record.getMonth().equalsIgnoreCase(month))
            .collect(Collectors.toList());

        // Simulate pagination
        int start = page * size;
        int end = Math.min(start + size, filteredRecords.size());
        List<RecordMetadata> paginatedRecords = (start < end) ? filteredRecords.subList(start, end) : Collections.emptyList();

        Map<String, Object> response = Map.of(
            "content", paginatedRecords,
            "pageable", Map.of(
                "pageNumber", page,
                "pageSize", size,
                "paged", true,
                "unpaged", false,
                "offset", (long) start,
                "sort", Map.of("empty", true, "sorted", false, "unsorted", true)
            ),
            "last", end == filteredRecords.size(),
            "totalElements", (long) filteredRecords.size(),
            "totalPages", (int) Math.ceil((double) filteredRecords.size() / size),
            "size", size,
            "number", page,
            "sort", Map.of("empty", true, "sorted", false, "unsorted", true),
            "first", start == 0,
            "numberOfElements", paginatedRecords.size(),
            "empty", paginatedRecords.isEmpty()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{lanId}/download")
    public ResponseEntity<Resource> downloadRecord(@PathVariable String lanId) throws IOException {
        // In a real scenario, this would fetch from NFS based on lanId
        // For simulation, we return a dummy file.
        String filename = "simulated_video_" + lanId + ".mp4";
        byte[] fileContent = recordService.getSimulatedVideoFileContent(); // Get dummy content

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileContent.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(fileContent));
    }

    @PostMapping("/bulk-download/upload")
    public ResponseEntity<Map<String, Object>> uploadBulkLanIds(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Please select a file to upload."));
        }

        try {
            String content = new String(file.getBytes());
            List<String> lanIds = Arrays.stream(content.split("\\r?\\n"))
                                        .map(String::trim)
                                        .filter(s -> !s.isEmpty())
                                        .collect(Collectors.toList());

            if (lanIds.size() < 2 || lanIds.size() > 50) {
                return ResponseEntity.badRequest().body(Map.of("message", "File must contain between 2 and 50 LAN IDs."));
            }

            // Simulate asynchronous processing for bulk download
            // In a real scenario, this would trigger a background task
            // that fetches videos from NFS, zips them, uploads to S3, and provides a pre-signed URL.
            String simulatedDownloadLink = "http://localhost:8080/api/v1/bulk-download/download/simulated_bulk_" + System.currentTimeMillis() + ".zip";

            // You might want to return the metadata for the requested LAN IDs here
            List<RecordMetadata> requestedRecords = simulatedRecords.stream()
                .filter(record -> lanIds.contains(record.getUserId()))
                .collect(Collectors.toList());


            return ResponseEntity.ok(Map.of(
                "message", "Bulk download processing initiated for " + lanIds.size() + " LAN IDs. A download link will be provided shortly.",
                "requestedLanIds", lanIds,
                "simulatedDownloadLink", simulatedDownloadLink, // This would be the S3 pre-signed URL
                "requestedRecordsMetadata", requestedRecords
            ));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Failed to process file: " + e.getMessage()));
        }
    }

    @GetMapping("/bulk-download/download/{filename}")
    public ResponseEntity<Resource> downloadSimulatedBulkFile(@PathVariable String filename) throws IOException {
        // This is a simulated endpoint. In a real scenario, this would be an S3 pre-signed URL.
        // We generate a dummy zip file content for demonstration.
        byte[] zipContent = recordService.getSimulatedZipFileContent();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/zip");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(zipContent.length)
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(new ByteArrayResource(zipContent));
    }
}

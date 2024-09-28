package sh.ivan.pojo;

import java.time.Instant;

public record ApiResponse(Status status, Instant received) {}

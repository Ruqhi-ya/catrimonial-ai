package com.catrimonial.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private long totalUsers;
    private long verifiedUsers;
    private long totalCats;
    private long verifiedCats;
    private long totalMatchRequests;
    private long totalMessages;
    private long totalPosts;
    private long pendingReports;
    private long activeLostFound;
    private long activeAdoptions;
}

package com.disaster.analysis.domain.contract;

import com.disaster.analysis.domain.entity.AiSummary;
import java.util.Optional;

/**
 * Giao diện quản lý các bản tóm tắt được tạo ra bởi trí tuệ nhân tạo (AI Summaries).
 * Đảm bảo việc lưu trữ, cập nhật và truy xuất các báo cáo tổng hợp cho từng dự án thiên tai.
 */
public interface IAiSummaryRepository {

    /**
     * Lưu bản tóm tắt AI vào hệ thống.
     * Logic thực thi cần xử lý: Nếu dự án đã có bản tóm tắt trước đó, hệ thống sẽ
     * thực hiện ghi đè (Update) dữ liệu cũ để đảm bảo mỗi dự án chỉ có một bản tóm tắt mới nhất.
     *
     * @param summary Đối tượng AiSummary chứa nội dung tóm tắt và thông tin liên quan.
     */
    void save(AiSummary summary);

    /**
     * Truy xuất bản tóm tắt AI dựa trên mã định danh của dự án.
     *
     * @param projectId Mã định danh của dự án cần lấy tóm tắt.
     * @return Một Optional chứa bản tóm tắt nếu đã được tạo, hoặc rỗng nếu dự án chưa được phân tích.
     */
    Optional<AiSummary> findByProjectId(Long projectId);

    /**
     * Xóa bỏ bản tóm tắt AI liên quan đến một dự án cụ thể.
     * Thường được gọi khi dự án bị xóa hoặc khi cần làm mới (Reset) lại toàn bộ dữ liệu phân tích.
     *
     * @param projectId Mã định danh của dự án có bản tóm tắt cần xóa.
     */
    void deleteByProjectId(Long projectId);
}
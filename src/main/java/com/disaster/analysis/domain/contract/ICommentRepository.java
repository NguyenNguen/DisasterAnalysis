package com.disaster.analysis.domain.contract;

import com.disaster.analysis.domain.entity.Comment;
import java.util.List;

/**
 * Giao diện quản lý dữ liệu bình luận (Comment) của người dùng.
 * Cung cấp các phương thức truy xuất và thao tác dữ liệu ở quy mô lớn (Big Data)
 * phục vụ phân tích chi tiết phản hồi cộng đồng.
 */
public interface ICommentRepository {

    /**
     * Lưu hàng loạt danh sách bình luận vào cơ sở dữ liệu (Batch Insert).
     * Được tối ưu hóa để xử lý trường hợp một bài đăng có số lượng phản hồi cực lớn,
     * giúp giảm thiểu số lượng kết nối tới Database.
     *
     * @param comments Danh sách các đối tượng Comment cần lưu trữ.
     */
    void saveAll(List<Comment> comments);

    /**
     * Lấy toàn bộ danh sách bình luận thuộc về một bài đăng cụ thể.
     *
     * @param postId Mã định danh của bài đăng (Post).
     * @return Danh sách các bình luận liên quan đến bài đăng đó.
     */
    List<Comment> findByPostId(Long postId);

    /**
     * Truy xuất nhanh toàn bộ bình luận thuộc về tất cả các bài đăng trong một dự án.
     * Phương thức này bỏ qua bước truy vấn trung gian qua bảng Post để tối ưu hiệu suất
     * khi cần thống kê tổng quan cho toàn bộ dự án thiên tai.
     *
     * @param projectId Mã định danh của dự án (Project).
     * @return Danh sách toàn bộ bình luận có trong dự án.
     */
    List<Comment> findByProjectId(Long projectId);

    /**
     * Xóa sạch toàn bộ bình luận của một bài đăng.
     * Thường được sử dụng khi cần làm sạch dữ liệu hoặc xóa bài đăng gốc.
     *
     * @param postId Mã định danh của bài đăng cần xóa bình luận.
     */
    void deleteByPostId(Long postId);
}
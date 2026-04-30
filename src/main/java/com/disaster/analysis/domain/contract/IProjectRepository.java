package com.disaster.analysis.domain.contract;

import com.disaster.analysis.domain.entity.Project;
import java.util.List;
import java.util.Optional;

/**
 * Giao diện định nghĩa các quy tắc quản lý dữ liệu cho đối tượng Dự án (Project).
 * Đóng vai trò là lớp trừu tượng (Contract) để tầng Infrastructure thực thi việc lưu trữ.
 */
public interface IProjectRepository {

    /**
     * Lưu mới một dự án vào hệ thống.
     *
     * @param project Đối tượng dự án cần lưu.
     * @return ID của dự án sau khi được lưu thành công (thường là ID tự sinh).
     */
    Long save(Project project);

    /**
     * Cập nhật thông tin của một dự án đã tồn tại.
     *
     * @param project Đối tượng dự án chứa dữ liệu mới.
     */
    void update(Project project);

    /**
     * Xóa dự án khỏi hệ thống dựa trên mã định danh.
     *
     * @param id Mã định danh (ID) của dự án cần xóa.
     */
    void delete(Long id);

    /**
     * Tìm kiếm một dự án cụ thể theo mã định danh.
     *
     * @param id Mã định danh của dự án cần tìm.
     * @return Một Optional chứa dự án nếu tìm thấy, hoặc rỗng nếu không tồn tại.
     */
    Optional<Project> findById(Long id);

    /**
     * Lấy danh sách toàn bộ các dự án có trong hệ thống.
     *
     * @return Danh sách (List) các đối tượng Project.
     */
    List<Project> findAll();
}
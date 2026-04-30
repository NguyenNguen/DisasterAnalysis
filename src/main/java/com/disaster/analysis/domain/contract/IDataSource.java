package com.disaster.analysis.domain.contract;

import com.disaster.analysis.domain.entity.Post;
import java.util.List;

/**
 * Giao diện định nghĩa các phương thức thu thập dữ liệu từ các nguồn bên ngoài.
 * Cho phép hệ thống kết nối với các API mạng xã hội, báo chí hoặc các kho dữ liệu thô.
 */
public interface IDataSource {

    /**
     * Thực hiện lấy dữ liệu bài đăng dựa trên từ khóa và giới hạn số lượng.
     *
     * @param keyword Từ khóa tìm kiếm hoặc thẻ băm (hashtag) liên quan đến thiên tai.
     * @param limit   Số lượng bài đăng tối đa cần thu thập trong một lần gọi.
     * @return Danh sách các đối tượng {@link Post} chứa nội dung thô và thông tin đi kèm.
     */
    List<Post> fetchData(String keyword, int limit);
}
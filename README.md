# SNAP & LEARN
Snap & Learn - Ứng dụng Học Từ Vựng Thông Minh
Snap & Learn là một ứng dụng di động được phát triển trên nền tảng Android, thiết kế để tối ưu hóa quá trình thu thập và ghi nhớ từ vựng thông qua công nghệ trí tuệ nhân tạo (AI) và điện toán đám mây.
Ứng dụng giúp người dùng biến các tài liệu thực tế thành các thẻ ghi nhớ (Flashcard) và bài tập trắc nghiệm sinh động chỉ trong vài giây.
________________________________________
🚀 Tính Năng Cốt Lõi
1. Xác Thực & Định Danh Người Dùng
•	Đăng ký/Đăng nhập: Linh hoạt qua Email hoặc tài khoản Google.
•	Bảo mật: Tính năng xác thực Email và khôi phục mật khẩu.
•	Cá nhân hóa: Quản lý hồ sơ, cập nhật thông tin người dùng.
2. Quản Lý Kho Từ Vựng
•	Cấu trúc dữ liệu: Tổ chức theo dạng Chủ đề (Topic) chứa nhiều Thẻ (Flashcard).
•	Thao tác nhanh: Hỗ trợ CRUD (Thêm, Sửa, Xóa) và tính năng "Vuốt để xóa" với khả năng Hoàn tác (Undo).
3. Phân Hệ Scanner (AI Powered)
•	Nhận diện (OCR): Trích xuất chữ tiếng Anh từ hình ảnh bằng Google ML Kit.
•	Dịch thuật & Phiên âm: Tự động dịch nghĩa Việt và lấy phiên âm quốc tế qua API.
4. Ôn Tập & Đánh Giá
•	Flashcard: Giao diện lật thẻ (Flip card) trực quan.
•	Quiz Engine: Tự động tạo bài tập trắc nghiệm từ kho từ vựng của chính người dùng.
•	Theo dõi tiến độ: Thanh ProgressBar hiển thị mức độ hoàn thành bài học.
________________________________________
🛠 Công Nghệ Sử Dụng
Thành phần	Công nghệ / Thư viện
Lập trình	Java (Android SDK)
Kiến trúc	MVVM + Repository Pattern
Database	Firebase Cloud Firestore
Auth	Firebase Authentication
AI/ML	Google ML Kit (OCR & Translation)
Camera	CameraX API
UI/UX	ViewPager2, Material Design, CanHub Image Cropper
________________________________________
📂 Kiến Trúc Hệ Thống
Dự án tuân thủ nghiêm ngặt mô hình MVVM để đảm bảo tính dễ bảo trì:
•	View: Đảm nhận hiển thị và lắng nghe sự kiện người dùng (Activity/Fragment).
•	ViewModel: Xử lý logic nghiệp vụ và quản lý trạng thái dữ liệu qua LiveData.
•	Repository: Quản lý nguồn dữ liệu (Firebase, API, ML Kit).
•	Model: Định nghĩa cấu trúc dữ liệu thực tế.


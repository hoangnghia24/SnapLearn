# SNAP & LEARN
Snap & Learn - Ứng dụng Học Từ Vựng Thông Minh
Snap & Learn là một ứng dụng di động được phát triển trên nền tảng Android, thiết kế để tối ưu hóa quá trình thu thập và ghi nhớ từ vựng thông qua công nghệ trí tuệ nhân tạo (AI) và điện toán đám mây. Ứng dụng giúp người dùng biến các tài liệu thực tế thành các thẻ ghi nhớ (Flashcard) và bài tập trắc nghiệm sinh động chỉ trong vài giây.
🚀 Tính Năng Cốt Lõi
1. Xác Thực & Định Danh Người Dùng
•	Đăng ký và đăng nhập linh hoạt qua Email/Mật khẩu hoặc tài khoản Google.
•	Tính năng khôi phục mật khẩu qua Email.
•	Quản lý hồ sơ cá nhân (cập nhật họ tên, hiển thị định danh).
2. Quản Lý Kho Từ Vựng Cá Nhân Hóa
•	Tổ chức từ vựng theo cấu trúc: Chủ đề (Topic) -> Thẻ ghi nhớ (Flashcard).
•	Hỗ trợ đầy đủ các thao tác CRUD (Thêm, Sửa, Xóa) cho cả chủ đề và thẻ từ vựng.
•	Tính năng "Vuốt để xóa" (Swipe to delete) giúp quản lý thẻ nhanh chóng.
3. Phân Hệ Scanner (OCR & AI Translation)
Đây là điểm đột phá của ứng dụng, cho phép:
•	Quét Camera: Chụp văn bản từ sách, báo hoặc biển hiệu.
•	Nhận diện văn bản (OCR): Tự động trích xuất chữ tiếng Anh từ hình ảnh bằng Google ML Kit.
•	Dịch thuật thông minh: Tự động dịch nghĩa sang tiếng Việt và truy xuất phiên âm quốc tế qua API từ điển.
4. Ôn Tập & Đánh giá (Quiz Engine)
•	Học qua Flashcard: Giao diện lật thẻ (Flip card) trực quan để ghi nhớ từ vựng.
•	Trắc nghiệm tự động: Hệ thống tự động sinh câu hỏi từ kho từ vựng hiện có của người dùng (yêu cầu tối thiểu 4 thẻ mỗi chủ đề).
•	Phản hồi tức thì: Hiển thị kết quả Đúng/Sai bằng màu sắc và âm thanh sinh động, kèm thanh tiến độ hoàn thành bài học.
🛠 Công Nghệ Sử Dụng
•	Ngôn ngữ lập trình: Java (Android SDK).
•	Kiến trúc phần mềm: MVVM (Model - View - ViewModel) kết hợp Repository Pattern.
•	Backend & Cơ sở dữ liệu: Firebase Authentication (Xác thực) và Cloud Firestore (Cơ sở dữ liệu NoSQL thời gian thực).
•	Trí tuệ nhân tạo (AI): Google ML Kit (Text Recognition & Translation).
•	Thư viện hỗ trợ: CameraX (Xử lý hình ảnh), CanHub Image Cropper (Cắt ảnh), ViewPager2 (Hiển thị thẻ).
📂 Kiến Trúc Hệ Thống
Dự án được phân chia thành các lớp xử lý tách biệt để đảm bảo khả năng mở rộng:
•	View (Activities/Fragments): Đảm nhận hiển thị và lắng nghe sự kiện người dùng.
•	ViewModel: Điều hướng luồng dữ liệu và quản lý trạng thái UI qua LiveData.
•	Repository: Lớp trung gian truy xuất dữ liệu từ Firestore, API và ML Kit.
•	Model: Định nghĩa cấu trúc dữ liệu (User, Topic, Flashcard).
👥 Nhóm Thực Hiện
Trường Đại học Sư phạm Kỹ thuật TP.HCM - Khoa Công nghệ Thông tin 
•	GVHD: ThS. Trương Thị Ngọc Phượng.
•	Sinh viên:
1.	Đặng Quang Hoàng Nghĩa (MSSV: 23110130).
2.	Phan Hồng Phúc (MSSV: 23110141).


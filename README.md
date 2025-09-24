<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   CHAT NHÓM BẰNG RMI
</h2>
<div align="center">
    <p align="center">
      <img src="https://github.com/Tank97king/LapTrinhMang/blob/main/X%C3%82Y%20D%E1%BB%B0NG%20%E1%BB%A8NG%20D%E1%BB%A4NG%20CHAT%20CLIENT-SERVER%20S%E1%BB%AC%20D%E1%BB%A4NG%20GIAO%20TH%E1%BB%A8C%20TCP/%E1%BA%A2nh/aiotlab_logo.png?raw=true" alt="AIoTLab Logo" width="170"/>
      <img src="https://github.com/Tank97king/LapTrinhMang/blob/main/X%C3%82Y%20D%E1%BB%B0NG%20%E1%BB%A8NG%20D%E1%BB%A4NG%20CHAT%20CLIENT-SERVER%20S%E1%BB%AC%20D%E1%BB%A4NG%20GIAO%20TH%E1%BB%A8C%20TCP/%E1%BA%A2nh/fitdnu_logo.png?raw=true" alt="FITDNU Logo" width="180"/>
      <img src="https://github.com/Tank97king/LapTrinhMang/blob/main/X%C3%82Y%20D%E1%BB%B0NG%20%E1%BB%A8NG%20D%E1%BB%A4NG%20CHAT%20CLIENT-SERVER%20S%E1%BB%AC%20D%E1%BB%A4NG%20GIAO%20TH%E1%BB%A8C%20TCP/%E1%BA%A2nh/dnu_logo.png?raw=true" alt="DaiNam University Logo" width="200"/>
    </p>

[![AIoTLab](https://img.shields.io/badge/AIoTLab-green?style=for-the-badge)](https://www.facebook.com/DNUAIoTLab)
[![Faculty of Information Technology](https://img.shields.io/badge/Faculty%20of%20Information%20Technology-blue?style=for-the-badge)](https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin)
[![DaiNam University](https://img.shields.io/badge/DaiNam%20University-orange?style=for-the-badge)](https://dainam.edu.vn)

</div>

## 📖 1. Giới thiệu hệ thống


Chat Group RMI là một ứng dụng chat nhóm được xây dựng dựa trên công nghệ Java RMI (Remote Method Invocation). Hệ thống cho phép người dùng đăng ký, đăng nhập, gửi tin nhắn nhóm, nhắn tin riêng, gửi tệp tin, và nhận thông báo trạng thái gõ. Giao diện người dùng được thiết kế thân thiện với thư viện FlatLaf và hỗ trợ hiển thị tin nhắn với định dạng tùy chỉnh.

- Các tính năng chính:
+ Đăng ký/Đăng nhập/Đăng xuất: Quản lý tài khoản người dùng.
+ Chat nhóm: Gửi và nhận tin nhắn trong nhóm.
+ Nhắn tin riêng: Gửi tin nhắn riêng giữa các người dùng.
+ Gửi tệp tin: Hỗ trợ gửi và tải tệp tin giữa các người dùng.
+ Thông báo trạng thái gõ: Hiển thị thông báo khi người dùng đang gõ tin nhắn.
+ Danh sách người dùng online: Hiển thị danh sách người dùng đang hoạt động.
+ Lịch sử trò chuyện: Lưu trữ và tải lại lịch sử tin nhắn.  

## 🔧 2. Công nghệ sử dụng
-Ngôn ngữ lập trình: Java

- Công nghệ chính:
  
+Java RMI: Sử dụng cho giao tiếp client-server thông qua gọi phương thức từ xa.

+Swing: Xây dựng giao diện người dùng đồ họa.

+FlatLaf: Thư viện giao diện hiện đại cho ứng dụng Swing.

-Thư viện phụ trợ:

+FlatSVGIcon: Hỗ trợ hiển thị biểu tượng SVG trong giao diện.

-Lưu trữ:

+Lưu trữ thông tin người dùng và lịch sử trò chuyện trong tệp tin.

+Công cụ quản lý thời gian: SimpleDateFormat để định dạng thời gian tin nhắn.

-Môi trường chạy:

+Java SE Development Kit (JDK) 8 hoặc cao hơn.
## 🚀 3. Hình ảnh các chức năng

<p align="center">
<img src="https://github.com/ThanhSon2904/LapTrinhMang/blob/main/RMIChat/%E1%BA%A3nh/giao%20di%E1%BB%87n.png?raw=true" alt="Chức năng đăng nhập" width="600"/>
</p>

<p align="center">
  <em>Hình 1: Ảnh giao diện  </em>
</p>

<p align="center">
<img src="https://github.com/ThanhSon2904/LapTrinhMang/blob/main/RMIChat/%E1%BA%A3nh/2%20ng%C6%B0%E1%BB%9Di%20chat%20v%E1%BB%9Bi%20nhau.png?raw=true" alt="Chức năng đăng ký" width="600"/>
</p>
<p align="center">
  <em> Hình 2: ảnh 2 người chat với nhau </em>
</p>


<p align="center">
  <img src="https://github.com/ThanhSon2904/LapTrinhMang/blob/main/RMIChat/%E1%BA%A3nh/%C4%91%C4%83ng%20k%C3%BD.png?raw=true" alt="Hệ thống thông báo tham gia thành công" width="400"/>
</p>
<p align="center">
  <em> Hình 3: đăng ký  .</em>
</p>

<p align="center">
  <img src="https://github.com/ThanhSon2904/LapTrinhMang/blob/main/RMIChat/%E1%BA%A3nh/%C4%91%C4%83ng%20nh%E1%BA%ADp.png?raw=true" alt="Giao diện hai người chat với nhau" width="400"/>
</p>
<p align="center">
  <em> Hình 4: đăng nhập </em>
</p>


## 📝 4. Hướng dẫn cài đặt và sử dụng


## Yêu cầu hệ thống

-JDK: Phiên bản 8 hoặc cao hơn.

-Maven: Khuyến nghị để quản lý phụ thuộc (tùy chọn).

-Hệ điều hành: Windows, macOS, Linux.

-Thư viện: FlatLaf và FlatSVGIcon.

### Hướng dẫn cài đặt

1. **Tải mã nguồn**:
   - Clone hoặc tải mã nguồn từ kho lưu trữ (repository).

2. **Cài đặt thư viện phụ thuộc**:
   - Thêm thư viện FlatLaf và FlatSVGIcon vào dự án:
     ```xml
     <dependency>
         <groupId>com.formdev</groupId>
         <artifactId>flatlaf</artifactId>
         <version>3.2.5</version>
     </dependency>
     <dependency>
         <groupId>com.formdev</groupId>
         <artifactId>flatlaf-extras</artifactId>
         <version>3.2.5</version>
     </dependency>

-Nếu không sử dụng Maven, tải các tệp JAR từ FlatLaf releases và thêm vào dự án.


-Cấu hình thư mục resources/icons:

-Tạo thư mục resources/icons trong dự án.
+ Thêm các tệp SVG (login.svg, register.svg, logout.svg, send.svg) vào thư mục này. Bạn có thể tải các biểu tượng SVG miễn phí từ các nguồn như Flaticon.


-Cấu hình Constants:

-Đảm bảo lớp Constants.java (nếu có) được định nghĩa với các hằng số như SERVER_PORT và SERVER_NAME. Ví dụ:

javapublic class Constants {
    public static final int SERVER_PORT = 1099;
    public static final String SERVER_NAME = "ChatService";
    public static final String USERS_FILE = "users.dat";
}



- Biên dịch dự án:

Sử dụng IDE (như IntelliJ IDEA, Eclipse) hoặc lệnh Maven:
bashmvn clean install




-Hướng dẫn sử dụng

-Khởi động Server:

+ Chạy lớp Server.java để khởi động RMI registry và dịch vụ chat:
bashjava Server

+ Server sẽ khởi động trên cổng được định nghĩa trong Constants.SERVER_PORT (mặc định: 1099).


-Chạy Client:

+ Chạy lớp ClientGUI.java để khởi động giao diện người dùng:
bashjava ClientGUI


+ Giao diện sẽ hiển thị cửa sổ chat với các nút Đăng nhập, Đăng ký, Đăng xuất.


-Đăng ký tài khoản:

+ Nhấn nút Đăng ký, nhập tên người dùng và mật khẩu để tạo tài khoản mới.


-Đăng nhập:

+ Nhấn nút Đăng nhập, nhập tên người dùng và mật khẩu để tham gia trò chuyện.


-Sử dụng các tính năng:

+ Gửi tin nhắn nhóm: Nhập tin nhắn vào ô văn bản và nhấn nút Gửi hoặc phím Enter.

+ Nhắn tin riêng: Nhấp chuột phải vào tên người dùng trong danh sách online, chọn Nhắn tin riêng, nhập tin nhắn.

+ Gửi tệp tin: Nhấp chuột phải vào tên người dùng, chọn Gửi tệp tin, chọn tệp để gửi.

+ Tải tệp tin: Khi nhận thông báo tệp tin, chọn Có để tải và chọn vị trí lưu.


-Đăng xuất:

+ Nhấn nút Đăng xuất để thoát khỏi phiên trò chuyện.



-Lưu ý

+ Đảm bảo server đang chạy trước khi khởi động client.
+ Tệp users.dat và lịch sử trò chuyện được lưu trong thư mục dự án.
+ Nếu gặp lỗi RMI, kiểm tra cổng SERVER_PORT không bị chặn bởi tường lửa.


## 5. Thông tin liên hệ  
Họ tên: Nguyễn Thanh Sơn
Lớp: CNTT 16-01.  
Email: sonn29042004@gmail.com

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

---

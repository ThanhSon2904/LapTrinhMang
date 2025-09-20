<h2 align="center">
    <a href="https://dainam.edu.vn/vi/khoa-cong-nghe-thong-tin">
    🎓 Faculty of Information Technology (DaiNam University)
    </a>
</h2>
<h2 align="center">
   XÂY DỰNG ỨNG DỤNG CHAT CLIENT-SERVER SỬ DỤNG GIAO THỨC TCP
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


# 📖 1. Giới thiệu hệ thống

<p align="left">
- Hệ thống được xây dựng theo mô hình Client – Server nhằm mục đích trao đổi dữ liệu và gửi/nhận tin nhắn giữa hai phía thông qua lập trình socket trong Java.<br>
- Server: đóng vai trò trung tâm, lắng nghe các kết nối từ client. Sau khi có kết nối, server nhận dữ liệu từ client và có thể phản hồi ngược lại.<br>
- Client: đóng vai trò gửi yêu cầu đến server, truyền dữ liệu/tin nhắn và chờ phản hồi.<br>
- Hệ thống này mô phỏng nguyên lý cơ bản của các ứng dụng chat, truyền tin hoặc trao đổi dữ liệu trong thực tế, giúp người học nắm vững cách xây dựng ứng dụng phân tán bằng Java.
</p>

# 🔧 2. Công nghệ sử dụng

<p align="left">
Ngôn ngữ lập trình: Java (JDK 8+)<br>
Mô hình mạng: Client–Server<br><br>
Kỹ thuật:<br>
- Java Socket API (<code>java.net.Socket</code>, <code>java.net.ServerSocket</code>) để thiết lập kết nối TCP<br>
- Luồng I/O (<code>InputStream</code>, <code>OutputStream</code>, <code>BufferedReader</code>, <code>PrintWriter</code>) để đọc/ghi dữ liệu qua mạng<br><br>
IDE khuyến nghị: Eclipse hoặc IntelliJ IDEA (có thể chạy bằng terminal)<br>
Hệ điều hành: Windows/Linux/macOS
</p>

# 🚀 3. Hình ảnh các chức năng

## 📝 4. Hướng dẫn cài đặt và sử dụng

- Bước 1 : Chuẩn bị môi trường
Cài đặt Java JDK 8 trở lên.  
Kiểm tra bằng lệnh:
java -version
javac -version
- **Bước 2: Biên dịch mã nguồn**  
Mở terminal/cmd tại thư mục chứa file `.java`.  
Chạy lệnh:
javac server.java
javac Client.java
- **Bước 3: Chạy chương trình**  
Mở cửa sổ terminal 1 để chạy server:
java server
text→ Server khởi động và lắng nghe kết nối.  
Mở cửa sổ terminal 2 để chạy client:
java Client
text→ Client kết nối đến server.  
- **Bước 4: Trao đổi dữ liệu**  
Client nhập tin nhắn trong console.  
Server nhận và hiển thị nội dung.  
Có thể mở nhiều client để kết nối vào server.

## Thông tin liên hệ  
- **Họ tên**: Nguyễn Thanh Sơn.  
- **Lớp**: CNTT 16-01.  
- **Email**: sonn29042004@gmail.com.  

© 2025 AIoTLab, Faculty of Information Technology, DaiNam University. All rights reserved.

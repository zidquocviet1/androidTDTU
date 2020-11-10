### Final Project Android Development Application
**The name of the app is Toeic Testing. The app is created with the purpose is training your listening and reading skills.**

_Các ứng dụng cần thiết để chạy_

1. Android Studio (phiên bản sử dụng hiện tại 4.1)
2. Visual Studio 2019 (cần có ASP.NET core phiên bản 3.0 trở lên)
3. SQL Server Management Studio (dùng để quản lý hệ cơ sử dữ liệu MSSQL)

_Các bước chạy chương trình_

1. Chạy file toeic.sql trong thư mục server để khởi tạo database, tables và insert dữ liệu
2. Mở project ToeicAPI trong thư mục server.
3. Vào Tools -> Nuget Package Manager -> Package Manager Console và chạy câu lệnh sau.

`

> Scaffold-DbContext "Server=YOUR_SERVER;Database=Toeic;user=YOUR_USER;password=YOUR_PASSWORD" Microsoft.EntityFrameworkCore.SqlServer -OutputDir Models

`

4. Sau khi đã tạo được Models và DbContext lúc này khởi chạy project (không được chạy bằng IIS). Lúc này có thể sử dụng phần mềm Post Man để test thử một số api.
5. Server lúc này đã được connect, vào project android và mở package api và chỉnh BASE_URL (chỉnh lại IP).
6. App lúc này đã có thể chạy toàn bộ tính năng.

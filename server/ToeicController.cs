using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using ToeicAPI.Models;

namespace ToeicAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ToeicController : ControllerBase
    {
        [HttpGet("account")]
        public IActionResult getAccount()
        {
            return Ok(new { status = true, data = DAO.getInstance().getAccount(),
                message = "Lay danh sach account thanh cong"});
        }

        [HttpGet("comment")]
        public List<Comment> getComment()
        {
            return DAO.getInstance().getComment();
        }

        [HttpPost("login")]
        public IActionResult login(string username, string password)
        {
            var oldAcc = DAO.getInstance().checkLogin(username, password);
            if (oldAcc == null)
            {
                return Ok(new { status = false, data = "", message = "Tài khoản hoặc mật khẩu không hợp lệ!" });
            }
            return Ok(new { status = true, data = new { id = oldAcc.Id, type = oldAcc.Type, fullName = oldAcc.FullName }, message = "Đăng nhập thành công" });
        }

        [HttpPost("register")]
        public IActionResult register([FromBody] Account acc)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return Ok(new { status = false, data = acc, message = "Đăng ký thất bại" });
                }
                var oldAcc = DAO.getInstance().register(acc);

                return Ok(new { status = true, data = new { id = oldAcc.Id, type = oldAcc.Type }, message = "Đăng ký thành công" });
            }
            catch (Exception e)
            {
                return Ok(new { status = false, data = "", message = e.Message.ToString()});
            }
        }

        [HttpGet("courses")]
        public IActionResult getAllCourse()
        {
            return Ok(new { status = true, data = DAO.getInstance().getAllCourse(), message = "Danh sach tat ca cac khoa thi" });
        }

        [HttpPost("question")]
        public IActionResult getAllQuestionFromCourse(int courseID)
        {
            return Ok(new { status = true, data = DAO.getInstance().getAllQuestion(courseID), message = "Danh sach cau hoi voi course = " + courseID});
        }

        [HttpPost("postComment")]
        public IActionResult createComment([FromBody]Comment comment)
        {
            try
            {
                var result = DAO.getInstance().createComment(comment);
                return Ok(new { status = true, data = result, message = "Them binh luan thanh cong" });
            }
            catch (Exception e)
            {
                return Ok(new { status = false, data = e.Message.ToString(), message = "Them binh luan that bai" });
            }
        }

        [HttpGet("categories")]
        public IActionResult getAllCategory()
        {
            return Ok(new { status = true, data = DAO.getInstance().getAllCategory(), message = "Danh sách thể loại"});
        }

        [HttpGet("user")]
        public IActionResult getUser(string id)
        {
            try
            {
                var result = DAO.getInstance().getUserInfo(id);
                return Ok(new { status = true, data = result, message = "Thông tin người dùng với id = " + id});
            }
            catch (Exception e)
            {
                return Ok(new { status = false, data = e.Message.ToString(), message = "Lấy thông tin người dùng thất bại" });
            }
        }

        [HttpGet("rank")]
        public IActionResult getRank()
        {
            return Ok(new { status = true, data = DAO.getInstance().getUserByScore(), message = "Danh sách người dung có điểm cao nhất"});
        }

        [HttpPut("user")]
        public IActionResult updateUser(User user)
        {
            try
            {
                return Ok(new { status = true, data = DAO.getInstance().updateUser(user), message = "Chỉnh sửa thông tin người dùng thành công"});
            }
            catch (Exception e)
            {
                return Ok(new { status = false, data = e.Message.ToString(), message = "Chỉnh sửa thông tin người dùng thất bại" });
            }
        }

        [HttpGet("comments")]
        public IActionResult getCommentByCourseID(int id)
        {
           return Ok(new { status = true, data = DAO.getInstance().getComments(id), message = "Danh sach comment" });
        }

        [HttpPost("addComment")]
        public IActionResult addComment(Comment comment)
        {
            try
            {
                return Ok(new { status = true, data = DAO.getInstance().createComment(comment), message = "Tao comment thành công" });
            }
            catch (Exception e)
            {
                return Ok(new { status = false, data = e.Message.ToString(), message = "Tao comment thất bại" });
            }
        }

        [HttpPut("updateRecord")]
        public IActionResult updateRecord(Record record)
        {
            try
            {
                return Ok(new { status = true, data = DAO.getInstance().updateRecord(record), message = "Chinh sua record thành công" });
            }
            catch (Exception e)
            {
                return Ok(new { status = false, data = e.Message.ToString(), message = "Chinh sua record thất bại" });
            }
        }

        [HttpGet("getRankByCourse")]
        public IActionResult getRankByCourse(int courseId)
        {
            try
            {
                return Ok(new { status = true, data = DAO.getInstance().getRankByCourse(courseId), message = "thành công" });
            }
            catch (Exception e)
            {
                return Ok(new { status = false, data = e.Message.ToString(), message = "thất bại" });
            }
        }
    }
} 
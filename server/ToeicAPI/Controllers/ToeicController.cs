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
                return Ok(new { status = false, data = "", message = "Dang nhap that bai" });
            }
            return Ok(new { status = true, data = new { id = oldAcc.Id, type = oldAcc.Type }, message = "Dang nhap thanh cong" });
        }
        [HttpPost("register")]
        public IActionResult register([FromBody] Account acc)
        {
            var oldAcc = DAO.getInstance().register(acc);
            if (oldAcc == null)
            {
                return NotFound();
            }
            return Ok(new { status = true, data = new { id = oldAcc.Id, type = oldAcc.Type}, message = "Dang ky thanh cong" });
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
    }
} 
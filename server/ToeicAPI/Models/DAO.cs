using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ToeicAPI.Models
{
    public class DAO
    {
        ToeicContext db;

        private static DAO instance;

        public static DAO getInstance()
        {
            if (instance == null)
                instance = new DAO();
            return instance;
        }

        private DAO() { db = new ToeicContext(); }

        public List<Account> getAccount()
        {
            return db.Account.Select(account => account).ToList();
        }
        public List<Comment> getComment()
        {
            return db.Comment.Select(cm => cm).ToList();
        }

        public Account checkLogin(string username, string password)
        {
            var acc = db.Account.FirstOrDefault(u => u.Username.Equals(username) && u.Password.Equals(password));
            return acc;
        }

        public Account register(Account acc)
        {
            var oldAcc = db.Account.FirstOrDefault(u => u.Username.Equals(acc.Username) && u.Password.Equals(acc.Password));
            if (oldAcc != null)
            {
                return null;
            }
            db.Account.Add(acc);
            db.SaveChanges();

            return acc;
        }

        public List<Course> getAllCourse()
        {
            return db.Course.Select(c => c).ToList();
        }

        public List<Question> getAllQuestion(int courseID) 
        {
            return db.Question.Where(q => q.CourseId == courseID).ToList();
        }

        public Comment createComment(Comment comment)
        {
            var course = db.Course.FirstOrDefault(c => c.Id == comment.CourseId);
            var user = db.Account.FirstOrDefault(a => a.Id == comment.UserId);

            if (course == null)
            {
                throw new Exception("Khoa hoc khong ton tai trong he thong");
            }
            if (user == null)
            {
                throw new Exception("Nguoi dung khong ton tai trong he thong");
            }

            db.Comment.Add(comment);
            db.SaveChanges();

            return comment;
        }
    }
}

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
            var oldAcc = db.Account.FirstOrDefault(u => u.Username.Equals(acc.Username));
            if (oldAcc != null)
            {
                throw new Exception("Đăng ký thất bại. Tên tài khoản đã tồn tại!");
            }
            User user = new User
            {
                Id = 0,
                Name = acc.FullName,
                Gender = false,
                Birthday = DateTime.Today,
                Address = "",
                Email = "",
                Score = 0,
                AccountId = acc.Id,
                Avatar = new Random().Next(0, 5),
                Account = acc
            };

            acc.User.Add(user);
            db.Account.Add(acc);
            db.User.Add(user);
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

        public List<Category> getAllCategory()
        {
            return db.Category.Select(c => c).ToList();
        }

        public User getUserInfo(string accountId)
        {
            var user = db.User.FirstOrDefault(u => u.AccountId.Equals(accountId));

            if (user == null)
            {
                throw new Exception("Người dùng không tồn tại!");
            }

            return user;
        }

        public List<User> getUserByScore()
        {
            var user = db.User.Select(u => u).OrderByDescending(u => u.Score);

            return user.ToList();
        }

        public User updateUser(User user)
        {
            var acc = db.Account.FirstOrDefault(u => u.Id.Equals(user.AccountId));
            var oldUser = db.User.FirstOrDefault(u => u.AccountId.Equals(acc.Id));

            if (acc == null)
            {
                throw new Exception("Khong ton tai tai khoan");
            }
            if (oldUser == null)
            {
                throw new Exception("Không tồn tại người dùng trong hệ thống.");
            }

            acc.FullName = user.Name;
            oldUser.Name = user.Name;
            oldUser.Gender = user.Gender;
            oldUser.Birthday = user.Birthday;
            oldUser.Address = user.Address;
            oldUser.Email = user.Email;
            oldUser.Score = user.Score;
            oldUser.Avatar = user.Avatar;

            db.SaveChanges();
            return oldUser;
        }

        public List<Comment> getComments(int courseID)
        {
            List<Comment> comList = new List<Comment>();
            
            var comments = from c in db.Comment
                           where c.CourseId == courseID
                           select new { c.Id, c.Description, c.Rating, c.UserId, c.User };

            foreach (var item in comments)
            {
                comList.Add(new Comment { Id = item.Id, Description = item.Description, Rating = item.Rating,
                CourseId = courseID, Course = null, UserId = item.UserId, User = item.User});
            }
            return comList;
        }

        public Record updateRecord(Record record)
        {
            var acc = db.Account.FirstOrDefault(u => u.Id.Equals(record.AccountId));
            var course = db.Course.FirstOrDefault(u => u.Id.Equals(record.CourseId));
            var old = db.Record.FirstOrDefault(r => r.Id == record.Id);

            if (acc == null)
            {
                throw new Exception("Khong ton tai tai khoan");
            }
            if (course == null)
            {
                throw new Exception("Không tồn tại khoa thi trong he thong");
            }
            if (old == null)
            {
                db.Record.Add(record);
                db.SaveChanges();
                return record;
            }

            old.Score = record.Score;
            old.CourseId = record.CourseId;
            old.AccountId = record.AccountId;

            db.SaveChanges();
            return old;
        }

        public List<Record> getRankByCourse(int courseId)
        {
            List<Record> records = new List<Record>();

            var course = db.Course.FirstOrDefault(c => c.Id == courseId);

            if (course == null)
            {
                throw new Exception("Khong ton tai khoa thi trong he thong");
            }

            var items = from r in db.Record
                        where r.CourseId == courseId
                        select new { r.Id, r.AccountId, r.Score, r.Account };

            foreach (var item in items)
            {
                records.Add(new Record { Id = item.Id, Account = item.Account, AccountId = item.AccountId,
                Score = item.Score, Course = null, CourseId = courseId});
            }

            return records;
        }
    }
}

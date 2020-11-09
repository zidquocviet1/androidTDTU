using System;
using System.Collections.Generic;

namespace ToeicAPI.Models
{
    public partial class Course
    {
        public Course()
        {
            Comment = new HashSet<Comment>();
            Question = new HashSet<Question>();
        }

        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public int? Rating { get; set; }

        public virtual ICollection<Comment> Comment { get; set; }
        public virtual ICollection<Question> Question { get; set; }
    }
}

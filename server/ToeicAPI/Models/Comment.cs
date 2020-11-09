using System;
using System.Collections.Generic;

namespace ToeicAPI.Models
{
    public partial class Comment
    {
        public int Id { get; set; }
        public string Description { get; set; }
        public int? Rating { get; set; }
        public int? CourseId { get; set; }
        public string UserId { get; set; }

        public virtual Course Course { get; set; }
        public virtual Account User { get; set; }
    }
}

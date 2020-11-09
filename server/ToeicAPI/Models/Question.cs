using System;
using System.Collections.Generic;

namespace ToeicAPI.Models
{
    public partial class Question
    {
        public int Id { get; set; }
        public string Description { get; set; }
        public string QuestionA { get; set; }
        public string QuestionB { get; set; }
        public string QuestionC { get; set; }
        public string QuestionD { get; set; }
        public string Answer { get; set; }
        public int? CourseId { get; set; }
        public int? CategoryId { get; set; }

        public virtual Category Category { get; set; }
        public virtual Course Course { get; set; }
    }
}

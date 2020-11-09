using System;
using System.Collections.Generic;

namespace ToeicAPI.Models
{
    public partial class Category
    {
        public Category()
        {
            Question = new HashSet<Question>();
        }

        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }

        public virtual ICollection<Question> Question { get; set; }
    }
}

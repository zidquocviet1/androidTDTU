using System;
using System.Collections.Generic;

namespace ToeicAPI.Models
{
    public partial class Account
    {
        public Account()
        {
            Comment = new HashSet<Comment>();
        }

        public string Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string Type { get; set; }

        public virtual ICollection<Comment> Comment { get; set; }
    }
}

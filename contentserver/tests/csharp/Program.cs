using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;
using System.IO;
using Everhomes;

namespace request
{
    class Program
    {
        static void Main(string[] args)
        {
            //String apiKey = "b86ddb3b-ac77-4a65-ae03-7e8482a3db70";
            String secretKey = "2nDpmzJj63Un0GzXyeZKUKlVSOKzNHv4FidFL9uCpNaLq6rqE0VAOv3uPaR0jWIRMNqedgci3vzLPAkaX1jg6Q==";
            //String secretKey = "testme";
            //String data = "1122334455667788";
            //byte[] bData = Encoding.ASCII.GetBytes(data);
            //Console.WriteLine(bData.Aggregate("", (s, e) => s + String.Format("{00:X2}", e), s => s));
            //HMACSHA1 hmacSha = new HMACSHA1(Encoding.ASCII.GetBytes(secretKey));
            //hmacSha.Initialize();
            //String rs = hmacSha.ComputeHash(Encoding.ASCII.GetBytes(data)).Aggregate("", (s, e) => s + String.Format("{00:X2}", e), s => s);
            //Console.WriteLine(rs);

            //a hello this is a test b please show me something c yes I got it
            Dictionary<String, String> ps = new Dictionary<string, string>();
            ps["a hello"] = " this is a test ";
            ps["b please show me"] = " something ";
            ps["c yes"] = " I got it";
            Console.WriteLine(SignatureHelper.ComputeSignature(ps, secretKey));
            Console.Read();
        }
    }
}

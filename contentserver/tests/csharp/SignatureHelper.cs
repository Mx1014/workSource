using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Security.Cryptography;

namespace Everhomes
{
    public class SignatureHelper
    {
        /// <summary>
        /// 计算一个字典的签名。
        /// 
        /// </summary>
        /// <param name="paramsMap">字典</param>
        /// <param name="secretKey">密钥，base64编码过的64字节的Key</param>
        /// <returns>base64字符串</returns>
        public static String ComputeSignature(Dictionary<String, String> paramsMap, String secretKey)
        {
            byte[] key = System.Convert.FromBase64String(secretKey);
            HMACSHA1 myhmacsha1 = new HMACSHA1(key);
            myhmacsha1.Initialize();
            List<String> keyList = new List<string>();
            keyList.AddRange(paramsMap.Keys);
            keyList.Sort();

            StringBuilder sb = new StringBuilder();
            byte[] b = null;
            foreach (var keyItem in keyList)
            {
                //b = Encoding.UTF8.GetBytes(keyItem);
                sb.Append(keyItem);
                if (!String.IsNullOrEmpty(paramsMap[keyItem]))
                {
                    sb.Append(paramsMap[keyItem]);
                }
            }
            //String r = myhmacsha1.ComputeHash(ms).Aggregate("", (s, e) => s + String.Format("{0:x2}", e), s => s);
            Console.WriteLine(sb.ToString());
            //char[] cs = r.ToCharArray();
            b = myhmacsha1.ComputeHash(Encoding.UTF8.GetBytes(sb.ToString()));
            return System.Convert.ToBase64String(b);
        }
    }
}

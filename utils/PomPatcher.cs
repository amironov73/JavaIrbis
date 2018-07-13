using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml.Linq;

namespace PomPatcher
{
    class Program
    {
        static void Main(string[] args)
        {
            if (args.Length != 2)
            {
                Console.WriteLine("USAGE: PomPatcher <pom.xml> <version>");
                return;
            }

            string fileName = args[0];
            string newVersion = args[1];
            XDocument document = XDocument.Load(fileName);
            if (document.Root != null)
            {
                XName name = document.Root.Name.Namespace.GetName("version");
                XElement element = document.Root.Element(name);
                if (element == null)
                {
                    element = new XElement(name);
                    document.Root.Add(element);
                }
                element.Value = newVersion;
                document.Save(fileName);
            }
        }
    }
}

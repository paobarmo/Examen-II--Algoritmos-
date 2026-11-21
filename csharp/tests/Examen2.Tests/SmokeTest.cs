using System.IO;
using NUnit.Framework;

namespace Examen2.Tests
{
    public class SmokeTest
    {
        [Test]
        public void EntornoDePruebasFunciona()
        {
            Assert.That(2 + 2, Is.EqualTo(4));
        }

        [Test]
        public void ExisteArchivoDeDatos()
        {
            string? carpetaDatos = BuscarCarpetaDatos();
            Assert.That(carpetaDatos, Is.Not.Null, "No se encontró la carpeta datos/");

            string archivo = Path.Combine(carpetaDatos!, "version-a", "tramos.csv");
            Assert.That(File.Exists(archivo), Is.True, "No se encontró " + archivo);
        }

        // Sube desde la carpeta de las pruebas compiladas hasta encontrar datos/
        private static string? BuscarCarpetaDatos()
        {
            DirectoryInfo? actual = new DirectoryInfo(TestContext.CurrentContext.TestDirectory);
            while (actual != null)
            {
                string candidata = Path.Combine(actual.FullName, "datos");
                if (Directory.Exists(candidata))
                {
                    return candidata;
                }
                actual = actual.Parent;
            }
            return null;
        }
    }
}

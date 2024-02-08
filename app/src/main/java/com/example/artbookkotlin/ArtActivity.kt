package com.example.artbookkotlin

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.PixelCopy.Request
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.artbookkotlin.databinding.ActivityArtBinding
import com.google.android.material.snackbar.Snackbar
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.Exception

class ArtActivity : AppCompatActivity() {
     private lateinit var binding: ActivityArtBinding
     private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
     private lateinit var permissionLauncher :ActivityResultLauncher<String>
     //var selectedBitmap: Bitmap? = null ifadesindeki null, selectedBitmap
     // değişkenine başlangıç değeri olarak atanmış olan bir değerdir.
     //Bitmap? veri tipi Kotlin dilinde "nullable" bir veri tipidir,
     // yani bu değişkenin null olabileceğini belirtir. Bu durumda selectedBitmap
     // değişkeni başlangıçta herhangi bir değer taşımaz, yani null'dır.
     //Bu şekilde tanımlanan bir nullable değişken, daha sonra null
     // değeri alabilir veya bir değer ataılabilir. Örneğin, bir görüntü
     // seçildiğinde veya oluşturulduğunda selectedBitmap değişkenine bir değer atanabilir.
     var selectedBitmap : Bitmap?= null
    private lateinit var database : SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityArtBinding.inflate(layoutInflater)
         val view = binding.root
         setContentView(view)
        database = this.openOrCreateDatabase("arts", Context.MODE_PRIVATE,null)
          registerLauncher()

     }
    fun saveButtonClicked(view:View){

        val artName=binding.artName.text.toString()
        val artistName=binding.artistName.text.toString()
        val year=binding.yearText.text.toString()

          if(selectedBitmap!=null){
              /*İlk olarak, makeSmallerBitmap işlevi çağrılıyor ve selectedBitmap
              üzerinde 300 piksel boyutunda bir küçültme yapılıyor. Yani,
              orijinal bitmap'in boyutu 300 pikselden daha büyükse, bu işlevle
              boyutu 300 pikselden daha küçük bir bitmap elde edilir. !! ifadesi,
              selectedBitmap değişkeninin null olmadığını garanti altına alır.*/
              val smallBitmap=makeSmallerBitmap(selectedBitmap!!,300)
              // görselleri byte dizisine çevirmemiz gerekiyor
              /*Sonraki kod satırında, bir ByteArrayOutputStream nesnesi oluşturuluyor.
               Bu nesne, bir byte dizisine yazılmak üzere verileri tutar.
              smallBitmap üzerindeki işlem sonucu, Bitmap.compress yöntemi
              kullanılarak outputStream üzerine sıkıştırılıyor ve burada
               Bitmap.CompressFormat.PNG formatında %50 sıkıştırma oranıyla
               sıkıştırma yapılıyor.*/
              val outputStream=ByteArrayOutputStream()
              smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
              //resmin veriye çevrilmiş hali
              /*   Son olarak, outputStream.toByteArray() yöntemi ile outputStream içeriği
              bir byte dizisine dönüştürülerek byteArray değişkenine atanır. Yani,
               bu adımdan sonra, byteArray içinde orijinal bitmap'in byte dizisine
                çevrilmiş hali bulunu*/
              val byteArray=outputStream.toByteArray()

              // VERİTABANI
              try {
                  /*  İlk olarak, CREATE TABLE IF NOT EXISTS ifadesiyle arts adında
                  bir tablo oluşturulur. Tablo alanları id (INTEGER PRIMARY KEY),
                   artname (VARCHAR), artistname (VARCHAR), year (VARCHAR) ve image
                    (BLOB) olarak tanımlanır.*/

                  database.execSQL("CREATE TABLE IF NOT EXISTS arts(id INTEGER PRIMARY KEY,artname VARCHAR ,artistname VARCHAR,year VARCHAR ," +
                          "image BLOB )")
                  // değişkenlerin değeri daha sonradan atanacak diye sorub işareti koyduk(statement sınıfı)
                  /*  Daha sonra, sqlString değişkenine INSERT INTO arts(artname,artistname,year,image) VALUES
                  (?,?,?,?) ifadesi atanır. Bu ifade, arts tablosuna artname, artistname, year ve image
                   alanlarına sırasıyla değerler ekleyeceğimizi belirtir. Değişkenlerin değerleri daha
                   sonra atanacak olduğu için soru işaretleri kullanılır.*/
                  val sqlString ="INSERT INTO arts(artname,artistname,year,image)VALUES (?,?,?,?)"
                  // değişkenleri index değerleriyle bağlayacağız
                  /* Sonraki satırda, database.compileStatement(sqlString) ifadesi kullanılarak
                   sqlString ifadesi derlenir ve SQLiteDatabase nesnesine bağlı bir SQLiteStatement
                   nesnesi oluşturulur. Bu SQLiteStatement nesnesi, daha sonra parametre
                    bağlama işlemleri için kullanılacaktır.*/
                  val statement =database.compileStatement(sqlString)
                  statement.bindString(1,artName)
                  statement.bindString(2,artistName)
                  statement.bindString(3,year)
                  statement.bindBlob(4,byteArray)
                  /*  statement.execute() ifadesi çağrılarak SQL
                  ifadesi çalıştırılır ve veritabanına yeni bir satır eklenir.*/
                  statement.execute()

                  println(artName+artistName)


              } catch (e:Exception){
                  e.printStackTrace()
              }
              // verileri veritabanına kaydettikten sonra maine geri dönmek için;
                val  intent=Intent(this@ArtActivity,MainActivity::class.java)
              // bundan önce açık olan aktiviteleri kapatmak için;
              /* FLAG_ACTIVITY_CLEAR_TOP bayrağı, sadece aktivite yığınındaki
               araaktiviteleri temizler ve hedeflenen aktiviteye gider.
                Kullanıcı geri tuşunu kullanarak araaktivitelerin üzerine
                geri dönebilir ve bu durumda uygulama yeniden o
                araaktiviteleri başlatabilir.*/
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
              startActivity(intent)

          }

    }
    // alacağımız resim verisini giricez ardından bir bitmap ögesi döndüreceğiz

     fun makeSmallerBitmap (image:Bitmap,maximumSize:Int) :Bitmap{
        // resmin genişliğini ve yüksekliğini aldık
        var width=image.width
        var height=image.height
        // bir oran oluşturuyoruz
        var bitmapRadio:Double =width.toDouble()/height.toDouble()
         if(bitmapRadio>1) {
             // resim yataydır(landscape)
             width=maximumSize
             val scaleHeight=width/bitmapRadio
             height=scaleHeight.toInt()

         }else{
             // resim dikeydir(portrait)
             height=maximumSize
             val scalewidth=height*bitmapRadio
             width=scalewidth.toInt()
         }

        // resim boyutunu büyüt ya da küçült
        return Bitmap.createScaledBitmap(image,100,100,true)

    }

    fun selectImage(view: View) {
        //İlk olarak, cihazın SDK sürümü TIRAMISU veya daha yeni ise (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        // , izin kontrolleri yapılır:
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            //Bu kod, READ_MEDIA_IMAGES izninin uygulama tarafından verilip verilmediğini kontrol eder.
            // İzin verilmediyse, iznin daha önce reddedildiği veya yönlendirme
            // mesajının gösterilmesi gerekip gerekmediği kontrol edilir.
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                   //Eğer izin verilmediyse ve kullanıcıya izinle ilgili açıklama gösterilmesi gerekiyorsa,
                    // Snackbar kullanarak izin talebi ile ilgili bir mesaj gösterilir:
                    //Kullanıcı bu açıklama mesajının altındaki "Give Permission" (İzni Ver)
                    // düğmesine tıklarsa, permissionLauncher'ı kullanarak izin istemi başlatılır.
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener {
                          //Eğer izin daha önce reddedildiyse veya açıklama mesajı gösterilmesi gerekmeyen
                            // başka bir durum varsa, permissionLauncher kullanılarak izin istemi başlatılır:
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                        }).show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            } else {
                //Eğer izin verilmişse, galeriyi açmak için bir intent oluşturulur:
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
                //registerLauncher() fonksiyonu ise aktivite başlatma ve izin isteme işlemleri için
            // ActivityResultLauncher'ı kaydeder. Fonksiyonun içeriği şu şekildedir:
            }
        } else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Snackbar.make(view, "Permission needed for gallery", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",
                        View.OnClickListener {
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }).show()
                } else {
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            } else {
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }
        }


    }

    //activityResultLauncher tanımlanır ve registerForActivityResult yöntemi kullanılarak başlatma sonuçlarını yönetir:
    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    //Eğer sonuç başarılı ise (resultCode == RESULT_OK), seçilen resmin verisi alınır ve işlenir:
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        val imageData = intentFromResult.data
                        try {
                            if (Build.VERSION.SDK_INT >= 28) {
                                //eğer cihazın SDK sürümü 28 veya daha yeni ise (Build.VERSION.SDK_INT >= 28),
                                // ImageDecoder kullanılarak resim verisi çözümlenir ve küçük resim oluşturulur:
                                val source = ImageDecoder.createSource(
                                    this@ArtActivity.contentResolver,
                                    imageData!!
                                )
                                selectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView.setImageBitmap(selectedBitmap)
                            } else {
                                //Eğer cihazın SDK sürümü 28 den küçük ise, MediaStore kullanılarak resim verisi
                                // çözümlenir ve küçük resim oluşturulur:
                                selectedBitmap = MediaStore.Images.Media.getBitmap(
                                    this@ArtActivity.contentResolver,
                                    imageData
                                )
                                binding.imageView.setImageBitmap(selectedBitmap)
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

        //permissionLauncher tanımlanır ve registerForActivityResult yöntemi kullanılarak izin talepleri yönetilir:
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted ,Eğer izin verildiyse, galeriyi açmak için bir intent oluşturulur:
                val intentToGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            } else {
                //permission denied,Eğer izin reddedildiyse, bir Toast mesajı gösterilir:
                Toast.makeText(this@ArtActivity, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
            //Bu şekilde selectImage() ve registerLauncher() fonksiyonları birbiriyle ilişkili olarak çalışır ve
            // kullanıcının galeriden bir görüntü seçmesini ve izinlerin yönetilmesini sağlar.
        }
    }
    }

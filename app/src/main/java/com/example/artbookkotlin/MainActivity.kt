package com.example.artbookkotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artbookkotlin.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var artList : ArrayList<Art>
    private lateinit var artAdapter : ArtAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        artList = ArrayList<Art>()
        artAdapter = ArtAdapter(artList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = artAdapter

        try {

            val database = this.openOrCreateDatabase("arts", Context.MODE_PRIVATE,null)

            val cursor = database.rawQuery("SELECT * FROM arts",null)
            val artNameIx = cursor.getColumnIndex("artname")

            val idIx = cursor.getColumnIndex("id")

            while (cursor.moveToNext()) {
                val name = cursor.getString(artNameIx)

                val id = cursor.getInt(idIx)
                val art = Art(name,id)
                artList.add(art)
            }

            artAdapter.notifyDataSetChanged()

            cursor.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        /*Inflater
        onCreateOptionsMenu metodu, aktivitenin menüsünü oluşturur.
         İlk olarak, menuInflater nesnesi oluşturulur ve menuInflater.
         inflate(R.menu.art_menu, menu) şeklinde kullanılarak
         art_menu.xml adlı bir menü dosyası şişirilir. Bu,
         R.menu.art_menu kaynağını kullanarak res/values/menu
         klasöründe bulunan art_menu.xml dosyasını şişirir.
        Ardından, üzerinde çalıştığınız aktivitenin
        super.onCreateOptionsMenu(menu) metodunu çağırarak,
        menu objesini süper sınıfa iletir ve şekillendirme
        işlemini tamamlar.*/
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.art_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    /*nOptionsItemSelected metodu, kullanıcının menü öğelerinden birini
     seçtiğinde tetiklenir. Burada, seçilen öğenin ID'sini kontrol ediyoruz.
     Eğer seçilen öğe ID'si R.id.add_art_item ile eşleşiyorsa
     (bu durumda art_menu.xml dosyasında tanımlanan bir öğe),
     yeni bir aktiviteye geçiş yapmak için Intent kullanılır.
     Intent oluşturularak, ArtActivity sınıfına geçiş yapılır ve
     "info" adlı bir ekstra veri eklenir. Son olarak,
     startActivity(intent) ile bu yeni aktiviteyi başlatırız.
     */
        if (item.itemId == R.id.add_art_item) {
            val intent = Intent(this,ArtActivity::class.java)
            /*Bu kod parçasında, MainActivity sınıfından ArtActivity
            sınıfına geçen bir intent oluşturuluyor. putExtra metoduyla
             "info" adında bir ekstra veri ekleniyor ve değeri "new"
             olarak ayarlanıyor. Ardından, startActivity metoduyla
             ArtActivity başlatılıyor ve bu ekstra veri,
             ArtActivity tarafından alınabilir ve kullanılabilir
             hale gelir.
             */
            intent.putExtra("info","new")
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }


}
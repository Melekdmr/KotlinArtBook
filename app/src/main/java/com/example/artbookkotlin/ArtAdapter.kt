package com.example.artbookkotlin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.artbookkotlin.databinding.RecyclerRowBinding

/* ArtAdapter sınıfı, RecyclerView.Adapter sınıfından genişletilerek
bir özel adaptör sınıfı oluşturuyor. Bu adaptör, veri kümesini bir
 RecyclerView'e bağlamak için kullanılıyor. Art tipinden bir
 ArrayList olan artList parametresini alıyor.*/
class ArtAdapter(val artList : ArrayList<Art>) : RecyclerView.Adapter<ArtAdapter.ArtHolder>() {
    /* ArtHolder sınıfı, RecyclerView.ViewHolder sınıfından kalıtım
   yaparak bir iç sınıf oluşturuyor. Bu iç sınıf, RecyclerView'de
   görüntülenen her bir öğenin tutulduğu görünüm öğelerini temsil
   ediyor. ArtHolder sınıfı, RecyclerRowBinding öğesini (binding) içeriyor.
   ArtHolder sınıfının içindeki binding öğesi, RecyclerRowBinding tipindedir.
   Bu öğe, RecyclerView'deki her bir satırın bağlamını temsil eder.
   Örneğin, bu bağlama, satırda yer alan görüntüler, metinler
   veya diğer öğelerin referanslarını tutar.
   ArtHolder sınıfı, RecyclerView.ViewHolder(binding.root) ifadesi
   ile oluşturulan bir temel sınıf nesnesini genişletir. Bu ifade,
   binding.root öğesini ViewHolder'ın kök görünümü olarak belirler.
   Burada root, bağlama hiyerarşisinin en üst düzeyindeki
  görünüm öğesine karşılık gelir.*/
    class ArtHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    /* Bu fonksiyon, RecyclerView'nin görünüm öğelerini oluştururken
 kullanılan ViewHolder nesnesini oluşturur. RecyclerRowBinding.
 inflate(LayoutInflater.from(parent.context), parent, false)
 ifadesi, RecyclerRowBinding sınıfından bir örneği şişirerek
 (inflate ederek) görünüm öğelerini oluşturur.
LayoutInflater.from(parent.context) ifadesi, LayoutInflater
 sınıfından bir örneği alarak görünüm öğelerini şişirmek için
 kullanılır. parent ifadesi, şişirilen görünüm öğesinin ebeveyn
 görünümüne eklenmesini sağlar. false ifadesi ise, ebeveyn
 görünüme otomatik olarak eklenmemesini sağlar. Son olarak,
 ArtHolder(binding) ifadesi, oluşturulan binding öğesini
 kullanarak yeni bir ArtHolder nesnesi oluşturur ve döndürür.
Bu nesne, RecyclerView'deki her bir öğenin tutulduğu görünüm
öğelerini temsil eder.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtHolder(binding)
    }
   /*  Bu fonksiyon, RecyclerView'nin her bir öğesi için gerçekleştirilen
     veri bağlama işlemini tanımlar. holder parametresi, öğenin tutulduğu
     ArtHolder nesnesini temsil eder. position parametresi, bağlanacak
     öğenin konumunu belirtir. Kod bloğunda, holder.binding.recyclerViewTextView.text
     ifadesi, öğenin görünüm öğesindeki recyclerViewTextView adlı bir
     TextView'in text özelliğine, artList listesindeki ilgili öğenin ismini
     atar. Bağlı görünüm öğesine tıklanıldığında ise bir Intent oluşturulur
     ve ArtActivity sınıfına geçiş yapılır. intent.putExtra("info", "old")
     ifadesi, "info" adında bir ekstra bilgiyi "old" değeriyle Intent'e ekler.
     Ayrıca, intent.putExtra("id", artList[position].id) ifadesi, seçilen
     öğenin benzersiz kimliğini Intent'e ekler. En sonda ise
     holder.itemView.context.startActivity(intent) ifadesiyle Intent
     başlatılır ve geçiş yapılır. */
    override fun onBindViewHolder(holder: ArtHolder, position: Int) {
        holder.binding.recyclerViewTextView.text = artList.get(position).name
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,ArtActivity::class.java)
            intent.putExtra("info","old")
            intent.putExtra("id",artList[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return artList.size
    }

}
/*  ViewHolder: ViewHolder, RecyclerView içindeki her bir öğeyi temsil eden
 ve görünüm öğelerini tutan bir sınıftır. ArtHolder sınıfı, bu örnekte
 kullanılan bir ViewHolder örneğidir. ViewHolder, öğelerin görünümlerine
 erişmek ve öğelere veri bağlamak için kullanılır.

ViewBinder: RecyclerRowBinding sınıfı, veri bağlamak için kullanılan bir
 ViewBinding sınıfının örneğidir. ViewBinding, bir XML dosyasındaki görünüm
 öğelerini bağlamak için kullanılan bir yöntemdir. RecyclerRowBinding,
 "recycler_row.xml" adlı bir görünüm öğesi dosyasını bağlar ve bu dosyadaki
 görünüm öğelerine erişmek için kullanılır.

RecyclerView: RecyclerView, Android SDK'nın bir parçasıdır ve karmaşık listeler
 veya tablolardan oluşan verileri düzgün şekilde göstermek için kullanılır.
 RecyclerView, esnek ve optimize edilmiş bir kullanıcı arabirimi oluşturmak
 için kullanılan bir widget'tır. Öğelerin yeniden kullanılması ve yalnızca
 görünür öğelerin oluşturulmasıyla performans avantajı sağlar.

Adapter: Adapter, RecyclerView ile veri kaynağı arasında bir bağlantı görevini
üstlenen bir sınıftır. Veri kaynağından verileri alır ve her öğe için bir
 ViewHolder oluşturur. Ayrıca, öğelerin görünümlerine verileri bağlar.
 RecyclerView.Adapter sınıfını türeterek özelleştirilmiş bir Adapter
 sınıfı oluşturmak mümkündür. Bu sınıf, onCreateViewHolder, onBindViewHolder
 ve getItemCount gibi metodları içerir */

/*   Buradaki "itemView", RecyclerView'da her bir öğeyi temsil eden
 öğenin kök görünüm elemanını temsil ediyor. Her bir öğe, "ArtHolder"
 olarak adlandırılan bir öğe tutucusu içerisinde saklanır ve bu öğe
 tutucusu, her bir öğenin görünüm öğelerine erişimi sağlar.

Dolayısıyla, "holder.itemView" ifadesi, RecyclerView'da belirli bir
konumdaki öğenin kök görünüm elemanını temsil eder. Bu öğenin üzerine
tıklanıldığında gerçekleşen olayları tanımlamak veya işlemek için
kullanılır. Yukarıdaki örnekte, setOnClickListener yöntemi kullanılarak
 tıklama olayı dinlenir ve bir intent oluşturularak ArtActivity'ye geçiş
 yapılır.*/
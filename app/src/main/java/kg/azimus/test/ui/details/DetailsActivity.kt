package kg.azimus.test.ui.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kg.azimus.test.databinding.ActivityDetailsBinding
import kg.azimus.test.model.Goods
import kotlinx.android.synthetic.main.activity_details.*


class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    companion object {
        fun start(context: Context, goods: Goods) {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("name", goods.name)
            intent.putExtra("price", goods.price)
            intent.putExtra("id", goods.id)
            intent.putExtra("desc", goods.desc)
            intent.putExtra("category", goods.category)
            intent.putExtra("company", goods.company)
            intent.putExtra("img", goods.img)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showDetails()
    }

    @SuppressLint("SetTextI18n")
    private fun showDetails() {
        Picasso.get().load(Uri.parse(intent.getStringExtra("img"))).into(binding.detailImg)
        detail_name.text = intent.getStringExtra("name")
        detail_desc.text = intent.getStringExtra("desc")
        detail_categ.text = "Category " + intent.getStringExtra("category")
        detail_price.text = "Price " + intent.getIntExtra("price", 0)
        detail_comp.text = "Company " + intent.getStringExtra("company")
    }
}

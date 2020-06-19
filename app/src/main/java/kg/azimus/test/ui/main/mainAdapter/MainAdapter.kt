package kg.azimus.test.ui.main.mainAdapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kg.azimus.test.databinding.ItemRecyclerBinding
import kg.azimus.test.model.Goods

class MainAdapter(itemListener: OnItemClickDetails) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var _binding: ItemRecyclerBinding? = null
    private var dataList: ArrayList<Goods> = ArrayList()
    private  var mListener: OnItemClickDetails = itemListener

    fun setList(list: List<Goods> = ArrayList()) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        _binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(_binding!!, mListener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    interface OnItemClickDetails {
        fun onItemClick(position: Int)
    }

    inner class MainViewHolder(
        private val binding: ItemRecyclerBinding,
        private val clickDetails: OnItemClickDetails
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(goods: Goods) {
            Picasso.get().load(goods.img).into(binding.image)
            binding.name.text = goods.price.toString()
            binding.description.text = goods.category
        }

        init {
            itemView.setOnClickListener {
                clickDetails.onItemClick(adapterPosition)
            }
        }

    }
}
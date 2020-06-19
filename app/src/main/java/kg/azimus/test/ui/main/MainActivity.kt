package kg.azimus.test.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kg.azimus.test.R
import kg.azimus.test.databinding.ActivityMainBinding
import kg.azimus.test.model.Goods
import kg.azimus.test.ui.details.DetailsActivity
import kg.azimus.test.ui.main.mainAdapter.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_alert.view.*
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MainAdapter.OnItemClickDetails {
    private lateinit var _binding: ActivityMainBinding
    private lateinit var mainAdapter: MainAdapter
    private val viewItems: ArrayList<Goods> = ArrayList()
    lateinit var categ: String
    lateinit var spinnerAdapter: SpinnerAdapter

    private  var tempArrayList = ArrayList<Goods>()
    private  var categList = ArrayList<Goods>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        initRecycler()
        readItemsFromJson()
    }

    private fun initRecycler() {
        mainAdapter = MainAdapter(this@MainActivity)
        _binding.mainRecycler.adapter = mainAdapter
        _binding.mainRecycler.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun readItemsFromJson() {
        try {
            val jsonDataString: String = readJSONDataFromFile()
            val jsonArray = JSONArray(jsonDataString)
            for (i in 0 until jsonArray.length()) {
                val itemObj = jsonArray.getJSONObject(i)
                val name = itemObj.getString("name")
                val price = itemObj.getInt("price")
                val desc = itemObj.getString("desc")
                val company = itemObj.getString("company")
                val category = itemObj.getString("category")
                val id = itemObj.getString("id")
                val img = itemObj.getString("img")
                val holidays = Goods(name, price, desc, company, category, id, img)
                viewItems.add(holidays)


                val timer = object : CountDownTimer(1000, 500) {
                    override fun onTick(millisUntilFinished: Long) {
                        showVisibility(true)
                    }

                    override fun onFinish() {
                        showVisibility(false)
                        mainAdapter.setList(viewItems)
                    }
                }
                timer.start()
            }
        } catch (e: JSONException) {
            Log.d("mainActivity", "addItemsFromJSON: ", e)
        } catch (e: IOException) {
            Log.d("mainActivity", "addItemsFromJSON: ", e)
        }
    }

    private fun readJSONDataFromFile(): String {
        var inputStream: InputStream? = null
        val builder = StringBuilder()
        try {
            var jsonString: String? = null
            inputStream = resources.openRawResource(R.raw.data_list)
            val bufferedReader = BufferedReader(
                InputStreamReader(inputStream, "UTF-8")
            )
            while (bufferedReader.readLine().also { jsonString = it } != null) {
                builder.append(jsonString)
            }
        } finally {
            inputStream?.close()
        }
        return String(builder)
    }

    private fun showVisibility(isShown: Boolean) {
        if (isShown) {
            progress_bar.visibility = View.VISIBLE
            main_recycler.visibility = View.GONE
            loading.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
            main_recycler.visibility = View.VISIBLE
            loading.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filters -> openFilter()
        }
        return true
    }

    private fun openFilter() {
        val listOfCategory = arrayOf("cat1", "cat2", "cat3", "cat4", "cat5")
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.item_alert, null)
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setIcon(R.drawable.ic_launcher_background)
        alertDialog.setView(mDialogView)
        alertDialog.setTitle("Фильтр")
        val dialog: AlertDialog = alertDialog.create()

        spinnerAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listOfCategory)
        mDialogView.spinner.adapter = spinnerAdapter

        mDialogView.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categ = mDialogView.spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        mDialogView.search_button.setOnClickListener {

            var objMin = mDialogView.input_from.text.toString().trim()
            var objMax = mDialogView.input_to.text.toString().trim()

            if (objMin == "" && objMax == "") {
                objMin = 0.toString()
                objMax = 0.toString()
            }

            val finalValue1: Int = objMin.toInt()
            val finalValue2: Int = objMax.toInt()
            filteredByPrice(finalValue1, finalValue2)
            dialog.cancel()

        }
        dialog.show()
    }

    private fun filteredByPrice(priceFrom: Int, priceTo: Int) {
        tempArrayList = ArrayList<Goods>()
        categList = ArrayList<Goods>()

        for (data in viewItems) {
            if (data.category == categ) {
                tempArrayList.add(data)
            }
        }

        for (i in 0 until tempArrayList.size) {
            val price: Int = tempArrayList[i].price
            if (price in priceFrom..priceTo) {
                categList.add(tempArrayList[i])
            }
        }

        if (categList.isNullOrEmpty()) {
            mainAdapter.setList(tempArrayList)
        } else {
            mainAdapter.setList(categList)
        }

        viewItems.addAll(tempArrayList)
        viewItems.addAll(categList)

    }

    override fun onItemClick(position: Int) {
        DetailsActivity.start(this, viewItems[position])
    }
}

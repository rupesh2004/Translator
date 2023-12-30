package com.example.translator

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {

    lateinit var fromLanguage: Spinner
    lateinit var toLanguage: Spinner
    lateinit var translateText: EditText
    lateinit var translatedText1: TextView
    private var items = arrayOf("English", "Hindi", "Bengali", "Marathi", "Urdu", "Chinese")
    private var conditions = DownloadConditions.Builder()
        .requireWifi()
        .build()

    private var selectedFromLanguage: String = ""
    private var selectedToLanguage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        translateText = findViewById(R.id.textToTranslate)
        fromLanguage = findViewById(R.id.fromLanguages)
        toLanguage = findViewById(R.id.toLanguages)
        translatedText1 = findViewById(R.id.translatedText)

        val itemsAdapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, items
        )

        fromLanguage.adapter = itemsAdapter
        toLanguage.adapter = itemsAdapter

        fromLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedFromLanguage = items[position].toLowerCase()

                Toast.makeText(this@MainActivity, "Selected From Language: ${items[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        toLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedToLanguage = items[position].toLowerCase()

                Toast.makeText(this@MainActivity, "Selected To Language: ${items[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    fun translate_language(view: View) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(selectFrom())
            .setTargetLanguage(selectTo())
            .build()
        val englishGermanTranslator = Translation.getClient(options)

        englishGermanTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {

                englishGermanTranslator.translate(translateText.text.toString())
                    .addOnSuccessListener { translatedText ->

                        translatedText1.text = translatedText

                    }
                    .addOnFailureListener { exception ->

                        Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()

                    }
            }
    }

    private fun selectFrom(): String {
        return when (selectedFromLanguage) {
            "" -> TranslateLanguage.ENGLISH
            "english" -> TranslateLanguage.ENGLISH
            "hindi" -> TranslateLanguage.HINDI
            "bengali" -> TranslateLanguage.BENGALI
            "chinese" -> TranslateLanguage.CHINESE
            "urdu" -> TranslateLanguage.URDU
            "marathi" -> TranslateLanguage.MARATHI
            else -> TranslateLanguage.ENGLISH
        }
    }

    private fun selectTo(): String {
        return when (selectedToLanguage) {
            "" -> TranslateLanguage.HINDI
            "english" -> TranslateLanguage.ENGLISH
            "hindi" -> TranslateLanguage.HINDI
            "arabic" -> TranslateLanguage.ARABIC
            "bengali" -> TranslateLanguage.BENGALI
            "urdu" -> TranslateLanguage.URDU
            "marathi" -> TranslateLanguage.MARATHI
            else -> TranslateLanguage.HINDI
        }
    }
}

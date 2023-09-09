package com.example.cheatsheet.util

import android.app.Activity
import android.content.Context
import android.content.res.AssetManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.cheatsheet.model.AnswerItem
import com.example.cheatsheet.model.QuestionElement
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception


fun copyFile(activity: Activity, file: File){
    var assetManager: AssetManager = activity.applicationContext.assets
    var inputStream = assetManager.open("file1.xlsx")

    // Создаем новый файл во внутренней директории и копируем данные
    val outputStream = FileOutputStream(file)
    val buffer = ByteArray(1024)
    var read: Int

    while (inputStream.read(buffer).also { read = it } != -1) {
        outputStream.write(buffer, 0, read)
    }

    // Закрываем потоки
    inputStream.close()
    outputStream.close()
}

fun getDataFromExcel(activity: Activity): List<QuestionElement>{
    val folder = activity.getDir("projectFolder", Context.MODE_PRIVATE)
    val filePath = File(folder, "file1.xlsx")
    if (!filePath.exists()) {
        copyFile(activity, filePath)
    }

    val inputStream= FileInputStream(filePath)

    /*var assetManager: AssetManager = activity.applicationContext.assets
    var inputStream = assetManager.open("file1.xlsx")*/
    //Здесь будет храниться наш итог
    val resultList: MutableList<QuestionElement> = mutableListOf()

    try{
        val workbook = XSSFWorkbook(inputStream)

        val sheet = workbook.getSheetAt(0)

        var i = 0



        var question: String? = null

        var answersList: MutableList<AnswerItem> = mutableListOf()

        var number: Int = 1


        while (i != sheet.physicalNumberOfRows){
            val row = sheet.getRow(i)
            val cell = row.getCell(0)
            if (cell != null) {
                val cellStyle = cell.cellStyle
                val fillForegroundColor = cellStyle.fillForegroundColorColor

                if (fillForegroundColor is XSSFColor) {
                    val argb = fillForegroundColor.argbHex

                    //Данный цвет означает начало нового вопроса
                    if (argb == "FF92D050"){
                        //Записать предыдущий вопрос в список если таковой есть
                        question?.let{q->
                            resultList.add(
                                QuestionElement(
                                    number,
                                    q,
                                    answersList.toList()
                                )
                            )
                            number += 1
                            question=null
                            answersList = mutableListOf()
                        }
                        i += 1
                        val nextCell = sheet.getRow(i).getCell(0)
                        if (nextCell != null) {
                            if (nextCell.cellType == CellType.STRING) {
                                //содержание вопроса
                                question = nextCell.stringCellValue
                            }
                        }
                    }

                    if (argb == "FF00B0F0"){
                        if (cell.cellType == CellType.STRING) {
                            //содержимое правильного ответа
                            answersList.add(
                                AnswerItem(
                                    cell.stringCellValue,
                                    true
                                )
                            )
                        }
                    }
                } else {
                    if (cell.cellType == CellType.STRING) {
                        //содержимое неправильного ответа
                        answersList.add(
                            AnswerItem(
                                cell.stringCellValue,
                                false
                            )
                        )
                    }
                }
            }
            i += 1
        }

        question?.let{ q->
            resultList.add(
                QuestionElement(
                    number,
                    q,
                    answersList.toList()
                )
            )
        }
    }catch (e:Exception){
        Log.e("AAA","${e.message}")
    }

    return resultList

}

fun loadExcelFile(file: Uri?, activity: Activity){

    file?.let { uri ->
        activity.contentResolver.openInputStream(uri)?.let { inputStream ->
            val byteArrayOutputStream = ByteArrayOutputStream()
            val bufferSize = 1024
            val buffer = ByteArray(bufferSize)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                byteArrayOutputStream.write(buffer, 0, len)
            }
            val fileBytes = byteArrayOutputStream.toByteArray()

            val folder = activity.getDir("projectFolder", Context.MODE_PRIVATE)

            // Определите путь к файлу, который вы хотите заменить
            val oldFile = File(folder, "file1.xlsx")

            // Удалите старый файл, если он существует
            if (oldFile.exists()) {
                oldFile.delete()
            }

            // Создайте новый файл с тем же именем и запишите в него содержимое байтового массива
            val newFile = File(folder, "file1.xlsx")
            val fileOutputStream = FileOutputStream(newFile)
            fileOutputStream.write(fileBytes)
            fileOutputStream.close()
        }
    }
}
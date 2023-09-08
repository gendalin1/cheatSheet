package com.example.cheatsheet.util

import android.app.Activity
import android.content.res.AssetManager
import android.util.Log
import com.example.cheatsheet.model.AnswerItem
import com.example.cheatsheet.model.QuestionElement
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception

fun getDataFromExcel(activity: Activity): List<QuestionElement>{
    var assetManager: AssetManager = activity.applicationContext.assets
    var inputStream = assetManager.open("file1.xlsx")
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
    }catch (e:Exception){
        Log.e("AAA","${e.message}")
    }

    return resultList


}